package pe.edu.upeu.microserviceimport.service;

import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pe.edu.upeu.microserviceimport.client.EnvironmentClient;
import pe.edu.upeu.microserviceimport.dto.AcademicSpaceDTO;
import pe.edu.upeu.microserviceimport.dto.BuildingDTO;
import pe.edu.upeu.microserviceimport.dto.FloorDTO;
import pe.edu.upeu.microserviceimport.dto.PrimaryDataImportResult;
import pe.edu.upeu.microserviceimport.dto.StateDTO;
import pe.edu.upeu.microserviceimport.dto.TypeAcademicSpaceDTO;
import pe.edu.upeu.microserviceimport.dto.request.AcademicSpaceCreateRequest;
import pe.edu.upeu.microserviceimport.dto.request.BuildingCreateRequest;
import pe.edu.upeu.microserviceimport.dto.request.FloorCreateRequest;
import pe.edu.upeu.microserviceimport.dto.request.StateCreateRequest;
import pe.edu.upeu.microserviceimport.dto.request.TypeAcademicSpaceCreateRequest;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class PrimaryDataImportService {

    private static final Logger log = LoggerFactory.getLogger(PrimaryDataImportService.class);
    private final EnvironmentClient environmentClient;
    private final DataFormatter dataFormatter = new DataFormatter();

    public PrimaryDataImportService(EnvironmentClient environmentClient) {
        this.environmentClient = environmentClient;
    }

    public PrimaryDataImportResult importPrimaryData(MultipartFile file) throws IOException {
        try (InputStream inputStream = file.getInputStream();
             Workbook workbook = WorkbookFactory.create(inputStream)) {

            Map<String, StateDTO> states = new HashMap<>();
            Map<String, TypeAcademicSpaceDTO> types = new HashMap<>();
            Map<String, BuildingDTO> buildings = new HashMap<>();
            Map<String, FloorDTO> floors = new HashMap<>();
            Map<String, AcademicSpaceDTO> academicSpaces = new HashMap<>();

            int statesCreated = 0;
            int typesCreated = 0;
            int buildingsCreated = 0;
            int floorsCreated = 0;
            int academicSpacesCreated = 0;

            // Procesar todas las hojas
            for (int sheetIndex = 0; sheetIndex < workbook.getNumberOfSheets(); sheetIndex++) {
                Sheet sheet = workbook.getSheetAt(sheetIndex);
                String sheetName = sheet.getSheetName().toLowerCase().trim();

                if (sheetName.contains("básico") || sheetName.contains("basico") || sheetName.contains("datos") || sheetIndex == 0) {
                    // Hoja de datos básicos (Estados, Tipos, Edificios, Pisos)
                    log.info("Procesando hoja de datos básicos: {}", sheet.getSheetName());
                    Map<String, Integer> headerIndex = buildHeaderIndex(sheet.getRow(0));
                    if (!headerIndex.isEmpty()) {
                        Map<String, StateDTO> existingStates = loadExistingStates();
                        Map<String, TypeAcademicSpaceDTO> existingTypes = loadExistingTypes();
                        Map<String, BuildingDTO> existingBuildings = loadExistingBuildings();
                        Map<String, FloorDTO> existingFloors = loadExistingFloors(existingBuildings);

                        for (int rowIndex = 1; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
                            Row row = sheet.getRow(rowIndex);
                            if (row == null || isRowEmpty(row)) {
                                continue;
                            }

                            String stateName = getCellValue(row, headerIndex, "state", "statename", "estado");
                            Character stateActive = getCellCharValue(row, headerIndex, "stateactive", "stateisactive", "estadoactivo", "estadoisactivo");
                            String typeName = getCellValue(row, headerIndex, "typeacademicspace", "tipoacademico", "typeacademicspacename");
                            Character typeActive = getCellCharValue(row, headerIndex, "typeactive", "typeisactive", "tipoactivo", "tipoacademicoactivo");
                            String buildingName = getCellValue(row, headerIndex, "building", "buildingname", "edificio");
                            Character buildingActive = getCellCharValue(row, headerIndex, "buildingactive", "buildingisactive", "edificioactivo");
                            Integer floorNumber = getCellIntValue(row, headerIndex, "floornumber", "floor", "piso", "pisonumero");
                            Character floorActive = getCellCharValue(row, headerIndex, "flooractive", "floorisactive", "pisoactivo");
                            String floorBuildingName = getCellValue(row, headerIndex, "floorbuilding", "buildingforfloor", "edificiopiso");

                            if (!stateName.isBlank()) {
                                StateDTO state = createOrUpdateState(existingStates, stateName, stateActive);
                                if (state != null && state.getIdState() != null && !existingStates.containsKey(normalizeKey(stateName))) {
                                    existingStates.put(normalizeKey(stateName), state);
                                    statesCreated++;
                                }
                            }
                            if (!typeName.isBlank()) {
                                TypeAcademicSpaceDTO typeAcademicSpace = createOrUpdateType(existingTypes, typeName, typeActive);
                                if (typeAcademicSpace != null && typeAcademicSpace.getIdTypeAcademicSpace() != null && !existingTypes.containsKey(normalizeKey(typeName))) {
                                    existingTypes.put(normalizeKey(typeName), typeAcademicSpace);
                                    typesCreated++;
                                }
                            }
                            if (!buildingName.isBlank()) {
                                BuildingDTO building = createOrUpdateBuilding(existingBuildings, buildingName, buildingActive);
                                if (building != null && building.getIdBuilding() != null && !existingBuildings.containsKey(normalizeKey(buildingName))) {
                                    existingBuildings.put(normalizeKey(buildingName), building);
                                    buildingsCreated++;
                                }
                            }

                            String effectiveBuildingName = !buildingName.isBlank() ? buildingName : floorBuildingName;
                            if (floorNumber != null && floorNumber > 0 && !effectiveBuildingName.isBlank()) {
                                String normalizedBuildingName = normalizeKey(effectiveBuildingName);
                                BuildingDTO floorBuilding = existingBuildings.get(normalizedBuildingName);
                                if (floorBuilding == null) {
                                    floorBuilding = createOrUpdateBuilding(existingBuildings, effectiveBuildingName, 'A');
                                    if (floorBuilding != null && floorBuilding.getIdBuilding() != null && !existingBuildings.containsKey(normalizedBuildingName)) {
                                        existingBuildings.put(normalizedBuildingName, floorBuilding);
                                        buildingsCreated++;
                                    }
                                }
                                if (floorBuilding != null) {
                                    if (!floorExists(existingFloors, floorBuilding, floorNumber)) {
                                        FloorDTO floor = createOrUpdateFloor(existingFloors, floorNumber, floorActive, floorBuilding);
                                        if (floor != null && floor.getIdFloor() != null) {
                                            existingFloors.put(buildingFloorKey(floorBuilding, floorNumber), floor);
                                            floorsCreated++;
                                        }
                                    }
                                }
                            }
                        }

                        states.putAll(existingStates);
                        types.putAll(existingTypes);
                        buildings.putAll(existingBuildings);
                        floors.putAll(existingFloors);
                    }

                } else if (sheetName.contains("ambiente") || sheetName.contains("espacio") || sheetName.contains("academic")) {
                    // Hoja de ambientes académicos
                    log.info("Procesando hoja de ambientes académicos: {}", sheet.getSheetName());
                    Map<String, Integer> headerIndex = buildHeaderIndex(sheet.getRow(0));
                    if (!headerIndex.isEmpty()) {
                        Map<String, AcademicSpaceDTO> existingAcademicSpaces = loadExistingAcademicSpaces();

                        for (int rowIndex = 1; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
                            Row row = sheet.getRow(rowIndex);
                            if (row == null || isRowEmpty(row)) {
                                continue;
                            }

                            String spaceName = getCellValue(row, headerIndex, "spacename", "academicspace", "ambiente", "espacio");
                            String observation = getCellValue(row, headerIndex, "observation", "observacion", "observaciones");
                            String location = getCellValue(row, headerIndex, "location", "ubicacion", "lugar");
                            Integer capacity = getCellIntValue(row, headerIndex, "capacity", "capacidad", "aforo");
                            String stateName = getCellValue(row, headerIndex, "state", "statename", "estado");
                            String typeName = getCellValue(row, headerIndex, "type", "typename", "tipo", "tipoacademico");
                            String buildingName = getCellValue(row, headerIndex, "building", "buildingname", "edificio");
                            Integer floorNumber = getCellIntValue(row, headerIndex, "floor", "floornumber", "piso");

                            if (!spaceName.isBlank()) {
                                AcademicSpaceDTO academicSpace = createOrUpdateAcademicSpace(
                                    existingAcademicSpaces, spaceName, observation, location, capacity,
                                    stateName, typeName, buildingName, floorNumber, states, types, buildings, floors);

                                if (academicSpace != null && academicSpace.getIdAcademicSpace() != null) {
                                    String spaceKey = normalizeKey(spaceName);
                                    if (!existingAcademicSpaces.containsKey(spaceKey)) {
                                        existingAcademicSpaces.put(spaceKey, academicSpace);
                                        academicSpacesCreated++;
                                    }
                                }
                            }
                        }

                        academicSpaces.putAll(existingAcademicSpaces);
                    }
                }
            }

            return PrimaryDataImportResult.builder()
                    .statesCreated(statesCreated)
                    .typesCreated(typesCreated)
                    .buildingsCreated(buildingsCreated)
                    .floorsCreated(floorsCreated)
                    .academicSpacesCreated(academicSpacesCreated)
                    .message("Importación completada correctamente")
                    .build();
        }
    }

    private Map<String, Integer> buildHeaderIndex(Row headerRow) {
        Map<String, Integer> headerIndex = new HashMap<>();
        if (headerRow == null) {
            return headerIndex;
        }
        for (int i = 0; i < headerRow.getLastCellNum(); i++) {
            String headerValue = getCellStringValue(headerRow.getCell(i));
            String normalized = normalizeHeader(headerValue);
            if (!normalized.isBlank()) {
                headerIndex.put(normalized, i);
            }
        }
        return headerIndex;
    }

    private Map<String, StateDTO> loadExistingStates() {
        Map<String, StateDTO> map = new HashMap<>();
        for (StateDTO state : environmentClient.getAllStates()) {
            if (state.getName() != null) {
                map.put(normalizeKey(state.getName()), state);
            }
        }
        return map;
    }

    private Map<String, TypeAcademicSpaceDTO> loadExistingTypes() {
        Map<String, TypeAcademicSpaceDTO> map = new HashMap<>();
        for (TypeAcademicSpaceDTO type : environmentClient.getAllTypeAcademicSpaces()) {
            if (type.getName() != null) {
                map.put(normalizeKey(type.getName()), type);
            }
        }
        return map;
    }

    private Map<String, BuildingDTO> loadExistingBuildings() {
        Map<String, BuildingDTO> map = new HashMap<>();
        for (BuildingDTO building : environmentClient.getAllBuildings()) {
            if (building.getName() != null) {
                map.put(normalizeKey(building.getName()), building);
            }
        }
        return map;
    }

    private Map<String, FloorDTO> loadExistingFloors(Map<String, BuildingDTO> buildings) {
        Map<String, FloorDTO> map = new HashMap<>();
        for (BuildingDTO building : buildings.values()) {
            if (building.getIdBuilding() != null) {
                List<FloorDTO> buildingFloors = environmentClient.getFloorsByBuilding(building.getIdBuilding());
                for (FloorDTO floor : buildingFloors) {
                    if (floor != null && floor.getBuilding() != null) {
                        map.put(buildingFloorKey(floor.getBuilding(), floor.getFloorNumber()), floor);
                    }
                }
            }
        }
        return map;
    }

    private Map<String, AcademicSpaceDTO> loadExistingAcademicSpaces() {
        Map<String, AcademicSpaceDTO> map = new HashMap<>();
        for (AcademicSpaceDTO space : environmentClient.getAllAcademicSpaces()) {
            if (space.getSpaceName() != null) {
                map.put(normalizeKey(space.getSpaceName()), space);
            }
        }
        return map;
    }

    private StateDTO createOrUpdateState(Map<String, StateDTO> states, String name, Character isActive) {
        String key = normalizeKey(name);
        StateDTO existing = states.get(key);
        if (existing == null) {
            StateCreateRequest stateRequest = new StateCreateRequest(name.trim(), isActive == null ? 'A' : isActive);
            return environmentClient.createState(stateRequest);
        }
        if (isActive != null && existing.getIsActive() != isActive) {
            StateCreateRequest stateRequest = new StateCreateRequest(existing.getName(), isActive);
            return environmentClient.createState(stateRequest);
        }
        return existing;
    }

    private TypeAcademicSpaceDTO createOrUpdateType(Map<String, TypeAcademicSpaceDTO> types, String name, Character isActive) {
        String key = normalizeKey(name);
        TypeAcademicSpaceDTO existing = types.get(key);
        if (existing == null) {
            TypeAcademicSpaceCreateRequest typeRequest = new TypeAcademicSpaceCreateRequest(name.trim(), isActive == null ? 'A' : isActive);
            return environmentClient.createTypeAcademicSpace(typeRequest);
        }
        if (isActive != null && existing.getIsActive() != isActive) {
            TypeAcademicSpaceCreateRequest typeRequest = new TypeAcademicSpaceCreateRequest(existing.getName(), isActive);
            return environmentClient.createTypeAcademicSpace(typeRequest);
        }
        return existing;
    }

    private BuildingDTO createOrUpdateBuilding(Map<String, BuildingDTO> buildings, String name, Character isActive) {
        String key = normalizeKey(name);
        BuildingDTO existing = buildings.get(key);
        if (existing == null) {
            BuildingCreateRequest buildingRequest = new BuildingCreateRequest(name.trim(), isActive == null ? 'A' : isActive);
            return environmentClient.createBuilding(buildingRequest);
        }
        if (isActive != null && existing.getIsActive() != isActive) {
            BuildingCreateRequest buildingRequest = new BuildingCreateRequest(existing.getName(), isActive);
            return environmentClient.createBuilding(buildingRequest);
        }
        return existing;
    }

    private FloorDTO createOrUpdateFloor(Map<String, FloorDTO> floors, int floorNumber, Character isActive, BuildingDTO building) {
        String key = buildingFloorKey(building, floorNumber);
        FloorDTO existing = floors.get(key);
        if (existing == null) {
            FloorCreateRequest floorRequest = new FloorCreateRequest(floorNumber, isActive == null ? 'A' : isActive, building.getIdBuilding());
            return environmentClient.createFloor(floorRequest);
        }
        if (isActive != null && existing.getIsActive() != isActive) {
            FloorCreateRequest floorRequest = new FloorCreateRequest(existing.getFloorNumber(), isActive, building.getIdBuilding());
            return environmentClient.createFloor(floorRequest);
        }
        return existing;
    }

    private AcademicSpaceDTO createOrUpdateAcademicSpace(Map<String, AcademicSpaceDTO> academicSpaces,
                                                        String spaceName, String observation, String location,
                                                        Integer capacity, String stateName, String typeName,
                                                        String buildingName, Integer floorNumber,
                                                        Map<String, StateDTO> states,
                                                        Map<String, TypeAcademicSpaceDTO> types,
                                                        Map<String, BuildingDTO> buildings,
                                                        Map<String, FloorDTO> floors) {
        String key = normalizeKey(spaceName);
        AcademicSpaceDTO existing = academicSpaces.get(key);

        // Resolver referencias
        StateDTO state = !stateName.isBlank() ? states.get(normalizeKey(stateName)) : null;
        TypeAcademicSpaceDTO type = !typeName.isBlank() ? types.get(normalizeKey(typeName)) : null;
        FloorDTO floor = null;

        if (!buildingName.isBlank() && floorNumber != null) {
            BuildingDTO building = buildings.get(normalizeKey(buildingName));
            if (building != null) {
                floor = floors.get(buildingFloorKey(building, floorNumber));
            }
        }

        if (existing == null) {
            AcademicSpaceCreateRequest spaceRequest = new AcademicSpaceCreateRequest(
                spaceName.trim(),
                observation != null ? observation.trim() : "",
                location != null ? location.trim() : "",
                capacity != null ? capacity : 30,
                state != null ? state.getIdState() : null,
                floor != null ? floor.getIdFloor() : null,
                type != null ? type.getIdTypeAcademicSpace() : null
            );
            return environmentClient.createAcademicSpace(spaceRequest);
        }

        // Para actualizaciones, por ahora solo devolvemos el existente
        // Si se necesita actualización, se debería implementar un endpoint PUT separado
        return existing;
    }

    private boolean floorExists(Map<String, FloorDTO> floors, BuildingDTO building, int floorNumber) {
        return floors.containsKey(buildingFloorKey(building, floorNumber));
    }

    private String buildingFloorKey(BuildingDTO building, int floorNumber) {
        return normalizeKey(building.getName()) + "|" + floorNumber;
    }

    private boolean isRowEmpty(Row row) {
        if (row == null) {
            return true;
        }
        for (int i = 0; i < row.getLastCellNum(); i++) {
            String value = getCellStringValue(row.getCell(i));
            if (!value.isBlank()) {
                return false;
            }
        }
        return true;
    }

    private String getCellValue(Row row, Map<String, Integer> headerIndex, String... keys) {
        for (String key : keys) {
            Integer idx = headerIndex.get(normalizeHeader(key));
            if (idx != null) {
                String rawValue = getCellStringValue(row.getCell(idx));
                if (rawValue != null) {
                    return rawValue.replace("\u0000", "").trim();
                }
                return "";
            }
        }
        return "";
    }

    private Character getCellCharValue(Row row, Map<String, Integer> headerIndex, String... keys) {
        String value = getCellValue(row, headerIndex, keys);
        if (value.isBlank()) {
            return null;
        }
        return Character.toUpperCase(value.trim().charAt(0));
    }

    private Integer getCellIntValue(Row row, Map<String, Integer> headerIndex, String... keys) {
        String value = getCellValue(row, headerIndex, keys);
        if (value.isBlank()) {
            return null;
        }
        try {
            return (int) Double.parseDouble(value.replace(',', '.'));
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private String getCellStringValue(org.apache.poi.ss.usermodel.Cell cell) {
        if (cell == null) {
            return "";
        }
        String rawValue = "";

        if (rawValue != null) {
            return rawValue.replace("\u0000", "").trim();
        }
        return "";
    }

    private String normalizeHeader(String header) {
        if (header == null) {
            return "";
        }
        return header.trim()
                .toLowerCase()
                .replaceAll("[ _\\-]", "")
                .replaceAll("\u00A0", "");
    }

    private String normalizeKey(String value) {
        if (value == null) {
            return "";
        }
        return value.trim().toLowerCase();
    }
}