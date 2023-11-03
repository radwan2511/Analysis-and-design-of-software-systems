package businessLayer.Sup_Inv;

import java.time.LocalDateTime;

public class SpecificProduct {

    public static final String LOCATION_STORE="local_store";
    public static final String LOCATION_STORAGE="local_storage";
    private int id;
    private String location;
    private boolean isFlaw;
    private String expired;


    private GeneralProduct generalProduct;





    public SpecificProduct(int id, String location, boolean isFlaw, String expired, GeneralProduct generalProduct) {
        super();
        this.id = id;
        this.location = location;
        this.isFlaw = isFlaw;
        this.expired = expired;
        this.generalProduct = generalProduct;
    }
    public SpecificProduct( String location, boolean isFlaw, String expired, GeneralProduct generalProduct) {
        super();
        this.location = location;
        this.isFlaw = isFlaw;
        this.expired = expired;
        this.generalProduct = generalProduct;
    }

    public int getId() {
        return id;
    }
    public String getLocation() {
        return location;
    }
    public boolean isFlaw() {
        return isFlaw;
    }
    public String getExpired() {
        return expired;
    }
    public GeneralProduct getGeneralProduct() {
        return generalProduct;
    }

    public void makeFlaw(){
        String [] arr = expired.split("/",0);
        if (LocalDateTime.now().getDayOfMonth() <= Integer.parseInt(arr[0]) && LocalDateTime.now().getMonthValue() <= Integer.parseInt(arr[1])&& LocalDateTime.now().getYear() <= Integer.parseInt(arr[2])){
            this.isFlaw = true;
        }else{
            throw new IllegalArgumentException("can't update Flaw, because the item is not expired");
        }
    }

    @Override
    public String toString() {

        return "SpecificProduct:\nid=" + id + "\nlocation=" + location + "\nisFlaw=" + isFlaw + "\nexpired=" + expired
                + "\ngeneralProduct=" + generalProduct;
    }

}
