package com.ticketdesk.service;

import com.ticketdesk.Entity.Student;
import com.ticketdesk.Repository.StudentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StudentService {

    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    // Create a new student
    public Student createStudent(Student student) {
        if (student.getEmail() != null && !student.getEmail().trim().isEmpty()) {
            Optional<Student> existing = studentRepository.findByEmail(student.getEmail().trim());
            if (existing.isPresent()) {
                Student s = existing.get();
                if (student.getName() != null && !student.getName().trim().isEmpty()) {
                    s.setName(student.getName().trim());
                    return studentRepository.save(s);
                }
                return s;
            }
        }
        return studentRepository.save(student);
    }

    // Get all students
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    // Get student by ID
    public Student getStudentById(Long id) {

        return studentRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Student not found with ID: " + id
                        )
                );
    }
}
