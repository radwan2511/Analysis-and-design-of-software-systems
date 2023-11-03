package businessLayer.Sup_Inv;

import dataAccessLayer.Sup_inv_dal.DBHandler;
import dataAccessLayer.Sup_inv_dal.DTO.GeneralProductDTO;
import dataAccessLayer.Sup_inv_dal.DTO.SaleDTO;
import dataAccessLayer.Sup_inv_dal.DTO.SpecificProductDTO;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.*;

public class ProductController {
    private static final String CODE="code";
    private static final String NAME="name";
    private static final String COSTPRICE="cost_price";
    private static final String SELLINGPRICE="selling_price";

    private static final String MIN_QUANTITY="min_quantity";
    private static final String QUANTITY="quantity";
    private static final String CATEGORY="category";

    private static final String MANUFACTOR="manufactor";

    private static final String SALEID="sale_id";

    private static final String SALETYPE="sale_type";

    private static final String DATE="date";

    private static final String SALEPERCENTAGE="sale_percentage";


    private static final String ID="id";
    private static final String LOCATION="location";
    private static final String IS_FLAW="is_flaw";
    private static final String EXPIRED="expired";

    private static final int LITTLE_MORE_THEN_MINIMUM=10;

    private static ProductController instance = null;

    private  Map<Integer,GeneralProduct> products;
    private  Map<Integer,SpecificProduct> specific_products;
    private Map<Integer,Sale> sales;
    private DBHandler dbHandler;
    private ProductController() throws Exception {
        products = new HashMap<>();
        specific_products = new HashMap<>();
        sales = new HashMap<>();
        dbHandler = InventoryController.getInstance().getDBHandler();
    }

    public static ProductController getInstance() throws Exception {
        if (instance == null){
            instance = new ProductController();
        }
        return instance;
    }

    public ArrayList<Order> checkMinQuantities() throws Exception {
        Map<Category,GeneralProductDTO> gpsdto = dbHandler.getAllGeneralProduct();
        Map<Integer,Integer> code_quantity = new HashMap<>();
        for (Category c : gpsdto.keySet()){
            GeneralProduct gp = convertGeneralProductDto(gpsdto.get(c),c);
            products.put(gpsdto.get(c).code,gp);
            if (gpsdto.get(c).quantity< gpsdto.get(c).min_quantity){
                code_quantity.put(gp.getCode(),gp.getQuantity());
            }
        }
        LocalDateTime now = LocalDateTime.now();

        return OrderController.getInstance().CheckAddOrder(code_quantity,now.getYear(),now.getMonthValue(),now.getDayOfMonth());
    }

    public String printSales(){
        //return InventoryController.getInstance().getDBHandler().printSales();
        String str = "";
        //List<Sale> sales = new LinkedList<>();
        try{
            Connection c=InventoryController.getInstance().getDBHandler().open();
            Statement stmt;
            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery( "SELECT * FROM "+DBHandler.SALE+";" );
            while ( rs.next() ) {
                int sale_id = rs.getInt(SALEID);
                int  sale_type = rs.getInt(SALETYPE);
                String date  = rs.getString(DATE);
                String sale_percentage = rs.getString(SALEPERCENTAGE);
                int id = rs.getInt(ID);
                Sale s = new Sale(sale_type, date, sale_percentage, id);
                //sales.add(s);

                str = str + s.toString() +"\n";
            }
            rs.close();
            stmt.close();
            c.close();
        }catch(Exception e){
            //e.printStackTrace();
        }
        //return sales;
        return str;
    }
//
    public boolean addSale(int sale_type, String date, String sale_percentage, int id) throws Exception {
        if (sales.containsKey(id)){
            throw new IllegalArgumentException("the sale is not exist in the system");
        }else{
            SaleDTO saleDTO = dbHandler.addSale(sale_type, date, sale_percentage, id);
            Sale s = convertDTOsale(saleDTO);
            sales.put(id,s);
            return true;
        }

        /*try{
            Connection c=InventoryController.getInstance().getDBHandler().open();
            Statement stmt = c.createStatement();
            stmt = c.createStatement();
            int sale_id = InventoryController.getInstance().getDBHandler().getSale_id();
            String sql = "INSERT INTO "+DBHandler.SALE+" ("+SALEID+","+SALETYPE+","+DATE+","+SALEPERCENTAGE+","+ID+") " +
                    "VALUES ("+ sale_id +", "+sale_type +", '"+ date+"', '" +sale_percentage+"', "+id+");";
            stmt.executeUpdate(sql);

            c.commit();
            stmt.close();

            return true;
        }catch(Exception e){
            e.printStackTrace();
        }
        return false;*/
    }
    private Sale convertDTOsale(SaleDTO saleDTO){
        if (saleDTO!=null){
            return new Sale(saleDTO.sale_type,saleDTO.date,saleDTO.sale_percentage,saleDTO.id);
        }else return null;
    }
    public GeneralProduct getGeneralProductByCode(int code) throws Exception {
        if (products.containsKey(code)){
            return products.get(code);
        }else {
            Map<Category,GeneralProductDTO> generalProductDTOMap = dbHandler.getGeneralProductByCode(code);
           if (generalProductDTOMap.size() == 0){
               return null;
               // throw new IllegalArgumentException("the product is not found!");
            }
            else {
                Category c = null;
                GeneralProductDTO gdto = null;
                for (Category ca : generalProductDTOMap.keySet()) {
                    c = ca;
                    gdto = generalProductDTOMap.get(c);
                }
                GeneralProduct gp = convertGeneralProductDto(gdto,c);
                products.put(code,gp);
                return gp;
            }
        }
    }


