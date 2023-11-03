package businessLayer.Sup_Inv;



import dataAccessLayer.Sup_inv_dal.DBHandler;
import dataAccessLayer.Sup_inv_dal.DTO.CategoryDTO;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.*;

public class CategoryController {

    private static final String ID="id";
    private static final String NAME="name";
    private static final String SUB_CATEGORY_ID="sub_category_id";

    private static CategoryController instance = null;
    private Map<Integer,Category> categories;

    private CategoryController(){categories = new HashMap<>();}
    public static CategoryController getInstance(){
        if (instance == null){
            instance = new CategoryController();
        }
        return instance;
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
            categories.put(category.getId(),category);
            for (int i = dcategories.size() - 2; i>0;i--){
                CategoryDTO cd = dcategories.get(i);
                Category category_curr = new Category(cd.pId,cd.name,category);
                category = category_curr;
                categories.put(category_curr.getId(),category_curr);
            }
            return category;
        }
    }
    public Category getCategoryById(int id) throws Exception {
        CategoryDTO cdto = InventoryController.getInstance().getDBHandler().getCategoryById(id);
        if (cdto!=null){
            return convertDTOcategory(cdto);
        }else{
            return null;
        }
    }
    public List<Category> getAllCategories() throws Exception {
        List<CategoryDTO> categoryDto=InventoryController.getInstance().getDBHandler().getAllCategories();
        List<Category> categories = new ArrayList<>();
        for (CategoryDTO cdto : categoryDto){
            categories.add(convertDTOcategory(cdto));
        }
        return categories;
    }

    public List<Category> getCategoriesbyNamesOrIds(List<String> names) throws Exception {
        List<Category> categories=new LinkedList<>();
        for(String name:names){
            try{
                int id=Integer.parseInt(name);
                categories.add(getCategoryById(id));
            }catch(Exception e){
                categories.add(getCategoryByName(name));
            }
        }
        return categories;
    }
    public Category getCategoryByName(String name) throws Exception {
        CategoryDTO categoryDTO = InventoryController.getInstance().getDBHandler().getCategoryByName(name);
        if (categoryDTO!=null){
            return convertDTOcategory(categoryDTO);
        }else{
            return null;
        }
    }

    /**
     *
     * @param cat
     * @return the inserted category id or -1 on fail
     */
    public int insertCategory(Category cat) throws Exception {
        if (cat == null){
            throw new IllegalArgumentException("category can't be null!");
        }
        String name = cat.getName();
        CategoryDTO cdto = InventoryController.getInstance().getDBHandler().getCategoryByName(name);
        if (cdto!= null){
            return -1;
        }
        int id=-1;
        try{
            Connection c=InventoryController.getInstance().getDBHandler().open();
            Statement stmt = c.createStatement();
            stmt = c.createStatement();
            Category sup = cat.getSupCategory();
            int id_sp = 0;
            if (sup != null ){
                id_sp = sup.getId();
            }
            String sql = "INSERT INTO "+DBHandler.CATEGORY+" ("+ID+","+NAME+","+SUB_CATEGORY_ID+") " +
                    "VALUES ("+InventoryController.getInstance().getDBHandler().getCategory_id()+", '"+cat.getName()+"' ," +id_sp +");";
            stmt.executeUpdate(sql);
            c.commit();
            InventoryController.getInstance().getDBHandler().incrementCategory_idBy(1);
            ResultSet rs = stmt.executeQuery( "SELECT "+ID+" FROM "+DBHandler.CATEGORY+" WHERE "+NAME+" = '"+cat.getName()+"' ;" );
            if(rs.next()){
                id= rs.getInt(ID);
            }
            categories.put(id,cat);
            stmt.close();
            c.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        return id;
    }

    public Map<Integer, Category> getCategories() {
        return categories;
    }
}
