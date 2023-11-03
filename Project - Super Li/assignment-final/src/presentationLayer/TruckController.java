package presentationLayer;

import businessLayer.deliveryPackage.Facade;
import businessLayer.deliveryPackage.Response;
import businessLayer.deliveryPackage.Truck;

import java.util.List;
import java.util.Scanner;

public class TruckController {
    private Facade facade;
    private Scanner scanner;

    public TruckController() throws Exception{
        this.facade = Facade.getInstance();
        scanner = new Scanner(System.in);
    }

    public void addTruck(){
        System.out.print("\nEnter plate number: ");
        String plateNumber = scanner.next();
        System.out.print("Enter truck's model:");
        String model = scanner.next();
        System.out.print("Enter truck's net weight: ");
        double netWeight = scanner.nextDouble();
        System.out.print("Enter truck's max weight: ");
        double maxWeight = scanner.nextDouble();
        System.out.println();
        Response response = facade.addTruck(plateNumber, model, netWeight, maxWeight);
        if (response.errorOccurred())
            System.out.println("Error: " + response.getMsg());
        else {
            System.out.println("Truck has been added successfully!");
        }
    }
    public void getTruck(){
        System.out.print("\nEnter truck's plate number: ");
        String plateNumber = scanner.next();
        System.out.println();
        Response<Truck> response = facade.getTruck(plateNumber);
        if (response.errorOccurred())
            System.out.println("Error: " + response.getMsg());
        else{
            Truck truck = response.getValue();
            System.out.println("\tPlate Number: " + truck.getPlateNumber() +
                    "\t\tModel: " + truck.getModel() +
                    "\t\tNet Weight: " + truck.getNetWeight() +
                    "\t\tMax Weight: " + truck.getMaxWeight());
            System.out.println();
        }
    }

    public void viewAllTrucks(){
        Response<List<Truck>> response = facade.getAllTrucks();
        if (response.errorOccurred())
            System.out.println("Error: " + response.getMsg());
        else {
            for(Truck truck : response.getValue()){
                System.out.println("\tPlate Number: " + truck.getPlateNumber() +
                        "\t\tModel: " + truck.getModel() +
                        "\t\tNet Weight: " + truck.getNetWeight() +
                        "\t\tMax Weight: " + truck.getMaxWeight());
            }
        }
    }

    public void deleteTruck(){
        System.out.print("\nEnter plate number: ");
        String plateNumber = scanner.next();
        System.out.println();
        Response<Truck> response = facade.deleteTruck(plateNumber);
        if (response.errorOccurred())
            System.out.println("Error: " + response.getMsg());
        else
            System.out.println("Truck has been deleted successfully!");
    }
}
