package data;

import utils.FilePaths;

import java.io.*;
import java.util.*;

public class ProductionService {

    private List<ProductionRecord> records = new ArrayList<>();

    public ProductionService() {
        load();
    }

    public List<ProductionRecord> getRecords() {
        return records;
    }

    public void load() {
        records.clear();
        File file = new File(FilePaths.PRODUCTION_FILE);

        try {
            if (!file.exists()) file.createNewFile();

            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;

            while ((line = br.readLine()) != null) {
                records.add(ProductionRecord.fromString(line));
            }
            br.close();
        } catch (Exception e) {
            System.out.println(FilePaths.MSG_ERROR + e.getMessage());
        }
    }

    public void save() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FilePaths.PRODUCTION_FILE))) {
            for (ProductionRecord r : records) {
                bw.write(r.toString());
                bw.newLine();
            }
        } catch (Exception e) {
            System.out.println(FilePaths.MSG_ERROR + e.getMessage());
        }
    }

    public void addRecord(ProductionRecord r) {
        records.add(r);
        save();
    }

    public void deleteRecord(int index) {
        records.remove(index);
        save();
    }

    // Индивидуальное задание
    public void showProductionByPeriod(int workshop, String dateFrom, String dateTo) {
        Map<String, Integer> result = new HashMap<>();

        for (ProductionRecord r : records) {
            if (r.getWorkshop() == workshop &&
                    r.getDate().compareTo(dateFrom) >= 0 &&
                    r.getDate().compareTo(dateTo) <= 0) {

                result.put(r.getProductName(),
                        result.getOrDefault(r.getProductName(), 0) + r.getQuantity());
            }
        }

        if (result.isEmpty()) {
            System.out.println("Нет данных за указанный период.");
            return;
        }

        System.out.println("\nКоличество выпущенных изделий по цеху " + workshop);
        result.forEach((k, v) ->
                System.out.println(k + " — " + v + " шт."));
    }
}
