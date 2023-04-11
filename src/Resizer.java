import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.List;

public class Resizer {
    private String path; // le chemin du dossier contenant les images à redimensionner
    private String[] fileExtension; // les extensions de fichiers à redimensionner
    private File[] files; // les fichiers à redimensionner

    /**
     * Constructeur de la classe Resizer.
     * @param path Le chemin du dossier contenant les images à redimensionner.
     * @param fileExtension Les extensions de fichiers à redimensionner.
     */
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

    /**
     * Méthode qui redimensionne une image.
     * @param file Le fichier représentant l'image à redimensionner.
     */
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

    /**
     * Méthode qui redimensionne les images dans le thread principal.
     * @return La durée de l'opération de redimensionnement.
     */
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

    /**
     * Méthode qui redimensionne les images en utilisant plusieurs threads.
     * @return La durée de l'opération de redimensionnement.
     */
    public Duration multiThreadResize() {
        this.createDir("multiThread");
        Instant start = Instant.now();

        List<File> filesList = List.of(this.files);

        filesList.parallelStream().forEach(this::resize);

        Instant end = Instant.now();
        this.removeDir("multiThread");
        return Duration.between(start, end);
    }

    /**
     * Méthode qui crée un dossier.
     * @param dirName Le nom du dossier à créer.
     */
    private void createDir(String dirName){
        File theDir = new File(this.path+"\\"+dirName);
        System.out.println("Create dir : " + theDir.getAbsolutePath());
        if (!theDir.exists()){
            theDir.mkdirs();
        }
    }

    /**
     * Méthode qui supprime un dossier.
     * @param dirName Le nom du dossier à supprimer.
     */
    private void removeDir(String dirName){
        File theDir = new File(this.path+"\\"+dirName);
        System.out.println("Remove dir : " + theDir.getAbsolutePath());
        if (theDir.exists()){
            theDir.delete();
        }
    }
}