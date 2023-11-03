package dataAccessLayer.Sup_inv_dal.DTO;

public class GeneralProductDTO {
    public int code;
    public String name;
    public double cost_price;
    public double selling_price;
    public int min_quantity;
    public int quantity;
    public String manufactor;
    public int cat_id;
    public double weight;

    public GeneralProductDTO(int code,String name,double cost_price,double selling_price,int min_quantity,int quantity,String manufactor,int cat_id, double weight){
        this.code = code;
        this.name = name;
        this.cost_price = cost_price;
        this.selling_price = selling_price;
        this.min_quantity = min_quantity;
        this.quantity = quantity;
        this.manufactor = manufactor;
        this.cat_id = cat_id;
        this.weight = weight;
    }
}
