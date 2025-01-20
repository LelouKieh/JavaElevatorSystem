package building;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import elevator.Elevator;
import java.util.Arrays;
import org.junit.BeforeClass;
import org.junit.Test;
import scanerzus.Request;

/**
 * A JUnit test class for the Building class.
 */
public class BuildingTest {
  private static Building building1;
  private static Building building2;

  /**
   * Set up the buildings in building tests.
   */
  @BeforeClass
  public static void setUpClass() {
    building1 = new Building(10, 5, 5);
    building2 = new Building(20, 10, 5);
  }

  /**
   * Test the constructor exceptions.
   * Number of floors must be greater than or equal to 2.
   */
  @Test(expected = IllegalArgumentException.class)
  public void buildingLessThan2Floors() {
    new Building(1, 5, 5);
  }

  /**
   * Test the constructor exceptions.
   * Number of elevators must be greater than 0.
   */
  @Test(expected = IllegalArgumentException.class)
  public void buildingLessThan1Elevator() {
    new Building(10, 0, 5);
  }

  /**
   * Test the constructor exceptions.
   * Number of capacity must be greater than 0.
   */
  @Test(expected = IllegalArgumentException.class)
  public void buildingLessThan1Capacity() {
    new Building(10, 5, 0);
  }

  /**
   * Test getSystemStatus method.
   */
  @Test
  public void testGetSystemStatus() {
    assertEquals("Out Of Service", building2.getSystemStatus().toString());

    building2.startElevatorSystem();
    assertEquals("Running", building2.getSystemStatus().toString());

    building2.stopElevatorSystem();
    assertEquals("Stopping", building2.getSystemStatus().toString());
  }

  /**
   * Test getNumberOfElevators().
   */
  @Test
  public void testGetNumberOfElevators() {
    assertEquals(5, building1.getNumberOfElevators());
    assertEquals(10, building2.getNumberOfElevators());
  }

  /**
   * Test getElevatorCapacity().
   */
  @Test
  public void testGetElevatorCapacity() {
    assertEquals(5, building1.getElevatorCapacity());
    assertEquals(5, building2.getElevatorCapacity());
  }

  /**
   * Test addRequest() method throws an exception
   * when adding a null request to the building.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testAddRequestNull() {
    Building building = new Building(10, 5, 5);
    building.startElevatorSystem();
    building.addRequest(null);
  }

  /**
   * Test addRequest() method throws an exception
   * when adding a request with a start floor less than 0.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testAddRequestStartFloorLessThan0() {
    Building building = new Building(10, 5, 5);
    building.startElevatorSystem();
    building.addRequest(new Request(-1, 5));
  }

  /**
   * Test addRequest() method throws an exception
   * when adding a request with an ending floor less than 0.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testAddRequestEndFloorLessThan0() {
    Building building = new Building(10, 5, 5);
    building.startElevatorSystem();
    building.addRequest(new Request(5, -1));
  }

  /**
   * Test addRequest() method throws an exception
   * when adding a request with an ending floor out of bounds.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testAddRequestEndFloorBeyondMax() {
    Building building = new Building(10, 5, 5);
    building.startElevatorSystem();
    building.addRequest(new Request(5, 20));
  }

  /**
   * Test addRequest() method throws an exception
   * when adding a request with a start floor out of bounds.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testAddRequestStartFloorBeyondMax() {
    Building building = new Building(10, 5, 5);
    building.startElevatorSystem();
    building.addRequest(new Request(100, 2));
  }

  /**
   * Test addRequest() method throws an exception
   * when the building is out of service.
   */
  @Test(expected = IllegalStateException.class)
  public void testAddRequestOutOfService() {
    Building building = new Building(10, 5, 5);
    building.addRequest(new Request(5, 2));
  }

  /**
   * Test addRequest() method throws an exception
   * when the building is stopping.
   */
  @Test(expected = IllegalStateException.class)
  public void testAddRequestStopping() {
    Building building = new Building(10, 5, 5);
    building.startElevatorSystem();
    building.stopElevatorSystem();
    building.addRequest(new Request(5, 2));
  }

