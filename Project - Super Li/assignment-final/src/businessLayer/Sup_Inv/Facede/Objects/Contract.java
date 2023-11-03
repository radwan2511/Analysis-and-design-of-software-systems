package businessLayer.Sup_Inv.Facede.Objects;

public class Contract {
    public int supplierId;
    public int contractNum;
    public boolean constantDaysDelivery;
    public int product_code;
    public String quantity_discount;// the format is "quantity1, discount1, quanity2, discount2 ..."
    public double price;

    public Contract(int supplierId,int contractNum,boolean constantDay,int product_code,String quantity_discount, double price){
        this.supplierId =supplierId;
        this.contractNum = contractNum;
        this.constantDaysDelivery = constantDay;
        this.product_code = product_code;
        this.quantity_discount = quantity_discount;
        this.price = price;
    }

    @Override
    public String toString() {
        return "Contract :" +
                "\nsupplierId=" + supplierId +
                "\ncontractNum=" + contractNum +
                "\nconstantDaysDelivery=" + constantDaysDelivery +
                "\nproduct_code=" + product_code +
                "\nquantity_discount='" + quantity_discount + '\'' +
                "\nprice=" + price +
                '\n';
    }
}
