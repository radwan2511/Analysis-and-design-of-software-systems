package businessLayer.Sup_Inv;

import businessLayer.API;
import dataAccessLayer.DalController;
import dataAccessLayer.Sup_inv_dal.DBHandler;
import dataAccessLayer.Sup_inv_dal.DTO.*;
import dataAccessLayer.Sup_inv_dal.OrderDalController;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

public class OrderController {
    private Map<Integer, ArrayList<Order>> supplierid_order; // <supplier id,<Order>>
    private int order_id;
    private OrderDalController odc;
    private DBHandler dbHandler;
    private Map<Date,Map<Integer, Integer>> failed_orders;
    private API api;


    private int ids;
    private static OrderController instance = null;
    private OrderController() throws Exception {
        supplierid_order = new HashMap<>();
        failed_orders = new HashMap<>();
        order_id = 1;
        odc = new OrderDalController();
        dbHandler = InventoryController.getInstance().getDBHandler();
        //LoadData();
        api = new API();
    }

    public static OrderController getInstance() throws Exception {
        if (instance == null){
            instance = new OrderController();
        }
        return instance;
    }
    //    private void LoadData(){
//        odc.selectAll().forEach(orderDTO -> {
//            if(!supplierid_order.containsKey(orderDTO.supplier_id)){
//                Map<Integer,Order> orders = new HashMap<>();
//                orders.put(orderDTO.id,convertOrderDto_toOrderB(orderDTO));
//                supplierid_order.put(orderDTO.supplier_id,orders);
//                order_id = orderDTO.id;
//            }
//            else{
//                if (!supplierid_order.get(orderDTO.supplier_id).containsKey(orderDTO.id)){
//                    supplierid_order.get(orderDTO.supplier_id).put(orderDTO.id,convertOrderDto_toOrderB(orderDTO));
//                }
//                if (order_id<orderDTO.id){
//                    order_id = orderDTO.id;
//                }
//            }
//        });
//    }

//    public void addOrder(int supplier_id, int product_code, Date date,int quantity){
//        if(supplier_id == -1){
//            throw new IllegalArgumentException("the supplier id is not legal");
//        }
//        if(quantity <1){
//            throw new IllegalArgumentException("can't order an order without products");
//        }
//        if(!supplierid_order.containsKey(supplier_id)){
//            supplierid_order.put(supplier_id,new HashMap<>());
//        }
//        Order order = new Order(order_id,product_code,date,quantity,supplier_id);
//        supplierid_order.get(supplier_id).put(order_id,order);
//        order_id ++;
//    }

    /*public void CancelOrder(int year, int month, int day) throws SQLException {
        if(!supplierid_order.containsKey(supplier_id)){
            OrderDTO o = dbHandler.getOrderFromSupplierAndCodeAndDate(supplier_id,product_code,year,month,day);
            if (o == null)
                throw new IllegalArgumentException("there is no order ordered from this supplier");
            else{
                dbHandler.deleteOrderYearMonthDay(supplier_id,product_code,year,month,day);
            }
        }else {
            if (!supplierid_order.get(supplier_id).containsKey(product_code)) {
                OrderDTO o = dbHandler.getOrderFromSupplierAndCodeAndDate(supplier_id,product_code,year,month,day);
                if (o == null)
                    throw new IllegalArgumentException("this order is not belong to this supplier");
                else {
                    dbHandler.deleteOrder(supplier_id, product_code,year,month,day);
                }
            }
            else{
                supplierid_order.get(supplier_id).remove(product_code);
                dbHandler.deleteOrder(supplier_id, product_code,year,month,day);
            }
        }
    }*/

    /*public void CancelOrder(int year, int month, int day) throws SQLException {
        List<Integer> d_id = new ArrayList<>();
        ArrayList<OrderDTO> os = dbHandler.getOrderFromDate(year,month,day);

        if (os.isEmpty()) {
            throw new IllegalArgumentException("there is no order in this date");
        }
        else {
            for (OrderDTO odto:os ){
                d_id.add(odto.delivery_id);
            }
            cancelDelivery(d_id);
            dbHandler.deleteOrderYearMonthDay(year, month, day);
        }
    }*/

    public void CancelOrder(int id) throws Exception {
//        List<Integer> d_id = new ArrayList<>();
        Set<Integer> d_id = new HashSet<>(); // new
        ArrayList<OrderDTO> os = dbHandler.getOrderFromID(id);
        if (os.isEmpty()) {
            throw new IllegalArgumentException("there is no order in this date");
        }
        else {
            for (OrderDTO odto:os ){
                d_id.add(odto.delivery_id);
            }
            //change to api.
            api.cancelDelivery(d_id);
            dbHandler.deleteOrderFromID(id);
        }
    }


    public void CancelAllOrders(int supplier_id){
        if(supplier_id == -1){
            throw new IllegalArgumentException("the supplier id is not legal");
        }
        if(!supplierid_order.containsKey(supplier_id)){
            throw new IllegalArgumentException("there is no order ordered from this supplier");
        }
        supplierid_order.remove(supplier_id);
    }


