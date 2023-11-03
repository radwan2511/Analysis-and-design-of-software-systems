package dataAccessLayer.identityMapperPackage;

import businessLayer.employeePackage.Bank;

import java.util.HashMap;
import java.util.Map;

public class IdentityBankMap {
    private Map<String, Bank> banks;                //String - empId

    public IdentityBankMap(){
        this.banks = new HashMap<String, Bank>();
    }

    public Map<String, Bank> getBanks(){
        return this.banks;
    }
}
