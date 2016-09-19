package net.logkeeper.spring.service;

import net.logkeeper.spring.dao.UserDao;
import net.logkeeper.spring.model.FileGroup;
import net.logkeeper.spring.model.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class UserService {
    @Autowired
    UserDao UserDao;

    public UserDao getUserDao() {
	return UserDao;
    }

    public void setUserDao(UserDao userDao) {
	UserDao = userDao;
    }

    @Transactional(readOnly = false)
    public void save(User user) {
	getUserDao().save(user);
    }

    @Transactional(readOnly = false)
    public void delete(User user) {
	getUserDao().delete(user);
    }

    @Transactional(readOnly = false)
    public void update(User user) {
	getUserDao().update(user);
    }

    public FileGroup fileGroupList() {
	return getUserDao().fileGroupList();
    }

    public User idUser(String name) {
	return getUserDao().idUser(name);
    }

    public boolean validate(String name, String password) {
	return getUserDao().validate(name, password);
    }

    public User emailUser(String email) {
	return getUserDao().emailUser(email);
    }
}
