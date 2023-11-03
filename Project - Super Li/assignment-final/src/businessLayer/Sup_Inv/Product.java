package businessLayer.Sup_Inv;

public class Product {
    private int id;
    private String name;
    private double listPrice;
    private double weight;
   // private Map<Integer,Integer> quantityDiscounts;

    public Product(int id, String name,double listPrice,double weight){
        this.id = id;
        this.name = name;
        this.listPrice = listPrice;
        this.weight = weight;
    }

    public double getListPrice() {
        return listPrice;
    }
    public String getName(){
        return name;
    }

    public int getId() {
        return id;
    }

    public double getWeight() {
        return weight;
    }
}