    public ArrayList<Order> DeleteSupplierCancelAllOrders(int supplier_id) throws Exception {
        ArrayList<Order> orders = new ArrayList<>();
        if(supplier_id == -1){
            throw new IllegalArgumentException("the supplier id is not legal");
        }
//        if(supplierid_order.containsKey(supplier_id)){
//            for(Order o : supplierid_order.get(supplier_id)){//
//                if (!o.finished()){//
//                    orders.add(o);
//                    //str = str + "Warning! the order:\n " + o.toString() + " is deleted because of deleting the supplier\n-------------------------------\n";
//                }// assign another supplier to make it or tell yhe user that an order is deleted
//            }
//        }else{
//            ArrayList<OrderDTO> ordersdto = dbHandler.getOrderFromSupplier(supplier_id);
////            List<Integer> d_ids = new ArrayList<>();
//            Set<Integer> d_ids = new HashSet<>(); // new
//            for (OrderDTO odto: ordersdto){
//                d_ids.add(odto.delivery_id);
//            }
//            orders = convertOrdersDTO(ordersdto);
//            for (Order o : orders){
//                if(o.finished()){
//                    orders.remove(o);
//                }
//            }
//            //change to api.
//            api.cancelDelivery(d_ids);
//        }

        // new
        ArrayList<OrderDTO> ordersdto = dbHandler.getOrderFromSupplier(supplier_id);
        Set<Integer> d_ids = new HashSet<>(); // new
        for (OrderDTO odto: ordersdto){
            d_ids.add(odto.delivery_id);
        }

        orders = convertOrdersDTO(ordersdto);
        for (Order o : orders){
            if(o.finished()){
                orders.remove(o);
                for(int deliveryId : o.getDelivery_ids())
                    d_ids.remove(deliveryId);
            }
        }
        //change to api.
        api.cancelDelivery(d_ids);

        supplierid_order.remove(supplier_id);
        dbHandler.deleteSupplierOrders(supplier_id);
        return orders;
    }

    /*public void ChangeStatus(int supplier_id,int product_code,int year,int month,int day) throws SQLException {
        if(order_id < 1){
            throw new IllegalArgumentException("the order is not exist");
        }
        OrderDTO o = null;
        if (!(supplierid_order.containsKey(supplier_id))) {
            o = dbHandler.getOrderFromSupplierAndCodeAndDate(supplier_id,product_code,year,month,day);
            if (o == null)
                throw new IllegalArgumentException("this order is not belong to the supplier ");
            else{
                supplierid_order.put(supplier_id,new HashMap<>());
                Order or = convertOrderDTO(o);
                supplierid_order.get(supplier_id).put(product_code,or);
                or.ChangeStatus();
                dbHandler.UpdateOrderStatus(supplier_id,product_code,"Done",year,month,day);
            }
        }
        else {
            if (!(supplierid_order.get(supplier_id)).containsKey(product_code)) {
                o = dbHandler.getOrderFromSupplierAndCodeAndDate(supplier_id,product_code,year,month,day);
                if (o == null)
                throw new IllegalArgumentException("this order is not exist in the system ");
                else{
                    Order or = convertOrderDTO(o);
                    supplierid_order.get(supplier_id).put(product_code,or);
                    or.ChangeStatus();
                    dbHandler.UpdateOrderStatus(supplier_id,product_code,"Done",year,month,day);
                }
            }else{
                Order or= supplierid_order.get(supplier_id).get(product_code);
                or.ChangeStatus();
                dbHandler.UpdateOrderStatus(supplier_id,product_code,"Done",year,month,day);
            }
        }
    }*/

    public void ChangeStatus(int did) throws Exception {
        //Map<Integer,Integer> code_quantity = API.getDeliveryItems(id)
        ArrayList<OrderDTO> Dorders = dbHandler.getOrderFromDeliveryID(did);
        if (Dorders.isEmpty()){
            throw new IllegalArgumentException("their is no order in this date ");
        }
        ArrayList<Order> orders = convertOrdersDTO(Dorders);
        //Map<Integer,Integer> code_quantity_lacks = new HashMap<>();
        for (Order o: orders){
            //Map<Integer,Integer> code_quantity_of_order = o.getCode_quantity();
            String status = "Done";
            /*for (Integer code:code_quantity_of_order.keySet()){
                if(!code_quantity.containsKey(code)){
                    code_quantity_lacks.put(code,code_quantity_of_order.get(code));
                    status = "Almost Done";
                }
            }*/
            o.ChangeStatus(status);
            dbHandler.UpdateOrderStatus(o.getStatus(),did);
                                //code_quantity
            for (Integer code : o.getCode_quantity().keySet()){         //code_quantity.get(code)
                ProductController.getInstance().updateCurrQuantity(code, o.getCode_quantity().get(code));
            }
        }
        //addComplexOrder(1,code_quantity_lacks,date)
    }

