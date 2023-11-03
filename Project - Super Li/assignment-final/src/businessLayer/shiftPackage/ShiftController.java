package businessLayer.shiftPackage;

import dataAccessLayer.DalController;

import java.sql.SQLException;
import java.util.*;

public class ShiftController {

    private final int numOfDays = 6;
    private final int numOfShifts = 2;

    private enum Days{
        sunday,
        monday,
        tuesday,
        wednesday,
        thursday
    }
    private enum jobTitles{
        cashier,
        storeKeeper,
        shiftManager,
        general,
        driver,
        logisticsManager,
        generalManager,
        manager
    }

    private enum sTime{
        morning,
        evening
    }
    private DalController dalController;
    private Map<Date, ArrayList<Shift>> shifts;
    private Map<String, Integer>[][] shiftsStructure;
    /*
        0 - morning
        1 - evening
    */



    public ShiftController() throws Exception {
        dalController = DalController.getInstance();
        this.shifts = new HashMap<Date, ArrayList<Shift>>();
        this.shiftsStructure = new Map[6][2];
        for(int i = 0; i < this.shiftsStructure.length; i++)
            for(int j = 0; j < this.shiftsStructure[i].length; j++) {
                this.shiftsStructure[i][j] = new HashMap<String, Integer>();
                for(jobTitles job : jobTitles.values())
                    this.shiftsStructure[i][j].put(job.toString(), 1);
            }
    }

    public void createShift(int day, Date date, String shift) throws Exception {
        ArrayList<Shift> arr = this.dalController.getShiftsPerDate(date);
        if(arr != null){
            for(Shift s : arr)
                if(s.getShift().equals(shift))
                    throw new IllegalArgumentException("A shift in the same day and shift time is already created");
        }
        this.dalController.insertShift(date, shift, day);
    }

    public void updateShift(Date shiftDate, String time, String jobTitle, String id) throws Exception {
        boolean found = false;
        ArrayList<Shift> arr = this.dalController.getShiftsPerDate(shiftDate);
        if(arr == null)
            throw new IllegalArgumentException("There are no shifts with this specific date");
        for(Shift s : arr)
            if(s.getShift().equals(time)) {
                for(String job : dalController.getWorkers(shiftDate, s.getShift()).keySet())
                    if(dalController.getWorkers(shiftDate, s.getShift()).get(job).contains(id))
                        throw new IllegalArgumentException("Employee Is Already Assigned To a Job In This Shift");
                assingEmployee(s, jobTitle, id);
                found = true;
            }
        if(!found)
            throw new IllegalArgumentException("There is no shift with this specific shift time");
    }

    private void assingEmployee(Shift s, String jobTitle, String id) throws Exception {
        dalController.assignEmployee(s.getDate(), s.getShift(), id);
    }

    public void deleteShift(Date date, String shift) throws Exception {
        boolean found = false;
        ArrayList<Shift> arr = this.dalController.getShiftsPerDate(date);
        if(arr == null)
            throw new IllegalArgumentException("There are no shifts with this specific date");
        for(Shift s : arr)
            if(s.getShift().equals(shift)) {
                found = true;
                //check if shift has a delivery? cancel deleting : delete shift.
                String date2 = dalController.convertDate(date);
                if(dalController.checkIfShiftHasDelivery(date2, shift)){
                    throw new IllegalArgumentException("There Is An Upcomming Delivery in this Shift, You Can't Delete it");
                }
                else
                    this.dalController.deleteShift(s.getDate(), s.getShift());
                break;
            }
        if(!found)
            throw new IllegalArgumentException("There is no shift with this specific shift time");
    }

