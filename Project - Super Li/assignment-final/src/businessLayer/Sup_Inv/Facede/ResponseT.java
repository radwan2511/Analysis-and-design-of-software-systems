package businessLayer.Sup_Inv.Facede;

public class ResponseT <T> extends Response{
    public T value;
    public ResponseT(String msg) {
        super(msg);
        this.value = null;
    }
    public ResponseT(T value) {
        super(null);
        this.value = value;
    }
    public ResponseT(String msg,T value){
        super(msg);
        this.value = value;
    }

}


