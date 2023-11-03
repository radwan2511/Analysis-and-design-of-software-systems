package businessLayer.deliveryPackage;

import java.util.*;

public class Delivery {
    private int id;
    private String date;
    private String launchTime;
    private String truckPlateNumber;
    private String driverId;
    private Location source;
    //private LinkedList<Location> destinations; // ??? maybe we don't need this
    private Map<Location, LinkedList<Item>> itemsPerDest;
    private double itemsWeight;
    private Map<Integer, Document> documents; // document id, Document

    public Delivery(int id ,String  date, String launchTime, String truckPlateNumber, String driverId, Location source, Map<Location, LinkedList<Item>> items){
        this.id = id;
        this.date = date;
        this.launchTime = launchTime;
        this.truckPlateNumber = truckPlateNumber;
        this.driverId = driverId;
        this.source = source;
        this.itemsPerDest = items;
        itemsWeight = itemsWeight();
        documents = new HashMap<>();
    }

    private double itemsWeight(){
        double weight = 0;
        for (LinkedList<Item> list : itemsPerDest.values()){
            for(Item item : list){
                weight += item.getTotalWeight();
            }
        }
        return weight;
    }

    public int getId(){
        return id;
    }
    public String getDate(){
        return date;
    }

    public String getLaunchTime(){
        return launchTime;
    }

    public String getTruckPlateNumber(){
        return truckPlateNumber;
    }

    public String getDriverId(){
        return driverId;
    }

    public Location getSource(){
        return source;
    }

    public Collection<Location> getDestinations(){
        return itemsPerDest.keySet();
    }

    public Map<Location, LinkedList<Item>> getItemsPerDest(){
        return itemsPerDest;
    }

    public double getItemsWeight(){
        return itemsWeight;
    }

    public Map<Integer, Document> getDocuments(){
        return documents;
    }

    public Document addDocument(int documentId, Location dest, LinkedList<Item> items, double totalWeight){
        Document document = new Document(documentId, this.id, dest, items, totalWeight);
        documents.put(id, document);
        return document;
    }
}
