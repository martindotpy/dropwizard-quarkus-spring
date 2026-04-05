package dev.martindotpy.dropwizardquarkusspring.spring.core.adapter.configuration;

import java.util.concurrent.atomic.AtomicLong;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class StartupReadyTracker {
    private final AtomicLong startupReadyMs = new AtomicLong(-1L);

    @EventListener(ApplicationReadyEvent.class)
    void onApplicationReady(ApplicationReadyEvent event) {
        if (event.getTimeTaken() == null) {
            return;
        }

        startupReadyMs.compareAndSet(-1L, event.getTimeTaken().toMillis());
    }

    public long startupReadyMs() {
        long value = startupReadyMs.get();
        return value >= 0 ? value : 0L;
    }
}
