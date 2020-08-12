package tech.icoding.jvmdemo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import tech.icoding.jvmdemo.data.MsgData;

@RestController
public class DemoController {

	@GetMapping("/demo")
	public ResponseEntity<MsgData> demo(){
		return ResponseEntity.ok(new MsgData("demo success"));
	}
}
