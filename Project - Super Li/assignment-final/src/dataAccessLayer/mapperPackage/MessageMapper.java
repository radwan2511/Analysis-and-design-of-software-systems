package dataAccessLayer.mapperPackage;

import businessLayer.deliveryPackage.Truck;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class MessageMapper { // new
    private String url;

    public MessageMapper(String url){
        this.url = url;
    }
// Messages Table = recipient , message , orderId
    public void addMessage(String recipient, String message, int orderId) throws Exception {
        String sql = "INSERT INTO Messages VALUES (?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, recipient);
            pstmt.setString(2, message);
            pstmt.setInt(3, orderId);
            pstmt.executeUpdate();
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    public void deleteMessages(String recipient) throws Exception{
        String sql = "DELETE FROM Messages WHERE recipient='" + recipient + "'";
        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement statement = conn.prepareStatement(sql)){
            statement.executeUpdate();
        } catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }

    public void deleteMessages(int orderId) throws Exception{
        String sql = "DELETE FROM Messages WHERE orderId='" + orderId + "'";
        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement statement = conn.prepareStatement(sql)){
            statement.executeUpdate();
        } catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }

    public List<String> getMessages(String recipient) throws Exception{
        String sql = "SELECT message from Messages where recipient='" + recipient + "'";
        List<String> messages = new LinkedList<>();
        try (Connection conn = DriverManager.getConnection(url);
             Statement statement = conn.createStatement()) {
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()) {
                messages.add(rs.getString(2));
            }
            return messages;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

}