    public List<SpecificProduct> getAllFlawSpecificProducts() throws Exception {
        Map<SpecificProductDTO,Map<Category,GeneralProductDTO>> specificProductDTOMapMap = dbHandler.getAllFlawSpecificProducts();
        if (specificProductDTOMapMap.size() == 0){
            throw new IllegalArgumentException("there is no flaw products");
        }else{
            List<SpecificProduct> specificProducts = new LinkedList<>();
            for (SpecificProductDTO sdto : specificProductDTOMapMap.keySet()){
                GeneralProductDTO gpDTO = specificProductDTOMapMap.get(sdto).entrySet().iterator().next().getValue();
                Category category = specificProductDTOMapMap.get(sdto).entrySet().iterator().next().getKey();
                GeneralProduct gp = convertGeneralProductDto(gpDTO,category);
                SpecificProduct s = new SpecificProduct(sdto.id,sdto.location,sdto.isFlaw,sdto.expired,gp);
                specificProducts.add(s);
            }
            return specificProducts;
        }
        /*List<SpecificProduct> products=new LinkedList<>();
        try{
            Connection c=InventoryController.getInstance().getDBHandler().open();
            Statement stmt;
            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery( "SELECT * FROM "+DBHandler.SPECIFIC_PRODUCT+" WHERE "+LOCATION+" = '"+SpecificProduct.LOCATION_STORAGE+"';" );
            while ( rs.next() ) {
                int pCode = rs.getInt(CODE);
                int  id = rs.getInt(ID);
                String location  = rs.getString(LOCATION);
                boolean isFlaw= rs.getBoolean(IS_FLAW);
                String expired=rs.getString(EXPIRED);
                GeneralProduct genProduct=getGeneralProductByCode(pCode);
                try{
                    if(isFlaw|| DateFormat.getDateInstance().parse(expired).before(new Date()))
                        products.add(new SpecificProduct(id, location, isFlaw, expired, genProduct));
                }catch(Exception e){}
            }
            rs.close();
            stmt.close();
        }catch(Exception e){
            //e.printStackTrace();
        }
        return products;*/
    }

//
    public SpecificProduct getSpecificProductById(int ide) throws Exception {
        if (!specific_products.containsKey(ide)) {
            Map<SpecificProductDTO, Map<Category, GeneralProductDTO>> SG = dbHandler.getSpecificProductById(ide);
            if (SG.size() == 0) {
                throw new IllegalArgumentException("the specific product is not exist");
            } else {
                SpecificProductDTO sdto = SG.entrySet().iterator().next().getKey();
                Category c = SG.entrySet().iterator().next().getValue().entrySet().iterator().next().getKey();
                GeneralProductDTO gdto = SG.entrySet().iterator().next().getValue().entrySet().iterator().next().getValue();
                GeneralProduct gp = convertGeneralProductDto(gdto, c);
                SpecificProduct sp = new SpecificProduct(sdto.id, sdto.location, sdto.isFlaw, sdto.expired, gp);
                specific_products.put(ide,sp);
                return sp;
            }
        }else{
            return specific_products.get(ide);
        }
    }

