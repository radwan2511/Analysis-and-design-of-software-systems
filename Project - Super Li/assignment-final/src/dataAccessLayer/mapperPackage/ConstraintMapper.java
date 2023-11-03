package dataAccessLayer.mapperPackage;

import businessLayer.employeePackage.Constraint;
import dataAccessLayer.identityMapperPackage.IdentityConstraintMap;

import java.sql.*;
import java.util.ArrayList;
import java.util.Map;

public class ConstraintMapper {
    private static ConstraintMapper constraintMapper = null;
    private IdentityConstraintMap identityMap;
    private String url;

    public static ConstraintMapper getInstance(String url){
        if(constraintMapper == null){
            constraintMapper = new ConstraintMapper(url);
        }
        return constraintMapper;
    }

    public ConstraintMapper(String url){
        identityMap = new IdentityConstraintMap();
        this.url = url;
    }

    public ArrayList<Constraint> getEmpConstraints(String empId) throws SQLException {
        ArrayList<Constraint> constraints;
        if(identityMap.checkIfEmpExist(empId))
            constraints = identityMap.getEmpConstraints(empId);
        else
            constraints = selectEmpConstraintsOperation(empId);
        return constraints;
    }

    private ArrayList<Constraint> selectEmpConstraintsOperation(String empId) throws SQLException {
        String sqlStatement = "SELECT * from constraints WHERE id = ?";
        ArrayList<Constraint> constraints = new ArrayList<Constraint>();
        try(Connection connection = DriverManager.getConnection(url);
            PreparedStatement statement = connection.prepareStatement(sqlStatement);){
            statement.setString(1, empId);
            ResultSet result = statement.executeQuery();
            while(result.next()){
                int day = result.getInt("day");
                String shiftTime = result.getString("shift_time");
                String reason = result.getString("reason");
                constraints.add(new Constraint(day, shiftTime, reason));
            }
        }catch(SQLException e){
            throw new SQLException(e.getMessage());
        }
        return constraints;
    }

    public Constraint getConstraint(String empId, int day, String shiftTime) throws SQLException {
        Constraint constraint;
        if(identityMap.checkIfEmpExist(empId))
            constraint = identityMap.getConstraint(empId, day, shiftTime);
        else
            constraint = selectConstraintOperation(empId, day, shiftTime);
        return constraint;
    }

    private Constraint selectConstraintOperation(String empId, int day, String shiftTime) throws SQLException {
        String sqlStatement = "SELECT * from constraints WHERE id = ? AND day = ? AND shift_time LIKE ?";
        Constraint constraint = null;
        try(Connection connection = DriverManager.getConnection(url);
            PreparedStatement statement = connection.prepareStatement(sqlStatement);){
            statement.setString(1, empId);
            statement.setInt(2, day);
            statement.setString(3, shiftTime);
            ResultSet result = statement.executeQuery();
            if(result.next()){
                String reason = result.getString("reason");
                constraint = new Constraint(day, shiftTime, reason);
            }
        }catch(SQLException e){
            throw new SQLException(e.getMessage());
        }
        return constraint;
    }

    public void insertConstraint(String empId, int day, String shiftTime, String reason) throws Exception {
        if(identityMap.checkIfEmpExist(empId))
            if(identityMap.checkIfConstraintExist(empId, day, shiftTime))
                throw new Exception("Constraint Is Already Exist.");
        insertConstraintOperation(empId, day, shiftTime, reason);
        if(identityMap.checkIfEmpExist(empId)){
            identityMap.addConstraint(empId, day, shiftTime, reason);
        }
    }

    private void insertConstraintOperation(String empId, int day, String shiftTime, String reason) throws SQLException {
        String sqlStatement = "insert into constraints values(?, ?, ?, ?)";
        try(Connection connection = DriverManager.getConnection(url);
            PreparedStatement statement = connection.prepareStatement(sqlStatement);){
            statement.setInt(1, day);
            statement.setString(2, shiftTime);
            statement.setString(3, empId);
            statement.setString(4, reason);
            statement.executeUpdate();
        }catch(SQLException e){
            throw new SQLException(e.getMessage());
        }
    }

    public void initConstraintsTable() throws SQLException {
        String sqlStatement1 = "insert into constraints values(3, \"morning\", \"123456789\", \"medicalIssue\");";
        String sqlStatement2 = "insert into constraints values(4, \"evening\", \"123456789\", \"university\");";
        String sqlStatement3 = "insert into constraints values(3, \"morning\", \"206469017\", \"religion\");";
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
        }catch (SQLException e){
            throw new SQLException(e.getMessage());
        }
    }

