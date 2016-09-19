package net.logkeeper.managedController;

import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;

import org.json.JSONException;
import org.json.JSONObject;

import net.logkeeper.spring.model.User;
import net.logkeeper.spring.service.ParameterService;
import net.logkeeper.spring.service.UserService;

@ManagedBean(name = "register")
@SessionScoped
public class Register implements Serializable {
    private String name;
    private String surname;
    private String email;
    private String pwdRegister;

    public Register() {

    }

    public void addRestRegister(ParameterService parameterService,
	    UserService userService) throws JSONException, IOException {
	JSONObject obj = new JSONObject(NetClientGet.getJsonDataStr());
	Calendar takvim = Calendar.getInstance();
	SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
	User userValidate = userService
		.emailUser(obj.getString("emailAddress"));
	if (userValidate == null) {
	    User user = new User(obj.getString("name"),
		    obj.getString("surname"), obj.getString("emailAddress"),
		    obj.getString("key"), sdf.format(takvim.getTime()), true);
	    userService.save(user);
	}
    }

    public void clearInputText() {
	this.name = null;
	this.surname = null;
	this.email = null;
	this.pwdRegister = null;
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public String getSurname() {
	return surname;
    }

    public void setSurname(String surname) {
	this.surname = surname;
    }

    public String getEmail() {
	return email;
    }

    public void setEmail(String email) {
	this.email = email;
    }

    public String getPwdRegister() {
	return pwdRegister;
    }

    public void setPwdRegister(String pwdRegister) {
	this.pwdRegister = pwdRegister;
    }

}
