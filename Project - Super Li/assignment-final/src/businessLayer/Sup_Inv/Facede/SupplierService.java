package businessLayer.Sup_Inv.Facede;

//import BusinessLayer.Supplier;

import businessLayer.Sup_Inv.ContractController;
import businessLayer.Sup_Inv.Facede.Objects.*;
import businessLayer.Sup_Inv.OrderController;
import businessLayer.Sup_Inv.SupplierController;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Map;

public class SupplierService {
    private static SupplierController supplierController;
    private static ContractController contractController;
    private static OrderController orderController;

    public SupplierService() throws Exception {
        supplierController = SupplierController.getInstance();
        contractController = new ContractController();
        orderController = OrderController.getInstance();
    }



    /*public static void supplierActions(Scanner s) throws SQLException {
        supplierController.supplierActions(s);
    }*/

    /*public static void contractAction(Scanner s) throws SQLException {
        //ContractController c1=new ContractController();
        contractController.contractAction(s);
    }*/

    /*public static void orderAction(Scanner s) throws SQLException {
        orderController.orderAction(s);
    }*/

    public Response AddSupplier(String name ,String address, String PhoneNumber, int type, String bank, int companyNum){
        try {
            supplierController.AddSupplier(name,address,PhoneNumber,type,bank, companyNum);
            return new Response();
        }catch (Exception e){
            return new Response(e.getMessage());
        }
    }

    public ResponseT<ArrayList<Supplier>> printSupplier() {
        try {
             ArrayList<businessLayer.Sup_Inv.Supplier> suppliersB = supplierController.printSuppliers();
             ArrayList<Supplier> Ssuppliers = new ArrayList<>();
             for (businessLayer.Sup_Inv.Supplier s : suppliersB){
                 Ssuppliers.add(convertBsupplier(s));
             }
            return new ResponseT<>(Ssuppliers);
        }catch (Exception e){
            return new ResponseT<>(e.getMessage());
        }
    }


//    public Response AddOrder(int supplier_id, int product_code, Date date, int quantity){
//        try {
//            orderController.addOrder(supplier_id,product_code,date,quantity);
//            return new Response();
//        }catch (Exception e){
//            return new Response(e.getMessage());
//        }
//    }

    public ResponseT<ArrayList<Order>> DeleteSupplier(int supplier_id){
        try{
            ArrayList<businessLayer.Sup_Inv.Order> Borders = orderController.DeleteSupplierCancelAllOrders(supplier_id);
            ArrayList<Order> Sorders = new ArrayList<>();
            for (businessLayer.Sup_Inv.Order o : Borders){
                Sorders.add(convertBorder(o));
            }
            supplierController.DeleteSupplier(supplier_id);
            contractController.DeleteSupplierContract(supplier_id);
            return new ResponseT<>(Sorders);
        }catch (Exception e){
            return new ResponseT<>(e.getMessage());
        }
    }

    public Response CancelOrder(int id){
        try{
            orderController.CancelOrder(id);
            return new Response();
        }catch (Exception e){
            return new Response(e.getMessage());
        }
    }

    public Response MakeContract(int supplier_id,int contractNum, boolean constant_day,Map<Integer,Map<Integer,Integer>> QuantityDiscountPerProduct,Map<Integer,Double> price){
        try{
            contractController.makeContract(supplier_id,contractNum,constant_day, QuantityDiscountPerProduct,price);
            return new Response();
        }catch (Exception e){
            return new Response(e.getMessage());
        }
    }

    public Response DeleteContract(int supplier_id){
        try{
            contractController.DeleteContract(supplier_id);
            return new Response();
        }catch (Exception e){
            return new Response(e.getMessage());
        }
    }

    public ResponseT<ArrayList<Contract>> viewProductsContract() {
        try {
            ArrayList<businessLayer.Sup_Inv.Contract> contracts = contractController.viewProductsContract();
            ArrayList<Contract> Scontracts = new ArrayList<>();
            for (businessLayer.Sup_Inv.Contract c : contracts){
                Scontracts.add(convertContract(c));
            }
            return new ResponseT<>(Scontracts);
        }catch (Exception e){
            return new ResponseT<>(e.getMessage());
        }
    }

//    public Response AddContact(int supplier_id,String name,String phoneNumber,String address){
//        try{
//            supplierController.AddContact(supplier_id,name,phoneNumber,address);
//            return new Response();
//        }catch (Exception e){
//            return new Response(e.getMessage());
//        }
//    }

