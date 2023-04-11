import java.io.File;
import java.time.Duration;
import java.time.Instant;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    public static void main(String[] args) {
        System.out.printf("Start \n");
        String[] filesTypes = {".jpg", ".png"};
        Resizer resizer = new Resizer("C:\\Users\\jgu\\OneDrive\\Images\\perfTest", filesTypes);
        String resultSingleThread = resizer.singleThreadResize();
        String resultMultiThread = resizer.multiThreadResize();

        printDuration("SingleThread : ", resultSingleThread);
        printDuration("MultiThread : ", resultMultiThread);

        calculateDurationDifference(resultSingleThread, resultMultiThread);
    }

    // Méthode pour afficher la durée d'exécution de la méthode de redimensionnement
    public static void printDuration(String context,String duration) {
        String regex = "^PT(\\d+\\.\\d+)S$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(duration);

        if (matcher.matches()) {
            double time = Double.parseDouble(matcher.group(1));
            String output = String.format("Time: %,.7fs", time).replace(',', '.');
            System.out.println(context+output);
        } else {
            System.out.println("Invalid duration string.");
        }
    }

    public static void calculateDurationDifference(String bigDuration, String smallDuration) {
        // Parse the big and small durations into seconds
        double bigSeconds = Double.parseDouble(bigDuration.substring(2, bigDuration.length() - 1));
        double smallSeconds = Double.parseDouble(smallDuration.substring(2, smallDuration.length() - 1));

        // Calculate the difference in seconds
        double difference = bigSeconds - smallSeconds;

        // Calculate the percentage reduction
        double percentageReduction = (difference / bigSeconds) * 100;

        // Print the results
        System.out.printf("Duration difference: %.7f seconds\n", difference);
        System.out.printf("Percentage reduction: %.2f%%\n", percentageReduction);
    }
}