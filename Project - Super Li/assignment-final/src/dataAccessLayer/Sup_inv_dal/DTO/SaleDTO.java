package dataAccessLayer.Sup_inv_dal.DTO;

public class SaleDTO {

    public int sale_type; // 1 - sale by category , 2 - sale by specific product
    public String sale_percentage;
    public String date;

    public int id;


    public SaleDTO(int sale_type, String date, String sale_percentage, int id){
        this.sale_type = sale_type;
        this.date = date;
        this.sale_percentage = sale_percentage;
        this.id = id;
    }
}
