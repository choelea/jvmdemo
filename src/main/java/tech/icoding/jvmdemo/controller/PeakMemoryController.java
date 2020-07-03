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
		int level2 = 40000000;
		int level1 = 2000;
		Object[] objs = new Object[level1];
		LOG.info("\n=================> OOM test started..\n");
		for (int outerIterator = 1; outerIterator < level1; outerIterator++) {
			LOG.info("Iteration " + outerIterator + " Free Mem: " + Runtime.getRuntime().freeMemory());
			int loop1 = 2;
			final Object[] memoryFillIntVar = new Object[level2];
			// feel memoryFillIntVar array in loop..
			do {
				memoryFillIntVar[loop1] = 0;
				loop1--;
			} while (loop1 > 0);
			objs[outerIterator] = memoryFillIntVar;
			Thread.sleep(1000);
		}
	}
}