    public List<GeneralProduct> getAllGeneralProductWithCategories(List<Category> categories) throws Exception {
        List<GeneralProduct> list=getAllGeneralProduct();
        List<GeneralProduct> result=new LinkedList<>();
        for(GeneralProduct sp:list){
            boolean flag=false;
            Category c=sp.getCategory();
            while(c!=null&&!flag){
                if(isContains(categories,c))
                    flag=true;
                else
                    c=c.getSupCategory();
            }
            if(flag)
                result.add(sp);
        }
        return result;
    }
    public List<GeneralProduct> getLessThenMinimumGeneralProductsWithCategories(List<Category> categories) throws Exception {
        List<GeneralProduct> list=getLessThenMinimumGeneralProducts();
        List<GeneralProduct> result=new LinkedList<>();
        for(GeneralProduct sp:list){
            boolean flag=false;
            Category c=sp.getCategory();
            while(c!=null&&!flag){
                if(isContains(categories,c))
                    flag=true;
                else
                    c=c.getSupCategory();
            }
            if(flag)
                result.add(sp);
        }
        return result;
    }

    public List<GeneralProduct> getLessThenMinimumGeneralProducts() throws Exception {
        Map<Category,GeneralProductDTO> gptos = dbHandler.getLessThenMinimumGeneralProducts();
        List<GeneralProduct> gps = new LinkedList<>();
        if (gptos.size() == 0){
            throw new IllegalArgumentException("there is not products that achieves the conditions");
        }else {
            for (Category c: gptos.keySet() ){
                GeneralProduct gp = convertGeneralProductDto(gptos.get(c),c);
                gps.add(gp);
            }
            return gps;
        }
        /*List<GeneralProduct> products=new LinkedList<>();
        try{
            Connection c=InventoryController.getInstance().getDBHandler().open();
            Statement stmt;
            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery( "SELECT * FROM "+DBHandler.GENERAL_PRODUCT+" WHERE "+QUANTITY+" < "+MIN_QUANTITY +";" );

            while ( rs.next() ) {
                int pCode = rs.getInt(CODE);
                String  name = rs.getString(NAME);
                double cost_price  = rs.getDouble(COSTPRICE);
                double selling_price  = rs.getDouble(SELLINGPRICE);

                int min_quantity= rs.getInt(MIN_QUANTITY);
                int quantity=rs.getInt(QUANTITY);
                String manufactor = rs.getString(MANUFACTOR);
                int categoryId = rs.getInt(CATEGORY);
                Category cat=CategoryController.getInstance().getCategoryById(categoryId);
                products.add(new GeneralProduct(selling_price, name, pCode, min_quantity, quantity,manufactor,cat));
            }
            rs.close();
            stmt.close();
        }catch(Exception e){
            //e.printStackTrace();
        }
        return products;*/
    }



//
//
    public List<GeneralProduct> getAllGeneralProduct() throws Exception {
        Map<Category,GeneralProductDTO> gptos = dbHandler.getAllGeneralProduct();
        List<GeneralProduct> gps = new LinkedList<>();
        if (gptos.size() == 0){
            throw new IllegalArgumentException("there is not products in the system");
        }else {
            for (Category c : gptos.keySet()) {
                GeneralProduct gp = convertGeneralProductDto(gptos.get(c), c);
                gps.add(gp);
            }
            return gps;
        }
        /*List<GeneralProduct> products=new LinkedList<>();
        try{
            Connection c=InventoryController.getInstance().getDBHandler().open();
            Statement stmt;
            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery( "SELECT * FROM "+DBHandler.GENERAL_PRODUCT+";" );

            while ( rs.next() ) {
                int pCode = rs.getInt(CODE);
                String  name = rs.getString(NAME);
                double cost_price  = rs.getDouble(COSTPRICE);
                double selling_price  = rs.getDouble(COSTPRICE);

                int min_quantity= rs.getInt(MIN_QUANTITY);
                int quantity=rs.getInt(QUANTITY);
                String manufactor = rs.getString(MANUFACTOR);
                int categoryId = rs.getInt(CATEGORY);
                Category cat=CategoryController.getInstance().getCategoryById(categoryId);
                products.add(new GeneralProduct(selling_price, name, pCode, min_quantity, quantity,manufactor,cat));
            }
            rs.close();
            stmt.close();
        }catch(Exception e){
    //			e.printStackTrace();
        }
        return products;*/
    }
    //    public static List<GeneralProduct> getLessThenMinimumGeneralProducts() {
//        return InventoryController.getInstance().getDBHandler().getLessThenMinimumGeneralProducts();
//    }
//
    public boolean addQuantity(List<SpecificProduct> productsToAdd){
        try{
            Connection c=InventoryController.getInstance().getDBHandler().open();
            Statement stmt = c.createStatement();
            stmt = c.createStatement();
            for(SpecificProduct sp:productsToAdd){
                String sql = "INSERT INTO "+DBHandler.SPECIFIC_PRODUCT+" ("+ID+","+CODE+","+LOCATION+","+IS_FLAW+","+EXPIRED+") " +
                        "VALUES ("+InventoryController.getInstance().getDBHandler().getSpecific_product_id()+", "+sp.getGeneralProduct().getCode() +", '"+ sp.getLocation()+"' ," +booleanToInt(sp.isFlaw())+", '"+ sp.getExpired() +"' );";
                stmt.executeUpdate(sql);
                String sql1 = "UPDATE "+DBHandler.GENERAL_PRODUCT+" set "+QUANTITY+" = "+QUANTITY+" + 1  WHERE "+CODE+" = "+ sp.getGeneralProduct().getCode() +";" ;
                stmt.executeUpdate(sql1);
                c.commit();
                InventoryController.getInstance().getDBHandler().incrementSpecific_product_idBy(productsToAdd.size());

            }
            stmt.close();
            c.close();
            return true;
        }catch(Exception e){
            //e.printStackTrace();
        }
        return false;
    }

