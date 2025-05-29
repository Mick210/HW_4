package ru.hogwarts.school.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.FacultyRepository;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Service
public class FacultyService {
    private final FacultyRepository facultyRepository;

    private final static Logger logger = LoggerFactory.getLogger(StudentService.class);

    public FacultyService(FacultyRepository facultyRepository) {
        this.facultyRepository = facultyRepository;
    }

    public Faculty createFaculty(Faculty faculty) {
        Faculty createFaculty = facultyRepository.save(faculty);
        logger.info("Метод: createFaculty {}", createFaculty);
        return createFaculty;
    }

    public Faculty findFaculty(Long id) {
        Faculty findFaculty = facultyRepository.findFacultyById(id);
        logger.info("Метод: findFaculty {}", findFaculty);
        return findFaculty;
    }

    public Faculty updateFaculty(long id, Faculty faculty) {
        Faculty updatedFaculty = facultyRepository.findFacultyById(id);
        logger.info("Метод: updateFaculty {}", updatedFaculty);
        if (updatedFaculty == null) {
            return null;
        }
        updatedFaculty.setName(faculty.getName());
        updatedFaculty.setColor(faculty.getColor());
        return facultyRepository.save(updatedFaculty);
    }

    public void deleteFaculty(Long id) {
        logger.info("Метод: deleteFaculty {}", facultyRepository.findFacultyById(id));
        facultyRepository.deleteById(id);
    }

    public Faculty findByName(String name) {
        Faculty findByName = facultyRepository.findFacultyByNameIgnoreCase(name);
        logger.info("Метод: findByName {}", findByName);
        return findByName;
    }

    public Faculty findByColor(String color) {
        Faculty findByColor = facultyRepository.findFacultyByColorIgnoreCase(color);
        logger.info("Метод: findByColor {}", findByColor);
        return findByColor;
    }

    public Collection<Student> getStudentsOfFaculty(long id) {
        Faculty getStudentsOfFaculty = facultyRepository.findFacultyById(id);
        logger.info("Метод: getStudentsOfFaculty {}", getStudentsOfFaculty);
        return (getStudentsOfFaculty != null) ? getStudentsOfFaculty.getStudent() : Collections.emptyList();
    }

    public Collection<Faculty> filterColor(String color) {
        Collection<Faculty> filterColor = facultyRepository.findAll()
                .stream()
                .filter(faculty -> faculty.getColor().equals(color))
                .collect(Collectors.toList());
        logger.info("Метод: filterColor {}", filterColor);
        return filterColor;
    }

    public String getLongNameFaculty() {
        String longName = facultyRepository.findAll()
                .stream()
                .map(Faculty::getName)
                .max(Comparator.comparing(String::length))
                .orElse("");
        logger.info("Метод: getLongNameFaculty {}", longName);
        System.out.println("longName = " + longName);
        return longName;
    }

    public long getInteger() {
        long startTime = System.currentTimeMillis();
        int sum = Stream.iterate(1, a -> a + 1)
                .limit(1_000_000)
                .reduce(0, (a, b) -> a + b);
        long endTime = System.currentTimeMillis();
        long time1 = endTime - startTime;
        logger.info("Метод без парралельных стримов {} миллисикунды", time1);
        return time1;
    }

    public Long getInteger_parallel_stream() {
        long startTime = System.currentTimeMillis();
        int sum =  Stream.iterate(1, a -> a + 1)
                .limit(1_000_000)
                .parallel()
                .reduce(0, (a, b) -> a + b);
        long endTime = System.currentTimeMillis();
        long time2 = endTime - startTime;
        logger.info("Метод c парралельными стримами {} миллисикунды", time2);
        return time2;
    }
}
