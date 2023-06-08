import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter meter number :");
        String  meterNumber = scanner.nextLine();
        System.out.println("Enter units to purchase ");
        int units = scanner.nextInt();

//        skip a space
        scanner.nextLine();

        ElectricityInvoice  electricityInvoice =  new ElectricityInvoice();
        electricityInvoice.processInvoice(meterNumber,units);
    }
}