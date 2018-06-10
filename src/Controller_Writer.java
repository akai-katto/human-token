import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;


public class Controller_Writer {

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
                alert.setTitle("It's dangerous out there, that this. ");
                alert.setHeaderText("Warning");
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
                alert.setTitle("It's dangerous out there, that this. ");
                alert.setHeaderText("Warning");
                alert.setContentText("Some of the words you inputted were invalid. Please try again!");
                alert.setResizable(true);
                alert.getDialogPane().setPrefSize(480, 320);
                alert.show();
                return; //go back
            }
        } while (!textIsValid);

        write writeObject;

        try {
            writeObject = new write(hiddenWords, location);
        } catch (IllegalArgumentException e) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("It's dangerous out there, that this. ");
            alert.setHeaderText("Warning");
            alert.setContentText("Error instantiating write object" + e.toString());
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
    public static boolean containsIllegals(String toExamine) {
        if (toExamine.equals(""))
            return true;
        //String[] arr = toExamine.split("[~#@*+%{}<>\\[\\]|\"\\_^]", 2);
        //return arr.length > 1;
        return false; //in process of finding characters to prohibit, such as ascii code 27.
    }

}
