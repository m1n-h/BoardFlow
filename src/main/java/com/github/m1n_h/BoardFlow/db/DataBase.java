package com.github.m1n_h.BoardFlow.db;

import com.github.m1n_h.BoardFlow.model.User;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DataBase {

    private static final Map<String, User> users = new ConcurrentHashMap<>();

    public static void addUser(User user) { users.put(user.getUserId(), user); }
    public static User findUserById(String userId) {
        if (userId == null) return null;
        return users.get(userId);
    }
    public static User getUserById(String userId) { return users.get(userId); }
    public static Collection<User> findAll() { return users.values(); }

}