    private static int booleanToInt(boolean flag){
        if(flag)
            return 1;
        return 0;
    }

//
//    public static boolean insertGeneralProduct(GeneralProduct gp){
//        return InventoryController.getInstance().getDBHandler().insertGeneralProduct(gp);
//    }
//
//    public static boolean IssueProducts(List<SpecificProduct> productsToAdd){
//        return InventoryController.getInstance().getDBHandler().IssueProducts(productsToAdd);
//    }
//

    public boolean makeFlaw(int specificProductId) throws Exception {
        if (specific_products.containsKey(specificProductId)){
            specific_products.get(specificProductId).makeFlaw();
            return dbHandler.makeFlaw(specificProductId);
        }else{
            Map<SpecificProductDTO,Map<Category,GeneralProductDTO>> sp = dbHandler.getSpecificProductById(specificProductId);
            GeneralProductDTO gpd = sp.entrySet().iterator().next().getValue().entrySet().iterator().next().getValue();
            Category c = sp.entrySet().iterator().next().getValue().entrySet().iterator().next().getKey();
            GeneralProduct gp = convertGeneralProductDto(gpd,c);
            SpecificProductDTO sdto = sp.entrySet().iterator().next().getKey();
            SpecificProduct sf = new SpecificProduct(sdto.id,sdto.location,sdto.isFlaw,sdto.expired,convertGeneralProductDto(gpd,c));
            specific_products.put(specificProductId,sf);
            sf.makeFlaw();
            return dbHandler.makeFlaw(specificProductId);
        }
        /*try{
			Connection c=InventoryController.getInstance().getDBHandler().open();
			Statement stmt = c.createStatement();
			stmt = c.createStatement();
			String sql = "UPDATE "+DBHandler.SPECIFIC_PRODUCT+" set "+IS_FLAW+" = "+booleanToInt(true)+" WHERE "+ID+" = "+ specificProductId +";" ;
			stmt.executeUpdate(sql);
			c.commit();
			stmt.close();
			return true;
		}catch(Exception e){
			//e.printStackTrace();
		}
		return false;*/
    }
    public List<SpecificProduct> getAllFlawSpecificProductsWithCategories(List<Category> categories) throws Exception {
        List<SpecificProduct> list=getAllFlawSpecificProducts();
        List<SpecificProduct> result=new LinkedList<>();
        for(SpecificProduct sp:list){
            boolean flag=false;
            Category c=sp.getGeneralProduct().getCategory();
            while(c!=null&&!flag){
                if(isContains(categories,c))
                    flag=true;
                else
                    c=c.getSupCategory();
            }
            if(flag)
                result.add(sp);
        }
        return result;
    }
    private static boolean isContains(List<Category> categories, Category c) {
        for(Category cat:categories){
            if(c.getId()==cat.getId())
                return true;
        }
        return false;
    }

