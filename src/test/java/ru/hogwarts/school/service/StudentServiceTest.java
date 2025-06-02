package ru.hogwarts.school.service;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class StudentServiceTest {
    @InjectMocks
    private StudentService service;
    @Mock
    private StudentRepository studentRepository;


    static Stream<Arguments> arguments() {
        return Stream.of(Arguments.of(1, "Harry Potter", 11),
                Arguments.of(2, "Hermione Granger", 10),
                Arguments.of(3, "Ronald Weasley", 11, 3));
    }

    @ParameterizedTest
    @MethodSource("arguments")
    void willReturnCreatedStudent(long id, String name, int age) {
        Student student = new Student(id, name, age);
        service.createStudent(student);
        assertNotNull(student);
        assertEquals(name, student.getName());
        assertEquals(age, student.getAge());
    }

    @ParameterizedTest
    @MethodSource("arguments")
    void willReturnFoundedStudent(long id, String name, int age) {
        Student student = new Student(id, name, age);
        Student createdStudent = service.createStudent(student);
        Student foundedStudent = service.findStudent(createdStudent.getId());
        assertEquals(createdStudent, foundedStudent);
    }

    @ParameterizedTest
    @MethodSource("arguments")
    void willReturnEditedStudent(long id, String name, int age) {
        Student student = new Student(id, name, age);
        Student createdStudent = service.createStudent(student);
        Student editedStudent = new Student(createdStudent.getId(), "Updated" + name, age);
        Student result = service.updateStudent(id, editedStudent);
        assertEquals(editedStudent, result);
    }


    @Test
    void filterAge() {
        Student student1 = new Student(1L, "David", 25);
        Student student2 = new Student(2L, "Eva", 25);
        Student student3 = new Student(3L, "Frank", 30);

        service.createStudent(student1);
        service.createStudent(student2);
        service.createStudent(student3);

        assertEquals(25, student1.getAge());
        assertEquals(25, student2.getAge());
    }
}
