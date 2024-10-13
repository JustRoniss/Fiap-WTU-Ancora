package fiap.wtu_ancora.service;

import fiap.wtu_ancora.dto.UnitDTO;
import fiap.wtu_ancora.model.Unit;
import fiap.wtu_ancora.repository.UnitRepository;

import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class UnitService {

    UnitRepository unitRepository;

    public UnitService(UnitRepository unitRepository) {
        this.unitRepository = unitRepository;
    }

    public Set<Unit> findUnitsByIds(Set<Long> unitsId) {
        Set<Unit> unitSet = new HashSet<>();
        for(Long id : unitsId) {
            Unit unit = unitRepository.findById(id).orElse(null);
            unitSet.add(unit);
        }
        return unitSet;
    }
}
