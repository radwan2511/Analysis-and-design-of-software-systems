package dataAccessLayer.Sup_inv_dal;

abstract class DalController<T> {
//    final static String JDBCurl="jdbc:sqlite:superLee.db";
//    String TableName;
//
//    public DalController(String tableName) {
//        TableName = tableName;
//        File DataBaseFile = new File("superLee.db");
//        if(!DataBaseFile.exists()) InitializeDB();
//    }
//
//    public static void InitializeDB(){
//        String suppliers_table = "CREATE TABLE IF NOT EXIST SUPPLIER(\n" +
//                                "id INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
//                                "name TEXT NOT NULL,\n" +
//                                "address TEXT NOT NULL,\n" +
//                                "phoneNumber TEXT NOT NULL,\n" +
//                                "workMethod INTEGER NOT NULL,\n" +
//                                "bank TEXT NOT NULL,\n" +
//                                "paymentMethod TEXT NOT NULL,\n" +
//                                "contactID INTEGER NOT NULL,\n" +
//                                "workDays TEXT NOT NULL,\n" +
//                                "FOREIGN KEY(contactID) REFERENCES CONTACT(id) ON DELETE CASCADE";
//
//        String orders_table= "CREATE TABLE IF NOT EXIST ORDER (\n" +
//                            "id INTEGER AUTOINCREMENT,\n" +
//                            "productCode INTEGER NOT NULL,\n" +
//                            "supplierID INTEGER NOT NULL,\n" +
//                            "quantity INTEGER NOT NULL,\n" +
//                            "status TEXT NOT NULL,\n" +
//                            "orderDate DATE NOT NULL,\n" +
//                            "PRIMARY KEY(id, supplierID),\n" +
//                            "FOREIGN KEY(supplierID) REFERENCES SUPPLIERS(id) ON DELETE CASCADE";
//
//        String contracts_table = "CREATE TABLE IF NOT EXIST CONTRACT(\n" +
//                                "id INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
//                                "supplierID INTEGER NOT NULL,\n" +
//                                "productCode INTEGER NOT NULL,\n" +
//                                "contractNumber INTEGER NOT NULL,\n" +
//                                "quantityDiscount TEXT NOT NULL,\n" +
//                                "constantDay BOOLEAN,\n" +
//                                "FOREIGN KEY(supplierID) REFERENCES SUPPLIERS(id) ON DELETE CASCADE,\n" +
//                                "FOREIGN KEY(productCode) REFERENCES SPECIFICPRODUCT(productCode)";
//
//        String contacts_table = "CREATE TABLE IF NOT EXIST CONTACT(\n" +
//                                "id INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
//                                "name TEXT NOT NULL,\n" +
//                                "phoneNumber TEXT NOT NULL,\n" +
//                                "address TEXT NOT NULL" ;
//
//
//        try (Connection conn = DriverManager.getConnection(JDBCurl);
//             Statement s = conn.createStatement()) {
//            s.addBatch(suppliers_table);
//            s.addBatch(orders_table);
//            s.addBatch(contracts_table);
//            s.addBatch(contacts_table);
//
//            // create a new table
//            s.executeBatch();
//        } catch (Exception e) {
//            System.out.println(e.getMessage());
//        }
    //}


//    public Connection connect() {
//        // SQLite connection string
//        Connection connection = null;
//        try {
//            connection = DriverManager.getConnection(JDBCurl);
//        } catch (SQLException e) {
//            System.out.println(e.getMessage());
//        }
//        return connection;
//    }
//
//    protected abstract T convert_to_DTO(ResultSet rs);
//
//    public List<T> selectAll(){
//        String sql_queiry = "SELECT * FROM "+TableName;
//        List<T> DTOs=new ArrayList<T>();
//        try (Connection connection = this.connect();
//             Statement s  = connection.createStatement();
//             ResultSet rs    = s.executeQuery(sql_queiry)){
//            while (rs.next()) {
//                DTOs.add(convert_to_DTO(rs));
//            }
//
//        } catch (SQLException e) {
//            System.out.println(e.getMessage());
//        }
//        return DTOs;
//    }
//
//    public boolean deleteAll(){
//        String sql_query = "DELETE FROM " + this.TableName;
//        int row_count=0;
//        try (Connection connection = this.connect();
//             PreparedStatement ps = connection.prepareStatement(sql_query)) {
//            // update
//            row_count =ps.executeUpdate();
//        } catch (SQLException e) {
//            System.out.println(e.getMessage());
//        }
//        return row_count>0;
//    }





}