    public void addGeneralProduct( int code, String name, double selling_price, String manufactor, int minQuantity,Category category, double weight) throws Exception {
        if (!products.containsKey(code)){
            GeneralProductDTO gb = dbHandler.getGeneralProduct(code,category.getId());
            if (gb == null){
                GeneralProduct p = new GeneralProduct(selling_price,name,code,minQuantity,0,manufactor,category, weight);
                products.put(code,p);
                dbHandler.insertGeneralProduct(convertGeneralProductB(p,category));
            }
            else{
                throw new IllegalArgumentException("the product is exist in the system");
            }
        }else{
            throw new IllegalArgumentException("the product is exist in the system");
        }
    }
    private GeneralProduct convertGeneralProductDto(GeneralProductDTO generalProductDTO,Category category){
        return new GeneralProduct(generalProductDTO.selling_price,generalProductDTO.name,generalProductDTO.code,generalProductDTO.min_quantity,generalProductDTO.quantity,generalProductDTO.manufactor,category, generalProductDTO.weight);
    }

    private GeneralProductDTO convertGeneralProductB(GeneralProduct p ,Category category){
        return new GeneralProductDTO(p.getCode(),p.getName(),p.getCostPrice(),p.getSellingPrice(),p.getMin_quantity(),p.getQuantity(),p.getManufactor(),category.getId(), p.getWeight());
    }


    public void setCostprice(int code,double price) throws Exception {
        if (products.containsKey(code)) {
            products.get(code).setCost_price(price);
            dbHandler.updateCostPrice(code,price);
        }
        else{
            GeneralProduct gp = getGeneralProductByCode(code);
            if (gp == null){
                throw new IllegalArgumentException("can't make a contract with unexisted product code");
            }
            gp.setCost_price(price);
            dbHandler.updateCostPrice(code,price);
        }
    }

    public int[] MinQuantity_AndCurrQuantity(int code) throws Exception {
        int[] min_curr = new int[2];
        if (products.containsKey(code)){
            min_curr[0] = products.get(code).getMin_quantity();
            min_curr[1] = products.get(code).getQuantity();
            return min_curr;
        }else{
            GeneralProduct gb = getGeneralProductByCode(code);
            if (gb!=null){
                min_curr[0] = gb.getMin_quantity();
                min_curr[1] = gb.getQuantity();
                return min_curr;
            }
        }
        return min_curr;
    }

    public void updateCurrQuantity(int code,int amount) throws Exception {
        if (products.containsKey(code)){
            products.get(code).setQuantity(amount);
            dbHandler.updateCurrQuantityPerProduct(code,amount);
        }else{
            GeneralProduct gb = getGeneralProductByCode(code);
            if (gb!=null){
                gb.setQuantity(amount);
                dbHandler.updateCurrQuantityPerProduct(code,amount);
            }
        }
    }

    public Map<Integer, GeneralProduct> getProducts() {
        return products;
    }

    public SpecificProductDTO getSpecificProductByCode(int code) throws Exception {
        return dbHandler.getSpecificProductByCode(code);
    }



