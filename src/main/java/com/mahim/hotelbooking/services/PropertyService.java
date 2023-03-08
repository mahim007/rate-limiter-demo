package com.mahim.hotelbooking.services;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource("ratelimiter.properties")
@Getter
public class PropertyService {
    @Value("${app.ratelimiter.paths}")
    private String[] paths;

    @Value("${app.ratelimiter.threshholds}")
    private String[] threshholds;

    @Value("${app.ratelimiter.timeranges}")
    private String[] timeranges;

    public int[] getThreshholds() {
        int[] arr = new int[this.threshholds.length];
        for (int i = 0; i < this.threshholds.length; i++) {
            try {
                arr[i] = Integer.parseInt(this.threshholds[i]);
            } catch (NumberFormatException ex) {
                arr[i] = 50;
            }
        }

        return arr;
    }

    public int[] getTimeranges() {
        int[] arr = new int[this.timeranges.length];
        for (int i = 0; i < this.timeranges.length; i++) {
            try {
                arr[i] = Integer.parseInt(this.timeranges[i]);
            } catch (NumberFormatException ex) {
                arr[i] = 10;
            }
        }

        return arr;
    }

}
