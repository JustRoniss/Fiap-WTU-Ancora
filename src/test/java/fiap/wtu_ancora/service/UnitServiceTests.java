package fiap.wtu_ancora.service;

import fiap.wtu_ancora.TestUtils;
import fiap.wtu_ancora.dto.UnitDTO;
import fiap.wtu_ancora.domain.Unit;
import fiap.wtu_ancora.model.ApiReponse;
import fiap.wtu_ancora.repository.UnitRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class UnitServiceTests {

    @Mock
    private UnitRepository unitRepository;

    @InjectMocks
    private UnitService unitService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateUnitWithSuccess(){
        UnitDTO unitDTO = TestUtils.createUnitDTOFake();
        Unit unit = new Unit();

        Mockito.when(unitRepository.save(Mockito.any(Unit.class))).thenReturn(unit);

        ResponseEntity<ApiReponse<String>> response = unitService.createUnit(unitDTO);
        ApiReponse<String> apiReponse = response.getBody();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assert apiReponse != null;
        assertEquals("Unidade criada com sucesso", apiReponse.getMessage());
        Mockito.verify(unitRepository, Mockito.times(1)).save(Mockito.any(Unit.class));
    }

    @Test
    public void testCreateUnitWithError(){
        UnitDTO unitDTO = TestUtils.createUnitDTOFake();
        Mockito.when(unitRepository.save(Mockito.any(Unit.class))).thenThrow(new RuntimeException("Error"));

        ResponseEntity<ApiReponse<String>> response = unitService.createUnit(unitDTO);
        ApiReponse<String> apiReponse = response.getBody();


        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assert apiReponse != null;
        assertEquals("Erro ao criar unidade", apiReponse.getMessage());
        Mockito.verify(unitRepository, Mockito.times(1)).save(Mockito.any(Unit.class));
    }

    @Test
    public void testUpdateUnitWithSuccess(){
        Long unitId = 1L;
        UnitDTO unitDTO = TestUtils.createUnitDTOFake();

        Unit unit = new Unit();
        unit.setId(unitId);

        Mockito.when(unitRepository.findById(unitId)).thenReturn(Optional.of(unit));
        Mockito.when(unitRepository.save(Mockito.any(Unit.class))).thenReturn(unit);

        ResponseEntity<ApiReponse<String>> response = unitService.updateUnit(unitId, unitDTO);
        ApiReponse<String> apiReponse = response.getBody();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assert apiReponse != null;
        assertEquals("Unidade atualizada com sucesso", apiReponse.getMessage());
        Mockito.verify(unitRepository, Mockito.times(1)).save(Mockito.any(Unit.class));
    }

    @Test
    public void testUpdateUnitWithError(){
        long unitId = 1L;
        UnitDTO unitDTO = TestUtils.createUnitDTOFake();

        Mockito.when(unitRepository.findById(unitId)).thenReturn(Optional.empty());

        ResponseEntity<ApiReponse<String>> response = unitService.updateUnit(unitId, unitDTO);
        ApiReponse<String> apiReponse = response.getBody();

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assert apiReponse != null;
        assertEquals("Unidade não encontrada", apiReponse.getMessage());
        Mockito.verify(unitRepository, Mockito.never()).save(Mockito.any(Unit.class));
    }

    @Test
    public void testDeleteUnitWithSuccess(){
        Long unitId = 1L;

        Mockito.doNothing().when(unitRepository).deleteById(unitId);

        ResponseEntity<ApiReponse<String>> response = unitService.deleteUnit(unitId);
        ApiReponse<String> apiReponse = response.getBody();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assert apiReponse != null;
        assertEquals("Unidade excluida de Id: " + unitId + " excluida com sucesso", apiReponse.getMessage());
        Mockito.verify(unitRepository, Mockito.times(1)).deleteById(unitId);
    }

    @Test
    public void testDeleteUnitWithError(){
        long unitId = 1L;

        Mockito.doThrow(new RuntimeException("Delete error")).when(unitRepository).deleteById(unitId);

        ResponseEntity<ApiReponse<String>> response = unitService.deleteUnit(unitId);
        ApiReponse<String> apiReponse = response.getBody();


        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assert apiReponse != null;
        assertEquals("Erro ao excluir unidade de Id: " + unitId, apiReponse.getMessage());
        Mockito.verify(unitRepository, Mockito.times(1)).deleteById(unitId);
    }

}
