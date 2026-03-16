import java.util.*;

class ParkingSpot {

    String licensePlate;
    long entryTime;
    String status;

    public ParkingSpot() {
        status = "EMPTY";
    }
}

public class ParkingLotManager {

    private ParkingSpot[] table;
    private int capacity = 500;
    private int occupied = 0;
    private int totalProbes = 0;

    public ParkingLotManager() {
        table = new ParkingSpot[capacity];
        for (int i = 0; i < capacity; i++) {
            table[i] = new ParkingSpot();
        }
    }

     private int hash(String licensePlate) {
        return Math.abs(licensePlate.hashCode()) % capacity;
    }

     public void parkVehicle(String licensePlate) {

        int index = hash(licensePlate);
        int probes = 0;

        while (!table[index].status.equals("EMPTY") &&
                !table[index].status.equals("DELETED")) {

            index = (index + 1) % capacity;
            probes++;
        }

        table[index].licensePlate = licensePlate;
        table[index].entryTime = System.currentTimeMillis();
        table[index].status = "OCCUPIED";

        occupied++;
        totalProbes += probes;

        System.out.println("Assigned spot #" + index +
                " (" + probes + " probes)");
    }

     public void exitVehicle(String licensePlate) {

        int index = hash(licensePlate);
        int probes = 0;

        while (!table[index].status.equals("EMPTY")) {

            if (table[index].status.equals("OCCUPIED") &&
                    table[index].licensePlate.equals(licensePlate)) {

                long durationMillis =
                        System.currentTimeMillis() - table[index].entryTime;

                double hours = durationMillis / (1000.0 * 60 * 60);

                double fee = Math.ceil(hours * 5); // $5 per hour

                table[index].status = "DELETED";
                occupied--;

                System.out.println("Spot #" + index + " freed");
                System.out.printf("Duration: %.2f hours, Fee: $%.2f\n", hours, fee);

                return;
            }

            index = (index + 1) % capacity;
            probes++;
        }

        System.out.println("Vehicle not found.");
    }

     public int findNearestSpot() {

        for (int i = 0; i < capacity; i++) {
            if (table[i].status.equals("EMPTY") ||
                    table[i].status.equals("DELETED")) {
                return i;
            }
        }

        return -1;
    }

     public void getStatistics() {

        double occupancyRate = (occupied * 100.0) / capacity;

        double avgProbes = occupied == 0 ? 0 :
                (double) totalProbes / occupied;

        System.out.printf("Occupancy: %.2f%%\n", occupancyRate);
        System.out.printf("Avg Probes: %.2f\n", avgProbes);
        System.out.println("Peak Hour: 2-3 PM (simulated)");
    }

     public static void main(String[] args) {

        ParkingLotManager lot = new ParkingLotManager();

        lot.parkVehicle("ABC-1234");
        lot.parkVehicle("ABC-1235");
        lot.parkVehicle("XYZ-9999");

        lot.exitVehicle("ABC-1234");

        lot.getStatistics();
    }
}