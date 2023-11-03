package businessLayer.Sup_Inv.Facede.Objects;

import java.util.ArrayList;
import java.util.Map;

public class Order {
    //public int product_code;
    public int id;
    public int year;
    public int month;
    public int day;
    //public int quantity;
    public int supplier_id;
    public String status;///////
    double price;
    public Map<Integer,Integer> code_quantity;
    public ArrayList<Integer> delivery_ids;




    public Order(int id,int year, int month, int day,int supplier_id,String status,double price,Map<Integer,Integer> code_quantity,ArrayList<Integer> delivery_ids)
    {
        this.year = year;
        this.month = month;
        this.day = day;
        //this.quantity = quantity;
        this.supplier_id = supplier_id;
        this.status = status;
        //this.product_code = product_code;
        this.price = price;
        this.code_quantity = code_quantity;
        this.delivery_ids = delivery_ids;
    }

    @Override
    public String toString(){
        String str = "supplier id: " + supplier_id + "\n" +
                "Order Date : " + year + "/" + month + "/" + day + "\n" ;
        int i = 1;
        for(Integer code : code_quantity.keySet()){
            str = str + "product " + i + " code: " + code + "\n";
            str = str + "product " + i + " quantity: " + code_quantity.get(code) + "\n\n";
            i++;
        }
        str = str + "status: " + status + "\n" +
                "price: " + price;
        return str;
    }
}
