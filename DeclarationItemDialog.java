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

        setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                return new DeclarationItem(
                        descField.getText(),
                        hsField.getText(),
                        Integer.parseInt(qtyField.getText()),
                        new BigDecimal(priceField.getText()),
                        originField.getText()
                );
            }
            return null;
        });
    }
}
