package fiap.wtu_ancora.controller;

import fiap.wtu_ancora.domain.Unit;
import fiap.wtu_ancora.dto.UnitDTO;
import fiap.wtu_ancora.model.ApiReponse;
import fiap.wtu_ancora.service.UnitService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/units")
public class UnitController {

    UnitService unitService;

    public UnitController(UnitService unitService) {
        this.unitService = unitService;
    }

    @GetMapping("/get-all")
    public ResponseEntity<ApiReponse<List<Unit>>> getAllUnits() {

        return unitService.getAllUnits();
    }

    @PostMapping("/create")
    public ResponseEntity<ApiReponse<String>> createUnit(@RequestBody UnitDTO unitDTO) {

        return unitService.createUnit(unitDTO);
    }

    @PutMapping("/edit/{id}")
    public ResponseEntity<ApiReponse<String>> editUnit(@PathVariable Long id, @RequestBody UnitDTO unitDTO) {
        return unitService.updateUnit(id, unitDTO);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiReponse<String>> deleteUnit(@PathVariable Long id) {

        return unitService.deleteUnit(id);
    }
}
