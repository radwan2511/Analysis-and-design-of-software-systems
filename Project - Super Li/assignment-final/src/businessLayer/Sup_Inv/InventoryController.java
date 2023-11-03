package businessLayer.Sup_Inv;

import dataAccessLayer.DalController;
import dataAccessLayer.Sup_inv_dal.DBHandler;
import dataAccessLayer.Sup_inv_dal.DTO.SpecificProductDTO;

import java.sql.SQLException;
import java.util.*;

public class InventoryController {

    private DBHandler db;
    private static InventoryController instance=null;
    //private ProductController productController;

    public InventoryController() {
        //productController = ProductController.getInstance();
    }
    public static InventoryController getInstance()  {
        if(instance==null){
            instance=new InventoryController();
            try{
                instance.db = DalController.getInstance().getDbHandler();
            } catch (Exception e) {}

        }
        return instance;
    }
    public DBHandler getDBHandler(){
        return db;
    }


/////////////////////////////////////////////////////////
    ///////////////////////////////////////

    public List<Category> printAllCategories() throws Exception {
        return CategoryController.getInstance().getAllCategories();
    }
    /*public static void printAllCategories() {
        List<Category> categories=CategoryController.getAllCategories();
        System.out.println("Categories:");
        for(Category cat:categories) {
            System.out.println(cat);
            System.out.println();
        }
        System.out.println("---------------------------------");
    }*/
    public Category searchCategory(String cat) throws Exception {
        return getCategoryByIdOrName(cat);
    }

    private Category getCategoryByIdOrName(String categoryNameOrId) throws Exception {
        Category cat=null;
        try{
            int sub_id=Integer.parseInt(categoryNameOrId);
            cat=CategoryController.getInstance().getCategoryById(sub_id);
            if(cat==null){
                System.out.println("Category id not found!");
                return null;
            }

        }catch(Exception e){
            cat=CategoryController.getInstance().getCategoryByName(categoryNameOrId);
            if(cat==null){
                System.out.println("Category name not found!");
                return null;
            }
        }
        return cat;
    }

    public Map<Integer,Category> insertCategoryMenu(String name, String subStr) throws Exception {
        Category sub=null;
        if(!subStr.equals("0")){
            try{
                int sub_id=Integer.parseInt(subStr);
                sub=CategoryController.getInstance().getCategoryById(sub_id);
            }catch(Exception e){
                sub=CategoryController.getInstance().getCategoryByName(subStr);
            }
        }
        int id=CategoryController.getInstance().insertCategory(new Category(name, sub));
        if (id == -1) return new HashMap<>();
        Map<Integer,Category> res = new HashMap<>();
        res.put(id,sub);
        return res;
    }


    public String report(String catString, int rep) throws Exception {
        List<Category> list;
        if(catString.equals("overall")){
            list=CategoryController.getInstance().getAllCategories();
        }
        else{
            List<String> namesList=new LinkedList<>();
            String[] names=catString.split(",");
            for(String ss:names)
                namesList.add(ss);
            list=CategoryController.getInstance().getCategoriesbyNamesOrIds(namesList);
        }
        if(rep == 1)
            return ReportController.getInstance().getReport(ReportController.getInstance().createInventoryReport(list));
    //        ReportController.printReport(System.out, ReportController.createInventoryReport(list));
        if(rep == 2)
           return ReportController.getInstance().getReport(ReportController.getInstance().createShortagesReport(list));
           // ReportController.printReport(System.out, ReportController.createShortagesReport(list));
        if(rep == 3)
            return ReportController.getInstance().getReport(ReportController.getInstance().createFlawsReport(list));
        //    ReportController.printReport(System.out, ReportController.createFlawsReport(list));
        return "";
    }

    public boolean insertProducts(List<SpecificProduct> lst) throws Exception {
        return ProductController.getInstance().addQuantity(lst);
    }


    /////////////////////////////////////////////////////////
    ///////////////////////////////////////


    /////////////////////////////////////////////////////////////
    public void printSales() throws Exception {
        String sales = ProductController.getInstance().printSales();
        if(sales.equals(""))
            System.out.println("No Sales");
        else
            System.out.println(sales);
    }
    public void addSaleByCategory(String cat, String date, String percentage) throws Exception {
        Category c=getCategoryByIdOrName(cat);
        if (c == null){
            throw new IllegalArgumentException("the category is not exist");
        }
        boolean suc = ProductController.getInstance().addSale(1,date, percentage, c.getId());
        if (!suc){
            throw new IllegalArgumentException("");
        }
    }

    public void addSaleBySpecificOrder(String id, String date, String percentage) throws Exception {
        SpecificProduct sp = getSpecificProduct(Integer.parseInt(id));
        if(sp==null){
            throw new IllegalArgumentException("Specific Product not found.");
        }
        ProductController.getInstance().addSale(2,date, percentage, sp.getId());
    }

//
    ////////////////////////////////////////////////////////////////////////

