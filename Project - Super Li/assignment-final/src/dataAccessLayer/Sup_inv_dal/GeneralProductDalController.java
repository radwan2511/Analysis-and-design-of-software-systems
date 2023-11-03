package dataAccessLayer.Sup_inv_dal;

import businessLayer.Sup_Inv.Category;
import businessLayer.Sup_Inv.CategoryController;
import businessLayer.Sup_Inv.SpecificProduct;
import dataAccessLayer.Sup_inv_dal.DTO.GeneralProductDTO;
import dataAccessLayer.Sup_inv_dal.DTO.SpecificProductDTO;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

public class GeneralProductDalController {
    private static final String CODE="code";
    private static final String NAME="name";
    private static final String COSTPRICE="cost_price";
    private static final String SELLINGPRICE="selling_price";

    private static final String MIN_QUANTITY="min_quantity";
    private static final String QUANTITY="quantity";

    private static final String MANUFACTOR="manufactor";
    public final static String CATEGORY="category";

    private static final String ID="id";
    private static final String LOCATION="location";
    private static final String IS_FLAW="is_flaw";
    private static final String EXPIRED="expired";


    public GeneralProductDTO getGeneralProduct(Connection c, int code, int category_id) {
        try{
            Statement stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery( "SELECT * FROM general_product WHERE code="+code+ " AND category ="+category_id+" ;" );
            while ( rs.next() ) {
                int prcode=rs.getInt("code");
                String name=rs.getString("name");
                double cost_price=rs.getDouble("cost_price");
                double selling_price=rs.getDouble("selling_price");
                int min_quantity=rs.getInt("min_quantity");
                int quantity=rs.getInt("quantity");
                String manufactor = rs.getString("manufactor");
                int cat = rs.getInt("category");
                double weight = rs.getDouble("weight");
                return new GeneralProductDTO(prcode,name,cost_price,selling_price,min_quantity,quantity,manufactor,cat, weight);
                //arr.add(new Order(code,year2,month2,day2,amount,id,price));
            }
            return null;
        }catch (Exception e){
        }
        return null;
    }

    public void insertGeneralProduct(Connection c, GeneralProductDTO gp) {
        try{
            Statement stmt;
            stmt = c.createStatement();
            String sql = "INSERT INTO "+DBHandler.GENERAL_PRODUCT+" ("+CODE+","+NAME+","+COSTPRICE+","+SELLINGPRICE+","+MIN_QUANTITY+","+QUANTITY+","+MANUFACTOR+","+CATEGORY+ ", weight "+") " +
                    "VALUES ("+gp.code +", '"+ gp.name+"' ," +gp.cost_price+", "+gp.selling_price+","+ gp.min_quantity +"," +gp.quantity+", '" +gp.manufactor+"' ," +gp.cat_id+" ,"+ gp.weight+");";
            stmt.executeUpdate(sql);

            c.commit();
            stmt.close();
        }catch(Exception e){
            //e.printStackTrace();
        }
    }

    public Map<Category, GeneralProductDTO> getGeneralProductByCode(Connection c, int code) {
        GeneralProductDTO gdto = null;
        Map<Category,GeneralProductDTO> generalProductDTOMap = new HashMap<>();
        try {
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
                double weight = rs.getDouble("weight");
                Category cat = CategoryController.getInstance().getCategoryById(categoryId);
                gdto = new GeneralProductDTO(pCode,name,cost_price,selling_price,min_quantity,quantity,manufactor,categoryId, weight);
                generalProductDTOMap.put(cat,gdto);
            }

            rs.close();
            stmt.close();
        } catch (Exception e) {
            //			e.printStackTrace();
        }
        return generalProductDTOMap;
    }

    public Map<SpecificProductDTO, Map<Category, GeneralProductDTO>> getSpecificProductById(Connection c, int ide) {
        SpecificProductDTO product = null;
        Map<SpecificProductDTO,Map<Category,GeneralProductDTO>> SG = new HashMap<>();
        try{
            Statement stmt;
            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery( "SELECT * FROM "+DBHandler.SPECIFIC_PRODUCT+" WHERE "+ID+" = "+ide+" AND "+LOCATION+" = '"+ SpecificProduct.LOCATION_STORAGE+"' ;" );
            while ( rs.next() ) {
                int pCode = rs.getInt(CODE);
                int  id = rs.getInt(ID);
                String location  = rs.getString(LOCATION);
                boolean isFlaw= rs.getBoolean(IS_FLAW);
                String expired=rs.getString(EXPIRED);
                //GeneralProductDTO genProduct=getGeneralProductByCode(pCode);
//                products.add(new SpecificProduct(id, location, isFlaw, expired, genProduct));
                product = new SpecificProductDTO(id, location, isFlaw, expired);
                SG.put(product,getGeneralProductByCode(c,pCode));
            }
            rs.close();
            stmt.close();
        }catch(Exception e){
            //e.printStackTrace();
        }
        return SG;
    }

    public void updateCostPrice(Connection c, int code, double price) {
        try{
            Statement stmt = c.createStatement();
            String sql ="UPDATE general_product "+
                    "SET cost_price = "+price +" WHERE code = "+code + ";";
            stmt.execute(sql);
            c.commit();
        }catch (Exception e){

        }
    }

    public void updateCurrQuantityPerProduct(Connection c, int code, int amount) {
        try{
            Statement stmt = c.createStatement();
            String sql ="UPDATE general_product "+
                    "SET quantity = "+amount +" WHERE code = "+code + ";";
            stmt.execute(sql);
            c.commit();
        }catch (Exception e){

        }
    }

    public Map<Category,GeneralProductDTO> getLessThenMinimumGeneralProducts(Connection c) {
        Map<Category,GeneralProductDTO> cat_product=new HashMap<>();
        try{
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
                double weight = rs.getDouble("weight");
                Category cat=CategoryController.getInstance().getCategoryById(categoryId);
                cat_product.put(cat,new GeneralProductDTO(pCode,name,cost_price,selling_price,min_quantity,quantity,manufactor,categoryId, weight));
            }
            rs.close();
            stmt.close();
        }catch(Exception e){
            //e.printStackTrace();
        }
        return cat_product;
    }

    public Map<Category, GeneralProductDTO> getAllGeneralProduct(Connection c) {
        Map<Category,GeneralProductDTO> cat_product=new HashMap<>();
        try{
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
                double weight = rs.getDouble("weight");
                Category cat=CategoryController.getInstance().getCategoryById(categoryId);
                cat_product.put(cat,new GeneralProductDTO(pCode,name,cost_price,selling_price,min_quantity,quantity,manufactor,categoryId, weight));
            }
            rs.close();
            stmt.close();
        }catch(Exception e){
            //			e.printStackTrace();
        }
        return cat_product;
    }
}
