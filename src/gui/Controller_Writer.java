package gui;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import stego.Picture;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;

import stego.StegoWrite;


public class Controller_Writer extends guiUtils {

    @FXML
    private JFXButton writeButton; // Value injected by FXMLLoade

    @FXML
    private JFXTextArea imageLocationText;

    @FXML
    private JFXTextArea hiddenWordsText;


    @FXML
    void writeInfo(ActionEvent event) {
        String hiddenWords = hiddenWordsText.getText();
        String imageLocation = imageLocationText.getText();
        System.out.println(hiddenWords + " " + imageLocation);
        writeInformation(hiddenWords, imageLocation);
    }

    //see https://docs.oracle.com/javafx/2/ui_controls/file-chooser.htm
    //for documentation
    @FXML
    void openPicture(ActionEvent event){
        String fileLocation = fileOpenerPicture();
        System.out.println(fileLocation);
        if(fileLocation!=null) {
            imageLocationText.setText(fileLocation);
            allowWrite();
        }
    }

    //verifiable way to check if writing is allowed. Checks if the default text is currently inputted.
    @FXML
    void allowWrite(){

        if(imageLocationText.getText().equals("Enter Image Location Here..")
                && hiddenWordsText.getText().equals("Words / Paragraphs to be embedded"))
            writeButton.setDisable(true);
        else
            writeButton.setDisable(false);
    }

    @FXML
    void openTextFile(ActionEvent event){
        String textLocation = fileOpenerText();

        if(textLocation!=null) {
            String textRead = readFileAsString(textLocation);
            hiddenWordsText.setText(textRead);
            allowWrite();
        }
        else
            hiddenWordsText.setText("Could not open text file");

        allowWrite();
    }

    /**
     * Write information driver for gui
     */
    static void writeInformation(String hiddenWords, String location) {

        Picture picture;
        boolean imageIsValid = false;
        boolean textIsValid = true;

        /*** Get a valid user input*/
        do {
            try {
                picture = new Picture(location);
                imageIsValid = true;
            } catch (IllegalArgumentException e) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Danger Will Robinson Danger Danger ");
                alert.setHeaderText("An error has occurred");
                alert.setContentText("Image location is not valid. Try again. \n" +
                        "-check if file type is accepted, or image is not corrupted");
                alert.setResizable(true);
                alert.getDialogPane().setPrefSize(480, 320);
                alert.show();

                return; //go back
            }
        } while (!imageIsValid);


        /*** Get a valid user input */
        do {
            if (containsIllegals(hiddenWords)) {
                textIsValid = false;
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Danger Will Robinson Danger");
                alert.setHeaderText("Warning");
                alert.setContentText("Some of the words you inputted were invalid. Please try again!");
                alert.setResizable(true);
                alert.getDialogPane().setPrefSize(480, 320);
                alert.show();
                return; //go back
            }
        } while (!textIsValid);

        StegoWrite writeObject;

        try {
            writeObject = new StegoWrite(hiddenWords, location);
        } catch (IllegalArgumentException e) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Danger Will Robinson Danger");
            alert.setHeaderText("Error:");
            alert.setContentText("Error instantiating StegoWrite object" + e.toString());
            alert.setResizable(true);
            alert.getDialogPane().setPrefSize(480, 320);
            alert.show();
            return;
        }

        if (!writeObject.checkEnoughSpace()) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("It's dangerous out there, that this. ");
            alert.setHeaderText("Warning");
            alert.setContentText("There wasn't enough space in the image to suit your picture. Try writing less maybe");
            alert.setResizable(true);
            alert.getDialogPane().setPrefSize(480, 320);
            alert.show();
            return;
        }

        try {
            writeObject.writeInformationToNewImage();
        } catch (IllegalArgumentException e) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Careful - take this. ");
            alert.setHeaderText("Error Warning");
            alert.setContentText("Error writing to image: " + e.toString());
            alert.setResizable(true);
            alert.getDialogPane().setPrefSize(480, 320);
            alert.show();
            return;
        }

        writeObject.showPicture();
    }

    /**From
     * /** From https://stackoverflow.com/questions/14635391/
     * java-function-to-return-if-string-contains-illegal-characters*/

    /**
     * hopefully the name of this method doesnt put me on uc berkeleys's exclusion list
     */

    //for the future when I implement illegal characters
    public static boolean containsIllegals(String toExamine) {
        if (toExamine.equals(""))
            return true;
        //String[] arr = toExamine.split("[~#@*+%{}<>\\[\\]|\"\\_^]", 2);
        //return arr.length > 1;
        return false;
    }

}
