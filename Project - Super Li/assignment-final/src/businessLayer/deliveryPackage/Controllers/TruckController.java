package businessLayer.deliveryPackage.Controllers;

import businessLayer.deliveryPackage.Truck;
import dataAccessLayer.DalController;

import java.util.*;

public class TruckController {
    //private Map<String, Truck> data; // plate number, Truck
    private DalController data;

    public TruckController() throws Exception {
        //data = new HashMap<>();
        data = DalController.getInstance();
    }

    public Truck getTruck(String plateNumber) throws Exception{
        if(plateNumber == null || plateNumber.length() == 0)
            throw new Exception("null or empty input");
        try {
            return data.getTruck(plateNumber);
        }catch (Exception e){
            throw new Exception(e.getMessage());
        }

    }

    public List<Truck> getAllTrucks() throws Exception{
        try {
            return data.getAllTrucks();
        } catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }

    public void addTruck(String plateNumber, String model, double netWeight, double maxWeight) throws Exception{
        if(plateNumber == null || plateNumber.length() == 0 || model == null || model.length() == 0)
            throw new Exception("null or empty input");
        if(netWeight <= 0 || maxWeight <= 0 || netWeight >= maxWeight)
            throw new Exception("invalid weight or net weight >= max weight");
        try {
            data.addTruck(plateNumber, model, netWeight, maxWeight);
        }catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }

    public Truck deleteTruck(String plateNumber) throws Exception{
        if(plateNumber == null || plateNumber.length() == 0)
            throw new Exception("null or empty input");
        try {
            return data.deleteTruck(plateNumber);
        }catch (Exception e){
            throw new Exception(e.getMessage());
        }

    }

    public Truck getAvailableTruck(Date date, String shift, double weight) throws Exception{
        if (weight < 0)
            throw new Exception("Illegal weight");
        try {
            return data.getAvailableTruck(date, shift, weight);
        } catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }

}
