package ru.hogwarts.school.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.StudentService;

import java.util.Collection;
import java.util.List;


@RestController
@RequestMapping("/student")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping
    public Student createStudent(@RequestBody Student student) {
        return studentService.createStudent(student);
    }

    @GetMapping("{id}")
    public ResponseEntity<Student> findStudent(@PathVariable long id) {
        Student student = studentService.findStudent(id);
        if (student == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(student);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Student> editStudent(@PathVariable long id, @RequestBody Student student) {
        Student updatedStudent = studentService.updateStudent(id, student);
        if (updatedStudent == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedStudent);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable long id) {
        studentService.deleteStudent(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/get-amount")//количество студентов
    public ResponseEntity<Integer> getAmountOfStudent() {
        return ResponseEntity.ok(studentService.getAmount());
    }

    @GetMapping("/get-average-age")//средний возраст студентов
    public ResponseEntity<Integer> getAverageAge() {
        return ResponseEntity.ok(studentService.getAverageAge());
    }

    @GetMapping("/get-5-last-students")
    public ResponseEntity<List<Student>> getLastFiveStudents() {
        List<Student> lastFiveStudents = studentService.getLastFiveStudents();
        if (lastFiveStudents.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(lastFiveStudents);
    }

    @GetMapping("/ageBetween/{min}/{max}")
    public ResponseEntity<Collection<Student>> findByAgeBetween(@PathVariable int min, @PathVariable int max) {
        Collection<Student> students = studentService.findByAgeBetween(min, max);
        if (students.isEmpty()) {
            ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(students);
    }

    @GetMapping("/studentsFaculty/{id}")
    public ResponseEntity<Faculty> getStudentsFaculty(@PathVariable long id) {
        return ResponseEntity.ok(studentService.getStudentsFaculty(id));
    }


    @GetMapping("/ageFilter/{age}")
    public Collection<Student> ageFilteredStudents(@PathVariable int age) {
        return studentService.filterAge(age);
    }
}
