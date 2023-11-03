package dataAccessLayer.identityMapperPackage;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class IdentityEnrollmentMap {
    private Map<Date, Map<String, ArrayList<String>>> employeesInShifts;                     //<Date, <ShiftTime, [ id ]>>

    public IdentityEnrollmentMap(){
        employeesInShifts = new HashMap<Date, Map<String, ArrayList<String>>>();
    }

    public boolean checkIfDateExist(Date date){
        return employeesInShifts.containsKey(date);
    }

    public ArrayList<String> getEmployees(Date shiftDate, String shiftTime){
        ArrayList<String> list = null;
        if(employeesInShifts.get(shiftDate).containsKey(shiftTime))
            list = employeesInShifts.get(shiftDate).get(shiftTime);
        return list;
    }

    public Map<Date, Map<String, ArrayList<String>>> getEmployeesInShifts(){
        return employeesInShifts;
    }

    public boolean checkIfShiftTimeExist(Date date, String shiftTime){
        return employeesInShifts.get(date).containsKey(shiftTime);
    }

    public boolean checkIfEmpExist(Date date, String shiftTime, String empId){
        return employeesInShifts.get(date).get(shiftTime).contains(empId);
    }

    public void setEnrollment(Date shiftDate, Map<String, ArrayList<String>> stringArrayListMap) {
        employeesInShifts.put(shiftDate, stringArrayListMap);
    }
}
