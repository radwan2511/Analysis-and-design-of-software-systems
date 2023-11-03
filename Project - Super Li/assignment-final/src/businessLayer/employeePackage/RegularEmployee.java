package businessLayer.employeePackage;
import java.util.ArrayList;
import java.util.Iterator;

public class RegularEmployee extends Employee {

    private int days = 6;
    private int shiftsNumber = 2;
    //    private int[][] shifts;
    private boolean isShiftManager;

    public RegularEmployee(String name, String id, boolean isShiftManager){
        super(name, id,isShiftManager);
        this.isShiftManager = isShiftManager;
    }



    public String printConstraints(ArrayList<Constraint> constraints){
        String message = "\n";
        if(constraints.size() == 0)
            return "\nThere Is No Constraints To Print\n";
        Iterator<Constraint> iterator = constraints.iterator();
        while(iterator.hasNext())
            message = message + iterator.next().print();
        return message;
    }

    public void setShiftManager(){
        this.isShiftManager = true;
    }

    public boolean getIsShiftManager(){
        return this.isShiftManager;
    }
}
