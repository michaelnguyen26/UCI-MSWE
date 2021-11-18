SELECT * FROM student_course_schedule.link_student_course;

SELECT * FROM student_course_schedule.students;

SELECT * FROM student_course_schedule.courses;

SELECT CONCAT(first_name, last_name) AS Student, course_name AS "Course Name",
COUNT(*) AS "Student in Course"
FROM student_course_schedule.link_student_course lsc 
JOIN student_course_schedule.courses c  
	ON c.course_id = lsc.course_id 
JOIN student_course_schedule.students s    
	ON s.student_id = lsc.student_id 
WHERE s.student_id IN (1,2)
GROUP BY course_name; 


SELECT s.student_id, CONCAT(first_name, last_name) AS Student, c.course_id, course_name AS Course 
FROM student_course_schedule.link_student_course lsc JOIN student_course_schedule.courses c
	ON c.course_id = lsc.course_id 
JOIN student_course_schedule.students s
	ON s.student_id = lsc.student_id 
ORDER BY Student;


INSERT INTO student_course_schedule.link_student_course 
	(student_id, course_id)
 SELECT DISTINCT 1, 4
 WHERE NOT EXISTS
 (SELECT *
 FROM student_course_schedule.link_student_course 
 WHERE student_id = 1 AND course_id = 4);
 
CREATE TABLE IF NOT EXISTS student_course_schedule.courses
(course_id INTEGER PRIMARY KEY AUTO_INCREMENT,
course_name VARCHAR(20) NOT NULL UNIQUE,
course_day VARCHAR (15) NOT NULL,
course_time VARCHAR(15) NOT NUll); 

DROP DATABASE student_course_schedule;

INSERT INTO student_course_schedule.link_student_course (student_id, course_id) 
SELECT 1, 2
WHERE NOT EXISTS 
	(SELECT *
	 FROM student_course_schedule.link_student_course lsc JOIN student_course_schedule.courses c
	 ON c.course_id = lsc.course_id
	 WHERE lsc.student_id = 1 AND c.course_id = 2 AND c.course_time = "10:00 - 10:50");
     
INSERT INTO student_course_schedule.link_student_course (student_id, course_id) 
SELECT 1, 2      
WHERE NOT EXISTS 
(SELECT * 
FROM student_course_schedule.link_student_course lsc 
JOIN student_course_schedule.courses c 
	ON c.course_id = lsc.course_id 
WHERE lsc.student_id = 1 AND c.course_id = 2 AND c.course_time = "12:00 - 12:50");


SELECT * 
FROM student_course_schedule.link_student_course lsc 
JOIN student_course_schedule.courses c 
	ON c.course_id = lsc.course_id 
WHERE lsc.student_id = 1 AND c.course_id = 2 AND c.course_time = "12:00 - 12:50";
     
SELECT * FROM student_course_schedule.link_student_course;
    
SELECT course_time AS Time FROM courses;