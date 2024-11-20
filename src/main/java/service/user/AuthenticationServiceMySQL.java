package service.user;
import model.Role;
import model.User;
import model.builder.UserBuilder;

import repository.security.RightsRolesRepository;
import repository.user.UserRepository;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Collections;

import static database.Constants.Roles.CUSTOMER;

// TEMA- redenumire AuthenticationServiceMySQL cu AuthenticationServiceImpl
public class AuthenticationServiceMySQL implements AuthenticationService {

    // putem avea mai multe repository-uri diferite care sa le folosim intr-un service
    private final UserRepository userRepository;
    private final RightsRolesRepository rightsRolesRepository;

    public AuthenticationServiceMySQL(UserRepository userRepository, RightsRolesRepository rightsRolesRepository) {
        // injectam cele 2 dependinte
        this.userRepository = userRepository;
        this.rightsRolesRepository = rightsRolesRepository;
    }

    @Override
    public boolean register(String username, String password) {
        String encodePassword = hashPassword(password);

        // Criptare: mesaj -> bviodifibifdupb -> mesaj
        // parolasinmpla123! -> hbgcidsvodsbvl8734ohwdo (se transforma intr-un hash)
        // de la un hash nu ma pot intoarce inapoi la textul initial
        Role customerRole = rightsRolesRepository.findRoleByTitle(CUSTOMER);

        User user = new UserBuilder()
                .setUsername(username)
                .setPassword(encodePassword)
                .setRoles(Collections.singletonList(customerRole))
                .build();

        return userRepository.save(user);
    }

    @Override
    public User login(String username, String password) {
        return userRepository.findByUsernameAndPassword(username, hashPassword(password));
    }

    @Override
    public boolean logout(User user) {
        return false;
    }

    private String hashPassword(String password) {
        try {
            // Sercured Hash Algorithm - 256 (se refera la nr biti)
            // 1 byte = 8 biți
            // 1 byte = 1 char (in cazul nostru) => parola pe 64 caract
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8)); // hash-ul se creaza prin transformarea parolei in biti, UTF_8 tipul de caractere folosit
            StringBuilder hexString = new StringBuilder();

            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }

            return hexString.toString();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}