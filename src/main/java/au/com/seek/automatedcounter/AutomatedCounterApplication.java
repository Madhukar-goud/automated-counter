package au.com.seek.automatedcounter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@Slf4j
@SpringBootApplication
public class AutomatedCounterApplication {

	public static void main(String[] args) {
		ApplicationContext ctx = SpringApplication.run(AutomatedCounterApplication.class, args);
		LoadDataSimple loadDataSimple = ctx.getBean(LoadDataSimple.class);
		loadDataSimple.printStats();
	}
}
