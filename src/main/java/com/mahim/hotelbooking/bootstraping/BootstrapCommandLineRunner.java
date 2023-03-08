package com.mahim.hotelbooking.bootstraping;

import com.mahim.hotelbooking.models.Hotel;
import com.mahim.hotelbooking.repositories.HotelRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;


@Component
public class BootstrapCommandLineRunner implements CommandLineRunner {

    private final HotelRepository hotelRepository;

    public BootstrapCommandLineRunner(HotelRepository hotelRepository) {
        this.hotelRepository = hotelRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        hotelRepository.save(new Hotel("Bangkok", "Deluxe", 1000L));
        hotelRepository.save(new Hotel("Amsterdam", "Superior", 2000L));
        hotelRepository.save(new Hotel("Ashburn", "Sweet Suite", 1300L));
        hotelRepository.save(new Hotel("Amsterdam", "Deluxe", 2200L));
        hotelRepository.save(new Hotel("Ashburn", "Sweet Suite", 1200L));
        hotelRepository.save(new Hotel("Bangkok", "Superior", 2000L));
        hotelRepository.save(new Hotel("Ashburn", "Deluxe", 1600L));
        hotelRepository.save(new Hotel("Bangkok", "Superior", 2400L));
        hotelRepository.save(new Hotel("Amsterdam", "Sweet Suite", 30000L));
        hotelRepository.save(new Hotel("Ashburn", "Superior", 1100L));
        hotelRepository.save(new Hotel("Bangkok", "Deluxe", 60L));
        hotelRepository.save(new Hotel("Ashburn", "Deluxe", 1800L));
        hotelRepository.save(new Hotel("Amsterdam", "Superior", 1000L));
        hotelRepository.save(new Hotel("Bangkok", "Sweet Suite", 25000L));
        hotelRepository.save(new Hotel("Bangkok", "Deluxe", 900L));
        hotelRepository.save(new Hotel("Ashburn", "Superior", 800L));
        hotelRepository.save(new Hotel("Ashburn", "Deluxe", 2800L));
        hotelRepository.save(new Hotel("Bangkok", "Sweet Suite", 5300L));
        hotelRepository.save(new Hotel("Ashburn", "Superior", 1000L));
        hotelRepository.save(new Hotel("Ashburn", "Superior", 4444L));
        hotelRepository.save(new Hotel("Ashburn", "Deluxe", 7000L));
        hotelRepository.save(new Hotel("Ashburn", "Sweet Suite", 14000L));
        hotelRepository.save(new Hotel("Amsterdam", "Deluxe", 5000L));
        hotelRepository.save(new Hotel("Ashburn", "Superior", 1400L));
        hotelRepository.save(new Hotel("Ashburn", "Deluxe", 1900L));
        hotelRepository.save(new Hotel("Amsterdam", "Deluxe", 2300L));
    }
}
