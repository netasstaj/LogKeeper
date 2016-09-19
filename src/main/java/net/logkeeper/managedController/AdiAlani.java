package net.logkeeper.managedController;

import java.io.Serializable;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import net.logkeeper.spring.model.FileGroup;
import net.logkeeper.spring.service.FileGroupService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@ManagedBean(name = "adiAlani")
@SessionScoped
@Component
public class AdiAlani implements Serializable {
    private String Alani;
    private String Category;
    private String KullaniciGirisi;
    private String PathName;
    private String Path;
    private String Extension;
    private String IdFile;
    private String DateName;
    private String User;
    private String fileGroup;
    private String Size;
    private Boolean Jira;
    private List<String> fileTag;
    private int id;
    private AdiAlani selectedFile;
    private int useful;
    private int useless;

    public AdiAlani() {

    }

    public AdiAlani(int id, String Alani, String User, String DateName) {
	this.id = id;
	this.Alani = Alani;
	this.User = User;
	this.DateName = DateName;
    }

    public List<String> getFileTag() {
	return fileTag;
    }

    public void setFileTag(List<String> fileTag) {
	this.fileTag = fileTag;
    }

    public Boolean getJira() {
	return Jira;
    }

    public void setJira(Boolean jira) {
	Jira = jira;
    }

    public String getSize() {
	return Size;
    }

    public void setSize(String size) {
	Size = size;
    }

    boolean editable;

    public boolean isEditable() {
	return editable;
    }

    public void setEditable(boolean editable) {
	this.editable = editable;
    }

    public String getFileGroup() {
	return fileGroup;
    }

    public void setFileGroup(String fileGroup) {
	this.fileGroup = fileGroup;
    }

    public String getUser() {
	return User;
    }

    public void setUser(String user) {
	User = user;
    }

    public String getDateName() {
	return DateName;
    }

    public void setDateName(String dateName) {
	DateName = dateName;
    }

    public String getIdFile() {
	return IdFile;
    }

    public void setIdFile(String idFile) {
	IdFile = idFile;
    }

    public String getExtension() {
	return Extension;
    }

    public void setExtension(String extension) {
	Extension = extension;
    }

    public String getPath() {
	return Path;
    }

    public void setPath(String path) {
	Path = path;
    }

    public AdiAlani getSelectedFile() {
	return selectedFile;
    }

    public void setSelectedFile(AdiAlani selectedFile) {
	this.selectedFile = selectedFile;
    }

    public int getId() {
	return id;
    }

    public void setId(int id) {
	this.id = id;
    }

    public String getPathName() {
	return PathName;
    }

    public void setPathName(String pathName) {
	PathName = pathName;
    }

    public String getKullaniciGirisi() {
	return KullaniciGirisi;
    }

    public void setKullaniciGirisi(String kullaniciGirisi) {
	KullaniciGirisi = kullaniciGirisi;
    }

    public String getCategory() {
	return Category;
    }

    public void setCategory(String category) {
	Category = category;
    }

    public String getAlani() {
	return Alani;
    }

    public void setAlani(String Alani) {
	this.Alani = Alani;
    }

    @Autowired
    public FileGroupService fileGroupService;

    public FileGroupService getFileGroupService() {
	return fileGroupService;
    }

    public void setFileGroupService(FileGroupService fileGroupService) {
	this.fileGroupService = fileGroupService;
    }

    public int getUseful() {
	return useful;
    }

    public void setUseful(int useful) {
	this.useful = useful;
    }

    public int getUseless() {
	return useless;
    }

    public void setUseless(int useless) {
	this.useless = useless;
    }

    public void saveAction(AdiAlani aa) {
	// get all existing value but set "editable" to false
	aa.setEditable(false);
	FileGroup fileGroups = getFileGroupService()
		.findByFileGroup(aa.getId());
	fileGroups.setName(aa.getAlani());
	getFileGroupService().update(fileGroups);
	// return to current page
    }

    public void editAction(AdiAlani aa) {
	aa.setEditable(true);
    }

    public void closeAction(AdiAlani aa) {
	aa.setEditable(false);
    }
}