  /**
   * Test addRequest() method adds a request to the building.
   */
  @Test
  public void testAddRequest() {
    Building building = new Building(10, 5, 5);
    building.startElevatorSystem();
    assertTrue(building.addRequest(new Request(5, 2)));
    assertTrue(building.addRequest(new Request(0, 8)));
    BuildingReport buildingReport = building.getElevatorSystemStatus();
    assertEquals("[0->8]", buildingReport.getUpRequests().toString());
    assertEquals("[5->2]", buildingReport.getDownRequests().toString());
  }

  /**
   * Test addRequest() method can not add a request with the
   * same start and end floor.
   */
  @Test
  public void testAddRequestSameStartEndFloor() {
    Building building = new Building(10, 5, 5);
    building.startElevatorSystem();
    assertFalse(building.addRequest(new Request(5, 5)));
    BuildingReport buildingReport = building.getElevatorSystemStatus();
    assertEquals("[]", buildingReport.getUpRequests().toString());
    assertEquals("[]", buildingReport.getDownRequests().toString());
  }

  /**
   * Test startElevatorSystem() method starts the elevator system.
   */
  @Test
  public void testStartElevatorSystem() {
    Building building = new Building(10, 5, 5);
    assertTrue(building.startElevatorSystem());
    assertEquals("Running", building.getSystemStatus().toString());
    BuildingReport buildingReport = building.getElevatorSystemStatus();
    String report = "[Waiting[Floor 0, Time 5], "
        + "Waiting[Floor 0, Time 5], Waiting[Floor 0, Time 5], "
        + "Waiting[Floor 0, Time 5], Waiting[Floor 0, Time 5]]";
    assertEquals(report, Arrays.toString(buildingReport.getElevatorReports()));
  }

  /**
   * Test startElevatorSystem() method when the elevator is already running.
   */
  @Test
  public void testStartElevatorSystemRunning() {
    Building building = new Building(10, 5, 5);
    building.startElevatorSystem();
    assertEquals("Running", building.getSystemStatus().toString());

    assertFalse(building.startElevatorSystem());
    assertEquals("Running", building.getSystemStatus().toString());
  }

  /**
   * Test startElevatorSystem() method throws an exception
   * when the elevator system is stopping.
   */
  @Test (expected = IllegalStateException.class)
  public void testStartElevatorSystemWhenStopping() {
    Building building = new Building(10, 5, 5);
    building.startElevatorSystem();
    building.stopElevatorSystem();
    building.startElevatorSystem();
  }

  /**
   * Test stopElevatorSystem() method stops the elevator system.
   */
  @Test
  public void testStopElevatorSystem() {
    Building building = new Building(10, 5, 5);
    building.startElevatorSystem();
    assertEquals("Running", building.getSystemStatus().toString());
    building.addRequest(new Request(5, 2));
    building.addRequest(new Request(0, 8));

    building.step();
    building.step();
    building.stopElevatorSystem();
    assertEquals("Stopping", building.getSystemStatus().toString());
    BuildingReport buildingReport = building.getElevatorSystemStatus();
    assertEquals("[]", buildingReport.getUpRequests().toString());
    assertEquals("[]", buildingReport.getDownRequests().toString());

    building.step();
    building.step();
    assertEquals("Out Of Service", building.getSystemStatus().toString());
    Elevator[] elevators = new Elevator[5];
    for (int i = 0; i < 5; ++i) {
      elevators[i] = new Elevator(10, 5);
    }
    for (Elevator elevator : elevators) {
      assertEquals(0, elevator.getCurrentFloor());
      assertEquals("-", elevator.getDirection().toString());
      assertTrue(elevator.isDoorClosed());
      assertFalse(elevator.isTakingRequests());
    }
  }

