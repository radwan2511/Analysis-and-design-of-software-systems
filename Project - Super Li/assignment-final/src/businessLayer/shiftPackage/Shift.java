package businessLayer.shiftPackage;

import java.util.Calendar;
import java.util.Date;

public class Shift {
    private final int days = 6;
    private int day;
    private Date date;
    private String shift;   //morning or evening

    public Shift(int day, Date date, String shift){
        this.day = day;
        this.checkDate(date);
        this.date = date;
        this.shift = shift;
    }

    public String printShift(String day){
        String message = "";
        message = "Shift Details: \n" + "    Day: " + day + "\n" + "    Date: " + this.date + "\n" + "    Shift: " + this.shift + "\n";
        return message;
    }

    public String getShift(){
        return this.shift;
    }


    public int getDay(){
        return this.day;
    }

    public Date getDate(){
        return this.date;
    }

    public void checkDate(Date date){
        Date today = Calendar.getInstance().getTime();
        if(date.before(today))
            throw new IllegalArgumentException("Date passed away");
    }
}
