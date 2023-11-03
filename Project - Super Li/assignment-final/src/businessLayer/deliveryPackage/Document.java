package businessLayer.deliveryPackage;

import java.util.List;

public class Document {
    private int documenId;
    private int deliveryId;
    private Location destination;
    private List<Item> items;
    private double totalWeight;

    public Document(int documenId, int deliveryId, Location destination, List<Item> items, double totalWeight){
        this.documenId = documenId;
        this.deliveryId = deliveryId;
        this.destination = destination;
        this.items = items;
        this.totalWeight = totalWeight;
    }

    public int getId(){
        return documenId;
    }

    public int getDeliveryId(){
        return  deliveryId;
    }

    public Location getDestination(){
        return destination;
    }

    public List<Item> getItems(){
        return items;
    }

    public double getTotalWeight(){
        return  totalWeight;
    }

    public void setTruckWeight(double weight){
        this.totalWeight = weight;
    }
}
