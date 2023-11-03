package businessLayer.Sup_Inv;

import dataAccessLayer.Sup_inv_dal.DBHandler;
import dataAccessLayer.Sup_inv_dal.DTO.SupplierDTO;
import dataAccessLayer.Sup_inv_dal.SupplierDalController;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SupplierController {
    private Map<Integer,Supplier> suppliers_id;
    private SupplierDalController sdc ;
    private DBHandler dbHandler;


    private static SupplierController instance = null ;

    private SupplierController() throws Exception {
        suppliers_id = new HashMap<>();
        sdc = new SupplierDalController();
        dbHandler = InventoryController.getInstance().getDBHandler();
    }


    public static SupplierController getInstance() throws Exception {
        if(instance == null){
            instance = new SupplierController();
        }
        return instance;
    }

    /*public Supplier getSupplierByPhoneNumber(String number){
        SupplierDTO sdto = dbHandler.getSupplierByPhoneNumber(number);
        if (sdto == null){
            return null;
        }
        else{
            return convertSupplierDTO_toSupplierB(sdto);
        }
    }*/

    public Map<Integer, Supplier> getSuppliers_id() {
        return suppliers_id;
    }

    public Supplier AddSupplier(String name , String address, String PhoneNumber, int type, String bank, int companyNum) throws Exception {
        if(address == null){
            throw new IllegalArgumentException("address cannot be null");
        }
        if(address.length() == 0){
            throw new IllegalArgumentException("address cannot be empty");
        }
        if(name.length() == 0){
            throw new IllegalArgumentException("name cannot be empty");
        }
        if(PhoneNumber == null || PhoneNumber.length() == 0){
            throw new IllegalArgumentException("phone Number cannot be null or empty");
        }
        if(type < 1 || type > 3){
            throw new IllegalArgumentException("not illegal supplier type");
        }
        SupplierDTO supplierDTO = dbHandler.getSupplierByPhoneNumber(PhoneNumber);
        if (supplierDTO!=null){
            if (suppliers_id.containsKey(supplierDTO.id)){
                throw new IllegalArgumentException("the supplier is existed in the system already");
            }else{
                suppliers_id.put(supplierDTO.id,convertSupplierDTO_toSupplierB(supplierDTO));
                throw new IllegalArgumentException("the supplier is existed in the system already");
            }
        }
        Supplier s = new Supplier(dbHandler.getSupplier_id(),name,address,PhoneNumber,type,bank, companyNum);
        int supplier_id = dbHandler.getSupplier_id();
        this.suppliers_id.put(supplier_id,s);
        // dal add
        dbHandler.insertSupplier(convertSupplierB_toSupplierDTO(s));
        //sdc.insert(convertSupplierB_toSupplierDTO(s));
        return s;
    }
    public Supplier AddSupplierTest(String name , String address, String PhoneNumber, int type, String bank, int companyNum){
        if(address == null){
            throw new IllegalArgumentException("address cannot be null");
        }
        if(address.length() == 0){
            throw new IllegalArgumentException("address cannot be empty");
        }
        if(name.length() == 0){
            throw new IllegalArgumentException("name cannot be empty");
        }
        if(PhoneNumber == null || PhoneNumber.length() == 0){
            throw new IllegalArgumentException("phone Number cannot be null or empty");
        }
        if(type < 1 || type > 3){
            throw new IllegalArgumentException("not illegal supplier type");
        }
        Supplier s = new Supplier(dbHandler.getSupplier_id(),name,address,PhoneNumber,type,bank, companyNum);
        int supplier_id = dbHandler.getSupplier_id();
        this.suppliers_id.put(supplier_id,s);
        return s;
    }

    public boolean DeleteSupplier(int supplier_id) throws Exception {
        if(suppliers_id.containsKey(supplier_id)){
           // Supplier s = suppliers_id.get(supplier_id);
          //  s.setContract_id(-1);
            suppliers_id.remove(supplier_id);
            dbHandler.deleteSupplier(supplier_id);
            return true;
        }else {
            SupplierDTO s = dbHandler.getSupplier(supplier_id);
            if (s == null){
                throw new IllegalArgumentException("the supplier is not exist in the system");
            }else{
                dbHandler.deleteSupplier(supplier_id);
                return true;
            }
        }
    }


//    public void AddContact(int supplier_id,String name,String phoneNumber,String address){
//        /*if(supplier_id < 1){
//            throw new IllegalArgumentException("the supplier with id : "+ supplier_id +" is not exist");
//        }*/
//        if(name == null){
//            throw new IllegalArgumentException("the name cannot be null");
//        }
//        if(phoneNumber == null){
//            throw new IllegalArgumentException("Phone Number cannot be null");
//        }
//        if(address == null){
//            throw new IllegalArgumentException("address cannot be null");
//        }
//        if (!(suppliers_id.containsKey(supplier_id))) {
//            throw new IllegalArgumentException("this supplier is not exist in the system ");
//        }
//        Supplier s = suppliers_id.get(supplier_id);
//        Contact c = new Contact(contact_id,name,phoneNumber,address);
//        s.setContact_id(contact_id);
//        // dal update c
//        sdc.update1(contact_id,supplier_id);
//        contact_id ++;
//    }

    public void setWorkDays(int supplier_id, int [] days) throws Exception {
        if(supplier_id < 1){
            throw new IllegalArgumentException("the supplier with id : "+ supplier_id +" is not exist");
        }
        if(days == null){
            throw new IllegalArgumentException("the constant days cannot be null");
        }
        for(int i=0;i<days.length;i++){
            if (days[i] < 1 || days[i] > 7){
                throw new IllegalArgumentException("the constant day is not illegal");
            }
        }
        if(!suppliers_id.containsKey(supplier_id)){
            SupplierDTO s = dbHandler.getSupplier(supplier_id);
            if (s == null) {
                throw new IllegalArgumentException("this supplier is not exist in the system ");
            }
            else{
                Supplier sup = convertSupplierDTO_toSupplierB(s);
                suppliers_id.put(supplier_id,sup);
                String workDays = sup.setWorkDays(days);
                dbHandler.UpdateWorkDays(supplier_id,workDays);
            }
        }
        else{
            Supplier s = suppliers_id.get(supplier_id);
            String workDays = s.setWorkDays(days);
            dbHandler.UpdateWorkDays(supplier_id,workDays);
        }
    }


    /*public void AddProduct(String name, double price,double weight){
        if(name == null){
            throw new IllegalArgumentException("the name cannot be null");
        }
        if(price <= -1){
            throw new IllegalArgumentException("price cannot be minus");
        }
        if(weight <= 0){
            throw new IllegalArgumentException("illegal weight");
        }
        Product p = new Product(productId,name,price,weight);
        id_product.put(productId++,p);
    }*/
    /*public void AddCompany(String address,String comp){
        /*if(supplier_id < 1){
            throw new IllegalArgumentException("the supplier with id : "+ supplier_id +" is not exist");
        }*/
        /*if(address ==null){
            throw new IllegalArgumentException("address is null");
        }
        if(comp == null){
            throw new IllegalArgumentException("company cannot be null");
        }
        if (!(address_supplier.containsKey(address))) {
            throw new IllegalArgumentException("this supplier is not exist in the system ");
        }
        Supplier s = address_supplier.get(address);
        s.AddCompany(comp);
    }*/



    public ArrayList<Supplier> GetAllSuppliers(){
        if (suppliers_id.size()==0){
            throw new IllegalArgumentException("there is no suppliers in the system yet");
        }
        ArrayList<Supplier> suppliers = new ArrayList<>(suppliers_id.values());
        return suppliers;
    }



    public Supplier getSupplier(int supplier_id){
        return suppliers_id.get(supplier_id);
    }

    private Supplier convertSupplierDTO_toSupplierB(SupplierDTO sdto){
        Supplier s = new Supplier(sdto.id,sdto.name,sdto.address,sdto.PhoneNumber,sdto.SupplierType,sdto.bank,sdto.workDays,sdto.companyNumber);
        suppliers_id.put(sdto.id,s);
        return s;
    }
    private SupplierDTO convertSupplierB_toSupplierDTO(Supplier sb){
        return sb.convert();
    }


    public ArrayList<Supplier> printSuppliers() throws Exception {
        ArrayList<SupplierDTO> supplierDTOS = dbHandler.viewSuppliers();
        ArrayList<Supplier> suplier = new ArrayList<>();
        for (SupplierDTO s: supplierDTOS){
            suplier.add(convertSupplierDTO_toSupplierB(s));
        }
        return suplier;
        /*for(Supplier a:a1)
        {
            System.out.println(a.toString());
        }*/
    }

    public ArrayList<Integer> AddConstantOrder(int supplierId,Map<Integer,Integer> code_quantity) throws Exception {
        if (suppliers_id.containsKey(supplierId)){
            Supplier s = suppliers_id.get(supplierId);
            if(!s.getWorkDays().isEmpty()){
                for (Integer code: code_quantity.keySet()){
                    double price = dbHandler.getPriceFromContract(supplierId,code);
                    if (price == -1){
                    throw new IllegalArgumentException("the Product is not appear in the contract");
                }
            }
               return convertDays(s.getWorkDays());
            }else {
                throw new IllegalArgumentException("Can't Assign Constant Automatic Order for the Supplier " +
                        "because he Holds A Wrong Type");
            }
        }else{
            SupplierDTO sdto = dbHandler.getSupplier(supplierId);
            if (sdto == null){
                throw new IllegalArgumentException("the supplier is not exist");
            }else{
                Supplier s = convertSupplierDTO_toSupplierB(sdto);
                suppliers_id.put(sdto.id,s);
                if(!s.getWorkDays().isEmpty()){
                    for (Integer code: code_quantity.keySet()){
                        double price = dbHandler.getPriceFromContract(supplierId,code);
                        if (price == -1){
                            throw new IllegalArgumentException("the Product is not appear in the contract");
                        }
                    }
                    return convertDays(s.getWorkDays());
                }else {
                    throw new IllegalArgumentException("Can't Assign Constant Automatic Order for the Supplier " +
                            "because he Holds A Wrong Type");
                }
            }
        }
    }

    public ArrayList<Integer> convertDays(String workDays) {
        String[] arrOfStr = workDays.split(", ",0);
        ArrayList<Integer> days = new ArrayList<>();
        for (String a : arrOfStr)
            days.add(convertDay(a));
        return days;
    }

    private int convertDay(String a) {
        switch (a){
            case "SUNDAY":
                return 1;
            case "MONDAY":
                return 2;
            case "TUESDAY":
                return 3;
            case "WEDNESDAY":
                return 4;
            case "THURSDAY":
                return 5;
            case "FRIDAY":
                return 6;
            case "SATURDAY":
                return 7;
        }
        return 0;
    }

    /*public void supplierActions(Scanner s) throws SQLException {
        DBHandler db1 = InventoryController.getInstance().getDBHandler();
        while (true) {
            printSuppliers(db1);
            System.out.println("Supplier Actions:");
            System.out.println("Please Select Action:");
            System.out.println("1 - add supplier ");
            System.out.println("2 - delete supplier");
            System.out.println("3 - exit ");

            Scanner io = new Scanner(System.in);
            int op = io.nextInt();

            switch (op) {
                case 1: {
                    AddSupplier(db1);
                    break;
                }

                case 2: {
                    DeleteSupplier(db1);
                    break;
                }
                case 3: {
                    return;
                }
                default: {
                    System.out.println("Selection Unrecognized");
                    break;
                }

            }
        }
    }*/
}
