package falstad;

import falstad.Robot.Turn;

/**
 * CRC Card: Responsibilities: • Drive the robot towards the exit • Assists
 * robot in sensing walls facing forward and facing left
 * 
 * Collaborators: • BasicRobot • ManualDriver • MazeController
 * 
 * This is a simple algorithm in which the driver guides the robot against the
 * wall, allowing it to find the exit.
 */

/**
 * @author roshanmanjaly
 *
 */
public class WallFollower extends ManualDriver {

	public WallFollower(MazeController maze) {
		super(maze);
	}

	@Override
	// we just need to implement the drive2Exit method
	public boolean drive2Exit() throws Exception {

		// while the robot has not stopped and is not at the exit
		while (!robot.hasStopped() && !robot.isAtExit()) {
			// Object mazeCells = robot.getMaze(); //ADD
			// if there is a wall to the robot's right and front, then we'll turn left
			;
			if (robot.cells.hasWall(robot.getCurrentPosition()[0], robot.getCurrentPosition()[1],
					robot.getCurrentDirection().rotateClockwise())) {
				if (robot.cells.hasWall(robot.getCurrentPosition()[0], robot.getCurrentPosition()[1],
						robot.getCurrentDirection())) {
					robot.rotate(Turn.LEFT);
					// else, if there is no wall ahead, then we can move forward
				} else {

					robot.move(1, false);
				}
				// else, if there isn't a wall to the right, then keep moving right until we hit
				// the wall
			} else {
				robot.rotate(Turn.RIGHT);
				robot.move(1, false);
			}
		}
		// if we are at the exit, return true
		if (robot.isAtExit()) {
			return true;
		}
		return false;
	}

}
