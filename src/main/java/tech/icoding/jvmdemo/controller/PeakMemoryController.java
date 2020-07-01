package tech.icoding.jvmdemo.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/memory")
public class PeakMemoryController {
	private static final Logger LOG = LoggerFactory.getLogger(PeakMemoryController.class);
	public ResponseEntity<String> peak(){
		return ResponseEntity.ok("cool");
	}
	
	@GetMapping("/peak")
	public void generateOOM() throws Exception {
		int iteratorValue = 20;
		LOG.info("\n=================> OOM test started..\n");
		for (int outerIterator = 1; outerIterator < 20; outerIterator++) {
			LOG.info("Iteration " + outerIterator + " Free Mem: " + Runtime.getRuntime().freeMemory());
			int loop1 = 2;
			final int[] memoryFillIntVar = new int[iteratorValue];
			// feel memoryFillIntVar array in loop..
			do {
				memoryFillIntVar[loop1] = 0;
				loop1--;
			} while (loop1 > 0);
			iteratorValue = iteratorValue * 5;
			LOG.info("\nRequired Memory for next loop: " + iteratorValue);
			Thread.sleep(1000);
		}
	}
}


