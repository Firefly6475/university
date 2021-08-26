INSERT INTO student(student_id, student_name, student_birthday, student_course) VALUES ('aabb', 'Nikolay', '2007-01-01', '1'), ('bbcc', 'Max', '2008-01-01', '2'), ('ccdd', 'Alex', '2009-01-01', '2'), ('ddee', 'Sam', '2010-01-01', '3'), ('eeff', 'John', '2011-01-01', '1'), ('ffgg', 'Stanislav', '2001-01-01', '4')
INSERT INTO teacher(teacher_id, teacher_name, teacher_birthday, teacher_salary) VALUES ('aabb', 'Noah', '2007-01-01', '750'), ('bbcc', 'Liam', '2006-01-01', '850'), ('ccdd', 'Oliver', '2005-01-01', '950'), ('ddee', 'Olivia', '2004-01-01', '1250'), ('eeff', 'Emma', '2003-01-01', '1500'), ('ffgg', 'Timofey', '2001-01-01', '400')
INSERT INTO "group"(group_id, group_name) VALUES ('aabb', 'BIb-16I1'), ('bbcc', 'PI-18I1'), ('ccdd', 'ASb-18I1')
INSERT INTO group_student("group", student) VALUES ('aabb', 'ccdd'), ('aabb', 'bbcc'), ('aabb', 'ddee'), ('bbcc', 'aabb'), ('bbcc', 'eeff'), ('ccdd', 'ccdd'), ('ccdd', 'bbcc')
INSERT INTO faculty(faculty_id, faculty_name) VALUES ('aabb', 'Information Systems'), ('bbcc', 'Automobile Roads')
INSERT INTO faculty_group(faculty, "group") VALUES ('aabb', 'aabb'), ('aabb', 'bbcc'), ('bbcc', 'bbcc')
INSERT INTO department(department_id, department_name) VALUES ('aabb', 'Information Security'), ('bbcc', 'Information Technologies'), ('ccdd', 'Automobile structure')
INSERT INTO department_teacher(department, teacher) VALUES ('aabb', 'eeff'), ('aabb', 'ddee'), ('bbcc', 'ccdd'), ('bbcc', 'bbcc'), ('ccdd', 'aabb')
INSERT INTO discipline(discipline_id, discipline_name) VALUES ('aabb', 'Programming Languages'), ('bbcc', 'Automobile parts'), ('ccdd', 'Information Security')
INSERT INTO discipline_teacher(discipline, teacher) VALUES ('aabb', 'aabb'), ('aabb', 'bbcc'), ('bbcc', 'ccdd'), ('ccdd', 'ddee'), ('ccdd', 'eeff')
INSERT INTO audience(audience_id, audience_number, audience_floor) VALUES ('aabb', '251', '2'), ('bbcc', '328', '3'), ('ccdd', '113', '1')
INSERT INTO lesson(lesson_id, discipline, audience, lesson_type, "date", time_start, time_end, teacher, "group") VALUES ('aabb', 'aabb', 'aabb', 'LECTURE', '2021-07-08', '13:45:00', '15:20:00', 'bbcc', 'aabb'), ('bbcc', 'bbcc', 'bbcc', 'LAB', '2021-07-09', '11:30:00', '13:00:00', 'ccdd', 'bbcc')