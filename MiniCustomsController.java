// controllers are like managers
// they each manage one screen or one logical UI area

// this controller controls MiniCustoms.fxml

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
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
    @FXML
    private TextField importerSearchField;
    @FXML
    private TextField declarationSearchField;

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

    // dashboard table
    @FXML
    private TableView<Declaration> recentDeclarationTable;
    @FXML
    private TableColumn<Declaration, Integer> recentDeclNoColumn;
    @FXML
    private TableColumn<Declaration, LocalDate> recentDateColumn;
    @FXML
    private TableColumn<Declaration, String> recentImporterColumn;
    @FXML
    private TableColumn<Declaration, String> recentStatusColumn;

    // data
    private ObservableList<Declaration> allDeclarations;
    private ObservableList<Declaration> recentDeclarations;

    @FXML
    public void initialize(){

        importerSearchField.textProperty().addListener((obs, oldText, newText) -> searchImporter());
        declarationSearchField.textProperty().addListener((obs, oldText, newText) -> searchDeclaration());

        // init and bind table to list
        importersList = FXCollections.observableArrayList();
        importerTable.setItems(importersList);
        allDeclarations = FXCollections.observableArrayList();
        declarationTable.setItems(allDeclarations);

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

        // dashboard
        recentDeclarations = FXCollections.observableArrayList();
        recentDeclarationTable.setItems(recentDeclarations);

        recentDeclNoColumn.setCellValueFactory(new PropertyValueFactory<>("declarationNo"));
        recentDateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        recentImporterColumn.setCellValueFactory(new PropertyValueFactory<>("importerName"));
        recentStatusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
    }

    // Importer tab
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

    // declaration tab
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

                allDeclarations.add(declaration);
                updateRecentDeclarations();
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

    private void updateRecentDeclarations() {
        recentDeclarations.setAll(
                allDeclarations.stream()
                        .sorted((d1, d2) -> d2.getDate().compareTo(d1.getDate()))
                        .limit(5)
                        .toList()
        );
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

    // delete buttons
    @FXML
    private void deleteDeclaration() {
        Declaration selected = declarationTable.getSelectionModel().getSelectedItem();

        if (selected == null) {
            Alert alert = new Alert(
                    Alert.AlertType.WARNING,
                    "Please select a declaration to delete",
                    ButtonType.OK
            );
            alert.showAndWait();
            return;
        }

        Alert confirm = new Alert(
                Alert.AlertType.CONFIRMATION,
                "Are you sure you want to delete Declaration No. "
                        + selected.getDeclarationNo() + "?",
                ButtonType.YES,
                ButtonType.NO
        );

        confirm.setHeaderText("Confirm Deletion");

        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                allDeclarations.remove(selected);
                updateRecentDeclarations();
            }
        });
    }

    @FXML
    private void deleteImporter() {

        Importer selected = importerTable.getSelectionModel().getSelectedItem();

        if (selected == null) {
            Alert alert = new Alert(
                    Alert.AlertType.WARNING,
                    "Please select an importer to delete",
                    ButtonType.OK
            );
            alert.showAndWait();
            return;
        }

        // Prevent deletion if importer has declarations
        boolean hasDeclarations = allDeclarations.stream()
                .anyMatch(d -> d.getImporter().equals(selected));
        // or d.getImporter().equals(selected)

        if (hasDeclarations) {
            Alert alert = new Alert(
                    Alert.AlertType.ERROR,
                    "This importer cannot be deleted because it has declarations.",
                    ButtonType.OK
            );
            alert.setHeaderText("Deletion Not Allowed");
            alert.showAndWait();
            return;
        }

        Alert confirm = new Alert(
                Alert.AlertType.CONFIRMATION,
                "Are you sure you want to delete importer "
                        + selected.getName() + "?",
                ButtonType.YES,
                ButtonType.NO
        );

        confirm.setHeaderText("Confirm Deletion");

        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                importersList.remove(selected);
            }
        });
    }

    // Search importers by name
    @FXML
    private void searchImporter() {
        String query = importerSearchField.getText().trim().toLowerCase();

        if (query.isEmpty()) {
            importerTable.setItems(importersList); // reset
            return;
        }

        ObservableList<Importer> filtered = importersList.filtered(
                i -> i.getName().toLowerCase().contains(query)
        );

        importerTable.setItems(filtered);
    }

    // Search declarations by number
    @FXML
    private void searchDeclaration() {
        String query = declarationSearchField.getText().trim();

        if (query.isEmpty()) {
            declarationTable.setItems(allDeclarations); // reset
            return;
        }

        ObservableList<Declaration> filtered = allDeclarations.filtered(d ->
                String.valueOf(d.getDeclarationNo()).contains(query)
        );

        declarationTable.setItems(filtered);
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