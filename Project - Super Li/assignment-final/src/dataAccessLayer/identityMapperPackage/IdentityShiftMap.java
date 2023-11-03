package dataAccessLayer.identityMapperPackage;

import businessLayer.shiftPackage.Shift;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class IdentityShiftMap {
    private Map<Date, ArrayList<Shift>> shifts;

    public IdentityShiftMap(){
        shifts = new HashMap<Date, ArrayList<Shift>>();
    }

    public Map<Date, ArrayList<Shift>> getShifts(){
        return shifts;
    }

    public boolean checkIfExist(Date date){
        return shifts.containsKey(date);
    }

    public Shift getShift(Date date, String shiftTime){
        if(shifts.get(date).size() != 0){
            for(Shift s : shifts.get(date))
                if(s.getShift().equals(shiftTime))
                    return s;
        }
        return null;
    }

    public ArrayList<Shift> getShiftsPerDate(Date shiftDate) {
        return shifts.get(shiftDate);
    }

    public void setShiftsPerDate(Date shiftDate, ArrayList<Shift> shifts) {
        this.shifts.put(shiftDate, shifts);
    }
}
