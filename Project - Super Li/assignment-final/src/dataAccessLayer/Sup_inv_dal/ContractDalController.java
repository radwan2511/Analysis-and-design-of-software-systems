package dataAccessLayer.Sup_inv_dal;

import dataAccessLayer.Sup_inv_dal.DTO.ContractDTO;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class ContractDalController {


    public static ArrayList<ContractDTO> getContract(Connection c, int supplier_id) {
        try {
            Statement stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery( "SELECT * FROM Supplier_Contract WHERE SUPPLIER_ID =" + supplier_id + ";" );
            ArrayList<ContractDTO> cp=new ArrayList<>();
            while ( rs.next() ) {
                cp.add(new ContractDTO(rs.getInt("SUPPLIER_ID"), rs.getInt("CONTRACT_NUM"), rs.getBoolean("CONSTANTDAYS"), rs.getInt("CODE"), rs.getString("QUANTITY_DISCOUNT"), rs.getInt("PRICE")));
            }
            return cp;
        }catch (Exception e){
        }
        return new ArrayList<>();
    }

    public ArrayList<ContractDTO> viewContract(Connection c) {
        try {
            Statement stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Supplier_Contract;");
            ArrayList<ContractDTO> cp = new ArrayList<>();
            while (rs.next()) {
                cp.add(new ContractDTO(rs.getInt("SUPPLIER_ID"), rs.getInt("CONTRACT_NUM"), rs.getBoolean("CONSTANTDAYS"), rs.getInt("CODE"), rs.getString("QUANTITY_DISCOUNT"), rs.getInt("PRICE")));
            }
            return cp;
        }catch (Exception e){

        }
        return new ArrayList<>();
    }

    public ArrayList<ContractDTO> selectContractwithProductCode(Connection c, int product_code) {
        try {
            Statement stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery( "SELECT * FROM Supplier_Contract WHERE CODE =" + product_code + ";" );
            ArrayList<ContractDTO> cp=new ArrayList<>();
            while ( rs.next() ) {
                cp.add(new ContractDTO(rs.getInt("SUPPLIER_ID"), rs.getInt("CONTRACT_NUM"), rs.getBoolean("CONSTANTDAYS"), rs.getInt("CODE"), rs.getString("QUANTITY_DISCOUNT"), rs.getInt("PRICE")));
            }
            return cp;
        }catch (Exception e){
        }
        return new ArrayList<>();
    }

    public void addContract(int supplierId, int code, int contract_num, double price, boolean constant, String quanity_discount, Connection c) {
        try {
            Statement stmt = c.createStatement();
            String sql = "INSERT INTO Supplier_Contract VALUES (" + supplierId + "," + code + "," + contract_num + "," +price + ",'" + constant + "','" + quanity_discount + "');";
            stmt.execute(sql);
            c.commit();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void deleteContract(int supplierId, Connection c) {
        try{
            Statement stmt = c.createStatement();
            String sql = "DELETE FROM Supplier_Contract WHERE SUPPLIER_ID="+supplierId +";";
            stmt.execute(sql);
            c.commit();
        }catch (Exception e){
        }
    }

    public ArrayList<ContractDTO> getContract_SupplierId_Contract_Num(Connection c, int supplier_id, int contract_num) {
        try {
            ArrayList<ContractDTO> arr= new ArrayList<>();
            Statement stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery( "SELECT * FROM Supplier_Contract WHERE SUPPLIER_ID="+supplier_id+" AND CONTRACT_NUM="+contract_num+ " ;" );
            while ( rs.next() ) {
                int id=rs.getInt("SUPPLIER_ID");
                int code=rs.getInt("CODE");
                int num=rs.getInt("CONTRACT_NUM");
                int price=rs.getInt("PRICE");
                boolean constant_days =rs.getBoolean("CONSTANTDAYS");
                String quantity_discount =rs.getString("QUANTITY_DISCOUNT");
                arr.add(new ContractDTO(id,num,constant_days,code,quantity_discount,price));
            }
            return arr;
        }catch (Exception e){

        }
        return new ArrayList<>();
    }

    public double getPriceFromContract(Connection c,int supplierId, int code) {
        try {
            Statement stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery( "SELECT * FROM Supplier_Contract WHERE SUPPLIER_ID="+supplierId+" AND CODE="+code+ " ;" );
            while ( rs.next() ) {
                return rs.getDouble("PRICE");
            }
        }catch (Exception e){

        }
        return -1;
    }
//    public ContractDalController() {
//        super("CONTRACT");
//    }
//
//    @Override
//    protected ContractDTO convert_to_DTO(ResultSet rs) {
//        try {
//            int id = rs.getInt(1);
//            int supplier_id = rs.getInt(2);
//            int product_code = rs.getInt(3);
//            int contactNum = rs.getInt(4);
//            String quantity_discount = rs.getString(5);
//            boolean constant_day = rs.getBoolean(6);
//            int quantity = rs.getInt(7);
//            int price = rs.getInt(8);
//            return new ContractDTO(id,supplier_id,contactNum,constant_day,product_code,quantity_discount,quantity, price);
//        }catch(Exception e){
//            System.out.println(e.getMessage());
//        }
//        return null;
//    }
//
//    public boolean insert(ContractDTO cdto){
//        String insert_queiry =
//                "INSERT INTO CONTRACT(id, supplierID, productCode, contractNumber, quantityDiscount, constantDay) " +
//                        "VALUES(?,?,?,?,?,?)";
//        int col_count = 0;
//        try(Connection connection = this.connect()){
//            PreparedStatement ps = connection.prepareStatement(insert_queiry);
//            ps.setInt(1, cdto.id);
//            ps.setInt(2, cdto.supplierId);
//            ps.setInt(3, cdto.product_code);
//            ps.setInt(4, cdto.contractNum);
//            ps.setString(5,cdto.quantity_discount);
//            ps.setBoolean(6,cdto.constantDaysDelivery);
//            col_count=ps.executeUpdate();
//        }
//        catch (Exception e){
//            System.out.println(e.getMessage());
//        }
//        return col_count>0;
//    }

}
