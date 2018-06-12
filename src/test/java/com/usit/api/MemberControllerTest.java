package com.usit.api;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;  
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;  
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status; 
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

import com.usit.controller.MemberController;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MemberControllerTest {
	
	Logger logger = Logger.getLogger(this.getClass());
	
	private MockMvc mockMvc;
	
	@Autowired
	private MemberController userController;
	
	@Before
	public void setUp() throws Exception {
		mockMvc = standaloneSetup(userController).build();
	}
	
	@Test
	public void contextLoads() {
	}
	
	@Test
	public void testPost() throws Exception {
		
		MvcResult result = mockMvc.perform(
	                post("/users")
					.param("username", "test@gmail.com")
					.param("password", "password")
					.param("email", "test@gmail.com")
					.param("name", "test@gmail.com")
					.param("genderCd", "1")
					.param("birthDate", "1984-05-26")
					.param("mobile", "01036628388")
					.param("postcode", "12344")
					.param("address", "서울시 중랑구 면목 3.8동 용마한신아파트")
					.param("addressDetail", "101동 1202호")
				)
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn();
		
		logger.info(result.getResponse().getContentAsString());

	}
}