  /**
   * Test stopElevatorSystem() method when the elevator system is already stopping.
   */
  @Test
  public void testStopElevatorSystemStopping() {
    Building building = new Building(10, 5, 5);
    building.startElevatorSystem();
    building.stopElevatorSystem();
    assertEquals("Stopping", building.getSystemStatus().toString());

    building.stopElevatorSystem();
    assertEquals("Stopping", building.getSystemStatus().toString());
  }

  /**
   * Test stopElevatorSystem() method when the elevator system is out of service.
   */
  @Test
  public void testStopElevatorSystemOutOfService() {
    Building building = new Building(10, 5, 5);
    building.stopElevatorSystem();
    assertEquals("Out Of Service", building.getSystemStatus().toString());
  }

  /**
   * Test step() method when the building is out of service.
   */
  @Test
  public void testStepOutOfService() {
    Building building = new Building(10, 5, 5);
    BuildingReport buildingReport1 = building.getElevatorSystemStatus();
    String report = "[Out of Service[Floor 0], Out of Service[Floor 0], "
        + "Out of Service[Floor 0], Out of Service[Floor 0], Out of Service[Floor 0]]";
    assertEquals(report, Arrays.toString(buildingReport1.getElevatorReports()));

    building.step();
    BuildingReport buildingReport2 = building.getElevatorSystemStatus();
    String report2 = "[Out of Service[Floor 0], Out of Service[Floor 0], "
        + "Out of Service[Floor 0], Out of Service[Floor 0], Out of Service[Floor 0]]";
    assertEquals(report2, Arrays.toString(buildingReport2.getElevatorReports()));
  }

  /**
   * Test step() method when the building is stopping.
   */
  @Test
  public void testStepStopping() {
    Building building = new Building(10, 3, 3);
    building.startElevatorSystem();
    for (int i = 0; i < 10; i++) {
      building.step();
    }
    BuildingReport buildingReport1 = building.getElevatorSystemStatus();
    String report = "[[5|^|C  ]< -- -- -- -- -- -- -- -- -- -->, "
        + "[5|^|C  ]< -- -- -- -- -- -- -- -- -- -->, "
        + "[5|^|C  ]< -- -- -- -- -- -- -- -- -- -->]";
    assertEquals(report, Arrays.toString(buildingReport1.getElevatorReports()));

    building.stopElevatorSystem();
    // after the system is stop, all elevators head to the ground floor.
    building.step();
    BuildingReport buildingReport2 = building.getElevatorSystemStatus();
    String report2 = "[[4|v|C  ]< -- -- -- -- -- -- -- -- -- -->, "
        + "[4|v|C  ]< -- -- -- -- -- -- -- -- -- -->, "
        + "[4|v|C  ]< -- -- -- -- -- -- -- -- -- -->]";
    assertEquals(report2, Arrays.toString(buildingReport2.getElevatorReports()));

    for (int i = 0; i < 4; i++) {
      building.step();
    }
    // after reaching to the ground floor, all elevators are taken out of service.
    Elevator[] elevators = new Elevator[3];
    for (int i = 0; i < 3; ++i) {
      elevators[i] = new Elevator(10, 3);
    }
    for (Elevator elevator : elevators) {
      assertEquals(0, elevator.getCurrentFloor());
      assertEquals("-", elevator.getDirection().toString());
      assertTrue(elevator.isDoorClosed());
      assertFalse(elevator.isTakingRequests());
    }
    BuildingReport buildingReport3 = building.getElevatorSystemStatus();
    String report3 = "[Out of Service[Floor 0], Out of Service[Floor 0], "
        + "Out of Service[Floor 0]]";
    assertEquals(report3, Arrays.toString(buildingReport3.getElevatorReports()));
  }

