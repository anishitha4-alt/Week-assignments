import java.util.Random;

class Asset {
    String name;
    double returnRate;
    double volatility;

    Asset(String name, double returnRate, double volatility) {
        this.name = name;
        this.returnRate = returnRate;
        this.volatility = volatility;
    }

    public String toString() {
        return name + ":" + returnRate + "%";
    }
}

public class PortfolioReturnSorter {


    public static void mergeSort(Asset[] arr, int left, int right) {
        if (left < right) {
            int mid = (left + right) / 2;

            mergeSort(arr, left, mid);
            mergeSort(arr, mid + 1, right);

            merge(arr, left, mid, right);
        }
    }

    public static void merge(Asset[] arr, int left, int mid, int right) {
        int n1 = mid - left + 1;
        int n2 = right - mid;

        Asset[] L = new Asset[n1];
        Asset[] R = new Asset[n2];

        for (int i = 0; i < n1; i++)
            L[i] = arr[left + i];

        for (int j = 0; j < n2; j++)
            R[j] = arr[mid + 1 + j];

        int i = 0, j = 0, k = left;

        // Stable: <= keeps original order for ties
        while (i < n1 && j < n2) {
            if (L[i].returnRate <= R[j].returnRate) {
                arr[k++] = L[i++];
            } else {
                arr[k++] = R[j++];
            }
        }

        while (i < n1)
            arr[k++] = L[i++];

        while (j < n2)
            arr[k++] = R[j++];
    }


    public static void quickSort(Asset[] arr, int low, int high) {
        if (low < high) {
            int pivotIndex = medianOfThree(arr, low, high);
            swap(arr, pivotIndex, high); // move pivot to end

            int pi = partition(arr, low, high);


            if (pi - low < 10)
                insertionSort(arr, low, pi - 1);
            else
                quickSort(arr, low, pi - 1);

            if (high - pi < 10)
                insertionSort(arr, pi + 1, high);
            else
                quickSort(arr, pi + 1, high);
        }
    }


    public static int partition(Asset[] arr, int low, int high) {
        Asset pivot = arr[high];
        int i = low - 1;

        for (int j = low; j < high; j++) {
            if (arr[j].returnRate > pivot.returnRate ||
                    (arr[j].returnRate == pivot.returnRate &&
                            arr[j].volatility < pivot.volatility)) {

                i++;
                swap(arr, i, j);
            }
        }

        swap(arr, i + 1, high);
        return i + 1;
    }


    public static int medianOfThree(Asset[] arr, int low, int high) {
        int mid = (low + high) / 2;

        if (arr[low].returnRate > arr[mid].returnRate)
            swap(arr, low, mid);

        if (arr[low].returnRate > arr[high].returnRate)
            swap(arr, low, high);

        if (arr[mid].returnRate > arr[high].returnRate)
            swap(arr, mid, high);

        return mid;
    }


    public static void insertionSort(Asset[] arr, int low, int high) {
        for (int i = low + 1; i <= high; i++) {
            Asset key = arr[i];
            int j = i - 1;

            while (j >= low && (
                    arr[j].returnRate < key.returnRate ||
                            (arr[j].returnRate == key.returnRate &&
                                    arr[j].volatility > key.volatility)
            )) {
                arr[j + 1] = arr[j];
                j--;
            }

            arr[j + 1] = key;
        }
    }


    public static void swap(Asset[] arr, int i, int j) {
        Asset temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }


    public static void printArray(Asset[] arr) {
        for (Asset a : arr) {
            System.out.print(a + " ");
        }
        System.out.println();
    }


    public static void main(String[] args) {

        Asset[] assets = {
                new Asset("AAPL", 12, 5),
                new Asset("TSLA", 8, 9),
                new Asset("GOOG", 15, 4)
        };

        Asset[] mergeArr = assets.clone();
        Asset[] quickArr = assets.clone();


        mergeSort(mergeArr, 0, mergeArr.length - 1);
        System.out.print("Merge (asc): ");
        printArray(mergeArr);


        quickSort(quickArr, 0, quickArr.length - 1);
        System.out.print("Quick (desc): ");
        printArray(quickArr);
    }
}