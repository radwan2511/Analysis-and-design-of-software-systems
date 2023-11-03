package presentationLayer;

import businessLayer.deliveryPackage.Facade;
import businessLayer.deliveryPackage.*;

import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.*;

public class DeliveryController {
    private Facade facade;
    private Scanner scanner;

    private static DeliveryController deliveryController = null;

    // ????????????????? change to singelton ????????????????
    private DeliveryController() throws Exception{
        this.facade = Facade.getInstance();
        scanner = new Scanner(System.in);
    }

    public static DeliveryController getInstance() throws Exception {
        if(deliveryController == null){
            deliveryController = new DeliveryController();
        }
        return deliveryController;
    }

//    public void addDelivery(){
//        try {
//            System.out.print("\nEnter delivery date in format dd/MM/yyyy: ");
//            Date date = new SimpleDateFormat("dd/MM/yyyy").parse(scanner.next());
////            System.out.print("Enter launch time in format hh:mm: ");
////            String hour = scanner.next();
////            LocalTime.parse(hour);
//            System.out.print("Enter delivery source -> ");
//            Location source = getLocation();
//            Map<Location, LinkedList<Item>> items = getOrder();
//            System.out.println();
//            Response<Delivery> response = facade.addDelivery(date, source, items, "");
//            if (response.errorOccurred())
//                System.out.println("Error: " + response.getMsg());
//            else {
//                System.out.println("Delivery has been added successfully!\nDelivery ID : " + response.getValue().getId());
//            }
//        }catch (Exception e){
//            System.out.println(e.getMessage());
//        }
//    }

    // ?????????????? change this to cancel order and call the fun cancel order in store or sup
//    public void cancelDelivery(){
//        try{
//            System.out.println("\nEnter delivery ID: ");
//            int deliveryId = scanner.nextInt();
//            System.out.println();
//            Response<Delivery> response = facade.cancelDelivery(deliveryId);
//            if(response.errorOccurred())
//                System.out.println("Error: " + response.getMsg());
//            else {
//                System.out.println("Delivery has been canceled successfully!");
//            }
//
////            System.out.print("\nEnter delivery's date in format dd/MM/yyyy: ");
////            Date date = new SimpleDateFormat("dd/MM/yyyy").parse(scanner.next());
////            System.out.print("Enter launch time in format hh:mm: ");
////            String hour = scanner.next();
////            LocalTime.parse(hour);
////            System.out.print("Enter truck plate number: ");
////            String plateNumber = scanner.next();
////            System.out.println();
////            Response<Delivery> response = facade.cancelDelivery(date, hour, plateNumber);
////            if(response.errorOccurred())
////                System.out.println("Error: " + response.getMsg());
////            else
////                System.out.println("Delivery has been canceled successfully!");
//        }catch (Exception e){
//            System.out.println(e.getMessage());
//        }
//    }

