package ru.nsu.timetable.models.mappers;

import org.springframework.stereotype.Component;
import ru.nsu.timetable.models.dto.OperationsDTO;
import ru.nsu.timetable.models.entities.Operations;

@Component
public class OperationsMapper {
    public OperationsDTO toOperationsDTO(Operations operations) {
        return new OperationsDTO(operations.getId(), operations.getDateOfCreation(),
                operations.getDescription(), operations.getUserAccount());
    }

    public Operations toOperations(OperationsDTO operationsDTO) {
        Operations operations = new Operations();
        operations.setId(operationsDTO.id());
        operations.setDateOfCreation(operationsDTO.dateOfCreation());
        operations.setDescription(operationsDTO.description());
        operations.setUserAccount(operationsDTO.userAccount());
        return operations;
    }
}
