package falstad;

import falstad.Robot.Turn;
import generation.Distance;

/**
 * @author roshanmanjaly
 *
 */
public class ManualDriver implements RobotDriver {

	public BasicRobot robot;
	protected int width;
	protected int height;
	protected Distance distance;
	protected float energy;
	protected int pathLength;

	public ManualDriver(MazeController maze) {
		robot = new BasicRobot();
		robot.setMaze(maze);
		// setDistance(maze.getMazeConfiguration().getMazedists());

	}

	@Override
	public void setRobot(Robot r) {
		robot = (BasicRobot) r;
		energy = robot.getBatteryLevel();
	}

	@Override
	public void setDimensions(int width, int height) {
		this.width = width;
		this.height = height;

	}

	@Override
	public void setDistance(Distance distance) {
		this.distance = distance;

	}

	@Override
	public boolean drive2Exit() throws Exception {
		return false;
	}

	@Override
	public float getEnergyConsumption() {
		return energy - robot.getBatteryLevel();
	}

	@Override
	public int getPathLength() {
		return pathLength;
	}

	/**
	 * This method is called in SimpleKeyListener.java. It is initiated when the up
	 * arrow key is pressed. Then the move method is called in BasicRobot
	 */
	// ADDED METHOD
	public void moveForward() {

		robot.move(1, true);
		pathLength++;
	}

	/**
	 * This method is called in SimpleKeyListener.java. It is initiated when the
	 * left arrow key is pressed. Then the rotate method is called in BasicRobot.
	 */
	// ADDED METHOD
	public void rotateLeft() {
		robot.rotate(Turn.LEFT);
	}

	/**
	 * This method is called in SimpleKeyListener.java. It is initiated when the
	 * right arrow key is pressed. Then the rotate method is called in BasicRobot.
	 */
	// ADDED METHOD
	public void rotateRight() {
		robot.rotate(Turn.RIGHT);
	}

	/**
	 * This method is called in SimpleKeyListener.java. It is initiated when the
	 * down arrow key is pressed. Then the rotate method is called in BasicRobot.
	 */
	// ADDED METHOD
	public void moveBackward() {
		robot.rotate(Turn.AROUND);
		robot.move(1, true);
		pathLength++;
	}

}