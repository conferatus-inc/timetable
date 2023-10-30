package org.conferatus.timetable.backend.control;

import io.micrometer.core.instrument.MeterRegistry;
import org.conferatus.timetable.backend.exception.ServerException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.LongAdder;

@RestController
@RequestMapping("/api/1.0")
public class PingController extends BaseController {
    private final MeterRegistry meterRegistry;
    private final LongAdder counter;

    public PingController(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
        counter = new LongAdder();
        meterRegistry.gauge("noteCounter", counter);
    }

    @GetMapping("/ping")
    public CompletableFuture<ResponseEntity<?>> ping() {
        return CompletableFuture.supplyAsync(() -> {
            counter.increment();
            if (counter.longValue() == 2) {
                ServerException.throwException(HttpStatus.NOT_FOUND);
            }
            return counter.longValue();
        }).thenApplyAsync(ResponseEntity::ok);

    }
}
