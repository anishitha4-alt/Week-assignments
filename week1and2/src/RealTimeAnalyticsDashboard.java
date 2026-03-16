import java.util.*;

class PageViewEvent {
    String url;
    String userId;
    String source;

    public PageViewEvent(String url, String userId, String source) {
        this.url = url;
        this.userId = userId;
        this.source = source;
    }
}

public class RealTimeAnalyticsDashboard {

     private HashMap<String, Integer> pageViews = new HashMap<>();

     private HashMap<String, Set<String>> uniqueVisitors = new HashMap<>();

     private HashMap<String, Integer> trafficSources = new HashMap<>();

     public void processEvent(PageViewEvent event) {

         pageViews.put(event.url,
                pageViews.getOrDefault(event.url, 0) + 1);

         uniqueVisitors.putIfAbsent(event.url, new HashSet<>());
        uniqueVisitors.get(event.url).add(event.userId);

         trafficSources.put(event.source,
                trafficSources.getOrDefault(event.source, 0) + 1);
    }

     private List<Map.Entry<String, Integer>> getTopPages() {

        PriorityQueue<Map.Entry<String, Integer>> pq =
                new PriorityQueue<>((a, b) -> a.getValue() - b.getValue());

        for (Map.Entry<String, Integer> entry : pageViews.entrySet()) {

            pq.offer(entry);

            if (pq.size() > 10) {
                pq.poll();
            }
        }

        List<Map.Entry<String, Integer>> result = new ArrayList<>(pq);
        Collections.reverse(result);

        return result;
    }

     public void getDashboard() {

        System.out.println("Top Pages:");

        List<Map.Entry<String, Integer>> topPages = getTopPages();

        int rank = 1;

        for (Map.Entry<String, Integer> entry : topPages) {

            String url = entry.getKey();
            int views = entry.getValue();
            int unique = uniqueVisitors.get(url).size();

            System.out.println(rank + ". " + url +
                    " - " + views + " views (" + unique + " unique)");

            rank++;
        }

        System.out.println("\nTraffic Sources:");

        int total = trafficSources.values().stream().mapToInt(i -> i).sum();

        for (String source : trafficSources.keySet()) {

            int count = trafficSources.get(source);

            double percent = (count * 100.0) / total;

            System.out.printf("%s: %.1f%%\n", source, percent);
        }
    }

     public static void main(String[] args) {

        RealTimeAnalyticsDashboard dashboard = new RealTimeAnalyticsDashboard();

        dashboard.processEvent(
                new PageViewEvent("/article/breaking-news", "user_123", "google"));

        dashboard.processEvent(
                new PageViewEvent("/article/breaking-news", "user_456", "facebook"));

        dashboard.processEvent(
                new PageViewEvent("/sports/championship", "user_789", "direct"));

        dashboard.processEvent(
                new PageViewEvent("/article/breaking-news", "user_123", "google"));

        dashboard.getDashboard();
    }
}