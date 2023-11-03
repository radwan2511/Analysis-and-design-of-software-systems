package dataAccessLayer.mapperPackage;

import businessLayer.employeePackage.Employee;
import businessLayer.employeePackage.Manager;
import businessLayer.employeePackage.RegularEmployee;
import dataAccessLayer.identityMapperPackage.IdentityEmployeeMap;

import java.sql.*;
import java.util.ArrayList;

public class EmployeeMapper {
    private static EmployeeMapper employeeMapper= null;
    private IdentityEmployeeMap identityMap;
    private String url;

    public static EmployeeMapper getInstance(String url){
        if(employeeMapper==null){
            employeeMapper= new EmployeeMapper(url);
        }
        return employeeMapper;
    }
    private EmployeeMapper(String url){
        identityMap=new IdentityEmployeeMap();
        this.url = url;
    }
    public void insertEmployee(String empId, String Name, boolean isShiftManager, boolean isManager) throws Exception {
        for(String id: identityMap.getEmployees().keySet()){
            if(id.equals(empId))
                throw new Exception("Employee Id Is Already Exists.");
        }
        int shiftmanager=0, manager=0;
        if(isShiftManager)
            shiftmanager=1;
        if(isManager)
            manager=1;
        insertEmployeeOperation(empId, Name, shiftmanager, manager);
    }


    private void insertEmployeeOperation(String empId, String name, int shiftmanager, int manager)throws SQLException {
        String sqlStatement = "insert into employee values(?, ?, ?, ?)";
        try(Connection connection = DriverManager.getConnection(url);
            PreparedStatement statement = connection.prepareStatement(sqlStatement)){
            statement.setString(1, empId);
            statement.setString(2, name);
            statement.setInt(3, manager);
            statement.setInt(4, shiftmanager);
            statement.executeUpdate();
        }catch(SQLException e){
            throw new SQLException(e.getMessage());
        }
    }

    public void updateEmployeeToShiftManager(String empId,boolean isShiftManager)throws Exception{
        int shiftManager=0;
        if(isShiftManager)
            shiftManager=1;
        updateEmployeeToShiftManagerOperation(empId,shiftManager);
        Employee emp=selectEmployeeOperation(empId);
        if(emp != null){
            for(String id : identityMap.getEmployees().keySet()){
                if(id.equals(empId)){
                    identityMap.getEmployees().replace(id,emp);
                }
            }
        }
        else
            throw new Exception("Updating Employee Data Failed.");
    }

    private void updateEmployeeToShiftManagerOperation(String empId,int isShiftManager)throws SQLException{
        String sqlStatement = "UPDATE employee set is_shift_manager = ? WHERE id = ?";
        try(Connection connection = DriverManager.getConnection(url);
            PreparedStatement statement = connection.prepareStatement(sqlStatement)){
            statement.setString(2, empId);
            statement.setInt(1, isShiftManager);
            statement.executeUpdate();
        }catch(SQLException e){
            throw new SQLException(e.getMessage());
        }
    }
    public Employee getEmployee(String empId) throws SQLException {
        for(String id : identityMap.getEmployees().keySet()){
            if(id.equals(empId))
                return identityMap.getEmployees().get(id);
        }
        Employee emp = selectEmployeeOperation(empId);
        if(emp != null){
            identityMap.getEmployees().put(empId, emp);
            return emp;
        }
        else
            throw new SQLDataException("Bank Account Does Not Exist.");
    }

    private Employee selectEmployeeOperation(String empId) throws SQLException {
        String sqlStatement = "SELECT * from employee WHERE id = ?";
        Employee emp = null;
        try(Connection connection = DriverManager.getConnection(url);
            PreparedStatement statement = connection.prepareStatement(sqlStatement)){
            statement.setString(1, empId);
            ResultSet result = statement.executeQuery();
            if(result.next()){
                String name= result.getString(2);
                int isShiftManager = result.getInt(4);
                int isManager = result.getInt(3);
                if(isManager==1){
                    emp =new Manager(name,empId,false);
                }else{
                    boolean shiftManager= false;
                    if(isShiftManager==1)
                        shiftManager=true;
                    emp= new RegularEmployee(name,empId,shiftManager);
                }
            }

        }catch(SQLException e){
            throw new SQLException(e.getMessage());
        }
        return emp;
    }

