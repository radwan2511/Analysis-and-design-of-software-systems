package presentationLayer.Sup_Inv_PresentationLayer;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class MenuController {
    private static PresentationController presentationController;
    private Scanner sc;
    private boolean started;
    private static MenuController menuController = null;

    private MenuController() throws Exception {
        presentationController = new PresentationController();
        sc = new Scanner(System.in);
        started = false;
    }

    public static MenuController getInstance() throws Exception {
        if(menuController != null)
            return menuController;
        return new MenuController();
    }

    public void InventoryMenu() throws Exception {
        int userAction = 0;
        while(userAction!=8) {
            System.out.println("Welcome to Inventory Menu, choose an option: ");
            presentationController.checkMinQuantities();
            System.out.println("1 - Manage Category");
            System.out.println("2 - report");
            System.out.println("3 - add general product");
            System.out.println("4 - add specific product");
            System.out.println("5 - add sale");
            System.out.println("6 - update flaw");
            System.out.println("7 - show all sales");
            System.out.println("8 - Close Inventory Menu");
            // cancel order
            // add order
            userAction = inputOption();
            if (userAction == 1)
                manageCategory();
            if (userAction == 2)
                issueReportMenu();
            if (userAction == 3)
                addGeneralProduct();
            if (userAction == 4)
                addSpecificProducts();
            if (userAction == 5)
                addSale();
            if (userAction == 6)
                nowFlaw();
            if (userAction == 7)
                printSales();
        }
        System.out.println("Closing Inventory Menu...\n--------------\n");
    }

    public void SupplierMenu(){
        int userAction = 0;
        while(userAction!=4){
            System.out.println("Welcome to Supplier Menu, choose an option: ");
            //ConstantOrder();
            presentationController.checkMinQuantities();
            System.out.println("1 - supplier actions");
            System.out.println("2 - contract actions");
            System.out.println("3 - order actions");
            System.out.println("4 - Close Supplier Menu");
            userAction = inputOption();
            if(userAction == 1)
                supplierActions();
            if(userAction == 2)
                contractAction();
            if(userAction == 3)
                orderAction();
        }
        System.out.println("Closing Supplier Menu...\n-------------\n");

    }
    public void mainMenu() throws Exception {
        int option = 0;
        while(option!=3) {
            System.out.println("Welcome, Choose A Menu: ");
            System.out.println("1 - Inventory Menu");
            System.out.println("2 - Supplier Menu");
            System.out.println("3 - Close the program");
            option = inputOption();
            if (option == 1) {
                InventoryMenu();
            }
            if (option == 2) {
                SupplierMenu();
            }
        }
        System.out.println("Closing ...");
    }

    public void SupplierType3or2Menu(){
        int option = 0;
        while (option!=2) {
            System.out.println("1 - Show My Orders");
            System.out.println("2 - close Menu");
            option = inputOption();
            if (option == 1){
                SupplierOrder();
            }
        }
    }

    public void SupplierType1Menu(){
        int option = 0;
        while (option!=3) {
            System.out.println("1 - Show My Orders");
            System.out.println("2 - Set My Work Days");
            System.out.println("2 - close Menu");
            option = inputOption();
            if (option == 1){
                SupplierOrder();
            }
            if (option ==2){
                SetWorkDays();
            }
        }
    }

    private void ConstantOrder(){
        Map<Integer,Integer> code_quantity = new HashMap<>();
        code_quantity.put(444,100);
        code_quantity.put(555,150);
        presentationController.AddConstantOrder(1,code_quantity);
    }



    private void printSales() throws Exception {
        presentationController.printSales();
    }

    private void nowFlaw() {
        System.out.println();
        System.out.println("Please enter specific product id:");
        int id=inputOption();
        presentationController.nowFlaw(id);
    }

    private void addSale(){
        System.out.println("choose sale by: 1-Category 2-Specific Product");
        String sale_type = sc.next();
        if(sale_type.equals("1")){
            System.out.println("Please Enter category id or name:");
            String cat=sc.next();
            System.out.println("Please Enter sale date:");
            String date=sc.next();
            System.out.println("Please Enter sale percentage:");
            String percentage=sc.next();
            presentationController.addSaleByCategory(cat,date,percentage);
        }
        else if(sale_type.equals("2")){
            System.out.println("Please Enter Specific Product id:");
            String id = sc.next();
            System.out.println("Please Enter sale date:");
            String date=sc.next();
            System.out.println("Please Enter sale percentage:");
            String percentage=sc.next();
            presentationController.addSaleBySpecificOrder(id,date,percentage);
        }
        else{
            System.out.println("Wrong input. choose 1 - category or 2 - specific product");
        }

    }

    private void addSpecificProducts() {
        System.out.println();
        System.out.println("Enter product code:");
        int code=sc.nextInt();
        System.out.println("product expire at date: ( format (dd/mm/yyyy) )");
        String date=sc.next();
        presentationController.addSpecificProducts(code,date);
    }

    /*private void addGeneralProduct(Scanner s) {
        presentationController.addGeneralProduct(s);
    }*/

    public void addGeneralProduct() {
        System.out.println();
        System.out.print("category id or name: ");
        String cat = sc.next();
        System.out.print("code: ");
        int code=inputOption();
        System.out.print("name: ");
        String name=sc.next();
        //System.out.println("cost price:");
        //double cost_price=inputDouble();
        System.out.print("selling price: ");
        double selling_price=inputDouble();
        System.out.print("manufactor name: ");
        String manufactor=sc.next();
        System.out.print("min quantity: ");
        int minQuantity=sc.nextInt();
        System.out.print("weight: ");
        double weight = inputDouble();
        presentationController.addGeneralProduct(cat,code,name,selling_price,manufactor,minQuantity, weight);
    }

    private void manageCategory() {
        System.out.println("Please Choose:");
        System.out.println("1 - view categories");
        System.out.println("2 - search category");
        System.out.println("3 - insert category");
        int choice=inputOption();
        switch (choice){
            case 1:
                printAllCategories();
                break;
            case 2:
                searchCategory();
                break;
            case 3:
                insertCategoryMenu();
                break;
            default:
                System.out.println("Wrong Choice.");
                break;
        }
    }


    private void printAllCategories() {
        presentationController.printAllCategories();
    }
    /*private static void searchCategory(Scanner s) {
        presentationController.searchCategory(s);
    }*/

    private void searchCategory(){
            System.out.println();
            System.out.println("Please Enter category id or name:");
            String cat=sc.next();
            presentationController.searchCategory(cat);
    }
    /*private static void insertCategoryMenu(Scanner s) {
        presentationController.insertCategoryMenu(s);
    }*/
    private void insertCategoryMenu() {
        System.out.println();
        System.out.println("please insert name:");
        String name=sc.next();
        System.out.println("Please insert sub category id or name (0 for no sub):");
        String subStr=sc.next();
        presentationController.insertCategoryMenu(name,subStr);
    }


    private void issueReportMenu() {
        System.out.println();
        System.out.println("enter categories for report:");
        System.out.println("to get overall report enter: overall, else enter categories separated by comma ( , )");
        String catString=sc.next();
        System.out.println("Report by:");
        System.out.println("1 - Inventory");
        System.out.println("2 - Shortages");
        System.out.println("3 - flaws");
        int rep=sc.nextInt();
        presentationController.issueReportMenu(catString,rep);
    }


    public void supplierActions() {
        while (true) {
            System.out.println("Supplier Actions:");
            System.out.println("Please Select Action:");
            System.out.println("1 - add supplier ");
            System.out.println("2 - delete supplier");
            System.out.println("3 - Set Work Days");
            System.out.println("4 - Show All Suppliers");
            System.out.println("5 - exit ");

            int op = inputOption();

            switch (op) {
                case 1: {
                    AddSupplier();
                    break;
                }

                case 2: {
                    DeleteSupplier();
                    break;
                }
                case 3: {
                    SetWorkDays();
                    break;
                }
                case 4:{
                    presentationController.printSuppliers();
                    break;
                }
                case 5:{
                    return;
                }
                default: {
                    System.out.println("Selection Unrecognized");
                    break;
                }
            }
        }
    }

    private void contractAction() {
        while (true) {
            System.out.println("Contract Actions:");
            System.out.println("Please Select Action:");
            System.out.println("1 - ADD Contract");
            System.out.println("2 - DELETE Contract");
            System.out.println("3 - Show Contracts ");
            System.out.println("4 - Exit ");
            int op = inputOption();

            switch (op) {
                case 1: {
                    MakeContract();
                    break;
                }
                case 2: {
                    cancelContract();
                    break;
                }
                case 3: {
                    presentationController.viewProductsContract();
                    break;
                }
                case 4: {
                    return;
                }
                default: {
                    System.out.println("Selection Unrecognized");
                    break;
                }

            }
        }
    }

    private void ShowOrderMenu(){
        System.out.println("Choose An Option: ");
        System.out.println("1 - Show Orders for a day ");
        System.out.println("2 - Show Orders for a supplier");
        System.out.println("3 - Show All Orders");
        int option = inputOption();
        if (option == 1){
            DayOrder();
        }
        if (option == 2){
            SupplierOrder();
        }
        if (option == 3){
            presentationController.printOrder();
        }
    }

    public void orderAction()  {
        while (true) {
            System.out.println("Order Actions:");
            System.out.println("Please Select Action:");
            System.out.println("1 - show Order Menu");
            System.out.println("2 - Add Order");
            System.out.println("3 - Cancel Order");
            System.out.println("4 - Change The Order Status"); // change
            System.out.println("5 - Handle Arrived Deliveries");
            System.out.println("6 - Exit ");
            Scanner io = new Scanner(System.in);
            int op = io.nextInt();
            switch (op) {
                case 1: {
                    ShowOrderMenu();
                    break;
                }
                case 2: {
                    addOrderMenu();
                    break;
                }
                case 3: {
                    cancelOrder();
                    break;
                }
                case 4:{
                    ChangeStatus();
                    break;
                }
                case 5:{
                    HandleArrivedOrders();
                    break;
                }
                case 6:{
                    return;
                }
                default: {
                    System.out.println("Selection Unrecognized");
                    break;
                }
            }
        }
    }

    private void HandleArrivedOrders() {
        presentationController.HandleArrivedOrders();
    }


    /////////////////////////////////////////////////////////
    private void AddSupplier(){
        System.out.print("enter a supplier name:");
        String supplierName = sc.nextLine();
        System.out.print("enter a supplier address:");
        String supplierAddress = sc.nextLine();
        System.out.print("enter a supplier phone number - 10 digits:");
        String supplierPhoneNum = sc.nextLine();
        while(true){
            if (supplierPhoneNum.length()!=10 ){
                System.out.println("invalid number, the phone number must have 10 digits!");
                System.out.print("phone number: ");
                supplierPhoneNum = sc.nextLine();
            }else{
                break;
            }
        }
        System.out.println("enter a supplier work method number:\n1 - constant day supplier\n" +
                "2 - supplier that comes when there is only an order\n" +
                "3 - super-lee order collecting supplier ");
        int type = inputOption();
        while(type< 1 || type > 3){
            System.out.println("error input, the type should be 1 or 2 or 3");
            type = inputOption();
        }
        System.out.print("enter a supplier bank number:");
        String supplierBnNumber = sc.nextLine();
        System.out.print("enter a supplier bank account:");
        String supplierBankAccount = sc.nextLine();
        System.out.println("enter a supplier company number: ");
        int companyNumber = inputOption();
        presentationController.AddSupplier(supplierName,supplierAddress,supplierPhoneNum,type,supplierBnNumber + ", " + supplierBankAccount,companyNumber);
    }

    private void DeleteSupplier(){
        System.out.print("enter supplier id to remove: ");
        int id = inputOption();
        presentationController.DeleteSupplier(id);
    }

    private void SetWorkDays(){
        System.out.print("enter a supplier id: ");
        int id = inputOption();
        System.out.println();
        System.out.print("enter number of days to work: ");
        int num = inputOption();
        System.out.println();
        int[] days = new int[num];
        System.out.println("enter the days of work as a number: ");
        for(int i = 0;i<num;i++){
            days[i] = inputOption();
        }
        presentationController.setWorkDays(id,days);
    }

    /*private void addCompany(){
        System.out.print("enter a supplier address: ");
        String address = sc.nextLine();
        System.out.println();
        System.out.print("enter a company name: ");
        String comp = sc.nextLine();
        System.out.println();
        presentationController.AddCompany(address,comp);
    }*/

    private void MakeContract(){
        System.out.print("enter a supplier id: ");
        int id = inputOption();
        System.out.print("enter a contract number: ");
        int contractNum = inputOption();
        System.out.print("is the supplying days constant? answer yes or no: ");
        boolean constant = "yes".equals(sc.nextLine());
        System.out.print("enter number of products in the contract: ");
        int num_of_products = inputOption();
        Map<Integer, Map<Integer,Integer>> QuantityDiscountPerProduct = new HashMap<>();
        Map<Integer,Double> price = new HashMap<>();
        System.out.println("enter: the products codes that are in the contract.\n" +
                "for each product enter:\n" +
                "1 - product price\n2- quantity and the discount for the given quantity.\n" +
                "note: if there is no discount for the specific product, press 0 next to quantity.\n" +
                "note: if there is no more discounts, press -1 next to quantity: ");
        for(int i = 0;i<num_of_products;i++){
            Map<Integer,Integer> discounts_quantity = new HashMap<>();
            System.out.print("product code: ");
            int product_code = inputOption();
            System.out.print("product price: ");
            price.put(product_code,inputDouble());
            System.out.print("quantity: ");
            int quantity = inputOption();
            if (quantity == 0){
                QuantityDiscountPerProduct.put(product_code,discounts_quantity);
                continue;
            }
            else {
                while (quantity!=-1) {
                    System.out.println();
                    System.out.print("discount: ");
                    int discount = inputOption();
                    System.out.println();
                    discounts_quantity.put(quantity,discount);
                    System.out.print("quantity: ");
                    quantity = inputOption();
                }
                QuantityDiscountPerProduct.put(product_code,discounts_quantity);
            }
        }
        presentationController.MakeContract(id,contractNum,constant,QuantityDiscountPerProduct,price);
    }

    private void cancelContract(){
        System.out.print("enter supplier id: ");
        int id = inputOption();
        presentationController.DeleteContract(id);
    }

    /*private  void AddOrder()  {
        System.out.println("Add Order:");
        System.out.println("Insert Date year,month,day (\\n for each)");
        int year=inputOption();
        int month=inputOption();
        int day=inputOption();
        System.out.println("Insert Product Code:");
        int code=inputOption();
        System.out.println("Insert quantity");
        int quantity=inputOption();
        presentationController.addOrder(year,month,day,code,quantity);
    }*/

    public void addComplexOrder(){
        Map<Integer,Integer> code_quantity = new HashMap<>();
        System.out.println("Add Complex Order:");
        System.out.println("Insert Date year,month,day");
        System.out.print("year: ");
        int year=inputOption();
        System.out.print("month: ");
        int month=inputOption();
        System.out.print("day: ");
        int day=inputOption();
        System.out.print("Insert number of products in the order: ");
        int num = inputOption();
        System.out.println("Insert the products codes, And for Each one insert the ordered quantity: ");
        for (int i = 0; i<num;i++){
            System.out.print("Insert Product Code: ");
            int code=inputOption();
            System.out.print("Insert quantity: ");
            int quantity=inputOption();
            code_quantity.put(code,quantity);
        }
        presentationController.addComplexOrder(code_quantity,year,month,day);
    }

    private void ChangeStatus(){
        System.out.print("enter delivery ID of the Order: ");
        int id = inputOption();
        presentationController.ChangeStatus(id);
    }

    private void addOrder(){
        System.out.print("enter order id: ");
        int id= inputOption();
        presentationController.addOrder(id);
    }

    private void addOrderMenu(){
        System.out.println("choose an Option: ");
        System.out.println("1- add order by products");
        System.out.println("2- add order by order id");
        int option = inputOption();
        while (true) {
            if (option == 1) {
                addComplexOrder();
                break;
            }
            if (option == 2) {
                addOrder();
                break;
            }
            else{
                System.out.println("Wrong input, please choose 1 or 2");
            }
            option = inputOption();
        }
    }

    //private void finishOrder(){
        /*System.out.print("enter the supplier id: ");
        int supid = inputOption();
        System.out.print("enter product code: ");
        int product_code = inputOption();*/
        /*System.out.println("enter order date:");
        System.out.print("year: ");
        int year = inputOption();
        System.out.print("month: ");
        int month = inputOption();
        System.out.print("day: ");
        int day = inputOption();
        System.out.println();
        presentationController.ChangeStatus(year,month,day);
    }*/

    /*private void cancelOrder(){
        /*System.out.print("enter supplier id: ");
        int supid = inputOption();
        System.out.print("enter product code: ");
        int prodict_code = inputOption();*/
        /*System.out.println("enter order date to be canceled: ");
        System.out.print("year: ");
        int year = inputOption();
        System.out.print("month: ");
        int month = inputOption();
        System.out.print("day: ");
        int day = inputOption();
        System.out.println();
        presentationController.CancelOrder(year,month,day);
    }*/

    public void cancelOrder(){
        /*System.out.print("enter supplier id: ");
        int supid = inputOption();
        System.out.print("enter product code: ");
        int prodict_code = inputOption();*/
        System.out.println("enter order ID to be canceled: ");
        int id = inputOption();
        System.out.println();
        presentationController.CancelOrder(id);
    }

    private  void DayOrder(){
        System.out.println("Show Orders From Date:");
        System.out.println("Insert Date year:");
        int year =inputOption();
        System.out.println("Insert Date month:");
        int month =inputOption();
        System.out.println("Insert Date day:");
        int day =inputOption();
        presentationController.DayOrder(year,month,day);

    }

    private void SupplierOrder(){
        System.out.println("Show Orders From Supplier:");
        System.out.println("Insert Supplier ID:");
        int id =inputOption();
        presentationController.SupplierOrder(id);
    }

    /*private void addContact(){
        System.out.print("enter supplier id: ");
        int id = inputOption();
        System.out.println("enter the contact details:");
        System.out.print("name: ");
        String name = sc.nextLine();
        System.out.print("phone number: ");
        String phoneNumber = sc.nextLine();
        System.out.print("address: ");
        String address = sc.nextLine();
       // presentationController.AddContact(id,name,phoneNumber,address);
    }*/

    private int inputOption(){
        try{
            int option = sc.nextInt();
            sc.nextLine();
            return option;
        }catch (Exception e){
            System.out.println("error input, you should enter a number");
            sc.nextLine();
            return inputOption();
        }
    }

    private double inputDouble(){
        try{
            double number = sc.nextDouble();
            sc.nextLine();
            return number;
        }catch (Exception e){
            System.out.println("error input, you should enter a number");
            sc.nextLine();
            return inputOption();
        }
    }

    //------------------------------Show Data------------------------------------
//    private void chooseMenu() throws SQLException {
//        System.out.println("*if you want to show more, press 1." +
//                "if you want to exit and back to main menu press 2");
//        int option = inputOption();
//        while(option!= 1 && option != 2){
//            System.out.println("wrong input, please press 1 or 2");
//            option = inputOption();
//        }
//        if (option == 1){
//            System.out.println("choose an option: ");
//            showData();
//        }
//        else{
//            mainMenu();
//        }
//    }

//    private void showData() throws SQLException {
//        String[] menu = {
//                "Show a supplier data",
//                "Show all suppliers data",
//                "Show the contract details with the supplier",
//                "Show the finished orders with the supplier",
//                "Show order data of supplier",
//                "exit 'show data menu'"
//        };
//        for (int i = 0 ;i<menu.length;i++){
//            System.out.println((i+1) + ") " + menu[i]);
//        }
//        int option = inputOption();
//        while (option<1 || option>menu.length ){
//            System.out.println("wrong input option , you should choose a number between 1 - 6");
//            option = inputOption();
//        }
//        switch (option){
//            case 1:
//                showSupplier();
//                chooseMenu();
//                break;
//            case 2:
//                showAllSuppliers();
//                chooseMenu();
//                break;
//            case 3:
//                showContractDetails();
//                chooseMenu();
//                break;
//            case 4:
//                showFinishedOrders();
//                chooseMenu();
//                break;
//            case 5:
//                showOrder();
//                chooseMenu();
//                break;
//            case 6:
//                System.out.println("*if you want to close the program, press 1." +
//                        "if you want to exit and back to main menu press 2");
//                int op = inputOption();
//                while(op!= 1 && op != 2){
//                    System.out.println("wrong input, please press 1 or 2");
//                    op = inputOption();
//                }
//                if (op == 1){
//                    break;
//                }
//                else{
//                    mainMenu();
//                }
//                break;
//        }
//    }

    private void showSupplier(){
        System.out.print("enter a id address: ");
        int id = inputOption();
        presentationController.GetSupplier(id);
    }

    private void showAllSuppliers(){
        presentationController.GetAllSuppliers();
    }

    private void showContractDetails(){
        System.out.print("enter a supplier id: ");
        int id = inputOption();
        presentationController.getContract(id);
    }



//    private void showFinishedOrders(){
//        System.out.print("enter a supplier id: ");
//        int id = inputOption();
//        presentationController.getFinishedOrders(id);
//    }
//
//    private void showOrder(){
//        System.out.print("enter a supplier id: ");
//        int supid = inputOption();
//        System.out.print("enter order id: ");
//        int id = inputOption();
//        presentationController.showOrder(supid,id);
//    }

    /*private void initializeData(){
        ArrayList<String> companies1 = new ArrayList<>();
        companies1.add("tnova");
        companies1.add("tara");
        ArrayList<String> companies2 = new ArrayList<>();
        companies2.add("coka cola");
        ArrayList<String> companies3 = new ArrayList<>();
        companies3.add("eliet");
        companies3.add("coka cola");
        presentationController.AddSupplier("Ross","Ross@Gmail.com", "0523456789", 1, "1111", "doar", companies1);
        presentationController.AddSupplier("joe","joe@Gmail.com", "0522222222", 2, "13", "boalem", companies2);
        presentationController.AddSupplier("james","james@Gmail.com", "0555555555", 3, "807", "leumit", companies3);

        presentationController.AddContact("joe@Gmail.com", "chandler", "0503456789", "chandler@Gmail.com");

        presentationController.AddProduct("milk",8, 0.250);
        presentationController.AddProduct("shoko", 12, 0.250);

        Map<Integer,Integer> Quantity_Discount = new HashMap<>();
        Quantity_Discount.put(100, 10);
        Map<Integer,Map<Integer,Integer>> Product_Quantity_Discount = new HashMap<>();
        Product_Quantity_Discount.put(1,Quantity_Discount);
        Product_Quantity_Discount.put(2, new HashMap<>());
        presentationController.MakeContract("Ross@Gmail.com", "1", true,Product_Quantity_Discount);

        int [] product_ids = new int[2];
        product_ids[0]= 1;
        product_ids[1]= 2;
        Map<Integer,Integer> Quantity = new HashMap<>();
        Quantity.put(1,150);
        Quantity.put(2,1000);
        presentationController.AddOrder("Ross@Gmail.com", product_ids, new Date(2022-1900,3,20), Quantity);

        int [] days = new int[2];
        days[0] = 2;
        days[1] = 3;
        presentationController.setWorkDays("Ross@Gmail.com", days);

    }

    private void ShowInitializedData(){
        System.out.println("you choose to start with initialized data, here is the data:");
        System.out.println("\n1. Products:");
        System.out.println("-Product 1: \n" +
                "\tid: 1\n" +
                "\tname: milk\n" +
                "\tprice: 8\n" +
                "\tweight: 0.250");
        System.out.println("-Product 2: \n" +
                "\tid: 2\n" +
                "\tname: shoko\n" +
                "\tprice: 12\n" +
                "\tweight: 0.250");
        System.out.println("\n2. Suppliers:");
        presentationController.GetAllSuppliers();
        System.out.println("\n3. Contracts:");
        presentationController.getContract("Ross@Gmail.com");
        System.out.println("\njoe and james didn't assign a contract yet");
        System.out.println("\n4. Orders:");
        presentationController.showOrder("Ross@Gmail.com", 1);
    }*/

}
