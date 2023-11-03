package dataAccessLayer.Sup_inv_dal.DTO;

public class OrderDTO {
    public int id;
    public int product_code;
    public int year;
    public int month;
    public int day;
    public int quantity;
    public int supplier_id;
    public String status;///////
    public double price;
    public int delivery_id;


    public OrderDTO(int id,int supplier_id,int year, int month, int day,int product_code, int quantity, String status, double price,int delivery_id) {
        this.id = id;
        this.year = year;
        this.month = month;
        this.day = day;
        this.quantity = quantity;
        this.supplier_id = supplier_id;
        this.status = status;
        this.product_code = product_code;
        this.price = price;
        this.delivery_id = delivery_id;
    }
}
