import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;


public class Controller_Reader {

    @FXML
    private JFXButton writeButton; // Value injected by FXMLLoade

    @FXML
    private JFXTextArea imageLocationText;

    @FXML
    private JFXTextArea outputText;


    @FXML
    void readInformation(ActionEvent event) {
        String imageLocation = imageLocationText.getText();
        String output = readObject(imageLocation);
        outputText.setText(output);
    }


    /**Write information driver for gui */
    static String readObject(String location){

        Picture picture;
        boolean imageIsValid = false;

        /*** Get a valid user input*/
        do{
            try{
                picture = new Picture(location);
                imageIsValid = true;
            } catch(IllegalArgumentException e){
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmation Dialog");
                alert.setHeaderText("Look, a Confirmation Dialog");
                alert.setContentText("Image location is not valid. Try again.\n " +
                                     "-check if file type is accepted, or image is not corrupted");
                alert.show();
                return "invalid input URL"; //go back
            }
        }while(!imageIsValid);

        read readObject;

        try {
            readObject = new read(location);
        } catch(IllegalArgumentException e){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation Dialog");
            alert.setHeaderText("Look, a Confirmation Dialog");
            alert.setContentText("Error instantiating read object" + e.toString());
            alert.show();
            return "Error trying to read image: instantiating read object error";
        }

        String output;
        try {
            output = readObject.extractInformation();
        } catch(IllegalArgumentException e){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation Dialog");
            alert.setHeaderText("Look, a Confirmation Dialog");
            alert.setContentText("Error reading image:  " + e.toString());
            alert.show();
            return "Error from reading image";
        }

        return output;
    }

    /**From
     * /** From https://stackoverflow.com/questions/14635391/
     * java-function-to-return-if-string-contains-illegal-characters*/

    /**hopefully the name of this method doesnt put me on uc berkeleys's exclusion list */
    public static boolean containsIllegals(String toExamine) {
        if(toExamine.equals(""))
            return true;
        String[] arr = toExamine.split("[~#@*+%{}<>\\[\\]|\"\\_^]", 2);
        return arr.length > 1;
    }

}
