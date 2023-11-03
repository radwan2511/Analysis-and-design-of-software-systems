package businessLayer.Sup_Inv;

import dataAccessLayer.Sup_inv_dal.ContractDalController;
import dataAccessLayer.Sup_inv_dal.DBHandler;
import dataAccessLayer.Sup_inv_dal.DTO.ContractDTO;
import dataAccessLayer.Sup_inv_dal.DTO.SupplierDTO;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ContractController {
    private Map<Integer, ArrayList<Contract>> supplierid_contract;
    private ContractDalController cdc;
    private DBHandler dbHandler;

    public ContractController() throws Exception {
        supplierid_contract = new HashMap<>();
        cdc = new ContractDalController();
        dbHandler = InventoryController.getInstance().getDBHandler();
        //LoadData();
    }

//    private void LoadData(){
//        cdc.selectAll().forEach(contractDTO -> {
//            if (!supplierid_contract.containsKey(contractDTO.supplierId)){
//                ArrayList<Contract> contracts = new ArrayList<>();
//                supplierid_contract.put(contractDTO.supplierId,contracts);
//                contracts.add(convertContractDTO_toContractB(contractDTO));
//            }else {
//                if (!check_product_id(contractDTO.supplierId, contractDTO.product_code)) {
//                    supplierid_contract.get(contractDTO.supplierId).add(convertContractDTO_toContractB(contractDTO));
//                }
//            }
//        });
//    }

    private boolean check_product_id(int supplier_id,int product_id){
        for (Contract c : supplierid_contract.get(supplier_id)){
            if (c.getProduct_code() == product_id) return true;
        }
        return false;
    }
    public void makeContract(int supplier_id,int contractNum, boolean constant_day,Map<Integer,Map<Integer,Integer>> QuantityDiscountPerProduct,Map<Integer,Double> price) throws Exception {
        ArrayList<Contract> contracts = new ArrayList<>();
        if(supplier_id == -1){
            throw new IllegalArgumentException("the supplier id is not legal");
        }
        for (Integer id : QuantityDiscountPerProduct.keySet()){
            if(QuantityDiscountPerProduct.get(id) == null){
                throw new IllegalArgumentException("the quantity of the product cannot be null");
            }
            for (Integer quantity : QuantityDiscountPerProduct.get(id).keySet()){
                if (quantity == 0){
                    throw new IllegalArgumentException("the quantity of the product cannot be zero");
                } else{
                    if(QuantityDiscountPerProduct.get(id).get(quantity)==null){
                        throw new IllegalArgumentException("the discount of the product cannot be null");
                    }
                }
            }
        }
        if(supplierid_contract.containsKey(supplier_id)){
            throw new IllegalArgumentException("there is already a contract with this supplier");
        }
        else {
            contracts = convertContractsDTO_to_Buss(dbHandler.getContract_SupplierId_Contract_Num(supplier_id,contractNum));
            if (contracts.size()!=0) {
                supplierid_contract.put(supplier_id,contracts);
                throw new IllegalArgumentException("the contract is already exist");
            }
        }
        supplierid_contract.put(supplier_id,new ArrayList<>());
        for (Integer product_code: QuantityDiscountPerProduct.keySet()) {
            String str ="";
            for (Integer q : QuantityDiscountPerProduct.get(product_code).keySet()){
                str = str + q + ", " + QuantityDiscountPerProduct.get(product_code).get(q) + ", ";
            }
            if (!str.isEmpty()) {
                str = str.substring(0, str.length() - 2);
            }
            Contract contract = new Contract(supplier_id,product_code,contractNum,price.get(product_code),constant_day,str);
            ProductController.getInstance().setCostprice(product_code,price.get(product_code));
            supplierid_contract.get(supplier_id).add(contract);
            dbHandler.addContract(supplier_id,product_code,contractNum,price.get(product_code), constant_day, str);
        }
     //   Supplier s = SupplierController.getInstance().getSupplier(supplier_id);
     //   s.setContract_id(contractNum);
    }

    private ArrayList<Contract> convertContractsDTO_to_Buss(ArrayList<ContractDTO> contractDTOS){
        ArrayList<Contract> contracts = new ArrayList<>();
        for (ContractDTO c: contractDTOS ){
            contracts.add(convertDTOcontract(c));
        }
        return contracts;
    }

    private Contract convertDTOcontract(ContractDTO c) {
        return new Contract(c.supplierId,c.product_code,c.contractNum,c.price,c.constantDaysDelivery,c.quantity_discount);
    }

    public void DeleteContract(int supplier_id) throws Exception {
        if (!(supplierid_contract.containsKey(supplier_id))) {
            SupplierDTO s = dbHandler.getSupplier(supplier_id);
            if (s == null){
                throw new IllegalArgumentException("this supplier is not exist in the system or does not a contract with the company yet");
            }else{
              //  s.DeleteContract();
                dbHandler.deleteContract(supplier_id);
            }
        }
        else{
           // Supplier s = dbHandler.getSupplier(supplier_id);
          //  s.DeleteContract();
            supplierid_contract.remove(supplier_id);
            dbHandler.deleteContract(supplier_id);
        }
    }



    public void DeleteSupplierContract(int supplier_id) throws Exception {
        if(supplierid_contract.containsKey(supplier_id)) {
            Supplier s = SupplierController.getInstance().getSupplier(supplier_id);
          //  s.DeleteContract();
            supplierid_contract.remove(supplier_id);
            dbHandler.deleteContract(supplier_id);
        }else{
            ArrayList<ContractDTO> cdtos = dbHandler.getContract(supplier_id);//
            if (cdtos != null){//
                dbHandler.deleteContract(supplier_id);//
            }
        }
    }

    public ArrayList<Contract> getContract(int supplier_id){
        if(!supplierid_contract.containsKey(supplier_id)){
            throw new IllegalArgumentException("the supplier is not exist in the system");
        }
        return supplierid_contract.get(supplier_id);
    }

//    private Contract convertContractDTO_toContractB(ContractDTO cdto){
//        return new Contract(cdto.supplierId,cdto.contractNum,cdto.constantDaysDelivery,
//                cdto.quantity_discount,cdto.product_code);
//    }

    public ArrayList<Contract> viewProductsContract() throws Exception {
        ArrayList<Contract> contracts = new ArrayList<>();
        int id = -1;
        for(ContractDTO a:dbHandler.viewContract())
        {
            if(!supplierid_contract.containsKey(a.supplierId)){
                //supplierid_contract.put(a.supplierId,new ArrayList<>());
                id = a.supplierId;
            }
            contracts.add(convertDTOcontract(a));
            //System.out.println(a.toString());
        }
        if(id!=-1)
            supplierid_contract.put(id,contracts);

        return contracts;
    }

    public Map<Integer, ArrayList<Contract>> getSupplierid_contract() {
        return supplierid_contract;
    }

    /*private  void DeleteContract(DBHandler db1) throws SQLException {
        System.out.println("Delete Contract:");
        Scanner io=new Scanner(System.in);
        System.out.println("Insert Item Code");
        int code=io.nextInt();
        System.out.println("Insert Item Supplier Id");
        int supplierId=io.nextInt();
        System.out.println("Insert Item Quantity");
        int quantity=io.nextInt();
        db1.deleteContract(code,supplierId,quantity);
        System.out.println("Deleted!");
    }*/








}

