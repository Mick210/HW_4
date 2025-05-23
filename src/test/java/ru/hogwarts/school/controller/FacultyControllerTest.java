package ru.hogwarts.school.controller;

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
import ru.hogwarts.school.repository.FacultyRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FacultyControllerTest {
    @LocalServerPort
    private int port;

    @Autowired
    private FacultyController facultyController;
    @Autowired
    private FacultyRepository facultyRepository;
    @Autowired
    private TestRestTemplate restTemplate;

    @AfterEach
    public void cleanUp() {
        facultyRepository.deleteAll();
    }

    @Test
    void contextLoads() throws Exception {
        assertThat(facultyController).isNotNull();
    }

    @Test
    void testCreateFaculty() throws Exception {
        Faculty expectedFaculty = new Faculty();
        expectedFaculty.setId(12L);
        expectedFaculty.setName("GazMias");
        expectedFaculty.setColor("Blue");
        facultyRepository.save(expectedFaculty);
        Faculty faculty = this.restTemplate.postForObject(
                "http://localhost:" + port + "/faculty", expectedFaculty, Faculty.class);
        assertThat(faculty).isNotNull();

        ResponseEntity<Faculty> responseEntity = (this.restTemplate.getForEntity(
                "http://localhost:" + port + "/faculty/" + expectedFaculty.getId(), Faculty.class));
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedFaculty, responseEntity.getBody());
    }

    @Test
    void testFindFaculty() throws Exception {
        Faculty expect = new Faculty();
        expect.setId(12L);
        expect.setName("GazMias");
        expect.setColor("Blue");
        facultyRepository.save(expect);
        Faculty faculty = this.restTemplate.getForObject(
                "http://localhost:" + port + "/faculty/" + expect.getId(), Faculty.class);
        assertThat(faculty).isEqualTo(expect);

        ResponseEntity<Faculty> responseEntity = this.restTemplate.getForEntity(
                "http://localhost:" + port + "/faculty/" + expect.getId(), Faculty.class);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expect, responseEntity.getBody());
    }

    @Test
    void testEditFaculty() throws Exception {
        Faculty expect = new Faculty(123L, "Spartak", "Red");
        facultyRepository.save(expect);
        Faculty changedExpect = new Faculty(expect.getId(), "Zenit", "Blue");
        this.restTemplate.put(
                "http://localhost:" + port + "/faculty", changedExpect);

        ResponseEntity<Void> responseEntity = this.restTemplate.exchange(
                "http://localhost:" + port + "/faculty",
                HttpMethod.PUT, new HttpEntity<>(changedExpect), Void.class);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        Faculty faculty = this.restTemplate.getForObject(
                "http://localhost:" + port + "/faculty/" + changedExpect.getId(), Faculty.class);
        assertThat(changedExpect).isEqualTo(faculty);
    }

    @Test
    void testDeleteFaculty() throws Exception {
        Faculty facultyToDelete = new Faculty(123L, "Spartak", "Red");
        facultyRepository.save(facultyToDelete);
        ResponseEntity<Void> responseEntity = this.restTemplate.exchange(
                "http://localhost:" + port + "/faculty/" + facultyToDelete.getId(),
                HttpMethod.DELETE, null, Void.class);

        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());

        Faculty deletedFaculty = this.restTemplate.getForObject(
                "http://localhost:" + port + "/faculty/" + facultyToDelete.getId(), Faculty.class);
        assertThat(deletedFaculty.getId()).isNull();
        assertThat(deletedFaculty.getName()).isNull();
        assertThat(deletedFaculty.getColor()).isNull();
        ResponseEntity<Faculty> deletedResponseEntity = this.restTemplate.getForEntity(
                "http://localhost:" + port + "/faculty/" + deletedFaculty.getId(), Faculty.class);
        assertEquals(HttpStatus.BAD_REQUEST, deletedResponseEntity.getStatusCode());
    }
}
