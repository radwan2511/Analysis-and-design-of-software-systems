package businessLayer.employeePackage;
import dataAccessLayer.DalController;

import java.text.SimpleDateFormat;
import java.util.*;

public class EmployeeController {
    private DalController dalController;
    private String loggedIn;           //Id

    private enum constraints {
        medicalIssue,
        religion,
        university,
        familyEvent
    }

    private enum jobTitles {
        cashier,
        storeKeeper,
        shiftManager,
        general,
        driver,
        logisticsManager,
        generalManager,
        manager
    }

    public EmployeeController() throws Exception {
        dalController = DalController.getInstance();
        this.loggedIn = "";
    }


    public boolean login(String id) throws Exception {
        if (dalController.getEmployee(id) != null) {
            loggedIn = id;
            if (dalController.getEmployee(id) instanceof Manager){
                return true;
            }
        } else
            throw new IllegalArgumentException("Id is not found");
        return false;
    }

    public void logout() {
        loggedIn = "";
    }

    public String printPersonalData() throws Exception{
        String message =  dalController.printPersonalData(loggedIn);
        return message;
    }

    public String printConstraints() throws Exception {
        if(dalController.getEmployee(loggedIn) instanceof RegularEmployee)
            return ((RegularEmployee) dalController.getEmployee(loggedIn)).printConstraints(dalController.getConstraints(loggedIn));
        return "";
    }

    public void addConstraint(int day, String shift, String reason) throws Exception {
        Date today = Calendar.getInstance().getTime();
        if (today.getDay() > 4)  //(0 = Sunday, 1 = Monday, 2 = Tuesday, 3 = Wednesday, 4 = Thursday, 5 = Friday, 6 = Saturday)
            throw new IllegalArgumentException("You don't have permission to add constraints in weekends");
        if(dalController.getEmployee(loggedIn) instanceof RegularEmployee){
            dalController.addConstraint(loggedIn,day,shift,reason);
        }
    }

    public void deleteConstraint(int day, String shift) throws Exception {
        dalController.deleteConstraint(loggedIn,day,shift);
    }

    public void deleteRegularConstraint(String empId, int day, String shift) throws Exception {
        dalController.deleteConstraint(empId,day,shift);
    }

    public void updateConstraintReason(int day, String shift, String newReason) throws Exception {
        dalController.updateConstraintReason(loggedIn,day,shift,newReason);
    }

    public void updateConstraintDay(int oldDay, String oldShift, int newDay, String newShift) throws Exception {
        dalController.updateConstraintDay(loggedIn,oldDay,oldShift,newDay,newShift);
    }

    public void createEmployee(String name, String id, int accountNumber, int department, String bankName, int salary, Date startDate,
                               String jobTitle, String jobType, boolean isShiftManager) throws Exception {
        boolean isManager= false;
        if(jobTitle.equals(jobTitles.manager.toString())){
            isManager=true;
        }
        dalController.createEmployee(id,name,isShiftManager,isManager);
        dalController.createBank(id,accountNumber,department,bankName);
        dalController.createContract(id,jobType,jobTitle,salary,startDate);
    }

    public void upgradeToShiftManager(String id) throws Exception {
        dalController.upgradeToShiftManager(id);
    }

    public ArrayList<String> suggestEmployees(String jobTitle, int day, int shiftTime) throws Exception {
        ArrayList<String> workers = new ArrayList<String>();
        ArrayList<String> AllEmployees= dalController.getEmployees();
        ArrayList<Constraint> constraints;
        for(String key :AllEmployees){
            if(dalController.getContract(key).getJobTitle().equals(jobTitle)){
                constraints= dalController.getConstraints(key);
                for (Constraint constraint:constraints) {
                    if(constraint.getDay()==day&& constraint.getShift().equals(shiftTime))
                        break;

                }
                workers.add(key);
            }
        }
        return workers;
    }

