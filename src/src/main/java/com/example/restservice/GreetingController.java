package com.example.restservice;

import java.util.concurrent.atomic.AtomicLong;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;
import java.util.stream.Collectors;

import static java.util.UUID.randomUUID;

@RestController
public class GreetingController {

	private static final String template = "Hello, %s!";
	private final AtomicLong counter = new AtomicLong();

	@GetMapping("/greeting")
	public Greeting greeting(@RequestParam(value = "name", defaultValue = "World") String name) {
		return new Greeting(counter.incrementAndGet(), String.format(template, name));
	}

	@Autowired
	private CounterRepository counterRepository;

	@GetMapping("/persistent_greeting")
	public Greeting persistentGreeting(@RequestParam(value = "name", defaultValue = "World") String name) {
		Counter persistentCounter = counterRepository.findById(1L).orElseGet(() -> new Counter(0L));
		Long persistentCounterValue = persistentCounter.incrementAndGet();
		counterRepository.save(persistentCounter);
		return new Greeting(persistentCounterValue, String.format(template, name));
	}

	@Autowired
	private HaystackRepository haystackRepository;

	@Autowired
	private HaystackUUIDRepository haystackUUIDRepository;

	@GetMapping("/benchmark")
	public String benchmark(@RequestParam(value = "count", defaultValue = "1000") long count) {
		long i = 1L;

		// flush tables
		haystackRepository.truncateTable();
		haystackUUIDRepository.truncateTable();

		// TODO: Initialize Tables
		// while (i < count) {
		//	// get random uuid
		//	//
		//	// generate new Haystack and HaystackUUID objects using uuid
		//	//
		//	// save objects to their respective Database tables
		//	//
		//	i++;
		// }
		// // generate and save needle object at end
		// //
		// // 

		// query1: Basic query with sequential scan
		Haystack query1 = haystackRepository.seqScan();
		String query1Perf = haystackRepository.seqScanPerf().stream().collect(Collectors.joining("\n"));

		// query2: Table join
		Haystack query2 = haystackRepository.tableJoin();
		String query2Perf = haystackRepository.tableJoinPerf().stream().collect(Collectors.joining("\n"));

		// query3: Basic query with indexing
		haystackRepository.indexHash();
		Haystack query3 = haystackRepository.seqScan();
		String query3Perf = haystackRepository.seqScanPerf().stream().collect(Collectors.joining("\n"));

		// query4: Table join with indexing
		haystackUUIDRepository.indexHash();
		Haystack query4 = haystackRepository.tableJoin();
		String query4Perf = haystackRepository.tableJoinPerf().stream().collect(Collectors.joining("\n"));

		return new String(query1.toString() +
							query1Perf + "\n---------------------------------------\n" +
							query2.toString() +
							query2Perf + "\n---------------------------------------\n" +
							query3.toString() +
							query3Perf + "\n---------------------------------------\n" +
							query4.toString()) +
							query4Perf;
	}


}