    //
    /*public static List<SpecificProduct> getSpecificProductsByCode(int code){
    List<SpecificProduct> products=new LinkedList<>();
		try{
        Connection c=InventoryController.getInstance().getDBHandler().open();
        Statement stmt;
        stmt = c.createStatement();
        ResultSet rs = stmt.executeQuery( "SELECT * FROM "+DBHandler.SPECIFIC_PRODUCT+" WHERE "+CODE+" = "+code+" AND "+LOCATION+" = '"+SpecificProduct.LOCATION_STORAGE+"' ;" );
        while ( rs.next() ) {
            int pCode = rs.getInt(CODE);
            int  id = rs.getInt(ID);
            String location  = rs.getString(LOCATION);
            boolean isFlaw= rs.getBoolean(IS_FLAW);
            String expired=rs.getString(EXPIRED);
            GeneralProduct genProduct=getGeneralProductByCode(pCode);
            products.add(new SpecificProduct(id, location, isFlaw, expired, genProduct));
        }
        rs.close();
        stmt.close();
    }catch(Exception e){
        //e.printStackTrace();
    }
		return products;
    }*/
//

    //    public static List<GeneralProduct> getAllGeneralProductWithCategories(List<Category> categories) {
    //        List<GeneralProduct> list=getAllGeneralProduct();
    //        List<GeneralProduct> result=new LinkedList<>();
    //        for(GeneralProduct sp:list){
    //            boolean flag=false;
    //            Category c=sp.getCategory();
    //            while(c!=null&&!flag){
    //                if(isContains(categories,c))
    //                    flag=true;
    //                else
    //                    c=c.getSupCategory();
    //            }
    //            if(flag)
    //                result.add(sp);
    //        }
    //        return result;
    //    }

    //    public static List<SpecificProduct> getAllFlawSpecificProductsWithCategories(List<Category> categories) {
    //        List<SpecificProduct> list=getAllFlawSpecificProducts();
    //        List<SpecificProduct> result=new LinkedList<>();
    //        for(SpecificProduct sp:list){
    //            boolean flag=false;
    //            Category c=sp.getGeneralProduct().getCategory();
    //            while(c!=null&&!flag){
    //                if(isContains(categories,c))
    //                    flag=true;
    //                else
    //                    c=c.getSupCategory();
    //            }
    //            if(flag)
    //                result.add(sp);
    //        }
    //        return result;
    //    }


    //    public static List<SpecificProduct> getAllFlawSpecificProducts(){
    //        List<SpecificProduct> products=new LinkedList<>();
    //        try{
    //            Connection c=InventoryController.getInstance().getDBHandler().open();
    //            Statement stmt;
    //            stmt = c.createStatement();
    //            ResultSet rs = stmt.executeQuery( "SELECT * FROM "+DBHandler.SPECIFIC_PRODUCT+" WHERE "+LOCATION+" = '"+SpecificProduct.LOCATION_STORAGE+"';" );
    //            while ( rs.next() ) {
    //                int pCode = rs.getInt(CODE);
    //                int  id = rs.getInt(ID);
    //                String location  = rs.getString(LOCATION);
    //                boolean isFlaw= rs.getBoolean(IS_FLAW);
    //                String expired=rs.getString(EXPIRED);
    //                GeneralProduct genProduct=getGeneralProductByCode(pCode);
    //                try{
    //                    if(isFlaw|| DateFormat.getDateInstance().parse(expired).before(new Date()))
    //                        products.add(new SpecificProduct(id, location, isFlaw, expired, genProduct));
    //                }catch(Exception e){}
    //            }
    //            rs.close();
    //            stmt.close();
    //        }catch(Exception e){
    //            //e.printStackTrace();
    //        }
    //        return products;
    //    }

