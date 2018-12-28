package generation;

import static org.junit.Assert.*;
import generation.Order.Builder;

import org.junit.Before;
import org.junit.Test;

/**
 * This is a Junit test for MazeBuilderEller.
 */
public class MazeBuilderEllerTest {

	private MazeFactory mazeFactory;
	private StubOrder stub;
	private MazeConfiguration config;

	@Before
	// set up a test maze
	public void setUp() {
		boolean bool = false;
		mazeFactory = new MazeFactory(bool);
		stub = new StubOrder(1, Builder.Eller, bool);
		mazeFactory.order(stub);
		mazeFactory.waitTillDelivered();
		config = stub.getMazeDetails();

	}

	@Test

	public void mazeTypeEller() {
		Builder mazeType = stub.getBuilder();
		Builder Eller = Builder.Eller;
		assertEquals(Eller, mazeType);
	}

	public void checkStartPosition() {
		int[] start = config.getStartingPosition();
		int x = start[0];
		int y = start[1];

		int distance = config.getDistanceToExit(x, y);
		int distance_start = config.getDistanceToExit(x + 1, y);

		boolean position = false;
		if (distance > distance_start) {
			position = true;
		} else {
			position = false;
		}
		assertEquals(true, position);
	}

	public void pos() {
		boolean pos = false;
		for (int x = 0; x < config.getWidth(); x++) {
			for (int y = 0; y < config.getHeight(); y++) {
				pos = config.isValidPosition(x, y);

			}
		}
		assertEquals(true, pos);
	}

}