    /*public void ChangeStatus(int year,int month,int day) throws SQLException {
        ArrayList<OrderDTO> Dorders = dbHandler.getOrderFromDate(year,month,day);
        if (Dorders.isEmpty()){
            throw new IllegalArgumentException("their is no order in this date ");
        }
        ArrayList<Order> orders = convertOrdersDTO(Dorders);
        for (Order o: orders){
            o.ChangeStatus();
            dbHandler.UpdateOrderStatus("Done",year,month,day);
            for (Integer code : o.getCode_quantity().keySet()){
                ProductController.getInstance().updateCurrQuantity(code, o.getCode_quantity().get(code));
            }
        }
    }*/

    public ArrayList<Order> getFinishedOrders(int supplier_id){
        if(!supplierid_order.containsKey(supplier_id)){
            throw new IllegalArgumentException("the supplier is not exist in the system");
        }
        ArrayList<Order> finished_orders = new ArrayList<>();
        for (Order o : supplierid_order.get(supplier_id)){
            if (o.finished()){
                finished_orders.add(o);
            }
        }
        if (finished_orders.size() == 0){
            throw new IllegalArgumentException("there is no finished orders yet");
        }
        return finished_orders;
    }

    /*public Order showOrder(int supplier_id,int order_id){
        if(order_id < 1){
            throw new IllegalArgumentException("the order with id : "+ order_id +" is not exist");
        }

        if (!(supplierid_order.containsKey(supplier_id))) {
            throw new IllegalArgumentException("this order is not belong to the supplier ");
        }

        if (!(supplierid_order.get(supplier_id)).containsKey(order_id)) {
            throw new IllegalArgumentException("this order is not exist in the system ");
        }
        return supplierid_order.get(supplier_id).get(order_id);
    }*/

     /*public Order BringOrder(int supplier_id,int order_id){
         if (!(supplierid_order.containsKey(supplier_id))) {
             throw new IllegalArgumentException("this order is not belong to the supplier ");
         }

         if (!(supplierid_order.get(supplier_id)).containsKey(order_id)) {
             throw new IllegalArgumentException("this order is not exist in the system ");
         }
         if(order_id < 1){
             throw new IllegalArgumentException("the order with id : "+ order_id +" is not exist");
         }
        Supplier s = SupplierController.getInstance().getSupplier(supplier_id);
        Order o = supplierid_order.get(supplier_id).get(order_id);
        if (s.getSupplierType() == 1){
            boolean done_order = s.GetConstantSupplierOrder() && o.finished();
            if(done_order){
                return o ;
            }
            else {
                throw new IllegalArgumentException("the order is not ready");
            }
        }
        else if(s.getSupplierType() == 2 || s.getSupplierType() == 3){
            if(o.finished()){
                return o;
            }
            else {
                throw new IllegalArgumentException("the order is not ready");
            }
        }
        else{
            throw new IllegalArgumentException("the supplier type is not legal");
        }
    }*/

//    private Order convertOrderDto_toOrderB(OrderDTO odto){
//        return new Order(odto.id,odto.product_code,odto.date,odto.quantity,odto.supplier_id);
//    }

    /*public Order addOrder(int year, int month, int day, int code, int quantity) throws SQLException {
        Map<Double,Contract> price_contract = bestDeal(code,quantity);
        double price = 0;
        for (Double p:price_contract.keySet()){
            price = p;
        }
        Contract c = price_contract.get(price);
        int supplier_id = c.getSupplierId();
        int[] min_curr = ProductController.getInstance().MinQuantity_AndCurrQuantity(code);
        if (min_curr[1]>=min_curr[0]) {
            int id = dbHandler.getOrder_ids();
            Order o = new Order(id,code, year, month, day, quantity, supplier_id, price);
            if (!supplierid_order.containsKey(supplier_id)) {
                supplierid_order.put(supplier_id, new HashMap<>());
            }
            supplierid_order.get(supplier_id).put(code, o);
            //call add delivery and send work days
            // send date as Date
            // from supplier send address , phone number , name , work days
            // dont send null work days
            // send product - call get general product

            dbHandler.insertOrder(convertOrderB_toOrderDTO(o));
            ProductController.getInstance().updateCurrQuantity(code,min_curr[1] + quantity);
            return o;
        }else{
            if (quantity < min_curr[0]) {
                int id = dbHandler.getOrder_ids();
                Order o = new Order(id,code, year, month, day, quantity + min_curr[0], supplier_id, price);
                if (!supplierid_order.containsKey(supplier_id)) {
                    supplierid_order.put(supplier_id, new HashMap<>());
                }
                supplierid_order.get(supplier_id).put(code, o);
                dbHandler.insertOrder(convertOrderB_toOrderDTO(o));
                ProductController.getInstance().updateCurrQuantity(code, min_curr[1] + quantity + min_curr[0]);
                return o;
            }
            else{
                int id = dbHandler.getOrder_ids();
                Order o = new Order(id,code, year, month, day, quantity , supplier_id, price);
                if (!supplierid_order.containsKey(supplier_id)) {
                    supplierid_order.put(supplier_id, new HashMap<>());
                }
                supplierid_order.get(supplier_id).put(code, o);
                dbHandler.insertOrder(convertOrderB_toOrderDTO(o));
                ProductController.getInstance().updateCurrQuantity(code, min_curr[1] + quantity );
                return o;
            }
        }
    }*/
    /*private OrderDTO convertOrderB_toOrderDTO(Order o){
        return new OrderDTO(o.getId(),o.getSupplier_id(),o.getYear(),o.getMonth(),o.getDay(),o.getProduct_code(),o.getQuantity(),o.getStatus(),o.getPrice());
    }*/

