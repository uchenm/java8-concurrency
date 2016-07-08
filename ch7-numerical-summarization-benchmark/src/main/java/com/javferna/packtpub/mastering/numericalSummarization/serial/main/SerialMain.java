package com.javferna.packtpub.mastering.numericalSummarization.serial.main;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.javferna.packtpub.mastering.numericalSummarization.common.Record;
import com.javferna.packtpub.mastering.numericalSummarization.serial.data.SerialDataLoader;
import com.javferna.packtpub.mastering.numericalSummarization.serial.data.SerialStatistics;

public class SerialMain {

	static Map<String, List<Double>> totalTimes = new LinkedHashMap<>();
	static List<Record> records;

	private static void measure(String name, Runnable r) {
		long start = System.nanoTime();
		r.run();
		long end = System.nanoTime();
		totalTimes.computeIfAbsent(name, k -> new ArrayList<>()).add((end - start) / 1_000_000.0);
	}

	public static void main(String[] args) throws IOException {
		Path path = Paths.get("data","bank-additional-full.csv");
		records = SerialDataLoader.load(path);
		
		for (int i = 0; i < 10; i++) {
			measure("Job Info", () -> SerialStatistics.jobDataFromSubscribers(records));
			measure("Age Info", () -> SerialStatistics.ageDataFromSubscribers(records));
			measure("Marital Info", () -> SerialStatistics.maritalDataFromSubscribers(records));
			measure("Multiple Filter", () -> SerialStatistics.multipleFilterData(records));
			measure("Multiple Filter Predicate", () -> SerialStatistics.multipleFilterDataPredicate(records));
			measure("Duration Data", () -> SerialStatistics.durationDataForNonSuscribers(records));
			measure("Number of Contacts", () -> SerialStatistics.campaignDataFromNonSubscribersOk(records));
			measure("People Between 25 and 50", () -> SerialStatistics.peopleBetween25and50(records));
		}

		totalTimes.forEach((name, times) -> System.out.printf("%25s: %s [avg: %6.2f] ms%n", name,
				times.stream().map(t -> String.format("%6.2f", t)).collect(Collectors.joining(" ")),
				times.stream().mapToDouble(Double::doubleValue).average().getAsDouble()));
	}

}
