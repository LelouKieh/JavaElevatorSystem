package building;

import building.enums.ElevatorSystemStatus;
import elevator.ElevatorReport;
import java.util.List;
import scanerzus.Request;


/**
 * This is the reporting class for the building.
 */
public class BuildingReport {
  private final int numFloors;
  private final int numElevators;
  private final int elevatorCapacity;
  private ElevatorReport[] elevatorReports;
  private List<Request> upRequests;
  private List<Request> downRequests;
  private ElevatorSystemStatus systemStatus;

  /**
   * This constructor is used to create a new BuildingReport object.
   *
   * @param numFloors        The number of floors in the building.
   * @param numElevators     The number of elevators in the building.
   * @param elevatorCapacity The capacity of the elevators.
   * @param elevatorsReports The status of the elevators.
   * @param upRequests       The up requests for the elevators.
   * @param downRequests     The down requests for the elevators.
   * @param systemStatus     The status of the elevator system.
   */
  public BuildingReport(int numFloors,
                        int numElevators,
                        int elevatorCapacity,
                        ElevatorReport[] elevatorsReports,
                        List<Request> upRequests,
                        List<Request> downRequests,
                        ElevatorSystemStatus systemStatus) {
    this.numFloors = numFloors;
    this.numElevators = numElevators;
    this.elevatorCapacity = elevatorCapacity;
    this.elevatorReports = elevatorsReports;
    this.upRequests = upRequests;
    this.downRequests = downRequests;
    this.systemStatus = systemStatus;
  }

  /**
   * This method is used to get the number of floors in the building.
   *
   * @return the number of floors in the building
   */
  public int getNumFloors() {
    return this.numFloors;
  }

  /**
   * This method is used to get the number of elevators in the building.
   *
   * @return the number of elevators in the building
   */
  public int getNumElevators() {
    return this.numElevators;
  }

  /**
   * This method is used to get the max occupancy of the elevator.
   *
   * @return the max occupancy of the elevator.
   */
  public int getElevatorCapacity() {
    return this.elevatorCapacity;
  }

  /**
   * This method is used to get the status of the elevators.
   *
   * @return the status of the elevators.
   */
  public ElevatorReport[] getElevatorReports() {
    return this.elevatorReports;
  }

  /**
   * This method is used to get the up requests for the elevators.
   *
   * @return the requests for the elevators.
   */
  public List<Request> getUpRequests() {
    return this.upRequests;
  }

  /**
   * This method is used to get the down requests for the elevators.
   *
   * @return the requests for the elevators.
   */
  public List<Request> getDownRequests() {
    return this.downRequests;
  }

  /**
   * This method is used to get the status of the elevator system.
   *
   * @return the status of the elevator system.
   */
  public ElevatorSystemStatus getSystemStatus() {
    return this.systemStatus;
  }

  /**
   * Returns a string representation of the building report.
   *
   * @return a string representation of the building report.
   */
  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    builder.append("Building Report:\n");
    builder.append("Number of Floors: ").append(numFloors).append("\n");
    builder.append("Number of Elevators: ").append(numElevators).append("\n");
    builder.append("Elevator Capacity: ").append(elevatorCapacity).append("\n");

    builder.append("Elevator Reports:\n");
    for (ElevatorReport report : elevatorReports) {
      builder.append(report.toString()).append("\n");
    }

    builder.append("Up Requests:\n");
    for (Request request : upRequests) {
      builder.append(request.toString()).append("\n");
    }

    builder.append("Down Requests:\n");
    for (Request request : downRequests) {
      builder.append(request.toString()).append("\n");
    }

    builder.append("System Status: ").append(systemStatus).append("\n");
    return builder.toString();
  }

}
