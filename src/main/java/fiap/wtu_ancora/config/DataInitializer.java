package fiap.wtu_ancora.config;


import fiap.wtu_ancora.domain.Unit;
import fiap.wtu_ancora.domain.User;
import fiap.wtu_ancora.domain.UserRole;
import fiap.wtu_ancora.repository.UnitRepository;
import fiap.wtu_ancora.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer {

    private final UserRepository userRepository;
    private final UnitRepository unitRepository;

    public DataInitializer(UserRepository userRepository, UnitRepository unitRepository) {
        this.userRepository = userRepository;
        this.unitRepository = unitRepository;
    }

    @Bean
    public CommandLineRunner init() {
        return args -> {
            if (unitRepository.count() == 0) {
                Unit defaultUnit = new Unit();
                defaultUnit.setName("Rede Ancora - Sede");
                defaultUnit.setEndereco("Rua Dr. Mello Nogueira, 105 - Casa Verde");
                defaultUnit.setFranchised(false);
                unitRepository.save(defaultUnit);
            }

            if (userRepository.count() == 0) {
                String encryptedPassword = new BCryptPasswordEncoder().encode("admin");
                User adminUser = new User("root", "root@ancora.com", encryptedPassword, UserRole.ADMIN);

                Unit defaultUnit = unitRepository.findAll().get(0);
                adminUser.setUnit(defaultUnit);

                userRepository.save(adminUser);
            }
        };
    }
}
