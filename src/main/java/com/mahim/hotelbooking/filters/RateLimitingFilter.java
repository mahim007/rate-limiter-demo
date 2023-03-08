package com.mahim.hotelbooking.filters;

import com.mahim.hotelbooking.services.PropertyService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
@Order(1)
public class RateLimitingFilter extends OncePerRequestFilter {
    protected final Logger LOGGER = LoggerFactory.getLogger(RateLimitingFilter.class);

    private record RateLimiterRecord(String path, int threshold, int timeline, long start, long end, boolean open) {};
    private Map<String, Queue<HttpServletRequest>> limiterQueue;
    private Map<String, RateLimiterRecord> limiterMap;
    private Map<String, AtomicInteger> counterMap;

    private final PropertyService propertyService;

    public RateLimitingFilter(PropertyService propertyService) {
        this.counterMap = new HashMap<>();
        this.propertyService = propertyService;
        this.limiterQueue = new HashMap<>();
        this.limiterMap = new HashMap<>();

        this.limiterMap = IntStream
                .range(0, propertyService.getPaths().length)
                .mapToObj(i -> new RateLimiterRecord(
                        this.propertyService.getPaths()[i],
                        this.propertyService.getThreshholds()[i],
                        this.propertyService.getTimeranges()[i],
                        System.currentTimeMillis(),
                        System.currentTimeMillis() + this.propertyService.getTimeranges()[i] * 1000L,
                        true
                        )
                )
                .collect(Collectors.toMap(RateLimiterRecord::path, Function.identity()));

        this.limiterQueue = limiterMap.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> new LinkedList<HttpServletRequest>()
                ));

        this.counterMap = limiterMap.entrySet().stream()
                        .collect(Collectors.toMap(
                                Map.Entry::getKey,
                                e -> new AtomicInteger(0)
                        ));
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String[] split = request.getRequestURI().split("/");
        String path = split.length > 1 ? "/" + split[1] : "";

        if (limiterMap.containsKey(path)) { // if the request path matches one of the rate-limited path
            RateLimiterRecord rateLimiterRecord = limiterMap.get(path);
            Queue<HttpServletRequest> queue = limiterQueue.get(path);
            AtomicInteger counter = counterMap.get(path);

            long time = System.currentTimeMillis();
            if (time >= rateLimiterRecord.start() && time <= rateLimiterRecord.end()) { // request time is within the time window
                if (rateLimiterRecord.open()) { // the request end point is open
                    if (counter.get() < rateLimiterRecord.threshold() &&  queue.size() < rateLimiterRecord.threshold()) {
                        /** counter indicates the no of request go through within the current time frame.
                         queue will keep track the no of requests that are still under processing within the current time frame
                         or may be from the previous window **/
                        counter.set(counter.get() + 1);
                        LOGGER.info("number of request allowed for path: {} is {}", path, counter.get());
                        proceed(path, rateLimiterRecord, time, queue, request, filterChain, response);
                    } else if (counter.get() >= rateLimiterRecord.threshold()){
                        // disable the end point for next 5 seconds, discard every request
                        updateLimiterMap(path, rateLimiterRecord, time, 5, false);
                        response.setStatus(503);

                        LOGGER.error("API disabled for path: {}, threshold: {} timerange:{} start:{} time:{} end:{} open:{}",
                                path,
                                rateLimiterRecord.threshold(),
                                rateLimiterRecord.timeline(),
                                time,
                                time,
                                time + 5 * 1000L,
                                false
                        );
                    } else {
                        /** this block will encounter when the no of newly coming request is still less than threshold
                         but there are still some in progress request from previous time window. in that case just
                         ignore the current request to cool down. **/
                        response.setStatus(429);
                        LOGGER.info("Request ignored for path: {}  no_of_req:{} no_of_req_processing:{}", path, counter.get(), queue.size());
                    }
                } else {
                    /** The processing pipeline for that specific path is closed temporarily, thus returning a
                     503 - Temporary Service Unavailable response returned **/
                    response.setStatus(503);
                }
            } else if (time > rateLimiterRecord.end()) {
                LOGGER.info("New timeframe created, path: {}, threshold: {} timerange:{} start:{} time:{} end:{} open:{}",
                        path,
                        rateLimiterRecord.threshold(),
                        rateLimiterRecord.timeline(),
                        time,
                        time,
                        time + rateLimiterRecord.timeline() * 1000L,
                        true
                );

                // new timeframe created with open end point
                counter.set(1);
                LOGGER.info("number of request allowed for path: {} is {}", path, counter.get());
                updateLimiterMap(path, rateLimiterRecord, time, rateLimiterRecord.timeline(), true);

                proceed(path, limiterMap.get(path), time, queue, request, filterChain, response);
            }

        } else {
            // the requested path is not rate-limited. let it execute
            LOGGER.info("Not rate limited path: {}", request.getRequestURI());
            filterChain.doFilter(request, response);
        }
    }

    private void proceed(String path, RateLimiterRecord rateLimiterRecord, long time, Queue<HttpServletRequest> queue,
                         HttpServletRequest request, FilterChain filterChain, HttpServletResponse response) throws ServletException, IOException {
        LOGGER.info("Processing started for path: {}, threshold: {} timerange:{} start:{} time:{} end:{} open:{}",
                path,
                rateLimiterRecord.threshold(),
                rateLimiterRecord.timeline(),
                rateLimiterRecord.start(),
                time,
                rateLimiterRecord.end(),
                rateLimiterRecord.open()
        );
        queue.add(request);
        filterChain.doFilter(request, response);
        LOGGER.info("Processing ended for path: {}, threshold: {} timerange:{} start:{} time:{} end:{} open:{}",
                path,
                rateLimiterRecord.threshold(),
                rateLimiterRecord.timeline(),
                rateLimiterRecord.start(),
                time,
                rateLimiterRecord.end(),
                rateLimiterRecord.open()
        );
        queue.remove();
    }

    private void updateLimiterMap(String path, RateLimiterRecord record, long time, int timeline, boolean open) {
        limiterMap.put(path, new RateLimiterRecord(
                record.path(),
                record.threshold(),
                record.timeline(),
                time,
                time + timeline * 1000L,
                open)
        );
    }
}
