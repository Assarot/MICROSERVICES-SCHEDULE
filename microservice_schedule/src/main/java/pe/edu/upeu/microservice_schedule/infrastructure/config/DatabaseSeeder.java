package pe.edu.upeu.microservice_schedule.infrastructure.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import pe.edu.upeu.microservice_schedule.infrastructure.adapter.out.persistence.entity.TypeHourEntity;
import pe.edu.upeu.microservice_schedule.infrastructure.adapter.out.persistence.entity.WeekDayEntity;
import pe.edu.upeu.microservice_schedule.infrastructure.adapter.out.persistence.repository.TypeHourJpaRepository;
import pe.edu.upeu.microservice_schedule.infrastructure.adapter.out.persistence.repository.WeekDayJpaRepository;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class DatabaseSeeder implements CommandLineRunner {

    private final TypeHourJpaRepository typeHourRepository;
    private final WeekDayJpaRepository weekDayRepository;

    @Override
    public void run(String... args) throws Exception {
        log.info("========== RUNNING DATABASE SEEDER ==========");
        seedTypeHours();
        seedWeekDays();
        log.info("========== DATABASE SEEDING COMPLETED ==========");
    }

    private void seedTypeHours() {
        try {
            typeHourRepository.deleteAll();
            log.info("Seeding Type Hours...");
            typeHourRepository.save(TypeHourEntity.builder().idTypeHour(1L).typeHour("HT").build());
            typeHourRepository.save(TypeHourEntity.builder().idTypeHour(2L).typeHour("HP").build());
            log.info("✓ Type Hours seeded successfully");
        } catch (Exception e) {
            log.warn("Could not re-seed Type Hours: {}", e.getMessage());
        }
    }

    private void seedWeekDays() {
        try {
            weekDayRepository.deleteAll();
            log.info("Seeding Week Days (starting with Domingo)...");
            List<String> days = List.of("Domingo", "Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado");
            long id = 1L;
            for (String day : days) {
                weekDayRepository.save(WeekDayEntity.builder()
                        .idSchedule(id++)
                        .name(day)
                        .isActive(true)
                        .build());
            }
            log.info("✓ Week Days seeded successfully");
        } catch (Exception e) {
            log.warn("Could not re-seed Week Days: {}", e.getMessage());
        }
    }
}