    private ArrayList<Order> convertOrdersDTO(ArrayList<OrderDTO> orderDTOS){
        ArrayList<Order> orders = new ArrayList<>();
        Map<Integer,Map<Integer,Integer>> oid_code_quantity = new HashMap<>();
        Map<Integer,Double> oid_price = new HashMap<>();
        for (OrderDTO o: orderDTOS){
            if(!oid_code_quantity.containsKey(o.id)){
                oid_code_quantity.put(o.id,new HashMap<>());
                oid_code_quantity.get(o.id).put(o.product_code,o.quantity);
                oid_price.put(o.id,o.price);
            }else{
                oid_code_quantity.get(o.id).put(o.product_code,o.quantity);
                double price = oid_price.get(o.id);
                price = price + o.price;
                oid_price.remove(o.id);
                oid_price.put(o.id,price);
            }
        }
        for (Integer id: oid_code_quantity.keySet()){
            ArrayList<Integer> d_ids = new ArrayList<>();
            for (OrderDTO o: orderDTOS){
                boolean exist = false;
                if (id == o.id){
                    d_ids.add(o.delivery_id);
                    for (Order order : orders){
                        if (order.getId() == o.id){
                            exist = true;
                        }
                    }
                    if(!exist) {
                        Order order = new Order(o.id, o.year, o.month, o.day, o.supplier_id, oid_price.get(o.id), oid_code_quantity.get(id),d_ids);
                        orders.add(order);
                    }
                }
            }
        }
        return orders;
    }

    /*private Order convertOrderDTO(OrderDTO orderDTO){
        Map<Integer,Integer> code_quantity = new HashMap<>();
        //return new Order(orderDTO.id,orderDTO.product_code,orderDTO.year,orderDTO.month,orderDTO.day,orderDTO.quantity,orderDTO.supplier_id,orderDTO.price,orderDTO.status);
    }*/
    private Map<Double,Contract> bestDeal(int code,int quantity) throws Exception {
        ArrayList<ContractDTO> contracts = dbHandler.selectContractwithProductCode(code);
        ArrayList<Contract> bcontracts = convertContractsDTO_to_Buss(contracts);
        Contract contract = null;
        double curr_price = Integer.MAX_VALUE;
        for (Contract c: bcontracts){
           double price = computePrice(quantity,c);
           if (price < curr_price){
               curr_price = price;
               contract = c;
           }
        }
        Map<Double,Contract> price_contract = new HashMap<>();
        price_contract.put(curr_price,contract);
        return price_contract;
    }

    private ArrayList<Contract> convertContractsDTO_to_Buss(ArrayList<ContractDTO> contractDTOS){
        ArrayList<Contract> contracts = new ArrayList<>();
        for (ContractDTO c: contractDTOS ){
            contracts.add(convertDTOcontract(c));
        }
        return contracts;
    }

    private Contract convertDTOcontract(ContractDTO c) {
        return new Contract(c.supplierId,c.product_code,c.contractNum,c.price,c.constantDaysDelivery,c.quantity_discount);
    }

    private double computePrice(int quantity, Contract c){
        Map<Integer,Integer> quantity_discount = new HashMap<>();
        int discount = 0;
        quantity_discount = c.GetProductQuantityDiscounts();
        if (quantity_discount.size() == 0){
            return c.getPrice()* quantity;
        }
        int quantityhelp = 0;
        Set<Integer> k = quantity_discount.keySet();// quantity: 50, 100, 200
        ArrayList<Integer> keys = new ArrayList<>(k);
        Collections.sort(keys);
        for (int i = 0; i < keys.size() ; i++) {
            if (i!=keys.size()-1 && keys.get(i) <= quantity && quantity < keys.get(i + 1)) {
                quantityhelp = keys.get(i);
            }
            if(i == keys.size()-1){
                if (quantity>= keys.get(i)){
                    quantityhelp = keys.get(i);
                }
            }
        }
        if(quantityhelp == 0){
            discount = 0;
        }
        else {
            discount = quantity_discount.get(quantityhelp);
        }
        double percent = (double) (100 - discount) / 100;
        return percent* quantity * c.getPrice();
    }

