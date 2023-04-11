import java.io.File;

public class Main {
    public static void main(String[] args) {
        System.out.printf("Start \n");
        String[] filesTypes = {".jpg", ".png"};
        Resizer resizer = new Resizer("C:\\Users\\jgu\\OneDrive\\Images\\perfTest", filesTypes);
        resizer.resize();
        resizer.resizeMultiThread();
    }
}