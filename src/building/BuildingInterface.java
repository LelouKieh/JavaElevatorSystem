package building;

import building.enums.ElevatorSystemStatus;
import elevator.Elevator;
import scanerzus.Request;

/**
 * This interface is used to represent a building.
 */
public interface BuildingInterface {

  /**
   * Elevator system status getter.
   *
   * @return the elevator system status.
   */
  ElevatorSystemStatus getSystemStatus();

  /**
   * This method is used to get the number of elevators in the building.
   *
   * @return the number of elevators in the building
   */
  int getNumberOfElevators();

  /**
   * This method is used to get the elevator capacity.
   *
   * @return the elevator capacity
   */
  int getElevatorCapacity();

  /**
   * This method is used to get the number of floors in the building.
   *
   * @return the number of floors in the building
   */
  int getNumberOfFloors();

  /**
   * This method is used to add a request to the building.
   *
   * @param request the request to be added to the building.
   * @return true if the request was added successfully, false otherwise.
   * @throws IllegalStateException if the elevator system is not running.
   */
  boolean addRequest(Request request) throws IllegalStateException;

  /**
   * Moves all the elevators in the building by one floor.
   * The elevator is going to move by one floor in the direction it is currently moving.
   * If the elevator is stopped, it will not move.
   * If the elevator arrives at a floor where it is supposed to stop then it will open
   * its doors and let people out.
   * The elevator will stop for 3 steps then it will close its doors and move on.
   * If the elevator arrives at the top floor, it will wait for 5 steps then go down.
   * If the elevator arrives at the bottom floor, it will wait for 5 steps then go up.
   */
  void step();

  /**
   * This method is used to start the elevator system.
   *
   * @return true if the elevator system was started successfully, false otherwise.
   * @throws IllegalStateException if the elevator system is stopping.
   */
  boolean startElevatorSystem() throws IllegalStateException;

  /**
   * This method is used to stop the elevator system.
   */
  void stopElevatorSystem();

  /**
   * This method is used to get the building report.
   *
   * @return the building report.
   */
  BuildingReport getElevatorSystemStatus();

  /**
   * This method is used to get the elevators in the building.
   *
   * @return the elevators in the building.
   */
  Elevator[] getElevators();
}