    public String printShift(Date date, String shift) throws Exception {
        boolean found = false;
        String message = "";
        ArrayList<Shift> arr = this.dalController.getShiftsPerDate(date);
        if(arr == null) {
            message = "There is no shift with specified date: " + date;
            throw new IllegalArgumentException(message);
        }
        else{
            for(Shift s : arr)
                if(s.getShift().equals(shift)){
                    found = true;
                    message = s.printShift(Days.values()[s.getDay() - 1].toString());
                    message = message + "Employees: ";
                    Map<String, ArrayList<String>> employees = new HashMap<String, ArrayList<String>>();
                    for(String id : dalController.getEmployeesInShift(date, s.getShift())){
                        if(employees.containsKey(dalController.getContract(id).getJobTitle()))
                            employees.get(dalController.getContract(id).getJobTitle()).add(id);
                        else{
                            ArrayList<String> emp = new ArrayList<String>();
                            emp.add(id);
                            employees.put(dalController.getContract(id).getJobTitle(), emp);
                        }
                    }
                    message = message + employees.toString();
                }
            if(!found) {
                message = "There is no shift with specified shift time: " + shift;
                throw new IllegalArgumentException(message);
            }
        }
        return message;
    }

    public void checkNeededJobTitleForDelivery(String jobTitle, Date date, String shiftTime) throws Exception {
        if(jobTitle.equals("driver") || jobTitle.equals("storeKeeper")){
            if(dalController.checkIfShiftHasDelivery(dalController.convertDate(date), shiftTime))
                throw new IllegalArgumentException("Employee has an Upcoming Delivery, You Can't Replace it Yet.");
        }
    }

    public void deleteEmployeeFromShift(String id, String jobTitle, Date date, String shiftTime) throws Exception {
        boolean foundShift = false;
        boolean foundWorker = false;
        String message = "";
        ArrayList<Shift> arr = this.dalController.getShiftsPerDate(date);
        if(arr == null)
            throw new IllegalArgumentException("There are no shifts with this specific date");
        for(Shift s : arr)
            if(s.getShift().equals(shiftTime)) {
                foundShift = true;
                if(dalController.getWorkers(date, s.getShift()).get(jobTitle).contains(id)) {
                    checkNeededJobTitleForDelivery(jobTitle, date, shiftTime);
                    dalController.deleteEmpFromShift(date, s.getShift(), id);
                    foundWorker = true;
                }
            }
        if(!foundShift)
            throw new IllegalArgumentException("There is no shift with this specific shift time");
        if(!foundWorker)
            message = "Worker: " + id + " is not assigned to this shift time";
        throw new IllegalArgumentException(message);
    }

