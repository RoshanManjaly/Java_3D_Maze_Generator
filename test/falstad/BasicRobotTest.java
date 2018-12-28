package falstad;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import falstad.BasicRobot;
import falstad.MazeApplication;
import falstad.Robot.Direction;
import falstad.Robot.Turn;
import generation.MazeFactory;
import generation.SingleRandom;
import generation.Order.Builder;

/**
 * @author roshanmanjaly
 *
 */
public class BasicRobotTest {
	BasicRobot robot;
	MazeApplication maze;

	private MazeFactory mazeFactory;
	private StubController stubController;
	BasicRobot testRobot = new BasicRobot();

	@Before
	public void setUp() throws Exception {
		SingleRandom.setSeed(17);
		robot = new BasicRobot();
		maze = new MazeApplication("Eller");
		maze.repaint();

		boolean det = false;
		// generate a maze
		mazeFactory = new MazeFactory(det);
		// create a StubOrder to set maze aspects
		// our skillLevel is set to 1, and the build type is default
		stubController = new StubController(1, Builder.DFS, det);
		mazeFactory.order(stubController);
		mazeFactory.waitTillDelivered();
		stubController.getMazeDetails();
	}

	@Test
	public void energyLevelCorrect() {
		float energy = 3000;
		// BasicRobot testRobot;
		testRobot.rotate(Turn.LEFT);
		float robotEnergy = testRobot.getBatteryLevel();

		boolean result = false;
		if (energy > robotEnergy) {
			result = true;
		}

		assertEquals(result, true);
	}

	@Test
	public void testGetEnergyForFullRotation() {

		assertTrue(12 == robot.getEnergyForFullRotation());
	}

	@Test
	public void testGetEnergyForStepForward() {

		assertTrue(robot.getEnergyForStepForward() == 5);
	}

	@Test
	public void testRoomSensorExists() {
		assertTrue(robot.hasRoomSensor());
	}

	@Test
	public void testBackSensorExists() {
		assertTrue(robot.hasDistanceSensor(Direction.BACKWARD));
	}

	@Test
	public void testFrontSensorExists() {

		assertTrue(robot.hasDistanceSensor(Direction.FORWARD));
	}

	@Test
	public void testLeftSensorExists() {

		assertTrue(robot.hasDistanceSensor(Direction.LEFT));
	}

	@Test
	public void testRightSensorExists() {

		assertTrue(robot.hasDistanceSensor(Direction.RIGHT));
	}

	public void tearDown() throws Exception {
	}
}
