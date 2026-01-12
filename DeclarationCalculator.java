import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

public class DeclarationCalculator {

    public static BigDecimal calculateCustomsValue(List<DeclarationItem> items) {
        return items.stream()
                .map(DeclarationItem::getTotalValue)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP);
    }

    public static BigDecimal calculateDuty(List<DeclarationItem> items) {
        BigDecimal total = BigDecimal.ZERO;
        for (DeclarationItem item : items) {
            BigDecimal rate = dutyRateForHs(item.getHsCode());
            total = total.add(item.getTotalValue().multiply(rate));
        }
        return total.setScale(2, RoundingMode.HALF_UP);
    }

    public static BigDecimal calculateVAT(List<DeclarationItem> items) {
        BigDecimal customs = calculateCustomsValue(items);
        BigDecimal duty = calculateDuty(items);
        return customs.add(duty)
                .multiply(BigDecimal.valueOf(0.15))
                .setScale(2, RoundingMode.HALF_UP);
    }

    public static BigDecimal calculateTotalPayable(List<DeclarationItem> items) {
        return calculateCustomsValue(items)
                .add(calculateDuty(items))
                .add(calculateVAT(items))
                .setScale(2, RoundingMode.HALF_UP);
    }

    private static BigDecimal dutyRateForHs(String hsCode) {
        if (hsCode == null) return BigDecimal.valueOf(0.10);
        if (hsCode.startsWith("94")) return BigDecimal.valueOf(0.25);
        if (hsCode.startsWith("85")) return BigDecimal.valueOf(0.15);
        return BigDecimal.valueOf(0.10);
    }
}
