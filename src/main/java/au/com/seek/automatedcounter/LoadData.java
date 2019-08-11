package au.com.seek.automatedcounter;

import aspect.LogExecutionTime;
import config.AppConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
public class LoadData {

    @Autowired
    AppConfig appConfig;
    TrafficData globalMin = new TrafficData(0, LocalDateTime.now());

    public List<TrafficData> readFile(String fileName) {
        List<TrafficData> trafficDataList = new ArrayList<>();
        try (Stream<String> stream = Files.lines(Paths.get(fileName)))
        {
            stream.forEach(s -> {
                trafficDataList.add(new TrafficData(Integer.parseInt(s.split(" ")[1]), LocalDateTime.parse(s.split(" ")[0])));
            });
        } catch (IOException e)
        {
            log.error("IO Exception in readFile {} ", e);
        }
        return trafficDataList;
    }

    @LogExecutionTime
    public void printStats() {
        List<TrafficData> trafficDataList = readFile(appConfig.getInputFileName());
        preparePerDayList(trafficDataList);
        topThreeHalfHrs(trafficDataList);
        log.info("Min in 1.5 hr period starts at {} with a count of {}", globalMin.getDateTime(), globalMin.getCount());
    }

    @LogExecutionTime
    public void topThreeHalfHrs(final List<TrafficData> trafficDataList) {
        List<TrafficData> topThreeList = new ArrayList<>(trafficDataList);
        topThreeList.sort(Comparator.comparing(TrafficData::getCount).reversed());
        log.info("TOP 3 half hours with most cars ");
        topThreeList.stream().limit(3).forEach(s -> log.info(" Date / Time {} with count {}", s.getDateTime(), s.getCount()));
    }

    @LogExecutionTime
    private void preparePerDayList(final List<TrafficData> trafficDataList) {
        List<TrafficData> perDayList = new ArrayList<>();
        TrafficData sameDate = null;
        int counter = 0;
        TrafficData[] totalCount = new TrafficData[3];
        for (TrafficData data: trafficDataList) {
            counter++;
            checkMinInTimePeriod(counter, totalCount, data);
            if (sameDate == null) {
                sameDate = new TrafficData(data.getCount(), data.getDateTime());
            } else if (sameDate.getDateTime().toLocalDate().equals(data.getDateTime().toLocalDate())) {
                sameDate.setCount(sameDate.getCount() + data.getCount());
            } else {
                perDayList.add(sameDate);
                sameDate = null;
            }
        }
        perDayList.stream().forEach(s -> log.info("{} ==> {}" , s.getDateTime().toLocalDate(), s.getCount()));
    }

    @LogExecutionTime
    private void checkMinInTimePeriod(int counter, TrafficData[] totalCount, TrafficData data) {
        int min;
        totalCount[2] = totalCount[1];
        totalCount[1] = totalCount[0];
        totalCount[0] = data;
        if (counter >= 3) {
            min = totalCount[0].getCount() + totalCount[1].getCount() + totalCount[2].getCount();
            if (counter == 3 || globalMin.getCount() > min) {
                if (ChronoUnit.HOURS.between(totalCount[2].getDateTime(), totalCount[0].getDateTime()) == 1) {
                    globalMin.setCount(min);
                    globalMin.setDateTime(totalCount[2].getDateTime());
                }
            }
        }
    }
}
