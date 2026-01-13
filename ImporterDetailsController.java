import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class ImporterDetailsController {
    @FXML private Label tinLabel;
    @FXML private Label nameLabel;
    @FXML private Label addressLabel;

    private Importer importer;

    public void setImporter(Importer importer) {
        this.importer = importer;

        tinLabel.setText(String.valueOf(importer.getTin()));
        nameLabel.setText(importer.getName());
        addressLabel.setText(importer.getAddress());
    }
}
