package com.es.phoneshop.model.security;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayDeque;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultDosProtectionService implements DosProtectionService {
    private static final long THRESHOLD = 20;
    private final ArrayDeque<LocalDateTime> times = new ArrayDeque<>();
    private final Map<String, Long> countMap = new ConcurrentHashMap();

    private static class SingletonHelper {
        private static final DefaultDosProtectionService INSTANCE =
                new DefaultDosProtectionService();
    }

    public static DefaultDosProtectionService getInstance() {
        return DefaultDosProtectionService.SingletonHelper.INSTANCE;
    }

    @Override
    public boolean isAllowed(String ip) {
        Long count = countMap.get(ip);
        if (count == null) {
            count = 1L;
        } else {
            if (count > THRESHOLD) {
                if (ChronoUnit.MINUTES.between(times.peekFirst(), times.peekLast()) < 1) {
                    return false;
                } else {
                    times.removeFirst();
                }
            }
            times.addLast(LocalDateTime.now());
            count++;
        }
        countMap.put(ip, count);
        return true;
    }
}
