package fiap.wtu_ancora.service;

import fiap.wtu_ancora.TestUtils;
import fiap.wtu_ancora.dto.UnitDTO;
import fiap.wtu_ancora.domain.Unit;
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

        ResponseEntity<?> response = unitService.createUnit(unitDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Successfully created a new unit", response.getBody());
        Mockito.verify(unitRepository, Mockito.times(1)).save(Mockito.any(Unit.class));
    }

    @Test
    public void testCreateUnitWithError(){
        UnitDTO unitDTO = TestUtils.createUnitDTOFake();
        Mockito.when(unitRepository.save(Mockito.any(Unit.class))).thenThrow(new RuntimeException("Error"));

        ResponseEntity<?> response = unitService.createUnit(unitDTO);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Error", response.getBody());
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

        ResponseEntity<?> response = unitService.updateUnit(unitId, unitDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Successfully updated a unit", response.getBody());
        Mockito.verify(unitRepository, Mockito.times(1)).save(Mockito.any(Unit.class));
    }

    @Test
    public void testUpdateUnitWithError(){
        long unitId = 1L;
        UnitDTO unitDTO = TestUtils.createUnitDTOFake();

        Mockito.when(unitRepository.findById(unitId)).thenReturn(Optional.empty());

        ResponseEntity<?> response = unitService.updateUnit(unitId, unitDTO);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Unit not found", response.getBody());
        Mockito.verify(unitRepository, Mockito.never()).save(Mockito.any(Unit.class));
    }

    @Test
    public void testDeleteUnitWithSuccess(){
        Long unitId = 1L;

        Mockito.doNothing().when(unitRepository).deleteById(unitId);

        ResponseEntity<?> response = unitService.deleteUnit(unitId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Successfully deleted a unit", response.getBody());
        Mockito.verify(unitRepository, Mockito.times(1)).deleteById(unitId);
    }

    @Test
    public void testDeleteUnitWithError(){
        long unitId = 1L;

        Mockito.doThrow(new RuntimeException("Delete error")).when(unitRepository).deleteById(unitId);

        ResponseEntity<?> response = unitService.deleteUnit(unitId);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Error to delete this unit", response.getBody());
        Mockito.verify(unitRepository, Mockito.times(1)).deleteById(unitId);
    }



}
