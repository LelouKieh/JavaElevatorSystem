package view;

import building.BuildingReport;
import building.enums.ElevatorSystemStatus;
import controller.BuildingControllerInterface;
import elevator.ElevatorReport;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import scanerzus.Request;

/**
 * The view class for the building.
 */
public class BuildingView extends JFrame implements BuildingViewInterface {
  private JFrame frame;
  private JTextArea statusTextArea;
  private JButton startButton;
  private JButton stopButton;
  private JButton stepButton;
  private JButton requestButton;
  private JTextField startFloorField;
  private JTextField endFloorField;
  private BuildingControllerInterface controller;
  private BuildingGridPanel buildingGridPanel;
  private JPanel buildingInfoPanel;
  private JSlider floorsSlider;
  private JSlider elevatorsSlider;
  private JSlider capacitySlider;
  private final JPanel panel;
  private JPanel startPanel;
  private JPanel mainPanel;
  private final CardLayout panelLayout;
  private ElevatorSystemStatus status = ElevatorSystemStatus.outOfService;
  private JLabel statusLabel;
  private String requestsText;
  private boolean[][] elevatorFloorRequests;

  /**
   * The constructor for the building view.
   *
   * @param title the title of the building view
   */
  public BuildingView(String title) {
    super(title);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    panelLayout = new CardLayout();
    panel = new JPanel(panelLayout);
    add(panel);

    setUpStartPanel();
    setUpMainPanel();
    panel.add(startPanel, "start");
    panel.add(mainPanel, "main");

    pack();
    setLocationRelativeTo(null);
    setVisible(true);
  }

  /**
   * Set up the start panel.
   */
  private void setUpStartPanel() {
    startPanel = new JPanel();
    startPanel.setLayout(new BoxLayout(startPanel, BoxLayout.Y_AXIS));
    startPanel.setPreferredSize(new Dimension(1000, 600));

    JPanel menuPanel = new JPanel(new GridLayout(3, 2));
    JLabel floorsLabel = new JLabel("Number of Floors:");
    int numOfFloors = 11;
    floorsSlider = createSlider(3, 30, numOfFloors);
    menuPanel.add(floorsLabel);
    menuPanel.add(floorsSlider);
    JLabel elevatorsLabel = new JLabel("Number of Elevators:");
    int numOfElevators = 8;
    elevatorsSlider = createSlider(1, 20, numOfElevators);
    menuPanel.add(elevatorsLabel);
    menuPanel.add(elevatorsSlider);
    JLabel capacityLabel = new JLabel("Elevator Capacity:");
    int elevatorCapacity = 3;
    capacitySlider = createSlider(3, 20, elevatorCapacity);
    menuPanel.add(capacityLabel);
    menuPanel.add(capacitySlider);

    JPanel buttonPanel = new JPanel();
    JButton setButton = new JButton("Set Building");
    buttonPanel.add(setButton);
    setButton.addActionListener(e -> setUpBuilding());

    JLabel title = new JLabel("Building Elevator System");
    startPanel.add(title);
    startPanel.add(menuPanel);
    startPanel.add(buttonPanel);
  }

  /**
   * Set up the main panel.
   */
  private void setUpMainPanel() {
    setResizable(true);
    mainPanel = new JPanel();
    mainPanel.setLayout(new BorderLayout());
    setUpInputPanel();
    setUpButtonPanel();
  }

  /**
   * Set up the input panel in the main panel.
   */
  private void setUpInputPanel() {
    JPanel inputPanel = new JPanel(new FlowLayout());

    inputPanel.add(new JLabel("Start Floor:"));
    startFloorField = new JTextField(5);
    inputPanel.add(startFloorField);
    inputPanel.add(new JLabel("End Floor:"));
    endFloorField = new JTextField(5);
    inputPanel.add(endFloorField);

    requestButton = new JButton("Make Request");
    inputPanel.add(requestButton);

    mainPanel.add(inputPanel, BorderLayout.SOUTH);
  }

  /**
   * Set up the building info panel.
   */
  private void setUpBuildingInfoPanel() {
    buildingInfoPanel = new JPanel();
  }

  /**
   * Update the building info panel in the view.
   */
  private void updateBuildingInfoPanel() {
    int numOfFloors = controller.updateBuildingFloors();
    buildingInfoPanel.add(new JLabel("Number of Floors: " + numOfFloors + "  "));
    int numOfElevators = controller.updateBuildingElevators();
    buildingInfoPanel.add(new JLabel("Number of Elevators: " + numOfElevators + "  "));
    int elevatorCapacity = controller.updateElevatorCapacity();
    buildingInfoPanel.add(new JLabel("Elevator Capacity: " + elevatorCapacity + "  "));
    statusLabel = new JLabel("Elevator System Status: " + status.toString() + "  ");
    buildingInfoPanel.add(statusLabel);
  }

