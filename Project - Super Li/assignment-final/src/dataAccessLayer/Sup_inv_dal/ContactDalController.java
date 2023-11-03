package dataAccessLayer.Sup_inv_dal;

public class ContactDalController{

//    public ContactDalController() {
//        super("CONTACT");
//    }
//
//    @Override
//    protected ContactDTO convert_to_DTO(ResultSet rs) {
//        try {
//            int id = rs.getInt(1);
//            String name = rs.getString(2);
//            String phoneNumber = rs.getString(3);
//            String address = rs.getString(4);
//            return new ContactDTO(id,name,phoneNumber,address);
//        }catch(Exception e){
//            System.out.println(e.getMessage());
//        }
//        return null;
//    }
//
//    public boolean insert(ContactDTO cdto){
//        String insert_queiry =
//                "INSERT INTO CONTACT(id, name, phoneNumber, address) " +
//                        "VALUES(?,?,?,?)";
//        int col_count = 0;
//        try(Connection connection = this.connect()){
//            PreparedStatement ps = connection.prepareStatement(insert_queiry);
//            ps.setInt(1, cdto.id);
//            ps.setString(2, cdto.name);
//            ps.setString(3, cdto.phoneNumber);
//            ps.setString(4, cdto.address);
//            col_count=ps.executeUpdate();
//        }
//        catch (Exception e){
//            System.out.println(e.getMessage());
//        }
//        return col_count>0;
//    }
}
