package dataAccessLayer.DeliveryModule;

import businessLayer.deliveryPackage.Truck;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class TruckMapper {
    private IdentityMap cache;
    private String url;

    public TruckMapper(String url) {
        cache = IdentityMap.getInstance();
        this.url = url;
    }

    public void initTrucksTable() throws SQLException {
        String sqlStatement1 = "insert into Trucks values(\"123456\", \"Toyota\", 5000, 16000);";
        String sqlStatement2 = "insert into Trucks values(\"456789\", \"Toyota\", 2000, 8000);";
        String sqlStatement3 = "insert into Trucks values(\"789123\", \"Mercedes\", 7000, 20000);";
        try{
            Connection connection = DriverManager.getConnection(url);
            PreparedStatement statement1 = connection.prepareStatement(sqlStatement1);
            PreparedStatement statement2 = connection.prepareStatement(sqlStatement2);
            PreparedStatement statement3 = connection.prepareStatement(sqlStatement3);
            statement1.executeUpdate();
            statement1.close();
            statement2.executeUpdate();
            statement2.close();
            statement3.executeUpdate();
            statement3.close();
        }catch (SQLException e){
            throw new SQLException(e.getMessage());
        }
    }

    public void initTrucksSchedulerTable() throws SQLException {
        String sqlStatement1 = "insert into TrucksScheduler values(\"123456\", \"17/09/2022\", \"morning\");";
        try{
            Connection connection = DriverManager.getConnection(url);
            PreparedStatement statement1 = connection.prepareStatement(sqlStatement1);
            statement1.executeUpdate();
            statement1.close();
        }catch (SQLException e){
            throw new SQLException(e.getMessage());
        }
    }

    public void addTruck (String plateNumber, String model, double netWeight, double maxWeight) throws Exception {
        if(cache.getTrucks().containsKey(plateNumber))
            throw new Exception("Truck already exists!");
        Truck t = null;
        try{
            t = getTruck(plateNumber);
        } catch (Exception e){
            if(e.getMessage().equals("This truck is not exist")){
                insertTruck(plateNumber, model, netWeight, maxWeight);
                cache.getTrucks().put(plateNumber, new Truck(plateNumber, model, netWeight, maxWeight));
            }
            else throw new Exception(e.getMessage());
        }

        if(t != null)
            throw new Exception("Truck plate number already exists!");

    }

    public Truck getTruck(String plateNumber) throws Exception{
        if(cache.getTrucks().containsKey(plateNumber))
            return cache.getTrucks().get(plateNumber);
        Truck truck = null;
        String sql = "SELECT * FROM Trucks WHERE plateNumber= '" + plateNumber + "'";
        try (Connection conn = DriverManager.getConnection(url);
             Statement statement = conn.createStatement()) {
            ResultSet rs = statement.executeQuery(sql);
            if (rs.next()) {
                truck = new Truck(rs.getString(1),
                        rs.getString(2),
                        rs.getDouble(3),
                        rs.getDouble(4)
                );
            }
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
        if(truck != null){
            cache.getTrucks().put(plateNumber, truck);
            return truck;
        }
        throw new Exception("This truck is not exist");
    }

    public List<Truck> getAllTrucks() throws Exception{
        String sql = "SELECT * FROM Trucks";
        HashMap<String, Truck> trucks = new HashMap<>();
        try (Connection conn = DriverManager.getConnection(url);
             Statement statement = conn.createStatement()) {
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()) {
                trucks.put(rs.getString(1), new Truck(rs.getString(1),
                        rs.getString(2),
                        rs.getDouble(3),
                        rs.getDouble(4)
                ));
            }
            cache.setTrucks(trucks);
            return new LinkedList<>(trucks.values());
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    public Truck getAvailableTruck(Date date, String shift, double weight) throws Exception{
        Truck truck = null;
        String sql = "SELECT * FROM\n" +
                "((SELECT t.plateNumber, t.model, t.netWeight, t.maxWeight FROM Trucks as t WHERE t.maxWeight>= "
                + weight + " EXCEPT " + "SELECT t.plateNumber, t.model, t.netWeight, t.maxWeight FROM TrucksScheduler AS ts\n" +
                "JOIN Trucks as t ON ts.plateNumber=t.plateNumber) as notscheduled)\n" +
                "UNION\n" + "SELECT t.plateNumber, t.model, t.netWeight, t.maxWeight FROM TrucksScheduler AS ts\n" +
                "JOIN Trucks as t ON ts.plateNumber=t.plateNumber WHERE t.maxWeight>= " + weight + " AND ts.Date != '" +
                new SimpleDateFormat("dd/MM/yyyy").format(date) + "' OR ts.shift='" + shift + "'";
        try (Connection conn = DriverManager.getConnection(url);
             Statement statement = conn.createStatement()) {
            ResultSet rs = statement.executeQuery(sql);
            if (rs.next()) {
                truck = new Truck(rs.getString(1),
                        rs.getString(2),
                        rs.getDouble(3),
                        rs.getDouble(4)
                );
            }

        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
        if(truck != null){
            //insertTruckScheduler(truck.getPlateNumber(), date, shift);
            cache.getTrucks().put(truck.getPlateNumber(), truck);
            return truck;
        }
        throw new Exception("There is no available truck");
    }

    public void addTruckScheduler(String plateNumber, Date date, String shift) throws Exception{
        String sql = "INSERT INTO TrucksScheduler (plateNumber, Date, shift) VALUES (?,?,?)";
        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, plateNumber);
            pstmt.setString(2, new SimpleDateFormat("dd/MM/yyyy").format(date));
            pstmt.setString(3, shift);
            pstmt.executeUpdate();
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    public void deleteTruckScheduler(String plateNumber, String date, String shift) throws Exception{
        String sql = "DELETE FROM TrucksScheduler WHERE plateNumber='" + plateNumber + "' AND Date='" + date + "' AND shift='" + shift + "'";
        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement statement = conn.prepareStatement(sql)){
            statement.executeUpdate();
        } catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }

    public Truck deleteTruck (String plateNumber) throws Exception{
        if(checkTruckScheduler(plateNumber))
            throw new Exception("Can't delete truck, because it's in use");
        Truck truck = getTruck(plateNumber);
        String sql = "DELETE FROM Trucks WHERE plateNumber='" + plateNumber + "'";
        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement statement = conn.prepareStatement(sql)){
            statement.executeUpdate();
        } catch (Exception e){
            throw new Exception(e.getMessage());
        }
        cache.getTrucks().remove(plateNumber);
        return truck;
    }

    private void insertTruck(String plateNumber, String model, double netWeight, double maxWeight) throws Exception {
        String sql = "INSERT INTO Trucks (plateNumber, model, netWeight, maxWeight) VALUES (?,?,?,?)";
        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setString(1, plateNumber);
            statement.setString(2, model);
            statement.setDouble(3, netWeight);
            statement.setDouble(4, maxWeight);
            ;
            statement.executeUpdate();
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    private boolean checkTruckScheduler(String plateNumber) throws Exception{
        String sql = "SELECT * FROM TrucksScheduler WHERE plateNumber= '" + plateNumber + "'";
        try (Connection conn = DriverManager.getConnection(url);
             Statement statement = conn.createStatement()) {
            ResultSet rs = statement.executeQuery(sql);
            if (rs.next()) {
                return true;
            }
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
        return false;
    }
}
