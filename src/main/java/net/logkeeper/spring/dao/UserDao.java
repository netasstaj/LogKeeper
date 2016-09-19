package net.logkeeper.spring.dao;

import java.util.List;

import net.logkeeper.spring.model.FileGroup;
import net.logkeeper.spring.model.User;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class UserDao {
    @Autowired
    SessionFactory sessionFactory;
    private static final Logger LOGGER = Logger.getLogger(UserDao.class
	    .getName());

    public void save(User user) {
	getCurrentSession().save(user);
	if (LOGGER.isDebugEnabled()) {
	    LOGGER.debug("Yeni bir kullanýcý oluþturuldu." + user.getName());
	}
    }

    public void update(User user) {
	getCurrentSession().update(user);
    }

    public void delete(User user) {
	try {
	    getCurrentSession().delete(user);
	    if (LOGGER.isDebugEnabled()) {
		LOGGER.debug("Kullanýcý silindi." + user.getName());
	    }
	} catch (Exception ex) {
	    if (LOGGER.isEnabledFor(Level.ERROR)) {
		LOGGER.error("Kullanýcý Silinemedi UserDao. " + user.getName()
			+ " " + ex.toString());
	    }
	}
    }

    public FileGroup fileGroupList() {
	Query q = getCurrentSession().createQuery("from FileGroup");
	return (FileGroup) q.uniqueResult();
    }

    public User idUser(String name) {
	String hql = "from User where name= :name";
	Query query = getCurrentSession().createQuery(hql);
	query.setParameter("name", name).setMaxResults(1);
	return (User) query.uniqueResult();
    }

    public User emailUser(String email) {
	String hql = "from User where email= :email";
	Query query = getCurrentSession().createQuery(hql);
	query.setParameter("email", email).setMaxResults(1);
	return (User) query.uniqueResult();
    }

    public boolean validate(String name, String password) {
	Query q = getCurrentSession()
		.createQuery(
			"Select name, password from User where name = ? and password = ?");
	List result = q.setParameter(0, name).setParameter(1, password).list();
	if (result.isEmpty()) {
	    // result found, means valid inputs
	    return false;
	} else {
	    return true;
	}
    }

    private Session getCurrentSession() {
	return sessionFactory.getCurrentSession();
    }
}
