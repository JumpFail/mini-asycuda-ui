// controllers are like managers
// they each manage one screen or one logical UI area

// this controller controls MiniCustoms.fxml

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
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
    private ObservableList<Importer> importersList;

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

        // init and bind table to list
        importersList = FXCollections.observableArrayList();
        importerTable.setItems(importersList);

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
            formController.setOnSaveCallback(importer -> {

                if (!isTinUnique(importer.getTin())) {
                    showError("Importer with this TIN already exists!");
                    return;
                }

                importersList.add(importer);
            });


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
    private void openImporterDetails() {

        Importer selected = importerTable.getSelectionModel().getSelectedItem();

        if (selected == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING,
                    "Please select an importer first",
                    ButtonType.OK);
            alert.showAndWait();
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("ImporterDetails.fxml")
            );
            Parent root = loader.load();

            ImporterDetailsController controller = loader.getController();
            controller.setImporter(selected);

            Stage stage = new Stage();
            stage.setTitle("Importer Details - " + selected.getName());
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
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

            controller.setImporterList(importersList);
            controller.setOnSaveCallback(declaration -> {

                if (!isDeclarationNoUnique(declaration.getDeclarationNo())) {
                    showError("Declaration number already exists!");
                    return;
                }

                declarationTable.getItems().add(declaration);
            });

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

    @FXML
    private void openDeclarationDetails() {
        // 1. Get the selected declaration from the table
        Declaration selected = declarationTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            // optional: show alert
            Alert alert = new Alert(Alert.AlertType.WARNING, "Please select a declaration first", ButtonType.OK);
            alert.showAndWait();
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("DeclarationDetails.fxml")
            );
            Parent root = loader.load();

            DeclarationDetailsController controller = loader.getController();
            controller.setDeclaration(selected);

            Stage stage = new Stage();
            stage.setTitle("Declaration Details - " + selected.getDeclarationNo());
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);

            stage.setOnHidden(e -> declarationTable.refresh()); // table refresh
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Failed to open Declaration Details:\n" + e.getMessage(), ButtonType.OK);
            alert.showAndWait();
        }
    }

    // unique TIN/DecNo validation
    private boolean isTinUnique(int tin) {
        return importersList.stream()
                .noneMatch(i -> i.getTin() == tin);
    }

    private boolean isDeclarationNoUnique(int declNo) {
        return declarationTable.getItems().stream()
                .noneMatch(d -> d.getDeclarationNo() == declNo);
    }

    // error message
    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}