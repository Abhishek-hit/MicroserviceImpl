package UserService.services;

import UserService.entities.User;

import java.util.List;

public interface UserService {
//user operations
    User saveUser(User user);
    //get all user

    List<User> getAllUser();

    //get single user

    User getUser(String userId);

}