    //public static SpecificProduct getSpecificProductById(int ide){
    //        SpecificProduct product= null;
    //        try{
    //            Connection c=InventoryController.getInstance().getDBHandler().open();
    //            Statement stmt;
    //            stmt = c.createStatement();
    //            ResultSet rs = stmt.executeQuery( "SELECT * FROM "+DBHandler.SPECIFIC_PRODUCT+" WHERE "+ID+" = "+ide+" AND "+LOCATION+" = '"+SpecificProduct.LOCATION_STORAGE+"' ;" );
    //            while ( rs.next() ) {
    //                int pCode = rs.getInt(CODE);
    //                int  id = rs.getInt(ID);
    //                String location  = rs.getString(LOCATION);
    //                boolean isFlaw= rs.getBoolean(IS_FLAW);
    //                String expired=rs.getString(EXPIRED);
    //                GeneralProduct genProduct=getGeneralProductByCode(pCode);
    ////                products.add(new SpecificProduct(id, location, isFlaw, expired, genProduct));
    //                product = new SpecificProduct(id, location, isFlaw, expired, genProduct);
    //            }
    //            rs.close();
    //            stmt.close();
    //        }catch(Exception e){
    //            //e.printStackTrace();
    //        }
    //        return product;
    //    }


//    public static GeneralProduct getGeneralProductByCode(int code){
//        GeneralProduct product=null;
//        try{
//            Connection c=InventoryController.getInstance().getDBHandler().open();
//            Statement stmt;
//            stmt = c.createStatement();
//            ResultSet rs = stmt.executeQuery( "SELECT * FROM "+DBHandler.GENERAL_PRODUCT+" WHERE "+CODE+" = "+code+";" );
//
//            while ( rs.next() ) {
//                int pCode = rs.getInt(CODE);
//                String  name = rs.getString(NAME);
//                double cost_price  = rs.getDouble(COSTPRICE);
//                double selling_price  = rs.getDouble(SELLINGPRICE);
//                int min_quantity= rs.getInt(MIN_QUANTITY);
//                int quantity=rs.getInt(QUANTITY);
//                String manufactor = rs.getString(MANUFACTOR);
//                int categoryId = rs.getInt(CATEGORY);
//                Category cat=CategoryController.getCategoryById(categoryId);
//                product=new GeneralProduct(selling_price, name, pCode, min_quantity, quantity,manufactor,cat);
//            }
//            rs.close();
//            stmt.close();
//        }catch(Exception e){
//    //			e.printStackTrace();
//        }
//        return product;
//    }

    /*public static boolean addGeneralProduct(GeneralProduct gp) {
        try{
            Connection c=InventoryController.getInstance().getDBHandler().open();
            Statement stmt = c.createStatement();
            stmt = c.createStatement();
            String sql = "INSERT INTO "+DBHandler.GENERAL_PRODUCT+" ("+CODE+","+NAME+","+COSTPRICE+","+SELLINGPRICE+","+MIN_QUANTITY+","+QUANTITY+","+MANUFACTOR+","+CATEGORY+") " +
                    "VALUES ("+gp.getCode() +", '"+ gp.getName()+"' ," +gp.getCostPrice()+", "+gp.getSellingPrice()+","+ gp.getMin_quantity() +"," +gp.getQuantity()+", '" +gp.getManufactor()+"' ," +gp.getCategory().getId()+");";
            stmt.executeUpdate(sql);

            c.commit();
            stmt.close();

            return true;
        }catch(Exception e){
            //e.printStackTrace();
        }
        return false;
    }*/

//    public static List<GeneralProduct> getAllGeneralProductWithCategories(List<Category> categories) {
//        List<GeneralProduct> list=getAllGeneralProduct();
//        List<GeneralProduct> result=new LinkedList<>();
//        for(GeneralProduct sp:list){
//            boolean flag=false;
//            Category c=sp.getCategory();
//            while(c!=null&&!flag){
//                if(isContains(categories,c))
//                    flag=true;
//                else
//                    c=c.getSupCategory();
//            }
//            if(flag)
//                result.add(sp);
//        }
//        return result;
//    }
//    public static List<GeneralProduct> getLessThenMinimumGeneralProductsWithCategories(List<Category> categories) {
//        List<GeneralProduct> list=getLessThenMinimumGeneralProducts();
//        List<GeneralProduct> result=new LinkedList<>();
//        for(GeneralProduct sp:list){
//            boolean boo =false;
//            Category c=sp.getCategory();
//            while(c!=null&&!boo){
//                if(isContains(categories,c))
//                    boo=true;
//                else
//                    c=c.getSupCategory();
//            }
//            if(boo)
//                result.add(sp);
//        }
//        return result;
//    }


}

