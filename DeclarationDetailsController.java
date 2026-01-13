import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.math.BigDecimal;

public class DeclarationDetailsController {

    // --- Header ---
    @FXML private Label declNoLabel;
    @FXML private Label importerLabel;
    @FXML private Label dateLabel;
    @FXML private Label statusLabel;

    // --- Items Table ---
    @FXML private TableView<DeclarationItem> itemsTable;
    @FXML private TableColumn<DeclarationItem, String> descColumn;
    @FXML private TableColumn<DeclarationItem, String> hsColumn;
    @FXML private TableColumn<DeclarationItem, Integer> qtyColumn;
    @FXML private TableColumn<DeclarationItem, BigDecimal> unitPriceColumn;
    @FXML private TableColumn<DeclarationItem, String> originColumn;

    @FXML private Button addItemButton;
    @FXML private Button removeItemButton;

    // --- Totals ---
    @FXML private Label customsValueLabel;
    @FXML private Label dutyLabel;
    @FXML private Label vatLabel;
    @FXML private Label totalPayableLabel;

    // --- Status Buttons ---
    @FXML private Button submitButton;
    @FXML private Button assessButton;
    @FXML private Button markPaidButton;

    private Declaration declaration;
    private ObservableList<DeclarationItem> itemsList;

    public void setDeclaration(Declaration declaration) {
        this.declaration = declaration;

        // Header
        declNoLabel.setText(String.valueOf(declaration.getDeclarationNo()));
        importerLabel.setText(declaration.getImporterName());
        dateLabel.setText(declaration.getDate().toString());
        statusLabel.setText(declaration.getStatus().name());

        // Items table
        itemsList = FXCollections.observableArrayList(declaration.getItems());
        itemsTable.setItems(itemsList);

        descColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getDescription()));
        hsColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getHsCode()));
        qtyColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleObjectProperty<>(data.getValue().getQuantity()));
        unitPriceColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleObjectProperty<>(data.getValue().getUnitPrice()));
        originColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getOriginCountry()));

        // Setup buttons
        refreshTotals();
        updateStatusButtons();

        addItemButton.setOnAction(e -> addItem());
        removeItemButton.setOnAction(e -> removeSelectedItem());
        submitButton.setOnAction(e -> changeStatus("SUBMIT"));
        assessButton.setOnAction(e -> changeStatus("ASSESS"));
        markPaidButton.setOnAction(e -> changeStatus("PAID"));
    }

    private void refreshTotals() {
        BigDecimal customs = DeclarationCalculator.calculateCustomsValue(itemsList);
        BigDecimal duty = DeclarationCalculator.calculateDuty(itemsList);
        BigDecimal vat = DeclarationCalculator.calculateVAT(itemsList);
        BigDecimal total = DeclarationCalculator.calculateTotalPayable(itemsList);

        customsValueLabel.setText("Customs Value: " + customs);
        dutyLabel.setText("Duty: " + duty);
        vatLabel.setText("VAT (15%): " + vat);
        totalPayableLabel.setText("Total Payable: " + total);
    }

    private void updateStatusButtons() {
        DeclarationStatus status = declaration.getStatus();
        submitButton.setDisable(status != DeclarationStatus.DRAFT || itemsList.isEmpty());
        assessButton.setDisable(status != DeclarationStatus.SUBMITTED);
        markPaidButton.setDisable(status != DeclarationStatus.ASSESSED);
    }

    private void addItem() {
        // Simple dialog example to add an item
        DeclarationItemDialog dialog = new DeclarationItemDialog();
        dialog.showAndWait().ifPresent(item -> {
            declaration.addItem(item);
            itemsList.add(item);
            refreshTotals();
            updateStatusButtons();
        });
    }

    private void removeSelectedItem() {
        DeclarationItem selected = itemsTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            declaration.removeItem(selected);
            itemsList.remove(selected);
            refreshTotals();
            updateStatusButtons();
        }
    }

    private void changeStatus(String action) {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirm Action");
        confirm.setHeaderText("Are you sure you want to " + action + " this declaration?");
        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    switch (action) {
                        case "SUBMIT" -> declaration.submit(true);
                        case "ASSESS" -> declaration.assess();
                        case "PAID" -> declaration.markPaid();
                    }
                    statusLabel.setText(declaration.getStatus().name());
                    updateStatusButtons();
                } catch (IllegalStateException ex) {
                    Alert alert = new Alert(Alert.AlertType.ERROR, ex.getMessage(), ButtonType.OK);
                    alert.showAndWait();
                }
            }
        });
    }
}

// validation needed