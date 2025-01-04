package com.furnistyle.furniturebackend;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootTest
@Slf4j
@EnableAsync
class FurnitureBackendApplicationTests {

	@Test
	void contextLoads() {
		log.info("The context is load successful");
	}

}
