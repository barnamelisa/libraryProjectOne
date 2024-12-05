package mapper;

import model.Book;
import model.User;
import model.builder.BookBuilder;
import model.builder.UserBuilder;
import view.model.BookDTO;
import view.model.UserDTO;
import view.model.builder.BookDTOBuilder;
import view.model.builder.UserDTOBuilder;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class UserMapper {
    // facem doar metode statice ca sa nu trebuiasca sa cream obiecte de tipul UserMapper
    public static UserDTO convertUserToUserDTO(User user){
        return new UserDTOBuilder().setUsername(user.getUsername()).setPassword(user.getPassword()).setRole("employee").build();
    }

    public static User convertUserDTOToUser(UserDTO userDTO){
        return new UserBuilder().setUsername(userDTO.getUsername()).setPassword(userDTO.getPassword()).build();
    }

    public static List<UserDTO> convertUserListToUserDTOList(List<User> users){
        // mapam fiecare elem la UserMapper de convertUserToUserDTO si colectam rezultatele intr-o lista
        return users.parallelStream().map(UserMapper::convertUserToUserDTO).collect(Collectors.toList());
    }
    public static List<User> convertUserDTOListToUserList(List<UserDTO> userDTOS){
        return userDTOS.parallelStream().map(UserMapper::convertUserDTOToUser).collect(Collectors.toList());
    }
}
