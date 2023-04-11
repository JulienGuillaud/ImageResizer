import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.regex.*;

public class Resizer {
    private String path;
    private String[] fileExtension;
    private File[] files;
    public Resizer(String path, String[] fileExtension) {
        this.path = path;
        this.fileExtension = fileExtension;

        File f = new File(path);
        System.out.println("Path: " + f.getAbsolutePath());

        FilenameFilter filter = new FilenameFilter() {
            @Override
            public boolean accept(File f, String name) {
                for (String extension : fileExtension) {
                    if (name.endsWith(extension)) {
                        return true;
                    }
                }
                return false;
            }
        };

        this.files = f.listFiles(filter);
    }
    public void resize() {
        Instant start = Instant.now();
        for (File file : this.files) {
            try {
                BufferedImage image = ImageIO.read(file);
                BufferedImage resizedImage = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
                Graphics2D g = resizedImage.createGraphics();
                g.drawImage(image, 0, 0, 100, 100, null);
                g.dispose();
                ImageIO.write(resizedImage, "jpg", new File(path+"\\resized\\" + file.getName()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Instant end = Instant.now();
        printDuration("Single thread : ", start, end);
    }
    public void resizeMultiThread() {
        Instant start = Instant.now();

        List<File> filesList = List.of(this.files);

        filesList.parallelStream().forEach(file -> {
            try {
                BufferedImage image = ImageIO.read(file);
                BufferedImage resizedImage = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
                Graphics2D g = resizedImage.createGraphics();
                g.drawImage(image, 0, 0, 100, 100, null);
                g.dispose();
                ImageIO.write(resizedImage, "jpg", new File(path+"\\resizedMultiThread\\" + file.getName()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        Instant end = Instant.now();
        printDuration("Multi thread : ", start, end);
    }
    private void printDuration(String context, Instant start, Instant end) {
        String input = String.valueOf(Duration.between(start, end));
        String regex = "^PT(\\d+\\.\\d+)S$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);

        if (matcher.matches()) {
            double time = Double.parseDouble(matcher.group(1));
            String output = String.format("Time: %,.7fs", time).replace(',', '.');
            System.out.println(context+output);
        } else {
            System.out.println("Invalid input string.");
        }
    }
}
