package businessLayer.employeePackage;
import java.util.Date;

public class Contract {
    private int salary;
    private Date startDate;
    private String jobTitle;
    private String jobType;

    public Contract(int salary, Date startDate, String jobTitle, String jobType){
        this.salary = salary;
        this.startDate = startDate;
        this.jobTitle = jobTitle;
        this.jobType = jobType;
    }

    public int getSalary(){
        return this.salary;
    }

    public Date getStartDate(){
        return this.startDate;
    }

    public String getJobTitle(){
        return this.jobTitle;
    }

    public String getJobType(){
        return this.jobType;
    }

    public void setSalary(int salary){
        this.salary = salary;
    }

    public void setJobType(String jobType){
        this.jobType = jobType;
    }
}
