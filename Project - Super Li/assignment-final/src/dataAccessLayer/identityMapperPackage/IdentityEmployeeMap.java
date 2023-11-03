package dataAccessLayer.identityMapperPackage;

import businessLayer.employeePackage.Employee;

import java.util.HashMap;
import java.util.Map;

public class IdentityEmployeeMap {

    private Map<String, Employee> employees;                //String - empId

    public IdentityEmployeeMap(){
        this.employees = new HashMap<String, Employee>();
    }

    public Map<String, Employee> getEmployees(){
        return this.employees;
    }
}