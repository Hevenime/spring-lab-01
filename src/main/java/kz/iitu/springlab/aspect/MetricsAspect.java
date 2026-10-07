package kz.iitu.springlab.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

@Aspect
@Component
@Order(4)
public class MetricsAspect {

    private final Map<String, Long> counts = new ConcurrentHashMap<>();
    private final Map<String, Long> totalMs = new ConcurrentHashMap<>();

    @Around("@annotation(kz.iitu.springlab.aspect.Measured)")
    public Object measure(ProceedingJoinPoint pjp) throws Throwable {
        long start = System.currentTimeMillis();
        try {
            return pjp.proceed();
        } finally {
            long ms = System.currentTimeMillis() - start;
            String name = pjp.getSignature().getName();
            counts.merge(name, 1L, Long::sum);
            totalMs.merge(name, ms, Long::sum);
        }
    }

    public Map<String, String> summary() {
        Map<String, String> result = new TreeMap<>();
        for (String name : counts.keySet()) {
            long count = counts.get(name);
            long total = totalMs.get(name);
            result.put(name, "count=" + count + ", total=" + total + " ms, avg=" + (total / count) + " ms");
        }
        return result;
    }
}