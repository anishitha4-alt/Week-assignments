import java.util.Arrays;

public class AccountIdLookup {


    public static void linearSearch(String[] arr, String target) {
        int first = -1, last = -1;
        int comparisons = 0;

        for (int i = 0; i < arr.length; i++) {
            comparisons++;
            if (arr[i].equals(target)) {
                if (first == -1)
                    first = i;
                last = i;
            }
        }

        System.out.println("Linear Search:");
        System.out.println("First occurrence: " + first);
        System.out.println("Last occurrence: " + last);
        System.out.println("Comparisons: " + comparisons);
    }


    public static int binarySearch(String[] arr, String target, Counter counter) {
        int low = 0, high = arr.length - 1;

        while (low <= high) {
            counter.count++;
            int mid = (low + high) / 2;

            int cmp = arr[mid].compareTo(target);

            if (cmp == 0)
                return mid;
            else if (cmp < 0)
                low = mid + 1;
            else
                high = mid - 1;
        }

        return -1;
    }


    public static int countOccurrences(String[] arr, int index, String target, Counter counter) {
        if (index == -1) return 0;

        int count = 1;


        int i = index - 1;
        while (i >= 0 && arr[i].equals(target)) {
            counter.count++;
            count++;
            i--;
        }


        int j = index + 1;
        while (j < arr.length && arr[j].equals(target)) {
            counter.count++;
            count++;
            j++;
        }

        return count;
    }


    static class Counter {
        int count = 0;
    }


    public static void main(String[] args) {

        String[] logs = {"accB", "accA", "accB", "accC"};

        String target = "accB";


        linearSearch(logs, target);


        Arrays.sort(logs);
        System.out.println("\nSorted logs: " + Arrays.toString(logs));


        Counter counter = new Counter();
        int index = binarySearch(logs, target, counter);

        int count = countOccurrences(logs, index, target, counter);

        System.out.println("Binary Search:");
        System.out.println("Found at index: " + index);
        System.out.println("Total occurrences: " + count);
        System.out.println("Comparisons: " + counter.count);
    }
}