    public String getEmployeesData(ArrayList<String> workers) throws Exception {
        ArrayList<String> AllEmployees= dalController.getEmployees();
        String message = "";
        int index = 1;
        for(String id : AllEmployees)
            if(workers.contains(id)){
                message = message + index + ")\n";
                message = message + "Name: " + dalController.getEmployee(id).getName() + "\n";
                message = message + "Id: " + dalController.getEmployee(id).getId() + "\n";
                message = message + "Job title: " + dalController.getContract(id).getJobTitle() + "\n";
                message = message + "Job type: " + dalController.getContract(id).getJobType();
                message = message + "\n";
            }
        message = message + "-------------------------------------------------------------------------\n";
        return message;
    }

    public String getConstraints() {
        int index = 1;
        String message = "";
        for(constraints constraint : constraints.values()){
            message = message + index + ") " + constraint.toString() + "\n";
            index++;
        }
        message = message + "\n";
        return message;
    }

    public void checkShiftAppointment(String jobTitle, String id, Date date, String shiftTime) throws Exception {
        if(dalController.getEmployee(id) == null)
            throw new IllegalArgumentException("Employee Does Not Exist");
        if(dalController.getEmployee(id) instanceof RegularEmployee) {
            if (jobTitle.equals(jobTitles.shiftManager.toString())){
                if (!((RegularEmployee) dalController.getEmployee(id)).getIsShiftManager()){
                    throw new IllegalArgumentException("Employee Is Not Competent For This Job Title");
                }
            }
            else{
                if(!( dalController.getContract(id).getJobTitle().equals(jobTitle)))
                    throw new IllegalArgumentException("Employee Is Not Competent For This Job Title");
            }
        }else
        if(!(jobTitle.equals(jobTitles.manager.toString()))) {
            throw new IllegalArgumentException("Employee Is Not Competent For This Job Title");
        }
    }

    public String getUserConstrainOption(int choice) {
        if(constraints.values().length > choice)
            return constraints.values()[choice].toString();
        return "error";
    }

    public String getJobTitles() {
        int index = 1;
        String message = "Job Titles:\n";
        for(jobTitles jobTitle : jobTitles.values()){
            message = message + index + ") " + jobTitle.toString() + "\n";
            index++;
        }
        message = message + "\n";
        return message;
    }

    public String getUserJobTitleOption(int choice) {
        if(jobTitles.values().length > choice)
            return jobTitles.values()[choice].toString();
        return "error";
    }

    public String getPossibleEmployees(Date shiftDate, String shiftTime, String jobTitle) throws Exception {
        ArrayList<String> AllEmployees= dalController.getEmployees();
        String message = "";
        int day = shiftDate.getDay();
        day++;
        int time = 0;
        if(shiftTime.equals("evening"))
            time = 1;
        message = message + "Possible Workers:\n";
        for(String id : AllEmployees)
            if(dalController.getContract(id).getJobTitle().equals(jobTitle)) {
                message = message + "Name: " + dalController.getEmployee(id).getName() + "   " + "Id: " + id;
                if(dalController.getEmployee(id) instanceof  RegularEmployee){
                    if(dalController.getConstraint(id,day,shiftTime)!=null)
                        message = message + "  *Has a " + dalController.getConstraint(id,day,shiftTime).getReason() + " Constraint*";
                }
                message = message + "\n";
            }
        if(jobTitle.equals("shiftManager"))
            for(String id : AllEmployees)
                if(dalController.getEmployee(id) instanceof RegularEmployee)
                    if(((RegularEmployee) dalController.getEmployee(id)).getIsShiftManager())
                        if(!dalController.getContract(id).getJobTitle().equals(jobTitle)) {
                            message = message + "Name: " + dalController.getEmployee(id).getName() + "   " + "Id: " + id;
                            if(dalController.getConstraint(id,day,shiftTime)!=null)
                                message = message + "  *Has a Constraint*";
                            message = message + "\n";
                        }
        return message;
    }

    public void updateEmployeeBankData(String empId, int newAccountNumber, int newDepartment, String newBankName) throws Exception {
        dalController.updateBankData(empId,newAccountNumber,newDepartment,newBankName);
    }

    public void updateEmployeeContractData(String empId, int salary, String jobType) throws Exception {
        dalController.updateContractData(empId,salary,jobType);
    }

