package businessLayer.employeePackage;

public class Constraint {
    private int day;
    private String shift;
    private String reason;

    public Constraint(int day, String shift, String reason){
        this.day = day;
        this.shift = shift;
        this.reason = reason;
    }

    public int getDay(){
        return this.day;
    }

    public String getShift(){
        return this.shift;
    }

    public String getReason(){
        return this.reason;
    }

    public void setReason(String newReason){
        this.reason = newReason;
    }

    public void setDay(int newDay){
        this.day = newDay;
    }

    public void setShift(String newShift){
        this.shift = newShift;
    }

    public String print() {
        String stringDay = convertDay(day);
        String message = "";
        message = message + "day: " + stringDay + "\n";
        message = message + "shift: " + shift + "\n";
        message = message + "reason: " + reason + "\n";
        message = message + "----------------------------------------------------------\n";
        return message;
    }

    private String convertDay(int day) {
        String result = "";
        switch (day){
            case 1:
                result = "Sunday";
                break;
            case 2:
                result = "Monday";
                break;
            case 3:
                result = "Tuesday";
                break;
            case 4:
                result = "Wednesday";
                break;
            case 5:
                result = "Thursday";
                break;
        }
        return result;
    }
}
