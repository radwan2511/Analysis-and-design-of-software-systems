package presentationLayer;
import businessLayer.deliveryPackage.Facade;
import businessLayer.deliveryPackage.Item;
import businessLayer.deliveryPackage.Location;
import presentationLayer.DeliveryController;
import presentationLayer.Sup_Inv_PresentationLayer.MenuController;
import presentationLayer.TruckController;

import java.text.SimpleDateFormat;
import java.util.*;

public class DeliveryMenu {

    public void run(String type) throws Exception {
        Scanner scanner = new Scanner(System.in);
//        Facade facade = Facade.getInstance();
//        try{
//            facade = new Facade();
//        }catch (Exception e){

        //}

        if(type.equals("driver")){
            driverRun();
        }
        else logisticRun();

    }

    private void driverRun() throws Exception {
        Scanner scanner = new Scanner(System.in);
        DeliveryController deliveryController = DeliveryController.getInstance();
        TruckController truckController = new TruckController();
        String input;
        boolean finish = false;
        while (!finish){
            LogisticMenu();
            input = scanner.next();
            if(validInput(3, input)){ // 3
                switch (Integer.parseInt(input)){
                    case 1:
                        //deliveryController.addDelivery(); // storeKeeper.addOrder
                        break;
                    case 2:
                        MenuController.getInstance().cancelOrder();
                        break;
                    case 3:
                        finish = true;
                        break;
                }
            }
            else
                System.out.println("Select valid number");
            System.out.println();
        }
    }

    private void logisticRun() throws Exception {
        Scanner scanner = new Scanner(System.in);
        DeliveryController deliveryController = DeliveryController.getInstance();
        TruckController truckController = new TruckController();
        String input;
        boolean finish = false;
        while (!finish){
            LogisticMenu();
            input = scanner.next();
            if(validInput(11, input)){ // 11
                switch (Integer.parseInt(input)){
                    case 1:
                        MenuController.getInstance().addComplexOrder();
                        break;
                    case 2:
                        MenuController.getInstance().cancelOrder();
                        break;
                    case 3:
                        deliveryController.getDelivery();
                        break;
                    case 4:
                        deliveryController.viewAllDeliveries();
                        break;
                    case 5:
                        deliveryController.viewDocument();
                        break;
                    case 6:
                        truckController.addTruck();
                        break;
                    case 7:
                        truckController.deleteTruck();
                        break;
                    case 8:
                        truckController.getTruck();
                        break;
                    case 9:
                        truckController.viewAllTrucks();
                        break;
                    case 10:
                        truckController.viewAllTrucks();
                        break;
                    case 11:
                        finish = true;
                        break;
                }
            }
            else
                System.out.println("Select valid number");
            System.out.println();
        }
    }

    private static boolean validInput(int maxInput, String input){
        try {
            int x = Integer.parseInt(input);
            if(x > 0 && x <= maxInput)
                return true;
            else
                return false;
        }catch (Exception e){
            return false;
        }
    }

    private static void LogisticMenu(){
        System.out.println("Welcome, Choose one of the following options:");
        System.out.println("1. Add Delivery");
        System.out.println("2. Cancel Delivery");
        System.out.println("3. View Delivery");
        System.out.println("4. View all deliveries");
        System.out.println("5. View document");
        System.out.println("6. View delivery documents");
        System.out.println("7. Add truck");
        System.out.println("8. Delete truck");
        System.out.println("9. View truck");
        System.out.println("10. View all trucks");
        System.out.println("11. Exit"); // change to Back
    }

    private static void DriverMenu(){
        System.out.println("Welcome, Choose one of the following options:");
        System.out.println("1. View document");
        System.out.println("2. View delivery documents");
        System.out.println("3. Exit"); // change to Back
    }
}