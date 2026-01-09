// BigDecimal is used for precise monetary calculations
import java.math.BigDecimal;

public class DeclarationItem {
    private String description;
    private String hsCode;
    private int quantity;
    private BigDecimal unitPrice;
    private String originCountry;

    public DeclarationItem(String description, String hsCode, int quantity, BigDecimal unitPrice, String originCountry) {
        if (description == null || description.trim().isEmpty()) {
            throw new IllegalArgumentException("description is required");
        }
        if (hsCode == null || hsCode.trim().isEmpty()) {
            throw new IllegalArgumentException("hsCode is required");
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("quantity must be a positive integer");
        }
        if (unitPrice == null || unitPrice.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("unitPrice cannot be negative");
        }
        if (originCountry == null || originCountry.trim().isEmpty()) {
            throw new IllegalArgumentException("originCountry is required");
        }
        this.description = description;
        this.hsCode = hsCode;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.originCountry = originCountry;
    }

    public BigDecimal getTotalValue() {
        return unitPrice.multiply(BigDecimal.valueOf(quantity));
    }

    // no setters to keep immutability
    public String getDescription() {
        return description;
    }
    public String getHsCode() {
        return hsCode;
    }
    public int getQuantity() {
        return quantity;
    }
    public BigDecimal getUnitPrice() {
        return unitPrice;
    }
    public String getOriginCountry() {
        return originCountry;
    }

    // String text
    @Override
    public String toString() {
        return description + " x" + quantity + " @ " + unitPrice;
    }
}