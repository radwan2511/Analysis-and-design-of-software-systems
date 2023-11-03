package businessLayer.deliveryPackage;

import java.util.List;

public class Item {
    private int code;
    private String name;
    private int amount;
    private double itemWeight;
    private double totalWeight;

    public Item(int code, String name, int amount, double itemWeight){
        this.code = code;
        this.name = name;
        this.amount = amount;
        this.itemWeight = itemWeight;
        totalWeight = amount * itemWeight;
    }

    public int getCode(){ return code;}

    public String getName(){
        return name;
    }

    public int getAmount(){
        return amount;
    }

    public double getItemWeight(){
        return itemWeight;
    }

    public double getTotalWeight(){
        return amount * itemWeight;
    }

//    public static double calculateWeight(List<Item> items){
//        double weight = 0;
//        for(Item item : items){
//            weight += item.getTotalWeight();
//        }
//        return weight;
//    }
}
