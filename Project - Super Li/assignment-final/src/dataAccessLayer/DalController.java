package dataAccessLayer;

import businessLayer.Sup_Inv.InventoryController;
import businessLayer.deliveryPackage.*;
import businessLayer.deliveryPackage.Document;
import businessLayer.deliveryPackage.Driver;
import businessLayer.deliveryPackage.Location;
import businessLayer.employeePackage.Constraint;
import businessLayer.employeePackage.Contract;
import businessLayer.employeePackage.Employee;
import businessLayer.shiftPackage.Shift;
import dataAccessLayer.DeliveryModule.DeliveryMapper;
import dataAccessLayer.DeliveryModule.DocumentMapper;
import dataAccessLayer.DeliveryModule.DriverMapper;
import dataAccessLayer.DeliveryModule.TruckMapper;
import dataAccessLayer.Sup_inv_dal.DBHandler;
import dataAccessLayer.mapperPackage.*;
import businessLayer.Response;

import java.io.File;
import java.sql.*;
import java.text.ParseException;
import java.util.*;
import java.util.Date;

public class DalController {
    private static DalController dalController = null;
    private final String fileName = "superLee.db";
    private final String url =  "jdbc:sqlite:" + fileName;
    private EmployeeMapper empMapper;
    private BankMapper bankMapper;
    private ContractMapper contractMapper;
    private ShiftMapper shiftMapper;
    private ConstraintMapper constraintMapper;
    private EnrollmentMapper enrollmentMapper;
    private ShiftsStructureMapper structureMapper;
    private DeliveryMapper deliveryMapper;
    private DocumentMapper documentMapper;
    private DriverMapper driverMapper;
    private TruckMapper truckMapper;
    private MessageMapper messageMapper; // new
    private DBHandler dbHandler;

    private DalController() throws Exception {
        bankMapper = BankMapper.getInstance(url);
        constraintMapper = ConstraintMapper.getInstance(url);
        contractMapper = ContractMapper.getInstance(url);
        shiftMapper = ShiftMapper.getInstance(url);
        enrollmentMapper = EnrollmentMapper.getInstance(url);
        structureMapper = ShiftsStructureMapper.getInstance(url);
        empMapper = EmployeeMapper.getInstance(url);
        deliveryMapper = new DeliveryMapper(url, this); // new
        documentMapper = new DocumentMapper(url);
        driverMapper = new DriverMapper(url);
        truckMapper = new TruckMapper(url);
        messageMapper = new MessageMapper(url); // new
        if (!new File(fileName).exists()){
            initialization();
            initData();
            dbHandler = DBHandler.getInstance(url);
        }
        loadData();
    }

    public static DalController getInstance() throws Exception {
        if(dalController == null){
            dalController = new DalController();
        }
        return  dalController;
    }

    public DBHandler getDbHandler(){
        if (dbHandler == null){
            return DBHandler.getInstance(url);
        }
        return dbHandler;
    }

    //----------------------------------------------------------------------------------------------------------------------
    //INIT DATA:
    private void initData() throws SQLException {
        Date today = null;
        Calendar calendar;
        calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        today = calendar.getTime();
        calendar.setTime(today);
        calendar.add(Calendar.DATE, 2);
        today = calendar.getTime();
        if(today.toString().substring(0, 3).equals("Fri") || today.toString().substring(0, 3).equals("Sat")){
            calendar.add(Calendar.DATE, 2);
            today = calendar.getTime();
        }
        String strDate = convertDate(today);
        structureMapper.initStructureTable();
        empMapper.initEmployeeTable();
        contractMapper.initContractTable();
        shiftMapper.initShiftTable(strDate);
        constraintMapper.initConstraintsTable();
        bankMapper.initBankTable();
        enrollmentMapper.initEnrollmentTable(strDate);
        truckMapper.initTrucksTable();
        driverMapper.initDriversTable();
        deliveryMapper.initDeliveriesTable();
        truckMapper.initTrucksSchedulerTable();
        documentMapper.initDocumentsTable();
        documentMapper.initItemsTable();
    }
    //----------------------------------------------------------------------------------------------------------------------

