class Client {
    String name;
    int riskScore;
    double accountBalance;

    Client(String name, int riskScore, double accountBalance) {
        this.name = name;
        this.riskScore = riskScore;
        this.accountBalance = accountBalance;
    }

    public String toString() {
        return name + ":" + riskScore;
    }
}

public class ClientRiskSorter {


    public static void bubbleSort(Client[] arr) {
        int n = arr.length;
        int swaps = 0;

        for (int i = 0; i < n - 1; i++) {
            boolean swapped = false;

            for (int j = 0; j < n - i - 1; j++) {
                if (arr[j].riskScore > arr[j + 1].riskScore) {
                    // swap
                    Client temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;

                    swaps++;
                    swapped = true;
                }
            }

            if (!swapped) break;
        }

        System.out.print("Bubble (asc): ");
        printArray(arr);
        System.out.println("Swaps: " + swaps);
    }


    public static void insertionSort(Client[] arr) {
        for (int i = 1; i < arr.length; i++) {
            Client key = arr[i];
            int j = i - 1;

            while (j >= 0 && (
                    arr[j].riskScore < key.riskScore ||
                            (arr[j].riskScore == key.riskScore &&
                                    arr[j].accountBalance < key.accountBalance)
            )) {
                arr[j + 1] = arr[j];
                j--;
            }

            arr[j + 1] = key;
        }

        System.out.print("Insertion (desc): ");
        printArray(arr);
    }


    public static void topKClients(Client[] arr, int k) {
        System.out.print("Top " + k + " risks: ");
        for (int i = 0; i < k && i < arr.length; i++) {
            System.out.print(arr[i].name + "(" + arr[i].riskScore + ") ");
        }
        System.out.println();
    }


    public static void printArray(Client[] arr) {
        for (Client c : arr) {
            System.out.print(c + " ");
        }
        System.out.println();
    }


    public static void main(String[] args) {

        Client[] clients = {
                new Client("clientC", 80, 5000),
                new Client("clientA", 20, 10000),
                new Client("clientB", 50, 7000)
        };


        Client[] bubbleArr = clients.clone();
        Client[] insertionArr = clients.clone();


        bubbleSort(bubbleArr);


        insertionSort(insertionArr);


        topKClients(insertionArr, 10);
    }
}