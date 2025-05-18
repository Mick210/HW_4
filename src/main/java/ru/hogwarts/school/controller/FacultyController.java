package ru.hogwarts.school.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.FacultyService;

import java.util.Collection;

@RestController
@RequestMapping("/faculty")
public class FacultyController {
    private final FacultyService facultyService;

    public FacultyController(FacultyService facultyService) {
        this.facultyService = facultyService;
    }

    @PostMapping//create Faculty
    public Faculty createFaculty(@RequestBody Faculty faculty) {
        return facultyService.createFaculty(faculty);
    }

    @GetMapping("{id}")//read Faculty
    public ResponseEntity<Faculty> findFaculty(@PathVariable long id) {
        Faculty foundedFaculty = facultyService.findFaculty(id);
        if (foundedFaculty == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(foundedFaculty);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Faculty> editFaculty(@PathVariable long id, @RequestBody Faculty faculty) {
        Faculty editedFaculty = facultyService.updateFaculty(id, faculty);
        if (editedFaculty == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(editedFaculty);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteFaculty(@PathVariable long id) {
        facultyService.deleteFaculty(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/findByColorOrName")
    public ResponseEntity<Faculty> findFacultyByColorOrName(@RequestParam(required = false) String name,
                                                            @RequestParam(required = false) String color) {
        if (name != null && !name.isBlank()) {
            return ResponseEntity.ok(facultyService.findByName(name));
        }
        if (color != null && !color.isBlank()) {
            return ResponseEntity.ok(facultyService.findByColor(color));
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/studentsOfFaculty/{facultyId}")
    public ResponseEntity<Collection<Student>> getStudentsOfFaculty(@PathVariable long facultyId) {
        Collection<Student> students = facultyService.getStudentsOfFaculty(facultyId);
        if (!students.isEmpty()) {
            return ResponseEntity.ok(students);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/colorFilter/{color}")
    public Collection<Faculty> colorFilteredFaculties(@PathVariable String color) {
        return facultyService.filterColor(color);
    }
}


