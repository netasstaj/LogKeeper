package net.logkeeper.spring.service;

import java.util.List;

import net.logkeeper.spring.dao.LogFileDao;
import net.logkeeper.spring.model.LogFile;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class LogFileService {
    @Autowired
    LogFileDao fileDao;

    public LogFileDao getFileDao() {
	return fileDao;
    }

    public void setFileDao(LogFileDao fileDao) {
	this.fileDao = fileDao;
    }

    @Transactional(readOnly = false)
    public void save(LogFile file) {
	getFileDao().save(file);
    }

    @Transactional(readOnly = false)
    public void delete(LogFile file) {
	getFileDao().delete(file);
    }

    @Transactional(readOnly = false)
    public void update(LogFile file) {
	getFileDao().update(file);
    }

    @Transactional(readOnly = false)
    public void delete(int id) {
	getFileDao().delete(id);
    }

    public LogFile findByLogFile(int id) {
	return getFileDao().findByLogFile(id);
    }

    public List listFile(int id) {
	return getFileDao().listFile(id);
    }

    public void deleteAll(int fileGroupId) {
	getFileDao().deleteAll(fileGroupId);
    }
}
