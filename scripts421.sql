ALTER TABLE student
    ADD CONSTRAINT age CHECK ( age >= 16 ); --Возраст студента не может быть меньше 16 лет.

ALTER TABLE student
    ADD CONSTRAINT name UNIQUE (name),
    ALTER COLUMN name SET NOT NULL ;--Имена студентов должны быть уникальными и не равны нулю.

ALTER TABLE faculty
    ADD CONSTRAINT color UNIQUE (color, name);--Пара “значение названия” - “цвет факультета” должна быть уникальной.

ALTER TABLE student
    ALTER COLUMN age SET DEFAULT 20;--При создании студента без возраста ему автоматически должно присваиваться 20 лет.