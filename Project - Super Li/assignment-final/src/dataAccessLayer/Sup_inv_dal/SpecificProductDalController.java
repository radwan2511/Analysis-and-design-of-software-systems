package dataAccessLayer.Sup_inv_dal;

import businessLayer.Sup_Inv.Category;
import dataAccessLayer.Sup_inv_dal.DTO.GeneralProductDTO;
import dataAccessLayer.Sup_inv_dal.DTO.SpecificProductDTO;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class SpecificProductDalController extends GeneralProductDalController {

    private static final String CODE="code";
    private static final String ID="id";
    private static final String LOCATION="location";
    private static final String IS_FLAW="is_flaw";
    private static final String EXPIRED="expired";
    public static final String LOCATION_STORAGE="local_storage";

    public static SpecificProductDTO getSpecificProductByCode(Connection c, int code) {
        SpecificProductDTO spdto=null;
        try {
            Statement stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery( "SELECT * FROM specific_product WHERE code="+code+";" );
            while ( rs.next() ) {
                int  id = rs.getInt(ID);
                String location  = rs.getString(LOCATION);
                boolean isFlaw= rs.getBoolean(IS_FLAW);
                String expired=rs.getString(EXPIRED);
                spdto = new SpecificProductDTO(id,location,isFlaw,expired);
            }
            c.close();
        }catch (Exception e){
        }
        return spdto;
    }


    public Map<SpecificProductDTO,Map<Category,GeneralProductDTO>> getAllFlawSpecificProducts(Connection c) {
        Map<SpecificProductDTO,Map<Category,GeneralProductDTO>> specificProductDTOMapMap = new HashMap<>();
        try{
            Statement stmt;
            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery( "SELECT * FROM "+DBHandler.SPECIFIC_PRODUCT+" WHERE "+LOCATION+" = '"+LOCATION_STORAGE+"';" );
            while ( rs.next() ) {
                int pCode = rs.getInt(CODE);
                int  id = rs.getInt(ID);
                String location  = rs.getString(LOCATION);
                boolean isFlaw= rs.getBoolean(IS_FLAW);
                String expired=rs.getString(EXPIRED);
                Map<Category,GeneralProductDTO> categoryGeneralProductDTOMap = getGeneralProductByCode(c,pCode);
                GeneralProductDTO genProduct=categoryGeneralProductDTOMap.entrySet().iterator().next().getValue();
                try{
                    if(isFlaw|| DateFormat.getDateInstance().parse(expired).before(new Date())) {
                        SpecificProductDTO s = new SpecificProductDTO(id, location, isFlaw, expired, genProduct);
                        //products.add(new SpecificProductDTO(id, location, isFlaw, expired, genProduct));
                        specificProductDTOMapMap.put(s,categoryGeneralProductDTOMap);
                    }
                }catch(Exception e){}
            }
            rs.close();
            stmt.close();
            c.close();
        }catch(Exception e){
            //e.printStackTrace();
        }
        return specificProductDTOMapMap;
    }

    public int countSpecificProducts(Connection c) {

        int counter=0;
        try{
            Statement stmt;
            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM specific_product WHERE id = (SELECT MAX(id) FROM specific_product) ;" );

            while ( rs.next() ) {
                counter = rs.getInt("id");
            }
            rs.close();
            stmt.close();
            c.close();
        }catch(Exception e){
            //e.printStackTrace();
        }
        return counter;
    }

    public boolean makeFlaw(Connection c, int specificProductId) {
        try{
            Statement stmt = c.createStatement();
            stmt = c.createStatement();
            String sql = "UPDATE "+DBHandler.SPECIFIC_PRODUCT+" set "+IS_FLAW+" = "+1+" WHERE "+ID+" = "+ specificProductId +";" ;
            stmt.executeUpdate(sql);
            c.commit();
            stmt.close();
            c.close();
            return true;
        }catch(Exception e){
            //e.printStackTrace();
        }
        return false;
    }
}
