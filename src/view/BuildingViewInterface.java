package view;

import controller.BuildingControllerInterface;
import java.awt.event.ActionListener;

/**
 * The interface for the building view.
 */
public interface BuildingViewInterface {
  /**
   * Set the controller for the view.
   *
   * @param controller the controller for the view
   */
  void setController(BuildingControllerInterface controller);

  /**
   * Update the view of building with the controller.
   */
  void updateView();

  /**
   * Add a listener to the start button.
   *
   * @param listener the listener for the button of starting the building
   */
  void addStartButtonListener(ActionListener listener);

  /**
   * Add a listener to the stop button.
   *
   * @param listener the listener for the button of stopping the building
   */
  void addStopButtonListener(ActionListener listener);

  /**
   * Add a listener to the step button.
   *
   * @param listener the listener for the button of stepping the building
   */
  void addStepButtonListener(ActionListener listener);

  /**
   * Display an error message to the user.
   *
   * @param errorMessage the error message to display
   */
  void displayErrorMessage(String errorMessage);

  /**
   * Add a listener to the request button.
   *
   * @param listener the listener for the button of requesting the building
   */
  void addRequestButtonListener(ActionListener listener);

  /**
   * Get the start floor from user input.
   * @return the start floor string from user input.
   */
  String getStartFloor();

  /**
   * Get the end floor from user input.
   * @return the end floor string from user input.
   */
  String getEndFloor();

  /**
   * Clear the status text of user input.
   */
  void clearStatusText();

  /**
   * Set the building view with the number of floors and elevators.
   *
   * @param numberOfFloors the number of floors in the building
   * @param numberOfElevators the number of elevators in the building
   */
  void setBuildingView(int numberOfFloors, int numberOfElevators);
}
