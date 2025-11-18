package pe.edu.upeu.microservice_course_management;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import pe.edu.upeu.microservice_course_management.infrastructure.adapters.output.persistence.entity.*;
import pe.edu.upeu.microservice_course_management.infrastructure.adapters.output.persistence.repository.*;

import java.time.Duration;
import java.util.ArrayList;


@SpringBootApplication
@RequiredArgsConstructor
public class MicroserviceCourseManagementApplication implements CommandLineRunner {

	private final CourseRepository courseRepository;
	private final CycleRepository cycleRepository;
	private final GroupRepository groupRepository;
	private final FacultyRepository facultyRepository;
	private final ProfessionalSchoolRepository professionalSchoolRepository;
	private final CourseTypeRepository courseTypeRepository;
	private final PlanRepository planRepository;
	private final CourseAssignmentRepository courseAssignmentRepository;
	private final TeacherRepository teacherRepository;
    private final CourseAssignmentCourseRepository courseAssignmentCourseRepository;

    public static void main(String[] args) {
		SpringApplication.run(MicroserviceCourseManagementApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		FacultyEntity faculty1 = new FacultyEntity(null, "Ciencias de la Salud", new ArrayList<>());
		faculty1 = facultyRepository.save(faculty1);

		ProfessionalSchoolEntity professionalSchool1 = new ProfessionalSchoolEntity(null,"NutryBaby",faculty1, new ArrayList<>());
		professionalSchool1 = professionalSchoolRepository.save(professionalSchool1);

		CycleEntity cycle1 = new CycleEntity(null,"Ciclo I", professionalSchool1, new ArrayList<>());
		cycle1 = cycleRepository.save(cycle1);

		// CourseType

		CourseTypeEntity courseType1 = new CourseTypeEntity(null,"Obligado a ir", new ArrayList<>());
		courseType1 = courseTypeRepository.save(courseType1);


		// Plan
		PlanEntity plan1 = new PlanEntity(null,"2023-1", new ArrayList<>());
		plan1 = planRepository.save(plan1);
		// 1. Cycle

		GroupEntity group1 = new GroupEntity(null, "1",25, cycle1, new ArrayList<>());
		group1 = groupRepository.save(group1);

		CourseEntity course1 = new CourseEntity(null,"Progrmación","202310615","20 y pasas", Duration.ofMinutes(300),Duration.ofMinutes(180),Duration.ofHours(180),Duration.ofHours(360),courseType1,plan1,group1, new ArrayList<>());
		course1 = courseRepository.save(course1);


		TeacherEntity teacher1 = new TeacherEntity(null,"Abel","Falcon","abel@email.com", new ArrayList<>());
		teacher1 = teacherRepository.save(teacher1);


		CourseAssignmentEntity courseAssignment1 = new CourseAssignmentEntity(null,teacher1,new ArrayList<>());
        courseAssignment1 =  courseAssignmentRepository.save(courseAssignment1);

        CourseAssignmentCourseEntity courseAssignmentCourse1 = new CourseAssignmentCourseEntity(null, course1, courseAssignment1);
        courseAssignmentCourse1 = courseAssignmentCourseRepository.save(courseAssignmentCourse1);
	}


}
