package com.subra;

import org.springframework.stereotype.Repository;


@Repository //it is dummy else should be interface and extends JPARepository or CurdRepository interface
public class OneRepository  {
	
	MyNode getNodeRepo(){
		MyNode mynode = new MyNode("Bb", "two");
		System.out.println("-----getNodeRepo()-----");
		return mynode;
	}

}
