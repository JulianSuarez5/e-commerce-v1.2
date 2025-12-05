package ppi.e_commerce.Service;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.github.vladimirbukhtoyarov.bucket4j.Bandwidth;
import com.github.vladimirbukhtoyarov.bucket4j.Bucket;
import com.github.vladimirbukhtoyarov.bucket4j.Bucket4j;
import com.github.vladimirbukhtoyarov.bucket4j.Refill;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.concurrent.ExecutionException;

@Service
public class RateLimitingService {

    @Value("${app.security.rate-limiting.capacity:100}")
    private int capacity; // Número máximo de tokens en el bucket

    @Value("${app.security.rate-limiting.refill-rate:10}")
    private int refillRate; // Tokens a añadir por minuto

    private final LoadingCache<String, Bucket> cache;

    public RateLimitingService() {
        this.cache = CacheBuilder.newBuilder()
                .expireAfterAccess(Duration.ofHours(1))
                .build(new CacheLoader<>() {
                    @Override
                    public Bucket load(String key) {
                        return createNewBucket();
                    }
                });
    }

    private Bucket createNewBucket() {
        Refill refill = Refill.intervally(refillRate, Duration.ofMinutes(1));
        Bandwidth limit = Bandwidth.classic(capacity, refill);
        return Bucket4j.builder().addLimit(limit).build();
    }

    public Bucket resolveBucket(String ipAddress) {
        try {
            return cache.get(ipAddress);
        } catch (ExecutionException e) {
            throw new RuntimeException("No se pudo obtener el bucket para la IP: " + ipAddress, e);
        }
    }
}
