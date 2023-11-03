package dataAccessLayer.mapperPackage;

import dataAccessLayer.identityMapperPackage.IdentityStructureMap;

import java.sql.*;
import java.util.Set;

public class ShiftsStructureMapper {
    private static ShiftsStructureMapper shiftsStructure = null;
    private IdentityStructureMap identitytMap;
    private String url;

    private  ShiftsStructureMapper(String url){
        identitytMap = new IdentityStructureMap();
        this.url = url;
    }

    public static ShiftsStructureMapper getInstance(String url){
        if(shiftsStructure == null)
            shiftsStructure = new ShiftsStructureMapper(url);
        return shiftsStructure;
    }

    public void updateNumOfEmp(int day, int shiftTime, String jobTitle, int numEmp) throws SQLException {
        updateNumOfEmpOperation(day, shiftTime, jobTitle, numEmp);
        identitytMap.setNumOfEmp(day, shiftTime, jobTitle, numEmp);
    }

    private void updateNumOfEmpOperation(int day, int shiftTime, String jobTitle, int numEmp) throws SQLException {
        String sqlStatement = "UPDATE structure set num_of_emp = ? WHERE day = ? AND shift_time = ? AND job_title LIKE ?";
        try(Connection connection = DriverManager.getConnection(url);
            PreparedStatement statement = connection.prepareStatement(sqlStatement);){
            statement.setInt(1, numEmp);
            statement.setInt(2, day);
            statement.setInt(3, shiftTime);
            statement.setString(4, jobTitle);
            statement.executeUpdate();
        }catch (SQLException e){
            throw new SQLException(e.getMessage());
        }
    }

    public String printDailyShiftStructure(int day, int shiftTime){
        return identitytMap.printDailyShiftStructure(day, shiftTime);
    }

    public Set<String> getJobTitles(int day, int time) {
        return identitytMap.getJobTitles(day, time);
    }

    public int getNumOfEmpInJobTitle(int day, int time, String jobTile) {
        return identitytMap.getNumOfEmpInJobTitle(day, time, jobTile);
    }

    public void load() throws SQLException {
        String sqlStatement = "SELECT * from structure";
        try(Connection connection = DriverManager.getConnection(url);
            PreparedStatement statement = connection.prepareStatement(sqlStatement);){
            ResultSet result = statement.executeQuery();
            while(result.next()){
                int day = result.getInt("day");
                int shiftTime = result.getInt("shift_time");
                String jobTitle = result.getString("job_title");
                int numOfEmp = result.getInt("num_of_emp");
                identitytMap.loadStrucutre(day, shiftTime, jobTitle, numOfEmp);
            }
        }catch (SQLException e){
            throw new SQLException(e.getMessage());
        }
    }