    public ArrayList<Order> CheckAddOrder(Map<Integer, Integer> code_quantity, int year, int month, int day) throws Exception {
        Map<Integer, Integer> code_quantity_to_order = new HashMap<>();
        for (Integer code:code_quantity.keySet()){
            OrderDTO odto = dbHandler.getOrderFromDateAndCode(code,year,month,day);
            if (odto == null){
                code_quantity_to_order.put(code,code_quantity.get(code));
            }
        }
        if (!code_quantity_to_order.isEmpty()) {
            return addComplexOrder(1,code_quantity_to_order, year, month, day);
        }
        else return new ArrayList<>();
    }

    public void addOrder(int supplierId, Map<Integer, Integer> code_quantity, int year, int month, int day) throws Exception {
        SupplierDTO sdto = dbHandler.getSupplier(supplierId);
        Supplier s = new Supplier(sdto.id,sdto.name,sdto.address,sdto.PhoneNumber,sdto.SupplierType,sdto.bank,sdto.companyNumber);
        //Date date = new Date(year,month-1,day);
        Date date = createDate(year, month, day); // new
        ArrayList<GeneralProduct> generalProducts = new ArrayList<>();
        for (Integer code : code_quantity.keySet()){
            GeneralProductDTO gb = dbHandler.getGeneralProductByCode(code).entrySet().iterator().next().getValue();
            CategoryDTO categoryDTO = dbHandler.getCategoryById(gb.cat_id);
            Category c = convertDTOcategory(categoryDTO);
//            GeneralProduct g = new GeneralProduct(gb.selling_price,gb.name,gb.code,gb.min_quantity,gb.quantity,gb.manufactor,c, gb.weight);
            GeneralProduct g = new GeneralProduct(gb.selling_price,gb.name,gb.code,gb.min_quantity,code_quantity.get(code),gb.manufactor,c, gb.weight); // new
            generalProducts.add(g);
        }
        Map<Integer, Date> asdsa = new HashMap<>();
        int order_id = dbHandler.getOrder_ids();
        try {
            asdsa = api.addDelivery(order_id,date, s, generalProducts);
        }catch (Exception e){
            int id = dbHandler.getOrder_ids();
            failed_orders.put(date,code_quantity);
            for (Integer code : code_quantity.keySet()){
                dbHandler.insertOrder(new OrderDTO(id,supplierId,0,0,0,code,0,"rejected",0,-1));
            }
            dbHandler.increment_Orderid();
            throw new IllegalArgumentException(e.getMessage());
        }
        double final_price = 0;
        ArrayList<Integer> d_ids = new ArrayList<>();
        for (Integer code : code_quantity.keySet()) {
            int did = asdsa.keySet().iterator().next();
            d_ids.add(did);
            Date d_date = asdsa.values().iterator().next();
            double price = dbHandler.getPriceFromContract(supplierId,code);
            if (price == -1){
                throw new IllegalArgumentException("the Product is not appear in the contract");
            }
            final_price = final_price + price;
            int quantity = code_quantity.get(code);
            int[] min_curr = ProductController.getInstance().MinQuantity_AndCurrQuantity(code);
            LocalDate localDate = d_date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            int d_year = localDate.getYear();
            int d_month = localDate.getMonthValue();
            int d_day = localDate.getDayOfMonth();
            if (min_curr[1] < min_curr[0]) {
                if (quantity < min_curr[0]) {
                    quantity = min_curr[1] + quantity + min_curr[0];
                    int id = dbHandler.getOrder_ids();
                    dbHandler.insertOrder(new OrderDTO(id,supplierId,d_year,d_month,d_day,code,quantity,"in progress",price,did));
                    code_quantity.put(code,quantity);
                } else {
                    quantity = min_curr[1] + quantity;
                    int id = dbHandler.getOrder_ids();
                    dbHandler.insertOrder(new OrderDTO(id,supplierId,d_year,d_month,d_day,code,quantity,"in progress",price,did));
                    code_quantity.put(code,quantity);
                }
            }else {
                int id = dbHandler.getOrder_ids();
                dbHandler.insertOrder(new OrderDTO(id, supplierId,d_year,d_month,d_day, code, quantity, "in progress", price,did));
                code_quantity.put(code, quantity);
            }
        }
        int id = dbHandler.getOrder_ids();
        supplierid_order.get(supplierId).add(new Order(id,year,month,day,supplierId,final_price,code_quantity,d_ids));
        dbHandler.increment_Orderid();
    }
    private Map<Integer,Date> addDelivery(int order_id,Date date,Supplier s,List<GeneralProduct> gb){
        Map<Integer,Date> res = new HashMap<>();
//        res.put(0,new Date(2022,6,18));
        res.put(0,createDate(2022,6,18)); // new
        return res;
    }

    private Category convertDTOcategory(CategoryDTO cdto){
        ArrayList<CategoryDTO> dcategories = new ArrayList<>();
        CategoryDTO c = cdto;
        if(cdto.main == null)
            return new Category(cdto.pId,cdto.name,null);
        else{
            while (c.main!=null){
                dcategories.add(c);
                c = c.main;
            }
            CategoryDTO curr = dcategories.get(dcategories.size()-1);
            Category category = new Category(curr.pId,curr.name,null);
            for (int i = dcategories.size() - 2; i>0;i--){
                CategoryDTO cd = dcategories.get(i);
                category = new Category(cd.pId,cd.name,category);
            }
            return category;
        }
    }

