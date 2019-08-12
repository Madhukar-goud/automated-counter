package au.com.seek.automatedcounter;

import aspect.LogExecutionTime;
import config.AppConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by madhukar on 8/08/19.
 */
@Slf4j
@Component
public class LoadData {

    @Autowired
    AppConfig appConfig;

    TrafficData globalMin = new TrafficData(0, LocalDateTime.now());

    /**
        Read the file from location specified in application.properties
     */
    public List<TrafficData> readFile(String fileName) {
        List<TrafficData> trafficDataList = new ArrayList<>();
        try (Stream<String> stream = Files.lines(Paths.get(fileName))) {
            stream.forEach(s -> {
                trafficDataList.add(new TrafficData(Integer.parseInt(s.split(" ")[1]), LocalDateTime.parse(s.split(" ")[0])));
            });
        } catch (IOException e) {
            log.error("IO Exception in readFile {} ", e);
        }
        return trafficDataList;
    }

    /**
     Custom method to call to print all the values
     */
    @LogExecutionTime
    public void printStats() {
        List<TrafficData> trafficDataList = readFile(appConfig.getInputFileName());
        preparePerDayList(trafficDataList);
        topThreeHalfHrs(trafficDataList);
        int minCount = getMinCountOneAndHalfHr(trafficDataList);
        log.info("Min count ==> {} ", minCount);
        log.info("Printing Global Min ==> {} ", globalMin.getCount());
    }

    /**
        Top 3 Half hours. Accepts the List of TrafficData
     */
    @LogExecutionTime
    public List<TrafficData> topThreeHalfHrs(final List<TrafficData> trafficDataList) {
        List<TrafficData> topThreeList = new ArrayList<>(trafficDataList);
        topThreeList.sort(Comparator.comparing(TrafficData::getCount).reversed());
        topThreeList = topThreeList.stream().limit(3).collect(Collectors.toList());
        log.info("Top 3 Half Hrs");
        topThreeList.forEach(s -> log.info("Count {}, Date {}", s.getCount(), s.getDateTime()));
        return topThreeList;
    }

    /**
        Sum of per day counts. Accepts the List of TrafficData. The last record needs
        to be added explicity
     */
    @LogExecutionTime
    public List<TrafficData> preparePerDayList(final List<TrafficData> trafficDataList) {
        List<TrafficData> perDayList = new ArrayList<>();
        TrafficData sameDate = null;
        int counter = 0;
        TrafficData[] totalCount = new TrafficData[3];
        for (TrafficData data : trafficDataList) {
            counter++;
            checkMinInTimePeriod(counter, totalCount, data);
            if (counter == 1) {
                sameDate = new TrafficData(data.getCount(), data.getDateTime());
            } else if (sameDate.getDateTime().toLocalDate().equals(data.getDateTime().toLocalDate())) {
                sameDate.setCount(sameDate.getCount() + data.getCount());
            } else {
                perDayList.add(sameDate);
                sameDate = new TrafficData(data.getCount(), data.getDateTime());
            }
            if (counter == trafficDataList.size()) {
                perDayList.add(sameDate);
            }
        }
        perDayList.stream().forEach(s -> log.info("{} ==> {}", s.getDateTime().toLocalDate(), s.getCount()));
        return perDayList;
    }

    /**
        Check the min in 1.5 hr time period. This can called from the preparePerDayList as well
        to avoid looping through the list again
     */
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

    /**
        Get the Min count in one and half hour time period. Accepts list of traffic data.
        It loops through the read file
     */
    @LogExecutionTime
    public Integer getMinCountOneAndHalfHr(final List<TrafficData> trafficDataList) {
        List<TrafficData> oneAndHalfHrList = new ArrayList<>();
        TrafficData[] trafficDataArray = trafficDataList.toArray(new TrafficData[trafficDataList.size()]);
        for (int i = 0; i < trafficDataArray.length - 2; i++) {
            if (ChronoUnit.HOURS.between(trafficDataArray[i].getDateTime(), trafficDataArray[i + 2].getDateTime()) == 1) {
                int totalCount = trafficDataArray[i].getCount() + trafficDataArray[i + 1].getCount() + trafficDataArray[i + 2].getCount();
                oneAndHalfHrList.add(new TrafficData(totalCount, trafficDataArray[i].getDateTime()));
            }
        }
        oneAndHalfHrList.sort(Comparator.comparing(TrafficData::getCount));
        if (oneAndHalfHrList != null && oneAndHalfHrList.size() > 0) {
            return oneAndHalfHrList.get(0).getCount();
        }
        return 0;
    }
}
