package dataAccessLayer.Sup_inv_dal;

import dataAccessLayer.Sup_inv_dal.DTO.SupplierDTO;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class SupplierDalController  {
    //private DBHandler dbHandler = DBHandler.getInstance();

    public int countSuppliers(Connection c) {
        int counter=0;
        try{
            //Connection c=DBHandler.getInstance().open();
            Statement stmt;
            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM SUPPLIER WHERE ID = (SELECT MAX(ID) FROM SUPPLIER) ;" );

            while ( rs.next() ) {
                counter = rs.getInt("ID");
            }
            rs.close();
            stmt.close();
            c.close();
        }catch(Exception e){
            //e.printStackTrace();
        }
        return counter;
    }

    public ArrayList<SupplierDTO> viewSuppliers(Connection c) {
        try{
            Statement stmt2 = c.createStatement();
            ResultSet rs = stmt2.executeQuery( "SELECT * FROM SUPPLIER;" );
            ArrayList<SupplierDTO> a1=new ArrayList<>();
            while ( rs.next() ) {
                a1.add(new SupplierDTO(rs.getInt("ID"), rs.getString("NAME") , rs.getString("ADDRESS"), rs.getString("PHONE"), rs.getInt("TYPE"), rs.getString("BANK") , rs.getString("DAY_WORKS"),rs.getInt("COMPANY_NUM")));
            }
            c.close();
            return a1;
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public void insertSupplier(Connection c, SupplierDTO s1) {
        try{
            Statement stmt;
            String sql;
            stmt = c.createStatement();
            sql = "INSERT INTO SUPPLIER (ID,NAME,ADDRESS,PHONE,TYPE,BANK,DAY_WORKS,COMPANY_NUM) " +
                    "VALUES ("+s1.id+", '"+s1.name+"','"+s1.address+"', '"+s1.PhoneNumber+"', "+s1.SupplierType+",'"+s1.bank+"', "+null+", "+s1.companyNumber+");";
            stmt.execute(sql);
            c.commit();
            c.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    public void deleteSupplier(Connection c, int id) {
        try {
            Statement stmt = c.createStatement();
            String sql = "DELETE FROM SUPPLIER WHERE ID=" + id + ";";
            stmt.execute(sql);
            c.commit();
            c.close();
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    public SupplierDTO getSupplier(Connection c, int id) {
        SupplierDTO sup=null;
        try {
            Statement stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery( "SELECT * FROM SUPPLIER WHERE ID="+id+";" );
            while ( rs.next() ) {
                int supID = rs.getInt("ID");
                String name = rs.getString("NAME");
                String bank = rs.getString("BANK");
                String phone = rs.getString("PHONE");
                String address = rs.getString("ADDRESS");
                int type = rs.getInt("TYPE");
                int company_num = rs.getInt("COMPANY_NUM");
                String work_days = rs.getString("DAY_WORKS");
                sup=new SupplierDTO(supID,name , address, phone,type, bank, work_days,company_num);

            }
            c.close();
        }catch (Exception e){
        }
        return sup;
    }

    public SupplierDTO getSupplierByPhoneNumber(Connection c, String number) {
        SupplierDTO sup=null;
        try {
            Statement stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery( "SELECT * FROM SUPPLIER WHERE PHONE= '"+number+"' ;" );
            while ( rs.next() ) {
                int supID = rs.getInt("ID");
                String name = rs.getString("NAME");
                String bank = rs.getString("BANK");
                String phone = rs.getString("PHONE");
                String address = rs.getString("ADDRESS");
                int type = rs.getInt("TYPE");
                int company_num = rs.getInt("COMPANY_NUM");
                String work_days = rs.getString("DAY_WORKS");
                sup=new SupplierDTO(supID,name , address, phone,type, bank, work_days,company_num);

            }
            c.close();
        }catch (Exception e){
        }
        return sup;
    }

    public void UpdateWorkDays(Connection c, int supplier_id, String workDays) {
        try{
            Statement stmt = c.createStatement();
            String sql ="UPDATE SUPPLIER "+
                    "SET DAY_WORKS = '"+workDays +"' WHERE ID="+supplier_id + ";";
            stmt.execute(sql);
            c.commit();
            c.close();
        }catch (Exception e){

        }
    }


//    public SupplierDalController() {
//        super("SUPPLIER");
//    }
//
//    @Override
//    protected SupplierDTO convert_to_DTO(ResultSet rs) {
//        try {
//            int id = rs.getInt(1);
//            String name = rs.getString(2);
//            String address = rs.getString(3);
//            String phoneNumber = rs.getString(4);
//            int workMethod = rs.getInt(5);
//            String bank = rs.getString(6);
//            String paymentMethod = rs.getString(7);
//            int contactId = rs.getInt(8);
//            String workDays = rs.getString(9);
//            int companyNum = rs.getInt(10);
//            return new SupplierDTO(id,name,address,phoneNumber,workMethod,bank,paymentMethod,workDays,contactId,companyNum);
//        }catch(Exception e){
//            System.out.println(e.getMessage());
//        }
//        return null;
//    }
//
//    public boolean insert(SupplierDTO sdto){
//        String insert_queiry =
//                "INSERT INTO SUPPLIER(id, name, address, phoneNumber," +
//                        " workMethod, bank, paymentMethod, contactID, workDays) VALUES(?,?,?,?,?,?)";
//        int col_count = 0;
//        try(Connection connection = this.connect()){
//            PreparedStatement ps = connection.prepareStatement(insert_queiry);
//            ps.setInt(1, sdto.id);
//            ps.setString(2, sdto.name);
//            ps.setString(3, sdto.address);
//            ps.setString(4, sdto.PhoneNumber);
//            ps.setInt(5,sdto.SupplierType);
//            ps.setString(6,sdto.bank);
//            ps.setString(7,sdto.TermOfPayment);
//            ps.setInt(8,sdto.contact_id);
//            ps.setString(9,sdto.workDays);
//            col_count=ps.executeUpdate();
//        }
//        catch (Exception e){
//            System.out.println(e.getMessage());
//        }
//        return col_count>0;
//    }
//
//    public void update1(int attributeValue, int ColumnValue) {
//        String sql_query = "UPDATE SUPPLIER SET contactID = ? WHERE id = ?";
//
//        try (Connection connection = this.connect();
//             PreparedStatement ps = connection.prepareStatement(sql_query)) {
//            // set the corresponding param
//            ps.setInt(1, attributeValue);
//            ps.setInt(2, ColumnValue);
//            // update
//            ps.executeUpdate();
//        } catch (SQLException e) {
//            System.out.println(e.getMessage());
//        }
//    }
//
//    public void update2(String attributeValue, int ColumnValue) {
//        String sql_query = "UPDATE SUPPLIER SET workDays = ? WHERE id = ?";
//
//        try (Connection connection = this.connect();
//             PreparedStatement ps = connection.prepareStatement(sql_query)) {
//            // set the corresponding param
//            ps.setString(1, attributeValue);
//            ps.setInt(2, ColumnValue);
//            // update
//            ps.executeUpdate();
//        } catch (SQLException e) {
//            System.out.println(e.getMessage());
//        }
//    }

}
