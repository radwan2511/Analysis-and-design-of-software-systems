package dataAccessLayer.DeliveryModule;

import businessLayer.deliveryPackage.*;

import java.sql.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class DocumentMapper {
    private IdentityMap cache;
    private String url;

    public DocumentMapper(String url) {
        cache = IdentityMap.getInstance();
        this.url = url;
    }

    public void initDocumentsTable() throws SQLException {
        String sqlStatement1 = "insert into Documents values(123, 2000 , 14000.0, \"Beer-sheba\" , \"0365214520\", \"Ahmad\");";
        String sqlStatement2 = "insert into Documents values(124, 2000 , 14000.0, \"Eilat\" , \"0523658745\", \"Mohamad\");";
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

    public void initItemsTable() throws SQLException {
        String sqlStatement1 = "insert into Items values(1234, 111, \"milk\", 700, 3);";
        String sqlStatement2 = "insert into Items values(1234, 234, \"pasta\", 1000, 2);";
        String sqlStatement3 = "insert into Items values(1234, 549, \"coca-cola\", 300, 9);";
        String sqlStatement4 = "insert into Items values(1235, 111, \"milk\", 400, 3);";
        String sqlStatement5 = "insert into Items values(1235, 872, \"water\", 500, 12);";
        try{
            Connection connection = DriverManager.getConnection(url);
            PreparedStatement statement1 = connection.prepareStatement(sqlStatement1);
            PreparedStatement statement2 = connection.prepareStatement(sqlStatement2);
            PreparedStatement statement3 = connection.prepareStatement(sqlStatement3);
            PreparedStatement statement4 = connection.prepareStatement(sqlStatement4);
            PreparedStatement statement5 = connection.prepareStatement(sqlStatement5);
            statement1.executeUpdate();
            statement1.close();
            statement2.executeUpdate();
            statement2.close();
            statement3.executeUpdate();
            statement3.close();
            statement4.executeUpdate();
            statement4.close();
            statement5.executeUpdate();
            statement5.close();
        }catch (SQLException e){
            throw new SQLException(e.getMessage());
        }
    }

    public void addDocument(int documentId, int deliveryId, Location destination, List<Item> items, double totalWeight) throws Exception {
        String sql = "INSERT INTO Documents (documentId, deliveryId, totalWeight, destinationAddress, phoneNumber, contactName) VALUES (?,?,?,?,?,?)";
        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setInt(1, documentId);
            statement.setInt(2, deliveryId);
            statement.setDouble(3, totalWeight);
            statement.setString(4, destination.getAddress());
            statement.setString(5, destination.getPhoneNumber());
            statement.setString(6, destination.getContactName());
            statement.executeUpdate();
            for(Item item : items)
                addItem(documentId, item.getCode(), item.getName(), item.getAmount(), item.getItemWeight());

        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
        cache.getDocuments().put(documentId, new Document(documentId, deliveryId, destination, items, totalWeight));
    }

    public List<Document> getDeliveryDocuments (int deliveryId) throws Exception{
        List<Document> documents = new LinkedList<>();
        String sql = "SELECT * FROM Documents WHERE deliveryId='" + deliveryId + "'";
        try (Connection conn = DriverManager.getConnection(url);
             Statement statement = conn.createStatement()) {
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()) {
                documents.add(new Document(rs.getInt(1),
                                    rs.getInt(2),
                                    new Location(rs.getString(4), rs.getString(5), rs.getString(6)),
                                    getItems(rs.getInt(1)),
                                    rs.getDouble(3)));
            }
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
        if(documents.isEmpty())
            throw new Exception("There is no documents to this delivery ID");
        return documents;
    }

    public void deleteDocuments(int deliveryId) throws Exception{
        for(Document document : cache.getDocuments().values()){
            if(document.getDeliveryId() == deliveryId)
                cache.getDocuments().remove(deliveryId);
        }
        List<Document> documents = getDeliveryDocuments(deliveryId);
        List<Integer> docsIDs = new LinkedList<>();
        for (Document document : documents){
            docsIDs.add(document.getId());
        }

        String sql = "DELETE FROM Documents WHERE deliveryId='" + deliveryId + "'";
        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement statement = conn.prepareStatement(sql)){
            statement.executeUpdate();
        } catch (Exception e){
            throw new Exception(e.getMessage());
        }

        for(int id : docsIDs){
            sql = "DELETE FROM Items WHERE documentId='" + id + "'";
            try (Connection conn = DriverManager.getConnection(url);
                 PreparedStatement statement = conn.prepareStatement(sql)){
                statement.executeUpdate();
            } catch (Exception e){
                throw new Exception(e.getMessage());
            }
        }

    }

//    public Delivery getDocument(int documentId) throws Exception {
//        int deliveryId = -1;
//        String sql = "SELECT * FROM Documents WHERE documentId= '" + documentId + "'";
//        try (Connection conn = dbMaker.connect();
//             Statement statement = conn.createStatement()) {
//            ResultSet rs = statement.executeQuery(sql);
//            if (rs.next()) {
//                deliveryId = rs.getInt(2);
//            }
//        } catch (Exception e) {
//            throw new Exception(e.getMessage());
//        }
//        if(deliveryId != -1){
//
//        }
//        throw new Exception("This document is not exist");
//    }

    public Map<Location, LinkedList<Item>> getItemsPerDest(int deliveryId) throws Exception{
        String sql = "SELECT * FROM Documents WHERE deliveryId= '" + deliveryId + "'";
        Map<Location, LinkedList<Item>> map = new HashMap<>();
        try (Connection conn = DriverManager.getConnection(url);
             Statement statement = conn.createStatement()) {
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()) {
                map.put(new Location(rs.getString(4),
                        rs.getString(5),
                        rs.getString(6)), getItems(rs.getInt(1)));
            }
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
        return map;
    }

    private void addItem(int documentId, int itemCode, String itemName, int amount, double weight) throws Exception {
        String sql = "INSERT INTO Items (documentId, code, itemName, amount, weight) VALUES (?,?,?,?,?)";
        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setInt(1, documentId);
            statement.setInt(2, itemCode);
            statement.setString(3, itemName);
            statement.setInt(4, amount);
            statement.setDouble(5, weight);
            statement.executeUpdate();
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    public LinkedList<Item> getItems(int documentId) throws Exception{
        String sql = "SELECT * FROM Items WHERE documentId='" + documentId + "'";
        LinkedList<Item> items = new LinkedList<>();
        try (Connection conn = DriverManager.getConnection(url);
             Statement statement = conn.createStatement()) {
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()) {
                items.add(new Item(rs.getInt(2),
                        rs.getString(3),
                        rs.getInt(4),
                        rs.getDouble(5)));
            }
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
        return items;
    }

    private void deleteDocumentItems(int documentsId) throws Exception{

    }
}
