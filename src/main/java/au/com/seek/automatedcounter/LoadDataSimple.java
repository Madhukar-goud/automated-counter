package au.com.seek.automatedcounter;

import config.AppConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

/**
 * Created by madhukar on 8/08/19.
 */
@Slf4j
@Configuration
@ComponentScan(basePackages = "au.com.seek.automatedcounter")
@ComponentScan(basePackages = "config")
public class LoadDataSimple {

    @Autowired
    AppConfig appConfig;

    public List<TrafficDataSimple> readFile(String fileName) {
        List<TrafficDataSimple> trafficDataList = new ArrayList<>();
        try (Stream<String> stream = Files.lines(Paths.get(fileName)))
        {
            stream.forEach(s -> {
                trafficDataList.add(new TrafficDataSimple(Integer.parseInt(s.split(" ")[1]), LocalDateTime.parse(s.split(" ")[0])));
            });
        } catch (IOException e)
        {
            log.error("IO Exception in readFile {} ", e);
        }

        return trafficDataList;
    }

    public void printStats() {
        List<TrafficDataSimple> trafficDataList = readFile(appConfig.getInputFileName());
        preparePerDayList(trafficDataList);
        topThreeHalfHrs(trafficDataList);
        prepareOneAndHalfHrList(trafficDataList);
    }

    private void topThreeHalfHrs(final List<TrafficDataSimple> trafficDataList) {
        List<TrafficDataSimple> topThreeList = new ArrayList<>(trafficDataList);
        topThreeList.sort(Comparator.comparing(TrafficDataSimple::getCount).reversed());
        log.info("TOP 3 half hours with most cars ");
        topThreeList.stream().limit(3).forEach(s -> log.info(" Date / Time {} with count {}", s.getDateTime(), s.getCount()));
    }

    private void preparePerDayList(final List<TrafficDataSimple> trafficDataList) {
        List<TrafficDataSimple> perDayList = new ArrayList<>();
        TrafficDataSimple sameDate = null;
        for (TrafficDataSimple data: trafficDataList) {
            if (sameDate == null) {
                sameDate = data;
            }
            if (sameDate.getDateTime().toLocalDate().equals(data.getDateTime().toLocalDate())) {
                sameDate.setCount(sameDate.getCount() + data.getCount());
            } else {
                perDayList.add(sameDate);
                sameDate = null;
            }
        }
        perDayList.stream().forEach(s -> log.info(s.getDateTime().toLocalDate() +" ==> " + s.getCount()));
    }

    private void prepareOneAndHalfHrList(final List<TrafficDataSimple> trafficDataList) {
        List<TrafficDataSimple> oneAndHalfHrList = new ArrayList<>();
        TrafficDataSimple[] trafficDataArray = trafficDataList.toArray(new TrafficDataSimple[trafficDataList.size()]);
        for (int i = 0 ; i < trafficDataArray.length -2 ; i++) {
            if (ChronoUnit.HOURS.between(trafficDataArray[i].getDateTime(), trafficDataArray[i+2].getDateTime()) == 1) {
                int totalCount = trafficDataArray[i].getCount()+ trafficDataArray[i+1].getCount()+ trafficDataArray[i+2].getCount();
                oneAndHalfHrList.add(new TrafficDataSimple(totalCount, trafficDataArray[i].getDateTime()));
            }
        }
        oneAndHalfHrList.sort(Comparator.comparing(TrafficDataSimple::getCount));
        oneAndHalfHrList.stream().limit(1).forEach(s -> log.info("1.5 HOUR with least cars starts from ==> " + s.getDateTime() +" with a count of ==> " + s.getCount()));
    }
}
