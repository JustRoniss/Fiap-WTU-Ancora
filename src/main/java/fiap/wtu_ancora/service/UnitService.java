package fiap.wtu_ancora.service;

import fiap.wtu_ancora.dto.UnitDTO;
import fiap.wtu_ancora.model.Unit;
import fiap.wtu_ancora.repository.UnitRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class UnitService {

    UnitRepository unitRepository;

    public UnitService(UnitRepository unitRepository) {
        this.unitRepository = unitRepository;
    }

    public ResponseEntity<?> getAllUnits() {
        return ResponseEntity.ok(unitRepository.findAll());
    }

    public Set<Unit> findUnitsByIds(Set<Long> unitsId) {
        Set<Unit> unitSet = new HashSet<>();
        for(Long id : unitsId) {
            Unit unit = unitRepository.findById(id).orElse(null);
            unitSet.add(unit);
        }
        return unitSet;
    }

   public ResponseEntity<?> createUnit(UnitDTO unitDTO) {
        try{
            Unit unit = new Unit();
            mapUnitDTOToUnit(unitDTO, unit);
            unitRepository.save(unit);
            return ResponseEntity.ok("Successfully created a new unit");
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<?> updateUnit(Long id, UnitDTO unitDTO) {
        Optional<Unit> unitOptional = unitRepository.findById(id);
        if (unitOptional.isPresent()) {
            Unit unit = unitOptional.get();
            mapUnitDTOToUnit(unitDTO, unit);
            unitRepository.save(unit);
            return ResponseEntity.ok("Successfully updated a unit");
        } else{
            return new ResponseEntity<>("Unit not found",HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<?> deleteUnit(Long id) {
        try{
            unitRepository.deleteById(id);
            return ResponseEntity.ok("Successfully deleted a unit");
        }catch (Exception e){
            return new ResponseEntity<>("Error to delete this unit", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public Unit mapUnitDTOToUnit(UnitDTO unitDTO, Unit unit) {
        unit.setName(unitDTO.getName());
        unit.setEndereco(unitDTO.getEndereco());
        unit.setFranchised(unitDTO.isFranchised());

        return unit;
    }
}
