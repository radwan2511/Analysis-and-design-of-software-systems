package businessLayer.Sup_Inv;


public class Category {
    private int id;
    private String name;


    private Category supCategory;
    public int getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public Category getSupCategory() {
        return supCategory;
    }
    public String supCategory() {
        if(supCategory==null)
            return "NULL";
        return ""+supCategory.getId();
    }
    public Category(int id, String name, Category supCategpry) {
        super();
        this.id = id;
        this.name = name;
        this.supCategory = supCategpry;
    }

    public Category(String name, Category main) {
        super();
        this.name = name;
        this.supCategory = main;
    }

    @Override
    public String toString() {
        String supCategpryStr = "";
        if(this.supCategory != null)
            supCategpryStr = "superCategpry= " + supCategory;
        return "Category:\nid=" + id + "\nname=" + name + "\n" + supCategpryStr;
    }



}
