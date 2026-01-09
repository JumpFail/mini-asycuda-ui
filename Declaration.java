import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

public class Declaration {
    private int declarationNo;
    private LocalDate date;
    private Importer importer;
    private DeclarationStatus status;
    private List<DeclarationItem> items;

    public Declaration(int declarationNo, LocalDate date, Importer importer, DeclarationStatus status, List<DeclarationItem> items) {
        if (declarationNo <= 0) {
            throw new IllegalArgumentException("declarationNo must be a positive integer");
        }
        if (date == null) {
            throw new IllegalArgumentException("date is required");
        }
        if (importer == null) {
            throw new IllegalArgumentException("importer is required");
        }
        if (status == null) {
            throw new IllegalArgumentException("status is required");
        }
        if (items == null || items.isEmpty()) {
            throw new IllegalArgumentException("items are required");
        }

        this.declarationNo = declarationNo;
        this.date = date;
        this.importer = importer;
        this.status = DeclarationStatus.DRAFT;
        this.items = List.copyOf(items); // see line 53
    }

    // getters and setters start
    public int getDeclarationNo() {
        return declarationNo;
    }
    public LocalDate getDate() {
        return date;
    }
    public Importer getImporter() {
        return importer;
    }
    public DeclarationStatus getStatus() {
        return status;
    }
    public void setStatus(DeclarationStatus status) {
        this.status = status;
    }
    public List<DeclarationItem> getItems() {
        return List.copyOf(items); // this prevents external modification
    }
    // getters and setters end

    // calculations
    // sum(quantity * unitPrice) for all items
    public BigDecimal calculateCustomsValue() {
        if (items == null || items.isEmpty()) return BigDecimal.ZERO;
        BigDecimal total = BigDecimal.ZERO;
        for (DeclarationItem item : items) {
            total = total.add(item.getTotalValue()); // refers to DeclarationItem method
        }
        return total.setScale(2, RoundingMode.HALF_UP); // 2 decimal places
    }

    // Duty rate based on hs code prefix and total duty for all items
    private BigDecimal dutyRateForHs(String hsCode) {
        if (hsCode == null) return BigDecimal.valueOf(0.10); // default rate
        String s = hsCode.trim();
        if (s.startsWith("94")) return BigDecimal.valueOf(0.25); // furniture
        if (s.startsWith("85")) return BigDecimal.valueOf(0.15); // electronics
        return BigDecimal.valueOf(0.10);
    }

    // sum(quantity * unitPrice * dutyRate) for all items
    public BigDecimal calculateDuty() {
        if (items == null || items.isEmpty()) return BigDecimal.ZERO;
        BigDecimal totalDuty = BigDecimal.ZERO;
        for (DeclarationItem item : items) {
            BigDecimal itemValue = item.getTotalValue();
            BigDecimal rate = dutyRateForHs(item.getHsCode());
            totalDuty = totalDuty.add(itemValue.multiply(rate)); // add to total
        }
        return totalDuty.setScale(2, RoundingMode.HALF_UP);
    }

    // 15% VAT on (customs value + duty)
    public BigDecimal calculateVAT() {
        BigDecimal customs = calculateCustomsValue();
        BigDecimal duty = calculateDuty();
        BigDecimal base = customs.add(duty); // VAT base (customs + duty)
        BigDecimal vat = base.multiply(BigDecimal.valueOf(0.15));
        return vat.setScale(2, RoundingMode.HALF_UP);
    }

    // total payable = customs value + duty + VAT
    public BigDecimal calculateTotalPayable() {
        BigDecimal customs = calculateCustomsValue();
        BigDecimal duty = calculateDuty();
        BigDecimal vat = calculateVAT();
        BigDecimal total = customs.add(duty).add(vat);
        return total.setScale(2, RoundingMode.HALF_UP);
    }
    // calculations end

    // status
    public void submit() {
        if (status != DeclarationStatus.DRAFT) {
            throw new IllegalStateException("Only DRAFT declarations can be submitted");
        }
        this.status = DeclarationStatus.SUBMITTED;
    }

    public void assess() {
        if (status != DeclarationStatus.SUBMITTED) {
            throw new IllegalStateException("Only SUBMITTED declarations can be assessed");
        }
        this.status = DeclarationStatus.ASSESSED;
    }

    public void markPaid() {
        if (status != DeclarationStatus.ASSESSED) {
            throw new IllegalStateException("Only ASSESSED declarations can be marked as PAID");
        }
        this.status = DeclarationStatus.PAID;
    }
}