    public void getDelivery(){
        try {
            System.out.println("\nEnter delivery ID: ");
            int deliveryId = scanner.nextInt();
            System.out.println();
            Response<Delivery> response = facade.getDelivery(deliveryId);
            if(response.errorOccurred())
                System.out.println("Error: " + response.getMsg());
            else {
                printDelivery(response.getValue());
            }

//            System.out.print("\nEnter delivery date in format dd/MM/yyyy: ");
//            Date date = new SimpleDateFormat("dd/MM/yyyy").parse(scanner.next());
//            System.out.print("Enter launch time in format hh:mm: ");
//            String hour = scanner.next();
//            LocalTime.parse(hour);
//            System.out.print("Enter truck plate number: ");
//            String plateNumber = scanner.next();
//            System.out.println();
//            Response<Delivery> response = facade.getDelivery(date, hour, plateNumber);
//            if(response.errorOccurred())
//                System.out.println("Error: " + response.getMsg());
//            else {
//                printDelivery(response.getValue());
//            }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    public void viewAllDeliveries(){
        Response<List<Delivery>> response = facade.getAllDeliveries();
        if(response.errorOccurred())
            System.out.println("Error: " + response.getMsg());
        else {
            for(Delivery delivery : response.getValue()){
                printDelivery(delivery);
                System.out.println();
            }
        }
    }

    public void viewDocument(){
        System.out.println("\nEnter document ID: ");
        int documentId = scanner.nextInt();
        Response<Map<Delivery, Document> > response = facade.getDocument(documentId);
        if(response.errorOccurred())
            System.out.println("Error: " + response.getMsg());
        else {
            Delivery delivery = (Delivery)response.getValue().keySet().toArray()[0];
            Document document = (Document) response.getValue().values().toArray()[0];
            System.out.println("\tDocument ID: " + document.getId() +
                    "\n\tTotal weight (Truck + Items): " + document.getTotalWeight());
            System.out.print("\tDate: " + delivery.getDate() +
                    "\n\tLaunch Time: " + delivery.getLaunchTime() +
                    "\n\tTruck plate number: " + delivery.getTruckPlateNumber() +
                    "\n\tDriver ID: " + delivery.getDriverId() +
                    "\n\tSource -> " );
            printLocation(delivery.getSource());
            System.out.print("\tDestination -> ");
            printLocation(document.getDestination());
            System.out.println("\tItems: ");
            for(Item item : document.getItems()){
                printItem(item);
            }
            System.out.println();
        }
    }

    public void getDeliveryDocuments(){

    }

//    public void viewDeliveryDocuments(){
//        System.out.println("\nEnter delivery ID: ");
//        int deliveryId = scanner.nextInt();
//        Response<List<Document>> response = facade.getDeliveryDocuments(deliveryId);
//        if(response.errorOccurred())
//            System.out.println("Error: " + response.getMsg());
//        else{
//
//        }
//    }

//    private Location getLocation() throws Exception{
//        System.out.print("\nEnter address: ");
//        String address = scanner.next();
//        System.out.print("Enter contact name: ");
//        String contactName = scanner.next();
//        System.out.print("Enter phone number: ");
//        String phoneNumber = scanner.next();
//        System.out.println();
//        return facade.newLocation(address, phoneNumber, contactName).getValue();
//    }

//    private Map<Location, LinkedList<Item>> getOrder(){
//        Map<Location, LinkedList<Item>> items = new HashMap<>();
//        boolean destFlag = true;
//        while (destFlag){
//            try {
//                System.out.print("Enter a destination: ");
//                Location dest = getLocation();
//                boolean itemFlag = true;
//                while (itemFlag){
//                    try {
//                        LinkedList<Item> list = new LinkedList<>();
//                        System.out.print("Enter item's details: ");
//                        System.out.print("Item's Name: ");
//                        String name = scanner.next();
//                        System.out.print("Item's Amount: ");
//                        double amount = scanner.nextDouble();
//                        System.out.print("Item's Weight: ");
//                        double weight = scanner.nextDouble();
//                        System.out.println();
//                        Item item = facade.newItem(name, amount, weight);
//                        list.add(item);
//                        System.out.println("Item has been added successfully!");
//                        System.out.print("Add another item? [yes / no]: ");
//                        String another = scanner.next();
//                        System.out.println();
//                        if (!another.equals("yes")){
//                            itemFlag = false;
//                            items.put(dest, list);
//                        }
//                    } catch (Exception e){
//                        System.out.println(e.getMessage());
//                    }
//                }
//                System.out.print("Add another destination? [yes / no]: ");
//                String another = scanner.next();
//                System.out.println();
//                if (!another.equals("yes"))
//                    destFlag = false;
//            }catch (Exception e){
//                System.out.println(e.getMessage());
//            }
//        }
//        return items;
//    }

    private void printDelivery(Delivery delivery){
        System.out.print("\tDeliveryID: " + delivery.getId() +
                "\n\tDate: " + delivery.getDate() +
                "\n\tLaunch Time: " + delivery.getLaunchTime() +
                "\n\tTruck plate number: " + delivery.getTruckPlateNumber() +
                "\n\tDriver ID: " + delivery.getDriverId() +
                "\n\tSource -> " );
        printLocation(delivery.getSource());

        Response<List<Document>> response = facade.getDeliveryDocuments(delivery.getId());
        if(response.errorOccurred())
            System.out.println("Error: " + response.getMsg());
        else{
            System.out.println("\tDocuments: ");
            for(Document document : response.getValue()){
                System.out.print("\tDocument ID: " + document.getId() +
                        "\n\tDestination -> ");
                printLocation(document.getDestination());
                //System.out.println();
            }
        }

    }

    private void printLocation(Location location){
        System.out.println("\tAddress: " + location.getAddress() +
                "\t\tContact Name: " + location.getContactName() +
                "\t\tPhone Number: " + location.getPhoneNumber());
    }

    private void printItem(Item item){
        System.out.println("\t\tItem Code: " + item.getCode() +
                "\t\tName: " + item.getName() +
                "\t\tAmount: " + item.getAmount() +
                "\t\tWeight: " + item.getItemWeight());
    }
}