    /*public Response AddProduct(String name, double price,double weight){
        try{
            supplierController.AddProduct(name,price,weight);
            return new Response();
        }catch (Exception e){
            return new Response(e.getMessage());
        }
    }*/

    /*public Response AddCompany(String address,String comp){
        try{
            supplierController.AddCompany(address,comp);
            return new Response();
        }catch (Exception e){
            return new Response(e.getMessage());
        }
    }*/

    public Response setWorkDays(int supplier_id, int [] days){
        try{
            supplierController.setWorkDays(supplier_id,days);
            return new Response();
        }catch (Exception e){
            return new Response(e.getMessage());
        }
    }

    public ResponseT<ArrayList<Order>> HandleArrivedOrders() {
        try{
            ArrayList<businessLayer.Sup_Inv.Order> Borders = orderController.HandleArrivedOrders();
            ArrayList<Order> orders = new ArrayList<>();
            for (businessLayer.Sup_Inv.Order o : Borders){
                orders.add(convertBorder(o));
            }
            return new ResponseT<>(orders);
        }catch (Exception e){
            return new ResponseT<>(e.getMessage());
        }
    }

    /*public Response addOrder(int year, int month, int day, int code, int quantity) {
        try{
            orderController.addOrder(year,month,day,code,quantity);
            return new Response();
        }catch (Exception e){
            return new Response(e.getMessage());
        }
    }*/

    public Response addComplexOrder(Map<Integer, Integer> code_quantity, int year, int month, int day) {
        try{
            orderController.addComplexOrder(0,code_quantity,year,month,day);
            return new Response();
        }catch (Exception e){
            return new Response(e.getMessage());
        }
    }


    public Response addOrder(int id) {
        try{
            orderController.addOrderFromID(id);
            return new Response();
        }catch (Exception e){
            return new Response(e.getMessage());
        }
    }

    public ResponseT<String> AddConstantOrder(int supplierId,Map<Integer,Integer> code_quantity) {
        try{
            ArrayList<Integer> r = supplierController.AddConstantOrder(supplierId,code_quantity);
            LocalDateTime now = LocalDateTime.now();
            String str = "";
            if(r.contains(now.getDayOfWeek().getValue() + 2)){
                str = str + "Dear Supplier, remind you that until tomorrow the Order should be ready, the orderd products are: \n";
                int i = 1;
                for (Integer code : code_quantity.keySet() ){
                    str  = str + i + " - product code: " +code + " --------- " + "quantity "+ code_quantity.get(code) + "\n";
                }
            }
            if(r.contains(now.getDayOfWeek().getValue() + 1)){
                orderController.addOrder(supplierId,code_quantity,now.getYear(),now.getMonthValue(),now.getDayOfWeek().getValue() + 1);
            }
            //orderController.addComplexOrder(code_quantity,now.getYear(),now.getMonthValue(),now.getDayOfWeek().getValue() + 1);
            return new ResponseT<>(str);
        }catch (Exception e){
            return new ResponseT<>(e.getMessage());
        }
    }

    public Response ChangeStatus(int id){
        try{
            orderController.ChangeStatus(id);
            return new Response();
        }catch (Exception e){
            return new Response(e.getMessage());
        }
    }

    public ResponseT<ArrayList<Supplier>> GetAllSuppliers(){
        try {
            ArrayList<businessLayer.Sup_Inv.Supplier> Bsuppliers = supplierController.GetAllSuppliers();
            ArrayList<Supplier> Ssuppliers = new ArrayList<>();
            for(businessLayer.Sup_Inv.Supplier s : Bsuppliers){
                Ssuppliers.add(convertBsupplier(s));
            }
            return new ResponseT<ArrayList<Supplier>> (Ssuppliers);
        }catch (Exception e){
            return new ResponseT<>(e.getMessage());
        }
    }

    public ResponseT<Supplier> GetSupplier(int supplier_id){
        try {
            businessLayer.Sup_Inv.Supplier bs = supplierController.getSupplier(supplier_id);
            Supplier ss = convertBsupplier(bs);
            return new ResponseT<>(ss);
        }catch (Exception e){
            return new ResponseT<>(e.getMessage());
        }
    }

