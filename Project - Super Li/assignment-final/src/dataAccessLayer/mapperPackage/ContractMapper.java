package dataAccessLayer.mapperPackage;
import businessLayer.employeePackage.Contract;
import dataAccessLayer.identityMapperPackage.IdentityContractMap;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ContractMapper {
    private static ContractMapper contractMapper = null;
    private IdentityContractMap identityMap;
    private String url;

    public static ContractMapper getInstance(String url){
        if(contractMapper == null){
            contractMapper = new ContractMapper(url);
        }
        return contractMapper;
    }

    private ContractMapper(String url){
        identityMap = new IdentityContractMap();
        this.url = url;
    }

    public Contract getContract(String empId) throws SQLException {
        for(String id : identityMap.getContracts().keySet()){
            if(id.equals(empId))
                return identityMap.getContracts().get(id);
        }
        Contract contract = selectContractOperation(empId);
        if(contract != null)
            identityMap.getContracts().put(empId, contract);
//        else
//            throw new SQLDataException("Contract Does Not Exist.");
        return contract;
    }

    public void insertContract(String empId, int salary, String startDate, String jobTitle, String jobType) throws Exception {
        insertContractOperation(empId, salary, startDate, jobTitle, jobType);
    }

    public void updateContract(String empId, String newJobType, int newSalary) throws Exception {
        updateContractOperation(empId, newJobType, newSalary);
        Contract contract = selectContractOperation(empId);
        if(contract != null){
            for(String id : identityMap.getContracts().keySet()){
                if(id.equals(empId))
                    identityMap.getContracts().replace(id, contract);
            }
        }
        else
            throw new Exception("Updating Contract Data Failed.");
    }

    public void deleteContract(String empId) throws SQLException {
        deleteContractOperation(empId);
        for(String id : identityMap.getContracts().keySet()){
            if(id.equals(empId)) {
                identityMap.getContracts().remove(id);
                break;
            }
        }
    }

    private void deleteContractOperation(String empId) throws SQLException {
        String sqlStatement = "DELETE from contract WHERE id = ?";
        try(Connection connection = DriverManager.getConnection(url);
            PreparedStatement statement = connection.prepareStatement(sqlStatement);){
            statement.setString(1, empId);
            statement.executeUpdate();
        }catch(SQLException e) {
            throw new SQLException(e.getMessage());
        }
    }

    private void updateContractOperation(String empId, String newJobType, int newSalary) throws SQLException {
        String sqlStatement = "UPDATE contract set salary = ?, job_type = ? WHERE id = ?";
        try(Connection connection = DriverManager.getConnection(url);
            PreparedStatement statement = connection.prepareStatement(sqlStatement);){
            statement.setInt(1, newSalary);
            statement.setString(2, newJobType);
            statement.setString(3, empId);
            statement.executeUpdate();
        }catch (SQLException e){
            throw new SQLException(e.getMessage());
        }
    }

    private void insertContractOperation(String empId, int salary, String startDate, String jobTitle, String jobType) throws Exception {
        String sqlStatement = "insert into contract values(?, ?, ?, ?, ?)";
        try(Connection connection = DriverManager.getConnection(url);
            PreparedStatement statement = connection.prepareStatement(sqlStatement);){
            statement.setString(1, empId);
            statement.setInt(2, salary);
            statement.setString(3, startDate);
            statement.setString(4, jobTitle);
            statement.setString(5, jobType);
            statement.executeUpdate();
        }catch(Exception e){
            throw new SQLException(e.getMessage());
        }
    }

    private Contract selectContractOperation(String empId) throws SQLException {
        String sqlStatement = "SELECT * from contract WHERE id = ?";
        Contract contract = null;
        try(Connection connection = DriverManager.getConnection(url);
            PreparedStatement statement = connection.prepareStatement(sqlStatement);){
            statement.setString(1, empId);
            ResultSet result = statement.executeQuery();
            if(result.next()){
                String jobTitle = result.getString("job_title");
                String jobType = result.getString("job_type");
                int salary = result.getInt("salary");
                String strDate = result.getString("start_date");
                SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                Date date = format.parse(strDate);
               contract = new Contract(salary, date, jobTitle, jobType);
            }
        }catch (Exception e){
            throw new SQLException(e.getMessage());
        }
        return contract;
    }

    public String getJobTitle(String empId) throws Exception {
        if(identityMap.checkIfIdExist(empId))
            return identityMap.getJobTitle(empId);
        return SelectEmpJobTitle(empId);
    }

    private String SelectEmpJobTitle(String empId) throws Exception {
        String sqlStatement = "SELECT job_title from contract WHERE id = ?";
        String jobTitle = "";
        try(Connection connection = DriverManager.getConnection(url);
            PreparedStatement statement = connection.prepareStatement(sqlStatement);){
            statement.setString(1, empId);
            ResultSet result = statement.executeQuery();
            if(result.next()){
                jobTitle = result.getString("job_title");
            }
        }catch (Exception e){
            throw new Exception(e.getMessage());
        }
        return jobTitle;
    }

    public void initContractTable() throws SQLException {
        String sqlStatement1 = "insert into contract values(\"123456789\", 8500, \"27/03/1998\", \"cashier\", \"full time\");";
        String sqlStatement2 = "insert into contract values(\"987654321\", 16000, \"11/11/2011\", \"manager\", \"full time\");";
        String sqlStatement3 = "insert into contract values(\"206469017\", 7000, \"21/01/2020\", \"storeKeeper\", \"full time\");";
        String sqlStatement4 = "insert into contract values(\"111111111\", 7000, \"10/01/2020\", \"driver\", \"full time\");";
        String sqlStatement44 = "insert into contract values(\"555555555\", 7000, \"10/08/2021\", \"driver\", \"full time\");";
        String sqlStatement5 = "insert into contract values(\"222222222\", 7000, \"30/06/2021\", \"shiftManager\", \"full time\");";
        String sqlStatement6 = "insert into contract values(\"333333333\", 7000, \"01/01/2018\", \"general\", \"full time\");";
        String sqlStatement7 = "insert into contract values(\"207965617\", 7000, \"31/05/2020\", \"logisticsManager\", \"full time\");";
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

    public boolean checkIfDriver(String id) throws SQLException {
        String sqlStatement = "SELECT * from contract WHERE id = ? AND job_title = ?";
        try(Connection connection = DriverManager.getConnection(url);
            PreparedStatement statement = connection.prepareStatement(sqlStatement);){
            statement.setString(1, id);
            statement.setString(2, "driver");
            ResultSet result = statement.executeQuery();
            if(result.next())
                return true;
            return false;
        }catch (SQLException e){
            throw new SQLException(e.getMessage());
        }
    }
}