  /**
   * Update the system status in the view.
   */
  private void updateSystemStatus() {
    status = controller.updateSystemStatus();
    statusLabel.setText("Elevator System Status: " + status.toString());
  }

  /**
   * Set up the split pane in the main panel.
   */
  private void setUpSplitPane() {
    JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
    splitPane.setDividerLocation(200);
    JPanel leftPanel = new JPanel(new BorderLayout());
    JPanel rightPanel = new JPanel(new BorderLayout());
    splitPane.setLeftComponent(leftPanel);
    splitPane.setRightComponent(rightPanel);

    statusTextArea = new JTextArea();
    statusTextArea.setEditable(false);
    JScrollPane scrollPane = new JScrollPane(statusTextArea);
    scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
    leftPanel.add(scrollPane, BorderLayout.CENTER);
    rightPanel.add(buildingGridPanel, BorderLayout.CENTER);

    mainPanel.add(splitPane, BorderLayout.CENTER);
  }

  /**
   * Set up the button panel in the main panel.
   */
  private void setUpButtonPanel() {
    JPanel buttonPanel = new JPanel();
    buttonPanel.setLayout(new FlowLayout());
    startButton = new JButton("Start");
    stopButton = new JButton("Stop");
    stepButton = new JButton("Step");
    setUpBuildingInfoPanel();
    buttonPanel.add(buildingInfoPanel);
    buttonPanel.add(startButton);
    buttonPanel.add(stopButton);
    buttonPanel.add(stepButton);

    mainPanel.add(buttonPanel, BorderLayout.NORTH);
  }

  /**
   * Set up the building with the slides' values.
   */
  private void setUpBuilding() {
    try {
      int numOfFloors = floorsSlider.getValue();
      int numOfElevators = elevatorsSlider.getValue();
      int elevatorCapacity = capacitySlider.getValue();
      controller.setBuilding(numOfFloors, numOfElevators, elevatorCapacity);
    } catch (IllegalArgumentException ex) {
      displayErrorMessage(ex.getMessage());
    }
  }

  /**
   * Update the requests lists in the view.
   *
   * @param buildingReport the building report
   */
  private void updateRequestsLists(BuildingReport buildingReport) {
    List<Request> upRequests = buildingReport.getUpRequests();
    List<Request> downRequests = buildingReport.getDownRequests();
    StringBuilder upRequestList = new StringBuilder();
    for (Request request : upRequests) {
      upRequestList.append(request.toString()).append("\n");
    }
    StringBuilder downRequestList = new StringBuilder();
    for (Request request : downRequests) {
      downRequestList.append(request.toString()).append("\n");
    }
    requestsText = "Up Requests:\n" + upRequestList
        + "\nDown Requests:\n" + downRequestList;
  }

  @Override
  public void setBuildingView(int numberOfFloors, int numberOfElevators) {
    updateBuildingInfoPanel();
    buildingGridPanel = new BuildingGridPanel(controller.updateBuildingFloors(),
        controller.updateBuildingElevators());
    setUpSplitPane();
    panelLayout.show(panel, "main");
    updateView();
  }

  /**
   * Create a slider with the given max and initial value.
   *
   * @param max          the maximum value of the slider
   * @param initialValue the initial value of the slider
   * @return the slider
   */
  private JSlider createSlider(int min, int max, int initialValue) {
    JSlider slider = new JSlider(JSlider.HORIZONTAL, min, max, initialValue);
    slider.setMajorTickSpacing(2);
    slider.setPaintTicks(true);
    slider.setPaintLabels(true);
    return slider;
  }

  @Override
  public void setController(BuildingControllerInterface controller) {
    this.controller = controller;
  }

  @Override
  public void updateView() {
    updateSystemStatus();
    updateRequestsLists(controller.updateStatus());
    statusTextArea.setText(requestsText);
    buildingGridPanel.updateElevatorPosition();
    buildingGridPanel.repaint();
  }

  @Override
  public void addStartButtonListener(ActionListener listener) {
    startButton.addActionListener(listener);
  }

  @Override
  public void addStopButtonListener(ActionListener listener) {
    stopButton.addActionListener(listener);
  }

  @Override
  public void addStepButtonListener(ActionListener listener) {
    stepButton.addActionListener(listener);
  }

  @Override
  public void displayErrorMessage(String message) {
    JOptionPane.showMessageDialog(frame, message, "Error", JOptionPane.ERROR_MESSAGE);
  }

  @Override
  public void addRequestButtonListener(ActionListener listener) {
    requestButton.addActionListener(listener);
  }

  /**
   * Get the start floor from the text field.
   *
   * @return the start floor
   */
  public String getStartFloor() {
    return startFloorField.getText();
  }

  /**
   * Get the end floor from the text field.
   *
   * @return the end floor
   */
  public String getEndFloor() {
    return endFloorField.getText();
  }

