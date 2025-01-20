package main;

import controller.BuildingController;
import controller.BuildingControllerInterface;
import view.BuildingView;
import view.BuildingViewInterface;

/**
 * The main class for the building elevator system.
 */
public class Main {

  /**
   * The main method for the building elevator system.
   *
   * @param args the command line arguments
   */
  public static void main(String[] args) {
    if (args.length != 0 && args.length != 3) {
      System.out.println(
          "Usage: java -jar BuildingElevatorSystem.jar <floors> <elevators> <capacity>"
      );
      System.exit(1);
    }

    BuildingViewInterface view = new BuildingView("Building Elevator System");
    BuildingControllerInterface controller = new BuildingController(view);

    if (args.length == 3) {
      try {
        int floors = Integer.parseInt(args[0]);
        int elevators = Integer.parseInt(args[1]);
        int capacity = Integer.parseInt(args[2]);
        controller.setBuilding(floors, elevators, capacity);
      } catch (IllegalArgumentException e) {
        System.out.println(e.getMessage());
        System.exit(1);
      }
    }
  }
}
