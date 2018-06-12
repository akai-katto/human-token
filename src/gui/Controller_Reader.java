package gui;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

import stego.Picture;
import stego.StegoRead;


public class Controller_Reader extends guiUtils {

    @FXML
    private JFXButton readButton;

    @FXML
    private JFXButton saveButton;

    @FXML
    private JFXTextArea imageLocationText;

    @FXML
    private JFXTextArea outputText;

    @FXML
    private JFXButton openButton;

    @FXML
    void saveOutput(ActionEvent event) {
        String fileOutput = fileSaver();
        if (fileOutput != null) {
            try (PrintWriter writer = new PrintWriter(fileOutput)) {
                writer.println(outputText.getText());
            } catch (FileNotFoundException e) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Warning");
                alert.setHeaderText("Error Warning: ");
                alert.setContentText("Could not save file: " + e.toString());
                alert.show();
            }
        } else
            System.out.println("no output text selected");
    }

    @FXML
    void readInformation(ActionEvent event) {
        String imageLocation = imageLocationText.getText();
        if (imageLocation.equals("") || imageLocation == null) {
            outputText.setText("No image selected");
        } else {
            String output = readObject(imageLocation);
            outputText.setText(output);
            saveButton.setDisable(false);
        }
    }

    //see https://docs.oracle.com/javafx/2/ui_controls/file-chooser.htm
    //for documentation
    @FXML
    void openPicture(ActionEvent event) {
        String fileLocation = fileOpenerPicture();
        if(fileLocation!=null) {
            imageLocationText.setText(fileLocation);
            readButton.setDisable(false);
        }
    }

    //lets the write button be toggleable until some input is put in.
    @FXML
    void allowRead(){
        readButton.setDisable(false);
    }
    /**
     * Write information driver for gui
     */
    static String readObject(String location) {
        Picture picture;
        boolean imageIsValid = false;
        /*** Get a valid user input*/
        do {
            try {
                picture = new Picture(location);
                imageIsValid = true;
            } catch (IllegalArgumentException e) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Danger Will Robinson Danger Danger ");
                alert.setHeaderText("Warning");
                alert.setContentText("Image location is not valid. Try again. \n" +
                        "-check if file type is accepted, or image is not corrupted. " +
                        "Also picture may or may not actually contain information.");
                alert.setResizable(true);
                alert.getDialogPane().setPrefSize(480, 320);
                alert.show();
                return "invalid input URL"; //go back
            }
        } while (!imageIsValid);

        StegoRead readObject;

        try {
            readObject = new StegoRead(location);
        } catch (IllegalArgumentException e) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Danger Will Robinson Danger Danger ");
            alert.setHeaderText("Warning");
            alert.setContentText("Error instantiating StegoRead object" + e.toString());
            alert.show();
            return "Error trying to stego.StegoRead image: instantiating stego. StegoRead object error";
        }

        String output;
        try {
            output = readObject.extractInformation();
        } catch (IllegalArgumentException e) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Danger Will Robinson Danger Danger ");
            alert.setHeaderText("Warning ");
            alert.setContentText("Error reading image:  " + e.toString());
            alert.show();
            return "Error from reading image";
        }
        return output;
    }
}
