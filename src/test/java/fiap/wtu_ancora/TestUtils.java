package fiap.wtu_ancora;

import fiap.wtu_ancora.dto.EventDTO;
import fiap.wtu_ancora.dto.UnitDTO;
import fiap.wtu_ancora.dto.UserDTO;
import fiap.wtu_ancora.model.Event;
import fiap.wtu_ancora.model.Unit;
import fiap.wtu_ancora.model.User;
import fiap.wtu_ancora.model.UserRole;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class TestUtils {

    public static Event createEventFake() {
        Event event = new Event();
        event.setTitle("Fake title");
        event.setDescription("Fake description");
        event.setStartDate(new Date());
        event.setEndDate(new Date());
        event.setIframe("fake iframe");
        event.setUnitId(1L);

        return event;
    }

    public static UnitDTO createUnitDTOFake() {
        UnitDTO unitDTO = new UnitDTO();
        unitDTO.setEndereco("Fake endereco");
        unitDTO.setFranchised(true);
        unitDTO.setName("Fake name");
        return unitDTO;
    }

    public static User createUserFake() {
        User user = new User();
        user.setName("Fake name");
        user.setEmail("fake@email.com");
        user.setRole(UserRole.USER);
        user.setPassword("password");
        return user;
    }


    public static EventDTO createEventDTOFake() {
        EventDTO eventDTO = new EventDTO();
        eventDTO.setTitle("Fake title");
        eventDTO.setDescription("Fake description");
        eventDTO.setStartDate(new Date());
        eventDTO.setEndDate(new Date());
        eventDTO.setIframe("fake iframe");

        Set<UnitDTO> unitDTOs = new HashSet<>();
        UnitDTO unitDTO = new UnitDTO();
        unitDTO.setId(1L);
        unitDTO.setName("Fake name");

        unitDTOs.add(unitDTO);

        Set<UserDTO> userDTOs = new HashSet<>();
        UserDTO userDTO = new UserDTO();
        userDTO.setEmail("fake@email.com");

        userDTOs.add(userDTO);

        return eventDTO;
    }

}
