package presentationLayer;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Scanner;

public class Handler {
    private Scanner scanner;
    public Handler(Scanner scanner) {
        this.scanner = scanner;
    };

    public String inputHandler1(String input) {
        switch (input){
            case "name":
                System.out.print("Please Enter Employee's Name: ");
                break;
            case "id":
                System.out.print("Please Enter Employee's id: ");
                break;
            case "bankName":
                System.out.print("Please Enter Employee's Bank Name: ");
                break;
            case "jobType":
                System.out.print("Please Enter Employee's Job Type: ");
                break;
        }
        return scanner.nextLine();
    }

    public int inputHandler2(String input) {
        Integer result = null;
        String stringInput = "";
        while(result == null){
            try{
                switch (input){
                    case "accountNumber":
                        System.out.print("Please Enter Employee's Account Number: ");
                        break;
                    case "department":
                        System.out.print("\nPlease Enter Bank's Department Number: ");
                        break;
                    case "salary":
                        System.out.print("\nPlease Enter Employee's Salary: ");
                        break;
                    case "amount":
                        System.out.print("\nPlease Enter new amount of employees: ");
                        break;
                }
                stringInput = scanner.nextLine();
                result = Integer.parseInt(stringInput);
                if(result < 0){
                    System.out.print("Please Enter Positive Numbers Only\n");
                    result = null;
                }
            }catch (Exception e){
                System.out.print("Please Enter Positive Numbers Only\n");
            }
        }
        return result;
    }

    public boolean inputHandler3(String input) {
        switch (input){
            case "isShiftManager":
                System.out.print("\nIs The Employee Qualified To Be Shift Manager? yes/no\n Your Choice: ");
                break;
        }
        boolean done = false;
        boolean result  = false;
        while(!done){
            input = scanner.nextLine();
            if(input.toLowerCase(Locale.ROOT).equals("yes")) {
                result = true;
                done = true;
            }
            else if(input.toLowerCase(Locale.ROOT).equals("no"))
                done =  true;
            else
                System.out.print("Please Enter Yes or No\n Your Choice: ");
        }
        return  result;
    }

    public Date dateHandler(boolean before) throws ParseException {
        Date date = null;
        String input = "";
        while(date == null){
            System.out.print("Enter Date - dd/MM/yyyy: ");
            input = scanner.nextLine();
            date = checkDate(input, before);
        }
        return date;
    }

    private Date checkDate(String dateFormat, boolean before) throws ParseException {
        Date date = null;
        if(dateFormat.length() != 10)
            return date;
        if(dateFormat.charAt(2) != '/' || dateFormat.charAt(5) != '/')
            return date;
        if(dateFormat.charAt(0) == '0' && dateFormat.charAt(1) == '0')
            return date;
        if(dateFormat.charAt(3) == '0' && dateFormat.charAt(4) == '0')
            return date;
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        date = format.parse(dateFormat);
        Date today = Calendar.getInstance().getTime();
        if(before && date.before(today))
            date = null;
        return date;
    }
}
