package com.ashish.controllers;

import java.io.IOException;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

	
	@GetMapping
	public String hellow() {
		return "hellow";
	}
	
	@GetMapping("/create")
	public String createFile(@RequestParam int num) throws IOException {
		return "hellow";
	}
}
