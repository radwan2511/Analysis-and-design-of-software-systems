package businessLayer.deliveryPackage;

public class Response<T> {
    private String msg;
    private T value;

    public Response() {}

    public Response(String msg){
        this.msg = msg;
    }

    public Response(T val) {
        value = val;
    }

    public T getValue() {
        return value;
    }

    public String getMsg(){
        return msg;
    }

    public boolean errorOccurred() {
        return msg != null;
    }
}
