package main;

import building.Building;
import building.BuildingInterface;
import building.BuildingReport;
import java.util.Scanner;
import scanerzus.Request;

/**
 * The driver for the elevator system.
 * This class will create the elevator system and run it.
 * this is for testing the elevator system.
 * <p>
 * It provides a user interface to the elevator system.
 */
public class MainConsole {

  /**
   * The main method for the elevator system.
   * This method creates the elevator system and runs it.
   *
   * @param args the command line arguments
   */
  public static void main(String[] args) {

    // the number of floors, the number of elevators, and the number of people.

    final int numFloors = 11;
    final int numElevators = 8;
    final int numPeople = 3;

    String[] introText = {
        "Welcome to the Elevator System!",
        "This system will simulate the operation of an elevator system.",
        "The system will be initialized with the following parameters:",
        "Number of floors: " + numFloors,
        "Number of elevators: " + numElevators,
        "Number of people: " + numPeople,
        "The system will then be run and the results will be displayed.",
        "",
        "Press enter to continue."
    };

    for (String line : introText) {
      System.out.println(line);
    }
    Scanner scanner = new Scanner(System.in);
    scanner.nextLine();
    BuildingInterface building = new Building(numFloors, numElevators, numPeople);

    boolean running = true;
    while (running) {
      System.out.println("[start] Start the building"
          + "\n[s steps] Run steps times [CR] one step"
          + "\n[r start end] make a request "
          + "\n[h] halt building [c] continue building [q] quit >\n");

      System.out.print("\nEnter your command: ");
      String input = scanner.nextLine().trim();

      String[] parts = input.split(" ");
      String command = parts[0];

      switch (command) {
        case "c":
          try {
            building.startElevatorSystem();
          } catch (IllegalStateException e) {
            System.out.println(e.getMessage());
            System.out.println("Press enter to continue");
            scanner.nextLine();
          }
          break;
        case "r":
          try {
            if (parts.length == 3) {
              int start = Integer.parseInt(parts[1]);
              int destination = Integer.parseInt(parts[2]);
              building.addRequest(new Request(start, destination));
            } else {
              System.out.println("Invalid command. Please provide start and destination floors.");
            }
          } catch (IllegalStateException e) {
            System.out.println(e.getMessage());
            System.out.println("Press enter to continue");
            scanner.nextLine();
          }
          break;
        case "s":
          if (parts.length > 1) {
            int steps = Integer.parseInt(parts[1]);
            for (int i = 0; i < steps; i++) {
              building.step();
            }
          } else {
            System.out.println("Invalid command. Please specify the number of steps.");
          }
          break;
        case "start":
          building.startElevatorSystem();
          break;
        case "h":
          building.stopElevatorSystem();
          break;
        case "q":
          running = false;
          break;
        default:
          System.out.println("Invalid command. Please try again.");
      }

      // Print building status after each command
      BuildingReport report = building.getElevatorSystemStatus();
      System.out.println(report.toString());
    }
    scanner.close();
  }
}
