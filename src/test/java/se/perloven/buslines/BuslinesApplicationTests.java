package se.perloven.buslines;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(properties = "TRAFIKLAB_API_KEY=test-placeholder")
class BuslinesApplicationTests {

	@Test
	void contextLoads() {
	}

}
