package businessLayer.Sup_Inv.Facede.Objects;

public class Category {
    public int id;
    public String name;
    public businessLayer.Sup_Inv.Category supCategory;

    public Category(int id, String name, businessLayer.Sup_Inv.Category supCategory){
        super();
        this.id = id;
        this.name = name;
        this.supCategory = supCategory;
    }

    @Override
    public String toString() {
        String supCategpryStr = "";
        if(this.supCategory != null)
            supCategpryStr = "superCategpry= " + supCategory;
        return "Category:\nid=" + id + "\nname=" + name + "\n" + supCategpryStr;
    }
}
