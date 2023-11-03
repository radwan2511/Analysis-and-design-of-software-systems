package businessLayer.Sup_Inv.Facede;

import businessLayer.Sup_Inv.Facede.Objects.Category;
import businessLayer.Sup_Inv.Facede.Objects.Contract;
import businessLayer.Sup_Inv.Facede.Objects.Order;
import businessLayer.Sup_Inv.Facede.Objects.Supplier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Service {
    private final SupplierService supplierService;
    private final InventoryService inventoryService;
    public Service() throws Exception {
        inventoryService = new InventoryService();
        supplierService = new SupplierService();
        InitializeData();
    }

    public void InitializeData() {
        InitializeInventory();
        InitializeSuppliers();
    }

    private void InitializeInventory(){
        insertCategoryMenu("food","0");
        insertCategoryMenu("Chocklates","1");
        addGeneralProduct("1",444,"Kinder fingers",9.99,"Kinder",100, 0.4);
        addGeneralProduct("1",555,"kitkat",2.99,"Elit",1000, 0.8);
        addSpecificProducts(444,"20/06/2023");
        addSpecificProducts(555,"07/12/2022");
    }

    private void InitializeSuppliers(){
        AddSupplier("james","Tel-Aviv, Dezingoph 123","0555555555",1,"221, boalim",512);
        AddSupplier("Ron","Ber-Sheva, Ben-Gurion  12","0505050505",2,"2214, leumi",4456);
        AddSupplier("moslem","Ber-Sheva, Ben-Gurion  546","0123456789",3,"21234, leumi",666);
        Map<Integer,Map<Integer,Integer>> code_quantity_discount1 = new HashMap<>();
        Map<Integer,Map<Integer,Integer>> code_quantity_discount2 = new HashMap<>();
        Map<Integer,Map<Integer,Integer>> code_quantity_discount3 = new HashMap<>();
        Map<Integer,Integer> quantity_discount1 = new HashMap<>();
        Map<Integer,Integer> quantity_discount2 = new HashMap<>();
        Map<Integer,Integer> quantity_discount3_1 = new HashMap<>();
        Map<Integer,Integer> quantity_discount3_2 = new HashMap<>();
        quantity_discount1.put(100,10);
        quantity_discount1.put(150,20);
        code_quantity_discount1.put(444,quantity_discount1);
        code_quantity_discount1.put(555,new HashMap<>());
        quantity_discount2.put(500,30);
        code_quantity_discount2.put(444,new HashMap<>());
        code_quantity_discount2.put(555,quantity_discount2);
        quantity_discount3_1.put(100,20);
        quantity_discount3_2.put(150,50);
        code_quantity_discount3.put(444,quantity_discount3_1);
        code_quantity_discount3.put(555,quantity_discount3_2);
        HashMap<Integer,Double> price1 = new HashMap<>();
        HashMap<Integer,Double> price2 = new HashMap<>();
        HashMap<Integer,Double> price3 = new HashMap<>();
        price1.put(444,100.0);
        price1.put(555,25.5);
        price2.put(444,111.2);
        price2.put(555,333.3);
        price3.put(444,10.0);
        price3.put(555,5.5);
        MakeContract(1,223,true,code_quantity_discount1,price1);
        MakeContract(2,354,false,code_quantity_discount2,price2);
        MakeContract(3,666,false,code_quantity_discount3,price3);
        int[] days = {2, 4, 6};
        setWorkDays(1,days);
//        checkMinQuantites(); // new
    }

    public ResponseT<String> AddConstantOrder(int supplierId, Map<Integer,Integer> code_quantity){
        return supplierService.AddConstantOrder(supplierId,code_quantity);
    }

    public Response AddSupplier(String name ,String address, String PhoneNumber, int type, String bank, int companyNum){
        return supplierService.AddSupplier(name, address, PhoneNumber, type, bank, companyNum);
    }

//    public Response AddOrder(int supplier_id, int product_code, Date date, int quantity){
//        return supplierService.AddOrder(supplier_id, product_code, date, quantity);
//    }

    public ResponseT<ArrayList<Order>> DeleteSupplier(int supplier_id){
        return supplierService.DeleteSupplier(supplier_id);
    }

    public Response CancelOrder(int id){
        return supplierService.CancelOrder(id);
    }

    public Response MakeContract(int supplier_id,int contractNum,boolean constantDay,Map<Integer,Map<Integer,Integer>> QuantityDiscountPerProduct,Map<Integer,Double> price) {
        return supplierService.MakeContract(supplier_id, contractNum, constantDay, QuantityDiscountPerProduct,price);
    }

    public Response DeleteContract(int supplier_id){
        return supplierService.DeleteContract(supplier_id);
    }
//    public Response AddContact(int supplier_id,String name, String phoneNumber, String address){
//        return supplierService.AddContact(supplier_id, name, phoneNumber, address);
//    }
    /*public Response AddProduct(String name, double price,double weight){
        return supplierService.AddProduct(name, price, weight);
    }*/
    /*public Response AddCompany(String address,String comp){
        return supplierService.AddCompany(address,comp);
    }*/
    public Response setWorkDays(int supplier_id, int [] days){
        return supplierService.setWorkDays(supplier_id, days);
    }
    public Response ChangeStatus(int id){
        return supplierService.ChangeStatus(id);
    }
    public ResponseT<ArrayList<Supplier>> GetAllSuppliers(){
        return supplierService.GetAllSuppliers();
    }
    public ResponseT<Supplier> GetSupplier(int supplier_id){
        return supplierService.GetSupplier(supplier_id);
    }
    public ResponseT<ArrayList<Contract>> getContract(int supplier_id){
        return supplierService.getContract(supplier_id);
    }
//    public ResponseT<ArrayList<Order>> getFinishedOrders(int supplier_id){
//        return supplierService.getFinishedOrders(supplier_id);
//    }
//
//    public ResponseT<Order> showOrder(int supplier_id,int order_id){
//        return supplierService.showOrder(supplier_id,order_id);
//    }

    public ResponseT<List<Category>> printAllCategories() {
        return inventoryService.printAllCategories();
    }

    public ResponseT<Category> searchCategory(String cat) {
        return inventoryService.searchCategory(cat);
    }

    public ResponseT<Map<Integer,Category>> insertCategoryMenu(String name, String subStr) {
        return inventoryService.insertCategoryMenu(name,subStr);
    }

    /*public void issueReportMenu(Scanner s) {
        inventoryService.issueReportMenu(s);
    }*/
    public ResponseT<String> issueReportMenu(String catString, int rep) {
        return inventoryService.issueReportMenu(catString,rep);
    }

    /*public void addGeneralProduct(Scanner s) {
        inventoryService.addGeneralProduct(s);
    }*/
    public Response addGeneralProduct(String cat, int code, String name,  double selling_price, String manufactor, int minQuantity, double weight) {
        return inventoryService.addGeneralProduct(cat,code,name,selling_price,manufactor,minQuantity, weight);
    }

    /*public void addSpecificProducts(Scanner s) {
        inventoryService.addSpecificProducts(s);
    }*/
    public Response addSpecificProducts(int code, String date) {
        return inventoryService.addSpecificProducts(code,date);
    }

    /*public void addSale(Scanner s) {
        inventoryService.addSale(s);
    }*/

    public Response addSaleByCategory(String cat, String date, String percentage) {
        return inventoryService.addSaleByCategory(cat,date,percentage);
    }
    public Response addSaleBySpecificOrder(String id, String date, String percentage) {
        return inventoryService.addSaleBySpecificOrder(id,date,percentage);
    }

    public Response nowFlaw(int id) {
        return inventoryService.nowFlaw(id);
    }

    public void printSales() throws Exception {
        inventoryService.printSales();
    }

    /*public void supplierActions(Scanner s) throws SQLException {
        SupplierService.supplierActions(s);
    }*/

    /*public void contractAction(Scanner s) throws SQLException {
        SupplierService.contractAction(s);
    }*/

    /*public void orderAction(Scanner s) throws SQLException {
        SupplierService.orderAction(s);

    }*/

    public ResponseT<ArrayList<Supplier>> printSuppliers() {
        return supplierService.printSupplier();
    }

    public ResponseT<ArrayList<Contract>> viewProductsContract() {
        return supplierService.viewProductsContract();
    }

    public ResponseT<ArrayList<Order>> DayOrder(int year, int month, int day) {
        return supplierService.DayOrder(year,month,day);
    }

    public ResponseT<ArrayList<Order>> SupplierOrder(int id) {
        return supplierService.SupplierOrder(id);
    }

    /*public Response addOrder(int year, int month, int day, int code, int quantity) {
        return supplierService.addOrder(year,month,day,code,quantity);
    }*/

    public ResponseT<ArrayList<Order>> printOrder() {
        return supplierService.printOrder();
    }

    public Response addComplexOrder(Map<Integer, Integer> code_quantity, int year, int month, int day) {
        return supplierService.addComplexOrder(code_quantity,year,month,day);
    }

    public Response addOrder(int id) {
        return supplierService.addOrder(id);
    }

    public ResponseT<ArrayList<Order>> checkMinQuantites() {
        return inventoryService.checkMinQuantites();
    }


    public ResponseT<ArrayList<Order>> HandleArrivedOrders() {
        return supplierService.HandleArrivedOrders();
    }
}
