package data;

public class ProductionRecord {
    private String date;   // Формат YYYY-MM-DD
    private int workshop;  // Номер цеха
    private String productName;
    private int quantity;
    private String responsible;

    public ProductionRecord(String date, int workshop, String productName, int quantity, String responsible) {
        this.date = date;
        this.workshop = workshop;
        this.productName = productName;
        this.quantity = quantity;
        this.responsible = responsible;
    }

    public String getDate() { return date; }
    public int getWorkshop() { return workshop; }
    public String getProductName() { return productName; }
    public int getQuantity() { return quantity; }
    public String getResponsible() { return responsible; }

    @Override
    public String toString() {
        return date + ";" + workshop + ";" + productName + ";" + quantity + ";" + responsible;
    }

    public static ProductionRecord fromString(String line) {
        String[] p = line.split(";");
        return new ProductionRecord(
                p[0], Integer.parseInt(p[1]), p[2],
                Integer.parseInt(p[3]), p[4]
        );
    }
}
