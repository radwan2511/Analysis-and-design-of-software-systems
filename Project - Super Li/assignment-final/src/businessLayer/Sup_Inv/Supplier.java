package businessLayer.Sup_Inv;

import dataAccessLayer.Sup_inv_dal.DTO.SupplierDTO;

import java.time.LocalDate;

public class Supplier {
    private int id;
    private String name;
    private String address;
    private String PhoneNumber;
    private int SupplierType;
    private String bank;
    private String TermOfPayment;
    private String workDays;
    //send work days
    private int companyNumber;
    // from supplier send address , phone number , name , work days
    // dont send null work days


    public Supplier(int id, String name,String address,String PhoneNumber,int SupplierType, String bank, int companyNumber){
        this.id = id;
        this.name = name;
        this.address = address;
        this.PhoneNumber = PhoneNumber;
        this.SupplierType = SupplierType;
        this.bank = bank;
        this.TermOfPayment = "cash, credit card";
        this.workDays = "";
        this.companyNumber = companyNumber;
    }

    public Supplier(int id, String name,String address,String PhoneNumber,int SupplierType, String bank,String workDays,int companyNumber){
        this.id = id;
        this.name = name;
        this.address = address;
        this.PhoneNumber = PhoneNumber;
        this.SupplierType = SupplierType;
        this.bank = bank;
        this.TermOfPayment = "cash, credit card";
        this.workDays = workDays;
        this.companyNumber = companyNumber;
    }


   /* public boolean AddContact(Contact contact){
        if (!contacts.contains(contact)){
            contacts.add(contact);
            return true;
        }
        return false;
    }*/

    public int getCompanyNumber() { return  companyNumber;}



    public String getBank() {
        return bank;
    }

    public String getTermOfPayment() {
        return TermOfPayment;
    }

    public String getWorkDays() {
        return workDays;
    }

    public String toString(){
       return "supplier id: "+ id + "\nsupplier name: " + name +
               "\nsupplier address: " + address + "\nsupplier phone number: " + PhoneNumber +
               "\nsupplier type: " + ConvertSupplierType(SupplierType) + "\nwork days: " + workDays +
               "\nsupplier bank account: " + bank;
              // "\nsupplier companies: " + companiesTostring();
   }

   /*public void DeleteContract(){
       if(contract_id == -1){
           throw new IllegalArgumentException("there is no contract with the supplier yet");
       }
       contract_id = -1;
       // dal delete
   }*/


  /* private String companiesTostring(){
       String s = "";
       for (int i = 0 ;i<companies.size();i++){
           s = s + "company number " + i + ": " + companies.get(i);
       }
       return s;
   }*/

   /*private String workdaysToString(){
       String s = "";
       switch (SupplierType){
           case 1:
               s = s + "the working days are: ";
               for (int i = 0 ; i<WorkDays.size();i++){
                   s = s + ConvertDay(WorkDays.get(i)) + ", ";
               }
               return s;
           case 2:
           case 3:
               return s + "there is no constant days";
       }
       return s;
   }*/


    /*public void AddContact(String name,String phoneNumber,String address){
        Contact c = new Contact(contactId,name,phoneNumber,address);
        id_contact.put(contactId++,c);
        contacts.add(c);
    }*/

    /*public Contract getContract() {
        return contract;
    }*/
    /*protected void DeleteContract(){
       if(this.contract == null){
           throw new IllegalArgumentException("there is no contract");
       }
        this.contract = null;
    }*/

    /*public ArrayList<Contact> getContacts() {
        return contacts;
    }*/


    /*private void setContractProducts(Map<Product,Map<Integer,Integer>> QuantityDiscountPerProduct){
        for(Product p : QuantityDiscountPerProduct.keySet()){
            this.contract.AddProduct(p,QuantityDiscountPerProduct.get(p));
        }
    }*/

    public EnumSupplierType ConvertSupplierType(int t){
        switch (t){
            case 1:
                return EnumSupplierType.ConstantDaysSupplier;
            case 2:
                return EnumSupplierType.JustOrderSupplier;
            case 3:
                return EnumSupplierType.CollectingSupplier;
        }
        return null;
    }

    /*public void AddCompany(String comp){
        if(companies.contains(comp)) {
            throw new IllegalArgumentException("the supplier is already in this  comany");
        }
        companies.add(comp);
    }*/

    private EnumDay ConvertDay(int number){
        switch (number){
            case 1:
                return EnumDay.SUNDAY;
            case 2:
                return EnumDay.MONDAY;
            case 3:
                return EnumDay.TUESDAY;
            case 4:
                return EnumDay.WEDNESDAY;
            case 5:
                return EnumDay.THURSDAY;
            case 6:
                return EnumDay.FRIDAY;
            case 7:
                return EnumDay.SATURDAY;
        }
        return null;
    }

    public String setWorkDays(int[] days){
        if(this.SupplierType == 1) {
            for(int i = 0;i<days.length;i++){
                if (days[i] == 1){
                    workDays = workDays + "SUNDAY, ";
                }
                else if(days[i] == 2){
                    workDays= workDays+ "MONDAY, ";
                }
                else if(days[i] == 3){
                    workDays= workDays+ "TUESDAY, ";
                }
                else if(days[i] == 4){
                    workDays= workDays+ "WEDNESDAY, ";
                }
                else if(days[i] == 5){
                    workDays= workDays+ "THURSDAY, ";
                }
                else if(days[i] == 6){
                    workDays= workDays+ "FRIDAY, ";
                }
                else if(days[i] == 7){
                    workDays= workDays+ "SATURDAY, ";
                }else{
                    throw new IllegalArgumentException("Illegal day");
                }
            }
            if(workDays.length()!=0){
                workDays = workDays.substring(0,workDays.length()-2);
                 return workDays;
            }
        }else{
            throw new IllegalArgumentException("constant days are defined just for the constant day supplier");
        }
        return "";
    }

    public int getSupplierType() {
        return SupplierType;
    }

    /*public void setContract_id(int contract_id) {
        if (this.contract_id!= -1){
            throw new IllegalArgumentException("there is already a contract with the supplier");
        }
        this.contract_id = contract_id;
    }*/

    protected boolean GetConstantSupplierOrder(){
        return workDays.contains(LocalDate.now().getDayOfWeek().name());
    }

    public String getName(){
        return name;
    }
    public int getId(){
        return id;
    }
    public String getAddress(){
        return address;
    }
    public String getPhoneNumber(){
        return PhoneNumber;
    }

   /* public void setContact_id(int contact_id) {
        if(contact_id!=-1){
            throw new IllegalArgumentException("there is already a contact with the supplier");
        }
        this.contact_id = contact_id;
    }*/

    public SupplierDTO convert() {
        return new SupplierDTO(id,name,address,PhoneNumber,SupplierType,bank,workDays, companyNumber);
    }

    /*public int getContactId() {
        return contactId;
    }

    public ArrayList<Integer> getWorkDays() {
        return WorkDays;
    }

    public int getProductId() {
        return productId;
    }

    public String getBankAccount() {
        return BankAccount;
    }

    public String getBnNumber() {
        return BnNumber;
    }

    public EnumPayment getTermOfPayment() {
        return TermOfPayment;
    }

    public ArrayList<String> getCompanies() {
        return companies;
    }


    public String contactToString(){
       return contract.toString();
    }*/

}
