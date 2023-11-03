package businessLayer.deliveryPackage;

import businessLayer.deliveryPackage.Controllers.DeliveryController;
import businessLayer.deliveryPackage.Controllers.DriverController;
import businessLayer.deliveryPackage.Controllers.TruckController;
import dataAccessLayer.DalController;

import java.math.BigInteger;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

public class Facade {
    private static Facade facade = null;
    private DeliveryController deliveryController;
    private DriverController driverController;
    private TruckController truckController;

    private Facade() throws Exception {
        deliveryController = new DeliveryController();
        driverController = new DriverController();
        truckController = new TruckController();
    }

    public static Facade getInstance() throws Exception {
        if(facade == null){
            facade = new Facade();
        }
        return facade;
    }

    // ------------------ Delivery ------------------
//    public Response<Delivery> getDelivery(Date date, String launchTime, String plateNumber){
//        try{
//            return new Response<>(deliveryController.getDelivery(date, launchTime, plateNumber));
//        } catch (Exception e){
//            return new Response<>(e.getMessage());
//        }
//    }

    public Response<Delivery> getDelivery(int deliveryId){
        try{
            return new Response<>(deliveryController.getDelivery(deliveryId));
        } catch (Exception e){
            return new Response<>(e.getMessage());
        }
    }

    public Response<List<Delivery>> getAllDeliveries(){
        try{
            return new Response<>(deliveryController.getAllDeliveries());
        } catch (Exception e){
            return new Response<>(e.getMessage());
        }
    }

