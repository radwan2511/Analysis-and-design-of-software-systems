package businessLayer.employeePackage;
import java.util.Date;

public class Manager extends Employee {

    public Manager(String name, String id, boolean isShiftManager){
        super(name, id,isShiftManager);
    }

    public RegularEmployee createEmployee(String name, String id, int accountNumber, int department, String bankName, int salary, Date startDate,
                                          String jobTitle, String jobType, boolean isShiftManager){
        Bank bank = new Bank(accountNumber, department, bankName);
        Contract contract = new Contract(salary, startDate, jobTitle, jobType);
        RegularEmployee newEmployee = new RegularEmployee(name, id, isShiftManager);
        return newEmployee;
    }

}
