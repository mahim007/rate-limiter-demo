package com.mahim.hotelbooking;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mahim.hotelbooking.models.Hotel;
import com.mahim.hotelbooking.services.PropertyService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@SpringBootTest(
		webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
		classes = HotelbookingApplication.class
)
@AutoConfigureMockMvc
class HotelbookingApplicationTests {

	@Autowired
	MockMvc mockMvc;

	@Autowired
	PropertyService propertyService;

	@Test
	void testRoom() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/room/Deluxe").accept(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(content().string(containsString("Deluxe")));
	}

	@Test
	void testRoomAsc() throws Exception {
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/room/Deluxe/asc")
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(content().string(containsString("Deluxe")))
				.andReturn();

		String contentAsString = mvcResult.getResponse().getContentAsString();
		ObjectMapper mapper = new ObjectMapper();
		List<Hotel> hotels = mapper.readValue(contentAsString, new TypeReference<List<Hotel>>() {
		});

		for (int i = 1; i < hotels.size(); i++) {
			Assertions.assertTrue(hotels.get(i - 1).getPrice() <= hotels.get(i).getPrice());
		}
	}

	@Test
	void testRoomDesc() throws Exception {
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/room/Deluxe/desc")
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(content().string(containsString("Deluxe")))
				.andReturn();

		String contentAsString = mvcResult.getResponse().getContentAsString();
		ObjectMapper mapper = new ObjectMapper();
		List<Hotel> hotels = mapper.readValue(contentAsString, new TypeReference<List<Hotel>>() {
		});

		for (int i = 1; i < hotels.size(); i++) {
			Assertions.assertTrue(hotels.get(i - 1).getPrice() >= hotels.get(i).getPrice());
		}
	}

	@Test
	void testRateLimiterForCity() throws Exception {
		String[] paths = propertyService.getPaths();
		int indexForCity = 0;
		for (int i = 0; i < paths.length; i++) {
			if (paths[i].startsWith("/city")) {
				indexForCity = i;
				break;
			}
		}

		for (int i = 1; i <= propertyService.getThreshholds()[indexForCity] ; i++) {
			mockMvc.perform(MockMvcRequestBuilders.get("/city/Bangkok/desc")
							.accept(MediaType.APPLICATION_JSON))
					.andExpect(MockMvcResultMatchers.status().isOk());
		}

		mockMvc.perform(MockMvcRequestBuilders.get("/city/Bangkok/asc")
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isServiceUnavailable());
	}

}