    public void initStructureTable() throws SQLException {
        String sqlStatement1 = "insert into structure values(0, 1, \"cashier\", 1);";
        String sqlStatement2 = "insert into structure values(1, 1, \"cashier\", 1);";
        String sqlStatement3 = "insert into structure values(0, 1, \"storeKeeper\", 1);";
        String sqlStatement4 = "insert into structure values(1, 1, \"storeKeeper\", 1);";
        String sqlStatement5 = "insert into structure values(0, 1, \"manager\", 1);";
        String sqlStatement6 = "insert into structure values(1, 1, \"manager\", 1);";
        String sqlStatement7 = "insert into structure values(0, 1, \"shiftManager\", 1);";
        String sqlStatement8 = "insert into structure values(1, 1, \"shiftManager\", 1);";
        String sqlStatement9 = "insert into structure values(0, 1, \"general\", 1);";
        String sqlStatement10 = "insert into structure values(1, 1, \"general\", 1);";
        String sqlStatement11 = "insert into structure values(0, 1, \"coronaInspector\", 1);";
        String sqlStatement12 = "insert into structure values(1, 1, \"coronaInspector\", 1);";
        String sqlStatement13 = "insert into structure values(0, 2, \"cashier\", 1);";
        String sqlStatement14 = "insert into structure values(1, 2, \"cashier\", 1);";
        String sqlStatement15 = "insert into structure values(0, 2, \"storeKeeper\", 1);";
        String sqlStatement16 = "insert into structure values(1, 2, \"storeKeeper\", 1);";
        String sqlStatement17 = "insert into structure values(0, 2, \"manager\", 1);";
        String sqlStatement18 = "insert into structure values(1, 2, \"manager\", 1);";
        String sqlStatement19 = "insert into structure values(0, 2, \"shiftManager\", 1);";
        String sqlStatement20 = "insert into structure values(1, 2, \"shiftManager\", 1);";
        String sqlStatement21 = "insert into structure values(0, 2, \"general\", 1);";
        String sqlStatement22 =  "insert into structure values(1, 2, \"general\", 1);";
        String sqlStatement23 = "insert into structure values(0, 2, \"coronaInspector\", 1);";
        String sqlStatement24 =   "insert into structure values(1, 2, \"coronaInspector\", 1);";
        String sqlStatement25 =  "insert into structure values(0, 3, \"cashier\", 1);";
        String sqlStatement26 =    "insert into structure values(1, 3, \"cashier\", 1);";
        String sqlStatement27 =   "insert into structure values(0, 3, \"storeKeeper\", 1);";
        String sqlStatement28 =    "insert into structure values(1, 3, \"storeKeeper\", 1);";
        String sqlStatement29 =      "insert into structure values(0, 3, \"manager\", 1);";
        String sqlStatement30 =    "insert into structure values(1, 3, \"manager\", 1);";
        String sqlStatement31 =       "insert into structure values(0, 3, \"shiftManager\", 1);";
        String sqlStatement32 =        "insert into structure values(1, 3, \"shiftManager\", 1);";
        String sqlStatement33 =       "insert into structure values(0, 3, \"general\", 1);";
        String sqlStatement34 =     "insert into structure values(1, 3, \"general\", 1);";
        String sqlStatement35 =      "insert into structure values(0, 3, \"coronaInspector\", 1);";
        String sqlStatement36 =       "insert into structure values(1, 3, \"coronaInspector\", 1);";
        String sqlStatement37 =       "insert into structure values(0, 4, \"cashier\", 1);";
        String sqlStatement38 =      "insert into structure values(1, 4, \"cashier\", 1);";
        String sqlStatement39 =       "insert into structure values(0, 4, \"storeKeeper\", 1);";
        String sqlStatement40 =        "insert into structure values(1, 4, \"storeKeeper\", 1);";
        String sqlStatement41 =       "insert into structure values(0, 4, \"manager\", 1);";
        String sqlStatement42 =       "insert into structure values(1, 4, \"manager\", 1);";
        String sqlStatement43 =        "insert into structure values(0, 4, \"shiftManager\", 1);";
        String sqlStatement44 =        "insert into structure values(1, 4, \"shiftManager\", 1);";
        String sqlStatement45 =       "insert into structure values(0, 4, \"general\", 1);";
        String sqlStatement46 =       "insert into structure values(1, 4, \"general\", 1);";
        String sqlStatement47 =       "insert into structure values(0, 4, \"coronaInspector\", 1);";
        String sqlStatement48 =       "insert into structure values(1, 4, \"coronaInspector\", 1);";
        String sqlStatement49 =       "insert into structure values(0, 5, \"cashier\", 1);";
        String sqlStatement50 =      "insert into structure values(1, 5, \"cashier\", 1);";
        String sqlStatement51 =     "insert into structure values(0, 5, \"storeKeeper\", 1);";
        String sqlStatement52 =     "insert into structure values(1, 5, \"storeKeeper\", 1);";
        String sqlStatement53 =       "insert into structure values(0, 5, \"manager\", 1);";
        String sqlStatement54 =       "insert into structure values(1, 5, \"manager\", 1);";
        String sqlStatement55 =       "insert into structure values(0, 5, \"shiftManager\", 1);";
        String sqlStatement56 =       "insert into structure values(1, 5, \"shiftManager\", 1);";
        String sqlStatement57 =       "insert into structure values(0, 5, \"general\", 1);";
        String sqlStatement58 =       "insert into structure values(1, 5, \"general\", 1);";
        String sqlStatement59 =       "insert into structure values(0, 5, \"coronaInspector\", 1);";
        String sqlStatement60 =       "insert into structure values(1, 5, \"coronaInspector\", 1);";
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
            PreparedStatement statement5 = connection.prepareStatement(sqlStatement5);
            statement5.executeUpdate();
            statement5.close();
            PreparedStatement statement6 = connection.prepareStatement(sqlStatement6);
            statement6.executeUpdate();
            statement6.close();
            PreparedStatement statement7 = connection.prepareStatement(sqlStatement7);
            statement7.executeUpdate();
            statement7.close();
            PreparedStatement statement8 = connection.prepareStatement(sqlStatement8);
            statement8.executeUpdate();
            statement8.close();
            PreparedStatement statement9 = connection.prepareStatement(sqlStatement9);
            statement9.executeUpdate();
            statement9.close();
            PreparedStatement statement10 = connection.prepareStatement(sqlStatement10);
            statement10.executeUpdate();
            statement10.close();
            PreparedStatement statement11 = connection.prepareStatement(sqlStatement11);
            statement11.executeUpdate();
            statement11.close();
            PreparedStatement statement12 = connection.prepareStatement(sqlStatement12);
            statement12.executeUpdate();
            statement12.close();
            PreparedStatement statement13 = connection.prepareStatement(sqlStatement13);
            statement13.executeUpdate();
            statement13.close();
            PreparedStatement statement14 = connection.prepareStatement(sqlStatement14);
            statement14.executeUpdate();
            statement14.close();
            PreparedStatement statement15 = connection.prepareStatement(sqlStatement15);
            statement15.executeUpdate();
            statement15.close();
            PreparedStatement statement16 = connection.prepareStatement(sqlStatement16);
            statement16.executeUpdate();
            statement16.close();
            PreparedStatement statement17 = connection.prepareStatement(sqlStatement17);
            statement17.executeUpdate();
            statement17.close();
            PreparedStatement statement18 = connection.prepareStatement(sqlStatement18);
            statement18.executeUpdate();
            statement18.close();
            PreparedStatement statement19 = connection.prepareStatement(sqlStatement19);
            statement19.executeUpdate();
            statement19.close();
            PreparedStatement statement20 = connection.prepareStatement(sqlStatement20);
            statement20.executeUpdate();
            statement20.close();
            PreparedStatement statement21 = connection.prepareStatement(sqlStatement21);
            statement21.executeUpdate();
            statement21.close();
            PreparedStatement statement22 = connection.prepareStatement(sqlStatement22);
            statement22.executeUpdate();
            statement22.close();
            PreparedStatement statement23 = connection.prepareStatement(sqlStatement23);
            statement23.executeUpdate();
            statement23.close();
            PreparedStatement statement24 = connection.prepareStatement(sqlStatement24);
            statement24.executeUpdate();
            statement24.close();
            PreparedStatement statement25 = connection.prepareStatement(sqlStatement25);
            statement25.executeUpdate();
            statement25.close();
            PreparedStatement statement26 = connection.prepareStatement(sqlStatement26);
            statement26.executeUpdate();
            statement26.close();
            PreparedStatement statement27 = connection.prepareStatement(sqlStatement27);
            statement27.executeUpdate();
            statement27.close();
            PreparedStatement statement28 = connection.prepareStatement(sqlStatement28);
            statement28.executeUpdate();
            statement28.close();
            PreparedStatement statement29 = connection.prepareStatement(sqlStatement29);
            statement29.executeUpdate();
            statement29.close();
            PreparedStatement statement30 = connection.prepareStatement(sqlStatement30);
            statement30.executeUpdate();
            statement30.close();
            PreparedStatement statement31 = connection.prepareStatement(sqlStatement31);
            statement31.executeUpdate();
            statement31.close();
            PreparedStatement statement32 = connection.prepareStatement(sqlStatement32);
            statement32.executeUpdate();
            statement32.close();
            PreparedStatement statement33 = connection.prepareStatement(sqlStatement33);
            statement33.executeUpdate();
            statement33.close();
            PreparedStatement statement34 = connection.prepareStatement(sqlStatement34);
            statement34.executeUpdate();
            statement34.close();
            PreparedStatement statement35 = connection.prepareStatement(sqlStatement35);
            statement35.executeUpdate();
            statement35.close();
            PreparedStatement statement36 = connection.prepareStatement(sqlStatement36);
            statement36.executeUpdate();
            statement36.close();
            PreparedStatement statement37 = connection.prepareStatement(sqlStatement37);
            statement37.executeUpdate();
            statement37.close();
            PreparedStatement statement38 = connection.prepareStatement(sqlStatement38);
            statement38.executeUpdate();
            statement38.close();
            PreparedStatement statement39 = connection.prepareStatement(sqlStatement39);
            statement39.executeUpdate();
            statement39.close();
            PreparedStatement statement40 = connection.prepareStatement(sqlStatement40);
            statement40.executeUpdate();
            statement40.close();
            PreparedStatement statement41 = connection.prepareStatement(sqlStatement41);
            statement41.executeUpdate();
            statement41.close();
            PreparedStatement statement42 = connection.prepareStatement(sqlStatement42);
            statement42.executeUpdate();
            statement42.close();
            PreparedStatement statement43 = connection.prepareStatement(sqlStatement43);
            statement43.executeUpdate();
            statement43.close();
            PreparedStatement statement44 = connection.prepareStatement(sqlStatement44);
            statement44.executeUpdate();
            statement44.close();
            PreparedStatement statement45 = connection.prepareStatement(sqlStatement45);
            statement45.executeUpdate();
            statement45.close();
            PreparedStatement statement46 = connection.prepareStatement(sqlStatement46);
            statement46.executeUpdate();
            statement46.close();
            PreparedStatement statement47 = connection.prepareStatement(sqlStatement47);
            statement47.executeUpdate();
            statement47.close();
            PreparedStatement statement48 = connection.prepareStatement(sqlStatement48);
            statement48.executeUpdate();
            statement48.close();
            PreparedStatement statement49 = connection.prepareStatement(sqlStatement49);
            statement49.executeUpdate();
            statement49.close();
            PreparedStatement statement50 = connection.prepareStatement(sqlStatement50);
            statement50.executeUpdate();
            statement50.close();
            PreparedStatement statement51 = connection.prepareStatement(sqlStatement51);
            statement51.executeUpdate();
            statement51.close();
            PreparedStatement statement52 = connection.prepareStatement(sqlStatement52);
            statement52.executeUpdate();
            statement52.close();
            PreparedStatement statement53 = connection.prepareStatement(sqlStatement53);
            statement53.executeUpdate();
            statement53.close();
            PreparedStatement statement54 = connection.prepareStatement(sqlStatement54);
            statement54.executeUpdate();
            statement54.close();
            PreparedStatement statement55 = connection.prepareStatement(sqlStatement55);
            statement55.executeUpdate();
            statement55.close();
            PreparedStatement statement56 = connection.prepareStatement(sqlStatement56);
            statement56.executeUpdate();
            statement56.close();
            PreparedStatement statement57 = connection.prepareStatement(sqlStatement57);
            statement57.executeUpdate();
            statement57.close();
            PreparedStatement statement58 = connection.prepareStatement(sqlStatement58);
            statement58.executeUpdate();
            statement58.close();
            PreparedStatement statement59 = connection.prepareStatement(sqlStatement59);
            statement59.executeUpdate();
            statement59.close();
            PreparedStatement statement60 = connection.prepareStatement(sqlStatement60);
            statement60.executeUpdate();
            statement60.close();
        }catch (SQLException e){
            throw new SQLException(e.getMessage());
        }
    }

    /*TODo:
        1. update
        -------------------
        Table:
            Name: ShiftStructure
            Columns: day(INT), shift_time(INT), job_title(STRING), num_of_emp(INT)
        */
}
