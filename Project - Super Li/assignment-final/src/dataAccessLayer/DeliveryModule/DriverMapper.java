package dataAccessLayer.DeliveryModule;

import businessLayer.deliveryPackage.Driver;
import businessLayer.deliveryPackage.Truck;

import java.sql.*;
import java.util.*;
import java.util.Date;

public class DriverMapper {
    private IdentityMap cache;
    private String url;

    public DriverMapper(String url) {
        cache = IdentityMap.getInstance();
        this.url = url;
    }

    public void initDriversTable() throws SQLException {
        String sqlStatement1 = "insert into Drivers values(\"111111111\", \"Robert\", \"C\");";
        String sqlStatement2 = "insert into Drivers values(\"555555555\", \"Mike\", \"C1\");";
        try{
            Connection connection = DriverManager.getConnection(url);
            PreparedStatement statement1 = connection.prepareStatement(sqlStatement1);
            PreparedStatement statement2 = connection.prepareStatement(sqlStatement2);
            statement1.executeUpdate();
            statement1.close();
            statement2.executeUpdate();
            statement2.close();
        }catch (SQLException e){
            throw new SQLException(e.getMessage());
        }
    }

    public void addDriver (String id, String name, String license) throws Exception{
        //double weightLimit = convertLicense(license);
        String sql = "INSERT INTO Drivers (id, name, license) VALUES (?,?,?)";
        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setString(1, id);
            statement.setString(2, name);
            statement.setString(3, license);
            statement.executeUpdate();
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
        cache.getDrivers().put(id, new Driver(id, name, license));
    }

    public Driver getAvailableDriver(Date date, String shift, double weight, ArrayList<String> availableDrivers) throws Exception {

        Driver driver = null;
        String sql = "SELECT * FROM Drivers";
        boolean found = false;
        try (Connection conn = DriverManager.getConnection(url);
             Statement statement = conn.createStatement();) {
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next() && !found) {
                if(convertLicense(rs.getString(3)) >= weight && availableDrivers.contains(rs.getString(1))){
                    driver = new Driver(rs.getString(1),
                            rs.getString(2),
                            rs.getString(3));
                    found = true;
                }
            }
            if(found){
                cache.getDrivers().put(driver.getId(), driver);
                return driver;
            }
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }

        throw new Exception("There is no available driver");
    }


    public void deleteDriver(String driverId) throws Exception{
        String sql = "DELETE FROM Drivers WHERE id='" + driverId + "'";
        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement statement = conn.prepareStatement(sql)){
            statement.executeUpdate();
        } catch (Exception e){
            throw new Exception(e.getMessage());
        }
        cache.getDrivers().remove(driverId);
    }

    public List<String> getAllDriversIDs() throws Exception{
        String sql = "SELECT id FROM Drivers";
        List<String> ids = new LinkedList<>();
        try (Connection conn = DriverManager.getConnection(url);
             Statement statement = conn.createStatement()) {
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()) {
                ids.add(rs.getString(1));
            }
            return ids;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    public List<String> getDriversByLicense(String license) throws Exception{
        String sql = "SELECT id FROM Drivers WHERE license='" + license + "'";
        List<String> ids = new LinkedList<>();
        try (Connection conn = DriverManager.getConnection(url);
             Statement statement = conn.createStatement()) {
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()) {
                ids.add(rs.getString(1));
            }
            return ids;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    private double convertLicense(String license) throws Exception {
        if(license.equals("C1"))
            return 12000;
        else if (license.equals("C"))
            return Double.MAX_VALUE;
        throw new Exception("license type is invalid");
    }

}
