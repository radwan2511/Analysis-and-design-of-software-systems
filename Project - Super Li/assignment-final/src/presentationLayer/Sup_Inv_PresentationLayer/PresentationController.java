package presentationLayer.Sup_Inv_PresentationLayer;

import businessLayer.Sup_Inv.Facede.Objects.*;
import businessLayer.Sup_Inv.Facede.Response;
import businessLayer.Sup_Inv.Facede.ResponseT;
import businessLayer.Sup_Inv.Facede.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PresentationController {
    private Service service;

    public PresentationController() throws Exception {
        service = new Service();
    }

    public  void printSuppliers() {
        ResponseT<ArrayList<Supplier>>suppliers = service.printSuppliers();
        for (Supplier s : suppliers.value){
            System.out.println(s.toString());
            System.out.println("-----------------");
        }
    }

    public void AddSupplier(String name ,String address, String PhoneNumber, int type, String bank, int companyNum){
        Response r = service.AddSupplier(name, address, PhoneNumber, type, bank, companyNum);
        if (r.ErrorOccured()){
            System.out.println("> Failed on adding the supplier : " + name + "- " + r.ErrorMessage);
        }
        else{
            System.out.println("> The supplier "+ name + " has been added successfully");
        }
    }

    public void AddConstantOrder(int supplierId, Map<Integer,Integer> code_quantity){
        ResponseT<String> r = service.AddConstantOrder(supplierId,code_quantity);
        if (r.ErrorOccured()){
            System.out.println(r.ErrorMessage);
        }else{
            System.out.println(r.value);
        }
    }

//    public void AddOrder(int supplier_id, int product_code, Date date, int quantity){
//         Response r =  service.AddOrder(supplier_id, product_code, date, quantity);
//         if(r.ErrorOccured()){
//             System.out.println("> Failed on adding the order : " + r.ErrorMessage);
//         }else{
//             System.out.println("> the order has been added successfully");
//         }
//    }

    public void DeleteSupplier(int supplier_id){
        ResponseT<ArrayList<Order>> r = service.DeleteSupplier(supplier_id);
        if(r.ErrorOccured()){
            System.out.println("> error occurred while deleting the supplier " + r.ErrorMessage);
        }else{
            System.out.println(">the supplier removed successfully" + "\n");
            for(Order o : r.value){
                System.out.println("Warning! the order:\n " + o.toString() + " is deleted because of deleting the supplier\n-------------------------------\n");
            }
        }
    }

    /*public void CancelOrder(int year,int month,int day){
        Response r = service.CancelOrder(year,month,day);
        if(r.ErrorOccured()){
            System.out.println("> error occurred while canceling the order " + r.ErrorMessage);
        }else{
            System.out.println(">the order canceled successfully");
        }
    }*/

    public void CancelOrder(int id){
        Response r = service.CancelOrder(id);
        if(r.ErrorOccured()){
            System.out.println("> error occurred while canceling the order " + r.ErrorMessage);
        }else{
            System.out.println(">the order canceled successfully");
        }
    }


    public void MakeContract(int supplier_id,int contractNum,boolean constantDay,Map<Integer,Map<Integer,Integer>> QuantityDiscountPerProduct,Map<Integer,Double> price){
        Response r = service.MakeContract(supplier_id, contractNum, constantDay, QuantityDiscountPerProduct,price);
        if(r.ErrorOccured()){
            System.out.println("> error occured on making the contract: " + r.ErrorMessage);
        }else{
            checkMinQuantities();
            System.out.println();
            System.out.println("> the contract " + contractNum + " is done successfully");
        }
    }

    public void DeleteContract(int supplpier_id){
        Response r = service.DeleteContract(supplpier_id);
        if(r.ErrorOccured()){
            System.out.println("> error occured on deleting the contract " + r.ErrorMessage);
        }else{
            System.out.println(">the contract with the supplier canceled");
        }
    }

    /*public void addOrder(int year, int month, int day, int code, int quantity) {
        Response r = service.addOrder(year,month,day,code,quantity);
        if(r.ErrorOccured()){
            System.out.println("> error occured on adding the order " + r.ErrorMessage);
        }else{
            System.out.println(">the order added successfully");
        }
    }*/

    public void addComplexOrder(Map<Integer, Integer> code_quantity, int year, int month, int day) {
        Response r = service.addComplexOrder(code_quantity,year,month,day);
        if(r.ErrorOccured()){
            System.out.println("> error occured on adding the order " + r.ErrorMessage);
        }else{
            System.out.println(">the order added successfully");
        }
    }

    public void addOrder(int id) {
        Response r = service.addOrder(id);
        if(r.ErrorOccured()){
            System.out.println("> error occured on adding the order " + r.ErrorMessage);
        }else{
            System.out.println(">the order added successfully");
        }
    }

//    public void AddContact(int supplier_id,String name, String phoneNumber, String address){
//        Response r = service.AddContact(supplier_id, name, phoneNumber, address);
//        if(r.ErrorOccured()){
//            System.out.println("> error occured while adding the contact " + r.ErrorMessage);
//        }else{
//            System.out.println("> the contact: " + name + ", "+ phoneNumber + ", " + address + " added successfully to the supplier ." );
//        }
//    }

    /*public void AddProduct(String name, double price,double weight){
        Response r = service.AddProduct(name, price, weight);
        if(r.ErrorOccured()){
            System.out.println("> error happened while adding the product " + name + " :" + r.ErrorMessage);
        }else{
            System.out.println("> the product: " + name +" added successfully");
        }
    }*/

    /*public void AddCompany(String address,String comp){
        Response r = service.AddCompany(address, comp);
        if(r.ErrorOccured()){
            System.out.println("> error happened while assigning the " + comp + " company: " + r.ErrorMessage);
        }else{
            System.out.println("> the " + comp + " company assigned to the supplier " + address + " successfully");
        }
    }*/

    public void setWorkDays(int supplier_id, int [] days){
        Response r = service.setWorkDays(supplier_id, days);
        if(r.ErrorOccured()){
            System.out.println("> error happened while setting the work days to the supplier\nthe error is: "+ r.ErrorMessage);
        }else{
            System.out.println("> the work days assigned succefully to the supplpier " );
        }
    }

    public void ChangeStatus(int id){
        Response r = service.ChangeStatus(id);
        if (r.ErrorOccured()){
            System.out.println("> error happened while changing the status of the order, the error is: " + r.ErrorMessage);
        }else{
            System.out.println("> the order is ready, let's get it:))");
        }
    }
    /*public void ChangeStatus(int year,int month,int day){
        Response r = service.ChangeStatus(year,month,day);
        if (r.ErrorOccured()){
            System.out.println("> error happened while changing the status of the order, the error is: " + r.ErrorMessage);
        }else{
            System.out.println("> the order is ready, let's get it:))");
        }
    }*/


    //------------------------------------Get All Suppliers---------------------------------------------------
    public void GetAllSuppliers(){
        ResponseT<ArrayList<Supplier>> r = service.GetAllSuppliers();
        if(r.ErrorOccured()){
            System.out.println("> error occured while getting the suppliers: " + r.ErrorMessage +"\n");
        }else{
            for (Supplier supplier: r.value){
                printSupplier(supplier);
            }
        }
    }


    private void printSupplier(Supplier supplier){
        String s = "";
        s = s + "\tsupplier id: "+ supplier.id + "\n\tsupplier name: " + supplier.name +
                "\n\tsupplier address: " + supplier.address + "\n\tsupplier phone number: " + supplier.PhoneNumber +
                "\n\tsupplier type: " + ConvertSupplierType(supplier.SupplierType) + "\n\t"+
                 "constant working days? "+ workdaysToString(supplier) +
                "\n\tsupplier bank account: " + supplier.bank +
                 "\n\n";

        System.out.println(s);
    }

    private String ConvertSupplierType(int t){
        switch (t){
            case 1:
                return "ConstantDaysSupplier";
            case 2:
                return "JustOrderSupplier";
            case 3:
                return "CollectingSupplier";
        }
        return "";
    }

    private String workdaysToString(Supplier supplier){
        String s = "";
        switch (supplier.SupplierType){
            case 1:
                s = s + "the days are: ";
                s= s + supplier.workDays;
                return s;
            case 2:
            case 3:
                return s + "there is no constant work days";
        }
        return s;
    }

    private String ConvertDay(int number){
        switch (number){
            case 1:
                return "SUNDAY";
            case 2:
                return "MONDAY";
            case 3:
                return "TUESDAY";
            case 4:
                return "WEDNESDAY";
            case 5:
                return "THURSDAY";
            case 6:
                return "FRIDAY";
            case 7:
                return "SATURDAY";
        }
        return "";
    }

    private String companiesTostring(ArrayList<String> companies){
        String s = "\n\t\t";
        for (int i = 0 ;i<companies.size();i++){
            s = s + "\t- company number " + i+1 + ": " + companies.get(i) + "\n\t\t";
        }
        return s;
    }

    private String contactsTostring(ArrayList<Contact> contacts){
        String s = "";
        if(contacts.size() == 0) return "there is no contacts";
        else {
            s = s + "\n";
            for (Contact contact : contacts){
                s = s + "contact name: " + contact.name + "\ncontact address: " + contact.address
                        +"\n contact phone number: " + contact.phoneNumber + "\n\n";
            }
        }
        return s;
    }

    //------------------------------------Get A Supplier-------------------------------------------
    public void GetSupplier(int supplier_id){
        ResponseT<Supplier> r = service.GetSupplier(supplier_id);
        if(r.ErrorOccured()){
            System.out.println("> error occured while getting a supplier information " + r.ErrorMessage);
        }else{
            printSupplier(r.value);
        }
    }

    //--------------------------------------Get the contract with a supplier-----------------------------
    public void getContract(int supplier_id){
        ResponseT<ArrayList<Contract>> r = service.getContract(supplier_id);
        if(r.ErrorOccured()){
            System.out.println("> error occured while getting the contract: " + r.ErrorMessage);
        }else{
            ArrayList<Contract> contract = r.value;
            printContract(contract);
        }
    }

    private void printContract(ArrayList<Contract> contract) {
        String s = "";
        s = s + "the contract number: " + contract.get(0).contractNum + ".\n";
        for (Contract c : contract){
            int product_code = c.product_code;
            s = s + "- product code: " + product_code;
            String[] quantity_discount = c.quantity_discount.split(", ",0);
            for(int i = 0; i<quantity_discount.length;i= i + 2){
                s = s + "\n- quantity: " + quantity_discount[i];
                s = s + "\t--> discount: " + quantity_discount[i+1];
            }
            s = s + "\n-----------------------------------------------------\n";
        }
        System.out.println(s);
    }

    //---------------------------------Get the finished orders------------------------------------
//    public void getFinishedOrders(int supplier_id){
//        ResponseT<ArrayList<Order>> r = service.getFinishedOrders(supplier_id);
//        if (r.ErrorOccured()){
//            System.out.println("> error occured while getting the orders - " + r.ErrorMessage);
//        }else{
//            printOrders(r.value,supplier_id);
//        }
//    }

    /*private void printOrders(ArrayList<Order> orders,int supplier_id){
        String s = "";
        for (Order order : orders) {
            s = s + order_data(order,supplier_id) + "\n\n";
        }
        System.out.println(s);
    }

    private String order_products_data(Order order){
        String s = "";
        s = s + "product code: " + order.product_code +
                "\nquantity: " + order.quantity
                +"\n\n";
        return s;
    }

    private String order_data(Order order , int supplier_id){
        String s = "the order assigned to the supplier with id: " + supplier_id + "\n";
        s = s + "order date: " + order.date +
                "\nthe products ids in the order , the quantity and the total price: \n" +
                "\nthe order is " + order.status + "\n";
        return s;
    }*/

    //----------------------------Get an order--------------------------------------
//    public void showOrder(int suppplier_id,int order_id){
//        ResponseT<Order> r = service.showOrder(suppplier_id,order_id);
//        if(r.ErrorOccured()){
//            System.out.println("> error occurred while getting the order " + r.ErrorMessage);
//        }else{
//            System.out.println(order_data(r.value,suppplier_id));
//        }
//    }

    public void printAllCategories() {
        ResponseT<List<Category>> categories = service.printAllCategories();
        for (Category c:categories.value){
                System.out.println(c);
                System.out.println();
            }
            System.out.println("---------------------------------");

    }

    public void searchCategory(String cat) {
        ResponseT<Category> category = service.searchCategory(cat);
        if(category.value==null) return;
        System.out.println(category.value);
    }

    public void insertCategoryMenu(String name, String subStr) {
        ResponseT<Map<Integer,Category>> responseT =  service.insertCategoryMenu(name,subStr);
        int id = -1;
        Category sub=null;
        for (Integer i: responseT.value.keySet()){
            id = i;
            sub = responseT.value.get(i);
        }
        if(sub==null) {
            System.out.println("sub category id not found!");
        }
        else if(id==-1)
            System.out.println("Problem inserting category");
        else
            System.out.println("Category inserted! id = "+id);
    }

    public void issueReportMenu(String catString, int rep) {
        ResponseT<String> r = service.issueReportMenu(catString,rep);
        if (!r.ErrorOccured()){
            System.out.println(r.value);
        }else{
            System.out.println(r.ErrorMessage);
        }
    }

    public void addGeneralProduct(String cat, int code, String name,double selling_price, String manufactor, int minQuantity, double weight) {
        Response r = service.addGeneralProduct(cat,code,name,selling_price,manufactor,minQuantity, weight);
        if (!r.ErrorOccured()){
            System.out.println("general product successfully added!");
        }else{
            System.out.println(r.ErrorMessage);
        }
    }

    public void addSpecificProducts(int code, String date) {
        Response r = service.addSpecificProducts(code,date);
        if(!r.ErrorOccured()){
            System.out.println("Product added successfully!");
        }else{
            System.out.println(r.ErrorMessage);
        }
    }


    public void addSaleByCategory(String cat, String date, String percentage) {
        Response r = service.addSaleByCategory(cat,date,percentage);
        if (r.ErrorOccured()){
            System.out.println("Sale adding failed!, " + r.ErrorMessage);
        }else{
            System.out.println("Sale added!");
        }
    }


    public void addSaleBySpecificOrder(String id, String date, String percentage) {
        Response r = service.addSaleBySpecificOrder(id,date,percentage);
        if (r.ErrorOccured()){
            System.out.println("Sale adding failed!, " + r.ErrorMessage);
        }else{
            System.out.println("Sale added!");
        }
    }

    public void nowFlaw(int id) {
        Response r = service.nowFlaw(id);
        if (r.ErrorOccured()){
            System.out.println(r.ErrorMessage);
        }
        else{
            System.out.println("Successfully Updated!");
        }
    }

    public void printSales() throws Exception {
        service.printSales();
    }

    public void viewProductsContract() {
        ResponseT<ArrayList<Contract>> res = service.viewProductsContract();
        for (Contract c : res.value){
            System.out.println(c);
        }
    }

    public void DayOrder(int year, int month, int day) {
        ResponseT<ArrayList<Order>> ordersR = service.DayOrder(year,month,day);
        ArrayList<Order> orders = ordersR.value;
        for(int i=0;i<orders.size();i++) {
            System.out.println(orders.get(i).toString());
        }
    }

    public void SupplierOrder(int id) {
        ResponseT<ArrayList<Order>> ordersR = service.SupplierOrder(id);
        ArrayList<Order> orders = ordersR.value;
        for(int i=0;i<orders.size();i++) {
            System.out.println(orders.get(i).toString());
        }
    }


    public void printOrder() {
        ResponseT<ArrayList<Order>> ordersR = service.printOrder();
        ArrayList<Order> orders = ordersR.value;
        for(int i=0;i<orders.size();i++) {
            System.out.println(orders.get(i).toString()+ "\n");
        }
    }

    public void getgetCategoryByIdOrName(String next) {

    }


    public void checkMinQuantities() {
        ResponseT<ArrayList<Order>> orders = service.checkMinQuantites();
        if (orders.ErrorOccured()){
            System.out.println("can't make automatic order , because : "+orders.ErrorMessage);
        }else {
            if (orders.value.isEmpty()) {
                System.out.print("");
            }else {
                System.out.println("automatic order sent, the order is: ");
                for (Order o : orders.value) {
                    System.out.println(o.toString() + "\n------------------\n");
                }
            }
        }
    }

    public void HandleArrivedOrders() {
        ResponseT<ArrayList<Order>> orders = service.HandleArrivedOrders();
        if (orders.ErrorOccured()){
            System.out.println("error happened while getting the deliveries: " + orders.ErrorMessage);
        }else{
            if (orders.value.size() == 0){
                System.out.println("there is no new arrived deliveries");
            }else{
                for (Order o : orders.value){
                    for (Integer did : o.delivery_ids){
                        ChangeStatus(did);
                    }
                }
            }
        }
    }
}
