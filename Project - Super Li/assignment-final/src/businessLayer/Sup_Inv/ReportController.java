package businessLayer.Sup_Inv;

import java.util.Date;
import java.util.List;

public class ReportController {
    /*public static void printReport(PrintStream out,Report report){
        if(report.getType().equals(Report.Type.ISSUE))
            printIssueReport(out,report);
        else if(report.getType().equals(Report.Type.FLAWS))
            printFlawsReport(out,report);
        else if(report.getType().equals(Report.Type.INVENTORY))
            printInventoryReport(out,report);
        else if(report.getType().equals(Report.Type.SHOTRAGES))
            printShortagesReport(out,report);
    }*/
    private static ReportController instance = null;

    private ReportController(){

    }

    public static ReportController getInstance(){
        if (instance == null){
            instance = new ReportController();
        }
        return instance;
    }

    public String getReport(Report report){
        if(report.getType().equals(Report.Type.ISSUE))
          return getIssueReport(report);
        else if(report.getType().equals(Report.Type.FLAWS))
           return getFlawsReport(report);
        else if(report.getType().equals(Report.Type.INVENTORY))
            return getInventoryReport(report);
        else if(report.getType().equals(Report.Type.SHOTRAGES))
            return getShortagesReport(report);
        else return "";
    }
    /*private static void printShortagesReport(PrintStream out, Report report) {
        out.println("Report by shortages:");
        List<GeneralProduct> list=report.getGeneralProductList();
        for(GeneralProduct p:list){
            out.println(p);
        }
    }*/

    private static String getShortagesReport( Report report) {
        String res = "Report by shortages:\n";
        List<GeneralProduct> list=report.getGeneralProductList();
        for(GeneralProduct p:list){
            res = res + p + "\n";
        }
        return res;
    }

    /*private static void printInventoryReport(PrintStream out, Report report) {
        out.println("Report by inventory: ");
        List<GeneralProduct> list=report.getGeneralProductList();
        for(GeneralProduct p:list){
            out.println(p);
        }
    }*/

    private static String getInventoryReport( Report report) {
        String res = "Report by inventory: \n";
        List<GeneralProduct> list=report.getGeneralProductList();
        for(GeneralProduct p:list){
            res = res + p + "\n";
        }
        return res;
    }
    /*private static void printFlawsReport(PrintStream out, Report report) {
        out.println("Report by flaws: ");
        List<SpecificProduct> list=report.getSpecificProductList();
        for(SpecificProduct p:list){
            out.println(p);
        }
    }*/
    private static String getFlawsReport(Report report) {
        String res = "Report by flaws: \n";
        List<SpecificProduct> list=report.getSpecificProductList();
        for(SpecificProduct p:list){
            res = res + p + "\n";
        }
        return res;
    }
    /*private static void printIssueReport(PrintStream out, Report report) {
        out.println("***************Issue Report******************");
        List<SpecificProduct> list=report.getSpecificProductList();
        for(SpecificProduct p:list){
            out.println(p);
        }
        out.println("*********************************************");
    }*/
    private static String getIssueReport( Report report) {
        String res = "***************Issue Report******************\n";
        List<SpecificProduct> list=report.getSpecificProductList();
        for(SpecificProduct p:list){
            res = res + p + "\n";
        }
        res = res + "*********************************************\n";
        return res;
    }

    public Report createFlawsReport(List<Category> categories) throws Exception {
        List<SpecificProduct> list= ProductController.getInstance().getAllFlawSpecificProductsWithCategories(categories);
        Report report=new Report(new Date(), Report.Type.FLAWS, null, list);
        return report;
    }
    public Report createInventoryReport(List<Category> categories) throws Exception {
        List<GeneralProduct> lst=ProductController.getInstance().getAllGeneralProductWithCategories(categories);
        Report report=new Report(new Date(), Report.Type.INVENTORY, lst, null);
        return report;
    }
    public Report createShortagesReport(List<Category> categories) throws Exception {
        List<GeneralProduct> lst=ProductController.getInstance().getLessThenMinimumGeneralProductsWithCategories(categories);
        Report report=new Report(new Date(), Report.Type.SHOTRAGES, lst, null);
        return report;
    }

}
