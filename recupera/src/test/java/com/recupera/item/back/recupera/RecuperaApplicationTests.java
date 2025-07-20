package com.recupera.item.back.recupera;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.Disabled;
import org.springframework.context.annotation.Import;

@SpringBootTest
@Import({
    com.recupera.item.back.recupera.config.MailSenderTestConfig.class,
    com.recupera.item.back.recupera.config.GoogleDriveConfigTestConfig.class
})
class RecuperaApplicationTests {

	//@Disabled("Ignorar teste de contexto enquanto não for necessário")
	@Test
	void contextLoads() {
	}
}