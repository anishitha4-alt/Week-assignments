import java.util.*;


class VideoData {
    String videoId;
    String title;

    VideoData(String videoId, String title) {
        this.videoId = videoId;
        this.title = title;
    }
}

 class LRUCache<K, V> extends LinkedHashMap<K, V> {

    private int capacity;

    public LRUCache(int capacity) {
        super(capacity, 0.75f, true); // access order
        this.capacity = capacity;
    }

    protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
        return size() > capacity;
    }
}

public class MultiLevelCacheSystem {


    static LRUCache<String, VideoData> L1 = new LRUCache<>(10000);


    static HashMap<String, VideoData> L2 = new HashMap<>();


    static HashMap<String, VideoData> L3 = new HashMap<>();


    static HashMap<String, Integer> accessCount = new HashMap<>();

    static int L1Hits = 0;
    static int L2Hits = 0;
    static int L3Hits = 0;

    static final int PROMOTION_THRESHOLD = 2;



    public static VideoData getVideo(String videoId) {

         if (L1.containsKey(videoId)) {
            L1Hits++;
            System.out.println("L1 Cache HIT");
            return L1.get(videoId);
        }

        System.out.println("L1 Cache MISS");


        if (L2.containsKey(videoId)) {
            L2Hits++;
            System.out.println("L2 Cache HIT");

            VideoData video = L2.get(videoId);

            int count = accessCount.getOrDefault(videoId, 0) + 1;
            accessCount.put(videoId, count);

            if (count >= PROMOTION_THRESHOLD) {
                promoteToL1(video);
            }

            return video;
        }

        System.out.println("L2 Cache MISS");


        if (L3.containsKey(videoId)) {

            L3Hits++;
            System.out.println("L3 Database HIT");

            VideoData video = L3.get(videoId);

            L2.put(videoId, video);
            accessCount.put(videoId, 1);

            return video;
        }

        System.out.println("Video Not Found");
        return null;
    }



    public static void promoteToL1(VideoData video) {

        System.out.println("Promoted to L1 Cache");

        L1.put(video.videoId, video);
    }



    public static void invalidateVideo(String videoId) {

        L1.remove(videoId);
        L2.remove(videoId);
        L3.remove(videoId);

        System.out.println("Video invalidated: " + videoId);
    }



    public static void getStatistics() {

        int total = L1Hits + L2Hits + L3Hits;

        System.out.println("\nCache Statistics");

        if (total == 0) return;

        System.out.println("L1 Hits: " + L1Hits);
        System.out.println("L2 Hits: " + L2Hits);
        System.out.println("L3 Hits: " + L3Hits);

        System.out.println("L1 Hit Rate: " + (L1Hits * 100.0 / total) + "%");
        System.out.println("L2 Hit Rate: " + (L2Hits * 100.0 / total) + "%");
        System.out.println("L3 Hit Rate: " + (L3Hits * 100.0 / total) + "%");
    }



    public static void main(String[] args) {

         L3.put("video_123", new VideoData("video_123", "Movie A"));
         L3.put("video_999", new VideoData("video_999", "Movie B"));

         System.out.println("Request 1:");
         getVideo("video_123");

         System.out.println("\nRequest 2:");
         getVideo("video_123");

         System.out.println("\nRequest 3:");
        getVideo("video_999");

         System.out.println("\nRequest 4:");
        getVideo("video_123");

        getStatistics();
    }
}