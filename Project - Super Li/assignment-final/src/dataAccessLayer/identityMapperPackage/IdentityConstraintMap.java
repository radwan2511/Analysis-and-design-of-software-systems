package dataAccessLayer.identityMapperPackage;

import businessLayer.employeePackage.Constraint;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class IdentityConstraintMap {
    private Map<String, Map<Integer, Map<String, String>>> constraints;       //<empId, <day, <shiftTime, reason> > >

    public IdentityConstraintMap(){
        constraints = new HashMap<String, Map<Integer, Map<String, String>>>();
    }

    public boolean checkIfEmpExist(String empId) {
        return constraints.containsKey(empId);
    }

    public boolean checkIfConstraintExist(String empId, int day, String shiftTime){
        if(constraints.containsKey(empId))
            if(constraints.get(empId).containsKey(day))
                if(constraints.get(empId).get(day).containsKey(shiftTime))
                    return true;
        return false;
    }

    public ArrayList<Constraint> getEmpConstraints(String empId) {
        ArrayList<Constraint> result = new ArrayList<Constraint>();
        for(Integer day : constraints.get(empId).keySet())
            for(String shiftTime : constraints.get(empId).get(day).keySet())
                result.add(new Constraint(day, shiftTime, constraints.get(empId).get(day).get(shiftTime)));
        return result;
    }

    public Constraint getConstraint(String empId, int day, String shiftTime) {
        String reason = constraints.get(empId).get(day).get(shiftTime);
        return new Constraint(day, shiftTime, reason);
    }

    public Map<String, Map<Integer, Map<String, String>>> getConstraints(){
        return constraints;
    }

    public void addConstraint(String empId, int day, String shiftTime, String reason) {
        if(constraints.get(empId).containsKey(day))
            constraints.get(empId).get(day).put(shiftTime, reason);
        else{
            Map<String, String> shiftTime_reason = new HashMap<String, String>();
            shiftTime_reason.put(shiftTime, reason);
            constraints.get(empId).put(day, shiftTime_reason);
        }
    }
}
