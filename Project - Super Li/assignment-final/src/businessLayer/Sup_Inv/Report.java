package businessLayer.Sup_Inv;

import java.util.Date;
import java.util.List;

public class Report {
    public static enum Type{ISSUE,SHOTRAGES,INVENTORY,FLAWS};
    private Date creationDate;
    private Type type;
    private List<GeneralProduct> generalProductList;
    private List<SpecificProduct> specificProductList;
    public Date getCreationDate() {
        return creationDate;
    }
    public Type getType() {
        return type;
    }
    /**
     *
     * @param creationDate
     * @param type
     * @param generalProductList - can be null
     * @param specificProductList -can be null
     */
    public Report(Date creationDate, Type type,List<GeneralProduct> generalProductList,List<SpecificProduct> specificProductList) {
        super();
        this.creationDate = creationDate;
        this.type = type;
        this.generalProductList=generalProductList;
        this.specificProductList=specificProductList;
    }

    public List<GeneralProduct> getGeneralProductList() {
        return generalProductList;
    }
    public List<SpecificProduct> getSpecificProductList() {
        return specificProductList;
    }
    @Override
    public String toString() {
        return "Report [creationDate=" + creationDate + ", type=" + type.name() + "]";
    }


}
