import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Declaration {
    private int declarationNo;
    private LocalDate date;
    private Importer importer;
    private DeclarationStatus status;
    private List<DeclarationItem> items = new ArrayList<>();

    public Declaration(int declarationNo, LocalDate date, Importer importer) {
        if (declarationNo <= 0) {
            throw new IllegalArgumentException("declarationNo must be a positive integer");
        }
        if (date == null) {
            throw new IllegalArgumentException("date is required");
        }
        if (importer == null) {
            throw new IllegalArgumentException("importer is required");
        }

        this.declarationNo = declarationNo;
        this.date = date;
        this.importer = importer;
        this.status = DeclarationStatus.DRAFT;
    }

    public int getDeclarationNo() { return declarationNo; }
    public LocalDate getDate() { return date; }
    public Importer getImporter() { return importer; }
    public String getImporterName() { return importer.getName(); }
    public DeclarationStatus getStatus() { return status; }

    public List<DeclarationItem> getItems() {
        return items;
    }

    public void addItem(DeclarationItem item) {
        if (item != null) {
            items.add(item);
        }
    }

    public void removeItem(DeclarationItem item) {
        items.remove(item);
    }

    public int getItemCount() {
        return items.size();
    }

    // --- Status transitions ---
    public void submit(boolean b) {
        if (status != DeclarationStatus.DRAFT) {
            throw new IllegalStateException("Only DRAFT declarations can be submitted");
        }
        if (items.isEmpty()) {
            throw new IllegalStateException("Declaration must have at least one item before submission");
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
