package dataAccessLayer.identityMapperPackage;

import businessLayer.employeePackage.Contract;

import java.util.HashMap;
import java.util.Map;

public class IdentityContractMap {
    private Map<String, Contract> contracts;        //String = empId

    public IdentityContractMap(){
        this.contracts = new HashMap<String, Contract>();
    }

    public Map<String, Contract> getContracts(){
        return this.contracts;
    }

    public boolean checkIfIdExist(String empId) {
        return contracts.containsKey(empId);
    }

    public String getJobTitle(String empId) {
        return contracts.get(empId).getJobTitle();
    }
}
