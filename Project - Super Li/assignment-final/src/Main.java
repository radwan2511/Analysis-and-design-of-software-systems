import presentationLayer.MainMenu;


public class Main {
    public static void main(String[] args) throws Exception {
        MainMenu menu = MainMenu.getInstance();
        menu.loginMenu();
//        boolean finish = false;
//        while(!finish){
//            System.out.println("****************************************");
//            System.out.println("WELCOME      TO         SUPER        LEE");
//            System.out.println("****************************************");
//            System.out.println("1) EMPLOYEE MODULE");
//            System.out.println("2) DELIVERY MODULE");
//            System.out.println("3) EXIST");
//            System.out.print("Please choose an option: ");
//            Scanner scanner = new Scanner(System.in);
//            String choice = scanner.nextLine();
//            if(validInput(choice)){
//                switch(Integer.parseInt(choice)){
//                    case 1:
//                        EmpMenu menu = EmpMenu.getInstance();
//                        menu.run();
//                        break;
//                    case 2:
//                        DeliveryMenu deliveryMenu = new DeliveryMenu();
//                        deliveryMenu.run();
//                        break;
//                    case 3:
//                        System.out.println("**** See You Later :) ****");
//                        finish = true;
//                        break;
//                }
//            }
//        }
    }
}