  /**
   * Test step() method when the building is running without requests.
   */
  @Test
  public void testStepRunningWithoutRequests() {
    Building building = new Building(10, 3, 3);
    // Before any steps, the building is out of service.
    BuildingReport buildingReport1 = building.getElevatorSystemStatus();
    String report1 = "[Out of Service[Floor 0], Out of Service[Floor 0], "
        + "Out of Service[Floor 0]]";
    assertEquals(report1, Arrays.toString(buildingReport1.getElevatorReports()));

    building.startElevatorSystem();
    // Start the elevator system and check each elevator's status.
    Elevator[] elevators = new Elevator[3];
    for (int i = 0; i < 3; ++i) {
      elevators[i] = new Elevator(10, 3);
    }
    for (Elevator elevator : elevators) {
      assertEquals(0, elevator.getCurrentFloor());
      assertEquals("-", elevator.getDirection().toString());
      assertTrue(elevator.isDoorClosed());
      assertFalse(elevator.isTakingRequests());
    }
    BuildingReport buildingReport2 = building.getElevatorSystemStatus();
    String report2 = "[Waiting[Floor 0, Time 5], Waiting[Floor 0, Time 5], "
        + "Waiting[Floor 0, Time 5]]";
    assertEquals(report2, Arrays.toString(buildingReport2.getElevatorReports()));

    // after 6 steps, the elevators quit stopping and prepare going upwards.
    for (int i = 0; i < 5; i++) {
      building.step();
    }

    BuildingReport buildingReport3 = building.getElevatorSystemStatus();
    String report3 = "[[0|^|C  ]< -- -- -- -- -- -- -- -- -- -->, "
        + "[0|^|C  ]< -- -- -- -- -- -- -- -- -- -->, "
        + "[0|^|C  ]< -- -- -- -- -- -- -- -- -- -->]";
    assertEquals(report3, Arrays.toString(buildingReport3.getElevatorReports()));

    for (int i = 0; i < 10; i++) {
      building.step();
    }

    BuildingReport buildingReport4 = building.getElevatorSystemStatus();
    String report4 = "[Waiting[Floor 9, Time 5], Waiting[Floor 9, Time 5], "
        + "Waiting[Floor 9, Time 5]]";
    assertEquals(report4, Arrays.toString(buildingReport4.getElevatorReports()));

    for (int i = 0; i < 5; i++) {
      building.step();
    }

    BuildingReport buildingReport5 = building.getElevatorSystemStatus();
    String report5 = "[[9|v|C  ]< -- -- -- -- -- -- -- -- -- -->, "
        + "[9|v|C  ]< -- -- -- -- -- -- -- -- -- -->, "
        + "[9|v|C  ]< -- -- -- -- -- -- -- -- -- -->]";
    assertEquals(report5, Arrays.toString(buildingReport5.getElevatorReports()));
  }

  /**
   * Test step() method when the building is running with requests within
   * 1 elevator's capacity.
   */
  @Test
  public void testStepRunningWithRequestsInAnElevator() {
    Building building = new Building(10, 3, 3);
    building.startElevatorSystem();
    building.addRequest(new Request(0, 5));
    building.addRequest(new Request(2, 6));
    building.addRequest(new Request(1, 8));
    for (int i = 0; i < 6; i++) {
      building.step();
    }

    BuildingReport buildingReport1 = building.getElevatorSystemStatus();
    String report1 = "[[1|^|O 3]< -- --  2 -- --  5  6 --  8 -->, "
        + "[1|^|C  ]< -- -- -- -- -- -- -- -- -- -->, "
        + "[1|^|C  ]< -- -- -- -- -- -- -- -- -- -->]";
    assertEquals(report1, Arrays.toString(buildingReport1.getElevatorReports()));
  }

  /**
   * Test step() method when the building is running with requests with more than
   * 1 elevator's capacity.
   */
  @Test
  public void testStepRunningWithRequestsInElevators() {
    Building building = new Building(10, 3, 3);
    building.startElevatorSystem();
    building.addRequest(new Request(0, 5));
    building.addRequest(new Request(2, 6));
    building.addRequest(new Request(1, 8));
    building.addRequest(new Request(0, 9));
    building.addRequest(new Request(3, 7));
    building.addRequest(new Request(4, 8));
    building.addRequest(new Request(5, 9));
    building.addRequest(new Request(6, 7));
    building.addRequest(new Request(2, 9));
    building.step();
    BuildingReport buildingReport1 = building.getElevatorSystemStatus();
    String report1 = "[[0|^|O 3]< --  1  2 -- --  5  6 --  8 -->, "
        + "[0|^|O 3]< -- -- --  3  4 -- --  7  8  9>, "
        + "[1|^|C  ]< -- --  2 -- --  5  6  7 --  9>]";
    assertEquals(report1, Arrays.toString(buildingReport1.getElevatorReports()));
  }

