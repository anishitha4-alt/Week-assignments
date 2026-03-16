import java.util.*;

class Transaction {
    int id;
    int amount;
    String merchant;
    String account;
    int time;

    Transaction(int id, int amount, String merchant, String account, int time) {
        this.id = id;
        this.amount = amount;
        this.merchant = merchant;
        this.account = account;
        this.time = time;
    }
}

public class TransactionAnalyzer {


    public static void findTwoSum(List<Transaction> transactions, int target) {

        HashMap<Integer, Transaction> map = new HashMap<>();

        for (Transaction t : transactions) {

            int complement = target - t.amount;

            if (map.containsKey(complement)) {
                Transaction pair = map.get(complement);

                System.out.println("Two Sum Pair: (" + pair.id + "," + t.id + ")");
            }

            map.put(t.amount, t);
        }
    }



    public static void findTwoSumWithTime(List<Transaction> transactions, int target) {

        HashMap<Integer, Transaction> map = new HashMap<>();

        for (Transaction t : transactions) {

            int complement = target - t.amount;

            if (map.containsKey(complement)) {

                Transaction pair = map.get(complement);

                if (Math.abs(t.time - pair.time) <= 60) {
                    System.out.println("Time Window Pair: (" + pair.id + "," + t.id + ")");
                }
            }

            map.put(t.amount, t);
        }
    }



    public static void detectDuplicates(List<Transaction> transactions) {

        HashMap<String, List<Transaction>> map = new HashMap<>();

        for (Transaction t : transactions) {

            String key = t.amount + "-" + t.merchant;

            map.putIfAbsent(key, new ArrayList<>());
            map.get(key).add(t);
        }

        for (String key : map.keySet()) {

            List<Transaction> list = map.get(key);

            if (list.size() > 1) {

                System.out.println("Duplicate Found for " + key);

                for (Transaction t : list) {
                    System.out.println("Transaction ID: " + t.id + " Account: " + t.account);
                }
            }
        }
    }



    public static void findKSum(List<Transaction> transactions, int k, int target) {

        List<Integer> amounts = new ArrayList<>();

        for (Transaction t : transactions) {
            amounts.add(t.amount);
        }

        kSumHelper(amounts, k, target, 0, new ArrayList<>());
    }

    static void kSumHelper(List<Integer> nums, int k, int target, int start, List<Integer> path) {

        if (k == 0 && target == 0) {
            System.out.println("K Sum Combination: " + path);
            return;
        }

        if (k == 0 || start == nums.size()) return;

        for (int i = start; i < nums.size(); i++) {

            path.add(nums.get(i));

            kSumHelper(nums, k - 1, target - nums.get(i), i + 1, path);

            path.remove(path.size() - 1);
        }
    }



    public static void main(String[] args) {

        List<Transaction> transactions = new ArrayList<>();

        transactions.add(new Transaction(1, 500, "StoreA", "acc1", 600));
        transactions.add(new Transaction(2, 300, "StoreB", "acc2", 615));
        transactions.add(new Transaction(3, 200, "StoreC", "acc3", 630));
        transactions.add(new Transaction(4, 500, "StoreA", "acc4", 640));

        int target = 500;

        System.out.println("Classic Two Sum:");
        findTwoSum(transactions, target);

        System.out.println("\nTwo Sum with Time Window:");
        findTwoSumWithTime(transactions, target);

        System.out.println("\nDuplicate Detection:");
        detectDuplicates(transactions);

        System.out.println("\nK Sum:");
        findKSum(transactions, 3, 1000);
    }
}