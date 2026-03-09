import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

public class AvailabilityChecker {



        private Set<String> existingUsernames;


        private Map<String, Integer> attemptFrequency;

        public AvailabilityChecker() {
            existingUsernames = new HashSet<>();
            attemptFrequency = new HashMap<>();
        }


        public void addExistingUsernames(List<String> usernames) {
            existingUsernames.addAll(usernames);
        }


        public boolean checkAvailability(String username) {

            attemptFrequency.put(username, attemptFrequency.getOrDefault(username, 0) + 1);


            return !existingUsernames.contains(username);
        }


        public List<String> suggestAlternatives(String username) {
            List<String> suggestions = new ArrayList<>();
            int suffix = 1;


            while(suggestions.size() < 5) {
                String newName = username + suffix;
                if(!existingUsernames.contains(newName)) {
                    suggestions.add(newName);
                }
                suffix++;
            }


            if(username.contains("_")) {
                String dotVersion = username.replace("_", ".");
                if(!existingUsernames.contains(dotVersion)) {
                    suggestions.add(dotVersion);
                }
            }

            return suggestions;
        }


        public String getMostAttempted() {
            String mostAttempted = null;
            int maxCount = 0;

            for(Map.Entry<String, Integer> entry : attemptFrequency.entrySet()) {
                if(entry.getValue() > maxCount) {
                    maxCount = entry.getValue();
                    mostAttempted = entry.getKey();
                }
            }

            return mostAttempted + " (" + maxCount + " attempts)";
        }


        public void registerUsername(String username) {
            existingUsernames.add(username);
        }


        public static void main(String[] args) {
            AvailabilityChecker checker = new AvailabilityChecker();


            checker.addExistingUsernames(Arrays.asList("john_doe", "admin", "user123"));


            System.out.println("john_doe available? " + checker.checkAvailability("john_doe")); // false
            System.out.println("jane_smith available? " + checker.checkAvailability("jane_smith")); // true


            System.out.println("Alternatives for john_doe: " + checker.suggestAlternatives("john_doe"));


            checker.checkAvailability("admin");
            checker.checkAvailability("admin");
            checker.checkAvailability("admin");


            System.out.println("Most attempted: " + checker.getMostAttempted());
        }
}


