package falstad;

import java.util.Random;

import falstad.Robot.Turn;
import generation.CardinalDirection;

/**
 * CRC Card: Responsibilities: • Picks random main direction • Senses obstacles
 * • Tracks right and left turns • Drives the robot to the exit
 * 
 * Collaborators: • BasicRobot • ManualDriver • MazeController •
 * CardinalDirection
 * 
 * This algorithm is a form of WallFollower except, in this case it allows the
 * robot to leave an obstacle. The driver starts off with a random direction and
 * once it runs into an obstacle, it keeps track of the amount of right and left
 * turns the robot makes (+1 for right and -1 for left). When the turns have
 * balanced each other off the robot is able to leave the obstacle.
 *
 */

/**
 * @author roshanmanjaly
 *
 */

public class Pledge extends ManualDriver {

	final int Right = 0;
	final int Left = 1;
	final int Forward = 2;
	Random rand;

	public Pledge(MazeController maze) {
		super(maze);
		rand = new Random();
	}

	@Override
	public boolean drive2Exit() throws Exception {

		int turns = 0;
		// while the robot is still moving and is not at the exit...
		while (!robot.hasStopped() && !robot.isAtExit()) {

			// if there is a wall to the left and a wall to the right...
			if (wallExists(Left) && wallExists(Right)) {
				// if there is no wall forward then move forward
				if (!wallExists(Forward)) {
					robot.move(1, false);
				} else {
					// else, turn around
					robot.rotate(Turn.LEFT);
					robot.rotate(Turn.LEFT);
					// update the amount of turns
					turns -= 2;
				}
				// if there is no wall to the left
			} else if (!wallExists(Left)) {
				// and we have already turned right, turn left
				if (turns > 0) {
					robot.rotate(Turn.LEFT);
					// subtract the counts
					turns--;
				}
				// if there is no wall to the right
			} else if (!wallExists(Right)) {
				// and if the turn tracker is negative
				if (turns < 0) {
					// turn right
					robot.rotate(Turn.RIGHT);
					// increment the count
					turns++;
				}
			}
			// if there is a wall in front of us
			if (wallExists(Forward)) {
				// and if we have made the equal amount of right and left turns
				if (turns == 0) {
					// we chose a random direction
					if (rand.nextBoolean()) {
						robot.rotate(Turn.LEFT);
						turns--;
					} else {
						robot.rotate(Turn.RIGHT);
						turns++;
					}
				} else if (turns < 0) {
					robot.rotate(Turn.RIGHT);
					turns++;
				} else if (turns > 0) {
					robot.rotate(Turn.LEFT);
					turns--;
				}
				// else if there is no wall in front then we can move forward
			} else {
				robot.move(1, false);
			}
		}
		if (robot.isAtExit()) {
			return true;
		}
		return false;
	}

	// this method checks if there's a wall in the left or right direction
	private boolean wallExists(int direction) throws Exception {
		CardinalDirection dir = robot.getCurrentDirection();

		switch (direction) {
		case Left:
			dir = dir.rotateCounterClockwise();
			break;
		case Right:
			dir = dir.rotateClockwise();
			break;
		}

		return robot.cells.hasWall(robot.getCurrentPosition()[0], robot.getCurrentPosition()[1], dir);
	}
}
