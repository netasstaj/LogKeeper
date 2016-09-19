package net.logkeeper.spring.dao;

import java.util.List;

import net.logkeeper.spring.model.LogFile;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class LogFileDao {
    @Autowired
    SessionFactory sessionFactory;
    private static final Logger LOGGER = Logger.getLogger(LogFileDao.class
	    .getName());

    public void save(LogFile file) {
	getCurrentSession().save(file);
	if (LOGGER.isDebugEnabled()) {
	    LOGGER.debug("yeni bir log file oluþturuldu. " + file.getPath());
	}
    }

    public void saveOrUpdate(LogFile file) {
	getCurrentSession().saveOrUpdate(file);
    }

    public void update(LogFile file) {
	getCurrentSession().update(file);
    }

    public void delete(LogFile file) {
	try {
	    getCurrentSession().delete(file);
	    if (LOGGER.isDebugEnabled()) {
		LOGGER.debug("Log dosyasý silindi." + file.getPath());
	    }
	} catch (Exception ex) {
	    if (LOGGER.isEnabledFor(Level.ERROR)) {
		LOGGER.error("Log dosyasý silinemedi LogFileDao. "
			+ file.getPath() + " " + ex.toString());
	    }
	}
    }

    public void delete(int id) {
	String hql = "DELETE FROM LogFile " + "WHERE id = :id";
	Query query = getCurrentSession().createQuery(hql);
	query.setParameter("id", id);
	int result = query.executeUpdate();
	System.out.println("Effect row: " + result);
    }

    public LogFile findByLogFile(int id) {
	String hql = "from LogFile where id= :id";
	Query query = getCurrentSession().createQuery(hql);
	query.setParameter("id", id).setMaxResults(1);
	return (LogFile) query.uniqueResult();
    }

    public List<Integer> listFile(int id) {
	String hql = "from LogFile f where f.fileGroupId= :id";
	Query query = getCurrentSession().createQuery(hql);
	query.setParameter("id", id);
	@SuppressWarnings("unchecked")
	List<Integer> results = query.list();
	return results;
    }

    private Session getCurrentSession() {
	return sessionFactory.getCurrentSession();
    }

    public void deleteAll(int fileGroupId) {
	String hql = "Delete from LogFile " + "where fileGroupId= :fileGroupId";
	Query query = getCurrentSession().createQuery(hql);
	query.setParameter("fileGroupId", fileGroupId);
	query.executeUpdate();
    }

}
