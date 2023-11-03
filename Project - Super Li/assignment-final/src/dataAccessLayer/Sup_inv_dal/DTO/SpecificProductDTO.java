package dataAccessLayer.Sup_inv_dal.DTO;

public class SpecificProductDTO {
    public int id;
    public int code;
    public String location;
    public boolean isFlaw;
    public String expired;
    public GeneralProductDTO generalProduct;


    public SpecificProductDTO(int id,String location,boolean isFlaw,String expired){
        this.id = id;
        this.location = location;
        this.isFlaw = isFlaw;
        this.expired = expired;
    }
    public SpecificProductDTO(int id,String location,boolean isFlaw,String expired,GeneralProductDTO generalProduct){
        this.id = id;
        this.location = location;
        this.isFlaw = isFlaw;
        this.expired = expired;
        this.generalProduct = generalProduct;
    }
}
