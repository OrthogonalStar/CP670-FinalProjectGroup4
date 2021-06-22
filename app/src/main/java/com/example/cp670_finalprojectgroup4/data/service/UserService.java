package com.example.cp670_finalprojectgroup4.data.service;

import com.example.cp670_finalprojectgroup4.data.dao.UserDAO;
import com.example.cp670_finalprojectgroup4.data.model.UserModel;

public class UserService {

    UserDAO userDAO;

    public UserService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public static UserModel getUserByEmail(String email) {
        return UserDAO.getUserByEmail(email);
    }

    public static Boolean userExists(String email) {
        return UserDAO.userExists(email);
    }

    public static UserModel addUser(UserModel userModel) {
        userModel.setSalt(EncryptionService.getNextSalt());
        userModel.setPassword(EncryptionService.hash(userModel.getPassword(), userModel.getSalt()));
        UserDAO.addUser(userModel);
        return userModel;
    }

    public static Boolean verifyUserByPassword(String email, String password){
        UserModel user = getUserByEmail(email);
        return user != null && user.getPassword().equals(EncryptionService.hash(password, user.getSalt()));
    }

    public UserDAO getUserDAO() {
        return userDAO;
    }

    public void setUserDAO(UserDAO userDAO) {
        this.userDAO = userDAO;
    }
}
