package businessLayer.Sup_Inv.Facede.Objects;

import businessLayer.Sup_Inv.EnumSupplierType;

public class Supplier {
    public int id;
    public String name;
    public String address;
    public String PhoneNumber;
    public int SupplierType;
    public String bank;
    public String TermOfPayment;
    public String workDays;

    public Supplier(int id, String name,String address,String PhoneNumber,int SupplierType, String bank
            , String TermOfPayment,
                    String workDays){
        this.id = id;
        this.name = name;
        this.address = address;
        this.PhoneNumber = PhoneNumber;
        this.SupplierType = SupplierType;
        this.bank = bank;
        this.TermOfPayment = TermOfPayment;
        this.workDays = workDays;
    }

    public String toString(){
        return "supplier id: "+ id + "\nsupplier name: " + name +
                "\nsupplier address: " + address + "\nsupplier phone number: " + PhoneNumber +
                "\nsupplier type: " + ConvertSupplierType(SupplierType) + "\nwork days: " + workDays +
                "\nsupplier bank account: " + bank;
        // "\nsupplier companies: " + companiesTostring();
    }

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
}
