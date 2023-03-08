package com.mahim.hotelbooking.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "hotels")
@Getter @Setter @NoArgsConstructor
public class Hotel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String city;
    private String room;
    private Long price;

    public Hotel(String city, String room, Long price) {
        this.city = city;
        this.room = room;
        this.price = price;
    }
}
