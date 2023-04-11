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

    private void resize(File file){
        try {
            BufferedImage image = ImageIO.read(file);
            BufferedImage resizedImage = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = resizedImage.createGraphics();
            g.drawImage(image, 0, 0, 100, 100, null);
            g.dispose();
            ImageIO.write(resizedImage, "jpg", new File(path+"\\singleThread\\" + file.getName()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Duration singleThreadResize() {
        this.createDir("singleThread");
        Instant start = Instant.now();
        for (File file : this.files) {
            resize(file);
        }
        Instant end = Instant.now();
        this.removeDir("singleThread");
        return Duration.between(start, end);
    }
    public Duration multiThreadResize() {
        this.createDir("multiThread");
        Instant start = Instant.now();

        List<File> filesList = List.of(this.files);

        filesList.parallelStream().forEach(this::resize);

        Instant end = Instant.now();
        this.removeDir("multiThread");
        return Duration.between(start, end);
    }

    private void createDir(String dirName){
        File theDir = new File(this.path+"\\"+dirName);
        System.out.println("Create dir : " + theDir.getAbsolutePath());
        if (!theDir.exists()){
            theDir.mkdirs();
        }
    }

    private void removeDir(String dirName){
        File theDir = new File(this.path+"\\"+dirName);
        System.out.println("Remove dir : " + theDir.getAbsolutePath());
        if (theDir.exists()){
            theDir.delete();
        }
    }
}