  /**
   * Test getElevatorSystemStatus() method when the building is out of service.
   */
  @Test
  public void testGetElevatorSystemStatusBeforeStart() {
    Building building = new Building(10, 5, 5);
    BuildingReport buildingReport = building.getElevatorSystemStatus();
    String report = "[Out of Service[Floor 0], Out of Service[Floor 0], "
        + "Out of Service[Floor 0], Out of Service[Floor 0], Out of Service[Floor 0]]";
    assertEquals(report, Arrays.toString(buildingReport.getElevatorReports()));
    assertEquals("Out Of Service", building.getSystemStatus().toString());

    Elevator[] elevators = new Elevator[5];
    for (int i = 0; i < 5; ++i) {
      elevators[i] = new Elevator(10, 5);
    }
    for (Elevator elevator : elevators) {
      assertEquals(0, elevator.getCurrentFloor());
      assertEquals("-", elevator.getDirection().toString());
    }
  }

  /**
   * Test getElevatorSystemStatus() method after the building is started
   * and one step is taken without any requests.
   */
  @Test
  public void testGetElevatorSystemStatusNoRequest() {
    Building building = new Building(10, 5, 5);
    building.startElevatorSystem();
    building.step();
    BuildingReport buildingReport = building.getElevatorSystemStatus();
    String report = "[Waiting[Floor 0, Time 4], Waiting[Floor 0, Time 4], "
        + "Waiting[Floor 0, Time 4], Waiting[Floor 0, Time 4], Waiting[Floor 0, Time 4]]";
    assertEquals(report, Arrays.toString(buildingReport.getElevatorReports()));
  }

  /**
   * Test getElevatorSystemStatus() method after the building is started
   * and 6 steps are taken and endWaitTimer has been stepped out without any requests.
   */
  @Test
  public void testGetElevatorSystemStatusAfter6StepsNoRequest() {
    Building building = new Building(10, 5, 5);
    building.startElevatorSystem();
    for (int i = 0; i < 5; i++) {
      building.step();
    }
    BuildingReport buildingReport = building.getElevatorSystemStatus();
    String report = "[[0|^|C  ]< -- -- -- -- -- -- -- -- -- -->, "
        + "[0|^|C  ]< -- -- -- -- -- -- -- -- -- -->, "
        + "[0|^|C  ]< -- -- -- -- -- -- -- -- -- -->, "
        + "[0|^|C  ]< -- -- -- -- -- -- -- -- -- -->, "
        + "[0|^|C  ]< -- -- -- -- -- -- -- -- -- -->]";
    assertEquals(report, Arrays.toString(buildingReport.getElevatorReports()));
  }