    //----------------------------------------------------------------------------------------------------------------------
    //LOAD DATA:
    private void loadData() throws Exception {
        //weekly shifts
        Date today = null;
        Calendar calendar;
        for(int i = 0; i < 7; i++){
            calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            today = calendar.getTime();
            calendar.setTime(today);
            calendar.add(Calendar.DATE, i);
            today = calendar.getTime();
            loadWeeklyShifts(today, "morning");
            loadWeeklyShifts(today, "evening");
        }
        //employee's enrollment of the week
        for(int i = 0; i < 7; i++){
            calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            today = calendar.getTime();
            calendar.setTime(today);
            calendar.add(Calendar.DATE, i);
            today = calendar.getTime();
            loadEmployeesEnrollment(today);
        }
        //employee of the week
        //employee's bank of the week
        //employee's contract of the week
        //employee's constraint of the week
        for(int i = 0; i < 7; i++){
            calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            today = calendar.getTime();
            calendar.setTime(today);
            calendar.add(Calendar.DATE, i);
            today = calendar.getTime();
            loadEmployeesDataOfTheWeek(today, "morning");
            loadEmployeesDataOfTheWeek(today, "evening");
        }
        //load structure
        loadStructure();
    }

    private void loadEmployeesDataOfTheWeek(Date shiftDate, String shiftTime) throws SQLException {
        String strShiftDate= convertDate(shiftDate);
        ArrayList<String> employeesId = enrollmentMapper.getEmployeesInShift(shiftDate, strShiftDate, shiftTime);
        for(String id : employeesId) {
            empMapper.getEmployee(id);
            bankMapper.getBank(id);
            contractMapper.getContract(id);
            constraintMapper.getEmpConstraints(id);
        }
    }

    private void loadWeeklyShifts(Date today, String shiftTime) throws Exception {
        String strShiftDate= convertDate(today);
        shiftMapper.getShift(today, strShiftDate, shiftTime);
    }

    private void loadEmployeesEnrollment(Date shiftDate) throws SQLException {
        String strShiftDate = convertDate(shiftDate);
        enrollmentMapper.getEnrollment(shiftDate, strShiftDate);
    }

    private void loadStructure() throws SQLException {
        structureMapper.load();
    }
//----------------------------------------------------------------------------------------------------------------------

    private void initialization() throws SQLException, ParseException {
        createDatabase();
        createEmpTable();
        createShiftTable();
        createBankTable();
        createContractTable();
        createEnrollmentTable();
        createStructureTable();
        createConstraintTable();
        createDriversTable();
        createTrucksTable();
        createTrucksSchedulerTable();
        createDeliveriesTable();
        createDocumentsTable();
        createItemsTable();
        createMessagesTable(); // new
    }