  /**
   * Clear the status text of the user input to "".
   */
  public void clearStatusText() {
    startFloorField.setText("");
    endFloorField.setText("");
  }

  /**
   * The panel for the building grid.
   */
  public class BuildingGridPanel extends JPanel {
    private final int numOfFloors;
    private final int numOfElevators;
    private boolean[][] elevatorFloors;
    private boolean[] elevatorDoorClose;
    private boolean[] elevatorOutOfService;
    private JLabel[][] elevatorLabel;

    /**
     * The constructor for the building grid panel.
     *
     * @param numOfFloors    the number of floors
     * @param numOfElevators the number of elevators
     */
    public BuildingGridPanel(int numOfFloors, int numOfElevators) {
      this.numOfFloors = numOfFloors;
      this.numOfElevators = numOfElevators;
      this.elevatorFloors = new boolean[numOfElevators][numOfFloors];
      this.elevatorLabel = new JLabel[numOfElevators][numOfFloors];
      setLayout(new GridLayout(numOfFloors, numOfElevators));

      for (int elevator = 0; elevator < numOfElevators; elevator++) {
        for (int floor = 0; floor < numOfFloors; floor++) {
          elevatorFloors[elevator][floor] = false;
          elevatorLabel[elevator][floor] = new JLabel("");
        }
      }
      for (int elevator = 0; elevator < numOfElevators; elevator++) {
        for (int floor = numOfFloors - 1; floor >= 0; floor--) {
          JPanel square = new JPanel();
          square.setBorder(BorderFactory.createLineBorder(Color.BLACK));
          add(square);
        }
      }
      setPreferredSize(new Dimension(50 * numOfElevators, 50 * numOfFloors));
    }

    /**
     * Update the elevator position.
     */
    public void updateElevatorPosition() {
      int[] elevatorCurrentFloors = controller.updateElevatorCurrentFloors();
      for (int elevator = 0; elevator < numOfElevators; elevator++) {
        for (int floor = 0; floor < numOfFloors; floor++) {
          elevatorFloors[elevator][floor] = (floor == elevatorCurrentFloors[elevator]);
        }
      }
    }

    /**
     * Update the elevator status with the elevator reports.
     */
    public void updateElevatorStatus() {
      ElevatorReport[] elevatorReports = controller.updateElevatorReports();
      elevatorOutOfService = new boolean[numOfElevators];
      elevatorDoorClose = new boolean[numOfElevators];
      elevatorFloorRequests = new boolean[numOfElevators][];
      for (int elevator = 0; elevator < numOfElevators; elevator++) {
        elevatorDoorClose[elevator] = elevatorReports[elevator].isDoorClosed();
        elevatorOutOfService[elevator] = elevatorReports[elevator].isOutOfService();
        elevatorFloorRequests[elevator] = elevatorReports[elevator].getFloorRequests();
      }
    }

    @Override
    protected void paintComponent(Graphics g) {
      super.paintComponent(g);
      updateElevatorStatus();

      for (int elevator = 0; elevator < numOfElevators; elevator++) {
        ElevatorReport elevatorReport = controller.updateElevatorReports()[elevator];
        for (int floor = 0; floor < numOfFloors; floor++) {
          JPanel square = (JPanel) getComponent((numOfFloors - floor - 1)
              * numOfElevators + elevator);
          configureSquare(square, elevator, floor, elevatorReport);
        }
      }
    }

    /**
     * Configure the square with the elevator, floor, and elevator report.
     *
     * @param square          the square to configure
     * @param elevator        the elevator
     * @param floor           the floor
     * @param elevatorReport  the elevator report
     */
    private void configureSquare(JPanel square, int elevator,
                                 int floor, ElevatorReport elevatorReport) {
      elevatorLabel[elevator][floor].setHorizontalAlignment(SwingConstants.CENTER);

      Color color = Color.WHITE; // Default color
      if (elevatorFloorRequests[elevator][floor]) {
        color = Color.GRAY;
      }
      if (elevatorFloors[elevator][floor]) {
        if (elevatorOutOfService[elevator]) {
          color = Color.RED;
        } else if (elevatorDoorClose[elevator]) {
          color = Color.YELLOW;
        } else {
          color = Color.GREEN;
        }

        if (elevatorReport.getEndWaitTimer() > 0) {
          elevatorLabel[elevator][floor].setText("" + elevatorReport.getEndWaitTimer());
        } else if (elevatorReport.getDoorOpenTimer() > 0) {
          elevatorLabel[elevator][floor].setText("" + elevatorReport.getDoorOpenTimer());
        } else {
          elevatorLabel[elevator][floor].setText(elevatorReport.getDirection().toString());
        }
      } else {
        elevatorLabel[elevator][floor].setText("");
      }

      square.setBackground(color);
      square.removeAll();
      square.add(elevatorLabel[elevator][floor]);
    }
  }
}
