package businessLayer;

import businessLayer.Sup_Inv.GeneralProduct;
import businessLayer.Sup_Inv.Supplier;
import businessLayer.deliveryPackage.Delivery;
import businessLayer.deliveryPackage.Facade;
import businessLayer.deliveryPackage.Item;
import businessLayer.deliveryPackage.Location;
import businessLayer.deliveryPackage.Response;

import java.util.*;

public class API {

    Facade deliveryFacade;
//    // constructor
    public API(){
        try {
            deliveryFacade = Facade.getInstance();
        } catch (Exception e){}
    }
//    // supAddress is the destination, and the store is the source
//    // make a function that returns the source (store address)
//
    public Map<Integer, Date> addDelivery(int orderId, Date orderDate, Supplier supp, List<GeneralProduct> products) throws Exception {
        Location store = new Location("Super_Lee", "0521452365", "Guy");
        Location supplier = new Location(supp.getAddress(), supp.getPhoneNumber(), supp.getName());
        String workDays = supp.getWorkDays();
        Map<Location, LinkedList<Item>> itemsPerSup = new HashMap<>();
        itemsPerSup.put(supplier, buildItems(products));
        Response<Map<Integer, Date>> response = deliveryFacade.addDelivery(orderId, orderDate, store, itemsPerSup, workDays);
        if(response.errorOccurred())
            throw new Exception(response.getMsg());
        return response.getValue();
    }

//    public int addDelivery(Date date, String supAddress, String phoneNumber, String contactName, LinkedList<Product> products, String workDays){
//        // make source : Location
//        // make dest : Location
//        // convert product Object to item Objets
//        // make map<dest, List<Items>
//        // call addDelivery in facade
//        // return deliveryId
//    }

    public void cancelDelivery(Set<Integer> deliveryIDs) throws Exception {
        for(int deliveryId : deliveryIDs){
            Response<Delivery> response = deliveryFacade.cancelDelivery(deliveryId);
            if(response.errorOccurred())
                throw new Exception(response.getMsg());
        }
    }

    public Map<Integer, Integer> getDeliveryItems(int deliveryId) throws Exception { // <code, quantity>
        Response<Map<Location, LinkedList<Item>>> response = deliveryFacade.getDeliveryItems(deliveryId);
        if(response.errorOccurred())
            throw new Exception(response.getMsg());
        LinkedList<Item> items = response.getValue().values().iterator().next();
        Map<Integer, Integer> code_quantity = new HashMap<>();
        for (Item item : items){
            code_quantity.put(item.getCode(), item.getAmount());
        }
        return code_quantity;
    }

    private LinkedList<Item> buildItems(List<GeneralProduct> products){
        LinkedList<Item> items = new LinkedList<>();
        for(GeneralProduct p : products){
            items.add(new Item(p.getCode(), p.getName(), p.getQuantity(), p.getWeight()));
        }
        return items;
    }
}
