package com.cst438.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.cst438.domain.Student;
import com.cst438.domain.StudentDTO;
import com.cst438.domain.StudentRepository;

@RestController
@CrossOrigin(origins = {"http://localhost:3000", "https://registerf-cst438.herokuapp.com/"})
public class StudentController {
	
	@Autowired
	StudentRepository studentRepository;
	
	/*
	 * add a new Student.
	 * RequestBody contains StudentDTO data with student_id 0
	 * Return an updated StudentDTO with student_id of the value generated by the database.
	 */
	@PostMapping("/student")
	public StudentDTO addStudent( @RequestBody StudentDTO newStudent ) {
		// check that student email does not already exist in the database.
		Student student = studentRepository.findByEmail(newStudent.email);
		if (student == null) {
			// Student email does not exist. Create new student
			Student s = new Student();
			s.setEmail(newStudent.email);
			s.setName(newStudent.name);
			s.setStatus(newStudent.status);
			s.setStatusCode(newStudent.statusCode);
			
			//save the student entity to the database.
			//database returns a updated entity with the new primary key
			Student savedStudent = studentRepository.save(s);
			//copy the primary key student id to the DTO, and return the DTO to the client
			newStudent.student_id= savedStudent.getStudent_id();
			
			return newStudent;
			
		} else {
			// Student email already exists. Can no create new student with same email.}
			throw new ResponseStatusException( HttpStatus.BAD_REQUEST, "Student email already exists. ");
		}
	}
}

