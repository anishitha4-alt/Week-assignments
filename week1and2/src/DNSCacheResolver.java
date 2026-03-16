import java.util.*;

class DNSEntry {
    String domain;
    String ipAddress;
    long expiryTime;

    DNSEntry(String domain, String ipAddress, long ttlSeconds) {
        this.domain = domain;
        this.ipAddress = ipAddress;
        this.expiryTime = System.currentTimeMillis() + (ttlSeconds * 1000);
    }

    boolean isExpired() {
        return System.currentTimeMillis() > expiryTime;
    }
}

public class DNSCacheResolver {

    private final int MAX_CACHE_SIZE = 5;

     private LinkedHashMap<String, DNSEntry> cache;

    private int cacheHits = 0;
    private int cacheMisses = 0;

    public DNSCacheResolver() {

        cache = new LinkedHashMap<String, DNSEntry>(16, 0.75f, true) {
            protected boolean removeEldestEntry(Map.Entry<String, DNSEntry> eldest) {
                return size() > MAX_CACHE_SIZE;
            }
        };

        startCleanupThread();
    }

     public synchronized String resolve(String domain) {

        long startTime = System.nanoTime();

        if (cache.containsKey(domain)) {
            DNSEntry entry = cache.get(domain);

            if (!entry.isExpired()) {
                cacheHits++;
                long time = (System.nanoTime() - startTime) / 1_000_000;
                System.out.println("Cache HIT → " + entry.ipAddress + " retrieved in " + time + " ms");
                return entry.ipAddress;
            } else {
                System.out.println("Cache EXPIRED");
                cache.remove(domain);
            }
        }

        cacheMisses++;

        String ip = queryUpstreamDNS(domain);

        DNSEntry newEntry = new DNSEntry(domain, ip, 300);

        cache.put(domain, newEntry);

        System.out.println("Cache MISS → Query upstream → " + ip + " (TTL: 300s)");

        return ip;
    }

     private String queryUpstreamDNS(String domain) {

        try {
            Thread.sleep(100); // simulate DNS delay
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return "172.217.14." + new Random().nextInt(255);
    }

     public void getCacheStats() {

        int total = cacheHits + cacheMisses;

        double hitRate = total == 0 ? 0 : ((double) cacheHits / total) * 100;

        System.out.println("Cache Hits: " + cacheHits);
        System.out.println("Cache Misses: " + cacheMisses);
        System.out.println("Hit Rate: " + hitRate + "%");
    }

     private void startCleanupThread() {

        Thread cleaner = new Thread(() -> {

            while (true) {

                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                synchronized (this) {

                    Iterator<Map.Entry<String, DNSEntry>> it = cache.entrySet().iterator();

                    while (it.hasNext()) {
                        Map.Entry<String, DNSEntry> entry = it.next();

                        if (entry.getValue().isExpired()) {
                            it.remove();
                        }
                    }
                }
            }
        });

        cleaner.setDaemon(true);
        cleaner.start();
    }

     public static void main(String[] args) throws InterruptedException {

        DNSCacheResolver resolver = new DNSCacheResolver();

        resolver.resolve("google.com");
        resolver.resolve("google.com");

        Thread.sleep(2000);

        resolver.resolve("google.com");

        resolver.getCacheStats();
    }
}