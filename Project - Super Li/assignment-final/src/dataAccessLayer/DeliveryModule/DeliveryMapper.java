package dataAccessLayer.DeliveryModule;

import businessLayer.deliveryPackage.Delivery;
import businessLayer.deliveryPackage.Item;
import businessLayer.deliveryPackage.Location;
import dataAccessLayer.DalController;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

public class DeliveryMapper {
    private IdentityMap cache;
    private DalController dalController;
    private String url;

    public DeliveryMapper(String url, DalController dalController) throws Exception {
        cache = IdentityMap.getInstance();
        this.dalController = dalController;
        this.url = url;
    }

    public void initDeliveriesTable() throws SQLException {
        String sqlStatement1 = "INSERT INTO Deliveries values(2000, \"17/09/2022\", \"10:00\", \"123456\", \"111111111\" , \"Tel-Aviv\", \"0526985214\", \"Danny\");";
        try{
            Connection connection = DriverManager.getConnection(url);
            PreparedStatement statement1 = connection.prepareStatement(sqlStatement1);
            statement1.executeUpdate();
            statement1.close();
        }catch (SQLException e){
            throw new SQLException(e.getMessage());
        }
    }

    public Delivery addDelivery(int deliveryId, String  date, String launchTime, String plateNumber, String driverId, Location source, Map<Location, LinkedList<Item>> itemsPerDest) throws Exception {
        String sql = "INSERT INTO Deliveries VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setInt(1, deliveryId);
            statement.setString(2, date);
            statement.setString(3, launchTime);
            statement.setString(4, plateNumber);
            statement.setString(5, driverId);
            statement.setString(6, source.getAddress());
            statement.setString(7, source.getPhoneNumber());
            statement.setString(8, source.getContactName());
            statement.executeUpdate();
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }

        Delivery delivery = new Delivery(deliveryId, date, launchTime, plateNumber, driverId, source, itemsPerDest);
        cache.getDeliveries().put(deliveryId, delivery);
        return delivery;
    }

    public List<Delivery> getAllDeliveries() throws Exception{
        HashMap<Integer, Delivery> deliveries = new HashMap<>();
        String sql = "SELECT * FROM Deliveries";
        try (Connection conn = DriverManager.getConnection(url);
             Statement statement = conn.createStatement()) {
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()) {
                deliveries.put(rs.getInt(1), new Delivery(rs.getInt(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getString(4),
                        rs.getString(5),
                        new Location(rs.getString(6),
                                rs.getString(7),
                                rs.getString(8)),
                        dalController.getItemsPerDest(rs.getInt(1))));
            }
            cache.setDeliveries(deliveries);
            return new LinkedList<>(deliveries.values());
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

//    public Delivery getDelivery(Date date, String launchTime, String plateNumber) throws Exception {
//        Delivery delivery = null;
//        String sql = "SELECT * FROM Deliveries WHERE date='" + date + "' AND launchTime='" + launchTime + "' AND truckPlateNumber='" + plateNumber + "'";
//        try (Connection conn = DriverManager.getConnection(url);
//             Statement statement = conn.createStatement()) {
//            ResultSet rs = statement.executeQuery(sql);
//            if (rs.next()) {
//                delivery = new Delivery(rs.getInt(1),
//                        rs.getString(2),
//                        rs.getString(3),
//                        rs.getString(4),
//                        rs.getString(5),
//                        new Location(rs.getString(6),
//                                rs.getString(7),
//                                rs.getString(8)),
//                        dalController.getItemsPerDest(rs.getInt(1))
//                );
//            }
//        } catch (Exception e) {
//            throw new Exception(e.getMessage());
//        }
//        if(delivery != null){
//            cache.getDeliveries().put(delivery.getId(), delivery);
//            return delivery;
//        }
//        throw new Exception("This delivery is not exist");
//    }

    public Delivery getDelivery(int id) throws Exception{
        if(cache.getDeliveries().containsKey(id))
            return cache.getDeliveries().get(id);
        Delivery delivery = null;
        String sql = "SELECT * FROM Deliveries WHERE id='" + id + "'";
        try (Connection conn = DriverManager.getConnection(url);
             Statement statement = conn.createStatement()) {
            ResultSet rs = statement.executeQuery(sql);
            if (rs.next()) {
                delivery = new Delivery(rs.getInt(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getString(4),
                        rs.getString(5),
                        new Location(rs.getString(6),
                                rs.getString(7),
                                rs.getString(8)),
                        dalController.getItemsPerDest(rs.getInt(1))
                );
            }
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
        if(delivery != null){
            cache.getDeliveries().put(delivery.getId(), delivery);
            return delivery;
        }
        throw new Exception("This delivery is not exist");
    }

//    public Delivery cancelDelivery(Date date, String launchTime, String plateNumber) throws Exception {
//        Delivery delivery = null;
//        try {
//            delivery = getDelivery(date, launchTime, plateNumber);
//            if(delivery == null)
//                throw new Exception("This Delivery is not exist!");
//            dalController.deleteDocuments(delivery.getId());
//            dalController.deleteTruckScheduler(plateNumber, date, convertTimeToShift(launchTime));
//            cache.getDeliveries().remove(delivery.getId());
//        } catch (Exception e){
//            throw new Exception(e.getMessage());
//        }
//
//        String sql = "DELETE FROM Deliveries WHERE id=" + delivery.getId() + "'";
//        try (Connection conn = DriverManager.getConnection(url);
//             PreparedStatement statement = conn.prepareStatement(sql)){
//            statement.executeUpdate();
//        }catch (Exception e){
//            throw new Exception(e.getMessage());
//        }
//        return delivery;
//    }

    public Delivery cancelDelivery(int deliveryId) throws Exception {
        Delivery delivery = null;
        try {
            delivery = getDelivery(deliveryId);
            if(delivery == null)
                throw new Exception("This Delivery is not exist!");
            dalController.deleteDocuments(delivery.getId());
            dalController.deleteTruckScheduler(delivery.getTruckPlateNumber(), delivery.getDate(), convertTimeToShift(delivery.getLaunchTime()));
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
            dalController.deleteEmpFromShift(format.parse(delivery.getDate()), convertTimeToShift(delivery.getLaunchTime()), delivery.getDriverId());
            cache.getDeliveries().remove(delivery.getId());
        } catch (Exception e){
            throw new Exception(e.getMessage());
        }

        String sql = "DELETE FROM Deliveries WHERE id='" + delivery.getId() + "'";
        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement statement = conn.prepareStatement(sql)){
            statement.executeUpdate();
        }catch (Exception e){
            throw new Exception(e.getMessage());
        }
        return delivery;
    }

    public boolean checkIfShiftHasDelivery (String date, String shift) throws Exception {
        String sql = "SELECT launchTime FROM Deliveries WHERE date='" + date + "'";
        try (Connection conn = DriverManager.getConnection(url);
             Statement statement = conn.createStatement()) {
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()) {
                if(shift.equals(convertTimeToShift(rs.getString("launchTime"))))
                    return true;
            }
            return false;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    private String convertTimeToShift(String time){
        String[] split = time.split(":");
        try{
            if(Integer.parseInt(split[0]) < 15)
                return "morning";
            else
                return "evening";
        } catch (Exception e){
            return "Invalid input - It's not in format hh:mm";
        }
    }
}
