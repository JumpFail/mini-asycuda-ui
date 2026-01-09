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

public class MiniCustomsController {

    @FXML
    private TableView<Importer> importerTable;

    @FXML
    private TableColumn<Importer, Integer> tinColumn;
    @FXML
    private TableColumn<Importer, String> nameColumn;
    @FXML
    private TableColumn<Importer, String> addressColumn;

    @FXML
    public void initialize(){
        tinColumn.setCellValueFactory(new PropertyValueFactory<>("tin"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
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
}