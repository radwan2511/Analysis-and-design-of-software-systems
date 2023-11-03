package businessLayer.deliveryPackage.Controllers;

import businessLayer.deliveryPackage.Driver;
import dataAccessLayer.DalController;

import java.util.*;

public class DriverController {
    //private Map<String, Driver> data; // id, driver
    private DalController data;

    public DriverController() throws Exception {
        //data = new HashMap<>();
        data = DalController.getInstance();
    }

    public Driver getAvailableDriver (Date date, String shift, double weight) throws Exception{
        if (weight < 0)
            throw new Exception("Illegal weight");
        try {
            return data.getAvailableDriver(date, shift, weight);
        } catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }

    public List<String> getAllDriversIDs() throws Exception{
        try{
            return data.getAllDriversIDs();
        } catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }

    public List<String> getDriversByLicense(String license) throws Exception{
        try{
            return data.getDriversByLicense(license);
        } catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }
}
