package businessLayer;
import businessLayer.employeePackage.EmployeeController;
import businessLayer.shiftPackage.ShiftController;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Fcade {
    private EmployeeController employeeController;
    private ShiftController shiftController;
    private boolean isManager;

    public Fcade() throws Exception {
        this.employeeController = new EmployeeController();
        this.shiftController = new ShiftController();
        this.isManager = false;
    }

    //-----------------------------------------Employee methods---------------------------------------------

    public Response<String> login(String id){
        Response<String> responseT;
        String response = "regular";
        try{
            isManager = employeeController.login(id);
            if(isManager){
                response = "manager";
            }
            responseT = new Response<String>("Login Ended Successfully", false, response);
        }catch(Exception e){
            responseT = new Response<String>(e.getMessage(), true);
        }
        return responseT;
    }

    public Response<?> logout(){
        this.employeeController.logout();
        return new Response<>("Logout Ended Successfully", false);
    }

    public Response<String> printPersonalData() throws Exception {
        String data = employeeController.printPersonalData();
        return new Response<String>(data);
    }

    public Response<String> printConstraints() throws Exception {
        String dataConstraints = employeeController.printConstraints();
        return new Response<String>(dataConstraints);
    }

    public Response<?> addConstraint(int day, String shift, String reason){
        Response<?> response;
        try{
            employeeController.addConstraint(day, shift, reason);
            response = new Response<>("Adding Constraint Ended Successfully", false);
        }catch(Exception e){
            response = new Response<>(e.getMessage(), true);
        }
        return response;
    }

    public Response<?> deleteConstraint(int day, String shift){
        Response<?> response;
        try{
            employeeController.deleteConstraint(day, shift);
            response = new Response<>("Deleting Constraint Ended Successfully", false);
        }catch(Exception e){
            response = new Response<>(e.getMessage(), true);
        }
        return response;
    }

    public Response<?> deleteRegularConstraint(String empId, int day, String shift){
        Response<?> response;
        try{
            employeeController.deleteRegularConstraint(empId, day, shift);
            response = new Response<>("Deleting Constraint Ended Successfully", false);
        }catch(Exception e){
            response = new Response<>(e.getMessage(), true);
        }
        return response;
    }

    public Response<?> updateConstraintReason(int day, String shift, String newReason) {
        Response<?> response;
        try{
            employeeController.updateConstraintReason(day, shift, newReason);
            response = new Response<>("updating Constraint Ended Successfully", false);
        }catch(Exception e){
            response = new Response<>(e.getMessage(), true);
        }
        return response;
    }

    public Response<?> updateConstraintDay(int oldDay, String oldShift, int newDay, String newShift) {
        Response<?> response;
        try{
            employeeController.updateConstraintDay(oldDay, oldShift, newDay, newShift);
            response = new Response<>("Updating Constraint Ended Successfully", false);
        }catch(Exception e){
            response = new Response<>(e.getMessage(), true);
        }
        return response;
    }

    public Response<?> createEmployee(String name, String id, int accountNumber, int department, String bankName, int salary, Date startDate,
                                      String jobTitle, String jobType, boolean isShiftManager) {
        Response<?> response;
        try{
            employeeController.createEmployee(name, id, accountNumber, department, bankName, salary, startDate, jobTitle,jobType,isShiftManager);
            response = new Response<>("Creating Employee Ended Successfully", false);
        }catch(Exception e){
            response = new Response<>(e.getMessage(), true);
        }
        return response;
    }

    public Response<?> upgradeToShiftManager(String id){
        Response<?> response;
        try{
            employeeController.upgradeToShiftManager(id);
            String message = "Upgrading Employee: "+id+" Ended Successfully";
            response = new Response<>(message, false);
        }catch(Exception e){
            response = new Response<>(e.getMessage(), true);
        }
        return response;
    }

    public Response<String> suggestEmployees(String jobTitle, int day, int shiftTime) throws Exception {
            shiftTime = shiftTime - 1;      //morning - 0, evening - 1
            ArrayList<String> employees = employeeController.suggestEmployees(jobTitle, day, shiftTime);
            employees = shiftController.filterEmployees(employees, day, shiftTime, jobTitle);
            String suggestion = employeeController.getEmployeesData(employees);
            return new Response<String>(suggestion);
    }

    public Response<String> getConstraints(){
        String constraints = employeeController.getConstraints();
        return new Response<String>(constraints);
    }

    public Response<String> getUserConstrainOption(int choice) {
        String ConstrainOption = employeeController.getUserConstrainOption(choice);
        return new Response<String>(ConstrainOption);
    }

    public Response<String> getJobTitles() {
        String jobTitles = employeeController.getJobTitles();
        return new Response<String>(jobTitles);
    }

    public Response<String> getUserJobTitleOption(int choice) {
        String jobTitleOption = employeeController.getUserJobTitleOption(choice);
        return new Response<String>(jobTitleOption);
    }

    public Response<String> getPossibleEmployees(Date shiftDate, String shiftTime, String jobTitle) throws Exception {
        String possibleWorkers = employeeController.getPossibleEmployees(shiftDate, shiftTime, jobTitle);
        return new Response<String>(possibleWorkers);
    }

    public Response<?> updateEmployeeBankData(String empId, int newAccountNumber, int newDepartment, String newBankName) {
        Response<?> response;
        try{
            employeeController.updateEmployeeBankData(empId, newAccountNumber, newDepartment, newBankName);
            String message = "Updating Employee Bank Data Ended Successfully";
            response = new Response<>(message, false);
        }catch (Exception e){
            response = new Response<>(e.getMessage(), true);
        }
        return response;
    }

    public Response<?> updateEmployeeContractData(String empId, int salary, String jobType) {
        Response<?> response;
        try{
            employeeController.updateEmployeeContractData(empId, salary, jobType);
            String message = "Updating Employee Contract Data Ended Successfully";
            response = new Response<>(message, false);
        }catch (Exception e){
            response = new Response<>(e.getMessage(), true);
        }
        return response;
    }

    public Response<?> deleteEmployee(String id) {
        Response<?> response;
        try{
            String previousEmployeeJobTitle = employeeController.deleteEmployee(id);
            shiftController.deleteEmployeeFromShifts(id, previousEmployeeJobTitle);
            String message = "Deleting Employee Ended Successfully";
            response = new Response<>(message, false);
        }catch (Exception e){
            response = new Response<>(e.getMessage(), true);
        }
        return response;
    }

    public Response<Boolean> checkIfEmployeeHasConstraint(Date shiftDate, String shiftTime, String empId) throws Exception {
        boolean answer = employeeController.checkIfEmployeeHasConstraint(shiftDate, shiftTime, empId);
        return new Response<Boolean>(answer);
    }

    public Response<String> addDriverLicense(String empId, String name, String license) {
        Response<String> response;
        try{
            employeeController.addDriverLicense(empId, name, license);
            String message = "Adding License Ended Successfully";
            response = new Response<>(message, false);
        }catch (Exception e){
            response = new Response<>(e.getMessage(), true);
        }
        return response;
    }

    public Response<String> getIdentity(String id) {
        Response<String> response;
        try {
            String jobTitle = employeeController.getIdentity(id);
            response = new Response<>(jobTitle);
        }catch (Exception e){
            response = new Response<>(e.getMessage());
        }
        return response;
    }

    public Response<String> getEmpReport() {
        Response<String> response;
        try{
            String message = employeeController.getAllEmpIds();
            response = new Response<>(message);
        }catch(Exception e){
            response = new Response<String>(e.getMessage());
        }
        return response;
    }

    //-----------------------------------------Shift methods---------------------------------------------

    public Response<?> createShift(int day, Date date, String shift){
        Response<?> response;
        try{
            shiftController.checkWeekend(day);
            shiftController.createShift(day, date, shift);
            response = new Response<>("Creating Shift Ended Successfully", false);
        }catch(Exception e){
            response = new Response<>(e.getMessage(), true);
        }
        return response;
    }

    public Response<?> updateShift(Date shiftDate, String time, String jobTitle, String id){
        Response<?> response;
        try{
            employeeController.checkShiftAppointment(jobTitle, id, shiftDate, time);
            shiftController.updateShift(shiftDate, time, jobTitle, id);
            response = new Response<>("Updating Shift Ended Successfully", false);
        }catch(Exception e){
            response = new Response<>(e.getMessage(), true);
        }
        return response;
    }

    public Response<?> deleteShift(Date date, String shift){
        Response<?> response;
        try{
            shiftController.deleteShift(date, shift);
            response = new Response<>("Deleting Shift Ended Successfully", false);
        }catch(Exception e){
            response = new Response<>(e.getMessage(), true);
        }
        return response;
    }

    public Response<String> printShift(Date date, String shift){
        Response<String> response;
        try{
            String message = shiftController.printShift(date, shift);
            response = new Response<String>(message);
        }catch(Exception e){
            response = new Response<String>(e.getMessage(), true);
        }
        return response;
    }

    public Response<?> deleteEmployeeFromShift(String id, String jobTitle, Date date, String shiftTime){
        Response<?> response;
        try{
            shiftController.deleteEmployeeFromShift(id, jobTitle, date, shiftTime);
            response = new Response<>("Deleting Assigned Worker Ended Successfully", false);
        }catch(Exception e){
            response = new Response<>(e.getMessage(), true);
        }
        return response;
    }

    public Response<String> printEmployeeShiftsForWeek(String id){
        Response<String> response;
        try{
            String message = shiftController.printEmployeeShiftsForWeek(id);
            response = new Response<String>(message);
        }catch(Exception e){
            response = new Response<String>(e.getMessage(), true);
        }
        return response;
    }

    public Response<String> printEmployeeShiftsForMonth(String id){
        Response<String> response;
        try{
            String message = shiftController.printEmployeeShiftsForMonth(id);
            response = new Response<String>(message);
        }catch(Exception e){
            response = new Response<String>(e.getMessage(), true);
        }
        return response;
    }

    public Response<?> updateShiftStructure(String jobTitle, int amount, int day, String shiftTime) throws SQLException {
        shiftController.updateShiftStructure(jobTitle, amount, day, shiftTime);
        return new Response<>("Updating Shift Sructure Ended Successfully", false);
    }

    public Response<String> printShiftsStructure(){
        String message = shiftController.printShiftsStructure();
        return new Response<String>(message);
    }

    public Response<String> printIncompleteShifts() throws Exception {
        String message = shiftController.printIncompleteShifts();
        return new Response<String>(message);
    }

    public Response<Boolean> checkIncompleteShifts() throws Exception {
        Boolean isIncomplete = shiftController.checkIncompleteShifts();
        return new Response<Boolean>(isIncomplete);
    }

    public Response<String> getWorkingDays() {
        String message = shiftController.getWorkingDays();
        return new Response<String>(message);
    }

    public Response<String> getShiftTime() {
        String message = shiftController.getShifTime();
        return new Response<String>(message);
    }

    public Response<String> getUserShiftTimeOption(int choice) {
        String UserShiftTimeOption = shiftController.getUserShiftTimeOption(choice);
        return new Response<String>(UserShiftTimeOption);
    }

    public Response<Integer> getUserDayOption(int choice) {
        int UserDayOption = shiftController.getUserDayOption(choice);
        return new Response<Integer>(UserDayOption);
    }

    public Response<String> getWeeklyShiftsReport() {
        Response<String> response;
        try{
            String message = shiftController. getWeeklyShiftsReport();
            response = new Response<>(message);
        }
        catch (Exception e){
            response = new Response<>(e.getMessage());
        }
        return response;
    }

    public Response<String> getMonthlyShiftsReport() {
        Response<String> response;
        try{
            String message = shiftController. getMonthlyShiftsReport();
            response = new Response<>(message);
        }
        catch (Exception e){
            response = new Response<>(e.getMessage());
        }
        return response;
    }

    //----------------------Messages----------------------//new
    public Response<String> messageRejected(int orderId) {

        String message =employeeController.messageRejected( orderId);
        return new Response<>(message);
    }


    public Response<String> messageTreated(int orderId) {
        String message = employeeController.messageTreated(orderId);
        return new Response<>(message);
    }

    public Response<Boolean> checkInComingMessages(){
        Boolean checkMassages=employeeController.checkInComingMessages();
        return new Response<Boolean>(checkMassages);
    }

    public Response<String> printInComingMassages() throws Exception {
        List<String> messageList =employeeController.printInComingMessages();
        String messages="";
        for (String m :messageList){
            messages= messages+ "/n"+m;
        }
        return new Response<String>(messages);
    }
}
