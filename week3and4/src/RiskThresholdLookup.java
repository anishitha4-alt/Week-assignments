import java.util.Arrays;

public class RiskThresholdLookup {


    public static void linearSearch(int[] arr, int target) {
        int comparisons = 0;
        boolean found = false;

        for (int i = 0; i < arr.length; i++) {
            comparisons++;
            if (arr[i] == target) {
                System.out.println("Linear: Found at index " + i);
                found = true;
                break;
            }
        }

        if (!found) {
            System.out.println("Linear: Not found");
        }

        System.out.println("Comparisons: " + comparisons);
    }


    public static int findInsertionPoint(int[] arr, int target, Counter counter) {
        int low = 0, high = arr.length - 1;
        int pos = arr.length;

        while (low <= high) {
            counter.count++;
            int mid = (low + high) / 2;

            if (arr[mid] >= target) {
                pos = mid;
                high = mid - 1;
            } else {
                low = mid + 1;
            }
        }

        return pos;
    }


    public static int findFloor(int[] arr, int target, Counter counter) {
        int low = 0, high = arr.length - 1;
        int floor = -1;

        while (low <= high) {
            counter.count++;
            int mid = (low + high) / 2;

            if (arr[mid] <= target) {
                floor = arr[mid];
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }

        return floor;
    }


    public static int findCeiling(int[] arr, int target, Counter counter) {
        int low = 0, high = arr.length - 1;
        int ceil = -1;

        while (low <= high) {
            counter.count++;
            int mid = (low + high) / 2;

            if (arr[mid] >= target) {
                ceil = arr[mid];
                high = mid - 1;
            } else {
                low = mid + 1;
            }
        }

        return ceil;
    }


    static class Counter {
        int count = 0;
    }


    public static void main(String[] args) {

        int[] risks = {10, 25, 50, 100};
        int target = 30;


        int[] unsorted = {50, 10, 100, 25};
        linearSearch(unsorted, target);


        Arrays.sort(risks);
        System.out.println("\nSorted risks: " + Arrays.toString(risks));

        Counter counter = new Counter();


        int pos = findInsertionPoint(risks, target, counter);
        System.out.println("Insertion position: " + pos);


        int floor = findFloor(risks, target, counter);
        System.out.println("Floor: " + floor);


        int ceil = findCeiling(risks, target, counter);
        System.out.println("Ceiling: " + ceil);

        System.out.println("Binary comparisons: " + counter.count);
    }
}