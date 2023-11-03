package dataAccessLayer.Sup_inv_dal;

import dataAccessLayer.Sup_inv_dal.DTO.OrderDTO;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class OrderDalController {

    public int countOrders(Connection c) {
        int counter=0;
        try{
            //Connection c=DBHandler.getInstance().open();
            Statement stmt;
            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM ORDER_TABLE WHERE ID = (SELECT MAX(ID) FROM ORDER_TABLE) ;" );

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

    public ArrayList<OrderDTO> viewOrder(Connection c) {
        try {
            Statement stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM ORDER_TABLE;");
            ArrayList<OrderDTO> oi = new ArrayList<>();
            while (rs.next()) {
                int Did = rs.getInt("Delivery_ID");
                oi.add(new OrderDTO(rs.getInt("ID"),rs.getInt("SUPPLIER_ID"), rs.getInt("YEAR"), rs.getInt("MONTH"), rs.getInt("DAY"), rs.getInt("CODE"), rs.getInt("QUANTITY"), rs.getString("STATUS"),rs.getDouble("PRICE"),Did));
            }
            c.close();
            return oi;
        }catch (Exception e){

        }
        return new ArrayList<>();
    }

    public void insertOrder(Connection c, OrderDTO item) {
        try{
            Statement stmt;
            String sql;
            stmt = c.createStatement();
            sql = "INSERT INTO ORDER_TABLE (ID,SUPPLIER_ID,YEAR,MONTH,DAY,CODE,QUANTITY,PRICE,STATUS,Delivery_ID) " +
                    "VALUES ("+item.id + ", "+item.supplier_id+", "+item.year+","+item.month+","+item.day+","+item.product_code+","+item.quantity+","+item.price+",'"+item.status+"', "+ item.delivery_id +");";
            stmt.execute(sql);
            c.commit();
            c.close();
        }catch (Exception e){

        }
    }

    public ArrayList<OrderDTO> getOrderFromSupplier(Connection c, int mid) {
        try{
            ArrayList<OrderDTO> arr= new ArrayList<>();
            Statement stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery( "SELECT * FROM ORDER_TABLE WHERE SUPPLIER_ID="+mid+";" );
            while ( rs.next() ) {
                int id=rs.getInt("ID");
                int sid=rs.getInt("SUPPLIER_ID");
                int code=rs.getInt("CODE");
                int amount=rs.getInt("QUANTITY");
                int year=rs.getInt("YEAR");
                int month=rs.getInt("MONTH");
                int day=rs.getInt("DAY");
                double price = rs.getDouble("PRICE");
                String status = rs.getString("STATUS");
                int Did = rs.getInt("Delivery_ID");
                arr.add(new OrderDTO(id,sid,year,month,day,code,amount,status,price,Did));
            }
            c.close();
            return arr;
        }catch (Exception e){

        }
        return new ArrayList<>();
    }

    public ArrayList<OrderDTO> getOrderFromID(Connection c, int id) {
        try{
            ArrayList<OrderDTO> arr= new ArrayList<>();
            Statement stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery( "SELECT * FROM ORDER_TABLE WHERE ID= "+id+" ;" );
            while ( rs.next() ) {
                int oid=rs.getInt("ID");
                int sid=rs.getInt("SUPPLIER_ID");
                int code=rs.getInt("CODE");
                int amount=rs.getInt("QUANTITY");
                int year=rs.getInt("YEAR");
                int month=rs.getInt("MONTH");
                int day=rs.getInt("DAY");
                double price = rs.getDouble("PRICE");
                String status = rs.getString("STATUS");
                int Did = rs.getInt("Delivery_ID");
                arr.add(new OrderDTO(oid,sid,year,month,day,code,amount,status,price,Did));
            }
            c.close();
            return arr;
        }catch (Exception e){

        }
        return new ArrayList<>();
    }

    public ArrayList<OrderDTO> getOrderFromDate(Connection c, int year1, int month1, int day1) {
        try{
            ArrayList<OrderDTO> arr= new ArrayList<>();
            Statement stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery( "SELECT * FROM ORDER_TABLE WHERE YEAR="+year1+" AND MONTH="+month1+" AND DAY="+day1+" ;" );
            while ( rs.next() ) {
                int id=rs.getInt("ID");
                int sid=rs.getInt("SUPPLIER_ID");
                int code=rs.getInt("CODE");
                int amount=rs.getInt("QUANTITY");
                int year=rs.getInt("YEAR");
                int month=rs.getInt("MONTH");
                int day=rs.getInt("DAY");
                double price = rs.getDouble("PRICE");
                String status = rs.getString("STATUS");
                int Did = rs.getInt("Delivery_ID");
                arr.add(new OrderDTO(id,sid,year,month,day,code,amount,status,price,Did));
            }
            c.close();
            return arr;
        }catch (Exception e){

        }
        return new ArrayList<>();
    }

    public OrderDTO getOrderFromDateAndCode(Connection c, Integer code, int year, int month, int day) {
        OrderDTO odto = null;
        try{
            Statement stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery( "SELECT * FROM ORDER_TABLE WHERE YEAR="+year+" AND MONTH="+month+" AND DAY="+day+" AND CODE="+code+" ;" );
            while ( rs.next() ) {
                int id=rs.getInt("ID");
                int sid=rs.getInt("SUPPLIER_ID");
                int code1=rs.getInt("CODE");
                int amount=rs.getInt("QUANTITY");
                int year1=rs.getInt("YEAR");
                int month1=rs.getInt("MONTH");
                int day1=rs.getInt("DAY");
                double price = rs.getDouble("PRICE");
                String status = rs.getString("STATUS");
                int Did = rs.getInt("Delivery_ID");
                odto = new OrderDTO(id,sid,year1,month1,day1,code1,amount,status,price,Did);
            }
            c.close();
        }catch (Exception e){
        }
        return odto;
    }

    public OrderDTO getOrderFromSupplierAndCodeAndDate(Connection c, int supplier_id, int product_code, int year, int month, int day) {
        try {
            Statement stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM ORDER_TABLE WHERE SUPPLIER_ID=" + supplier_id + " AND CODE=" + product_code + " AND YEAR=" + year + " AND MONTH=" + month + " AND DAY=" + day + " ;");
            while (rs.next()) {
                int id=rs.getInt("ID");
                int sid=rs.getInt("SUPPLIER_ID");
                int code=rs.getInt("CODE");
                int amount=rs.getInt("QUANTITY");
                int year2=rs.getInt("YEAR");
                int month2=rs.getInt("MONTH");
                int day2=rs.getInt("DAY");
                double price = rs.getDouble("PRICE");
                String status = rs.getString("STATUS");
                int Did = rs.getInt("Delivery_ID");
                return new OrderDTO(id,sid,year,month,day,code,amount,status,price,Did);
            }
            c.close();
        }catch (Exception e){
        }
        return null;
    }

    public void deleteOrder(Connection c, int supplier_id, int product_code, int year, int month, int day) {
        try{
            Statement stmt = c.createStatement();
            String sql = "DELETE FROM ORDER_TABLE WHERE SUPPLIER_ID="+supplier_id +" AND CODE=" + product_code + " AND YEAR=" + year+" AND MONTH=" + month+" AND DAY=" + day + ";";
            stmt.execute(sql);
            c.commit();
            c.close();
        }catch (Exception e){
        }
    }

    public void deleteAllOrder(Connection c, int id) {
        try{
            Statement stmt = c.createStatement();
            String sql = "DELETE FROM ORDER_TABLE WHERE ID="+id + ";";
            stmt.execute(sql);
            c.commit();
            c.close();
        }catch (Exception e){
        }
    }

    public void UpdateOrderStatus(Connection c, String done, int did) {
        try{
            Statement stmt = c.createStatement();
            String sql ="UPDATE ORDER_TABLE "+
                    "SET STATUS = '"+done+"' WHERE Delivery_ID="+ did + ";";
            stmt.execute(sql);
            c.commit();
            c.close();
        }catch (Exception e){

        }
    }

    public void UpdateAllOrderStatus(Connection c, int id,String done) {
        try{
            Statement stmt = c.createStatement();
            String sql ="UPDATE ORDER_TABLE "+
                    "SET STATUS = '"+done+"' WHERE ID="+id +";";
            stmt.execute(sql);
            c.commit();
            c.close();
        }catch (Exception e){

        }
    }

    public void deleteSupplierOrders(Connection c,int supplier_id) {
        try{
            Statement stmt = c.createStatement();
            String sql = "DELETE FROM ORDER_TABLE WHERE SUPPLIER_ID="+supplier_id +";";
            stmt.execute(sql);
            c.commit();
            c.close();
        }catch (Exception e){

        }
    }

    public void deleteOrderYearMonthDay(Connection c, int year, int month, int day) {
        try{
            Statement stmt = c.createStatement();
            String sql = "DELETE FROM ORDER_TABLE WHERE YEAR="+year +" AND MONTH="+month+ " AND DAY="+day +";";
            stmt.execute(sql);
            c.commit();
            c.close();
        }catch (Exception e){

        }
    }

    public void deleteOrderFromID(Connection c, int id) {
        try{
            Statement stmt = c.createStatement();
            String sql = "DELETE FROM ORDER_TABLE WHERE ID= "+id +" ;";
            stmt.execute(sql);
            c.commit();
            c.close();
        }catch (Exception e){

        }
    }

    public ArrayList<OrderDTO> getOrderFromDeliveryID(Connection c, int did) {
        try{
            ArrayList<OrderDTO> arr= new ArrayList<>();
            Statement stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery( "SELECT * FROM ORDER_TABLE WHERE Delivery_ID= "+did+" ;" );
            while ( rs.next() ) {
                int id=rs.getInt("ID");
                int sid=rs.getInt("SUPPLIER_ID");
                int code=rs.getInt("CODE");
                int amount=rs.getInt("QUANTITY");
                int year=rs.getInt("YEAR");
                int month=rs.getInt("MONTH");
                int day=rs.getInt("DAY");
                double price = rs.getDouble("PRICE");
                String status = rs.getString("STATUS");
                int Did = rs.getInt("Delivery_ID");
                arr.add(new OrderDTO(id,sid,year,month,day,code,amount,status,price,Did));
            }
            c.close();
            return arr;
        }catch (Exception e){

        }
        return new ArrayList<>();
    }

    public ArrayList<OrderDTO> getActiveOrders(Connection c) {
        try{
            ArrayList<OrderDTO> arr= new ArrayList<>();
            Statement stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery( "SELECT * FROM ORDER_TABLE WHERE STATUS= '"+"in progress"+"' ;" );
            while ( rs.next() ) {
                int id=rs.getInt("ID");
                int sid=rs.getInt("SUPPLIER_ID");
                int code=rs.getInt("CODE");
                int amount=rs.getInt("QUANTITY");
                int year=rs.getInt("YEAR");
                int month=rs.getInt("MONTH");
                int day=rs.getInt("DAY");
                double price = rs.getDouble("PRICE");
                String status = rs.getString("STATUS");
                int Did = rs.getInt("Delivery_ID");
                arr.add(new OrderDTO(id,sid,year,month,day,code,amount,status,price,Did));
            }
            c.close();
            return arr;
        }catch (Exception e){

        }
        return new ArrayList<>();
    }


//    public OrderDalController() {
//        super("ORDER");
//    }
//
//    @Override
//    protected OrderDTO convert_to_DTO(ResultSet rs) {
//        try {
//            int id = rs.getInt(1);
//            int product_code = rs.getInt(2);
//            int supplier_id = rs.getInt(3);
//            int quantity = rs.getInt(4);
//            String status = rs.getString(5);
//            Date order_date = rs.getDate(6);
//            return new OrderDTO(id,product_code,order_date,quantity,supplier_id,status);
//        }catch(Exception e){
//            System.out.println(e.getMessage());
//        }
//        return null;
//    }
//
//    public boolean insert(OrderDTO odto){
//        String insert_queiry =
//                "INSERT INTO ORDER(id, productCode, supplierID, quantity, status, orderDate) " +
//                        "VALUES(?,?,?,?,?,?)";
//        int col_count = 0;
//        try(Connection connection = this.connect()){
//            PreparedStatement ps = connection.prepareStatement(insert_queiry);
//            ps.setInt(1, odto.id);
//            ps.setInt(2, odto.product_code);
//            ps.setInt(3, odto.supplier_id);
//            ps.setInt(4, odto.quantity);
//            ps.setString(5,odto.status);
//            ps.setDate(6,(Date) odto.date);
//            col_count=ps.executeUpdate();
//        }
//        catch (Exception e){
//            System.out.println(e.getMessage());
//        }
//        return col_count>0;
//    }
}
