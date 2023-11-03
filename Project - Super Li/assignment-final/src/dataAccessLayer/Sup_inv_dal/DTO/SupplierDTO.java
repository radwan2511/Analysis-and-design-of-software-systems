package dataAccessLayer.Sup_inv_dal.DTO;

public class SupplierDTO {
    public int id;
    public String name;
    public String address;
    public String PhoneNumber;
    public int SupplierType;
    public String bank;
    //public String TermOfPayment;
    public String workDays;
    public int companyNumber;

    public SupplierDTO(int id, String name,String address,String PhoneNumber,int SupplierType, String bank
                    ,String workDays,  int companyNumber){
        this.id = id;
        this.name = name;
        this.address = address;
        this.PhoneNumber = PhoneNumber;
        this.SupplierType = SupplierType;
        this.bank = bank;
        //this.TermOfPayment = TermOfPayment;
        this.workDays = workDays;
        this.companyNumber = companyNumber;
    }
}
