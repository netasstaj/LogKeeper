package net.logkeeper.spring.service;

import java.util.List;

import net.logkeeper.spring.dao.FileGroupDao;
import net.logkeeper.spring.model.FileGroup;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class FileGroupService {

    @Autowired
    FileGroupDao fileGroupDao;

    public FileGroupDao getFileGroupDao() {
	return fileGroupDao;
    }

    public void setFileGroupDao(FileGroupDao fileGroupDao) {
	this.fileGroupDao = fileGroupDao;
    }

    public List listTxtSearchFile(String languagePrefix) {
	return getFileGroupDao().listTxtSearchFile(languagePrefix);
    }

    public FileGroup findByFileGroupName(String name) {
	return getFileGroupDao().findByFileGroupName(name);
    }

    @Transactional(readOnly = false)
    public void save(FileGroup fileGroup) {
	getFileGroupDao().save(fileGroup);
    }

    @Transactional(readOnly = false)
    public void delete(FileGroup fileGroup) {
	getFileGroupDao().delete(fileGroup);
    }

    @Transactional(readOnly = false)
    public void update(FileGroup fileGroup) {
	getFileGroupDao().update(fileGroup);
    }

    public List listFileGroupUser() {
	return getFileGroupDao().listFileGroupUser();
    }

    public List listFileGroupMerge(String merge) {
	return getFileGroupDao().listFileGroupMerge(merge);
    }

    @Transactional(readOnly = false)
    public void delete(int id) {
	getFileGroupDao().delete(id);
    }

    public List listFileGroup(String filey) {
	return getFileGroupDao().listFileGroup(filey);
    }

    public FileGroup findByFileGroup(int id) {
	return getFileGroupDao().findByFileGroup(id);
    }

    public List<Integer> getFilesByTag(List<Integer> tags) {
	return getFileGroupDao().getFilesByTag(tags);
    }

    @Transactional(readOnly = false)
    public void saveOrUpdate(FileGroup fileGroup) {
	getFileGroupDao().saveOrUpdate(fileGroup);
    }

    public List listNameFileGroup() {
	return getFileGroupDao().listNameFileGroup();
    }

    public List lisTagFileGroup(String tag) {
	return getFileGroupDao().lisTagFileGroup(tag);
    }
}
