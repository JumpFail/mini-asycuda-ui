import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class DeclarationFormController {

    @FXML
    private TextField declNoField;
    @FXML
    private DatePicker datePicker;
    @FXML
    private ComboBox<Importer> importerBox;

    private Consumer<Declaration> onSaveCallback;

    public void setImporterList(List<Importer> importers) {
        importerBox.getItems().setAll(importers);
    }
    public void setOnSaveCallback(Consumer<Declaration> callback) {
        this.onSaveCallback = callback;
    }

    @FXML
    private void saveDeclaration() {
        Declaration declaration = new Declaration(
                Integer.parseInt(declNoField.getText()),
                datePicker.getValue(),
                importerBox.getValue()
        );

        if (onSaveCallback != null) {
            onSaveCallback.accept(declaration);
        }

        close();
    }

    @FXML
    private void cancelDeclaration() {
        ((Stage) declNoField.getScene().getWindow()).close();
    }

    private void close() {
        ((Stage) declNoField.getScene().getWindow()).close();
    }
}