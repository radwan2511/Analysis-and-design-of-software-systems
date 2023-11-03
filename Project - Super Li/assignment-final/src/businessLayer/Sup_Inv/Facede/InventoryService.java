package businessLayer.Sup_Inv.Facede;

import businessLayer.Sup_Inv.Facede.Objects.Category;
import businessLayer.Sup_Inv.Facede.Objects.Order;
import businessLayer.Sup_Inv.InventoryController;

import java.util.*;


public class InventoryService {
    private InventoryController inventoryController;


    public InventoryService(){
        inventoryController = new InventoryController();
    }


    public ResponseT<List<Category>> printAllCategories() {
        try{
            List<businessLayer.Sup_Inv.Category> Bcategories = inventoryController.printAllCategories();
            List<Category> Scategories = new LinkedList<>();
            for (int i = 0 ;i<Bcategories.size();i++){
                Scategories.add(convertBcategory(Bcategories.get(i)));
            }
            return new ResponseT<>(Scategories);
        }catch (Exception e){
            return new ResponseT<>(e.getMessage());
        }
    }

    private Category convertBcategory(businessLayer.Sup_Inv.Category category) {
        if (category == null) return null;
        return new Category(category.getId(),category.getName(),category.getSupCategory());
    }

    public ResponseT<Category> searchCategory(String cat) {
        try{
            businessLayer.Sup_Inv.Category Bc = inventoryController.searchCategory(cat);
            Category Sc = convertBcategory(Bc);
            return new ResponseT<>(Sc);
        }catch (Exception e){
            return new ResponseT<>(e.getMessage());
        }
    }

    public ResponseT<Map<Integer, Category>> insertCategoryMenu(String name, String subStr) {
        try{
            Map<Integer, businessLayer.Sup_Inv.Category> m = inventoryController.insertCategoryMenu(name,subStr);
            Map<Integer, Category> res = new HashMap<>();
            for (Integer i : m.keySet()){
                res.put(i,convertBcategory(m.get(i)));
            }
            return new ResponseT<>(res);
        }catch (Exception e){
            return new ResponseT<>(e.getMessage());
        }
    }

    public ResponseT<String> issueReportMenu(String catString, int rep) {
        try{
            String r = inventoryController.report(catString,rep);
            return new ResponseT<>(r);
        }catch (Exception e){
            return new ResponseT<>(e.getMessage());
        }
    }

    /*public void addGeneralProduct(Scanner s) {
        inventoryController.addGeneralProduct(s);
    }*/
    public Response addGeneralProduct(String cat, int code, String name, double selling_price, String manufactor, int minQuantity, double weight) {
        try{
            inventoryController.addGeneralProduct(cat,code,name,selling_price,manufactor,minQuantity,weight);
            return new Response();
        }catch (Exception e){
            return new Response(e.getMessage());
        }
    }

    public Response addSpecificProducts(int code, String date) {
        try {
           inventoryController.addSpecificProducts(code,date);
            return new Response();
        }catch (Exception e){
            return new Response(e.getMessage());
        }
    }

    public Response nowFlaw(int id) {
        try{
            inventoryController.nowFlaw(id);
            return new Response();
        }catch (Exception e){
            return new Response(e.getMessage());
        }
    }

    public void printSales() throws Exception {
        inventoryController.printSales();
    }


    public Response addSaleByCategory(String cat, String date, String percentage) {
        try {
            inventoryController.addSaleByCategory(cat,date,percentage);
            return new Response();
        }catch (Exception e){
            return new Response(e.getMessage());
        }
    }

    public Response addSaleBySpecificOrder(String id, String date, String percentage) {
        try {
            inventoryController.addSaleBySpecificOrder(id,date,percentage);
            return new Response();
        }catch (Exception e){
            return new Response(e.getMessage());
        }
    }

    public ResponseT<ArrayList<Order>> checkMinQuantites() {
        try{
            ArrayList<businessLayer.Sup_Inv.Order> Borders = inventoryController.chekMinQuantites();
            ArrayList<Order> orders = new ArrayList<>();
            for (businessLayer.Sup_Inv.Order o : Borders){
                orders.add(new Order(o.getId(),o.getYear(),o.getMonth(),o.getDay(),o.getSupplier_id(),o.getStatus(),o.getPrice(),o.getCode_quantity(),o.getDelivery_ids()));
            }
            return new ResponseT<>(orders);
        }catch (Exception e){
            return new ResponseT<>(e.getMessage());
        }
    }
}