    public void deleteEmployee(String empId) throws Exception {
        deleteEmployeeOperation(empId);
        for(String id : identityMap.getEmployees().keySet()){
            if(id.equals(empId)) {
                identityMap.getEmployees().remove(id);
                break;
            }
        }
    }
    private void deleteEmployeeOperation(String empId) throws SQLException {
        String sqlStatement = "DELETE from employee WHERE id = ?";
        try(Connection connection = DriverManager.getConnection(url);
            PreparedStatement statement = connection.prepareStatement(sqlStatement)){
            statement.setString(1, empId);
            statement.executeUpdate();
        }catch(SQLException e) {
            throw new SQLException(e.getMessage());
        }
    }

    public ArrayList<String> getAllEmployees() throws SQLException {
        String sqlStatement = "SELECT * from Employee";
        ArrayList<String> employees = new ArrayList<String>();
        try(Connection connection = DriverManager.getConnection(url);
            PreparedStatement statement = connection.prepareStatement(sqlStatement)){
            ResultSet result = statement.executeQuery();
            String id = "";
            while(result.next()){
                id = result.getString("id");
                employees.add(id);
            }
        }catch(SQLException e) {
            throw new SQLException(e.getMessage());
        }
        return employees;
    }

    public void initEmployeeTable() throws SQLException {
        String sqlStatement1 = "insert into employee values(?, \"Adel\", 0, 0);";
        String sqlStatement2 = "insert into employee values(?, \"Mary\", 1, 0);";
        String sqlStatement3 = "insert into employee values(?, \"Nadia\", 0, 1);";
        String sqlStatement4 = "insert into employee values(?, \"Robert\", 0, 1);";
        String sqlStatement44 = "insert into employee values(?, \"Mike\", 0, 1);";
        String sqlStatement5 = "insert into employee values(?, \"John\", 0, 1);";
        String sqlStatement6 = "insert into employee values(?, \"James\", 0, 0);";
        String sqlStatement8 = "insert into employee values(?, \"Baraa\", 0, 0);";
        try{
            Connection connection = DriverManager.getConnection(url);
            PreparedStatement statement1 = connection.prepareStatement(sqlStatement1);
            PreparedStatement statement2 = connection.prepareStatement(sqlStatement2);
            PreparedStatement statement3 = connection.prepareStatement(sqlStatement3);
            PreparedStatement statement4 = connection.prepareStatement(sqlStatement4);
            PreparedStatement statement44 = connection.prepareStatement(sqlStatement44);
            PreparedStatement statement5 = connection.prepareStatement(sqlStatement5);
            PreparedStatement statement6 = connection.prepareStatement(sqlStatement6);
            PreparedStatement statement8 = connection.prepareStatement(sqlStatement8);
            statement1.setString(1,"123456789");
            statement1.executeUpdate();
            statement1.close();
            statement2.setString(1,"987654321");
            statement2.executeUpdate();
            statement2.close();
            statement3.setString(1,"206469017");
            statement3.executeUpdate();
            statement3.close();
            statement4.setString(1,"111111111");
            statement4.executeUpdate();
            statement4.close();
            statement44.setString(1,"555555555");
            statement44.executeUpdate();
            statement44.close();
            statement5.setString(1,"222222222");
            statement5.executeUpdate();
            statement5.close();
            statement6.setString(1,"333333333");
            statement6.executeUpdate();
            statement6.close();
            statement8.setString(1,"207965617");
            statement8.executeUpdate();
            statement8.close();
        }catch (SQLException e){
            throw new SQLException(e.getMessage());
        }
    }

}

