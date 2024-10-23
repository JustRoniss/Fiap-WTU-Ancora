package fiap.wtu_ancora.dto;

import fiap.wtu_ancora.domain.Event;
import fiap.wtu_ancora.domain.User;

import java.util.Set;

public class UnitDTO {

    private Long id;

    private String name;

    private String endereco;

    private boolean isFranchised;

    private Set<Event> events;

    private Set<User> users;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public boolean isFranchised() {
        return isFranchised;
    }

    public void setFranchised(boolean franchised) {
        isFranchised = franchised;
    }

    public Set<Event> getEvents() {
        return events;
    }

    public void setEvents(Set<Event> events) {
        this.events = events;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }
}