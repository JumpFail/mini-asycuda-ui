import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

public class DeclarationCalculator {

    // total customs value
    // by summing up their individual totals
    public static BigDecimal calculateCustomsValue(List<DeclarationItem> items) {
        return items.stream()
                .map(DeclarationItem::getTotalValue)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP);
    }

    // total duty
    // by applying the duty rate based on HS code
    // to each item's total value and summing them up
    public static BigDecimal calculateDuty(List<DeclarationItem> items) {
        BigDecimal total = BigDecimal.ZERO;
        for (DeclarationItem item : items) {
            BigDecimal rate = dutyRateForHs(item.getHsCode());
            total = total.add(item.getTotalValue().multiply(rate));
        }
        return total.setScale(2, RoundingMode.HALF_UP);
    }

    // total VAT
    // by applying a fixed VAT rate of 15%
    // on the sum of customs value and duty
    public static BigDecimal calculateVAT(List<DeclarationItem> items) {
        BigDecimal customs = calculateCustomsValue(items);
        BigDecimal duty = calculateDuty(items);
        return customs.add(duty)
                .multiply(BigDecimal.valueOf(0.15))
                .setScale(2, RoundingMode.HALF_UP);
    }

    // total payable amount
    // by summing customs value, duty, and VAT
    public static BigDecimal calculateTotalPayable(List<DeclarationItem> items) {
        return calculateCustomsValue(items)
                .add(calculateDuty(items))
                .add(calculateVAT(items))
                .setScale(2, RoundingMode.HALF_UP);
    }

    // duty rate for HS code
    // 10% - default
    // 25% - furniture (HS code 94)
    // 15% - electronics (HS code 85)
    private static BigDecimal dutyRateForHs(String hsCode) {
        if (hsCode == null) return BigDecimal.valueOf(0.10);
        if (hsCode.startsWith("94")) return BigDecimal.valueOf(0.25); // furniture
        if (hsCode.startsWith("85")) return BigDecimal.valueOf(0.15); // electronics
        return BigDecimal.valueOf(0.10);
    }
}
