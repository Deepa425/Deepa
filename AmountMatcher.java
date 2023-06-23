import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

class FieldLayout {
    private final int startIndex;
    private final int endIndex;

    public FieldLayout(int startIndex, int endIndex) {
        this.startIndex = startIndex;
        this.endIndex = endIndex;
    }

    public int getStartIndex() {
        return startIndex;
    }

    public int getEndIndex() {
        return endIndex;
    }
}

class CurrencyAmount {
    private final double netAmount;
    private final double usdAmount;

    public CurrencyAmount(double netAmount, double usdAmount) {
        this.netAmount = netAmount;
        this.usdAmount = usdAmount;
    }

    public double getNetAmount() {
        return netAmount;
    }

    public double getUsdAmount() {
        return usdAmount;
    }
}

public class AmountMatcher {
    public static void main(String[] args) {
        String genesisLayoutFile = "C:\\Users\\a840014\\OneDrive - Atos\\Desktop\\GMAS-Genesis Balancing\\genesis_layout.txt";
        String genesisDataFile = "C:\\Users\\a840014\\OneDrive - Atos\\Desktop\\GMAS-Genesis Balancing\\genesis_file.txt";
        String gmasLayoutFile = "C:\\Users\\a840014\\OneDrive - Atos\\Desktop\\GMAS-Genesis Balancing\\gmas_layout.txt";
        String gmasDataFile = "C:\\Users\\a840014\\OneDrive - Atos\\Desktop\\GMAS-Genesis Balancing\\gmas_file.txt";

        // Step 1: Parse the Genesis and GMAS files using layout files
        Map<String, CurrencyAmount> genesisData = parseFile(genesisDataFile, genesisLayoutFile);
        Map<String, CurrencyAmount> gmasData = parseFile(gmasDataFile, gmasLayoutFile);

        // Step 2: Aggregate the amounts for each currency in each file
        Map<String, CurrencyAmount> genesisAggregatedAmounts = aggregateAmounts(genesisData);
        Map<String, CurrencyAmount> gmasAggregatedAmounts = aggregateAmounts(gmasData);

        // Step 3: Compare the amounts between Genesis and GMAS files
        compareAmounts(genesisAggregatedAmounts, gmasAggregatedAmounts);

        // Step 4: Print out the differences and flag the mismatches
        // You can modify the code in Step 3 to highlight or flag the mismatched amounts.
    }

    private static Map<String, CurrencyAmount> parseFile(String dataFile, String layoutFile) {
        Map<String, FieldLayout> layoutMap = readLayoutFile(layoutFile);
        Map<String, CurrencyAmount> data = new HashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader(dataFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                String currency = extractField(line, layoutMap.get("currency"));
                double netAmount = Double.parseDouble(extractField(line, layoutMap.get("netAmount")));
                double usdAmount = Double.parseDouble(extractField(line, layoutMap.get("usdAmount")));
                data.put(currency, new CurrencyAmount(netAmount, usdAmount));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return data;
    }

    private static Map<String, FieldLayout> readLayoutFile(String layoutFile) {
        Map<String, FieldLayout> layoutMap = new HashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader(layoutFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] fields = line.split(":");
                String fieldName = fields[0];
                int startIndex = Integer.parseInt(fields[16]);
                int endIndex = Integer.parseInt(fields[17]);
                layoutMap.put(fieldName, new FieldLayout(startIndex, endIndex));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return layoutMap;
    }

    private static String extractField(String line, FieldLayout layout) {
        int startIndex = layout.getStartIndex();
        int endIndex = layout.getEndIndex();
        return line.substring(startIndex, endIndex).trim();
    }

    private static Map<String, CurrencyAmount> aggregateAmounts(Map<String, CurrencyAmount> data) {
        Map<String, CurrencyAmount> aggregatedAmounts = new HashMap<>();

        for (Map.Entry<String, CurrencyAmount> entry : data.entrySet()) {
            String currency = entry.getKey();
            CurrencyAmount amount = entry.getValue();

            if (aggregatedAmounts.containsKey(currency)) {
                CurrencyAmount aggregatedAmount = aggregatedAmounts.get(currency);
                double netAmount = aggregatedAmount.getNetAmount() + amount.getNetAmount();
                double usdAmount = aggregatedAmount.getUsdAmount() + amount.getUsdAmount();
                aggregatedAmounts.put(currency, new CurrencyAmount(netAmount, usdAmount));
            } else {
                aggregatedAmounts.put(currency, amount);
            }
        }

        return aggregatedAmounts;
    }

    private static void compareAmounts(Map<String, CurrencyAmount> genesisData, Map<String, CurrencyAmount> gmasData) {
        for (Map.Entry<String, CurrencyAmount> entry : genesisData.entrySet()) {
            String currency = entry.getKey();
            CurrencyAmount genesisAmount = entry.getValue();
            CurrencyAmount gmasAmount = gmasData.get(currency);



                    System.out.println("Currency: " + currency);
                    System.out.println("Genesis Net Amount: " + genesisAmount.getNetAmount());
                    System.out.println("Genesis USD Amount: " + genesisAmount.getUsdAmount());
                    System.out.println("GMAS Net Amount: " + gmasAmount.getNetAmount());
                    System.out.println("GMAS USD Amount: " + gmasAmount.getUsdAmount());
                    System.out.println("--------------------");
                }
            }
        }



