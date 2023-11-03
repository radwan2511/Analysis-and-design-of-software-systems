package businessLayer.Sup_Inv;

import java.util.ArrayList;
import java.util.Map;

public class Order {
    private int id;
    //private int product_code;
    //private Date date;
    private int year;
    private int month;
    private int day;

    //private int quantity;
    private int supplier_id;
    private String status;///////
    private double price;
    private Map<Integer,Integer> code_quantity;
    private ArrayList<Integer> delivery_ids;


    /*public Order(int id,int product_code, int year, int month, int day, int quantity,int supplier_id,double price){
        this.id = id;
        this.year = year;
        this.month = month;
        this.day = day;
        this.quantity = quantity;
        this.supplier_id = supplier_id;
        this.status = "In Progress";
        this.product_code = product_code;
        this.price = price;
    }
    public Order(int id,int product_code, int year, int month, int day, int quantity,int supplier_id,double price,String status){
        this.id = id;
        this.year = year;
        this.month = month;
        this.day = day;
        this.quantity = quantity;
        this.supplier_id = supplier_id;
        this.status = status;
        this.product_code = product_code;
        this.price = price;
    }*/

    public Order(int id, int year, int month, int day,int supplier_id,double price,Map<Integer,Integer> code_quantity,ArrayList<Integer> delivery_ids){
        this.id = id;
        this.year = year;
        this.month = month;
        this.day = day;
        this.supplier_id = supplier_id;
        this.status = "In Progress";
        this.price = price;
        this.code_quantity = code_quantity;
        this.delivery_ids = delivery_ids;
    }
    public int getId() {
        return id;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public ArrayList<Integer> getDelivery_ids() {
        return delivery_ids;
    }

    public int getSupplier_id() {
        return supplier_id;
    }

    public int getYear() { return year; }

    public int getMonth() { return month; }

    public int getDay() { return day; }

    public Map<Integer, Integer> getCode_quantity() {
        return code_quantity;
    }

    @Override
    public String toString(){
        String str = "supplier id: " + supplier_id + "\n" +
                "Order Date : " + year + "/" + month + "/" + day + "\n" ;
        int i = 1;
        for(Integer code : code_quantity.keySet()){
            str = str + "product " + i + "code: " + code + "\n";
            str = str + "product " + i + "quantity: " + code_quantity.get(code) + "\n\n";
        }
        str = str + "status: " + status + "\n" +
                "price: " + price;
        return str;
    }


    /*public String toString(){
        return "Supplier Name: " + supplier.getName() + "\nSupplier id: " + supplier.getId() +
                "\naddress: " + supplier.getAddress() + "\ndate of order: "+ date.toString() +
                "\nnumber of order: " + id + "\ncontact phone number: " + supplier.getPhoneNumber() +
                "\n----------------------------------------------------------\n ";
    }*/

    /*public String quantityOfProductToString(int productId){
        return "" + quantity.get(productId);
    }*/
   /* public String totalPricePerProductToString(int productId){
        return "" + totalPrice.get(productId);
    }*/

    /*public void ComputePricePerProduct(){
        Contract c = supplier.getContract();
        if(c == null){
            throw new IllegalArgumentException("contract cannot be null");
        }
        for(int j = 0 ;j<products_id.length;j++) {
            int product_id = products_id[j];
            int quantity_of_product = quantity.get(product_id);
            Product p = c.getProduct(product_id);
            Map<Integer, Integer> quantityDiscounts = c.GetProductQuantityDiscounts(product_id);
            if (quantityDiscounts == null) {
                totalPrice.put(product_id, quantity_of_product * p.getListPrice());
            } else {
                int quantityhelp = 0;
                Set<Integer> k = quantityDiscounts.keySet();// quantity: 50, 100, 200
                ArrayList<Integer> keys = new ArrayList<>(k);
                Collections.sort(keys);
                for (int i = 0; i < keys.size() ; i++) {
                    if (i!=keys.size()-1 && keys.get(i) <= quantity_of_product && quantity_of_product < keys.get(i + 1)) {
                        quantityhelp = keys.get(i);
                    }
                    if(i == keys.size()-1){
                        if (quantity_of_product>= keys.get(i)){
                            quantityhelp = keys.get(i);
                        }
                    }
                }
                if(quantityhelp == 0){
                    discount = 0;
                }
                else {
                    this.discount = quantityDiscounts.get(quantityhelp);
                }
                double percent = (double) (100 - discount) / 100;
                double price = percent* quantity_of_product * p.getListPrice();
                totalPrice.put(product_id, price);
            }
        }
        setOrderTotalPrice();
    }*/

    public void ChangeStatus(String status){
        if (status.equals("Done")){
            throw new IllegalArgumentException("the order is done already");
        }else {
            this.status = status;
        }
    }

    public String getStatus(){
        return status;
    }

    /*public int getDiscount(){
        return discount;
    }*/

    /*private void setOrderTotalPrice(){
        for (double price : totalPrice.values()){
            orderTotalPrice += price;
        }
    }*/

    /*public double getOrderTotalPrice() {
        return orderTotalPrice;
    }*/

//    // public int getId() {
//        return id;
//    }
//
//   // public Date getDate() {
//        return date;
//    }

    public boolean finished() {
        return status.equals("Done");
    }

    public double getPrice() {
        return price;
    }

    /*public Supplier getSupplier() {
        return supplier;
    }

    public int[] getProducts_id() {
        return products_id;
    }

    public Map<Integer, Integer> getQuantity() {
        return quantity;
    }

    public Map<Integer, Double> getTotalPrice() {
        return totalPrice;
    }*/

}
