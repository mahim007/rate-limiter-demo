package com.mahim.hotelbooking.services;

import com.mahim.hotelbooking.models.Hotel;
import com.mahim.hotelbooking.repositories.HotelRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class HotelService {
    private HotelRepository hotelRepository;

    public HotelService(HotelRepository hotelRepository) {
        this.hotelRepository = hotelRepository;
    }

    public List<Hotel> getHotelsByCity(String cityName) throws InterruptedException {
        List<Hotel> hotels = hotelRepository.findAllByCity(cityName);
//        Thread.sleep(1000);
        return hotels;
    }

    public List<Hotel> getHotelsByRoomType(String roomType) throws InterruptedException {
        List<Hotel> hotels = hotelRepository.findAllByRoom(roomType);
//        Thread.sleep(1000);
        return hotels;
    }
}
