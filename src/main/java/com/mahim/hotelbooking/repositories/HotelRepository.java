package com.mahim.hotelbooking.repositories;

import com.mahim.hotelbooking.models.Hotel;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface HotelRepository extends CrudRepository<Hotel, Integer> {
    public List<Hotel> findAllByCity(String cityName);

    public List<Hotel> findAllByRoom(String roomType);
}