  /**
   * Test getElevatorSystemStatus() method after the building is started
   * with a request.
   */
  @Test
  public void testGetElevatorSystemStatusWithOneRequest() {
    Building building = new Building(11, 8, 3);
    building.startElevatorSystem();
    building.addRequest(new Request(2, 5));
    building.step();

    // after 1 step, the first elevator goes up and arrives at floor 1.
    BuildingReport buildingReport1 = building.getElevatorSystemStatus();
    String report1 = "[[1|^|C  ]< -- --  2 -- --  5 -- -- -- -- -->, "
        + "Waiting[Floor 0, Time 4], Waiting[Floor 0, Time 4], "
        + "Waiting[Floor 0, Time 4], Waiting[Floor 0, Time 4], "
        + "Waiting[Floor 0, Time 4], Waiting[Floor 0, Time 4], Waiting[Floor 0, Time 4]]";
    assertEquals(report1, Arrays.toString(buildingReport1.getElevatorReports()));

    building.step();
    // after 2 steps, the first elevator goes up and arrives at floor 2 with door closed.
    BuildingReport buildingReport2 = building.getElevatorSystemStatus();
    String report2 = "[[2|^|C  ]< -- --  2 -- --  5 -- -- -- -- -->, "
        + "Waiting[Floor 0, Time 3], Waiting[Floor 0, Time 3], "
        + "Waiting[Floor 0, Time 3], Waiting[Floor 0, Time 3], "
        + "Waiting[Floor 0, Time 3], Waiting[Floor 0, Time 3], Waiting[Floor 0, Time 3]]";
    assertEquals(report2, Arrays.toString(buildingReport2.getElevatorReports()));

    building.step();
    // after 3 steps, the first elevator arrives at floor 2 and opens door.
    BuildingReport buildingReport3 = building.getElevatorSystemStatus();
    String report3 = "[[2|^|O 3]< -- -- -- -- --  5 -- -- -- -- -->, "
        + "Waiting[Floor 0, Time 2], Waiting[Floor 0, Time 2], "
        + "Waiting[Floor 0, Time 2], Waiting[Floor 0, Time 2], "
        + "Waiting[Floor 0, Time 2], Waiting[Floor 0, Time 2], Waiting[Floor 0, Time 2]]";
    assertEquals(report3, Arrays.toString(buildingReport3.getElevatorReports()));

    building.step();
    building.step();
    building.step();
    // after another 3 steps, the first elevator closes the door on the 2nd floor.
    BuildingReport buildingReport4 = building.getElevatorSystemStatus();
    String report4 = "[[2|^|C  ]< -- -- -- -- --  5 -- -- -- -- -->, "
        + "[1|^|C  ]< -- -- -- -- -- -- -- -- -- -- -->, "
        + "[1|^|C  ]< -- -- -- -- -- -- -- -- -- -- -->, "
        + "[1|^|C  ]< -- -- -- -- -- -- -- -- -- -- -->, "
        + "[1|^|C  ]< -- -- -- -- -- -- -- -- -- -- -->, "
        + "[1|^|C  ]< -- -- -- -- -- -- -- -- -- -- -->, "
        + "[1|^|C  ]< -- -- -- -- -- -- -- -- -- -- -->, "
        + "[1|^|C  ]< -- -- -- -- -- -- -- -- -- -- -->]";
    assertEquals(report4, Arrays.toString(buildingReport4.getElevatorReports()));

    for (int i = 0; i < 4; i++) {
      building.step();
    }
    // after another 4 steps, the first elevator arrives at floor 5 and opens door.
    BuildingReport buildingReport5 = building.getElevatorSystemStatus();
    String report5 = "[[5|^|O 3]< -- -- -- -- -- -- -- -- -- -- -->, "
        + "[5|^|C  ]< -- -- -- -- -- -- -- -- -- -- -->, "
        + "[5|^|C  ]< -- -- -- -- -- -- -- -- -- -- -->, "
        + "[5|^|C  ]< -- -- -- -- -- -- -- -- -- -- -->, "
        + "[5|^|C  ]< -- -- -- -- -- -- -- -- -- -- -->, "
        + "[5|^|C  ]< -- -- -- -- -- -- -- -- -- -- -->, "
        + "[5|^|C  ]< -- -- -- -- -- -- -- -- -- -- -->, "
        + "[5|^|C  ]< -- -- -- -- -- -- -- -- -- -- -->]";
    assertEquals(report5, Arrays.toString(buildingReport5.getElevatorReports()));
  }

