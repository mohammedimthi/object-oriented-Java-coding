
import java.util.*;
import java.text.DecimalFormat;

class Item {
    private String name;
    private double price;
    private boolean isClearance;
    private static final double BOOK_FOOD_DRINK_DISCOUNT = 0.05;
    private static final double CLOTH_DISCOUNT = 0.20;
    private static final double OTHER_DISCOUNT = 0.03;
    private static final double CLEARANCE_DISCOUNT = 0.20;

    public Item(String name, double price, boolean isClearance) {
        this.name = name;
        this.price = price;
        this.isClearance = isClearance;
    }

    public double getDiscountedPrice() {
        double discount;
        if (name.contains("book") || name.contains("food") || name.contains("drink")) {
            discount = BOOK_FOOD_DRINK_DISCOUNT;
        } else if (name.contains("shirt") || name.contains("dress") || name.contains("pants") || name.contains("cloth")) {
            discount = CLOTH_DISCOUNT;
        } else {
            discount = OTHER_DISCOUNT;
        }
        double discountedPrice = price * (1 - discount);
        if (isClearance) {
            discountedPrice *= (1 - CLEARANCE_DISCOUNT);
        }
        return roundToNearestCent(discountedPrice);
    }

    public double getOriginalPrice() {
        return price;
    }

    public boolean isClearance() {
        return isClearance;
    }

    public String getName() {
        return name;
    }

    private double roundToNearestCent(double value) {
        return Math.round(value * 100.0) / 100.0;
    }
}

class Receipt {
    private List<Item> items;
    private double totalCost;
    private double totalSaved;

    public Receipt() {
        this.items = new ArrayList<>();
        this.totalCost = 0.0;
        this.totalSaved = 0.0;
    }

    public void addItem(Item item) {
        double discountedPrice = item.getDiscountedPrice();
        double originalPrice = item.getOriginalPrice();
        items.add(item);
        totalCost += discountedPrice;
        totalSaved += originalPrice - discountedPrice;
    }

    public void printReceipt() {
        DecimalFormat df = new DecimalFormat("0.00");
        for (Item item : items) {
            double discountedPrice = item.getDiscountedPrice();
            System.out.println("1 " + item.getName() + " at " + df.format(discountedPrice));
        }
        System.out.println("Total: " + df.format(totalCost));
        System.out.println("You saved: " + df.format(totalSaved));
    }
}

public class ShoppingBasket {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int numberOfItems = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        Receipt receipt = new Receipt();

        for (int i = 0; i < numberOfItems; i++) {
            String line = scanner.nextLine();
            String[] parts = line.split(" at ");
            String description = parts[0];
            double price = Double.parseDouble(parts[1]);
            boolean isClearance = description.contains("clearance");

            // Normalize item name by removing "clearance" if present
            String name = description.replace("clearance", "").trim();

            Item item = new Item(name, price, isClearance);
            receipt.addItem(item);
        }

        receipt.printReceipt();
    }
}
