import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import java.math.BigDecimal;
import java.util.Optional;

public class DeclarationItemDialog extends Dialog<DeclarationItem> {
    public DeclarationItemDialog() {
        setTitle("Add Declaration Item");

        Label descLabel = new Label("Description:");
        TextField descField = new TextField();

        Label hsLabel = new Label("HS Code:");
        TextField hsField = new TextField();

        Label qtyLabel = new Label("Quantity:");
        TextField qtyField = new TextField();

        Label priceLabel = new Label("Unit Price:");
        TextField priceField = new TextField();

        Label originLabel = new Label("Origin Country:");
        TextField originField = new TextField();

        GridPane grid = new GridPane();
        grid.setVgap(5);
        grid.setHgap(5);
        grid.add(descLabel, 0, 0); grid.add(descField, 1, 0);
        grid.add(hsLabel, 0, 1); grid.add(hsField, 1, 1);
        grid.add(qtyLabel, 0, 2); grid.add(qtyField, 1, 2);
        grid.add(priceLabel, 0, 3); grid.add(priceField, 1, 3);
        grid.add(originLabel, 0, 4); grid.add(originField, 1, 4);

        getDialogPane().setContent(grid);
        getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        // --- Intercept OK button ---
        Button okButton = (Button) getDialogPane().lookupButton(ButtonType.OK);
        okButton.addEventFilter(javafx.event.ActionEvent.ACTION, event -> {
            if (!validateFields(descField, hsField, qtyField, priceField, originField)) {
                event.consume(); // ⛔ prevent dialog from closing
            }
        });

        setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                return new DeclarationItem(
                        descField.getText().trim(),
                        hsField.getText().trim(),
                        Integer.parseInt(qtyField.getText().trim()),
                        new BigDecimal(priceField.getText().trim()),
                        originField.getText().trim()
                );
            }
            return null;
        });
    }

    private boolean validateFields(
            TextField desc,
            TextField hs,
            TextField qty,
            TextField price,
            TextField origin
    ) {
        if (desc.getText().isBlank()) {
            showError("Item description is required.");
            return false;
        }

        if (hs.getText().isBlank()) {
            showError("HS Code is required.");
            return false;
        }

        try {
            int q = Integer.parseInt(qty.getText());
            if (q <= 0) {
                showError("Quantity must be greater than zero.");
                return false;
            }
        } catch (NumberFormatException e) {
            showError("Quantity must be a valid number.");
            return false;
        }

        try {
            BigDecimal p = new BigDecimal(price.getText());
            if (p.compareTo(BigDecimal.ZERO) <= 0) {
                showError("Unit price must be greater than zero.");
                return false;
            }
        } catch (NumberFormatException e) {
            showError("Unit price must be a valid number.");
            return false;
        }

        if (origin.getText().isBlank()) {
            showError("Country of origin is required.");
            return false;
        }

        return true;
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Validation Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
