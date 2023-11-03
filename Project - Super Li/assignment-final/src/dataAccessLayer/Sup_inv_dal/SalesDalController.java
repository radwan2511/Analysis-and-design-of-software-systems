package dataAccessLayer.Sup_inv_dal;

import dataAccessLayer.Sup_inv_dal.DTO.SaleDTO;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class SalesDalController {

    private static final String SALEID="sale_id";

    private static final String SALETYPE="sale_type";

    private static final String DATE="date";

    private static final String SALEPERCENTAGE="sale_percentage";

    private static final String ID="id";


    public int countSales(Connection c) {
        int counter=0;
        try{
            Statement stmt;
            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM sale WHERE sale_id = (SELECT MAX(sale_id) FROM sale) ;" );

            while ( rs.next() ) {
                counter = rs.getInt("sale_id");
            }
            rs.close();
            stmt.close();
            c.close();
        }catch(Exception e){
            //e.printStackTrace();
        }
        return counter;

    }

    public SaleDTO addSale(Connection c, int sale_type, String date, String sale_percentage, int id, int sale_id) {
        try{
            Statement stmt = c.createStatement();
            stmt = c.createStatement();
            String sql = "INSERT INTO "+DBHandler.SALE+" ("+SALEID+","+SALETYPE+","+DATE+","+SALEPERCENTAGE+","+ID+") " +
                    "VALUES ("+ sale_id +", "+sale_type +", '"+ date+"', '" +sale_percentage+"', "+id+");";
            stmt.executeUpdate(sql);
            c.commit();
            stmt.close();
            c.close();
            return new SaleDTO(sale_type,date,sale_percentage,sale_id);
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