    public String printEmployeeShiftsForWeek(String id) throws Exception {
        String message = "Shifts:\n";
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
            message = message + printShiftForEmployee(today,id);
        }
        message = message + "\n-------------------------------------------------------------------------\n";
        return message;
    }
    public String printEmployeeShiftsForMonth(String id) throws Exception {
        String message = "Shifts:\n";
        Date today = null;
        Calendar calendar;
        for(int i = 0; i < 31; i++){
            calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            today = calendar.getTime();
            calendar.setTime(today);
            calendar.add(Calendar.DATE, i);
            today = calendar.getTime();
            message = message + printShiftForEmployee(today,id);
        }
        message = message + "\n-------------------------------------------------------------------------\n";
        return message;
    }

    private String printShiftForEmployee(Date date, String id) throws Exception { //+2 point ;)
        ArrayList<Shift> arr = this.dalController.getShiftsPerDate(date);
        String message = "" + date + "\n";
        int i = 1;
        boolean found = false;
        if(arr != null){
            for(Shift s : arr)
                for(String job : dalController.getWorkers(date, s.getShift()).keySet())
                    if(dalController.getWorkers(date, s.getShift()).get(job).contains(id)){
                        found = true;
                        message = message + i + ")\n" + s.printShift(Days.values()[s.getDay() - 1].toString()) + "\n";
                        message = message + "**********************\n";
                        i++;
                    }
            if(!found) {
                message = message + "There is no shifts to show\n";
                message = message + "*****************************************************************\n";
            }

        }
        else {
            message = message + "There is no shifts to show\n";
            message = message + "*****************************************************************\n";
        }
        return message;
    }

    public void updateShiftStructure(String jobTitle, int amount, int day, String shiftTime) throws SQLException { //shiftTime == 0? 'morning' : 'evening'
        int time = 0;
        for(sTime s : sTime.values())
            if(s.toString().equals(shiftTime))
                break;
            else
                time++;
        this.dalController.updateNumOfEmp(day, time, jobTitle, amount);
    }

    public String printShiftsStructure(){
        String message = "";
        for(int i = 1; i < numOfDays; i++)
            for(int j = 0; j < numOfShifts; j++){
                message = message + "Day: " + Days.values()[i-1] + "\nShift: " + sTime.values()[j] + "\n";
                message = message + "   "+this.dalController.printDailyShiftStructure(i, j)+"\n" ;
                message = message + "-------------------------------------------------------------------------\n";
            }
        return message;
    }

    public String printIncompleteShifts() throws Exception {
        String message = "Incomplete Shifts:\n";
        Date today = null;
        Calendar calendar;
        for(int i = 0; i < 7; i++) {
            calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            today = calendar.getTime();
            calendar.setTime(today);
            calendar.add(Calendar.DATE, i);
            today = calendar.getTime();
            message = message + today.toString() + ":\n";
            message = message + checkIncompleteShift(today);
            message = message + "-------------------------------------------------------------------------\n";
        }
        return message;
    }

    private String checkIncompleteShift(Date date) throws Exception {
        String message = "";
        int entered = 0;
        ArrayList<Shift> shifts = this.dalController.getShiftsPerDate(date);
//        ArrayList<Shift> shifts = this.shifts.get(date);
        int time = 0;
        if(shifts != null){
            if(shifts.size() == 0){
                message = "  There is no shifts\n";
            }
            else{
                for(Shift s : shifts) {
                    entered = 0;
                    message = message + "   Shift Time: " + s.getShift() + "\n";
                    if (s.getShift().equals("evening")) time = 1;
                    for (String jobTitle : dalController.getJobTitles(s.getDay(), time)) {
                        if(dalController.getWorkers(date, s.getShift()).get(jobTitle) != null) {
                            if (dalController.getWorkers(date, s.getShift()).get(jobTitle).size() <  dalController.getNumOfEmpInJobTitle(s.getDay(), time, jobTitle)){
                                entered++;
                                int lack = dalController.getNumOfEmpInJobTitle(s.getDay(), time, jobTitle) - dalController.getWorkers(date, s.getShift()).get(jobTitle).size();
                                message = message + "      job title: " + jobTitle + "\n";
                                message = message + "      Lack: " + lack + " workers\n";
                                message = message + "\n";
                            }
                        }
                        else{
                            entered++;
                            message = message + "      job title: " + jobTitle + "\n";
                            message = message + "      Lack: " +  dalController.getNumOfEmpInJobTitle(s.getDay(), time, jobTitle) + " workers\n";
                            message = message + "\n";
                        }
                    }
                    if(entered == 0)
                        message = message + "      There Is No Lack Of Employees\n";
                }
            }
        }
        else
            message = "  There is no shifts\n";
        return message;
    }


    public Boolean checkIncompleteShifts() throws Exception {
        boolean isIncomplete = false;
        Date today = null;
        Calendar calendar;
        for(int i = 0; i < 7; i++) {
            calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            today = calendar.getTime();
            calendar.setTime(today);
            calendar.add(Calendar.DATE, i);
            today = calendar.getTime();
            isIncomplete = checkIncomplete(today);
            if(isIncomplete)
                break;
        }
        return isIncomplete;
    }

    private boolean checkIncomplete(Date date) throws Exception {
        boolean isIncomplete = false;
        ArrayList<Shift> shifts = this.dalController.getShiftsPerDate(date);
        int time = 0;
        if(shifts != null){
            for(Shift s : shifts) {
                if (s.getShift().equals("evening")) time = 1;
                for (String jobTitle : dalController.getJobTitles(s.getDay(), time)) {
                    if(dalController.getWorkers(date, s.getShift()).get(jobTitle) != null){
                        if (dalController.getWorkers(date, s.getShift()).get(jobTitle).size() <  dalController.getNumOfEmpInJobTitle(s.getDay(), time, jobTitle)){
                            isIncomplete = true;
                            break;
                        }
                    }
                    else {
                        isIncomplete = true;
                        break;
                    }
                }
            }
        }
        return isIncomplete;
    }

    public ArrayList<String> filterEmployees(ArrayList<String> employees, int day, int shiftTime, String jobTitle) throws Exception {
        Date today = Calendar.getInstance().getTime();
        Calendar calendar;
        int currentDay = today.getDay();
        currentDay++;
        int toAdd = day - currentDay;
        calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.add(Calendar.DATE, toAdd);
        today = calendar.getTime();
        calendar.setTime(today);
        today = calendar.getTime();
        ArrayList<Shift> shifts = this.dalController.getShiftsPerDate(today);
        if(shifts != null){
            Shift shift = shifts.get(shiftTime);
            if (shift != null){
                if(dalController.getWorkers(today, shift.getShift()) != null){
                    if(dalController.getWorkers(today, shift.getShift()).containsKey(jobTitle)){
                        for(String id : dalController.getWorkers(today, shift.getShift()).get(jobTitle)){
                            employees.remove(id);
                        }
                    }
                }

            }
        }
        return employees;
    }

    public String getWorkingDays() {
        int index = 1;
        String message = "";
        for(Days day : Days.values()){
            message = message + index + ") " + day.toString() + "\n";
            index++;
        }
        message = message + "\n";
        return message;
    }

    public String getShifTime() {
        int index = 1;
        String message = "";
        for(sTime time : sTime.values()){
            message = message + index + ") " + time.toString() + "\n";
            index++;
        }
        message = message + "\n";
        return message;
    }

    public String getUserShiftTimeOption(int choice) {
        if(sTime.values().length > choice)
            return sTime.values()[choice].toString();
        return "error";
    }

    public int getUserDayOption(int choice) {
        int result = 0;
        switch (choice){
            case 1:
                result = 1;
                break;
            case 2:
                result = 2;
                break;
            case 3:
                result = 3;
                break;
            case 4:
                result = 4;
                break;
            case 5:
                result = 5;
                break;
            default:
                result = -1;
                break;
        }
        return result;
    }

    public void checkWeekend(int day) {
        if(day > Days.values().length)
            throw new IllegalArgumentException("You Can't Create Shifts on Weekends");
    }

    public void deleteEmployeeFromShifts(String id, String jobTitle) throws Exception {
        Date today = null;
        Calendar calendar;
        calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        today = calendar.getTime();
        for(Date date : dalController.getDates()){
            if(date.after(today)){
                if(dalController.getShiftsPerDate(date).size() > 0){
                    for(Shift s : dalController.getShiftsPerDate(date)){
                       if( dalController.getWorkers(today, s.getShift()).get(jobTitle) != null)
                           dalController.getWorkers(today, s.getShift()).get(jobTitle).remove(id);
                    }
                }
            }
        }
    }

    public Map<Date, ArrayList<Shift>> getShifts(){
        return this.shifts;
    }

    public String getWeeklyShiftsReport() throws Exception {
        String message = "Shifts:\n";
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
            message = message + printShift(today, "morning");
            message = message + printShift(today, "evening");
        }
        message = message + "\n-------------------------------------------------------------------------\n";
        return message;
    }

    public String getMonthlyShiftsReport() throws Exception {
        String message = "Shifts:\n";
        Date today = null;
        Calendar calendar;
        for(int i = 0; i < 31; i++){
            calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            today = calendar.getTime();
            calendar.setTime(today);
            calendar.add(Calendar.DATE, i);
            today = calendar.getTime();
            message = message + printShift(today, "morning");
            message = message + printShift(today, "evening");
        }
        message = message + "\n-------------------------------------------------------------------------\n";
        return message;
    }
}

