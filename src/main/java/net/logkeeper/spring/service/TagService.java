package net.logkeeper.spring.service;

import java.util.List;

import net.logkeeper.spring.dao.TagDao;
import net.logkeeper.spring.model.FileGroup;
import net.logkeeper.spring.model.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class TagService {
    @Autowired
    TagDao tagDao;

    public TagDao getTagDao() {
	return tagDao;
    }

    public void setTagDao(TagDao tagDao) {
	this.tagDao = tagDao;
    }

    @Transactional(readOnly = false)
    public void save(Tag tag) {
	getTagDao().save(tag);
    }

    @Transactional(readOnly = false)
    public void update(Tag tag) {
	getTagDao().update(tag);
    }

    @Transactional(readOnly = false)
    public Tag findByTag(String tag) {
	return getTagDao().findByTag(tag);
    }

    @Transactional(readOnly = false)
    public void delete(Tag tag) {
	getTagDao().delete(tag);
    }

    public List listTagFileGroup(String languagePrefix, Boolean jira) {
	return getTagDao().listTagFileGroup(languagePrefix, jira);
    }

    public List findTag(String tag) {
	return getTagDao().findTag(tag);
    }

    public List findMergeTag(String merge) {
	return getTagDao().findMergeTag(merge);
    }

    public List findFileTag(String fileTag) {
	return getTagDao().findFileTag(fileTag);
    }

    public List findByTagId(int fileGroupId) {
	return getTagDao().findByTagId(fileGroupId);
    }

    public void deleteTagToFileGroup(Tag t, FileGroup u) {
	getTagDao().deleteTagToFileGroup(t, u);
    }

    public void saveOrUpdate(Tag tag) {
	getTagDao().saveOrUpdate(tag);
    }

    public List listTag() {
	return getTagDao().listTag();
    }

    public Tag findByTag(int id) {
	return getTagDao().findByTag(id);
    }
}