    public void addOrderFromID(int id) throws Exception {
        ArrayList<OrderDTO> odto = dbHandler.getOrderFromID(id);
        if (odto.isEmpty()){
            throw new IllegalArgumentException("the order is not exist");
        }
        ArrayList<Order> orders = convertOrdersDTO(odto);
        Map<Integer, Integer> code_quantity = new HashMap<>();
        for (Order o: orders){
            for (Integer code : o.getCode_quantity().keySet()){
                code_quantity.put(code,o.getCode_quantity().get(code));
            }
        }
        Date date = new Date();
        // new
        dbHandler.deleteOrderFromID(id);
        addComplexOrder(1,code_quantity,getYear(date),getMonth(date),getDay(date)); // change
    }
    public ArrayList<Order> addComplexOrder(int flag,Map<Integer, Integer> code_quantity, int year, int month, int day) throws Exception {
        ArrayList<Order> orders = new ArrayList<>();
        Map<Double, Contract> price_contract = new HashMap<>();
        Map<Integer,Map<Integer,Integer>> supplier_code_qantity = new HashMap<>(); // for each supplier we save here the products of the order that will be assigned to him
        for (Integer code : code_quantity.keySet()) {
            int quantity = code_quantity.get(code);
            int[] min_curr = ProductController.getInstance().MinQuantity_AndCurrQuantity(code);
            if (min_curr[1] < min_curr[0]) {
                if (quantity < min_curr[0]) {
                    quantity = min_curr[1] + quantity + min_curr[0];
                }else{
                    quantity = min_curr[1] + quantity;
                }
            }
            price_contract = bestDeal(code, quantity);
            double price = price_contract.keySet().iterator().next();
            Contract c = price_contract.get(price);
            int supplier_id = c.getSupplierId();
            if (!supplier_code_qantity.containsKey(supplier_id)){
                Map<Integer, Integer> cq = new HashMap<>();
                cq.put(code,quantity);
                supplier_code_qantity.put(supplier_id,cq);
            }else{
                supplier_code_qantity.get(supplier_id).put(code,quantity);
            }
        }
        Map<Integer,Map<Integer,Date>> sup_id_date = new HashMap<>();
        // add delivery(Date,Supplier,List<GeneralProduct>) : delivery id
        for (Integer suppId :supplier_code_qantity.keySet()){
            sup_id_date.put(suppId,new HashMap<>());
            SupplierDTO sdto = dbHandler.getSupplier(suppId);
            Supplier s = new Supplier(sdto.id,sdto.name,sdto.address,sdto.PhoneNumber,sdto.SupplierType,sdto.bank,sdto.companyNumber);
            if (s.getSupplierType() == 3 | flag==1) {
                //Date date = new Date(year, month-1, day);
                Date date = createDate(year, month, day); // new
                ArrayList<GeneralProduct> generalProducts = new ArrayList<>();
                for (Integer code : supplier_code_qantity.get(suppId).keySet()) {
                    GeneralProductDTO gb = dbHandler.getGeneralProductByCode(code).entrySet().iterator().next().getValue();
                    CategoryDTO categoryDTO = dbHandler.getCategoryById(gb.cat_id);
                    Category c = convertDTOcategory(categoryDTO);
//                    GeneralProduct g = new GeneralProduct(gb.selling_price, gb.name, gb.code, gb.min_quantity, gb.quantity, gb.manufactor, c, gb.weight);
                    GeneralProduct g = new GeneralProduct(gb.selling_price, gb.name, gb.code, gb.min_quantity, supplier_code_qantity.get(suppId).get(code), gb.manufactor, c, gb.weight); // new
                    generalProducts.add(g);
                }
                try {
                    int id = dbHandler.getOrder_ids();
                    Map<Integer, Date> asdsa = api.addDelivery(id, date, s, generalProducts);
                    sup_id_date.put(suppId, asdsa);
                } catch (Exception e) {
                    int id = dbHandler.getOrder_ids();
                    failed_orders.put(date, code_quantity);
                    for (Integer code : code_quantity.keySet()) {
//                        dbHandler.insertOrder(new OrderDTO(id, suppId, 0, 0, 0, code, 0, "rejected", price_contract.keySet().iterator().next(), -1));
                        dbHandler.insertOrder(new OrderDTO(id, 0, 0, 0, 0, code, 0, "rejected", 0, -1)); // new
                    }
                    dbHandler.increment_Orderid();
                    throw new IllegalArgumentException(e.getMessage());
                }
            }
        }

        for (Integer suppId: supplier_code_qantity.keySet()){
            //or type = 3 or flag==1 :
            ArrayList<Integer> d_ids = new ArrayList<>();
            int delivery_id = -2 ;
            Date date = new Date();
            SupplierDTO sdto = dbHandler.getSupplier(suppId);
            Supplier s = new Supplier(sdto.id,sdto.name,sdto.address,sdto.PhoneNumber,sdto.SupplierType,sdto.bank, sdto.workDays,sdto.companyNumber);
            if (s.getSupplierType() == 3 | flag==1) {
                delivery_id = sup_id_date.get(suppId).keySet().iterator().next();
                date = sup_id_date.get(suppId).values().iterator().next();
            }else{
                if (s.getSupplierType() == 1){
                    ArrayList<Integer> workDays = new ArrayList<>();
                    String supplierworkDays = s.getWorkDays();
                    System.out.println(supplierworkDays.toString());
                    workDays = SupplierController.getInstance().convertDays(supplierworkDays);
//                    int curr_day = date.getDay()+1;
                    int curr_day = getDay(date); // new
                    int closest_day = find_closestDay(curr_day,workDays);
                    System.out.println(workDays.toString());
                    System.out.println(closest_day);
                    if (curr_day>closest_day){
                        date = new Date(getYear(date),date.getMonth(),getDay(date) + 7 - curr_day + closest_day); // ?month
                        System.out.println(getDay(date));
                        System.out.println(curr_day);
                        System.out.println(closest_day);
                        System.out.println(getDay(date) + 7 - curr_day + closest_day -1);
                    }else{
                        date = new Date(getYear(date),date.getMonth(),getDay(date)  + closest_day - curr_day); // ?month
                    }
                }
            }
            d_ids.add(delivery_id);
            Map<Integer,Integer> cq = supplier_code_qantity.get(suppId);
            double price = 0;
            for (Integer code:cq.keySet()){
                int quantity = cq.get(code);
                price_contract = bestDeal(code, quantity);
                price = price + price_contract.keySet().iterator().next();
                int[] min_curr = ProductController.getInstance().MinQuantity_AndCurrQuantity(code);
                if (min_curr[1] >= min_curr[0]) {
                    int id = dbHandler.getOrder_ids();
                    if (delivery_id == -2)
                        //dbHandler.insertOrder(new OrderDTO(id,suppId,date.getYear() + 1900,date.getMonth()+1,date.getDate(),code,quantity,"in progress",price_contract.keySet().iterator().next(),delivery_id));
                        dbHandler.insertOrder(new OrderDTO(id,suppId,getYear(date),getMonth(date),getDay(date),code,quantity,"in progress",price_contract.keySet().iterator().next(),delivery_id)); // new
                    else
//                        dbHandler.insertOrder(new OrderDTO(id,suppId,date.getYear() ,date.getMonth(),date.getDate(),code,quantity,"in progress",price_contract.keySet().iterator().next(),delivery_id));
                        dbHandler.insertOrder(new OrderDTO(id,suppId,getYear(date) ,getMonth(date) ,getDay(date) ,code,quantity,"in progress",price_contract.keySet().iterator().next(),delivery_id)); // new

                    cq.put(code,quantity + min_curr[1]);
                    //ProductController.getInstance().updateCurrQuantity(code, min_curr[1] + quantity);
                }else{
                    if (quantity < min_curr[0]) {
                        int id = dbHandler.getOrder_ids();
                        if (delivery_id == -2)
                            dbHandler.insertOrder(new OrderDTO(id,suppId,getYear(date),getMonth(date), getDay(date),code,quantity,"in progress",price_contract.keySet().iterator().next(),delivery_id));
                        else dbHandler.insertOrder(new OrderDTO(id,suppId,getYear(date) ,getMonth(date), getDay(date),code,quantity,"in progress",price_contract.keySet().iterator().next(),delivery_id));
                        cq.put(code,min_curr[1] + quantity + min_curr[0]) ;
                        //ProductController.getInstance().updateCurrQuantity(code, min_curr[1] + quantity + min_curr[0]);
                    }else{
                        int id = dbHandler.getOrder_ids();
                        if (delivery_id == -2)
                            dbHandler.insertOrder(new OrderDTO(id,suppId,getYear(date),getMonth(date), getDay(date),code,quantity,"in progress",price_contract.keySet().iterator().next(),delivery_id));
                        else dbHandler.insertOrder(new OrderDTO(id,suppId,getYear(date) ,getMonth(date),getDay(date),code,quantity,"in progress",price_contract.keySet().iterator().next(),delivery_id));

                        cq.put(code,min_curr[1] + quantity) ;
                        //ProductController.getInstance().updateCurrQuantity(code, min_curr[1] + quantity);
                    }
                }
            }
            int id = dbHandler.getOrder_ids();
            Order o = new Order(id,year,month,day,suppId,price,cq,d_ids);
            if (!supplierid_order.containsKey(suppId)) {
                ArrayList<Order> or = new ArrayList<>();
                or.add(o);
                supplierid_order.put(suppId,or);
            }else{
                supplierid_order.get(suppId).add(o);
            }
            orders.add(o);
        }
        if (orders.size()>0){
            dbHandler.increment_Orderid();
        }
        return orders;
    }

