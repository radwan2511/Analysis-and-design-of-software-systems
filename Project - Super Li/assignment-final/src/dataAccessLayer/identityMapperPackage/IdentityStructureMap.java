package dataAccessLayer.identityMapperPackage;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class IdentityStructureMap {
    private Map<String, Integer>[][] shiftsStructure;
    private int days = 6;     //sunday(1) - thursday(5)
    private int shifts = 2;  //morning(0) or evening(1)
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

    public IdentityStructureMap(){
        this.shiftsStructure = new Map[days][shifts];
        for(int i = 0; i < this.shiftsStructure.length; i++)
            for(int j = 0; j < this.shiftsStructure[i].length; j++) {
                this.shiftsStructure[i][j] = new HashMap<String, Integer>();
                for(jobTitles job : jobTitles.values())
                    this.shiftsStructure[i][j].put(job.toString(), 1);
            }
    }

    public void setNumOfEmp(int day, int shiftTime, String jobTitle, int numEmp){
        shiftsStructure[day][shiftTime].replace(jobTitle, numEmp);
    }

    public void loadStrucutre(int day, int shiftTime, String jobTitle, int numOfEmp){
        shiftsStructure[day][shiftTime].put(jobTitle, numOfEmp);
    }

    public String printDailyShiftStructure(int day, int shiftTime) {
        return shiftsStructure[day][shiftTime].toString();
    }

    public Set<String> getJobTitles(int day, int time) {
        return shiftsStructure[day][time].keySet();
    }

    public int getNumOfEmpInJobTitle(int day, int time, String jobTitle) {
        return shiftsStructure[day][time].get(jobTitle);
    }

}