    public String convertDate(Date date){
        String day = date.toString().substring(8, 10);
        String year = date.toString().substring(24, 28);
        String tmp = date.toString().substring(4, 7);
        String month = "";
        switch (tmp) {
            case "Jan":
                month = "01";
                break;
            case "Feb":
                month = "02";
                break;
            case "Mar":
                month = "03";
                break;
            case "Apr":
                month = "04";
                break;
            case "May":
                month = "05";
                break;
            case "Jun":
                month = "06";
                break;
            case "Jul":
                month = "07";
                break;
            case "Aug":
                month = "08";
                break;
            case "Sep":
                month = "09";
                break;
            case "Oct":
                month = "10";
                break;
            case "Nov":
                month = "11";
                break;
            case "Dec":
                month = "12";
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + month);
        }
        String result = day+"/"+month+"/"+year;
        return result;
    }

    private Response<String> createDatabase() {
        Connection c = null;
        Response<String> response;
        try{
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection(url);
            response = new Response<String>("database successfully created");
        }catch(Exception e) {
            response = new Response<String>(e.getClass().getName() + ": " + e.getMessage());
        }
        return response;
    }

    private void createStructureTable() {
        Connection c = null;
        Statement stmt = null;
        try{
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection(url);
            stmt = c.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS structure (\n"
                    + " shift_time INTEGER NOT NULL,\n"
                    + " day INTEGER NOT NULL,\n"
                    + " job_title TEXT NOT NULL,\n"
                    + " num_of_emp INTEGER NOT NULL,\n"
                    + "PRIMARY KEY(shift_time, day, job_title)\n"
                    + ");";
            stmt.executeUpdate(sql);
            stmt.close();
            c.close();
        }catch ( Exception e ) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }

    private void createEnrollmentTable() {
        Connection c = null;
        Statement stmt = null;
        try{
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection(url);
            stmt = c.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS enrollment (\n"
                    + " date TEXT NOT NULL,\n"
                    + " shift_time TEXT NOT NULL,\n"
                    + " id TEXT NOT NULL,\n"
                    + "PRIMARY KEY(date, shift_time, id),\n"
                    + " FOREIGN KEY(id) REFERENCES employee(id),\n"
                    + " FOREIGN KEY(date, shift_time) REFERENCES shift(date, shift_time)\n"
                    + ");";
            stmt.executeUpdate(sql);
            stmt.close();
            c.close();
        }catch ( Exception e ) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }

    private void createConstraintTable() {
        Connection c = null;
        Statement stmt = null;
        try{
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection(url);
            stmt = c.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS constraints (\n"
                    + " day INTEGER NOT NULL,\n"
                    + " shift_time TEXT NOT NULL,\n"
                    + " id TEXT NOT NULL,\n"
                    + " reason TEXT NOT NULL,\n"
                    + "PRIMARY KEY(day, shift_time, id),\n"
                    + " FOREIGN KEY(id) REFERENCES employee(id)\n"
                    + ");";
            stmt.executeUpdate(sql);
            stmt.close();
            c.close();
        }catch ( Exception e ) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }

    private void createContractTable() {
        Connection c = null;
        Statement stmt = null;
        try{
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection(url);
            stmt = c.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS contract (\n"
                    + " id TEXT PRIMARY KEY,\n"
                    + " salary INTEGER NOT NULL,\n"
                    + " start_date TEXT NOT NULL,\n"
                    + " job_title TEXT NOT NULL,\n"
                    + " job_type TEXT NOT NULL,\n"
                    + " FOREIGN KEY(id) REFERENCES employee(id)\n"
                    + ");";
            stmt.executeUpdate(sql);
            stmt.close();
            c.close();
        }catch ( Exception e ) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }

    private void createBankTable() {
        Connection c = null;
        Statement stmt = null;
        try{
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection(url);
            stmt = c.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS bank (\n"
                    + " id TEXT PRIMARY KEY,\n"
                    + " name TEXT NOT NULL,\n"
                    + " account_number INTEGER NOT NULL,\n"
                    + " department INTEGER NOT NULL,\n"
                    + " FOREIGN KEY(id) REFERENCES employee(id)\n"
                    + ");";
            stmt.executeUpdate(sql);
            stmt.close();
            c.close();
        }catch ( Exception e ) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }

    private void createShiftTable() {
        Connection c = null;
        Statement stmt = null;
        try{
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection(url);
            stmt = c.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS shift (\n"
                    + " date TEXT NOT NULL,\n"
                    + " shift_time TEXT NOT NULL,\n"
                    + " day INTEGER NOT NULL,\n"
                    + "PRIMARY KEY(date, shift_time)\n"
                    + ");";
            stmt.executeUpdate(sql);
            stmt.close();
            c.close();
        }catch ( Exception e ) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }

    private void createEmpTable() {
        Connection c = null;
        Statement stmt = null;
        try{
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection(url);
            stmt = c.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS employee (\n"
                    + " id TEXT PRIMARY KEY,\n"
                    + " name TEXT NOT NULL,\n"
                    + " is_manager INTEGER NOT NULL,\n"             //true(1) , false(0)
                    + " is_shift_manager INTEGER NOT NULL\n"
                    + ");";
            stmt.executeUpdate(sql);
            stmt.close();
            c.close();
        }catch ( Exception e ) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }

    //------------------------------------------------------------------------------------------------------------------
    //SHIFT CONTROLLER:
    public ArrayList<Shift> getShiftsPerDate(Date shiftDate) throws SQLException {
        String strShiftDate = convertDate(shiftDate);
        return shiftMapper.getShiftsPerDate(shiftDate, strShiftDate);
    }

    public void deleteShift(Date date, String shift) throws SQLException {
        String strShiftDate = convertDate(date);
        shiftMapper.deleteShift(date, strShiftDate, shift);
    }

    public void insertShift(Date date, String shift, int day) throws Exception {
        //Date shiftDate, String strShiftDate, String shiftTime, int shiftDay
        String strShiftTime = convertDate(date);
        shiftMapper.insertShift(date, strShiftTime, shift, day);
    }

    public ArrayList<Date> getDates() throws SQLException {
        return shiftMapper.getDates();
    }

    public void assignEmployee(Date date, String shift, String id) throws Exception {
        System.out.println("in assign employee");
        String strShiftDate = convertDate(date);
        enrollmentMapper.insertEmployeeToShift(date, strShiftDate, shift, id);
    }
    //------------------------------------------------------------------------------------------------------------------
    //SHIFT STRUCTURE:
    public void updateNumOfEmp(int day, int time, String jobTitle, int amount) throws SQLException {
        structureMapper.updateNumOfEmp(day, time, jobTitle, amount);
    }

    public String printDailyShiftStructure(int day, int shiftTime) {
        return structureMapper.printDailyShiftStructure(day, shiftTime);
    }

    public Set<String> getJobTitles(int day, int time) {
        return structureMapper.getJobTitles(day, time);
    }

    public int getNumOfEmpInJobTitle(int day, int time, String jobTile) {
        return structureMapper.getNumOfEmpInJobTitle(day, time, jobTile);
    }
    //------------------------------------------------------------------------------------------------------------------
    //SHIFT:
    public Map<String, ArrayList<String>> getWorkers(Date shiftDate, String shift) throws Exception {    //Map<jobTitle, [ id ]>
        Map<String, ArrayList<String>> result = new HashMap<String, ArrayList<String>>();
        String strShiftDate = convertDate(shiftDate);
        ArrayList<String> employeesId = enrollmentMapper.getEmployeesInShift(shiftDate, strShiftDate, shift);
        String jobTitle = "";
        for(String id : employeesId){
            jobTitle = contractMapper.getJobTitle(id);
            if(result.containsKey(jobTitle))
                result.get(jobTitle).add(id);
            else{
                ArrayList<String> employees = new ArrayList<String>();
                employees.add(id);
                result.put(jobTitle, employees);
            }
        }
        return result;
    }
    //------------------------------------------------------------------------------------------------------------------
    //SHIPMENTS:
    public boolean checkStoreKeeperInShift(String shiftTime, Date shiftDate) throws Exception {
        String strShiftDate = convertDate(shiftDate);
        ArrayList<String> employeesId = enrollmentMapper.getEmployeesInShift(shiftDate, strShiftDate, shiftTime);
        String jobTitle = "";
        for(String id : employeesId){
            jobTitle = contractMapper.getJobTitle(id);
            if(jobTitle.equals("storeKeeper"))
                return true;
        }
        return false;
    }

    public boolean checkIfShiftExist(String shiftTime, Date shiftDate) throws Exception {
        String strShiftDate = convertDate(shiftDate);
        return shiftMapper.checkIfShiftExist(shiftTime, shiftDate, strShiftDate);
    }

    public ArrayList<String> getAvailableDrivers(Date shiftDate, String shiftTime, int day) throws SQLException {
        ArrayList<String> drivers = new ArrayList<String>();
        ArrayList<String> availableDrivers = new ArrayList<String>();
        String strShiftDate = convertDate(shiftDate);
        ArrayList<String> allEmployees = empMapper.getAllEmployees();
        for(String id : allEmployees){
            if(contractMapper.checkIfDriver(id))
                drivers.add(id);
        }
        for(String id : drivers){
            if(!enrollmentMapper.isInShift(id, strShiftDate, shiftTime)){
                if(!constraintMapper.hasConstraint(id, shiftTime, day))
                    availableDrivers.add(id);
            }
        }
        return availableDrivers;
    }
    //------------------------------------------------------------------------------------------------------------------
    //EMPLOYEE:
    public Employee getEmployee(String empId) throws Exception{

        return empMapper.getEmployee(empId);
    }

    public ArrayList<Constraint> getConstraints(String empId) throws Exception {
        return constraintMapper.getEmpConstraints(empId);
    }

    public void addConstraint(String empId, int day, String shift, String reason) throws Exception {
        constraintMapper.insertConstraint(empId,day,shift,reason);
    }

    public void deleteConstraint(String empId, int day, String shift) throws Exception {
        constraintMapper.deleteConstraint(empId,day,shift);
    }

    public void updateConstraintReason(String empId, int day, String shift, String newReason) throws Exception {
        constraintMapper.updateConstraintReason(empId,day,shift,newReason);
    }

    public void updateConstraintDay(String empId, int oldDay, String oldShift, int newDay, String newShift) throws Exception {
        constraintMapper.updateConstraintDay(empId,oldDay,oldShift,newDay,newShift);
    }

    public void createEmployee(String id, String name, boolean isShiftManager, boolean isManager) throws Exception {
        empMapper.insertEmployee(id, name, isShiftManager, isManager);
    }

    public void createBank(String id, int accountNumber, int department, String bankName) throws Exception {
        bankMapper.insertBank(id,bankName,accountNumber,department);
    }

    public void createContract(String id, String jobType, String jobTitle, int salary, Date startDate) throws Exception {
        String conStartDate= convertDate(startDate);
        contractMapper.insertContract(id,salary,conStartDate,jobTitle,jobType);
    }

    public void upgradeToShiftManager(String id) throws Exception {
        empMapper.updateEmployeeToShiftManager(id,true);
    }

    public ArrayList<String> getEmployees() throws SQLException {
        return empMapper.getAllEmployees();
    }

    public Contract getContract(String id) throws Exception {
        return contractMapper.getContract(id);
    }

    public Constraint getConstraint(String id, int day, String time) throws Exception {
        return constraintMapper.getConstraint(id,day,time);
    }

    public void updateBankData(String empId, int newAccountNumber, int newDepartment, String newBankName) throws Exception {
        bankMapper.updateBank(empId,newAccountNumber,newDepartment,newBankName);
    }

    public void updateContractData(String empId, int salary, String jobType) throws Exception {
        contractMapper.updateContract(empId,jobType,salary);
    }

    public void deleteContract(String id) throws Exception {
        contractMapper.deleteContract(id);
    }

    public void deleteBank(String id) throws Exception {
        bankMapper.deleteBank(id);
    }

    public void deleteEmployee(String id) throws Exception {
        empMapper.deleteEmployee(id);
        driverMapper.deleteDriver(id);
    }

    public String printPersonalData(String id) throws Exception {
        String data = "";
        data = data + "--------------------Personal Information--------------------\n"+"Name: " + empMapper.getEmployee(id).getName()+ "\n" + "id: " + id +
                "\n" + "Bank Account: \n" + "    bank name: " + bankMapper.getBank(id).getBankName() + "\n" + "    account number: " + bankMapper.getBank(id).getAccountNumber() +
                "\n" + "    department: " + bankMapper.getBank(id).getDepartment() + "\n" + "Contract: \n" + "    Salary: " + contractMapper.getContract(id).getSalary() + "\n" +
                "    start date: " + contractMapper.getContract(id).getStartDate() + "\n" + "    job title: " + contractMapper.getContract(id).getJobTitle() + "\n" + "    job type: " + contractMapper.getContract(id).getJobType() +
                "\n"+ "    isShiftManager: "+ empMapper.getEmployee(id).getIsShiftManager()+ "\n";
        return data;
    }

    public ArrayList<String> getEmployeesInShift(Date shiftDate, String shiftTime) throws SQLException {
        String strShiftDate = convertDate(shiftDate);
        return enrollmentMapper.getEmployeesInShift(shiftDate, strShiftDate, shiftTime);
    }

    public void addDriverLicense(String empId, String name,  String license) throws Exception{
        driverMapper.addDriver(empId, name, license);
    }

    public void deleteEmpFromShift(Date date, String shift, String id) throws SQLException {
        String strShiftDate = convertDate(date);
        enrollmentMapper.deleteEmpFromShift(date, strShiftDate, shift, id);
    }

    public boolean checkUpcomingShifts(String id) throws SQLException {
        Date today = null;
        Calendar calendar;
        calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        today = calendar.getTime();
        calendar.setTime(today);
        String strDate = convertDate(today);
        return enrollmentMapper.checkUpcomingShifts(strDate, id);
    }
    //------------------------------------------------------------------------------------------------------------------
    //DELIVERY GROUP:
    private void createTrucksTable() {
        Connection c = null;
        Statement stmt = null;
        try{
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection(url);
            stmt = c.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS Trucks (\n"
                    + " plateNumber TEXT PRIMARY KEY,\n"
                    + " model TEXT NOT NULL,\n"
                    + " netWeight REAL NOT NULL,\n" //DOUBLE
                    + " maxWeight REAL NOT NULL\n" //DOUBLE
                    + ");";
            stmt.executeUpdate(sql);
            stmt.close();
            c.close();
        }catch ( Exception e ) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }

    private void createTrucksSchedulerTable() {
        Connection c = null;
        Statement stmt = null;
        try{
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection(url);
            stmt = c.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS TrucksScheduler (\n"
                    + " plateNumber TEXT NOT NULL,\n"
                    + " Date TEXT NOT NULL,\n"
                    + " shift TEXT NOT NULL,\n"
                    + " PRIMARY KEY(plateNumber, Date, shift),\n"
                    + " FOREIGN KEY(plateNumber) REFERENCES Trucks(plateNumber)\n"
                    + ");";
            stmt.executeUpdate(sql);
            stmt.close();
            c.close();
        }catch ( Exception e ) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }

    private void createDriversTable() {
        Connection c = null;
        Statement stmt = null;
        try{
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection(url);
            stmt = c.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS Drivers (\n"
                    + " id TEXT PRIMARY KEY,\n"
                    + " name TEXT NOT NULL,\n"
                    + " license Text NOT NULL\n"
                    + ");";
            stmt.executeUpdate(sql);
            stmt.close();
            c.close();
        }catch ( Exception e ) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }

    private void createItemsTable() {
        Connection c = null;
        Statement stmt = null;
        try{
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection(url);
            stmt = c.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS Items (\n"
                    + " documentId INTEGER NOT NULL,\n"
                    + " code INTEGER NOT NULL,\n"
                    + " itemName TEXT NOT NULL,\n"
                    + " amount INTEGER NOT NULL,\n"
                    + " weight REAL NOT NULL,\n"
                    + " PRIMARY KEY(documentId, code),\n"
                    + " FOREIGN KEY(documentId) REFERENCES Documents(documentId)\n"
                    + ");";
            stmt.executeUpdate(sql);
            stmt.close();
            c.close();
        }catch ( Exception e ) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }

    private void createDocumentsTable() {
        Connection c = null;
        Statement stmt = null;
        try{
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection(url);
            stmt = c.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS Documents (\n"
                    + " documentId INTEGER PRIMARY KEY,\n"
                    + " deliveryId INTEGER NOT NULL,\n"
                    + " totalWeight REAL NOT NULL,\n"
                    + " destinationAddress TEXT NOT NULL,\n"
                    + " phoneNumber TEXT NOT NULL,\n"
                    + " contactName TEXT NOT NULL,\n"
                    + " FOREIGN KEY(deliveryId) REFERENCES Deliveries(id)\n"
                    + ");";
            stmt.executeUpdate(sql);
            stmt.close();
            c.close();
        }catch ( Exception e ) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }

    private void createDeliveriesTable() {
        Connection c = null;
        Statement stmt = null;
        try{
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection(url);
            stmt = c.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS Deliveries (\n"
                    + " id INTEGER PRIMARY KEY,\n"
                    + " date TEXT NOT NULL,\n"
                    + " launchTime TEXT NOT NULL,\n"
                    + " truckPlateNumber TEXT NOT NULL,\n"
                    + " driverId TEXT NOT NULL,\n"
                    + " sourceAddress TEXT NOT NULL,\n"
                    + " phoneNumber TEXT NOT NULL,\n"
                    + " contactName TEXT NOT NULL,\n"
                    + " FOREIGN KEY(truckPlateNumber) REFERENCES Trucks(plateNumber),\n"
                    + " FOREIGN KEY(driverId) REFERENCES Drivers(id)\n"
                    + ");";
            stmt.executeUpdate(sql);
            stmt.close();
            c.close();
        }catch ( Exception e ) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }

    private void createMessagesTable() { // new
        Connection c = null;
        Statement stmt = null;
        try{
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection(url);
            stmt = c.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS Messages (\n"
                    + " recipient TEXT NOT NULL,\n"
                    + " message TEXT PRIMARY KEY,\n"
                    + " orderId INTEGER NOT NULL\n"
                    + ");";
            stmt.executeUpdate(sql);
            stmt.close();
            c.close();
        }catch ( Exception e ) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }

    // --------------  Delivery + Document  -----------------
//    public Delivery getDelivery(Date date, String launchTime, String truckPlateNumber) throws Exception {
//        return deliveryMapper.getDelivery(date, launchTime, truckPlateNumber);
//    }

    public Delivery getDelivery(int deliveryId) throws Exception {
        return deliveryMapper.getDelivery(deliveryId);
    }

    public List<Delivery> getAllDeliveries() throws Exception{
        return deliveryMapper.getAllDeliveries();
    }

    public Delivery addDelivery(int deliveryId, Date date, String launchTime, String plateNumber, String driverId, Location source, Map<Location, LinkedList<Item>> itemsPerDest) throws Exception { // TODO
        return deliveryMapper.addDelivery(deliveryId, convertDate(date), launchTime, plateNumber, driverId, source, itemsPerDest);
    }

//    public Delivery cancelDelivery(Date date, String launchTime, String truckPlateNumber) throws Exception{
//        return deliveryMapper.cancelDelivery(date, launchTime, truckPlateNumber);
//    }

    public Delivery cancelDelivery(int deliveryId) throws Exception{
        return deliveryMapper.cancelDelivery(deliveryId);
    }

    public void addDocument(int documentId, int deliveryId, Location destination, List<Item> items, double totalWeight) throws Exception {
        documentMapper.addDocument(documentId, deliveryId, destination, items, totalWeight);
    }

    public Map<Delivery, Document> getDocument(int documentId) throws Exception {
        int deliveryId = -1;
        Map<Delivery, Document> map = new HashMap<>();
        Document document = null;
        String sql = "SELECT * FROM Documents WHERE documentId= '" + documentId + "'";
        try (Connection conn = DriverManager.getConnection(url);
             Statement statement = conn.createStatement()) {
            ResultSet rs = statement.executeQuery(sql);
            if (rs.next()) {
                document = new Document(rs.getInt(1),
                                        rs.getInt(2),
                                        new Location(rs.getString(4), rs.getString(5), rs.getString(6)),
                                        documentMapper.getItems(rs.getInt(1)),
                                        rs.getDouble(3));
                deliveryId = rs.getInt(2);
            }
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
        if(deliveryId != -1){
            map.put(deliveryMapper.getDelivery(deliveryId), document);
            return map;
        }
        throw new Exception("This document is not exist");
    }

    public List<Document> getDeliveryDocuments(int deliveryId) throws Exception{
        return documentMapper.getDeliveryDocuments(deliveryId);
    }

    public Map<Location, LinkedList<Item>> getItemsPerDest(int deliveryId) throws Exception{
        return documentMapper.getItemsPerDest(deliveryId);
    }

    public void deleteDocuments(int deliveryId) throws Exception{
        documentMapper.deleteDocuments(deliveryId);
    }

    // ------------ Truck --------------
    public Truck getTruck(String plateNumber) throws Exception {
        return truckMapper.getTruck(plateNumber);
    }

    public List<Truck> getAllTrucks() throws Exception{
        return truckMapper.getAllTrucks();
    }

    public void addTruck(String plateNumber, String model, double netWeight, double maxWeight) throws Exception{
        truckMapper.addTruck(plateNumber, model, netWeight, maxWeight);
    }

    public Truck deleteTruck(String plateNumber) throws Exception{
        return truckMapper.deleteTruck(plateNumber);
    }

    public Truck getAvailableTruck(Date date, String shift, double weight) throws Exception{
        return truckMapper.getAvailableTruck(date, shift, weight);
    }

    public void addTruckScheduler(String plateNumber, Date date, String shift) throws Exception{
        truckMapper.addTruckScheduler(plateNumber, date, shift);
    }

    public void deleteTruckScheduler(String plateNumber, String  date, String shift) throws Exception{
        truckMapper.deleteTruckScheduler(plateNumber, date, shift);
    }

//    public void addDriver(String id, String name, String license) throws Exception{
//        driverMapper.addDriver(id, name, license);
//    }

    public Driver getAvailableDriver(Date date, String shift, double weight) throws Exception{
        int day = date.getDay();
        day++;
        ArrayList<String> availableDriversIds = getAvailableDrivers(date, shift, day);
        Driver driver = driverMapper.getAvailableDriver(date, shift, weight, availableDriversIds);
        this.assignEmployee(date, shift, driver.getId());
        System.out.println("after assign");
        return driver;
    }

    public List<String> getAllDriversIDs() throws Exception{
        return driverMapper.getAllDriversIDs();
    }

    public List<String> getDriversByLicense(String license) throws Exception{
        return driverMapper.getDriversByLicense(license);
    }

    // ------------ Message -------------- // new
    public void addMessage(String recipient, String msg, int orderId) throws Exception { // ??????
        String message = "Order ID: " + orderId + ", " + msg;
        messageMapper.addMessage(recipient, message, orderId);

//        if(recipient.equals("manager")){
//            String message = "Order ID: " + orderId + " " + msg;
//            messageMapper.addMessage(recipient, message, orderId);
//        }
//        else if(recipient.equals("storeKeeper")){
//
//        }
        //else throw new Exception(recipient + "can't add message");
    }

    public void deleteMessages(int orderId) throws Exception {
        messageMapper.deleteMessages(orderId);
    }

    public List<String> getMessages(String recipient) throws Exception {
        return messageMapper.getMessages(recipient);
    }

    public boolean checkIfShiftHasDelivery (String date, String shift) throws Exception {
        return deliveryMapper.checkIfShiftHasDelivery(date, shift);
    }

    public Map<String, ArrayList<String>> getUpcomingShifts(String id) throws SQLException {
        Date today = null;
        Calendar calendar;
        calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        today = calendar.getTime();
        calendar.setTime(today);
        String strDate = convertDate(today);
        return enrollmentMapper.getUpcomingShifts(strDate, id);
    }

    public String getSupplier (String id) throws Exception {
        return dbHandler.getSupplierStringByPhoneNumber(id);
    }



}
