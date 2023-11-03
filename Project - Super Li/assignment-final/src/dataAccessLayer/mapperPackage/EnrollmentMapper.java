package dataAccessLayer.mapperPackage;

import dataAccessLayer.identityMapperPackage.IdentityEnrollmentMap;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class EnrollmentMapper {
    private static EnrollmentMapper enrollmentMapper = null;
    private IdentityEnrollmentMap identityMap;
    private String url;

    private EnrollmentMapper(String url){
        identityMap = new IdentityEnrollmentMap();
        this.url = url;
    }

    public static EnrollmentMapper getInstance(String url){
        if(enrollmentMapper == null)
            enrollmentMapper = new EnrollmentMapper(url);
        return enrollmentMapper;
    }

    public Map<Date, Map<String, ArrayList<String>>> getEnrollment(Date shiftDate, String strShiftDate) throws SQLException {
        Map<Date, Map<String, ArrayList<String>>> enrollment = new HashMap<Date, Map<String, ArrayList<String>>>();
        if(identityMap.checkIfDateExist(shiftDate))
            return identityMap.getEmployeesInShifts();
        else {
            enrollment = getEnrollmentOperation(shiftDate, strShiftDate);
            identityMap.setEnrollment(shiftDate, enrollment.get(shiftDate));
            return enrollment;
        }
    }

    private Map<Date, Map<String, ArrayList<String>>> getEnrollmentOperation(Date shiftDate, String strShiftDate) throws SQLException {
        String sqlStatement = "SELECT shift_time, id from Enrollment where date LIKE ?";
        Map<Date, Map<String, ArrayList<String>>> enrollment = new HashMap<Date, Map<String, ArrayList<String>>>();
        Map<String, ArrayList<String>> shiftTime_ids = new HashMap<String, ArrayList<String>>();
        enrollment.put(shiftDate, shiftTime_ids);
        try(Connection connection = DriverManager.getConnection(url);
            PreparedStatement statement = connection.prepareStatement(sqlStatement)){
            statement.setString(1, strShiftDate);
            ResultSet result = statement.executeQuery();
            while(result.next()){
                String shiftTime = result.getString("shift_time");
                String empId = result.getString("id");
                if(enrollment.get(shiftDate).containsKey(shiftTime))
                    enrollment.get(shiftDate).get(shiftTime).add(empId);
                else{
                    ArrayList<String> employeesId = new ArrayList<String>();
                    employeesId.add(empId);
                    enrollment.get(shiftDate).put(shiftTime, employeesId);
                }
            }
        } catch (Exception e) {
            throw new SQLException(e.getMessage());
        }
        return enrollment;
    }

    //<Date, <ShiftTime, [ id ]>>
    public ArrayList<String> getEmployeesInShift(Date shiftDate, String strShiftDate, String shiftTime) throws SQLException {
        ArrayList<String> employees = new ArrayList<String>();
        if(identityMap.checkIfDateExist(shiftDate)) {
            employees = identityMap.getEmployees(shiftDate, shiftTime);
            if(employees != null) {
                return employees;
            }
        }
        employees = selectEmployeesOperation(strShiftDate, shiftTime);
        return employees;
    }

    private ArrayList<String> selectEmployeesOperation(String shiftDate, String shiftTime) throws SQLException {
        String sqlStatement = "SELECT id from Enrollment where date LIKE ? AND shift_time LIKE ?";
        ArrayList<String> employees = new ArrayList<String>();
        try(Connection connection = DriverManager.getConnection(url);
            PreparedStatement statement = connection.prepareStatement(sqlStatement)){
            statement.setString(1, shiftDate);
            statement.setString(2, shiftTime);
            ResultSet result = statement.executeQuery();
            while(result.next()){
                String id = result.getString("id");
                employees.add(id);
            }
        } catch (Exception e) {
            throw new SQLException(e.getMessage());
        }
        return employees;
    }

    public void insertEmployeeToShift(Date shiftDate, String strShiftDate, String shiftTime, String empId) throws Exception {
        if(identityMap.checkIfDateExist(shiftDate))
            if(identityMap.checkIfShiftTimeExist(shiftDate, shiftTime))
                if(!identityMap.checkIfEmpExist(shiftDate, shiftTime, empId)){
                    insertEmployeeOperation(strShiftDate, shiftTime, empId);
                    identityMap.getEmployeesInShifts().get(shiftDate).get(shiftTime).add(empId);
                }
                else
                    throw new Exception("Employee Is Already Exists.");
            else{
                insertEmployeeOperation(strShiftDate, shiftTime, empId);
                ArrayList<String> emps = new ArrayList<String>();
                emps.add(empId);
                identityMap.getEmployeesInShifts().get(shiftDate).put(shiftTime, emps);
            }

        else{
            insertEmployeeOperation(strShiftDate, shiftTime, empId);
            ArrayList<String> emps = new ArrayList<String>();
            emps.add(empId);
            Map<String, ArrayList<String>> empsInShift = new HashMap<String, ArrayList<String>>();
            empsInShift.put(shiftTime, emps);
            identityMap.getEmployeesInShifts().put(shiftDate, empsInShift);
        }

    }

    private void insertEmployeeOperation(String shiftDate, String shiftTime, String empId) throws SQLException {
        String sqlStatement = "insert into Enrollment values(?, ?, ?)";
        try(Connection connection = DriverManager.getConnection(url);
            PreparedStatement statement = connection.prepareStatement(sqlStatement)){
            statement.setString(1, shiftDate);
            statement.setString(2, shiftTime);
            statement.setString(3, empId);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException(e.getMessage());
        }
    }

    public void deleteShift(Date shiftDate, String strShiftDate, String shiftTime) throws SQLException {
        deleteShiftOperation(strShiftDate, shiftTime);
        if(identityMap.checkIfDateExist(shiftDate))
            if(identityMap.checkIfShiftTimeExist(shiftDate, shiftTime))
                identityMap.getEmployeesInShifts().get(shiftDate).remove(shiftTime);
    }

    private void deleteShiftOperation(String shiftDate, String shiftTime) throws SQLException {
        String sqlStatement = "DELETE from Enrollment WHERE date LIKE ? AND shift_time LIKE ?";
        try(Connection connection = DriverManager.getConnection(url);
            PreparedStatement statement = connection.prepareStatement(sqlStatement)){
            statement.setString(1, shiftDate);
            statement.setString(2, shiftTime);
            statement.executeUpdate();
        }catch(SQLException e){
            throw new SQLException(e.getMessage());
        }
    }

    public void deleteEmpFromShift(Date shiftDate, String strShiftDate, String shiftTime, String empId) throws SQLException {
        deleteEmpOperation(strShiftDate, shiftTime, empId);
        if(identityMap.checkIfDateExist(shiftDate))
            if(identityMap.checkIfShiftTimeExist(shiftDate, shiftTime))
                identityMap.getEmployeesInShifts().get(shiftDate).get(shiftTime).remove(empId);
    }

    private void deleteEmpOperation(String shiftDate, String shiftTime, String empId) throws SQLException {
        String sqlStatement = "DELETE from Enrollment WHERE date LIKE ? AND shift_time LIKE ? AND id LIKE ?";
        try(Connection connection = DriverManager.getConnection(url);
            PreparedStatement statement = connection.prepareStatement(sqlStatement)){
            statement.setString(1, shiftDate);
            statement.setString(2, shiftTime);
            statement.setString(3, empId);
            statement.executeUpdate();
        }catch(SQLException e){
            throw new SQLException(e.getMessage());
        }
    }

    public void initEnrollmentTable(String date) throws SQLException {
        String sqlStatement = "insert into enrollment values(?, \"morning\", \"987654321\");";
        String sqlStatement2 = "insert into enrollment values(?, \"morning\", \"206469017\");";
        String sqlStatement3 = "insert into enrollment values(?, \"morning\", \"111111111\");";
        String sqlStatement4 = "insert into enrollment values(?, \"morning\", \"222222222\");";
        String sqlStatement5 = "insert into enrollment values(?, \"morning\", \"333333333\");";

        String sqlStatement7 = "insert into enrollment values(?, \"evening\", \"123456789\");";
        String sqlStatement8 = "insert into enrollment values(?, \"evening\", \"987654321\");";
        String sqlStatement9 = "insert into enrollment values(?, \"evening\", \"206469017\");";
        String sqlStatement10 = "insert into enrollment values(?, \"evening\", \"111111111\");";
        String sqlStatement11 = "insert into enrollment values(?, \"evening\", \"222222222\");";
        String sqlStatement12 = "insert into enrollment values(?, \"evening\", \"333333333\");";
        try{
            Connection connection = DriverManager.getConnection(url);
            PreparedStatement statement = connection.prepareStatement(sqlStatement);
            PreparedStatement statement2 = connection.prepareStatement(sqlStatement2);
            PreparedStatement statement3 = connection.prepareStatement(sqlStatement3);
            PreparedStatement statement4 = connection.prepareStatement(sqlStatement4);
            PreparedStatement statement5 = connection.prepareStatement(sqlStatement5);
            PreparedStatement statement7 = connection.prepareStatement(sqlStatement7);
            PreparedStatement statement8 = connection.prepareStatement(sqlStatement8);
            PreparedStatement statement9 = connection.prepareStatement(sqlStatement9);
            PreparedStatement statement10 = connection.prepareStatement(sqlStatement10);
            PreparedStatement statement11 = connection.prepareStatement(sqlStatement11);
            PreparedStatement statement12 = connection.prepareStatement(sqlStatement12);
            statement.setString(1, date);
            statement2.setString(1, date);
            statement3.setString(1, date);
            statement4.setString(1, date);
            statement5.setString(1, date);
            statement7.setString(1, date);
            statement8.setString(1, date);
            statement9.setString(1, date);
            statement10.setString(1, date);
            statement11.setString(1, date);
            statement12.setString(1, date);
            statement.executeUpdate();
            statement.close();
            statement2.executeUpdate();
            statement2.close();
            statement3.executeUpdate();
            statement3.close();
            statement4.executeUpdate();
            statement4.close();
            statement5.executeUpdate();
            statement5.close();
            statement7.executeUpdate();
            statement7.close();
            statement8.executeUpdate();
            statement8.close();
            statement9.executeUpdate();
            statement9.close();
            statement10.executeUpdate();
            statement10.close();
            statement11.executeUpdate();
            statement11.close();
            statement12.executeUpdate();
            statement12.close();
        }catch (SQLException e){
            throw new SQLException(e.getMessage());
        }
    }

    public boolean isInShift(String id, String strShiftDate, String shiftTime) throws SQLException {
        String sqlStatement = "SELECT * from enrollment WHERE date LIKE ? AND shift_time LIKE ? AND id LIKE ?";
        try(Connection connection = DriverManager.getConnection(url);
            PreparedStatement statement = connection.prepareStatement(sqlStatement);){
            statement.setString(1, strShiftDate);
            statement.setString(2, shiftTime);
            statement.setString(3, id);
            ResultSet result = statement.executeQuery();
            if(result.next())
                return true;
            return false;
        }catch (Exception e){
            throw new SQLException(e.getMessage());
        }
    }

    public boolean checkUpcomingShifts(String todayDate, String id) throws SQLException {
        String sqlStatement = "SELECT * from enrollment WHERE date >= ? AND id LIKE ?";
        try(Connection connection = DriverManager.getConnection(url);
            PreparedStatement statement = connection.prepareStatement(sqlStatement)){
            statement.setString(1, todayDate);
            statement.setString(2, id);
            ResultSet result = statement.executeQuery();
            if(result.next())
                return true;
            return false;
        }catch (Exception e){
            throw new SQLException(e.getMessage());
        }
    }

    public Map<String, ArrayList<String>> getUpcomingShifts(String todayDate, String empId) throws SQLException {
        Map<String, ArrayList<String>> upcomingShifts = new HashMap<String, ArrayList<String>>();
        String sqlStatement = "SELECT * from enrollment WHERE date >= ? AND id LIKE ?";
        try(Connection connection = DriverManager.getConnection(url);
            PreparedStatement statement = connection.prepareStatement(sqlStatement)){
            statement.setString(1, todayDate);
            statement.setString(2, empId);
            ResultSet result = statement.executeQuery();
            while(result.next()){
                String shiftDate = result.getString("date");
                String shiftTime = result.getString("shift_time");
                if(upcomingShifts.containsKey(shiftDate))
                    upcomingShifts.get(shiftDate).add(shiftTime);
                else{
                    ArrayList<String> shiftTimes = new ArrayList<String>();
                    shiftTimes.add(shiftTime);
                    upcomingShifts.put(shiftDate, shiftTimes);
                }
            }
            return upcomingShifts;
        }catch (Exception e){
            throw new SQLException(e.getMessage());
        }
    }
}

