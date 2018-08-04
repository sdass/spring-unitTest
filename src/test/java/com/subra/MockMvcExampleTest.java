package com.subra;



import java.io.IOException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JsonContent;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonAnyFormatVisitor;

@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
public class MockMvcExampleTest {

	@Autowired
	MockMvc mockmvc;
	
	@InjectMocks
	OneController oneController;
	
	@Mock
	OneService oneservice;
	//OneService oneservice = Mockito.mock(OneService.class, Mockito.RETURNS_DEEP_STUBS);
	
	@Mock
	OneRepository oneRepository;
	
	@Test
	public void exampleTest() throws Exception {
		ResultActions resultActions = mockmvc.perform(MockMvcRequestBuilders.get("/"));
		resultActions.andDo(MockMvcResultHandlers.print());
		resultActions.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.content().string("hello world"));
	}
	
	@Before
	public void setup1(){
		mockmvc = MockMvcBuilders.standaloneSetup(oneController).build(); //important line
		
		Mockito.when(oneservice.getOne()).thenReturn("hello mock4"); //this can be 1st lin in @Test method
		
		oneservice.oneRepository = oneRepository;
	}
	
	@Test
	public void exampleSvc() throws Exception {
		//Mockito.when(oneservice.getOne()).thenReturn("hello mock5");
		ResultActions resultActions = mockmvc.perform(MockMvcRequestBuilders.get("/svc"));
		resultActions.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.content().string("hello mock4")); //mocked in @Before		
		Mockito.verify(oneservice).getOne();
	}	
	

	/* service return java Object while controller returns json. Passed
	 * Home made solution for json->object convert, then compare with  verifyJson() method. SOLID 
	 * */
	@Test
	public void exampleNode() throws Exception {
		//setup
		MyNode t8 = new MyNode("tt", "88");
		Mockito.when(oneservice.getMyNode()).thenReturn(t8);
		//execute
		//works and simple MvcResult mvcresult = mockmvc.perform(MockMvcRequestBuilders.get("/node")).andReturn();
		//compounded status check for OK AND return result
		MvcResult mvcresult = mockmvc.perform(MockMvcRequestBuilders.get("/node")).andExpect(MockMvcResultMatchers.status().is(200)).andReturn();
		//verify
		System.out.println("mvcresult.content.string=" + mvcresult.getResponse().getContentAsString());		
		//Assert.assertTrue( verifyJson(mvcresult.getResponse().getContentAsString()));
		//or
		Assert.assertTrue( verifyJsonRepo(mvcresult.getResponse().getContentAsString(), t8));
		Mockito.verify(oneservice).getMyNode();

	}	
	
	/*
	 * following exampleNode() approach has repo could not be tested.
	This call Mockito.verify(oneRepository).getNodeRepo(); fails.
	nesting/chaning when, thenReturn() is not supported.
	  Revisit
	*/
	@Test
	public void exampleRepo() throws Exception {
		//setup
		MyNode expectM6 = new MyNode("mm", "66");
		
		
		//Mockito.when(oneRepository.getNodeRepo()).thenReturn(expectM6); not possible to test repo from controller this way
			
		//Mockito.when(oneservice.getMyNodeFromRepo()).thenReturn(oneRepository.getNodeRepo()).thenReturn(expectM6);
		Mockito.when(oneRepository.getNodeRepo()).thenReturn(expectM6); 
		Mockito.when(oneservice.getMyNodeFromRepo()).thenReturn(expectM6);
		
		//execute
		//works and simple MvcResult mvcresult = mockmvc.perform(MockMvcRequestBuilders.get("/node")).andReturn();
		//below compounded: 1. status check for OK AND 2. return result
		MvcResult mvcresult = mockmvc.perform(MockMvcRequestBuilders.get("/repo")).andExpect(MockMvcResultMatchers.status().is(200)).andReturn();
		//verify
		System.out.println("mvcresult.content.string=" + mvcresult.getResponse().getContentAsString());		
		Assert.assertTrue( verifyJsonRepo(mvcresult.getResponse().getContentAsString(), expectM6));
		Mockito.verify(oneservice).getMyNodeFromRepo();
		//FAIL on this line. Not possible to tet repo this way from controller
		//Mockito.verify(oneRepository).getNodeRepo(); // how to make this also to be invoked? May be integration

	}
	
	@Test 
	public void postandReturn() throws Exception{
		//setup
		MyNode expectM9 = new MyNode("doll", "99");
		Mockito.when(oneservice.getMyNodeFromRepo()).thenReturn(expectM9);
		MvcResult mvcresult = mockmvc.perform(MockMvcRequestBuilders.post("/postjson")
				.content(beJsonString(expectM9))
				.contentType("application/json")
				)
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andReturn();
		Assert.assertTrue(verifyJsonRepo(mvcresult.getResponse().getContentAsString(), expectM9));
		Mockito.verify(oneservice).getMyNodeFromRepo();
		
	}
			
	private boolean verifyJson(String parm){
		boolean ret = false;
		
		ObjectMapper objmapper = new ObjectMapper();
		MyNode xm = null;
		System.out.println("parm=" + parm);
		try {
			xm = objmapper.readValue(parm, MyNode.class);
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(xm.name.equals("tt") && xm.value.equals("88")){
			ret= true;
		}else {
			ret = false; 
		}
		return ret;
	}
	
	//this method is reusable, so efficient. MyNode Serializable implements will reduce manual compare
	private boolean verifyJsonRepo(String parm, MyNode expect){
		boolean ret = false;
		
		ObjectMapper objmapper = new ObjectMapper();
		MyNode recvd = null;
		System.out.println("parm=" + parm);
		try {
			recvd = objmapper.readValue(parm, MyNode.class);
		} catch (IOException e) {
			e.printStackTrace();
		}
		ret = recvd.equals(expect);
		return ret;
	}
	
    public static String beJsonString(final Object obj) {
        try {
            final ObjectMapper mapper = new ObjectMapper();
            String retStr = mapper.writeValueAsString(obj);
            System.out.println("retStr=" + retStr);
            return retStr;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
			
}
