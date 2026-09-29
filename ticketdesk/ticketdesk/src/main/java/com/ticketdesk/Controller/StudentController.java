package com.ticketdesk.Controller;

import com.ticketdesk.Entity.Student;
import com.ticketdesk.service.StudentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    // CREATE STUDENT
    @PostMapping
    public ResponseEntity<Student> createStudent(
            @RequestBody(required = false) Student student,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String email) {

        if (student == null) {
            student = new Student();
        }
        if (name != null && !name.trim().isEmpty()) {
            student.setName(name);
        }
        if (email != null && !email.trim().isEmpty()) {
            student.setEmail(email);
        }

        Student savedStudent =
                studentService.createStudent(student);

        return new ResponseEntity<>(
                savedStudent,
                HttpStatus.CREATED
        );
    }

    // GET ALL STUDENTS
    @GetMapping
    public ResponseEntity<List<Student>> getAllStudents() {

        return ResponseEntity.ok(
                studentService.getAllStudents()
        );
    }

    // GET STUDENT BY ID
    @GetMapping("/{id}")
    public ResponseEntity<Student> getStudentById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                studentService.getStudentById(id)
        );
    }
}