package au.com.seek.automatedcounter;

import aspect.LogExecutionTime;
import config.AppConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import java.util.List;

@Slf4j
@SpringBootApplication
@ComponentScan(basePackages = "au.com.seek.automatedcounter")
@ComponentScan(basePackages = "config")
@ComponentScan(basePackages = "aspect")
public class AutomatedCounterApplication {

    @Autowired
    LoadData loadData;

    @Autowired
    AppConfig appConfig;

    public static void main(String[] args) {
        ApplicationContext ctx = SpringApplication.run(AutomatedCounterApplication.class, args);
        AutomatedCounterApplication automatedCounterApplication = ctx.getBean(AutomatedCounterApplication.class);
        automatedCounterApplication.initializeApp();
    }

    @LogExecutionTime
    public void initializeApp() {
        List<TrafficData> trafficDataList = loadData.readFile(appConfig.getInputFileName());
        loadData.preparePerDayList(trafficDataList);
        loadData.topThreeHalfHrs(trafficDataList);
        loadData.getMinCountOneAndHalfHr(trafficDataList);
    }
}
