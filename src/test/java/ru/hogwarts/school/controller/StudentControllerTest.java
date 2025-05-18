package ru.hogwarts.school.controller;

import org.assertj.core.api.Assertions;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.StudentRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StudentControllerTest {
    @LocalServerPort
    private int port;
    @Autowired
    private StudentController studentController;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private TestRestTemplate testTemplate;

    @AfterEach
    public void cleanUp() {
        studentRepository.deleteAll();
    }

    @Test
    void testCreateStudent() throws Exception {
        Student expect = new Student();
        expect.setName("Ivan");
        expect.setAge(56);
        studentRepository.save(expect);
        Student student = this.testTemplate.postForObject(
                "http://localhost:" + port + "/student", expect, Student.class);
        Assertions.assertThat(student).isNotNull();

        ResponseEntity<Student> responseEntity = this.testTemplate.getForEntity(
                "http://localhost:" + port + "/student/" + student.getId(), Student.class);

        Assertions.assertThat(HttpStatus.OK).isEqualTo(responseEntity.getStatusCode());
        Assertions.assertThat(expect).isEqualTo(responseEntity.getBody());
    }

    @Test
    void testFindStudent() throws Exception {
        Student expect = new Student();
        expect.setName("Ivan");
        expect.setAge(56);
        studentRepository.save(expect);
        Student student = this.testTemplate.getForObject(
                "http://localhost:" + port + "/student/" + expect.getId(), Student.class);

        Assertions.assertThat(student).isNotNull();
        Assertions.assertThat(student).isEqualTo(expect);

        ResponseEntity<Student> responseEntity = this.testTemplate.getForEntity(
                "http://localhost:" + port + "/student/" + student.getId(), Student.class);
        Assertions.assertThat(HttpStatus.OK).isEqualTo(responseEntity.getStatusCode());
        Assertions.assertThat(expect).isEqualTo(responseEntity.getBody());
    }

    @Test
    void testEditStudent() throws Exception {
        Student expect = new Student();
        expect.setName("Ivan");
        expect.setAge(56);
        studentRepository.save(expect);
        Student changedStudent = new Student(expect.getId(), "Fedor", 72);

        this.testTemplate.put("http://localhost:" + port + "/student", changedStudent);

        ResponseEntity<Void> responseEntity = this.testTemplate.exchange(
                "http://localhost:" + port + "/faculty",
                HttpMethod.PUT, new HttpEntity<>(changedStudent), Void.class);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        Student student = this.testTemplate.getForObject(
                "http://localhost:" + port + "/student/" + changedStudent.getId(), Student.class);
        assertThat(changedStudent).isEqualTo(student);
    }
    @Test
    void testDeleteStudent() throws Exception {
        Student studentToDelete = new Student(123L, "Spartak", 15);
        studentRepository.save(studentToDelete);
        ResponseEntity<Void> responseEntity = this.testTemplate.exchange(
                "http://localhost:" + port + "/faculty/" + studentToDelete.getId(),
                HttpMethod.DELETE, null, Void.class);

        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());

        Faculty deletedStudent = this.testTemplate.getForObject(
                "http://localhost:" + port + "/faculty/" + studentToDelete.getId(), Faculty.class);
        assertThat(deletedStudent.getId()).isNull();
        assertThat(deletedStudent.getName()).isNull();
        assertThat(deletedStudent.getColor()).isNull();
        ResponseEntity<Faculty> deletedResponseEntity = this.testTemplate.getForEntity(
                "http://localhost:" + port + "/faculty/" + deletedStudent.getId(), Faculty.class);
        assertEquals(HttpStatus.BAD_REQUEST, deletedResponseEntity.getStatusCode());
    }
}
