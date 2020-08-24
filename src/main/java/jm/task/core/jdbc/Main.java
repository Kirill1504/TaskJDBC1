package jm.task.core.jdbc;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.service.UserService;
import jm.task.core.jdbc.service.UserServiceImpl;
import jm.task.core.jdbc.util.Util;

import java.util.List;


public class Main {
    public static void main(String[] args) {
        UserService userService = new UserServiceImpl();

        userService.createUsersTable();
        userService.saveUser("user1", "userovich", (byte) 3);
        userService.saveUser("user2", "userovich", (byte) 98);
        userService.saveUser("user3", "userovich", (byte) 31);
        userService.saveUser("user4", "userovich", (byte) 46);
        System.out.println(userService.getAllUsers());
        userService.cleanUsersTable();
        userService.dropUsersTable();
        Util.shutdown();
    }
}
