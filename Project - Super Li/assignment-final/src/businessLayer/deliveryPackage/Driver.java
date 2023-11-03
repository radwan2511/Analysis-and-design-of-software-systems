package businessLayer.deliveryPackage;

public class Driver {
    private String id;
    private String name;
    private String license;
//    private double goodsWeightLimit;

    public Driver(String id, String name, String license){
        this.id = id;
        this.name = name;
        this.license = license;
    }

    public String getId(){
        return id;
    }

    public String getName(){
        return name;
    }

    public String getLicense() {
        return license;
    }

    public void setLicense(String license) {
        this.license = license;
    }
}