    // ????????????????????? delete this fun ???????????????????????????
//    public Response addDelivery(Date date, String launchTime, Location source, Map<Location, LinkedList<Item>> items){
//        try {
//            double itemsWeight = 0;
//            for(LinkedList<Item> list : items.values()){
//                itemsWeight += Item.calculateWeight(list);
//            }
//            String shift = convertTimeToShift(launchTime);
//            //System.out.println("0");
//            deliveryController.checkIfShiftExist(shift, date);
//            //System.out.println("1");
//            deliveryController.checkStoreKeeperInShift(shift, date);
//            //System.out.println("2");
//            Truck truck = truckController.getAvailableTruck(date, shift, itemsWeight);
//            //System.out.println("3");
//            double totalWeight = itemsWeight + truck.getNetWeight();
//            Driver driver = driverController.getAvailableDriver(date, shift, totalWeight);
//            //System.out.println("4");
//            Delivery delivery = deliveryController.addDelivery(date, launchTime, truck.getPlateNumber(), driver.getId(), source, items, totalWeight);
//            //System.out.println("5");
//            deliveryController.addTruckScheduler(truck.getPlateNumber(), date, shift);
//            //System.out.println("6");
//            return new Response();
//        } catch (Exception e){
//            return new Response(e.getMessage());
//        }
//    }

//    public Response<Delivery> addDelivery(Date orderDate, Location source, Map<Location, LinkedList<Item>> items, String workDays){
//        try {
//            double itemsWeight = 0;
//            for(LinkedList<Item> list : items.values()){
//                itemsWeight += calculateWeight(list);
//            }
//            List<Date> dates = getDatesInNextWeek(orderDate);
//            ArrayList<String> shifts = new ArrayList<>();
//            shifts.add("morning");
//            shifts.add("evening");
//            for(Date date : dates){
//                if(isWorkDay(date, workDays)){
//                    for(String shift : shifts){
//                        try {
//                            deliveryController.checkIfShiftExist(shift, date);
//                            deliveryController.checkStoreKeeperInShift(shift, date);
//                            Truck truck = truckController.getAvailableTruck(date, shift, itemsWeight);
//                            double totalWeight = itemsWeight + truck.getNetWeight();
//                            Driver driver = driverController.getAvailableDriver(date, shift, totalWeight);
//                            String launchTime = generateLaunchTime(shift);
//                            Delivery delivery = deliveryController.addDelivery(date, launchTime, truck.getPlateNumber(), driver.getId(), source, items, totalWeight);
//                            deliveryController.addTruckScheduler(truck.getPlateNumber(), date, shift);
//                            return new Response<>(delivery);
//                        }catch (Exception e) { }
//                    }
//                }
//            }
//            // ?????????? add a notification to DB ??????????
//            throw new Exception("The order was rejected");
//
//        } catch (Exception e){
//            return new Response<>(e.getMessage());
//        }
//    }
public Response<Map<Integer, Date>> addDelivery(int orderId, Date orderDate, Location store, Map<Location, LinkedList<Item>> items, String workDays){
    try {
        double itemsWeight = 0;
        for(LinkedList<Item> list : items.values()){
                itemsWeight += calculateWeight(list);
        }
        List<Date> dates = getDatesInNextWeek(orderDate);
        ArrayList<String> shifts = new ArrayList<>();
        shifts.add("morning");
        shifts.add("evening");
        List<String> messages = new LinkedList<>();
        double totalWeight = 0;
        for(Date date : dates){
            if(isWorkDay(date, workDays)) {
                for(String shift : shifts){
                    try {
                        deliveryController.checkIfShiftExist(shift, date);
                        deliveryController.checkStoreKeeperInShift(shift, date);
                        Truck truck = truckController.getAvailableTruck(date, shift, itemsWeight);
                        totalWeight = itemsWeight + truck.getNetWeight();
                        Driver driver = driverController.getAvailableDriver(date, shift, totalWeight);
                        String launchTime = generateLaunchTime(shift);
                        Delivery delivery = deliveryController.addDelivery(date, launchTime, truck.getPlateNumber(), driver.getId(), store, items, totalWeight);
                        deliveryController.addTruckScheduler(truck.getPlateNumber(), date, shift);
                        Map<Integer, Date> out = new HashMap<>();
                        out.put(delivery.getId(), date);
                        return new Response<>(out);
                    }catch (Exception e) {
                        if(e.getMessage().equals("There is no available driver")){
                            messages.add("Date: " + date + ", Shift: " + shift + ", I need a driver from these: " + properDrivers(totalWeight));
                        }
                    }
                }
            }
        }
        // ?????????? add a notification to DB ??????????
        for(String msg : messages){
            deliveryController.addMessage("manager", msg, orderId);
        }
        throw new Exception("The order was rejected");

    } catch (Exception e){
        return new Response<>(e.getMessage());
    }
}

    private String properDrivers(double totalWeight) throws Exception{
        String out = "";
        List<String> ids;
        if(totalWeight <= 12000) // license C and C1
            ids = driverController.getAllDriversIDs();
        else // license C
            ids = driverController.getDriversByLicense("C");

        for(String id : ids){
            out = out + id + ". ";
        }
        return out;
    }
    private double calculateWeight(List<Item> items){
        double weight = 0;
        for(Item item : items){
            weight += item.getTotalWeight();
        }
        return weight;
    }

    private List<Date> getDatesInNextWeek(Date dueDate) {
        List<Date> dates = new LinkedList<>();
        long ONE_DAY_MILLI_SECONDS = 24 * 60 * 60 * 1000;
        Date nextDay;
        for (int i = 0; i < 7; i++) {
            nextDay = new Date(dueDate.getTime() + (i) * ONE_DAY_MILLI_SECONDS);
            dates.add(nextDay);
        }
        return dates;
    }

    private boolean isWorkDay(Date date, String workDays){
        if(workDays.isEmpty())
            return true;
        List<String> days = Arrays.asList(workDays.split(", "));
        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        String dayOfWeek = localDate.getDayOfWeek().toString();
        return days.contains(dayOfWeek);
    }

