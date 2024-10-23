package fiap.wtu_ancora.service;


import fiap.wtu_ancora.domain.User;
import fiap.wtu_ancora.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public List<User> findAllUsers(){
        return userRepository.findAllUsersWithUnits();
    }

    public Optional<User> findUser(Long id){
        return userRepository.findById(id);
    }

    public UserDetails findByEmail(String email){return userRepository.findByEmail(email);}

    public User saveUser(User user){
        return userRepository.save(user);
    }

    public Set<User> findByEmails(Set<String> usersEmails){
        Set<User> users = new HashSet<>();
        for(String email : usersEmails) {
            User user = userRepository.findUserByEmail(email);
            if (user != null) {
                users.add(user);
            }
        }
        return users;
    }
}
