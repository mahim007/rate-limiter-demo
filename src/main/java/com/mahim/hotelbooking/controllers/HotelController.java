package com.mahim.hotelbooking.controllers;

import com.mahim.hotelbooking.models.Hotel;
import com.mahim.hotelbooking.services.HotelService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class HotelController {
    private final HotelService hotelService;

    public HotelController(HotelService hotelService) {
        this.hotelService = hotelService;
    }

    @GetMapping("/city/{cityName}")
    public List<Hotel> getHotelsByCity(@PathVariable String cityName) throws InterruptedException {
        return hotelService.getHotelsByCity(cityName);
    }

    @GetMapping("/city/{cityName}/{order}")
    public List<Hotel> getOrderedHotelsByCity(@PathVariable String cityName, @PathVariable String order) throws InterruptedException {
        if (order.equals("asc")) {
            return hotelService.getHotelsByCity(cityName)
                    .stream()
                    .sorted((Hotel h1, Hotel h2) -> (int)( h1.getPrice() - h2.getPrice()))
                    .collect(Collectors.toList());
        } else {
            return hotelService.getHotelsByCity(cityName)
                    .stream()
                    .sorted((Hotel h1, Hotel h2) -> (int)( h2.getPrice() - h1.getPrice()))
                    .collect(Collectors.toList());
        }
    }

    @GetMapping("/room/{roomType}")
    public List<Hotel> getHotelsByRoomType(@PathVariable String roomType) throws InterruptedException {
        return hotelService.getHotelsByRoomType(roomType);
    }

    @GetMapping("/room/{roomType}/{order}")
    public List<Hotel> getOrderedHotelsByRoomType(@PathVariable String roomType, @PathVariable String order) throws InterruptedException {
        if (order.equals("asc")) {
            return hotelService.getHotelsByRoomType(roomType)
                    .stream()
                    .sorted((Hotel h1, Hotel h2) -> (int)( h1.getPrice() - h2.getPrice()))
                    .collect(Collectors.toList());
        } else {
            return hotelService.getHotelsByRoomType(roomType)
                    .stream()
                    .sorted((Hotel h1, Hotel h2) -> (int)( h2.getPrice() - h1.getPrice()))
                    .collect(Collectors.toList());
        }
    }

    @GetMapping({"", "/"})
    public String getHomePage() {
        return "Rate Limited Hotel Booking Service";
    }
}
