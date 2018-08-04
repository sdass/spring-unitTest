package com.subra;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OneService {

	@Autowired
	OneRepository oneRepository;
	
	String getOne(){		
		return "hello one from svc";
	}
	
	MyNode getMyNode(){
		MyNode mynode = new MyNode("Aa", "one");
		System.out.println("-----getMyNode()-----");
		return mynode;
	}	
	
	MyNode getMyNodeFromRepo(){
		System.out.println("-----getMyNodeFromRepo()-----");
		MyNode mn = oneRepository.getNodeRepo();
		return mn;
	}
	
}