    private Date createDate(int year, int month, int day){ // new
        LocalDate localDate = LocalDate.of(year, month, day);
        ZoneId defaultZoneId = ZoneId.systemDefault();
        return Date.from(localDate.atStartOfDay(defaultZoneId).toInstant());
    }

    private int getYear(Date date){ // new
        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        return localDate.getYear();
    }

    private int getMonth(Date date){ // new
        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        return localDate.getMonthValue();
    }

    private int getDay(Date date){ // new
        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        return localDate.getDayOfMonth();
    }

    private static int find_closestDay(int curr_day, ArrayList<Integer> workDays) {
        int counter = 0;
        while (counter<=7) {
            if (counter+curr_day ==7 && workDays.contains(7)) return 7;
            else if (!workDays.contains((counter + curr_day) % 7)) {
                counter++;
            }else{
                return (counter + curr_day) % 7;
            }
        }
        return curr_day;
    }

//    private int find_closestDay(int curr_day, ArrayList<Integer> workDays) {
//        Date date = new Date();
//        int day = date.getDate();
//        int counter = 1;
//        while (counter<=7) {
//            if (counter+curr_day ==7 && workDays.contains(7)) return day+1;
//            else if (!workDays.contains((counter + curr_day) % 7)) {
//                counter++;
//                day ++;
//            }else{
//                return day+1;
//            }
//        }
//        return day+1;
//    }






