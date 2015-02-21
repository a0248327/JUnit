import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class TestHelloWorld {

	private HelloWorld helloworld;

	@Before
	public void setUp() throws Exception {
		helloworld = new HelloWorld();
	}

	@Test
	public void testHelloEmpty() {
		assertEquals(helloworld.getName(), "");
		assertEquals(helloworld.getMessage(), "Hello!");
	}

	@Test
	public void testHelloWorld() {
		helloworld.setName("World");
		assertEquals(helloworld.getName(), "World");
		assertEquals(helloworld.getMessage(), "Hello World!");
	}
}