//    public void deleteConstraint(String empId, int day, String shift) {
//    }

    public void deleteConstraint(String empId,int day ,String shift) throws Exception {
        deleteConstraintOperation(empId,day,shift);
        for(String id  : identityMap.getConstraints().keySet()){ //Map<String, Map<Integer, Map<String, String>>> //<empId, <day, <shiftTime, reason> > >
            for(int d:  identityMap.getConstraints().get(id).keySet()){
                for(String time: identityMap.getConstraints().get(id).get(d).keySet()) {
                    if (time.equals(shift))
                        identityMap.getConstraints().get(id).get(d).remove(time);
                }
                if(identityMap.getConstraints().get(id).get(d).size()==0)
                    identityMap.getConstraints().get(id).remove(d);
            }
            if(identityMap.getConstraints().get(id).size()==0)
                identityMap.getConstraints().remove(id);

        }
    }
    private void deleteConstraintOperation(String empId,int day ,String shift) throws SQLException {
        String sqlStatement = "DELETE from constraints  WHERE day = ?  AND shift_time LIKE ? AND id LIKE ?";
        try(Connection connection = DriverManager.getConnection(url);
            PreparedStatement statement = connection.prepareStatement(sqlStatement);){
            statement.setInt(1, day);
            statement.setString(2, shift);
            statement.setString(3, empId);
            statement.executeUpdate();
        }catch(SQLException e) {
            throw new SQLException(e.getMessage());
        }
    }

    public void updateConstraintReason(String empId, int day, String shift, String newReason) throws Exception {
        updateConstraintReasonOperation(empId,day,shift,newReason);
        Constraint constraint= getConstraint(empId,day,shift);
        for(String id  : identityMap.getConstraints().keySet()) { //Map<String, Map<Integer, Map<String, String>>> //<empId, <day, <shiftTime, reason> > >
            for (int d : identityMap.getConstraints().get(id).keySet()) {
                for (String time : identityMap.getConstraints().get(id).get(d).keySet()) {
                    if (time.equals(shift))
                        identityMap.getConstraints().get(id).get(d).replace(shift, newReason);
                }
            }
        }
        if(constraint==null)
            throw new Exception("Updating Constraint Data Failed.");

    }

    private void updateConstraintReasonOperation(String empId, int day, String shift, String newReason) throws Exception {
        String sqlStatement = "UPDATE constraints set reason = ? WHERE day = ?  AND shift_time LIKE ? AND id LIKE ? ";
        try(Connection connection = DriverManager.getConnection(url);
            PreparedStatement statement = connection.prepareStatement(sqlStatement);){
            statement.setString(1, newReason);
            statement.setInt(2, day);
            statement.setString(3, shift);
            statement.setString(4, empId);
            statement.executeUpdate();

        }catch (SQLException e){
            throw new SQLException(e.getMessage());
        }

    }

    public void updateConstraintDay(String empId, int oldDay, String oldShift, int newDay,String newShift) throws Exception {
        updateConstraintDayOperation(empId,oldDay,oldShift,newDay,newShift);
        Constraint constraint= getConstraint(empId,newDay,newShift);

        //Map<String, Map<Integer, Map<String, String>>> //<empId, <day, <shiftTime, reason> > >
        if(identityMap.getConstraints().containsKey(empId)) {
            if (identityMap.getConstraints().get(empId).containsKey(oldDay)) {
                Map<String, String> shiftReason = identityMap.getConstraints().get(empId).get(oldDay);//<shift,reson>
                if (shiftReason.containsKey(oldShift)) {
                    shiftReason.replace(newShift, shiftReason.get(oldShift));
                }
                identityMap.getConstraints().get(empId).remove(oldDay);
                identityMap.getConstraints().get(empId).put(newDay, shiftReason);
            }
        }
        if(constraint==null)
            throw new Exception("Updating Constraint Data Failed.");

    }

    private void updateConstraintDayOperation(String empId, int oldDay, String oldShift, int newDay,String newShift) throws Exception {
        Constraint constraint= getConstraint(empId,oldDay,oldShift);
        deleteConstraint(empId,oldDay,oldShift);
        insertConstraint(empId, newDay, newShift, constraint.getReason());

    }

    public boolean hasConstraint(String empId, String shiftTime, int day) throws SQLException {
        if(identityMap.checkIfEmpExist(empId))
            if(identityMap.checkIfConstraintExist(empId, day, shiftTime))
                return true;
        Constraint constraint = getConstraint(empId, day, shiftTime);
        return constraint != null;
    }
}
