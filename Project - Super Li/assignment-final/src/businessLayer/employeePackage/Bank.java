package businessLayer.employeePackage;

public class Bank {
    private int accountNumber;
    private int department;
    private String bankName;

    public Bank(int accountNumber, int department, String bankName){
        this.accountNumber = accountNumber;
        this.department = department;
        this.bankName = bankName;
    }
    public int getAccountNumber(){
        return this.accountNumber;
    }

    public int getDepartment(){
        return this.department;
    }

    public String getBankName(){
        return this.bankName;
    }

    public void setBankName(String bankName){
        this.bankName = bankName;
    }

    public void setAccountNumber(int accountNumber){
        this.accountNumber = accountNumber;
    }

    public void setDepartment(int department){
        this.department = department;
    }
}
