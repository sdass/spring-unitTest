package com.subra;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OneController {
	
	@Autowired
	OneService oneservice;

	@GetMapping("/")
	String getHello(){
		return "hello world";
	}
	
	@GetMapping("/svc")
	String getHellosvc(){
		String ss = oneservice.getOne();
		return ss;
	}
	@GetMapping("/node")
	MyNode getMyNodesvc(){
		MyNode m = oneservice.getMyNode();
		return m;
	}
	
	@GetMapping("/repo")
	MyNode getMyNoderepo(){
		MyNode m = oneservice.getMyNodeFromRepo();
		return m;
	}
	
	@PostMapping(value="/postjson")
	MyNode postMyNoderepo(@RequestBody MyNode myin){
		System.out.println("myn=" + myin);
		MyNode out = oneservice.getMyNodeFromRepo();
		out.name= myin.name;
		System.out.println("out=" + out);
		return out;
	}
	
	
}
