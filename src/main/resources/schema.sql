DROP TABLE IF EXISTS lesson, audience, department_teacher, discipline_teacher, department, discipline, teacher, faculty_group, faculty, group_student, "group", student;
CREATE TABLE IF NOT EXISTS student(student_id varchar(255), student_email varchar(255), student_password varchar(255), student_name varchar(255), student_birthday date, PRIMARY KEY (student_id));
CREATE TABLE IF NOT EXISTS teacher(teacher_id varchar(255), teacher_email varchar(255), teacher_password varchar(255), teacher_name varchar(255), teacher_birthday date, PRIMARY KEY (teacher_id));
CREATE TABLE IF NOT EXISTS "group"(group_id varchar(255), group_name varchar(255), group_course integer, PRIMARY KEY(group_id));
CREATE TABLE IF NOT EXISTS faculty(faculty_id varchar(255), faculty_name varchar(255), PRIMARY KEY (faculty_id));
CREATE TABLE IF NOT EXISTS department(department_id varchar(255), department_name varchar(255), PRIMARY KEY (department_id));
CREATE TABLE IF NOT EXISTS discipline(discipline_id varchar(255), discipline_name varchar(255), PRIMARY KEY (discipline_id));
CREATE TABLE IF NOT EXISTS group_student("group" varchar(255), student varchar(255),
	FOREIGN KEY ("group") REFERENCES "group"(group_id) ON DELETE CASCADE, FOREIGN KEY (student) REFERENCES student(student_id));
CREATE TABLE IF NOT EXISTS faculty_group(faculty varchar(255), "group" varchar(255),
	FOREIGN KEY (faculty) REFERENCES faculty(faculty_id) ON DELETE CASCADE, FOREIGN KEY ("group") REFERENCES "group"(group_id));
CREATE TABLE IF NOT EXISTS department_teacher(department varchar(255), teacher varchar(255),
	FOREIGN KEY (department) REFERENCES department(department_id) ON DELETE CASCADE, FOREIGN KEY (teacher) REFERENCES teacher(teacher_id));
CREATE TABLE IF NOT EXISTS discipline_teacher(discipline varchar(255), teacher varchar(255),
	FOREIGN KEY (discipline) REFERENCES discipline(discipline_id) ON DELETE CASCADE, FOREIGN KEY (teacher) REFERENCES teacher(teacher_id));
CREATE TABLE IF NOT EXISTS audience(audience_id varchar(255), audience_number integer, audience_floor integer, PRIMARY KEY (audience_id));
CREATE TABLE IF NOT EXISTS lesson(lesson_id varchar(255), discipline varchar(255), audience varchar(255), lesson_type varchar(255),
	"group" varchar(255), teacher varchar(255), "date" date, time_start time without time zone,
		time_end time without time zone, PRIMARY KEY (lesson_id),
			FOREIGN KEY (discipline) REFERENCES discipline(discipline_id), FOREIGN KEY (audience) REFERENCES audience(audience_id),
				FOREIGN KEY ("group") REFERENCES "group"(group_id), FOREIGN KEY (teacher) REFERENCES teacher(teacher_id));