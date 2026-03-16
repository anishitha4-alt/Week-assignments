import java.util.*;

class TrieNode {
    Map<Character, TrieNode> children = new HashMap<>();
    Map<String, Integer> queries = new HashMap<>();
}

public class AutocompleteSystem {

    private TrieNode root;
    private HashMap<String, Integer> frequencyMap;
    private static final int TOP_K = 10;

    public AutocompleteSystem() {
        root = new TrieNode();
        frequencyMap = new HashMap<>();
    }

     public void addQuery(String query) {

        frequencyMap.put(query,
                frequencyMap.getOrDefault(query, 0) + 1);

        TrieNode node = root;

        for (char c : query.toCharArray()) {

            node.children.putIfAbsent(c, new TrieNode());
            node = node.children.get(c);

            node.queries.put(query, frequencyMap.get(query));
        }
    }

     public List<String> search(String prefix) {

        TrieNode node = root;

        for (char c : prefix.toCharArray()) {
            if (!node.children.containsKey(c))
                return new ArrayList<>();
            node = node.children.get(c);
        }

         PriorityQueue<Map.Entry<String, Integer>> pq =
                new PriorityQueue<>((a, b) -> a.getValue() - b.getValue());

        for (Map.Entry<String, Integer> entry : node.queries.entrySet()) {

            pq.offer(entry);

            if (pq.size() > TOP_K) {
                pq.poll();
            }
        }

        List<String> result = new ArrayList<>();

        while (!pq.isEmpty()) {
            result.add(pq.poll().getKey());
        }

        Collections.reverse(result);

        return result;
    }

     public void updateFrequency(String query) {
        addQuery(query);
        System.out.println(query + " frequency: " + frequencyMap.get(query));
    }

     public static void main(String[] args) {

        AutocompleteSystem system = new AutocompleteSystem();

        system.addQuery("java tutorial");
        system.addQuery("javascript");
        system.addQuery("java download");
        system.addQuery("java 21 features");
        system.addQuery("java interview questions");

        List<String> suggestions = system.search("jav");

        System.out.println("Suggestions:");

        int rank = 1;

        for (String s : suggestions) {
            System.out.println(rank + ". " + s);
            rank++;
        }

        system.updateFrequency("java 21 features");
    }
}