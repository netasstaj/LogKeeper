package net.logkeeper.managedController;

import java.io.Serializable;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.servlet.http.HttpSession;

import org.primefaces.component.growl.Growl;
import org.primefaces.context.RequestContext;

@ManagedBean
@SessionScoped
public class Login implements Serializable {

    private static final long serialVersionUID = 1094801825228386363L;

    private String pwd;
    private String msg;
    private String user;

    public String getPwd() {
	return pwd;
    }

    public void setPwd(String pwd) {
	this.pwd = pwd;
    }

    public String getMsg() {
	return msg;
    }

    public void setMsg(String msg) {
	this.msg = msg;
    }

    public String getUser() {
	return user;
    }

    public void setUser(String user) {
	this.user = user;
    }

    // validate login
    public void addMessageInfo(String summary, String detail) {
	FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO,
		summary, detail);
	FacesContext.getCurrentInstance().addMessage(null, message);
    }

    public void addMessage(String summary, String detail) {
	FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_WARN,
		summary, detail);
	FacesContext.getCurrentInstance().addMessage(null, message);
    }

    public void validateUsernamePassword(ActionEvent event) {
	RequestContext context = RequestContext.getCurrentInstance();
	boolean loggedIn = false;
	App app = new App();
	boolean valid = app.validate(user, pwd);
	if (valid) {
	    HttpSession session = SessionBean.getSession();
	    session.setAttribute("username", user);
	    loggedIn = true;
	    addMessageInfo("Welcome", user);
	} else {
	    loggedIn = false;
	    addMessage("Loggin Error", "Invalid credentials");
	}

	// FacesContext.getCurrentInstance().addMessage(null, message);
	context.addCallbackParam("loggedIn", loggedIn);
    }

    // logout event, invalidate session
    public void logout() {
	HttpSession session = SessionBean.getSession();
	session.invalidate();
	// return "login";
    }
}