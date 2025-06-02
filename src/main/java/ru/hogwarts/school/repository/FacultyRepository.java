package ru.hogwarts.school.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.hogwarts.school.model.Faculty;

public interface FacultyRepository extends JpaRepository<Faculty, Long> {
    Faculty findFacultyByNameIgnoreCase(String name);

    Faculty findFacultyByColorIgnoreCase(String color);

    Faculty findFacultyById(long id);

}