    /*public static void addOrderDB(DBHandler db1,int year,int month,int day,int code, int quantity) throws SQLException{
        Contract supplierId=getBestDeal(code,quantity , db1);
        if(supplierId==null)
        {
            throw new SQLException();
        }
        Order item= new Order(code, year,month,day, quantity, supplierId.getSupplierId()) ;
        db1.insertOrder(item);
    }*/


   /* private static double getBasic(int code,int supplierid, DBHandler db1)  {
        try{
            ArrayList<Contract> a1=new ArrayList<Contract>();
            a1=db1.viewContract();
            double min=Integer.MAX_VALUE;
            int quan=Integer.MAX_VALUE;
            for(Contract cp:a1)
            {
                if(cp.getQuantity()<quan)
                {
                    quan=cp.getQuantity();
                    min=cp.getPrice();
                }

            }

            return min;
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return -1;

        }
    }*/


    /*private void printOrder() throws SQLException
    {
        for(Order a1:dbHandler.viewOrder())
        {
            GeneralProduct gp=ProductController.getGeneralProductByCode(a1.getProduct_code());
            Supplier sup=dbHandler.getSupplier(a1.getSupplier_id());
            Contract cp= bestDeal(a1.getProduct_code(), a1.getQuantity());
            double basic= getBasic(a1.getProduct_code(),a1.getSupplier_id(),db1);
            double discount= ((double)(basic-cp.getPrice()))/basic;
            System.out.print(a1.toString());
            System.out.println(" Supplier phone: "+sup.getPhoneNumber()+", " +
                    "Product name: "+gp.getName()+", " +
                    "Contract price: "+cp.getPrice()+", " +
                    "Discount: "+discount+", " +
                    "Total price: "+ (cp.getPrice()*a1.getQuantity()));
        }

    }*/
    public ArrayList<Order> getOrders() throws Exception {
        ArrayList<OrderDTO> Dorders = dbHandler.viewOrder();
        ArrayList<Order> orders = convertOrdersDTO(Dorders);
        for (Order o: orders){
            int supplier_id = o.getSupplier_id();
            if(!supplierid_order.containsKey(supplier_id)){
               supplierid_order.put(supplier_id,new ArrayList<>());
               supplierid_order.get(supplier_id).add(o);
            }else{
               if(!supplierid_order.get(supplier_id).contains(o)){
                   supplierid_order.get(supplier_id).add(o);
               }
           }
        }
        return orders;
    }

    public ArrayList<Order> SupplierOrder(int id) throws Exception {
        return convertOrdersDTO(dbHandler.getOrderFromSupplier(id));
    }


    public ArrayList<Order> DayOrder(int year, int month, int day) throws Exception {
        return convertOrdersDTO(dbHandler.getOrderFromDate(year,month,day));
    }


    public ArrayList<Order> HandleArrivedOrders() throws Exception {
        ArrayList<OrderDTO> orderDTOS = dbHandler.getActiveOrders();
        ArrayList<Order> orders = convertOrdersDTO(orderDTOS);
        ArrayList<Order> res = new ArrayList<>();
        for (Order o : orders){
            if (ArrivedOrder(o)){
                res.add(o);
            }
        }
        return res;
    }

    private boolean ArrivedOrder(Order o) {
        Date date = new Date();
//        int year = date.getYear();
//        int month = date.getMonth();
//        int day = date.getDate();
        int year = getYear(date); // new
        int month = getMonth(date);
        int day = getDay(date);
        return year == o.getYear() && month == o.getMonth() && day == o.getDay();
    }
}
