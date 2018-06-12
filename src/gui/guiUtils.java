package gui;

import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.nio.file.Files;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.Collections;


/**
 * Utility class for handeling GUI elements, such as
 * opening files, writing files, etc.
 */
public abstract class guiUtils {

    //returns a string to file address
    static String fileOpenerPicture() {
        Stage stage = new Stage();
        stage.setTitle("File Chooser Sample");

        final FileChooser fileChooser = new FileChooser();
        configureFileChooserPicture(fileChooser);
        File file = fileChooser.showOpenDialog(stage);
        if (file != null)
            return file.getAbsolutePath();

        return null;
    }

    private static void configureFileChooserPicture(
            final FileChooser fileChooser) {
        fileChooser.setTitle("Open stego.Picture");
        fileChooser.setInitialDirectory(
                new File(System.getProperty("user.home"))); //

        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Image Files (.jpg or .png)", "*.png", "*.jpg"));
    }


    //returns a string to file address
    static String fileOpenerText() {
        Stage stage = new Stage();
        stage.setTitle("File Chooser Sample");

        final FileChooser fileChooser = new FileChooser();
        configureFileChooserText(fileChooser);
        File file = fileChooser.showOpenDialog(stage);
        if (file != null)
            return file.getAbsolutePath();

        return null;
    }

    private static void configureFileChooserText(
            final FileChooser fileChooser) {
        fileChooser.setTitle("Open Text File");
        fileChooser.setInitialDirectory(
                new File(System.getProperty("user.home")));
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Text Files", "*.txt"));
    }

    //returns string of file
    static String fileSaver() {
        Stage stage = new Stage();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Image");
        File file = fileChooser.showSaveDialog(stage);
        if (file != null) {
            return file.getAbsolutePath();
        }
        return null;
    }

    public static String readFileAsString(String fileName)
    {

        List<String> lines = Collections.emptyList();
        String output = "";
        try
        {
            lines = Files.readAllLines(Paths.get(fileName), StandardCharsets.UTF_8);
        }

        catch (IOException e)
        {
            e.printStackTrace();
        }

        while(!lines.isEmpty()){
            output += lines.remove(0) + "\n";
        }
        return output;
    }

}