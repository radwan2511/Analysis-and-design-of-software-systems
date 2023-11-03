package dataAccessLayer.Sup_inv_dal.DTO;

public class ContractDTO {
    public int supplierId;
    public int contractNum;
    public boolean constantDaysDelivery;
    public int product_code;
    public String quantity_discount;// the format is "quantity1, discount1, quanity2, discount2 ..."
    public double price;

    public ContractDTO(int supplierId,int contractNum,boolean constantDay,int product_code,String quantity_discount,double price){
        this.supplierId =supplierId;
        this.contractNum = contractNum;
        this.constantDaysDelivery = constantDay;
        this.product_code = product_code;
        this.quantity_discount = quantity_discount;
        this.price = price;
    }
}
