package au.com.seek.automatedcounter;

import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;


@RunWith(SpringRunner.class)
@SpringBootTest
@ComponentScan("au.com.seek.automatedcounter")
@Slf4j
public class AutomatedCounterApplicationTests {

	List<TrafficData> trafficDataList = new ArrayList<>();

	@Autowired
	LoadData loadData;

	@Before
	public void prepareData() {
		trafficDataList.add(new TrafficData(22, LocalDate.now().atTime(6, 0).minusDays(3)));
		trafficDataList.add(new TrafficData(23, LocalDate.now().atTime(6, 30).minusDays(3)));
		trafficDataList.add(new TrafficData(78, LocalDate.now().atTime(6, 0).minusDays(2)));
		trafficDataList.add(new TrafficData(26, LocalDate.now().atTime(6, 0).minusDays(1)));
		trafficDataList.add(new TrafficData(51, LocalDate.now().atTime(6, 0).minusMinutes(120)));
		trafficDataList.add(new TrafficData(4, LocalDate.now().atTime(6, 0).minusMinutes(90)));
		trafficDataList.add(new TrafficData(44, LocalDate.now().atTime(6, 0).minusMinutes(60)));
		trafficDataList.add(new TrafficData(28, LocalDate.now().atTime(6, 0).minusMinutes(30)));
		trafficDataList.add(new TrafficData(10, LocalDate.now().atTime(6, 0)));
		trafficDataList.add(new TrafficData(26, LocalDate.now().atTime(6, 0).plusDays(1)));
	}

	@Test
	public void testTopThreeHalfHrsList() {
		List<TrafficData> topThreeList = loadData.topThreeHalfHrs(trafficDataList);
		List<TrafficData> expectedList = new ArrayList<>();
		expectedList.add(new TrafficData(78, LocalDate.now().atTime(6, 0).minusDays(2)));
		expectedList.add(new TrafficData(51, LocalDate.now().atTime(6, 0).minusMinutes(120)));
		expectedList.add(new TrafficData(44, LocalDate.now().atTime(6, 0).minusMinutes(60)));
		assertEquals(topThreeList, expectedList);
	}

	@Test
	public void testTopThreeHalfHrsList_EmptyList() {
		List<TrafficData> topThreeList = loadData.topThreeHalfHrs(new ArrayList<>());
		List<TrafficData> expectedList = new ArrayList<>();
		assertEquals(topThreeList, expectedList);
	}

	@Test
	public void testPerDayList() {
		List<TrafficData> perDayList = loadData.preparePerDayList(trafficDataList);
		List<TrafficData> expectedList = new ArrayList<>();
		expectedList.add(new TrafficData(45, LocalDate.now().atTime(6, 0).minusDays(3)));
		expectedList.add(new TrafficData(78, LocalDate.now().atTime(6, 0).minusDays(2)));
		expectedList.add(new TrafficData(26, LocalDate.now().atTime(6, 0).minusDays(1)));
		expectedList.add(new TrafficData(137, LocalDate.now().atTime(6, 0).minusMinutes(120)));
		expectedList.add(new TrafficData(26, LocalDate.now().atTime(6, 0).plusDays(1)));
		assertEquals(perDayList, expectedList);
	}

	@Test
	public void testPerDayList_EmptyList() {
		List<TrafficData> perDayList = loadData.preparePerDayList(new ArrayList<>());
		List<TrafficData> expectedList = new ArrayList<>();
		assertEquals(perDayList, expectedList);
	}

	@Test
	public void testOneAndHrList() {
		int minCountOneAndHalfHr = loadData.getMinCountOneAndHalfHr(trafficDataList);
		assertEquals(minCountOneAndHalfHr, 76);
	}

	@Test
	public void testOneAndHrList_EmptyList() {
		int minCountOneAndHalfHr = loadData.getMinCountOneAndHalfHr(new ArrayList<>());
		assertEquals(minCountOneAndHalfHr, 0);
	}

}
