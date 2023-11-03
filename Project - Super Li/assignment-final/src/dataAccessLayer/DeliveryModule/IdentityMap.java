package dataAccessLayer.DeliveryModule;

import businessLayer.deliveryPackage.Delivery;
import businessLayer.deliveryPackage.Document;
import businessLayer.deliveryPackage.Driver;
import businessLayer.deliveryPackage.Truck;

import java.util.HashMap;
import java.util.Map;

public class IdentityMap {
    private static IdentityMap instance = null;
    private Map<String, Truck> trucks; // plateNumber, truck
    private Map<String, Driver> drivers; // id, driver
    private Map<Integer, Document> documents; // id, document
    private Map<Integer, Delivery> deliveries; // id, delivery

    private IdentityMap() {
        trucks = new HashMap<>();
        drivers = new HashMap<>();
        documents = new HashMap<>();
        deliveries = new HashMap<>();
    }

    public static IdentityMap getInstance() {
        if (instance == null)
            instance = new IdentityMap();
        return instance;
    }

    public Map<String, Truck> getTrucks() {
        return trucks;
    }

    public void setTrucks(Map<String, Truck> trucks) {
        this.trucks = trucks;
    }

    public Map<String, Driver> getDrivers() {
        return drivers;
    }

    public void setDrivers(Map<String, Driver> drivers) {
        this.drivers = drivers;
    }

    public Map<Integer, Document> getDocuments() {
        return documents;
    }

    public void setDocuments(Map<Integer, Document> documents) {
        this.documents = documents;
    }

    public Map<Integer, Delivery> getDeliveries() {
        return deliveries;
    }

    public void setDeliveries(Map<Integer, Delivery> deliveries) {
        this.deliveries = deliveries;
    }
}
