// controllers are like managers
// they each manage one screen or one logical UI area

// this controller controls MiniCustoms.fxml

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.time.LocalDate;

public class MiniCustomsController {

    // importers
    @FXML
    private TableView<Importer> importerTable;

    @FXML
    private TableColumn<Importer, Integer> tinColumn;
    @FXML
    private TableColumn<Importer, String> nameColumn;
    @FXML
    private TableColumn<Importer, String> addressColumn;

    // declaration
    @FXML
    private TableView<Declaration> declarationTable;

    @FXML
    private TableColumn<Declaration, Integer> declNoColumn;
    @FXML
    private TableColumn<Declaration, LocalDate> dateColumn;
    @FXML
    private TableColumn<Declaration, String> importerColumn;
    @FXML
    private TableColumn<Declaration, String> statusColumn;
    @FXML
    private TableColumn<Declaration, Integer> itemsColumn;

    @FXML
    public void initialize(){
        // importers
        tinColumn.setCellValueFactory(new PropertyValueFactory<>("tin"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));

        // declarations
        declNoColumn.setCellValueFactory(new PropertyValueFactory<>("declarationNo"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        importerColumn.setCellValueFactory(new PropertyValueFactory<>("importerName"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        itemsColumn.setCellValueFactory(new PropertyValueFactory<>("itemCount"));
    }

    @FXML
    private void openAddImporter() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("AddImporter.fxml")
            );
            Parent root = loader.load();

            ImporterFormController formController = loader.getController();
            formController.setOnSaveCallback(importer -> importerTable.getItems().add(importer));

            Stage stage = new Stage();
            stage.setTitle("Add Importer");
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void openCreateDeclaration() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("AddDeclaration.fxml")
            );
            Parent root = loader.load();

            DeclarationFormController controller = loader.getController();

            controller.setImporterList(importerTable.getItems());
            controller.setOnSaveCallback(
                    declaration -> declarationTable.getItems().add(declaration)
            );

            Stage stage = new Stage();
            stage.setTitle("Create Declaration");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}