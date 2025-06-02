package ru.hogwarts.school.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StudentService {
    private final StudentRepository studentRepository;

    private final static Logger logger = LoggerFactory.getLogger(StudentService.class);

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public Student createStudent(Student student) {
        Student createStudent = studentRepository.save(student);
        logger.info("Метод. Создание нового студента: {}", createStudent);
        return createStudent;
    }

    public Student findStudent(Long id) {
        Student findStudent = studentRepository.findStudentById(id);
        logger.info("Метод. Найти студента: {}", findStudent);
        return findStudent;
    }

    public Student updateStudent(long id, Student student) {
        Student updatedStudent = studentRepository.findStudentById(id);
        logger.info("Метод. Обновить карточку студента: {}", updatedStudent);
        if (updatedStudent == null) {
            return null;
        }
        updatedStudent.setName(student.getName());
        updatedStudent.setAge(student.getAge());
        return studentRepository.save(updatedStudent);
    }

    public void deleteStudent(Long id) {
        logger.info("Метод. Удалить студента: {}", studentRepository.findStudentById(id));
        studentRepository.deleteById(id);
    }

    public Collection<Student> findByAgeBetween(int min, int max) {
        logger.info("Метод. Найти студентов по возрасту от {} до {}", min, max);
        return studentRepository.findByAgeBetween(min, max);
    }

    public Faculty getStudentsFaculty(long id) {
        logger.info("Метод. Найти факультет студента {}", studentRepository.findStudentById(id));
        return studentRepository.findStudentById(id).getFaculty();
    }

    public Collection<Student> filterAge(int age) {
        logger.info("Метод. Поиск студентов по возрасту: {}", age);
        return studentRepository.findAll()
                .stream()
                .filter(student -> student.getAge() == age)
                .collect(Collectors.toList());
    }

    public Integer getAmount() {
        logger.info("Метод. Всего студентов: {}", studentRepository.getAmountOfStudents());
        return studentRepository.getAmountOfStudents();
    }

    public Integer getAverageAge() {
        logger.info("Метод. Средний возраст студентов: {}", studentRepository.getAverageAge());
        return studentRepository.getAverageAge();
    }

    public List<Student> getLastFiveStudents() {
        List<Student> getLastFiveStudents = studentRepository.getLastFiveStudents();
        logger.info("Метод. Посмотреть последних пятерых студентов {}", getLastFiveStudents);
        return getLastFiveStudents;
    }

    public List<String> getAllStudentNameBeginWithLetterA() {
        logger.info("Метод для получения имен всех студентов, чье имя начинается с буквы А");
        return studentRepository.findAll()
                .stream()
                .map(Student::getName)
                .map(String::toUpperCase)
                .filter(name -> name.startsWith("A"))
                .sorted()
                .collect(Collectors.toList());
    }

    public Double getAverageAgeAllStudentStream() {
        logger.info("Метод, который будет возвращать средний возраст всех студентов.");
        return studentRepository.findAll()
                .stream()
                .mapToDouble(Student::getAge)
                .average()
                .orElse(0);
    }

    public void getStudentPrintParallel() {
        logger.info("Метод вывод в консоль имена всех студентов в параллельном режиме");
        List<Student> students = studentRepository.findAll()
                .stream()
                .limit(7)
                .toList();

        printName(students, 0);
        printName(students, 1);

        Thread thread1 = new Thread(() -> {
            printName(students, 2);
            printName(students, 3);
        });
        thread1.start();

        Thread thread2 = new Thread(() -> {
            printName(students, 4);
            printName(students, 5);
        });
        thread2.start();
    }

    private void printName(List<Student> students, int number) {
        System.out.println(students.get(number).getName());
    }

    public void getStudentPrintSynchronized() {
        logger.info("Метод вывод в консоль имена всех студентов в синхронном режиме");
        List<Student> students = studentRepository.findAll()
                .stream()
                .limit(7)
                .toList();

        synchronizedPrintName(students, 0);
        synchronizedPrintName(students, 1);

        Thread thread1 = new Thread(() -> {
            synchronizedPrintName(students, 2);
            synchronizedPrintName(students, 3);
        });
        thread1.start();

        Thread thread2 = new Thread(() -> {
            synchronizedPrintName(students, 4);
            synchronizedPrintName(students, 5);
        });
        thread2.start();
    }

    private synchronized void synchronizedPrintName(List<Student> students, int number) {
        System.out.println(students.get(number).getName());
    }
}
