package businessLayer.employeePackage;

public abstract class Employee {
    private String name;
    private String id;
    private boolean isShiftManager;

    public Employee(String name, String id,boolean isShiftManager){
        this.name = name;
        this.id = id;
        isShiftManager=isShiftManager;

    }

    private void checkId(String id){
        if(id.length() == 9){
            for(int i = 0; i < id.length(); i++){
                if(id.charAt(i) < '0' || id.charAt(i) > '9')
                    throw new IllegalArgumentException("Illegal Id number");
            }
        }
        else{
            throw new IllegalArgumentException("Illegal Id number");
        }
    }

    private void checkName(String name){
        if(name.equals(""))
            throw new IllegalArgumentException("Illegal name");
    }

    public String getId(){
        return id;
    }

    public String getName(){
        return name;
    }
    public boolean getIsShiftManager(){return isShiftManager;}


}
