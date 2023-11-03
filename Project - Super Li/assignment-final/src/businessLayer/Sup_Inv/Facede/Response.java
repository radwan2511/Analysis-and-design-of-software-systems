package businessLayer.Sup_Inv.Facede;

public class Response {

    public String ErrorMessage;
    private boolean ErrorOccured;
    public Response() {ErrorOccured = true;  }

    public Response(String msg) {
        this.ErrorMessage = msg;
        this.ErrorOccured = msg!=null;
    }

    public boolean ErrorOccured() {
        return ErrorMessage != null;
    }
}
