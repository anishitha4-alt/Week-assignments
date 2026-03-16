import java.util.*;

public class PlagiarismDetector {

    private static final int N = 5;

     private HashMap<String, Set<String>> ngramIndex = new HashMap<>();

     private HashMap<String, Set<String>> documentNgrams = new HashMap<>();

     public void addDocument(String docId, String text) {

        List<String> words = tokenize(text);

        Set<String> ngrams = new HashSet<>();

        for (int i = 0; i <= words.size() - N; i++) {

            String ngram = String.join(" ", words.subList(i, i + N));
            ngrams.add(ngram);

            ngramIndex.putIfAbsent(ngram, new HashSet<>());
            ngramIndex.get(ngram).add(docId);
        }

        documentNgrams.put(docId, ngrams);
    }

     public void analyzeDocument(String docId) {

        Set<String> ngrams = documentNgrams.get(docId);

        HashMap<String, Integer> matchCount = new HashMap<>();

        for (String ngram : ngrams) {

            Set<String> docs = ngramIndex.get(ngram);

            if (docs != null) {
                for (String otherDoc : docs) {

                    if (!otherDoc.equals(docId)) {
                        matchCount.put(otherDoc,
                                matchCount.getOrDefault(otherDoc, 0) + 1);
                    }
                }
            }
        }

        System.out.println("Extracted " + ngrams.size() + " n-grams");

        for (String otherDoc : matchCount.keySet()) {

            int matches = matchCount.get(otherDoc);

            double similarity =
                    (matches * 100.0) / ngrams.size();

            System.out.println("Found " + matches +
                    " matching n-grams with \"" + otherDoc + "\"");

            System.out.printf("Similarity: %.2f%%", similarity);

            if (similarity > 60) {
                System.out.println(" (PLAGIARISM DETECTED)");
            } else if (similarity > 10) {
                System.out.println(" (suspicious)");
            } else {
                System.out.println();
            }
        }
    }

     private List<String> tokenize(String text) {

        text = text.toLowerCase()
                .replaceAll("[^a-zA-Z ]", "");

        return Arrays.asList(text.split("\\s+"));
    }

     public static void main(String[] args) {

        PlagiarismDetector detector = new PlagiarismDetector();

        String essay1 =
                "Artificial intelligence is transforming the world of technology and education";

        String essay2 =
                "Artificial intelligence is transforming the world of technology rapidly";

        String essay3 =
                "Football is a popular sport played around the world";

        detector.addDocument("essay_089.txt", essay1);
        detector.addDocument("essay_092.txt", essay2);
        detector.addDocument("essay_123.txt", essay3);

        detector.analyzeDocument("essay_092.txt");
    }
}