    public ResponseT<ArrayList<Contract>> getContract(int supplier_id){
        try{
            ArrayList<businessLayer.Sup_Inv.Contract> Bcontract = contractController.getContract(supplier_id);
            ArrayList<Contract> Scontract = new ArrayList<>();
            if(Bcontract== null){
                throw new IllegalArgumentException("the supplier didn't assign contract yet");
            }
            for (businessLayer.Sup_Inv.Contract c: Bcontract){
                Scontract.add(convertContract(c));
            }
            return new ResponseT<>(Scontract);
        }catch (Exception e){
            return new ResponseT<>(e.getMessage());
        }
    }

    public ResponseT<ArrayList<Order>> DayOrder(int year, int month, int day) {
        try{
            ArrayList<businessLayer.Sup_Inv.Order> Borders = orderController.DayOrder(year,month,day);
            ArrayList<Order> Sorders = new ArrayList<>();
            for (businessLayer.Sup_Inv.Order order : Borders){
                Sorders.add(convertBorder(order));
            }
            return new ResponseT<>(Sorders);
        }catch (Exception e){
            return new ResponseT<>(e.getMessage());
        }
    }

    public ResponseT<ArrayList<Order>> SupplierOrder(int id) {
        try {
            ArrayList<businessLayer.Sup_Inv.Order> Borders = orderController.SupplierOrder(id);
            ArrayList<Order> Sorders = new ArrayList<>();
            for (businessLayer.Sup_Inv.Order order : Borders){
                Sorders.add(convertBorder(order));
            }
            return new ResponseT<>(Sorders);
        }catch (Exception e){
            return new ResponseT<>(e.getMessage());
        }
    }
    public ResponseT<ArrayList<Order>> printOrder() {
        try {
            ArrayList<businessLayer.Sup_Inv.Order> Borders = orderController.getOrders();
            ArrayList<Order> Sorders = new ArrayList<>();
            for (businessLayer.Sup_Inv.Order order : Borders){
                Sorders.add(convertBorder(order));
            }
            return new ResponseT<>(Sorders);
        }catch (Exception e){
            return new ResponseT<>(e.getMessage());
        }
    }

//    public ResponseT<ArrayList<Order>> getFinishedOrders(int supplier_id){
//        try {
//            ArrayList<BusinessLayer.Order> Borders = orderController.getFinishedOrders(supplier_id);
//            ArrayList<Order> Sorders = new ArrayList<>();
//            for (BusinessLayer.Order order : Borders){
//                Sorders.add(convertBorder(order));
//            }
//            return new ResponseT<>(Sorders);
//        }catch (Exception e){
//            return new ResponseT<>(e.getMessage());
//        }
//    }

//    public ResponseT<Order> showOrder(int supplier_id,int order_id){
//        try{
//            BusinessLayer.Order Border = orderController.showOrder(supplier_id,order_id);
//            Order Sorder = convertBorder(Border);
//            return new ResponseT<>(Sorder);
//        }catch (Exception e){
//            return new ResponseT<>(e.getMessage());
//        }
//    }

    // ============================================================================================//
    private Supplier convertBsupplier(businessLayer.Sup_Inv.Supplier supplier){
        return new Supplier(supplier.getId(),supplier.getName(),supplier.getAddress(),
                supplier.getPhoneNumber(),supplier.getSupplierType(),supplier.getBank(),
                supplier.getTermOfPayment(),supplier.getWorkDays());
               // supplier.getContract_id());
    }

    private Contract convertContract(businessLayer.Sup_Inv.Contract contract){
        if (contract == null) return null;
        return new Contract(contract.getSupplierId(),contract.getContractNum(),
                contract.getConstantDay(),contract.getProduct_code(),contract.getQuantity_discount(),contract.getPrice());
    }

    private Product convertBproduct(businessLayer.Sup_Inv.Product product){
        return new Product(product.getId(),product.getName(),product.getListPrice(),product.getWeight());
    }

    private Contact convertBcontact(businessLayer.Sup_Inv.Contact contact){
        return new Contact(contact.getId(),contact.getName(),contact.getPhoneNumber(),contact.getAddress());
    }


    private Order convertBorder(businessLayer.Sup_Inv.Order order){
        return new Order(order.getId(),order.getYear(),order.getMonth(),order.getDay(),order.getSupplier_id(),order.getStatus(),order.getPrice(),order.getCode_quantity(),order.getDelivery_ids());
    }


}
