package com.hr.demo.website.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Configuration
@EnableCaching
public class WebsiteCacheAndRateLimitConfig {

    @Bean
    public CacheManager websiteCacheManager() {
        return new ConcurrentMapCacheManager(
            "websiteHomeData",
            "websiteServices",
            "websitePlans",
            "websiteJobs",
            "websiteBlogs",
            "superAdminCompanies"
        );
    }

    /**
     * O(1) time & bounded-space per-IP rate limiter for public website POST endpoints.
     * Prevents form/upload spam at scale without touching DB.
     */
    @Component
    @Order(Ordered.HIGHEST_PRECEDENCE + 10)
    public static class WebsiteRateLimitFilter extends OncePerRequestFilter {
        private static final int MAX_REQUESTS_PER_MINUTE = 20;
        private static final int MAX_TRACKED_IPS = 10_000;
        private static final long WINDOW_MS = 60_000L;

        private final Map<String, WindowCounter> buckets = new ConcurrentHashMap<>();

        @Override
        protected boolean shouldNotFilter(HttpServletRequest request) {
            return !("POST".equalsIgnoreCase(request.getMethod())
                && request.getRequestURI().startsWith("/api/public/website/"));
        }

        @Override
        protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
        ) throws ServletException, IOException {
            String ip = extractClientIp(request);
            long now = System.currentTimeMillis();

            if (buckets.size() > MAX_TRACKED_IPS) {
                buckets.entrySet().removeIf(e -> now - e.getValue().windowStart > WINDOW_MS);
            }

            WindowCounter counter = buckets.compute(ip, (k, existing) -> {
                if (existing == null || now - existing.windowStart > WINDOW_MS) {
                    return new WindowCounter(now);
                }
                return existing;
            });

            if (counter.count.incrementAndGet() > MAX_REQUESTS_PER_MINUTE) {
                response.setStatus(429);
                response.setContentType("application/json");
                response.getWriter().write("{\"success\":false,\"message\":\"Too many requests. Please try again in a minute.\"}");
                return;
            }

            filterChain.doFilter(request, response);
        }

        private String extractClientIp(HttpServletRequest request) {
            String xff = request.getHeader("X-Forwarded-For");
            if (xff != null && !xff.isBlank()) {
                return xff.split(",")[0].trim();
            }
            return request.getRemoteAddr();
        }

        private static final class WindowCounter {
            final long windowStart;
            final AtomicInteger count = new AtomicInteger(0);

            WindowCounter(long windowStart) {
                this.windowStart = windowStart;
            }
        }
    }
}
