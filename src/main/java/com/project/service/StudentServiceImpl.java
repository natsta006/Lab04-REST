package com.project.service;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.project.model.Student;
import com.project.repository.StudentRepository;

@Service
public class StudentServiceImpl implements StudentService {
	private StudentRepository studentRepository;
	
	 @Autowired 
	 public StudentServiceImpl(StudentRepository studentRepository) {
	 this.studentRepository = studentRepository;
	 }

	@Override
	public Optional<Student> getStudent(Integer studentId) {
		return studentRepository.findById(studentId);
	}

	@Override
	public Student setStudent(Student student) {
		return studentRepository.save(student);
	}

	@Override
	public void deleteStudent(Integer studentId) {
		studentRepository.deleteById(studentId);
	}

	@Override
	public Page<Student> getStudenci(Pageable pageable) {
		return studentRepository.findAll(pageable);
	}

	@Override
	public Optional<Student> searchByIndeks(String nrIndeksu) {
		return studentRepository.findByNrIndeksu(nrIndeksu);
	}

	@Override
	public Page<Student> searchByNrIndeksuStartsWith(String nrIndeksu, Pageable pageable) {
		return studentRepository.findByNrIndeksuStartsWith(nrIndeksu, pageable);
	}

	@Override
	public Page<Student> searchByNazwiskoStartsWith(String nazwisko, Pageable pageable) {
		return studentRepository.findByNazwiskoStartsWithIgnoreCase(nazwisko, pageable);
	}

}
