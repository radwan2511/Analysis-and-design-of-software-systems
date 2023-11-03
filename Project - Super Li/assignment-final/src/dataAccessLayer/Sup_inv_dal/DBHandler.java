package dataAccessLayer.Sup_inv_dal;

import businessLayer.Sup_Inv.Category;
import dataAccessLayer.Sup_inv_dal.DTO.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class DBHandler {
    public final static String GENERAL_PRODUCT="general_product";
    public final static String SPECIFIC_PRODUCT="specific_product";
    public final static String CATEGORY="category";

    public final static String SALE = "sale";
    public static final String ORDER = "order";
    public static final String SUPPLIERS = "supplier";

    private static final String CODE="code";
    private static final String NAME="name";
    private static final String COSTPRICE="cost_price";
    private static final String SELLINGPRICE="selling_price";

    private static final String MIN_QUANTITY="min_quantity";
    private static final String QUANTITY="quantity";

    private static final String MANUFACTOR="manufactor";

    private static final String SALEID="sale_id";

    private static final String SALETYPE="sale_type";

    private static final String DATE="date";

    private static final String SALEPERCENTAGE="sale_percentage";

    public Connection c=null;
    private static DBHandler instance = null ;

    int category_id=1;
    int specific_product_id=1;

    int order_ids = 1;

    int sale_ids = 1;

    int supplier_id = 1;

    private SupplierDalController supplierDalController;
    private ContractDalController contractDalController;
    private OrderDalController orderDalController;
    private GeneralProductDalController generalProductDalController;
    private CategoryDalController categoryDalController;
    private SpecificProductDalController specificProductDalController;
    private SalesDalController salesDalController;
    private String url;

    private DBHandler(String url) {
        supplierDalController = new SupplierDalController();
        contractDalController = new ContractDalController();
        orderDalController = new OrderDalController();
        generalProductDalController = new GeneralProductDalController() ;
        categoryDalController = new CategoryDalController();
        specificProductDalController = new SpecificProductDalController();
        salesDalController = new SalesDalController();
        this.url = url;
        try {
            open();
            init();
            initIds();
        }catch (Exception e){
        }
    }

    public static DBHandler getInstance(String url){
        if (instance == null){
            return instance = new DBHandler(url);
        }else{
            return instance;
        }
    }


    /*public static DBHandler createDBHandler(){
        DBHandler db=new DBHandler();
        try{
            db.open();
            db.init();
        }catch(Exception e){}


        return db;
    }*/
    public Connection open() throws Exception{
        if(c!=null&&!c.isClosed())
            return c;
        Class.forName("org.sqlite.JDBC");
        c = DriverManager.getConnection(url);
        c.setAutoCommit(false);
        return c;
    }

    public void close() throws Exception{
        if(c!=null)
            c.close();
    }
    // use in Tests
    public void cleanDB(){
        try {
            Statement stmt = c.createStatement();
            String sql1 = "DELETE FROM SUPPLIER;";
            stmt.execute(sql1);
            String sql2 = "DELETE FROM "+ SALE + ";";
            stmt.execute(sql2);
            String sql3 = "DELETE FROM "+ CATEGORY + ";";
            stmt.execute(sql3);
            String sql4 = "DELETE FROM "+ SPECIFIC_PRODUCT+";";
            stmt.execute(sql4);
            String sql5 = "DELETE FROM " + GENERAL_PRODUCT + ";";
            stmt.execute(sql5);
            String sql6 = "DELETE FROM Supplier_CONTRACT;";
            stmt.execute(sql6);
            String sql7 = "DELETE FROM ORDER_TABLE;";
            stmt.execute(sql7);
        }catch (Exception e){

        }
    }
    public void init() throws Exception{
        try{
            createGeneralProductTable();
        }catch(Exception e){}
        try{
            createSpecificProductTable();
        }catch(Exception e){}
        try{
            createCategoryTable();
        }catch(Exception e){}
        try{
            createSaleTable();
        }catch(Exception e){}
        try{
            createSupplierTable();
        }catch(Exception e){}
        try{
            createContractTable();
        }catch(Exception e){}
        try{
            createOrderTable();
        }catch(Exception e){}

    }

    private void createSaleTable() throws SQLException {
        Statement stmt = null;

        stmt = c.createStatement();
        String sql = "CREATE TABLE " + SALE +
                " (sale_id           			 INT PRIMARY KEY," +
                " sale_type           			 INT NOT NULL," +
                " date           			 VARCHAR (30) NOT NULL," +
                " sale_percentage            		 VARCHAR (5)  		NOT NULL, " +
                " id        INT NOT NULL);";
        stmt.executeUpdate(sql);
        c.commit();
        stmt.close();
    }

    private void createCategoryTable() throws Exception {
        Statement stmt = null;

        stmt = c.createStatement();
        String sql = "CREATE TABLE " + CATEGORY +
                " (id           			 INT PRIMARY KEY," +
                " name            		 VARCHAR (50)  UNIQUE  		NOT NULL, " +
                " sub_category_id        INT REFERENCES category (id) ON DELETE CASCADE ON UPDATE CASCADE);";
        stmt.executeUpdate(sql);
        c.commit();
        stmt.close();
    }
    private void createSpecificProductTable() throws Exception{
        Statement stmt = null;

        stmt = c.createStatement();
        String sql = "CREATE TABLE " + SPECIFIC_PRODUCT +
                " (id              INTEGER  ," +
                " code             INT  		NOT NULL	 REFERENCES general_product (code) ON DELETE CASCADE ON UPDATE CASCADE, " +
                " location         VARCHAR (20)       ," +
                " is_flaw          BOOLEAN	  	DEFAULT '0'	 ," +
                " expired          DATE	          	,"
                + "PRIMARY KEY (id, code) );";
        stmt.executeUpdate(sql);
        c.commit();
        stmt.close();
    }
    private void createGeneralProductTable() throws Exception{
        Statement stmt = null;

        stmt = c.createStatement();
        String sql = "CREATE TABLE " + GENERAL_PRODUCT +
                " (code            INT  PRIMARY KEY       NOT NULL," +
                " name             VARCHAR (30)    		NOT NULL, " +
                " cost_price            DOUBLE          		NOT NULL, " +
                " selling_price            DOUBLE          		NOT NULL, " +
                " min_quantity     INT	          		NOT NULL, " +
                " quantity    	  INT	          		NOT NULL, " +
                " manufactor		  VARCHAR (30)					, " +
                " weight          DOUBLE                   NOT NULL, " +
                " category         INT	          		REFERENCES category (id) ON DELETE SET NULL ON UPDATE SET NULL);";
        stmt.executeUpdate(sql);
        c.commit();
        stmt.close();
    }
    private void createSupplierTable() throws Exception{

        Statement stmt = null;

        stmt = c.createStatement();
        String sql = "CREATE TABLE SUPPLIER " +
                "(ID INT PRIMARY KEY     NOT NULL," +
                " NAME VARCHAR(30)    NOT NULL, " +
                " ADDRESS VARCHAR(30)    NOT NULL, " +
                " PHONE VARCHAR(30)    NOT NULL, " +
                " TYPE INT    NOT NULL, " +
                " BANK VARCHAR(30)    NOT NULL, " +
                " DAY_WORKS    VARCHAR(30), " +
                //" TERM_OF_PAYMENT VARCHAR(30)    NOT NULL DEFAULT 'credit', " +
                " COMPANY_NUM INT  NOT NULL) ";
        stmt.executeUpdate(sql);
        c.commit();
        stmt.close();
    }
    private void createContractTable() throws Exception{

        Statement stmt = null;

        stmt = c.createStatement();
        String sql =  "CREATE TABLE Supplier_Contract " +
                "(SUPPLIER_ID INT       NOT NULL, " +
                " CODE INT   NOT NULL, " +
                " CONTRACT_NUM INT   NOT NULL, " +
                " PRICE INT  NOT NULL, "+
                " CONSTANTDAYS BOOLEAN  NOT NULL, "+
                " QUANTITY_DISCOUNT VARCHAR(30)  NOT NULL, "+
                "FOREIGN KEY (SUPPLIER_ID) REFERENCES SUPPLIER(ID) ON DELETE CASCADE,"+
                //"FOREIGN KEY (CODE) REFERENCES "+GENERAL_PRODUCT+"(CODE),"+
                " PRIMARY KEY (CODE, CONTRACT_NUM,SUPPLIER_ID) )";
        stmt.executeUpdate(sql);
        c.commit();
        stmt.close();
    }
    private void createOrderTable() throws Exception{

        Statement stmt = null;

        stmt = c.createStatement();
        String sql =
                "CREATE TABLE ORDER_TABLE " +
                        "(ID INT NOT NULL," +
                        "SUPPLIER_ID INT  NOT NULL, " +
                        " YEAR INT   NOT NULL, " +
                        " MONTH INT   NOT NULL, " +
                        " DAY INT   NOT NULL, " +
                        " CODE INT   NOT NULL, "+
                        " QUANTITY INT   NOT NULL, " +
                        "PRICE REAL NOT NULL, " +
                        "STATUS TEXT NOT NULL, "+
                        "Delivery_ID INT  NOT NULL, " +
                        "FOREIGN KEY (SUPPLIER_ID) REFERENCES SUPPLIER(ID),"+
                        "FOREIGN KEY (CODE) REFERENCES "+SPECIFIC_PRODUCT+"(CODE)"+
                        " PRIMARY KEY (ID, SUPPLIER_ID, YEAR, MONTH, DAY, CODE) )";
        stmt.executeUpdate(sql);
        c.commit();
        stmt.close();
    }

    public int getCategory_id() {
        return category_id;
    }
    public void incrementCategory_idBy(int inc) {
        this.category_id += inc;
    }
    public int getSpecific_product_id() {
        return specific_product_id;
    }
    public void incrementSpecific_product_idBy(int inc) {
        this.specific_product_id += inc;
    }

    public void initIds() {
        try {
            specific_product_id=countSpecificProducts()+1;
            category_id= CountCategories()+1;
            sale_ids = countSales()+1;
            order_ids = countOrders()+1;
            supplier_id = countSuppliers()+1;
        }catch(Exception e){
        }
    }

    private int countSpecificProducts(){
        //open();
        return specificProductDalController.countSpecificProducts(c);
    }

    private int CountCategories() throws Exception {
        open();
        return categoryDalController.CountCategories(c);
    }

    private int countSuppliers() throws Exception {
        open();
        return supplierDalController.countSuppliers(c);
    }

    private int countOrders() throws Exception {
        open();
        return orderDalController.countOrders(c);
    }

    private int countSales() throws Exception {
        open();
        return salesDalController.countSales(c);
    }

    public int getSale_id() {
        return sale_ids;
    }

    public int getOrder_ids(){
        return order_ids;
    }

    public int increment_Orderid(){
        order_ids++;
        return order_ids;
    }

    public ArrayList<SupplierDTO> viewSuppliers() throws Exception {
        open();
        return supplierDalController.viewSuppliers(c);
    }

    public void insertSupplier(SupplierDTO s1) throws Exception {

        open();
        supplierDalController.insertSupplier(c,s1);
        supplier_id++;
    }

    public void deleteSupplier(int id) throws Exception {
        open();
        supplierDalController.deleteSupplier(c,id);

    }

    public ArrayList<ContractDTO> viewContract() throws Exception {
        open();
        return contractDalController.viewContract(c);
    }

    public ArrayList<ContractDTO> selectContractwithProductCode(int product_code) throws Exception {
        open();
        return contractDalController.selectContractwithProductCode(c,product_code);
    }

    /*public void addContract(int supplierId, int code, int quantity, double price, boolean constant, String quanity_discount) throws SQLException {
        Statement stmt = this.c.createStatement();
        String sql = "INSERT INTO CONTRACT VALUES ("+supplierId+","+code+","+quantity+","+price+ ",'" + constant +"','"+ quanity_discount +"');";
        stmt.execute(sql);
        c.commit();
    }*/
    public void addContract(int supplierId, int code,int contract_num,double price, boolean constant, String quanity_discount) throws Exception {
        open();
        contractDalController.addContract(supplierId,code,contract_num,price,constant,quanity_discount,c);

    }
    public void deleteContract(int supplierId) throws Exception {
        open();
        contractDalController.deleteContract(supplierId,c);
    }

    public ArrayList<OrderDTO> viewOrder() throws Exception {
        open();
        return orderDalController.viewOrder(c);
    }


    public SupplierDTO getSupplier(int id) throws Exception {
        open();
        return supplierDalController.getSupplier(c,id);
    }

    public String getSupplierStringByPhoneNumber(String number) throws Exception {
        open();
        SupplierDTO sdto =  supplierDalController.getSupplierByPhoneNumber(c,number);
        String str = "";
        if (sdto!=null){
            if (sdto.SupplierType == 1){
                str =  "ConstantDaysSupplier";
            }
            if (sdto.SupplierType == 2){
                str =  "JustOrderSupplier";
            }
            if (sdto.SupplierType == 3){
                str =  "CollectingSupplier";
            }
        }
        return str;
    }
    public SupplierDTO getSupplierByPhoneNumber(String number) throws Exception {
        open();
        return supplierDalController.getSupplierByPhoneNumber(c,number);
    }

    public void insertOrder(OrderDTO item) throws Exception {
        open();
        orderDalController.insertOrder(c,item);
    }


    public ArrayList<OrderDTO> getOrderFromSupplier(int mid) throws Exception {
        open();
        return orderDalController.getOrderFromSupplier(c,mid);

    }
//
    public ArrayList<OrderDTO> getOrderFromDate(int year, int month, int day) throws Exception {
        open();
        return orderDalController.getOrderFromDate(c,year,month,day);

    }

    public OrderDTO getOrderFromSupplierAndCodeAndDate(int supplier_id, int product_code,int year,int month,int day) throws Exception {
        open();
        return orderDalController.getOrderFromSupplierAndCodeAndDate(c,supplier_id,product_code,year,month,day);
    }

    public int getSupplier_id() {
        return supplier_id;
    }

    public void deleteOrder(int supplier_id, int product_code,int year,int month,int day) throws Exception {
        open();
        orderDalController.deleteOrder(c,supplier_id,product_code,year,month,day);
    }

    public void deleteOrderYearMonthDay(int year,int month,int day) throws Exception {
        open();
        orderDalController.deleteOrderYearMonthDay(c,year,month,day);
    }

    public void UpdateOrderStatus(String done, int did) throws Exception {
        open();
        orderDalController.UpdateOrderStatus(c,done,did);

    }

    public void UpdateWorkDays(int supplier_id, String workDays) throws Exception {
        open();
        supplierDalController.UpdateWorkDays(c,supplier_id,workDays);
    }

    public ArrayList<ContractDTO> getContract_SupplierId_Contract_Num(int supplier_id, int contract_num) throws Exception {
        open();
        return contractDalController.getContract_SupplierId_Contract_Num(c,supplier_id,contract_num);
    }

    public GeneralProductDTO getGeneralProduct(int code, int category_id) throws Exception {
        open();
        return generalProductDalController.getGeneralProduct(c,code,category_id);

    }

    public void insertGeneralProduct(GeneralProductDTO gp) throws Exception {
        open();
        generalProductDalController.insertGeneralProduct(c,gp);

    }

    public Map<Category,GeneralProductDTO> getGeneralProductByCode(int code) throws Exception {
        open();
        return generalProductDalController.getGeneralProductByCode(c,code);
        /*GeneralProductDTO gdto = null;
        Map<Category,GeneralProductDTO> generalProductDTOMap = new HashMap<>();
        try {
            Connection c = open();
            Statement stmt;
            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM " + DBHandler.GENERAL_PRODUCT + " WHERE " + CODE + " = " + code + ";");

            while (rs.next()) {
                int pCode = rs.getInt(CODE);
                String name = rs.getString(NAME);
                double cost_price = rs.getDouble(COSTPRICE);
                double selling_price = rs.getDouble(SELLINGPRICE);
                int min_quantity = rs.getInt(MIN_QUANTITY);
                int quantity = rs.getInt(QUANTITY);
                String manufactor = rs.getString(MANUFACTOR);
                int categoryId = rs.getInt(CATEGORY);
                Category cat = CategoryController.getInstance().getCategoryById(categoryId);
                gdto = new GeneralProductDTO(pCode,name,cost_price,selling_price,min_quantity,quantity,manufactor,categoryId);
                generalProductDTOMap.put(cat,gdto);
            }

            rs.close();
            stmt.close();
        } catch (Exception e) {
            //			e.printStackTrace();
        }
        return generalProductDTOMap;*/
    }

    public Map<SpecificProductDTO,Map<Category,GeneralProductDTO>> getSpecificProductById(int ide) throws Exception {
        open();
        return generalProductDalController.getSpecificProductById(c, ide);
        /*SpecificProductDTO product = null;
        Map<SpecificProductDTO,Map<Category,GeneralProductDTO>> SG = new HashMap<>();
        try{
            Connection c=InventoryController.getInstance().getDBHandler().open();
            Statement stmt;
            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery( "SELECT * FROM "+DBHandler.SPECIFIC_PRODUCT+" WHERE "+ID+" = "+ide+" AND "+LOCATION+" = '"+SpecificProduct.LOCATION_STORAGE+"' ;" );
            while ( rs.next() ) {
                int pCode = rs.getInt(CODE);
                int  id = rs.getInt(ID);
                String location  = rs.getString(LOCATION);
                boolean isFlaw= rs.getBoolean(IS_FLAW);
                String expired=rs.getString(EXPIRED);
                //GeneralProductDTO genProduct=getGeneralProductByCode(pCode);
//                products.add(new SpecificProduct(id, location, isFlaw, expired, genProduct));
                product = new SpecificProductDTO(id, location, isFlaw, expired);
                SG.put(product,getGeneralProductByCode(pCode));
            }
            rs.close();
            stmt.close();
        }catch(Exception e){
            //e.printStackTrace();
        }
        return SG;*/
    }

    public void updateCostPrice(int code, double price) throws Exception {
        open();
        generalProductDalController.updateCostPrice(c,code,price);

    }

    public void updateCurrQuantityPerProduct(int code, int amount) throws Exception {
        open();
        generalProductDalController.updateCurrQuantityPerProduct(c,code,amount);
    }

    public CategoryDTO getCategoryById(int id) throws Exception {
        open();
        return categoryDalController.getCategoryById(c,id);
    }

    public List<CategoryDTO> getAllCategories() throws Exception {
        open();
        return categoryDalController.getAllCategories(c);
    }

    public CategoryDTO getCategoryByName(String name) throws Exception {
        open();
        return categoryDalController.getCategoryByName(c, name);
    }

    public SaleDTO addSale(int sale_type, String date, String sale_percentage, int id) throws Exception {
        open();
        return salesDalController.addSale(c,sale_type,date,sale_percentage,id,getSale_id());
    }

    public Map<SpecificProductDTO,Map<Category,GeneralProductDTO>> getAllFlawSpecificProducts() throws Exception {
        open();
        return specificProductDalController.getAllFlawSpecificProducts(c);
    }

    public Map<Category,GeneralProductDTO> getLessThenMinimumGeneralProducts() throws Exception {
        open();
        return generalProductDalController.getLessThenMinimumGeneralProducts(c);
    }

    public Map<Category, GeneralProductDTO> getAllGeneralProduct() throws Exception {
        open();
        return generalProductDalController.getAllGeneralProduct(c);
    }

    public boolean makeFlaw(int specificProductId) throws Exception {
        open();
        return specificProductDalController.makeFlaw(c,specificProductId);
    }

    public void deleteSupplierOrders(int supplier_id) throws Exception {
        open();
        orderDalController.deleteSupplierOrders(c,supplier_id);
    }

    public ArrayList<ContractDTO> getContract(int supplier_id) throws Exception {
        open();
        return ContractDalController.getContract(c,supplier_id);
    }

    public SpecificProductDTO getSpecificProductByCode(int code) throws Exception {
        open();
        return SpecificProductDalController.getSpecificProductByCode(c,code);
    }

    public OrderDTO getOrderFromDateAndCode(Integer code, int year, int month, int day) throws Exception {
        open();
        return orderDalController.getOrderFromDateAndCode(c,code,year,month,day);
    }

    public double getPriceFromContract(int supplierId, int code) throws Exception {
        open();
        return contractDalController.getPriceFromContract(c,supplierId,code);
    }

    public ArrayList<OrderDTO> getOrderFromID(int id) throws Exception {
        open();
        return orderDalController.getOrderFromID(c,id);
    }

    public void deleteOrderFromID(int id) throws Exception {
        open();
        orderDalController.deleteOrderFromID(c,id);
    }

    public ArrayList<OrderDTO> getOrderFromDeliveryID(int id) throws Exception {
        open();
        return orderDalController.getOrderFromDeliveryID(c,id);
    }

    public ArrayList<OrderDTO> getActiveOrders() throws Exception {
        open();
        return orderDalController.getActiveOrders(c);
    }





























        /*public ArrayList<Order> getOrderFromSupplierAndCode(int supplier_id, int product_code) throws SQLException {
        ArrayList<Order> arr= new ArrayList<>();
        Statement stmt = this.c.createStatement();
        ResultSet rs = stmt.executeQuery( "SELECT * FROM ORDER_TABLE WHERE SUPPLIER_ID="+supplier_id+" AND CODE="+product_code+" ;" );
        while ( rs.next() ) {
            int id=rs.getInt("SUPPLIER_ID");
            int code=rs.getInt("CODE");
            int amount=rs.getInt("QUANTITY");
            int year2=rs.getInt("YEAR");
            int month2=rs.getInt("MONTH");
            int day2=rs.getInt("DAY");
            double price = rs.getDouble("PRICE");
            arr.add(new Order(code,year2,month2,day2,amount,id,price));
        }
        return arr;
    }*/

//
//    public void deleteQuantityListItem(int code, int supplierId, int quantity) throws SQLException {
//
//
//
//        Statement stmt = this.c.createStatement();
//        String sql = "DELETE FROM CONTRACT_PRODUCT WHERE CODE="+code+" AND SUPPLIER_ID="+supplierId+" AND QUANTITY="+quantity+";";
//        stmt.execute(sql);
//        c.commit();
//    }
//
//    public void updateQuantityListItem(int code, int supplierId, int quantity, int price) throws SQLException {
//        Statement stmt = this.c.createStatement();
//        String sql ="UPDATE CONTRACT_PRODUCT "+
//                "SET PRICE = "+price+" WHERE CODE="+code+" AND SUPPLIER_ID="+supplierId+" AND QUANTITY="+quantity+";";
//        stmt.execute(sql);
//        c.commit();
//    }
//
//    public void addQuantityListItem(int code, int supplierId, int quantity, int price) throws SQLException {
//        Statement stmt = this.c.createStatement();
//        String sql = "INSERT INTO CONTRACT_PRODUCT VALUES ("+code+","+supplierId+","+quantity+","+price+");";
//        stmt.execute(sql);
//        c.commit();
//    }
//
//    public ArrayList<ContractProduct> QuantityListItemForCode(int code) throws SQLException {
//        ArrayList<ContractProduct> arr= new ArrayList<>();
//        Statement stmt = this.c.createStatement();
//        ResultSet rs = stmt.executeQuery( "SELECT * FROM CONTRACT_PRODUCT WHERE CODE="+code+";" );
//        while ( rs.next() ) {
//            int id=rs.getInt("SUPPLIER_ID");
//            int quantity=rs.getInt("QUANTITY");
//            int price=rs.getInt("PRICE");
//            int month2=rs.getInt("MONTH");
//            int day2=rs.getInt("DAY");
//            arr.add(new ContractProduct(code, id,quantity,price));
//        }
//        return arr;
//    }
//
//    public ArrayList<OrderItem> viewOrderItem() throws  SQLException{
//
//
//        Statement stmt = this.c.createStatement();
//        ResultSet rs = stmt.executeQuery( "SELECT * FROM ORDER_ITEM;" );
//        ArrayList<OrderItem> oi=new ArrayList<OrderItem>();
//        while ( rs.next() ) {
//            oi.add(new OrderItem(rs.getInt("SUPPLIER_ID"),rs.getInt("YEAR") , rs.getInt("MONTH"), rs.getInt("DAY"), rs.getInt("CODE"), rs.getInt("AMOUNT")));
//        }
//        return oi;
//    }
//    public ArrayList<ContractProduct> viewContractProduct() throws  SQLException{
//
//
//        Statement stmt = this.c.createStatement();
//        ResultSet rs = stmt.executeQuery( "SELECT * FROM CONTRACT_PRODUCT;" );
//        ArrayList<ContractProduct> cp=new ArrayList<ContractProduct>();
//        while ( rs.next() ) {
//            cp.add(new ContractProduct(rs.getInt("CODE"),rs.getInt("SUPPLIER_ID"),  rs.getInt("QUANTITY"), rs.getInt("PRICE")));
//        }
//        return cp;
//
//    }


    //
//    public void deleteSupplier(int id) throws SQLException {
//        Statement stmt = this.c.createStatement();
//        String sql = "DELETE FROM SUPPLIER WHERE ID="+id+";";
//        stmt.execute(sql);
//    }
//
//    public void insertSupplier(Supplier s1)  {
//        try{
//
//            Statement stmt;
//            String sql;
//            stmt = this.c.createStatement();
//            sql = "INSERT INTO SUPPLIER (ID,COMPANY_NUM,BANK_ACCOUNT,CONTACT,PAYMENT_METHOD,CONTRACT,ADDRESS) " +
//                    "VALUES ("+s1.id+", "+s1.company_num+","+s1.bank_account+", '"+s1.Contact+"', '"+s1.PaymentMethod+"','"+s1.Contract+"','"+s1.getAddress()+"');";
//            stmt.execute(sql);
//            c.commit();
//        }
//        catch(Exception e)
//        {
//            e.printStackTrace();
//        }
//    }
//
//
//    public ArrayList<Supplier> viewSuppliers(){
//        try{
//
//            Statement stmt2 = this.c.createStatement();
//            ResultSet rs = stmt2.executeQuery( "SELECT * FROM SUPPLIER;" );
//            ArrayList<Supplier> a1=new ArrayList<Supplier>();
//            while ( rs.next() ) {
//                a1.add(new Supplier(rs.getInt("ID"), rs.getInt("COMPANY_NUM"), rs.getInt("BANK_ACCOUNT"), rs.getString("PAYMENT_METHOD"), rs.getString("CONTACT"), rs.getString("CONTRACT"),rs.getString("ADDRESS")));
//            }
//            return a1;
//        }
//        catch(Exception e)
//        {
//            e.printStackTrace();
//        }
//        return null;
//
//    }
//    public void insertOrderItem(OrderItem item) throws SQLException {
//
//        Statement stmt;
//        String sql;
//        stmt = this.c.createStatement();
//        sql = "INSERT INTO ORDER_ITEM (SUPPLIER_ID,YEAR,MONTH,DAY,CODE,AMOUNT) " +
//                "VALUES ("+item.supplier_id+", "+item.buyYear+","+item.buyMonth+","+item.buyDay+","+item.code+","+item.amount+");";
//        stmt.execute(sql);
//        c.commit();
//    }
//

}


