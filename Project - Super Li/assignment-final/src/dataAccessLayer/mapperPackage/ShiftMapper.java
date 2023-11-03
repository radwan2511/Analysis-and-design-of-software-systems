package dataAccessLayer.mapperPackage;
import businessLayer.shiftPackage.Shift;
import dataAccessLayer.identityMapperPackage.IdentityShiftMap;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ShiftMapper {
    private static ShiftMapper shiftMapper = null;
    private IdentityShiftMap identitytMap;
    private String url;

    private ShiftMapper(String url){
        identitytMap = new IdentityShiftMap();
        this.url = url;
    }

    public static ShiftMapper getInstance(String url){
        if(shiftMapper == null){
            shiftMapper = new ShiftMapper(url);
        }
        return  shiftMapper;
    }

    public Shift getShift(Date shiftDate, String strShiftDate, String shiftTime) throws Exception {
        Shift shift = null;
        if(identitytMap.checkIfExist(shiftDate)){
            shift = identitytMap.getShift(shiftDate, shiftTime);
            if(shift != null)
                return shift;
        }
        shift = selectShiftOperation(shiftDate, strShiftDate, shiftTime);
        if(shift != null){
            ArrayList<Shift> shifts;
            if(identitytMap.getShifts().containsKey(shiftDate))
                identitytMap.getShifts().get(shiftDate).add(shift);
            else{
                shifts = new ArrayList<Shift>();
                shifts.add(shift);
                identitytMap.getShifts().put(shiftDate, shifts);
            }
        }
        return shift;
    }

    private Shift selectShiftOperation(Date shiftDate, String strShiftDate, String shiftTime) throws SQLException {
        String sqlStatement = "SELECT * from shift WHERE date = ? AND shift_time LIKE ?";
        Shift shift = null;
        try(Connection connection = DriverManager.getConnection(url);
            PreparedStatement statement = connection.prepareStatement(sqlStatement)){
            statement.setString(1, strShiftDate);
            statement.setString(2, shiftTime);
            ResultSet result = statement.executeQuery();
            if(result.next()){
                String shift_time = result.getString("shift_time");
                int day = result.getInt("day");
                shift = new Shift(day, shiftDate, shift_time);
            }
        }catch(SQLException e) {
            throw new SQLException(e.getMessage());
        }
        return shift;
    }

    public ArrayList<Shift> getShiftsPerDate(Date shiftDate, String strShiftDate) throws SQLException {
        ArrayList<Shift> shifts = new ArrayList<Shift>();
        if(identitytMap.checkIfExist(shiftDate))
            shifts = identitytMap.getShiftsPerDate(shiftDate);
        else{
            shifts = selectShiftsPerDate(strShiftDate, shiftDate);
            identitytMap.setShiftsPerDate(shiftDate, shifts);
        }
        return shifts;
    }

    private ArrayList<Shift> selectShiftsPerDate(String strShiftDate, Date shiftDate) throws SQLException {
        String sqlStatement = "SELECT * from shift WHERE date = ?";
        ArrayList<Shift> shifts = new ArrayList<Shift>();
        try(Connection connection = DriverManager.getConnection(url);
            PreparedStatement statement = connection.prepareStatement(sqlStatement)){
            statement.setString(1, strShiftDate);
            ResultSet result = statement.executeQuery();
            while(result.next()){
                String shift_time = result.getString("shift_time");
                int day = result.getInt("day");
                shifts.add(new Shift(day, shiftDate, shift_time));
            }
        }catch(SQLException e) {
            throw new SQLException(e.getMessage());
        }
        return shifts;
    }

    public void insertShift(Date shiftDate, String strShiftDate, String shiftTime, int shiftDay) throws Exception {
        if(identitytMap.checkIfExist(shiftDate))
            if(identitytMap.getShift(shiftDate, shiftTime) != null)
                throw new Exception("Shift Is Already Exists.");
        insertShiftOperation(strShiftDate, shiftTime, shiftDay);
        if(identitytMap.checkIfExist(shiftDate))
            identitytMap.getShifts().get(shiftDate).add(new Shift(shiftDay, shiftDate, shiftTime));
    }

    private void insertShiftOperation(String shiftDate, String shiftTime, int shiftDay) throws SQLException {
        String sqlStatement = "insert into shift values(?, ?, ?)";
        try(Connection connection = DriverManager.getConnection(url);
            PreparedStatement statement = connection.prepareStatement(sqlStatement)){
            statement.setString(1, shiftDate);
            statement.setString(2, shiftTime);
            statement.setInt(3, shiftDay);
            statement.executeUpdate();
        }catch(SQLException e){
            throw new SQLException(e.getMessage());
        }
    }

    public void deleteShift(Date shiftDate, String strShiftDate, String shiftTime) throws SQLException {
        deleteShiftOperation(strShiftDate, shiftTime);
        if(identitytMap.checkIfExist(shiftDate))
            if(identitytMap.getShift(shiftDate, shiftTime) != null)
                identitytMap.getShifts().get(shiftDate).remove(identitytMap.getShift(shiftDate, shiftTime));
    }

    private void deleteShiftOperation(String shiftDate, String shiftTime) throws SQLException {
        String sqlStatement = "DELETE from shift WHERE date = ? AND shift_time LIKE ?";
        try(Connection connection = DriverManager.getConnection(url);
            PreparedStatement statement = connection.prepareStatement(sqlStatement)){
            statement.setString(1, shiftDate);
            statement.setString(2, shiftTime);
            statement.executeUpdate();
        }catch(SQLException e){
            throw new SQLException(e.getMessage());
        }
    }

    public ArrayList<Date> getDates() throws SQLException {
        ArrayList<Date> dates = new ArrayList<Date>();
        String sqlStatement = "SELECT date from shift";
        try(Connection connection = DriverManager.getConnection(url);
            PreparedStatement statement = connection.prepareStatement(sqlStatement)){
            ResultSet result = statement.executeQuery();
            while(result.next()){
                String strDate = result.getString("date");
                SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                Date date = format.parse(strDate);
                dates.add(date);
            }
        }catch(SQLException | ParseException e){
            throw new SQLException(e.getMessage());
        }
        return dates;
    }

    public boolean checkIfShiftExist(String shiftTime, Date shiftDate, String strShiftDate) throws Exception {
        Shift shift = null;
        if(identitytMap.checkIfExist(shiftDate)){
            shift = identitytMap.getShift(shiftDate, shiftTime);
            if(shift != null)
                return true;
            return false;
        }
        else{
            return checkIfShiftExistOperation(shiftTime, strShiftDate);
        }
    }

    private boolean checkIfShiftExistOperation(String shiftTime, String strShiftDate) throws Exception {
        String sqlStatement = "SELECT * from shift WHERE shift_time LIKE ? AND shift_date LIKE ?";
        try(Connection connection = DriverManager.getConnection(url);
            PreparedStatement statement = connection.prepareStatement(sqlStatement);){
            ResultSet result = statement.executeQuery();
            if(result.next())
                return true;
            else
                return false;
        }catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }

    public void initShiftTable(String date) throws SQLException {
        String sqlStatement = "insert into shift values(?, \"morning\", 1);";
        String sqlStatement2 = "insert into shift values(?, \"evening\", 1);";
        try{
            Connection connection = DriverManager.getConnection(url);
            PreparedStatement statement = connection.prepareStatement(sqlStatement);
            PreparedStatement statement2 = connection.prepareStatement(sqlStatement2);
            statement.setString(1, date);
            statement2.setString(1, date);
            statement.executeUpdate();
            statement.close();
            statement2.executeUpdate();
            statement2.close();
        }catch (SQLException e){
            throw new SQLException(e.getMessage());
        }
    }
}
