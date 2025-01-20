package building;

import static org.junit.Assert.assertEquals;

import building.enums.Direction;
import building.enums.ElevatorSystemStatus;
import elevator.ElevatorReport;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import scanerzus.Request;

/**
 * The BuildingReportTest class is a test suite for the BuildingReport class.
 */
public class BuildingReportTest {
  private BuildingReport report;

  /**
   * Sets up the test.
   */
  @Before
  public void setup() {
    report = new BuildingReport(
        3,  // numberOfFloors
        1,  // numberOfElevators
        2,  // elevatorCapacity
        new ElevatorReport[]{
            new ElevatorReport(
                1,  // elevatorId
                1,  // currentFloor
                Direction.STOPPED,  // direction
                true,  // doorClosed
                new boolean[]{false, true, true},  // floorRequests
                0,  // doorOpenTimer
                0,  // endWaitTimer
                false,  // outOfService
                false  // isTakingRequests
            )
        },
        List.of(new Request[]{new Request(0, 1), new Request(0, 2)}),
        List.of(new Request[]{new Request(2, 0)}),
        ElevatorSystemStatus.outOfService  // systemStatus
    );
  }

  @Test
  public void testGetNumFloors() {
    assertEquals(3, report.getNumFloors());
  }

  @Test
  public void testGetNumElevators() {
    assertEquals(1, report.getNumElevators());
  }

  @Test
  public void testGetElevatorCapacity() {
    assertEquals(2, report.getElevatorCapacity());
  }

  @Test
  public void testGetElevatorReports() {
    assertEquals(1, report.getElevatorReports().length);
    assertEquals(1, report.getElevatorReports()[0].getElevatorId());
  }

  @Test
  public void testGetUpRequests() {
    assertEquals(2, report.getUpRequests().size());
    assertEquals(0, report.getUpRequests().get(0).getStartFloor());
    assertEquals(1, report.getUpRequests().get(0).getEndFloor());
    assertEquals(0, report.getUpRequests().get(1).getStartFloor());
    assertEquals(2, report.getUpRequests().get(1).getEndFloor());
  }

  @Test
  public void testGetDownRequests() {
    assertEquals(1, report.getDownRequests().size());
    assertEquals(2, report.getDownRequests().get(0).getStartFloor());
    assertEquals(0, report.getDownRequests().get(0).getEndFloor());
  }

  @Test
  public void testGetSystemStatus() {
    assertEquals(ElevatorSystemStatus.outOfService, report.getSystemStatus());
  }

  @Test
  public void testToString() {
    String reportToString = "Building Report:\n"
        + "Number of Floors: 3\n"
        + "Number of Elevators: 1\n"
        + "Elevator Capacity: 2\n"
        + "Elevator Reports:\n"
        + "[1|-|C  ]< --  1  2>\n"
        + "Up Requests:\n"
        + "0->1\n"
        + "0->2\n"
        + "Down Requests:\n"
        + "2->0\n"
        + "System Status: Out Of Service\n";
    assertEquals(reportToString, report.toString());
  }
}
