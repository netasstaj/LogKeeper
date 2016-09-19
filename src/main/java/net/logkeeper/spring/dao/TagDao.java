package net.logkeeper.spring.dao;

import java.util.List;

import net.logkeeper.spring.model.FileGroup;
import net.logkeeper.spring.model.Tag;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class TagDao {

    @Autowired
    SessionFactory sessionFactory;

    private static final Logger LOGGER = Logger.getLogger(TagDao.class
	    .getName());

    public void save(Tag tag) {
	getCurrentSession().save(tag);
	if (LOGGER.isDebugEnabled()) {
	    LOGGER.debug("yeni bir tag oluþturuldu. " + tag.getName());
	}
    }

    public void saveOrUpdate(Tag tag) {
	getCurrentSession().saveOrUpdate(tag);
    }

    public void update(Tag tag) {
	getCurrentSession().update(tag);
    }

    public void deleteTagToFileGroup(Tag t, FileGroup u) {
	// mandatory:
	t.getFilegroup().remove(u);
	// optional:
	u.getTags().remove(t);
    }

    public void delete(Tag tag) {
	try {
	    getCurrentSession().delete(tag);
	    if (LOGGER.isDebugEnabled()) {
		LOGGER.debug("Tag silindi." + tag.getName());
	    }
	} catch (Exception ex) {
	    if (LOGGER.isEnabledFor(Level.ERROR)) {
		LOGGER.error("Tag Silinemedi TagDao. " + tag.getName() + " "
			+ ex.toString());
	    }
	}
    }

    // TODO:jiraa
    public List listTagFileGroup(String languagePrefix, Boolean jira) {
	Query query = getCurrentSession().createQuery(
		"from Tag c where c.name like :fileTag and jira= :jira");
	List tagList = query
		.setParameter("fileTag", "%" + languagePrefix + "%")
		.setMaxResults(10).setParameter("jira", jira).list();
	return tagList;
    }

    public List findFileTag(String fileTag) {
	Query query = getCurrentSession().createQuery(
		"from Tag c where c.name= :fileTag");
	List tagList = query.setParameter("fileTag", fileTag).list();
	return tagList;
    }

    public List findMergeTag(String merge) {
	Query query = getCurrentSession().createQuery(merge);
	List tagList = query.list();
	return tagList;
    }

    public List findTag(String tag) {
	Query query = getCurrentSession().createQuery(
		"from Tag c where c.name= :fileTag");
	List tagList = query.setParameter("fileTag", tag).list();
	return tagList;
    }

    public List listTag() {
	Query query = getCurrentSession().createQuery(
		"Select id,name from Tag order by clickCount desc");
	List tagList = query.setMaxResults(10).list();
	return tagList;
    }

    public Tag findByTag(String tag) {
	String hql = "from Tag where name= :fileTag";
	Query query = getCurrentSession().createQuery(hql);
	query.setParameter("fileTag", tag).setMaxResults(1);
	return (Tag) query.uniqueResult();
    }

    public Tag findByTag(int id) {
	String hql = "from Tag where id= :id";
	Query query = getCurrentSession().createQuery(hql);
	query.setParameter("id", id).setMaxResults(1);
	return (Tag) query.uniqueResult();
    }

    public List findByTagId(int fileGroupId) {
	Query query = getCurrentSession()
		.createQuery(
			"select t.id,t.name,t.createDate,t.jira from FileGroup fg join fg.tags t where fg.id= :id");
	List tagList = query.setParameter("id", fileGroupId).list();
	return tagList;
    }

    private Session getCurrentSession() {
	return sessionFactory.getCurrentSession();
    }

}
