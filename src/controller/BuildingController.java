package controller;

import building.Building;
import building.BuildingInterface;
import building.BuildingReport;
import building.enums.ElevatorSystemStatus;
import elevator.Elevator;
import elevator.ElevatorReport;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import scanerzus.Request;
import view.BuildingViewInterface;

/**
 * The controller class for the building.
 */
public class BuildingController implements BuildingControllerInterface {
  private BuildingInterface model;
  private final BuildingViewInterface view;

  /**
   * The constructor for the building controller.
   *
   * @param view the view of building
   */
  public BuildingController(BuildingViewInterface view) {
    this.view = view;
    view.setController(this);
  }

  @Override
  public void setBuilding(int numOfFloors, int numOfElevators, int elevatorCapacity) {
    try {
      model = new Building(numOfFloors, numOfElevators, elevatorCapacity);
      view.setBuildingView(numOfFloors, numOfElevators);
      view.addStartButtonListener(new StartButtonListener());
      view.addStopButtonListener(new StopButtonListener());
      view.addStepButtonListener(new StepButtonListener());
      view.addRequestButtonListener(new RequestButtonListener());
    } catch (IllegalArgumentException ex) {
      throw new IllegalArgumentException(ex.getMessage());
    }
  }

  @Override
  public BuildingReport updateStatus() {
    return model.getElevatorSystemStatus();
  }

  /**
   * Get the elevators in the building.
   *
   * @return an array of the elevators in the building
   */
  private Elevator[] updateElevators() {
    return model.getElevators();
  }

  @Override
  public int[] updateElevatorCurrentFloors() {
    Elevator[] elevators = updateElevators();
    int[] elevatorCurrentFloors = new int[elevators.length];
    for (int i = 0; i < elevators.length; i++) {
      int elevatorCurrentFloor = elevators[i].getCurrentFloor();
      elevatorCurrentFloors[i] = elevatorCurrentFloor;
    }
    return elevatorCurrentFloors;
  }

  @Override
  public ElevatorReport[] updateElevatorReports() {
    List<ElevatorReport> reportList = new ArrayList<>();
    for (Elevator elevator : model.getElevators()) {
      reportList.add(elevator.getElevatorStatus());
    }
    return reportList.toArray(new ElevatorReport[0]);
  }

  @Override
  public int updateBuildingFloors() {
    return model.getNumberOfFloors();
  }

  @Override
  public int updateBuildingElevators() {
    return model.getNumberOfElevators();
  }

  @Override
  public int updateElevatorCapacity() {
    return model.getElevatorCapacity();
  }

  @Override
  public ElevatorSystemStatus updateSystemStatus() {
    return model.getSystemStatus();
  }

  /**
   * The listener for the start button.
   * When the building is stopping, display error message.
   * When the building is ready to start, start the building and display the view.
   */
  private class StartButtonListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
      if (model.getSystemStatus() == ElevatorSystemStatus.running) {
        view.displayErrorMessage("The building is already running.");
      }
      try {
        model.startElevatorSystem();
        view.updateView();
      } catch (IllegalStateException ex) {
        view.displayErrorMessage(ex.getMessage());
      }
    }
  }

  /**
   * The listener for the stop button.
   * When the building is stopping, display error message.
   * When the building is running, stop the building and update the view.
   */
  private class StopButtonListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
      if (model.getSystemStatus() == ElevatorSystemStatus.outOfService) {
        view.displayErrorMessage("The building is already stopped.");
      }
      if (model.getSystemStatus() == ElevatorSystemStatus.stopping) {
        view.displayErrorMessage("The building is stopping.");
      }
      model.stopElevatorSystem();
      view.updateView();
    }
  }

  /**
   * The listener for the step button.
   * When the building is out of service, display error message.
   * When the building is running or stopping, step the building and update the view.
   */
  private class StepButtonListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
      if (model.getSystemStatus() == ElevatorSystemStatus.outOfService) {
        view.displayErrorMessage("The building has not been started.");
      }
      model.step();
      view.updateView();
    }
  }

  /**
   * The listener for the request button.
   * When the building is out of service, display error message.
   * When the building is running or stopping, add the request to the building and update the view.
   */
  private class RequestButtonListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
      try {
        String startFloor = view.getStartFloor();
        String endFloor = view.getEndFloor();

        int start = Integer.parseInt(startFloor);
        int end = Integer.parseInt(endFloor);
        if (start == end) {
          view.displayErrorMessage("The start and end floors cannot be the same.");
        }
        model.addRequest(new Request(start, end));
        view.clearStatusText();
        view.updateView();
      } catch (NumberFormatException ex) {
        view.displayErrorMessage("Please enter a valid floor number.");
      } catch (IllegalStateException | IllegalArgumentException ex) {
        view.displayErrorMessage(ex.getMessage());
      }
    }
  }
}