    private String generateLaunchTime(String shift) {
        String hours, minutes;
        Random rand = new Random();
        minutes = ((int) (Math.random() * 60)) + "";
        if (shift.equals("morning")) // morning : 7:00 - 14:59
            hours = (rand.nextInt((14 - 7) + 1) + 7) + "";
        else // evening : 15:00 - 23:00
            hours = (rand.nextInt((22 - 15) + 1) + 15) + "";
        if (hours.length() == 1)
            hours = "0" + hours;
        if (minutes.length() == 1)
            minutes = "0" + minutes;
        return hours + ":" + minutes;
    }


//    public Response<Delivery> cancelDelivery(Date date, String launchTime, String truckPlateNumber){
//        try {
//            Delivery delivery = deliveryController.cancelDelivery(date, launchTime, truckPlateNumber);
//            return new Response<>(delivery);
//        } catch (Exception e){
//            return new Response<>(e.getMessage());
//        }
//    }

    public Response<Delivery> cancelDelivery(int deliveryId){
        try {
            Delivery delivery = deliveryController.cancelDelivery(deliveryId);
            return new Response<>(delivery);
        } catch (Exception e){
            return new Response<>(e.getMessage());
        }
    }

    public Response<Map<Location, LinkedList<Item>>> getDeliveryItems(int deliveryId){
        try {
            Map<Location, LinkedList<Item>> itemsPerDest = deliveryController.getDeliveryItems(deliveryId);
            return new Response<>(itemsPerDest);
        } catch (Exception e){
            return new Response<>(e.getMessage());
        }
    }

    public Response<Map<Delivery, Document>> getDocument(int documentId){
        try {
            return new Response<>(deliveryController.getDocument(documentId));
        } catch (Exception e){
            return new Response<>(e.getMessage());
        }
    }

    public Response<List<Document>> getDeliveryDocuments(int deliveryId){
        try {
            return new Response<>(deliveryController.getDeliveryDocuments(deliveryId));
        } catch (Exception e){
            return new Response<>(e.getMessage());
        }
    }

    private String convertTimeToShift(String time){
        String[] split = time.split(":");
        try{
            if(Integer.parseInt(split[0]) < 15)
                return "morning";
            else
                return "evening";
        } catch (Exception e){

        }
        return "";
    }

//    public Item newItem(String name, double amount, double weight) throws Exception{
//        if(name.length() == 0 || amount <= 0 || weight <= 0)
//            throw new Exception("Invalid Item's details");
//        return new Item(name, amount, weight);
//    }

//    public Response<Location> newLocation(String address, String phoneNumber, String contactName) throws Exception{
//        try {
//            if(address == null || address.length() == 0 || phoneNumber == null || phoneNumber.length() != 10 || contactName == null || contactName.length() == 0)
//                throw new Exception("Invalid input");
//            try {
//                new BigInteger(phoneNumber); // check if this is a number (not contains characters)
//            }catch (Exception e){
//                throw new Exception("Invalid phone number");
//            }
//            return new Response<>(new Location(address, phoneNumber, contactName));
//        }catch (Exception e){
//            return new Response<>(e.getMessage());
//        }
//    }

    // ------------------ Truck ------------------
    public Response<Truck> getTruck(String plateNumber){
        try {
            return new Response<>(truckController.getTruck(plateNumber));
        } catch (Exception e){
            return new Response<>(e.getMessage());
        }
    }

    public Response<List<Truck>> getAllTrucks(){
        try{
            return new Response<>(truckController.getAllTrucks());
        }catch (Exception e){
            return new Response<>(e.getMessage());
        }
    }

    public Response addTruck(String plateNumber, String model, double netWeight, double maxWeight){
        try {
            truckController.addTruck(plateNumber, model, netWeight, maxWeight);
            return new Response();
        }catch (Exception e){
            return new Response(e.getMessage());
        }
    }

    public Response<Truck> deleteTruck(String plateNumber){
        try {
            Truck truck = truckController.deleteTruck(plateNumber);
            return new Response<>(truck);
        } catch (Exception e){
            return new Response<>(e.getMessage());
        }
    }

}
