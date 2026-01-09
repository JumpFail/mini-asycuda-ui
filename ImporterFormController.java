// this controller controls importer form logic

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.function.Consumer;

public class ImporterFormController {

    @FXML private TextField tinField;
    @FXML private TextField nameField;
    @FXML private TextField addressField;

    // This will be set from MiniCustoms
    private Consumer<Importer> onSaveCallback;

    public void setOnSaveCallback(Consumer<Importer> callback){
        this.onSaveCallback = callback;
    }

    @FXML
    void save() {
        String tinText = tinField.getText().trim();
        String name = nameField.getText().trim();
        String address = addressField.getText().trim();

        // validation
        if (tinText.isEmpty() || name.isEmpty() || address.isEmpty()) {
            showAlert("All fields are required!");
            return;
        }

        int tin;
        try {
            tin = Integer.parseInt(tinText);
        } catch (NumberFormatException e) {
            showAlert("TIN must be a number");
            return;
        }

        // Create Importer object
        Importer importer = new Importer(tin, name, address);

        // Pass to the callback (e.g., table controller)
        if (onSaveCallback != null) {
            onSaveCallback.accept(importer);
        }

        closeWindow();
    }

    @FXML
    void cancel() {
        closeWindow();
    }

    private void closeWindow() {
        Stage stage = (Stage) tinField.getScene().getWindow();
        stage.close();
    }

    private void showAlert(String message){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

// inputted fields aren't saved to table