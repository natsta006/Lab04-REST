package com.project.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.project.model.Student;

public interface StudentService {
	 Optional<Student> getStudent(Integer studentId);
	 Student setStudent(Student student);
	 void deleteStudent(Integer studentId);
	 Page<Student> getStudenci(Pageable pageable);
	 Optional<Student> searchByIndeks(String nrIndeksu);
	 Page<Student> searchByNrIndeksuStartsWith(String nrIndeksu, Pageable pageable);
	 Page<Student> searchByNazwiskoStartsWith(String nazwisko, Pageable pageable);
}
