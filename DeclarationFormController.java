import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
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

    public void setImporterList(ObservableList<Importer> importers) {
        importerBox.setItems(importers);
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

    @FXML
    private void openAddImporter() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("AddImporter.fxml")
            );
            Parent root = loader.load();

            ImporterFormController formController = loader.getController();
            formController.setOnSaveCallback(importer -> {
                importerBox.getItems().add(importer);
                importerBox.getSelectionModel().select(importer);
            });

            Stage stage = new Stage();
            stage.setTitle("Add Importer");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initOwner(importerBox.getScene().getWindow());
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.showAndWait();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}