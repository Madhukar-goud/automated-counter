package au.com.seek.automatedcounter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import java.util.ArrayList;

@Slf4j
@SpringBootApplication
@ComponentScan(basePackages = "au.com.seek.automatedcounter")
@ComponentScan(basePackages = "config")
@ComponentScan(basePackages = "aspect")
public class AutomatedCounterApplication {

	public static void main(String[] args) {
		ApplicationContext ctx = SpringApplication.run(AutomatedCounterApplication.class, args);
		LoadData loadData = ctx.getBean(LoadData.class);
		loadData.printStats();
		loadData.topThreeHalfHrs(new ArrayList<>());
	}
}
