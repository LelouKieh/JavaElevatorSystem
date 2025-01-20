package controller;

import building.BuildingReport;
import building.enums.ElevatorSystemStatus;
import elevator.ElevatorReport;

/**
 * The controller interface for the building.
 */
public interface BuildingControllerInterface {

  /**
   * Update the status of the building.
   *
   * @return building report representing the status of the building
   */
  BuildingReport updateStatus();

  /**
   * Update the floors of the building.
   *
   * @return the number of floors in the building
   */
  int updateBuildingFloors();

  /**
   * Update the elevators of the building.
   *
   * @return the number of elevators in the building
   */
  int updateBuildingElevators();

  /**
   * Update the current floors of the elevators.
   *
   * @return an int array of the current floors of the elevators
   */
  int[] updateElevatorCurrentFloors();

  /**
   * Set the building with the input parameter from the user.
   *
   * @param numOfFloors the number of floors in the building
   * @param numOfElevators the number of elevators in the building
   * @param elevatorCapacity the capacity of the elevators
   */
  void setBuilding(int numOfFloors, int numOfElevators, int elevatorCapacity);

  /**
   * Update the elevator reports.
   *
   * @return an array of elevator reports for all elevators
   */
  ElevatorReport[] updateElevatorReports();

  /**
   * Update the system status of the building.
   *
   * @return the system status of the building
   */
  ElevatorSystemStatus updateSystemStatus();

  /**
   * Update the elevator capacity of the building.
   *
   * @return the elevator capacity of the building
   */
  int updateElevatorCapacity();
}
