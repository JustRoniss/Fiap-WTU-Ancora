package fiap.wtu_ancora.service;

import fiap.wtu_ancora.dto.UnitDTO;
import fiap.wtu_ancora.domain.Unit;
import fiap.wtu_ancora.model.ApiReponse;
import fiap.wtu_ancora.repository.UnitRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UnitService {

    private final UnitRepository unitRepository;

    public UnitService(UnitRepository unitRepository) {
        this.unitRepository = unitRepository;
    }

    public Optional<Unit> findUnitById(Long id) {return unitRepository.findById(id);}

    public ResponseEntity<ApiReponse<List<Unit>>> getAllUnits() {

        try{
            List<Unit> units = unitRepository.findAll();
            ApiReponse<List<Unit>> response = new ApiReponse<>(
                    "Unidades encontradas",
                    HttpStatus.OK.value(),
                    null,
                    LocalDateTime.now(),
                    units
            );
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            ApiReponse<List<Unit>> errorResponse = new ApiReponse<>(
                    "Erro ao buscar todas as unidades cadastradas",
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    null,
                    LocalDateTime.now(),
                    null
            );
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    // Não é utilizado pela controller, é um metodo auxiliar utilizado no EventService(nao precisa ajustar o retorno para ApiResponse)
    public Set<Unit> findUnitsByIds(Set<Long> unitsId) {
        Set<Unit> unitSet = new HashSet<>();
        for(Long id : unitsId) {
            Unit unit = unitRepository.findById(id).orElse(null);
            unitSet.add(unit);
        }
        return unitSet;
    }

   public ResponseEntity<ApiReponse<String>> createUnit(UnitDTO unitDTO) {
        try{
            Unit unit = new Unit();
            mapUnitDTOToUnit(unitDTO, unit);
            unitRepository.save(unit);

            ApiReponse<String> response = new ApiReponse<>(
                    "Unidade criada com sucesso",
                    HttpStatus.OK.value(),
                    null,
                    LocalDateTime.now(),
                    null
            );

            return ResponseEntity.ok(response);
        }catch (Exception e){
            ApiReponse<String> errorResponse = new ApiReponse<>(
                    "Erro ao criar unidade",
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    null,
                    LocalDateTime.now(),
                    e.toString()
            );
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<ApiReponse<String>> updateUnit(Long id, UnitDTO unitDTO) {
        Optional<Unit> unitOptional = unitRepository.findById(id);
        if (unitOptional.isPresent()) {
            Unit unit = unitOptional.get();
            mapUnitDTOToUnit(unitDTO, unit);
            unitRepository.save(unit);

            ApiReponse<String> response = new ApiReponse<>(
                    "Unidade atualizada com sucesso",
                    HttpStatus.OK.value(),
                    null,
                    LocalDateTime.now(),
                    null
            );
            return ResponseEntity.ok(response);
        } else{
            ApiReponse<String> errorResponse = new ApiReponse<>(
                    "Unidade não encontrada",
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    null,
                    LocalDateTime.now(),
                    null
            );
            return new ResponseEntity<>(errorResponse,HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<ApiReponse<String>> deleteUnit(Long id) {
        try{
            unitRepository.deleteById(id);
            ApiReponse<String> response = new ApiReponse<>(
                    "Unidade excluida de Id: " + id + " excluida com sucesso",
                    HttpStatus.OK.value(),
                    null,
                    LocalDateTime.now(),
                    null
            );

            return ResponseEntity.ok(response);
        }catch (Exception e){
            ApiReponse<String> errorResponse = new ApiReponse<>(
                    "Erro ao excluir unidade de Id: " + id,
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    null,
                    LocalDateTime.now(),
                    e.toString()
            );
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public Unit mapUnitDTOToUnit(UnitDTO unitDTO, Unit unit) {
        unit.setName(unitDTO.getName());
        unit.setEndereco(unitDTO.getEndereco());
        unit.setFranchised(unitDTO.isFranchised());

        return unit;
    }
}
