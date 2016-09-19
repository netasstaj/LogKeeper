package net.logkeeper.managedController;

import java.io.Serializable;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;

import net.logkeeper.spring.model.Parameter;
import net.logkeeper.spring.service.FileGroupService;
import net.logkeeper.spring.service.ParameterService;

@ManagedBean(name = "parameters")
@SessionScoped
public class Parameters implements Serializable {
    public enum Paramet {
	JIRA_URL, JIRA_PREF, JIRA_LOGIN, SERVER_URL, REST_URL, JIRA_TAG_URL
    }

    Paramet cName;
    List<Parameter> parameterList;

    public Parameters(Paramet cName) {
	this.cName = cName;
    }

    @ManagedProperty(value = "#{ParameterService}")
    public ParameterService parameterService;

    public ParameterService getParameterService() {
	return parameterService;
    }

    public void setParameterService(ParameterService parameterService) {
	this.parameterService = parameterService;
    }

    @ManagedProperty(value = "#{FileGroupService}")
    public FileGroupService fileGroupService;

    public FileGroupService getFileGroupService() {
	return fileGroupService;
    }

    public void setFileGroupService(FileGroupService fileGroupService) {
	this.fileGroupService = fileGroupService;
    }

    public String paramDetails() {
	parameterList = getParameterService().findByParameter();
	switch (cName) {
	case JIRA_URL:
	    return parameterList.get(0).getValue();
	case JIRA_PREF:
	    return parameterList.get(1).getValue();
	case JIRA_LOGIN:
	    return parameterList.get(2).getValue();
	case SERVER_URL:
	    return parameterList.get(3).getValue();
	case REST_URL:
	    return parameterList.get(4).getValue();
	case JIRA_TAG_URL:
	    return parameterList.get(5).getValue();
	default:
	    return parameterList.get(6).getValue();
	}
    }

}
