package building;

import building.enums.ElevatorSystemStatus;
import elevator.Elevator;
import elevator.ElevatorReport;
import java.util.LinkedList;
import java.util.List;
import scanerzus.Request;

/**
 * This class represents a building.
 */
public class Building implements BuildingInterface {
  private final int numberOfFloors;
  private final int numberOfElevators;
  private final int elevatorCapacity;
  private final Elevator[] elevators;
  private List<Request> upRequests = new LinkedList<>();
  private List<Request> downRequests = new LinkedList<>();
  private ElevatorSystemStatus systemStatus;

  /**
   * The constructor for the building.
   *
   * @param numberOfFloors    the number of floors in the building.
   * @param numberOfElevators the number of elevators in the building.
   * @param elevatorCapacity  the capacity of the elevators
   *                          in the building.
   * @throws IllegalArgumentException if the number of floors, elevators,
   *                                  or elevator capacity is less than 1.
   */
  public Building(int numberOfFloors, int numberOfElevators,
                  int elevatorCapacity) throws IllegalArgumentException {
    if (numberOfFloors <= 1) {
      throw new IllegalArgumentException("The number "
          + "of floors must be greater than 1.");
    } else if (numberOfElevators < 1 || elevatorCapacity < 1) {
      throw new IllegalArgumentException("The number of "
          + "elevators and elevator capacity must be greater than 0.");
    } else {
      this.numberOfFloors = numberOfFloors;
      this.numberOfElevators = numberOfElevators;
      this.elevatorCapacity = elevatorCapacity;
      this.elevators = new Elevator[numberOfElevators];
      initializeElevators();
    }
    this.systemStatus = ElevatorSystemStatus.outOfService;
  }

  /**
   * This method is used to initialize the elevators in the building.
   */
  private void initializeElevators() {
    for (int i = 0; i < numberOfElevators; ++i) {
      this.elevators[i] = new Elevator(numberOfFloors, this.elevatorCapacity);
    }
  }

  @Override
  public ElevatorSystemStatus getSystemStatus() {
    return this.systemStatus;
  }

  @Override
  public int getNumberOfElevators() {
    return this.numberOfElevators;
  }

  @Override
  public int getElevatorCapacity() {
    return this.elevatorCapacity;
  }

  @Override
  public int getNumberOfFloors() {
    return this.numberOfFloors;
  }

  @Override
  public Elevator[] getElevators() {
    return this.elevators;
  }

  @Override
  public boolean addRequest(Request request) throws IllegalStateException {
    if (getSystemStatus() == ElevatorSystemStatus.running) {
      if (request == null) {
        throw new IllegalArgumentException("Request cannot be null.");
      } else if (request.getStartFloor() >= 0 && request.getStartFloor() < this.numberOfFloors) {
        if (request.getEndFloor() >= 0 && request.getEndFloor() < this.numberOfFloors) {
          if (request.getStartFloor() == request.getEndFloor()) {
            return false;
          } else {
            if (request.getStartFloor() < request.getEndFloor()) {
              upRequests.add(request);
            } else {
              downRequests.add(request);
            }
            return true;
          }
        } else {
          throw new IllegalArgumentException("End floor must be between 0 and "
              + (this.numberOfFloors - 1));
        }
      } else {
        throw new IllegalArgumentException("Start floor must be between 0 and "
            + (this.numberOfFloors - 1));
      }
    } else {
      throw new IllegalStateException("Request rejected. "
          + "The elevator system is not running.");
    }
  }


  /**
   * This method is used to distribute the requests to the elevators.
   */
  private void distributeRequests() {
    if (!upRequests.isEmpty() || !downRequests.isEmpty()) {
      for (Elevator elevator : elevators) {
        if (elevator.isTakingRequests()) {
          List<Request> requestsForElevator;
          if (elevator.getCurrentFloor() == 0) {
            requestsForElevator = this.getRequests(this.upRequests);
            elevator.processRequests(requestsForElevator);
          } else if (elevator.getCurrentFloor() == this.numberOfFloors - 1) {
            requestsForElevator = this.getRequests(this.downRequests);
            elevator.processRequests(requestsForElevator);
          }
        }
      }
    }
  }

  /**
   * This method is used to get the requests for the specific elevator.
   *
   * @param requests the request list for all elevators.
   * @return the requests for a specific elevator.
   */
  private List<Request> getRequests(List<Request> requests) {
    List<Request> requestsForElevator = new LinkedList<>();
    while (!requests.isEmpty() && requestsForElevator.size() < this.elevatorCapacity) {
      requestsForElevator.add(requests.remove(0));
    }
    return requestsForElevator;
  }

  @Override
  public void step() {
    if (this.systemStatus != ElevatorSystemStatus.outOfService) {
      if (this.systemStatus != ElevatorSystemStatus.stopping) {
        this.distributeRequests();
      }

      for (Elevator elevator : elevators) {
        elevator.step();
      }

      if (this.systemStatus == ElevatorSystemStatus.stopping) {
        boolean checkElevatorsOnGroundFloor = true;

        for (Elevator elevator : elevators) {
          if (elevator.getCurrentFloor() != 0 || elevator.isDoorClosed()) {
            checkElevatorsOnGroundFloor = false;
            break;
          }
        }

        if (checkElevatorsOnGroundFloor) {
          this.systemStatus = ElevatorSystemStatus.outOfService;
        }
      }
    }
  }

  @Override
  public boolean startElevatorSystem() throws IllegalStateException {
    if (this.systemStatus != ElevatorSystemStatus.running) {
      if (this.systemStatus == ElevatorSystemStatus.stopping) {
        throw new IllegalStateException("Elevator cannot be started when it is stopping.");
      } else {
        for (Elevator elevator : elevators) {
          elevator.start();
        }
        this.systemStatus = ElevatorSystemStatus.running;
        return true;
      }
    }
    return false;
  }

  @Override
  public void stopElevatorSystem() {
    if (this.systemStatus != ElevatorSystemStatus.outOfService
        && this.systemStatus != ElevatorSystemStatus.stopping) {
      for (Elevator elevator : elevators) {
        elevator.takeOutOfService();
      }
      this.systemStatus = ElevatorSystemStatus.stopping;
      this.upRequests.clear();
      this.downRequests.clear();
    }
  }

  @Override
  public BuildingReport getElevatorSystemStatus() {
    ElevatorReport[] elevatorReports = new ElevatorReport[this.elevators.length];

    for (int i = 0; i < this.elevators.length; ++i) {
      elevatorReports[i] = this.elevators[i].getElevatorStatus();
    }

    return new BuildingReport(this.numberOfFloors, this.numberOfElevators,
        this.elevatorCapacity, elevatorReports, this.upRequests, this.downRequests,
        this.systemStatus);
  }
}