    public void nowFlaw(int id) throws Exception {
       ProductController.getInstance().makeFlaw(id);
    }
    /*private static void findCategory(Scanner s) {
        System.out.println();
        System.out.println("category id/name:");
        String cat=s.next();
        Category c=getCategoryByIdOrName(cat);
        if(c==null) return;
        System.out.println(c);
    }*/
    public ArrayList<Order> chekMinQuantites() throws Exception {
        return ProductController.getInstance().checkMinQuantities();
    }

    public void addGeneralProduct(String cat, int code, String name, double selling_price, String manufactor, int minQuantity, double weight) throws Exception {
        Category category=getCategoryByIdOrName(cat);
        if(category==null)
            throw new IllegalArgumentException("the category is not exist");
        if (checkProductCode(code)){
            throw new IllegalArgumentException("the product is already exist in the system");
        }
        ProductController.getInstance().addGeneralProduct(code,name,selling_price,manufactor,minQuantity,category, weight);
        /*Map<Integer,Integer> code_quantity = new HashMap<>();
        code_quantity.put(code,minQuantity);
        LocalDateTime now = LocalDateTime.now();
        OrderController.getInstance().addComplexOrder(code_quantity,now.getYear(),now.getMonthValue(),now.getDayOfWeek().getValue() + 1);*/
    }
    /*public static void addGeneralProduct(Scanner s) {

        boolean isAdded = ProductController.addGeneralProduct(new GeneralProduct(cost_price, selling_price, name, code, minQuantity, 0, manufactor, category));
		if(isAdded)
            System.out.println("general product successfully added!");
        else
            System.out.println("general product adding failed!");
    }*/
    private boolean checkProductCode(int code) throws Exception {
        if(ProductController.getInstance().getGeneralProductByCode(code)==null)
            return false;
       // System.out.println("Code found!");
        return true;
    }
//    private static Category findCategoryByIdOrName(String id_name){
//        Category cat=null;
//        try{
//            int supCategory_id=Integer.parseInt(id_name);
//            cat= CategoryController.getCategoryById(supCategory_id);
//            if(cat==null){
//                System.out.println("Category id not found!");
//                return null;
//            }
//
//        }catch(Exception e){
//            cat=CategoryController.getCategoryByName(id_name);
//            if(cat==null){
//                System.out.println("Category name not found!");
//                return null;
//            }
//        }
//        return cat;
//    }
//    private static void addCategory(Scanner s) {
//        System.out.println();
//        System.out.println("enter name:");
//        String name=s.next();
//        System.out.println("enter id/name of superior category: (enter -1 for no category)");
//        String sup=s.next();
//        Category supCat=null;
//        if(!sup.equals("-1")){
//            try{
//                int supCat_id=Integer.parseInt(sup);
//                supCat=CategoryController.getCategoryById(supCat_id);
//                if(supCat==null){
//                    System.out.println("invalid category id!");
//                    return;
//                }
//
//            }catch(Exception e){
//                supCat=CategoryController.getCategoryByName(sup);
//                if(supCat==null){
//                    System.out.println("main category name not found!");
//                    return;
//                }
//            }
//        }
//        int id=CategoryController.insertCategory(new Category(name, supCat));
//        if(id==-1)
//            System.out.println("category adding failed!");
//        else
//            System.out.println("Category Added successfully, id = "+id);
//    }
//
    private SpecificProduct getSpecificProduct(int id) throws Exception {
        return ProductController.getInstance().getSpecificProductById(id);
    }

    public void addSpecificProducts(int code, String date) throws Exception {
        GeneralProduct p=ProductController.getInstance().getGeneralProductByCode(code);
        if (p == null){
            throw new IllegalArgumentException("Code not found");
        }
        SpecificProductDTO sp = ProductController.getInstance().getSpecificProductByCode(code);
        if (sp!=null){
            throw new IllegalArgumentException("the specific product is already existed");
        }
        SpecificProduct product=new SpecificProduct( SpecificProduct.LOCATION_STORAGE, false, date, p);
        if(!getInstance().insertProduct(product)){
            throw new IllegalArgumentException("Product adding failed!");
        }
    }

 //   public static void addSpecificProducts(Scanner s) {
        /*System.out.println();
        System.out.println("Enter product code:");
        int code=s.nextInt();
        GeneralProduct p=ProductController.getGeneralProductByCode(code);
        if(p==null){
            System.out.println("Code not found");
            return;
        }*/
        /*System.out.println("product expire at date: ( format (dd/mm/yyyy) )");
        String date=s.next();*/
        /*SpecificProduct product=new SpecificProduct( SpecificProduct.LOCATION_STORAGE, false, date, p);
        boolean isSucceeded=getInstance().insertProduct(product);
        if(isSucceeded)
            System.out.println("Product added successfully!");
        else
            System.out.println("Product adding failed!");*/
 //   }

    public boolean insertProduct(SpecificProduct product) throws Exception {
        List<SpecificProduct> lst=new LinkedList<>();
        lst.add(product);
        return ProductController.getInstance().addQuantity(lst);
    }



}

