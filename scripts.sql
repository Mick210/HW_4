select *
from faculty
where id > 2;
select name
from student;
select *
from faculty
where color in ('Green', 'Purple');
SELECT *
FROM student
WHERE age BETWEEN 10 AND 20;
SELECT *
FROM student
WHERE LOWER(name)
   OR UPPER(name) LIKE '%î%';
SELECT * FROM student
WHERE age < id;
SELECT * FROM student
ORDER BY age;
