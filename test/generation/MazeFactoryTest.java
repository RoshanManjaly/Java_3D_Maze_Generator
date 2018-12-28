package generation;

import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class MazeFactoryTest {

	private MazeFactory maze_fact;
	private MazeConfiguration config;

	@Before
	public void setUp() throws Exception {
		boolean det = false;
		maze_fact = new MazeFactory(det);
		maze_fact.waitTillDelivered();
	}

	@After
	public void tearDown() throws Exception {
	}

	/**
	 * No null
	 */
	@Test
	public void doesSetupWork() {
		assertNotNull(maze_fact);
		assertNotNull(config);
	}

	/**
	 * Check for exit
	 */
	@Test
	public void doesMazeHaveExit() {
		int exit = 0; // initializes variable, could be anything, 0 is easiest
		Distance m_dist = config.getMazedists(); // gets distance from exit

		for (int i = 0; i < config.getWidth(); i++) {
			for (int j = 0; j < config.getHeight(); j++) {
				int distance = m_dist.getDistance(i, j);
				if (distance == 1) { // if distance from exit is 1 then there is an exit, so you increment the
										// counter
					exit = exit + 1;
				}
			}
		}
		assertEquals(1, exit); // there should only be 1 exits so that means the correct answer for exit is 1
	}

	@Test
	private boolean checkIfSolvable(MazeContainer m) {
		int[] startpos = m.getStartingPosition();
		if (m.getDistanceToExit(startpos[0], startpos[1]) < 3) {

		}
		return false;
	}

	/**
	 * dimensions
	 */
	@Test
	public void areDimensionsCorrect() {
	}
}