  /**
   * Test getElevatorSystemStatus() method after the building is started
   * with multiple requests.
   */
  @Test
  public void testGetElevatorSystemStatusWithMultipleRequests() {
    Building building = new Building(11, 3, 3);
    building.startElevatorSystem();
    building.addRequest(new Request(2, 5));
    building.addRequest(new Request(0, 1));
    building.addRequest(new Request(0, 2));
    building.addRequest(new Request(0, 3));
    building.addRequest(new Request(5, 6));
    building.addRequest(new Request(5, 9));
    building.addRequest(new Request(5, 10));
    building.addRequest(new Request(10, 3));
    building.addRequest(new Request(8, 1));
    BuildingReport buildingReport = building.getElevatorSystemStatus();
    assertEquals("[2->5, 0->1, 0->2, 0->3, 5->6, 5->9, 5->10]",
        buildingReport.getUpRequests().toString());
    assertEquals("[10->3, 8->1]", buildingReport.getDownRequests().toString());

    for (int i = 0; i < 5; i++) {
      building.step();
    }

    BuildingReport buildingReport1 = building.getElevatorSystemStatus();
    String report1 = "[[1|^|C  ]< --  1  2 -- --  5 -- -- -- -- -->, "
        + "[1|^|C  ]< -- -- --  3 --  5  6 -- --  9 -->, "
        + "[5|^|C  ]< -- -- -- -- --  5 -- -- -- -- 10>]";
    assertEquals(report1, Arrays.toString(buildingReport1.getElevatorReports()));

    for (int i = 0; i < 15; i++) {
      building.step();
    }

    BuildingReport buildingReport2 = building.getElevatorSystemStatus();
    String report2 = "[[5|^|O 1]< -- -- -- -- -- -- -- -- -- -- -->, "
        + "[6|^|O 2]< -- -- -- -- -- -- -- -- --  9 -->, "
        + "[10|v|O 3]< --  1 --  3 -- -- -- --  8 -- -->]";
    assertEquals(report2, Arrays.toString(buildingReport2.getElevatorReports()));
  }

  /** Test that the building will eventually stop
   * when a take out of service is issued and one elevator has a door open.
   */
  @Test
  public void testStopElevatorSystemWithDoorOpen() {
    Building building = new Building(11, 3, 3);
    building.startElevatorSystem();
    building.addRequest(new Request(0, 1));
    BuildingReport buildingReport = building.getElevatorSystemStatus();
    assertEquals("[0->1]", buildingReport.getUpRequests().toString());

    building.step();

    BuildingReport buildingReport1 = building.getElevatorSystemStatus();
    String report1 = "[[0|^|O 3]< --  1 -- -- -- -- -- -- -- -- -->, "
        + "Waiting[Floor 0, Time 4], Waiting[Floor 0, Time 4]]";
    assertEquals(report1, Arrays.toString(buildingReport1.getElevatorReports()));

    for (int i = 0; i < 5; i++) {
      building.step();
    }

    BuildingReport buildingReport2 = building.getElevatorSystemStatus();
    String report2 = "[[1|^|O 3]< -- -- -- -- -- -- -- -- -- -- -->, "
        + "[1|^|C  ]< -- -- -- -- -- -- -- -- -- -- -->, "
        + "[1|^|C  ]< -- -- -- -- -- -- -- -- -- -- -->]";
    assertEquals(report2, Arrays.toString(buildingReport2.getElevatorReports()));

    building.stopElevatorSystem();
    BuildingReport buildingReport3 = building.getElevatorSystemStatus();
    String report3 = "[[1|v|O 3]< -- -- -- -- -- -- -- -- -- -- -->, "
        + "[1|v|C  ]< -- -- -- -- -- -- -- -- -- -- -->, "
        + "[1|v|C  ]< -- -- -- -- -- -- -- -- -- -- -->]";
    assertEquals(report3, Arrays.toString(buildingReport3.getElevatorReports()));

    for (int i = 0; i < 5; i++) {
      building.step();
    }

    BuildingReport buildingReport4 = building.getElevatorSystemStatus();
    String report4 = "[Out of Service[Floor 0], "
        + "Out of Service[Floor 0], Out of Service[Floor 0]]";
    assertEquals(report4, Arrays.toString(buildingReport4.getElevatorReports()));
  }
}