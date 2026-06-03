package pe.edu.upeu.microservice_reservation.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.MonthDay;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Servicio de política de feriados.
 * Los feriados se configuran en application.yml como lista de MM-DD.
 * Si se necesita un calendario académico dinámico en el futuro,
 * se puede reemplazar este servicio por una consulta a un MS de calendario.
 */
@Slf4j
@Service
public class HolidayPolicyService {

    @Value("${reservation.holidays:}")
    private List<String> holidays;

    /**
     * Verifica si una fecha es feriado.
     * @param date fecha a verificar
     * @return true si es feriado
     */
    public boolean isHoliday(LocalDate date) {
        if (holidays == null || holidays.isEmpty()) {
            return false;
        }

        MonthDay toCheck = MonthDay.from(date);
        boolean isHoliday = holidays.stream()
                .map(h -> {
                    String[] parts = h.split("-");
                    return MonthDay.of(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]));
                })
                .anyMatch(md -> md.equals(toCheck));

        if (isHoliday) {
            log.info("Fecha {} corresponde a un feriado", date);
        }
        return isHoliday;
    }

    /**
     * Verifica si alguna fecha en el rango es feriado.
     */
    public boolean rangeContainsHoliday(LocalDate start, LocalDate end) {
        LocalDate current = start;
        while (!current.isAfter(end)) {
            if (isHoliday(current)) {
                return true;
            }
            current = current.plusDays(1);
        }
        return false;
    }
}
