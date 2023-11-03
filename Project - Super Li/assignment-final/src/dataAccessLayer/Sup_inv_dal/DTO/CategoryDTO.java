package dataAccessLayer.Sup_inv_dal.DTO;

public class CategoryDTO {

    public int pId;
    public String name;
    public CategoryDTO main;

    public CategoryDTO(int pId, String name, CategoryDTO main) {
        super();
        this.pId = pId;
        this.name = name;
        this.main = main;
    }
}
