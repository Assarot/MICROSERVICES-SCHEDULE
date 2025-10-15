package pe.edu.upeu.microservice_course_management;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import pe.edu.upeu.microservice_course_management.domain.model.CourseAssignment;
import pe.edu.upeu.microservice_course_management.domain.model.CourseType;
import pe.edu.upeu.microservice_course_management.infrastructure.adapters.output.persistence.entity.*;
import pe.edu.upeu.microservice_course_management.infrastructure.adapters.output.persistence.repository.*;

import java.util.Arrays;
import java.util.List;

@SpringBootApplication
@RequiredArgsConstructor
public class MicroserviceCourseManagementApplication implements CommandLineRunner {

	private final CourseRepository courseRepository;
	private final CycleRepository cycleRepository;
	private final GroupRepository groupRepository;
	private final FacultyRepository facultyRepository;
	private final ProfessionalSchoolRepository professionalSchoolRepository;
	private final CourseModeRepository courseModeRepository;
	private final CourseTypeRepository courseTypeRepository;
	private final PlanRepository planRepository;
	private final CourseAssignmentRepository courseAssignmentRepository;
	private final TeacherRepository teacherRepository;

	public static void main(String[] args) {
		SpringApplication.run(MicroserviceCourseManagementApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		FacultyEntity faculty1 = new FacultyEntity(null,"Ciencias de la Salud");
		faculty1 = facultyRepository.save(faculty1);

		ProfessionalSchoolEntity professionalSchool1 = new ProfessionalSchoolEntity(null,"NutryBaby",faculty1);
		professionalSchool1 = professionalSchoolRepository.save(professionalSchool1);

		CycleEntity cycle1 = new CycleEntity(null,"Ciclo I", professionalSchool1);
		cycle1 = cycleRepository.save(cycle1);

		// CourseType

		CourseTypeEntity courseType1 = new CourseTypeEntity(null,"Obligado a ir");
		courseType1 = courseTypeRepository.save(courseType1);

		//CourseMode

		CourseModeEntity courseMode1 = new CourseModeEntity(null,"Virtual");
		courseMode1 = courseModeRepository.save(courseMode1);

		// Plan
		PlanEntity plan1 = new PlanEntity(null,"2023-1");
		plan1 = planRepository.save(plan1);
		// 1. Cycle

		GroupEntity group1 = new GroupEntity(null, "1",25, cycle1);
		group1 = groupRepository.save(group1);

		CourseEntity course1 = new CourseEntity(null,"Progrmación","202310615","20 y pasas",4,2,2,4,courseType1,courseMode1,plan1,group1);
		course1 = courseRepository.save(course1);


		TeacherEntity teacher1 = new TeacherEntity(null,"Abel","Falcon","abel@email.com");
		teacher1 = teacherRepository.save(teacher1);

		CourseAssignmentEntity courseAssignment1 = new CourseAssignmentEntity(null,course1,teacher1);

	}


}
