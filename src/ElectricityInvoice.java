import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class ElectricityInvoice {

    private static final String PURCHASE_HISTORY_FILE = "purchase_history.txt";
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private static final double[] RESIDENTIAL_TARIFF = {89, 212, 249};
    private static final double[] NON_RESIDENTIAL_TARIFF = {227, 255};

    public void processInvoice(String meterNumber, int units) {
        String customerName = "";
        String customerCategory = "";

        boolean customerFound = searchCustomerByMeterNumber(meterNumber);

        if (!customerFound) {
            customerName = getCustomerName();
            customerCategory = getCustomerCategory();

            recordNewCustomer(meterNumber, customerName, customerCategory, units);
        } else {
            updateCustomerPurchase(meterNumber, units);
        }

        generateInvoice(meterNumber, customerName, customerCategory, units);
    }

    private boolean searchCustomerByMeterNumber(String meterNumber) {
        File purchaseHistoryFile = new File(PURCHASE_HISTORY_FILE);

        if (!purchaseHistoryFile.exists()) {
            try {
                purchaseHistoryFile.createNewFile();
            } catch (IOException e) {
                System.out.println("An error occurred while creating the purchase history file.");
                e.printStackTrace();
                return false;
            }
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(purchaseHistoryFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data[0].equals(meterNumber)) {
                    return true;
                }
            }
        } catch (IOException e) {
            System.out.println("An error occurred while accessing the purchase history file.");
            e.printStackTrace();
        }
        return false;
    }

    private String getCustomerName() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter customer name: ");
        return scanner.nextLine();
    }

    private String getCustomerCategory() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter customer category (Residential/Non-Residential): ");
        return scanner.nextLine();
    }

    private void recordNewCustomer(String meterNumber, String customerName, String customerCategory, int units) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(PURCHASE_HISTORY_FILE, true))) {
            String purchaseDate = LocalDateTime.now().format(DATE_TIME_FORMATTER);
            String record = meterNumber + "," + customerName + "," + customerCategory + "," + purchaseDate + "," + units;
            writer.write(record);
            writer.newLine();
        } catch (IOException e) {
            System.out.println("An error occurred while updating the purchase history file.");
            e.printStackTrace();
        }
    }

    private void updateCustomerPurchase(String meterNumber, int units) {
        try (BufferedReader reader = new BufferedReader(new FileReader(PURCHASE_HISTORY_FILE));
             BufferedWriter writer = new BufferedWriter(new FileWriter("temp.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data[0].equals(meterNumber)) {
                    String purchaseDate = LocalDateTime.now().format(DATE_TIME_FORMATTER);
                    String record = meterNumber + "," + data[1] + "," + data[2] + "," + purchaseDate + "," + units;
                    writer.write(record);
                    writer.newLine();
                } else {
                    writer.write(line);
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            System.out.println("An error occurred while updating the purchase history file.");
            e.printStackTrace();
        }

        // Rename the temp file to the purchase history file
        File tempFile = new File("temp.txt");
        File purchaseHistoryFile = new File(PURCHASE_HISTORY_FILE);
        if (!tempFile.renameTo(purchaseHistoryFile)) {
            System.out.println("An error occurred while updating the purchase history file.");
        }
    }

    private void generateInvoice(String meterNumber, String customerName, String customerCategory, int units) {
        double currentTariff = getCurrentTariff(customerCategory, units);
        double totalCost = units * currentTariff;
        String token = generateElectricityToken();

        System.out.println("-------- Invoice --------");
        System.out.println("Meter Number: " + meterNumber);
        System.out.println("Customer Name: " + customerName);
        System.out.println("Customer Category: " + customerCategory);
        System.out.println("Units Purchased: " + units);
        System.out.println("Tariff: " + currentTariff + " FRW/kWh");
        System.out.println("Total Cost: " + totalCost + " FRW");
        System.out.println("Electricity Token: " + token);
        System.out.println("-------------------------");
    }

    private double getCurrentTariff(String customerCategory, int units) {
        double[] tariff;

        if (customerCategory.equalsIgnoreCase("Residential")) {
            tariff = RESIDENTIAL_TARIFF;
        } else {
            tariff = NON_RESIDENTIAL_TARIFF;
        }

        if (units <= 15) {
            return tariff[0];
        } else if (units <= 50) {
            return tariff[1];
        } else {
            return tariff[2];
        }
    }

    private String generateElectricityToken() {
        // Algorithm for generating the electricity token
        // Implementation omitted for brevity
        return "TOKEN12345";
    }
}
