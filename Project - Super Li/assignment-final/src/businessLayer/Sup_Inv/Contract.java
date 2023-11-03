package businessLayer.Sup_Inv;

import java.util.HashMap;
import java.util.Map;

public class Contract {
    private int supplierId;
    private int contractNum;
    private boolean constantDaysDelivery;
    private int product_code;
    private String quantity_discount;// the format is "quantity1, discount1, quanity2, discount2 ..."

    //private int quantity;

    private double price;

    public Contract(int supplierId, int product_code, int contractNum, double price,boolean constantDay,String quantity_discount){
        this.supplierId =supplierId;
        this.product_code = product_code;
        this.contractNum = contractNum;
       // this.quantity = quantity;
        this.constantDaysDelivery = constantDay;
        this.quantity_discount = quantity_discount;
        this.price = price;
    }

    public int getProduct_code() {
        return product_code;
    }

    public String getQuantity_discount() {
        return quantity_discount;
    }

    /*public boolean AddProduct(Product p , Map<Integer,Integer> quantity_discount){
        if(!ProductCatalogNumber.containsValue(p)){
            ProductCatalogNumber.put(p.getId(),p);
            QuantityDiscountPerProduct.put(p,quantity_discount);
            return true;
        }
        return false;
    }*/

    /*public Map<Product, Map<Integer, Integer>> getQuantityDiscountPerProduct() {
        return QuantityDiscountPerProduct;
    }*/

    public Map<Integer,Integer> GetProductQuantityDiscounts(){
        if (quantity_discount.isEmpty()){
            return new HashMap<>();
        }
        String[] arrOfStr = quantity_discount.split(", ", 0);
        if(arrOfStr.length%2!=0){
            throw new IllegalArgumentException("every quanitity should have discount");
        }
        Map<Integer,Integer> q_d = new HashMap<>();
        for (int i = 0; i<arrOfStr.length;i= i + 2){
            try{
                int q = Integer.parseInt(arrOfStr[i]);
                int d = Integer.parseInt(arrOfStr[i+1]);
                q_d.put(q,d);
            }
            catch (NumberFormatException ex){
                ex.printStackTrace();
            }
        }
        return q_d;
    }

    public int getSupplierId() {
        return supplierId;
    }

    public int getContractNum() {
        return contractNum;
    }

    public boolean getConstantDay(){
        return constantDaysDelivery;
    }

    public double getPrice() {
        return price;
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
