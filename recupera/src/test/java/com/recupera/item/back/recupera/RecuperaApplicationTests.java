package com.recupera.item.back.recupera;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@SpringBootTest
@Import({
    com.recupera.item.back.recupera.config.MailSenderTestConfig.class
})
class RecuperaApplicationTests {

	//@Disabled("Ignorar teste de contexto enquanto não for necessário")
	@Test
	void contextLoads() {
	}
}