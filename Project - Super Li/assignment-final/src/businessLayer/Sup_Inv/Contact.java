package businessLayer.Sup_Inv;

public class Contact {
    private int id;
    private String name;
    private String phoneNumber;
    private String address;

    public Contact(int id,String name,String phoneNumber,String address){
        this.id = id;
        this.name= name;
        this.phoneNumber = phoneNumber;
        this.address = address;
    }

    public int getId() {
        return id;
    }

    public String getAddress() {
        return address;
    }

    public String getName() {
        return name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

}
