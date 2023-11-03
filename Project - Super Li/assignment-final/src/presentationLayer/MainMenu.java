package presentationLayer;
import java.util.Scanner;

public class MainMenu {
    private static  MainMenu instance = null;
    private Scanner scanner;
    private EmpMenu empMenu;
    private DeliveryMenu deliveryMenu;
    private  int numOfOptions = 2;
//    private SupplierMenu supplierMenu; *
//    private StoreMenu storeMenu; *

    private MainMenu() throws Exception {
        scanner = new Scanner(System.in);
        empMenu = EmpMenu.getInstance();
        deliveryMenu = new DeliveryMenu();
//        supplierMenu = new SupplierMenu(); *
//        storeMenu = new StoreMenu(); *
    }

    public static MainMenu getInstance() throws Exception{
        if(instance == null){
            instance = new MainMenu();
        }
        return instance;
    }

    private boolean validMenuInput(String input){
        try {
            int x = Integer.parseInt(input);
            if(x > 0 && x <= numOfOptions)
                return true;
            else
                return false;
        }catch (Exception e){
            return false;
        }
    }

    private void login() throws Exception {
        System.out.print("Please enter your id, supplier's phone number or -1 to exit\nID: ");
         String input = scanner.nextLine();
         if(!input.equals("-1")){
             while(!validId(input)){
                 System.out.println("Please Enter a valid Id Number.");
                 System.out.print("Please enter your id, supplier's phone number or -1 to exit\nID: ");
                 input = scanner.nextLine();
             }
             identification(input);
         }
    }

    private boolean validId(String input) {
        boolean result = false;
        try{
            if(input.length() == 9 || input.length() == 10){
                int id = Integer.parseInt(input);
                if(id > 0)
                    result = true;
            }
            return result;
        }catch (Exception e){
            return result;
        }
    }

    /*cashier,
        storeKeeper,
        shiftManager,
        general,
        driver,
        logisticsManager,
        generalManager,
        manager*/

    private void identification(String input) throws Exception {
        String jobTitle = empMenu.getIdentity(input);
        switch(jobTitle){
            case "manager":
                empMenu.run(input, jobTitle);
                break;
            case "shiftManager":
                empMenu.run(input, jobTitle);
                break;
            case "cashier":
                empMenu.run(input, jobTitle);
                break;
            case "general":
                empMenu.run(input, jobTitle);
                break;
            case "driver":
                deliveryMenu.run("driver");
                break;
            case "logisticsManager":
                deliveryMenu.run("logisticsManager");
            case "generalManager":
                empMenu.run(input, jobTitle);
            case "storeKeeper":
                //call store menu *
                //send jobTitle *
                break;
                ///*ConstantDaysSupplier, JustOrderSupplier, CollectingSupplier*/
            case "ConstantDaysSupplier": // *
                //call supplier menu
                //send jobTitle
                break;
            case "JustOrderSupplier": // *
                //call supplier menu
                //send jobTitle
                break;
            case "CollectingSupplier": // *
                //call supplier menu
                //send jobTitle
                break;
            default:
                if(jobTitle.equals("none")) {
                    System.out.println("ID Does Not Exist.");
                }
                else
                    System.out.println(jobTitle);
                break;
        }
    }

    public void loginMenu() throws Exception {
        boolean exit = false;
        Scanner loginScanner = new Scanner(System.in);
        while(!exit){
            System.out.println("****************************************");
            System.out.println("WELCOME      TO         SUPER        LEE");
            System.out.println("****************************************");
            System.out.println("1) Login");
            System.out.println("2) Exit");
            System.out.print("Please choose an option: ");
            String option = loginScanner.nextLine();
            if(validMenuInput(option)){
                switch(Integer.parseInt(option)){
                    case 1:
                        login();
                        break;
                    case 2:
                        System.out.println("**** See You Later :) ****");
                        exit = true;
                        break;
                }
            }
            else {
                System.out.println("Please Enter a Valid Number.");
            }
        }
    }
}


/*
    Opened Issues:
        1. EMPLOYEE CONTROLLER - GET IDENTITY
        2. EmpMenu GENERAL MANAGER MENU
        3. MainMenu 10, 11, 17, 18
        4. EMPLOYEE CONTROLLER - delete storeKeeper
        5. SHIFT CONTROLLER - delete shift
        6. SHIFT CONTROLLER - deleteEmployeeFromShift
        7. getUpcomingShifts - enrollmentMapper
        9. supplierIdentity - dalController
        10. update GeneralManager menu - two more options
        11. add Supplier's types
*/