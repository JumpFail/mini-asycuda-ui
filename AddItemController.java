/*
controller for saving item input
 */

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.math.BigDecimal;
import java.util.function.Consumer;

public class AddItemController {

    @FXML private TextField descriptionField;
    @FXML private TextField hsCodeField;
    @FXML private TextField quantityField;
    @FXML private TextField unitPriceField;
    @FXML private TextField originCountryField;

    /*
    consumer works as a reference to a function
     */
    private Consumer<DeclarationItem> onSaveCallback;

    public void setOnSaveCallback(Consumer<DeclarationItem> callback) {
        this.onSaveCallback = callback;
    }

    @FXML
    private void saveItem() {
        String desc = descriptionField.getText();
        String hs = hsCodeField.getText();
        int qty = Integer.parseInt(quantityField.getText());
        BigDecimal price = new BigDecimal(unitPriceField.getText());
        String origin = originCountryField.getText();

        DeclarationItem item = new DeclarationItem(desc, hs, qty, price, origin);

        if (onSaveCallback != null) {
            onSaveCallback.accept(item);
        }

        close();
    }

    @FXML
    private void cancel() {
        close();
    }

    private void close() {
        Stage stage = (Stage) descriptionField.getScene().getWindow();
        stage.close();
    }
}
