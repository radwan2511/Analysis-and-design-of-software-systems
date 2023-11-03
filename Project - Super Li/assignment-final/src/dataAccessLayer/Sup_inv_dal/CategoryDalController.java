package dataAccessLayer.Sup_inv_dal;

import dataAccessLayer.Sup_inv_dal.DTO.CategoryDTO;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

public class CategoryDalController {

    private static final String ID="id";
    private static final String NAME="name";
    private static final String SUB_CATEGORY_ID="sub_category_id";

    public int CountCategories(Connection c) {
        int counter=0;
        try{
            Statement stmt= c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM category WHERE id = (SELECT MAX(id) FROM category) ;" );

            while ( rs.next() ) {
                counter = rs.getInt("id");
            }
            rs.close();
            stmt.close();
            c.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        return counter;
    }

    public CategoryDTO getCategoryById(Connection c, int id) {
        CategoryDTO category=null;
        try{
            Statement stmt;
            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery( "SELECT * FROM "+DBHandler.CATEGORY+" WHERE "+ID+" = "+id+";" );

            if ( rs.next() ) {
                int pId = rs.getInt(ID);
                String  name = rs.getString(NAME);
                int mainCategory= rs.getInt(SUB_CATEGORY_ID);
                CategoryDTO main=null;
                if(mainCategory!=0)
                    main=getCategoryById(c,mainCategory);
                category=new CategoryDTO(pId, name, main);
            }
            rs.close();
            stmt.close();
            if(!c.isClosed()) {
                c.close();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return category;
    }

    public List<CategoryDTO> getAllCategories(Connection c) {
        List<CategoryDTO> category=new LinkedList<>();
        try{
            Statement stmt;
            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery( "SELECT * FROM "+DBHandler.CATEGORY+";" );
            while ( rs.next() ) {
                int pId = rs.getInt(ID);
                String  name = rs.getString(NAME);
//				 int mainCategory= rs.getInt(MAIN_CATEGORY_ID);
                CategoryDTO main=null;
//				 if(mainCategory!=0)
//					 main=getCategoryById(mainCategory);
                category.add(new CategoryDTO(pId, name, main));
            }
            rs.close();
            stmt.close();
            c.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        return category;
    }

    public CategoryDTO getCategoryByName(Connection c, String name) {
        CategoryDTO category=null;
        try{
            Statement stmt;
            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery( "SELECT * FROM "+DBHandler.CATEGORY+" WHERE "+NAME+" = '"+name+"';" );

            if ( rs.next() ) {
                int pId = rs.getInt(ID);
                String  pName = rs.getString(NAME);
                int mainCategory= rs.getInt(SUB_CATEGORY_ID);
                CategoryDTO main=null;
                if(mainCategory!=0)
                    main=getCategoryById(c,mainCategory);
                category=new CategoryDTO(pId, pName, main);
            }
            rs.close();
            if(!c.isClosed()) {
                stmt.close();
                c.close();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return category;
    }
}
