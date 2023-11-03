package dataAccessLayer.Sup_inv_dal.DTO;

public class ContactDTO {
    public int id;
    public String name;
    public String phoneNumber;
    public String address;

    public ContactDTO(int id,String name,String phoneNumber,String address){
        this.id = id;
        this.name= name;
        this.phoneNumber = phoneNumber;
        this.address = address;
    }
}
