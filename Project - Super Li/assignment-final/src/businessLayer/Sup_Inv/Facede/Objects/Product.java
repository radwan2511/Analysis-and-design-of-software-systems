package businessLayer.Sup_Inv.Facede.Objects;

public class Product {
    public int id;
    public String name;
    public double listPrice;
    public double weight;

    public Product(int id, String name,double listPrice,double weight){
        this.id = id;
        this.name = name;
        this.listPrice = listPrice;
        this.weight = weight;
    }
}
