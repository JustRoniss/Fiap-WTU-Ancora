package fiap.wtu_ancora.domain;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "unidades")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Unit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String endereco;
    private boolean isFranchised;

    @ManyToMany(mappedBy = "units")
    private Set<Event> events;

    @OneToMany(mappedBy = "unit", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<User> users = new HashSet<>();

    public Unit(Long id, String name, String endereco, boolean isFranchised) {
        this.id = id;
        this.name = name;
        this.endereco = endereco;
        this.isFranchised = isFranchised;
    }

    public Unit() {

    }

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

}
