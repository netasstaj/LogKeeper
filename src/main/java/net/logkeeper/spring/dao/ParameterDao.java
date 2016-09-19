package net.logkeeper.spring.dao;

import java.util.List;

import net.logkeeper.spring.model.Parameter;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class ParameterDao {
    @Autowired
    SessionFactory sessionFactory;
    private static final Logger LOGGER = Logger.getLogger(LogFileDao.class
	    .getName());

    public void save(Parameter param) {
	getCurrentSession().save(param);
	if (LOGGER.isDebugEnabled()) {
	    LOGGER.debug("yeni bir parametre oluþturuldu. ");
	}
    }

    public void saveOrUpdate(Parameter param) {
	getCurrentSession().saveOrUpdate(param);
    }

    public void update(Parameter param) {
	getCurrentSession().update(param);
    }

    public void delete(Parameter param) {
	try {
	    getCurrentSession().delete(param);
	    if (LOGGER.isDebugEnabled()) {
		LOGGER.debug("parametre silindi.");
	    }
	} catch (Exception ex) {
	    if (LOGGER.isEnabledFor(Level.ERROR)) {
		LOGGER.error("parametre silinemedi ParameterDao.  "
			+ ex.toString());
	    }
	}
    }

    public Parameter findByParameter(String key) {
	String hql = "from Parameter where key= :key";
	Query query = getCurrentSession().createQuery(hql);
	query.setParameter("key", key).setMaxResults(1);
	return (Parameter) query.uniqueResult();
    }

    @SuppressWarnings("unchecked")
    public List<Integer> findByParameter() {
	return getCurrentSession().createQuery("from Parameter").list();
    }

    private Session getCurrentSession() {
	return sessionFactory.getCurrentSession();
    }

}
