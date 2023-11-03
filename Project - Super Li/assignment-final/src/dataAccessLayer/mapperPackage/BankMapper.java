package dataAccessLayer.mapperPackage;

import businessLayer.employeePackage.Bank;
import dataAccessLayer.identityMapperPackage.IdentityBankMap;

import java.sql.*;

public class BankMapper {
    private static BankMapper bankMapper = null;
    private IdentityBankMap identityMap;
    private String url;

    public static BankMapper getInstance(String url){
        if(bankMapper == null){
            bankMapper = new BankMapper(url);
        }
        return bankMapper;
    }

    private BankMapper(String url){
        identityMap = new IdentityBankMap();
        this.url = url;
    }

    public Bank getBank(String empId) throws SQLException {
        for(String id : identityMap.getBanks().keySet()){
            if(id.equals(empId))
                return identityMap.getBanks().get(id);
        }
        Bank bank = selectBankOperation(empId);
        if(bank != null){
            identityMap.getBanks().put(empId, bank);
        }
        return bank;
    }

    public void updateBank(String empId, int newBankNumber, int newBankDepartment, String newBankName) throws Exception {
        updateBankOperation(empId, newBankNumber, newBankDepartment, newBankName);
        Bank bank = selectBankOperation(empId);
        if(bank != null){
            for(String id : identityMap.getBanks().keySet()){
                if(id.equals(empId)){
                    identityMap.getBanks().replace(id, new Bank(newBankNumber, newBankDepartment, newBankName));
                }
            }
        }
        else
            throw new Exception("Updating Bank Data Failed.");
    }

    public void deleteBank(String empId) throws Exception {
        deleteBankOperation(empId);
        for(String id : identityMap.getBanks().keySet()){
            if(id.equals(empId)) {
                identityMap.getBanks().remove(id);
                break;
            }
        }
    }

    public void insertBank(String empId, String bankName, int accountNumber, int department) throws Exception {
        for(String id: identityMap.getBanks().keySet()){
            if(id.equals(empId))
                throw new Exception("Employee Id Is Already Exists.");
        }
        insertBankOperation(empId, bankName, accountNumber, department);
    }

    private void insertBankOperation(String empId, String bankName, int accountNumber, int department) throws SQLException {
        String sqlStatement = "insert into bank values(?, ?, ?, ?)";
        try(Connection connection = DriverManager.getConnection(url);
            PreparedStatement statement = connection.prepareStatement(sqlStatement)){
            statement.setString(1, empId);
            statement.setString(2, bankName);
            statement.setInt(3, accountNumber);
            statement.setInt(4, department);
            statement.executeUpdate();
        }catch(SQLException e){
            throw new SQLException(e.getMessage());
        }
    }

    private void deleteBankOperation(String empId) throws SQLException {
        String sqlStatement = "DELETE from bank WHERE id = ?";
        try(Connection connection = DriverManager.getConnection(url);
            PreparedStatement statement = connection.prepareStatement(sqlStatement)){
            statement.setString(1, empId);
            statement.executeUpdate();
        }catch(SQLException e) {
            throw new SQLException(e.getMessage());
        }
    }

    private void updateBankOperation(String empId, int newBankNumber, int newBankDepartment, String newBankName) throws SQLException {
        String sqlStatement = "UPDATE bank set name = ?, account_number = ?, department = ? WHERE id = ?";
        try(Connection connection = DriverManager.getConnection(url);
            PreparedStatement statement = connection.prepareStatement(sqlStatement)){
            statement.setString(1, newBankName);
            statement.setInt(2, newBankNumber);
            statement.setInt(3, newBankDepartment);
            statement.setString(4, empId);
            statement.executeUpdate();
        }catch (SQLException e){
            throw new SQLException(e.getMessage());
        }
    }


    private Bank selectBankOperation(String empId) throws SQLException {
        String sqlStatement = "SELECT * from bank WHERE id LIKE ?";
        Bank bank = null;
        try(Connection connection = DriverManager.getConnection(url);
            PreparedStatement statement = connection.prepareStatement(sqlStatement)){
            statement.setString(1, empId);
            ResultSet result = statement.executeQuery();
            if(result.next()){
                String bankName = result.getString("name");
                int accountNumber = result.getInt("account_number");
                int department = result.getInt("department");
                bank = new Bank(accountNumber, department, bankName);
            }
        }catch(SQLException e){
            throw new SQLException(e.getMessage());
        }
        return bank;
    }

    public void initBankTable() throws SQLException {
        String sqlStatement1 = "insert into bank values(\"123456789\", \"Leomi\", 443322, 666);\n";
        String sqlStatement2 =  "insert into bank values(\"987654321\", \"Hapoalim\", 112233, 777);\n";
        String sqlStatement3 = "insert into bank values(\"206469017\", \"Hapoalim\", 111666, 712);\n";
        String sqlStatement4 = "insert into bank values(\"111111111\", \"Hapoalim\", 111111, 712);\n";
        String sqlStatement44 = "insert into bank values(\"555555555\", \"Hapoalim\", 777890, 127);\n";
        String sqlStatement5 = "insert into bank values(\"222222222\", \"Hapoalim\", 222222, 712);\n";
        String sqlStatement6 = "insert into bank values(\"333333333\", \"Hapoalim\", 222666, 712);\n";
        String sqlStatement7 = "insert into bank values(\"207965617\", \"Hapoalim\", 111666, 712);";
        try{
            Connection connection = DriverManager.getConnection(url);
            PreparedStatement statement1 = connection.prepareStatement(sqlStatement1);
            statement1.executeUpdate();
            statement1.close();
            PreparedStatement statement2 = connection.prepareStatement(sqlStatement2);
            statement2.executeUpdate();
            statement2.close();
            PreparedStatement statement3 = connection.prepareStatement(sqlStatement3);
            statement3.executeUpdate();
            statement3.close();
            PreparedStatement statement4 = connection.prepareStatement(sqlStatement4);
            statement4.executeUpdate();
            statement4.close();
            PreparedStatement statement44 = connection.prepareStatement(sqlStatement44);
            statement44.executeUpdate();
            statement44.close();
            PreparedStatement statement5 = connection.prepareStatement(sqlStatement5);
            statement5.executeUpdate();
            statement5.close();
            PreparedStatement statement6 = connection.prepareStatement(sqlStatement6);
            statement6.executeUpdate();
            statement6.close();
            PreparedStatement statement7 = connection.prepareStatement(sqlStatement7);
            statement7.executeUpdate();
            statement7.close();
        }catch (SQLException e){
            throw new SQLException(e.getMessage());
        }
    }
}

