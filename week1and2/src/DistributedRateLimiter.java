import java.util.concurrent.ConcurrentHashMap;

class TokenBucket {

    private int tokens;
    private final int maxTokens;
    private final double refillRate; // tokens per millisecond
    private long lastRefillTime;

    public TokenBucket(int maxTokens, long refillIntervalMillis) {
        this.maxTokens = maxTokens;
        this.tokens = maxTokens;
        this.refillRate = (double) maxTokens / refillIntervalMillis;
        this.lastRefillTime = System.currentTimeMillis();
    }

     private void refill() {
        long now = System.currentTimeMillis();
        long elapsed = now - lastRefillTime;

        int refillTokens = (int) (elapsed * refillRate);

        if (refillTokens > 0) {
            tokens = Math.min(maxTokens, tokens + refillTokens);
            lastRefillTime = now;
        }
    }

     public synchronized boolean allowRequest() {
        refill();

        if (tokens > 0) {
            tokens--;
            return true;
        }
        return false;
    }

    public int getRemainingTokens() {
        refill();
        return tokens;
    }
}

public class DistributedRateLimiter {

     private ConcurrentHashMap<String, TokenBucket> clients = new ConcurrentHashMap<>();

    private final int LIMIT = 1000;
    private final long WINDOW = 3600000; // 1 hour in milliseconds

    public boolean checkRateLimit(String clientId) {

        clients.putIfAbsent(clientId, new TokenBucket(LIMIT, WINDOW));

        TokenBucket bucket = clients.get(clientId);

        boolean allowed = bucket.allowRequest();

        if (allowed) {
            System.out.println("Allowed (" + bucket.getRemainingTokens() + " requests remaining)");
        } else {
            System.out.println("Denied (0 requests remaining)");
        }

        return allowed;
    }

    public void getRateLimitStatus(String clientId) {

        TokenBucket bucket = clients.get(clientId);

        if (bucket == null) {
            System.out.println("No requests made by client.");
            return;
        }

        int remaining = bucket.getRemainingTokens();

        int used = LIMIT - remaining;

        System.out.println("{used: " + used +
                ", limit: " + LIMIT +
                ", remaining: " + remaining + "}");
    }


    public static void main(String[] args) {

        DistributedRateLimiter limiter = new DistributedRateLimiter();

        String clientId = "abc123";

        for (int i = 0; i < 5; i++) {
            limiter.checkRateLimit(clientId);
        }

        limiter.getRateLimitStatus(clientId);
    }
}