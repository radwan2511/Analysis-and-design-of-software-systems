package businessLayer.Sup_Inv;

public class Sale {
    private int sale_type; // 1 - sale by category , 2 - sale by specific product
    private String sale_percentage;
    private String date;

    private int id;


    public Sale(int sale_type, String date, String sale_percentage, int id){
        this.sale_type = sale_type;
        this.date = date;
        this.sale_percentage = sale_percentage;
        this.id = id;
    }

    public int getSale_type(){return this.sale_type;}
    public String getDate(){return this.date;}
    public String getSale_percentage() {return this.sale_percentage;}

    @Override
    public String toString() {
        String type = "";
        if(sale_type == 1)
            type = "category";
        else
            type = "Specific Product";
        return "Sale: id=" + id + ", by=" + type + ", date=" + date +", sale percentage=" + sale_percentage;
    }

}
