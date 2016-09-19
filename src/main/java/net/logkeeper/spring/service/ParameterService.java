package net.logkeeper.spring.service;

import java.util.List;

import net.logkeeper.spring.dao.ParameterDao;
import net.logkeeper.spring.model.Parameter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ParameterService {
    @Autowired
    ParameterDao parameterDao;

    public ParameterDao getParameterDao() {
	return parameterDao;
    }

    public void setParameterDao(ParameterDao parameterDao) {
	this.parameterDao = parameterDao;
    }

    @Transactional(readOnly = false)
    public void save(Parameter param) {
	getParameterDao().save(param);
    }

    @Transactional(readOnly = false)
    public void saveOrUpdate(Parameter param) {
	getParameterDao().saveOrUpdate(param);
    }

    @Transactional(readOnly = false)
    public void delete(Parameter param) {
	getParameterDao().delete(param);
    }

    @Transactional(readOnly = false)
    public void update(Parameter param) {
	getParameterDao().update(param);
    }

    public Parameter findByParameter(String key) {
	return getParameterDao().findByParameter(key);
    }

    public List findByParameter() {
	return getParameterDao().findByParameter();
    }
}
