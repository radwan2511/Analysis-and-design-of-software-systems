package businessLayer.deliveryPackage.Controllers;

import businessLayer.deliveryPackage.Delivery;
import businessLayer.deliveryPackage.Document;
import businessLayer.deliveryPackage.Item;
import businessLayer.deliveryPackage.Location;
import dataAccessLayer.DalController;

import java.util.*;

public class DeliveryController {
    //private Map<Integer, Delivery> data; // delivery id, Delivery
    private DalController data;
    private Map<Integer, Document> documentsData; // document id, Document
    private int deliveryId;
    private int documentId;

    public DeliveryController() throws Exception {
        //data = new HashMap<>();
        data = DalController.getInstance();
        documentsData = new HashMap<>();
        this.deliveryId = 10000;
        this.documentId = 1000;
    }

//    public Delivery getDelivery(Date date, String launchTime, String truckPlateNumber) throws Exception{
//        if(date == null || launchTime == null || truckPlateNumber == null || launchTime.length() == 0 || truckPlateNumber.length() == 0)
//            throw new Exception("null or empty input");
//        try{
//            return data.getDelivery(date, launchTime, truckPlateNumber);
//        } catch (Exception e){
//            throw new Exception(e.getMessage());
//        }
//    }

    public Delivery getDelivery(int deliveryId) throws Exception{
//        if(date == null || launchTime == null || truckPlateNumber == null || launchTime.length() == 0 || truckPlateNumber.length() == 0)
//            throw new Exception("null or empty input");
        try{
            return data.getDelivery(deliveryId);
        } catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }

    public List<Delivery> getAllDeliveries() throws Exception {
        try {
            return data.getAllDeliveries();
        } catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }

    public Delivery addDelivery(Date date, String launchTime, String truckPlateNumber, String driverId, Location source, Map<Location, LinkedList<Item>> items, double totalWeight) throws Exception{
        if(date.before(new Date()))
            throw new Exception("Invalid date!");
        if(launchTime == null || launchTime.length() == 0 || truckPlateNumber == null || truckPlateNumber.length() == 0 ||
                driverId == null || driverId.length() == 0 || source == null || items.isEmpty())
            throw new Exception("null or empty input");
        Delivery d = null;
        try{
//            Delivery delivery = null;
//            try {
//                delivery = data.getDelivery(date, launchTime, truckPlateNumber);
//            } catch (Exception e){
//            }
//            if(delivery != null)
//                throw new Exception("This delivery date, launch time and plate number are already exist");

            //System.out.println("addDelivery");

            d = data.addDelivery(deliveryId, date, launchTime, truckPlateNumber, driverId, source, items);
            for(Location location : items.keySet()){
                d.addDocument(documentId, location, items.get(location), totalWeight);
                data.addDocument(documentId, deliveryId,location, items.get(location), totalWeight);
            }
            deliveryId += 1;
        } catch (Exception e){
            throw new Exception(e.getMessage());
        }
        return d;
    }

//    public Delivery cancelDelivery(Date date, String launchTime, String truckPlateNumber) throws Exception{
//        if(date.before(new Date()))
//            throw new Exception("Invalid date!");
//        if(launchTime == null || launchTime.length() == 0 || truckPlateNumber == null || truckPlateNumber.length() == 0)
//            throw new Exception("null  or empty input");
//        try {
//            return data.cancelDelivery(date, launchTime, truckPlateNumber);
//        } catch (Exception e){
//            throw new Exception(e.getMessage());
//        }
//    }
    public Delivery cancelDelivery(int deliveryId) throws Exception{
        try {
            return data.cancelDelivery(deliveryId);
        } catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }

    public Map<Delivery, Document> getDocument(int documentId) throws Exception{
        try {
            return data.getDocument(documentId);
        } catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }

    public List<Document> getDeliveryDocuments(int deliveryId) throws Exception{
        try{
            return data.getDeliveryDocuments(deliveryId);
        } catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }

    public Map<Location, LinkedList<Item>> getDeliveryItems (int deliveryId) throws Exception {
        try{
            return data.getItemsPerDest(deliveryId);
        } catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }

    public boolean checkStoreKeeperInShift(String shiftTime, Date shiftDate) throws Exception {
        if(!data.checkStoreKeeperInShift(shiftTime, shiftDate))
            throw new Exception("There is no storekeeper in this shift");
        return true;
    }

    public boolean checkIfShiftExist(String shiftTime, Date shiftDate) throws Exception {
        if(!data.checkIfShiftExist(shiftTime, shiftDate))
            throw new Exception("There is no shift");
        return true;
    }

    public void addTruckScheduler(String plateNumber, Date date, String shift) throws Exception{
        try {
            data.addTruckScheduler(plateNumber, date, shift);
        }catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }

    public void addMessage(String recipient, String msg, int orderId) throws Exception{
        try {
            data.addMessage(recipient, msg, orderId);
        }catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }
}
