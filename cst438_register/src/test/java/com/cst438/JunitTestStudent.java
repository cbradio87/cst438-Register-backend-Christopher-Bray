package com.cst438;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.cst438.controller.StudentController;
import com.cst438.domain.Student;
import com.cst438.domain.StudentDTO;
import com.cst438.domain.StudentRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;


@ContextConfiguration(classes = { StudentController.class })
@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest
public class JunitTestStudent {
	
	static final String URL = "http://localhost.8080";
	
	@Autowired
	private MockMvc mvc;
	
	@MockBean
	StudentRepository studentRepository;
	
	@Test
	public void testAddStudent() throws Exception{
	
		MockHttpServletResponse response;
	
		//create Student DTO which is the body of the HTTP POST message.
		StudentDTO studentDTO = new StudentDTO();
		studentDTO.email="tstudent@csumb.edu";
		studentDTO.name="Test Student";
		studentDTO.status="ok to register";
		studentDTO.statusCode=0;
		
		//create Student entity with primary key that mock studentRepository will return on save 
		Student s = new Student();
		s.setStudent_id(10101);
		
		//create mock bean behavior to the fake database
		//for findByEmail the mock bean should return null
		given(studentRepository.findByEmail("tstudent@csumb.edu")).willReturn(null);
	
		//for save, the mock bean will return a Student entity with primary key 10101
		given(studentRepository.save(any())).willReturn(s);
		
		
		//do the Simulated HTTP POST call to /student, passing studentDTO
		//as the request body
		response = mvc.perform(
				MockMvcRequestBuilders
					.post("/student")
					.content(asJsonString(studentDTO))
					.contentType(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON))
				.andReturn().getResponse();
					
		//check the result of the call
		//check that HTTP status code is OK 200
		assertEquals(200, response.getStatus());
		
		//convert response body from JSON string to a Java object of class StudentDTO
		StudentDTO newStudent = fromJsonString(response.getContentAsString(), StudentDTO.class);
		//check that the primary key of new student was returned in Response Body is not 0.
		assertEquals(10101, newStudent.student_id);
		}
	
	private static String asJsonString(final Object obj) {
		try {

			return new ObjectMapper().writeValueAsString(obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private static <T> T  fromJsonString(String str, Class<T> valueType ) {
		try {
			return new ObjectMapper().readValue(str, valueType);
		} catch (Exception e) {
			throw new 
RuntimeException(e);
		}
	}

}