    public String deleteEmployee(String id) throws Exception {
        checkIfEmpIsDriver(id);
        checkIfEmpIsStoreKeeper(id);
        ArrayList<Constraint> constraints= dalController.getConstraints(id);
        String jobTitle = "";
        if(dalController.getContract(id)!=null){
            jobTitle= dalController.getContract(id).getJobTitle();
        }
        for(Constraint constraint:constraints){
            dalController.deleteConstraint(id,constraint.getDay(),constraint.getShift());
        }
        dalController.deleteContract(id);
        dalController.deleteBank(id);
        dalController.deleteEmployee(id);
        Map<String, ArrayList<String>> upcomingShifts = dalController.getUpcomingShifts(id);
        Date date = null;
        for(String shiftDate : upcomingShifts.keySet()){
            for(String shiftTime : upcomingShifts.get(shiftDate)) {
                SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                date = format.parse(shiftDate);
                dalController.deleteEmpFromShift(date, shiftTime, id);
            }
        }
        return jobTitle;
    }

    private void checkIfEmpIsStoreKeeper(String id) throws Exception {
        String jobTitle = dalController.getContract(id).getJobTitle();
        if(jobTitle.equals("storeKeeper")){
            Map<String, ArrayList<String>> upcomingShifts = dalController.getUpcomingShifts(id);
            for(String shiftDate : upcomingShifts.keySet()){
                for(String shiftTime : upcomingShifts.get(shiftDate)){
                    if(dalController.checkIfShiftHasDelivery(shiftDate, shiftTime))
                        throw new Exception("Store Keeper Has An Upcoming Delivery, You Can't Delete it until that");
                }
            }
        }
    }

    private void checkIfEmpIsDriver(String id) throws Exception {
        String jobTitle = dalController.getContract(id).getJobTitle();
        if(jobTitle.equals("driver")){
            if(dalController.checkUpcomingShifts(id))
                throw new Exception("Driver Has An Upcoming Delivery, You Can't Delete it until that");
        }
    }

    public boolean checkIfEmployeeHasConstraint(Date shiftDate, String shiftTime, String empId) throws Exception {
        int day = shiftDate.getDay();
        day++;
        if(dalController.getConstraint(empId,day,shiftTime) != null){
            return true;
        }
        return false;
    }

    public void addDriverLicense(String empId, String name, String license) throws Exception{
        dalController.addDriverLicense(empId, name, license);
    }

    public String getIdentity(String id) throws Exception {
        String jobTitle = "none";
        if(dalController.getContract(id) != null){
            jobTitle = dalController.getContract(id).getJobTitle();
        }
        else if(!dalController.getSupplier(id).equals("")){
            jobTitle = dalController.getSupplier(id);
        }
        return jobTitle;
    }

    public String getAllEmpIds() throws Exception {
        String message = "";
        ArrayList<String> ids = dalController.getEmployees();
        int index = 1;
        for(String id : ids){
            message = message + index + ")\n";
            dalController.printPersonalData(id);
            message = message + "--------------------------------------------------------\n";
            index++;
        }
        return message;
    }

    //----------------------Messages---------------------//new
    public String messageRejected(int orderId) {
        String message;
        try{
            dalController.deleteMessages(orderId);
            dalController.addMessage("storeKeeper","Rejected",orderId);
            message="massege sent successfully";
        }catch (Exception e){
            message="Message can't be send , Wrong order ID";

        }
        return message;
    }

    public String messageTreated(int orderId) {
        String message;
        try{
            dalController.deleteMessages(orderId);
            dalController.addMessage("storeKeeper","Treated",orderId);
            message="massege sent successfully";
        }catch (Exception e){
            message="Message can't be send , Wrong order ID";

        }
        return message;
    }

    public Boolean checkInComingMessages() {
        try{
            List<String> messages= dalController.getMessages("manager");
            return true;
        }catch (Exception e){
            return false;
        }
    }

    public List<String>  printInComingMessages() throws Exception {
        return dalController.getMessages("manager");
    }
}
