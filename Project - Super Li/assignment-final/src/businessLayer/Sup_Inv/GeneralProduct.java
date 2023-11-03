package businessLayer.Sup_Inv;

public class GeneralProduct {

    private double selling_price;

    private double cost_price;

    private String name;
    private int code;
    private int min_quantity;
    private int quantity;
    private Category category;
    private String manufactor;
    private double weight;

    public double getSellingPrice() {
        return selling_price;
    }

    public double getCostPrice() {
        return cost_price;
    }




    public String getName() {
        return name;
    }
    public int getCode() {
        return code;
    }
    public int getMin_quantity() {
        return min_quantity;
    }
    public int getQuantity() {
        return quantity;
    }

    public double getWeight() {
        return weight;
    }

    public GeneralProduct(double selling_price, String name, int code, int min_quantity, int quantity, String manufactor, Category category, double weight) {
        super();
        this.selling_price = selling_price;
        this.cost_price = Integer.MAX_VALUE;
        this.name = name;
        this.code = code;
        this.min_quantity = min_quantity;
        this.quantity = quantity;
        this.manufactor = manufactor;
        this.category = category;
        this.weight =weight;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setCost_price(double cost_price) {
        this.cost_price = cost_price;
    }

    @Override
    public String toString() {
        return "{GeneralProduct:\ncost price=" + cost_price + "\nselling price=" + selling_price + "\nname=" + name + "\ncode=" + code + "\nmin_quantity=" + min_quantity
                + "\nquantity=" + quantity + "\n" + category + "manufactor=" + manufactor+"}\n";
    }
    public Category getCategory() {
        return this.category;
    }
    public String getManufactor() {
        return this.manufactor;
    }






}
