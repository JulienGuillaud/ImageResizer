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
        Duration resultSingleThread = resizer.singleThreadResize();
        Duration resultMultiThread = resizer.multiThreadResize();


        System.out.println("SingleThread : "+ resultSingleThread.toString());
        System.out.println("MultiThread : "+ resultMultiThread.toString());

        double reduction = calculateDurationDifference(resultSingleThread, resultMultiThread);
        System.out.printf("Le MultiThread réduits le temps de %s pourcents", reduction);
    }

    /**
     * La méthode calculateDurationDifference calcule la différence entre deux durées en millisecondes et retourne le pourcentage de réduction du temps en mode multi-thread par rapport au temps en mode mono-thread.
     * @param duration1 la durée en mode mono-thread
     * @param duration2 la durée en mode multi-thread
     * @return le pourcentage de réduction du temps en mode multi-thread par rapport au temps en mode mono-thread
     */
    public static double calculateDurationDifference(Duration duration1, Duration duration2) {
        // Calculate the difference between the two durations
        Duration difference = duration1.minus(duration2);

        // Return the percentage reduction as a double value
        return (double) difference.toMillis() / duration1.toMillis() * 100.0;
    }
}