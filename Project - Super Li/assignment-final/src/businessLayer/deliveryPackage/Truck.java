package businessLayer.deliveryPackage;

public class Truck {
    private String plateNumber;
    private String model;
    private double netWeight;
    private double maxWeight;

    public Truck(String plateNumber, String model, double netWeight, double maxWeight){
        this.plateNumber = plateNumber;
        this.model = model;
        this.netWeight = netWeight;
        this.maxWeight = maxWeight;
    }

    public String getPlateNumber(){
        return plateNumber;
    }

    public String getModel(){
        return model;
    }

    public double getNetWeight(){
        return netWeight;
    }

    public double getMaxWeight(){
        return maxWeight;
    }

}
