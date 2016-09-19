package net.logkeeper.managedController;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.Part;

import net.logkeeper.spring.model.FileGroup;
import net.logkeeper.spring.model.LogFile;
import net.logkeeper.spring.model.Tag;
import net.logkeeper.spring.model.User;
import net.logkeeper.spring.service.FileGroupService;
import net.logkeeper.spring.service.LogFileService;
import net.logkeeper.spring.service.ParameterService;
import net.logkeeper.spring.service.TagService;
import net.logkeeper.spring.service.UserService;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.json.JSONObject;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.RowEditEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.tagcloud.DefaultTagCloudItem;
import org.primefaces.model.tagcloud.DefaultTagCloudModel;
import org.primefaces.model.tagcloud.TagCloudItem;
import org.primefaces.model.tagcloud.TagCloudModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lowagie.text.BadElementException;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.PageSize;

@ManagedBean(name = "app")
@SessionScoped
@Component
public class App implements Serializable {
    private Part file;
    private String Alani;
    private String KullaniciGirisi;
    private String FileGroup;
    private String fileGFileGroup;
    private String zipfile;
    private AdiAlani selectedAdiAlani;
    private StreamedContent sCFile;
    private int fileGroupId;
    private String fileGroupName;
    public static String fileRequestName;
    private boolean responseRenderedDescription;
    private boolean responseRendered = false;
    private TagCloudModel model;
    private StreamedContent pdfDocument;
    String previewPath;
    private String txtFileGroupName;
    private String txtFileUpload;
    public static String SERVER_URL;
    public static Parameters JIRA_URL;
    private String fileListDescription;
    private String language;
    private String dltFile;
    List<String> listZip = new ArrayList<String>();
    String newFileName = "";
    private List<String> search;
    private List<AdiAlani> filteredAdiAlani;
    List<String> listeDeneme = new ArrayList<String>();
    List<String> jiraTagList = new ArrayList<String>();
    List<String> listFile = new ArrayList<String>();
    List<String> listFileSize = new ArrayList<String>();
    List<String> listFileMsg = new ArrayList<String>();
    List<AdiAlani> ListPath = new ArrayList<AdiAlani>();
    List<AdiAlani> listFileGroups = new ArrayList<AdiAlani>();
    List<AdiAlani> listeRecord = new ArrayList<AdiAlani>();
    List<Integer> listTagIdInt = new ArrayList<Integer>();
    List<FileGroup> listFileGroup = new ArrayList<FileGroup>();

    StringBuilder builder = new StringBuilder();

    @Autowired
    public FileGroupService fileGroupService;

    @Autowired
    public TagService tagService;

    @Autowired
    public LogFileService logFileService;

    @Autowired
    private UserService userService;

    @Autowired
    private ParameterService parameterService;

    @PostConstruct
    public void init() {
	bringTagCloud();
	listsAll();
	SERVER_URL = parameterService.findByParameter("SERVER_URL").getValue();
    }

    public void bringTagCloud() {
	model = new DefaultTagCloudModel();
	List ListTags = tagService.listTag();

	List<Tag> tagList = new ArrayList<Tag>();
	if (ListTags != null) {
	    for (int k = 0; k < ListTags.size(); k++) {
		Object[] oTag = (Object[]) ListTags.get(k);
		Tag tag = new Tag();
		tag.setId(Integer.parseInt(oTag[0].toString()));
		tag.setName(oTag[1].toString());
		tagList.add(tag);
	    }
	    int i = 10;
	    int j = 5;
	    for (Tag tags : tagList) {
		switch (i) {
		case 8:
		case 7:
		    j = 4;
		    break;
		case 6:
		case 5:
		    j = 3;
		    break;
		case 4:
		case 3:
		    j = 2;
		    break;
		case 2:
		case 1:
		    j = 1;
		    break;
		}
		model.addTag(new DefaultTagCloudItem(tags.getName(), j));
		i--;
	    }
	}

    }

    public void listsAll() {
	List<FileGroup> FileGroupList = getFileGroupService()
		.listFileGroupUser();
	if (FileGroupList != null) {
	    for (int k = 0; k < FileGroupList.size(); k++) {
		FileGroup fileGroups = FileGroupList.get(k);
		AdiAlani adiAlani = new AdiAlani();
		adiAlani.setAlani(fileGroups.getName());
		adiAlani.setUser(fileGroups.getUser().getName());
		adiAlani.setDateName(fileGroups.getCreateDate());
		adiAlani.setId(fileGroups.getId());
		adiAlani.setUseful(fileGroups.getUseful());
		adiAlani.setUseless(fileGroups.getUseless());
		listeRecord.add(adiAlani);
	    }
	}
    }

    public void editAction() {
	Map<String, String> params = FacesContext.getCurrentInstance()
		.getExternalContext().getRequestParameterMap();
	fileGroupId = Integer.parseInt(params.get("fileGroupId"));
	fileGroupName = params.get("fileGroupName");
	fileNameAddZip(fileGroupId, fileGroupName);
	fileRequestName = getServlet("files.zip") + params.get("fileGroupName")
		+ ".zip";
    }

    String generateUniqueFileName() {
	String filename = "";
	long millis = System.currentTimeMillis();
	@SuppressWarnings("deprecation")
	String datetime = new Date().toGMTString();
	datetime = datetime.replace(" ", "");
	datetime = datetime.replace(":", "");
	String rndchars = RandomStringUtils.randomAlphanumeric(16);
	filename = rndchars + "_" + datetime + "_" + millis;
	return filename;
    }

    public List<String> complete(String languagePrefix, Boolean jira) {
	languagePrefix = languagePrefix + "%";
	List<String> liste = new ArrayList<String>();
	List<Tag> tagList = tagService.listTagFileGroup(languagePrefix, jira);
	for (int k = 0; k < tagList.size(); k++) {
	    Tag tags = tagList.get(k);

	    String temp = tags.getName();
	    for (String query : listeDeneme) {
		if (!query.equals(temp)) {
		    liste.add(temp);
		}
	    }
	}
	return (liste);
    }

    public List<String> completeTagSearch(String languagePrefix) {
	return complete(languagePrefix, false);
    }

    public String mergeQuery() {
	String merge = "";
	if (!listFileMsg.isEmpty()) {
	    for (String query : listFileMsg) {
		merge = merge + query + ", ";
	    }
	    merge = merge.substring(0, merge.length() - 2);
	}
	return merge;
    }

    public void addTagToDatabase(Boolean jira) {
	List<Tag> hashTagList = new ArrayList<Tag>();
	listFileMsg.clear();
	Calendar takvim = Calendar.getInstance();
	SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
	@SuppressWarnings("unchecked")
	List ListTags = tagService.findByTagId(fileGroupId);
	List<Tag> tagList = new ArrayList<Tag>();

	if (ListTags.size() <= 15) {
	    for (int k = 0; k < ListTags.size(); k++) {
		Object[] oTag = (Object[]) ListTags.get(k);
		Tag tag = new Tag();
		tag.setId(Integer.parseInt(oTag[0].toString()));
		tag.setName(oTag[1].toString());
		tag.setCreateDate(oTag[2].toString());
		tag.setJira(Boolean.parseBoolean(oTag[3].toString()));
		tagList.add(tag);
	    }
	    for (Tag tags : tagList) {
		for (String query : listeDeneme) {
		    if (tags.getName().equals(query)) {
			jiraTagList.remove(query);
			listFileMsg.add(query);
		    }
		}
	    }
	    if (mergeQuery() != "") {
		addMessageWarn("Warning", mergeQuery() + " already have");
	    }
	    listFileMsg.clear();
	    for (String query : jiraTagList) {
		Tag tag = tagService.findByTag(query);
		if (tag == null) {
		    Tag tags = new Tag();
		    tags.setName(query);
		    tags.setCreateDate(sdf.format(takvim.getTime()));
		    tags.setJira(jira);
		    tagService.save(tags);

		    if (jira) {

			if (query.substring(0, 4).equals("ABE-")
				|| query.substring(0, 4).equals("abe-")) {

			    if (query.length() > 4
				    && Integer.parseInt(query.substring(5,
					    query.length())) > 0) {

				Tag tag2 = tagService.findByTag(query);
				hashTagList.add(tag2);

				listFileMsg.add(query);

				if (mergeQuery() != "") {
				    FacesMessage message = new FacesMessage(
					    FacesMessage.SEVERITY_INFO,
					    "Tag has been added successfully.",
					    " ");
				    RequestContext.getCurrentInstance()
					    .showMessageInDialog(message);
				}
			    } else {
				FacesMessage message = new FacesMessage(
					FacesMessage.SEVERITY_WARN,
					"It isn't suitable for Jira tag structure!!",
					"Sapmle: ABE-556 ");
				RequestContext.getCurrentInstance()
					.showMessageInDialog(message);
			    }
			} else {
			    FacesMessage message = new FacesMessage(
				    FacesMessage.SEVERITY_WARN,
				    "It isn't suitable for Jira tag structure!!",
				    "Sample: ABE-556 ");
			    RequestContext.getCurrentInstance()
				    .showMessageInDialog(message);
			}
		    } else {
			Tag tag2 = tagService.findByTag(query);
			hashTagList.add(tag2);
			listFileMsg.add(query);

			if (mergeQuery() != "") {
			    addMessage("INFO", mergeQuery()
				    + " tag has been added successfully... ");
			} else {
			    addMessage("INFO", mergeQuery()
				    + " adding is unsuccessful... ");
			}

		    }

		} else {
		    hashTagList.add(tag);
		    listFileMsg.add(query);

		}

	    }

	    if (!jiraTagList.isEmpty()) {
		FileGroup fileGroups = getFileGroupService().findByFileGroup(
			fileGroupId);
		for (Tag tagStep : tagList) {
		    hashTagList.add(tagStep);
		}
		fileGroups.setTags(hashTagList);
		fileGroups.setCreateDate("24.03.2016");
		getFileGroupService().update(fileGroups);
	    }

	} else {
	    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_WARN,
		    "Tag limit is full!!", " ");
	    RequestContext.getCurrentInstance().showMessageInDialog(message);
	}
	clearList();
	search = null;
	getListFileGroup();
    }

    public List<String> register() {
	return listeDeneme;
    }

    public void clearList() {
	jiraTagList.clear();
	listeDeneme.clear();
    }

    public static void addMessageInfo(String summary, String detail) {
	FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO,
		summary, detail);
	FacesContext.getCurrentInstance().addMessage(null, message);
    }

    public void selectListener(SelectEvent event) {
	String selectedJiraTag = event.getObject().toString();
	String donen;
	NetClientGet nct = new NetClientGet();
	String url;
	try {
	    if (!(jiraTagList.contains(selectedJiraTag))) {

		if (jiraTagList.size() < 15) {
		    jiraTagList.add(selectedJiraTag);
		    listeDeneme.add(selectedJiraTag);

		    if (selectedJiraTag.length() > 4
			    && Integer.parseInt(selectedJiraTag.substring(5,
				    selectedJiraTag.length())) > 0) {

			if (selectedJiraTag.substring(0, 4).equals("ABE-")
				|| selectedJiraTag.substring(0, 4).equals(
					"abe-")) {

			    String databaseUrl = parameterService
				    .findByParameter("REST_URL").getValue();

			    url = databaseUrl + selectedJiraTag;

			    donen = nct.descriptionRestConn("GET", url,
				    LoginRest.jsessionId);

			    JSONObject donenJsonObject = new JSONObject(donen);
			    String jiraDescription = donenJsonObject
				    .getJSONObject("fields").getString(
					    "description");

			    if (jiraDescription.equals(null)) {
				addMessageInfo("Description area is empty!!",
					" ");
			    } else {
				String description = descriptionIntegration(
					selectedJiraTag, jiraDescription);
				setTxtFileUpload("");
				setTxtFileUpload(description);
				RequestContext.getCurrentInstance().update(
					"frmUpload:inptDescriptionFU");
			    }
			}
		    }
		} else {
		    FacesMessage message = new FacesMessage(
			    FacesMessage.SEVERITY_WARN, "Tag limit is full!!!",
			    " ");
		    RequestContext.getCurrentInstance().showMessageInDialog(
			    message);
		}
	    } else {
		FacesMessage message = new FacesMessage(
			FacesMessage.SEVERITY_WARN,
			"The same tag more than one can not add!!", " ");
		RequestContext.getCurrentInstance()
			.showMessageInDialog(message);
	    }
	} catch (Exception e) {
	    e.printStackTrace();
	}

    }

    public String descriptionIntegration(String selectedJiraTag,
	    String description) {

	builder.insert(0, "\n");
	builder.insert(0, description);
	builder.insert(0, "\n");
	builder.insert(0, selectedJiraTag);
	return builder.toString();

    }

    public void useful(AdiAlani dosyaAdi) {
	FileGroup fileGroup = getFileGroupService().findByFileGroup(
		dosyaAdi.getId());
	List<FileGroup> fileGroupList = fileGroupService
		.listFileGroup(fileGroup.getName());

	for (FileGroup likeFileGroup : fileGroupList) {
	    int likeLogFileId = likeFileGroup.getId();
	    FileGroup likeFileGroupEkleme = fileGroupService
		    .findByFileGroup(likeLogFileId);
	    likeFileGroupEkleme.setUseful(likeFileGroup.getUseful() + 1);
	    fileGroupService.update(likeFileGroupEkleme);

	}
	selectRecord();
    }

    public void useless(AdiAlani dosyaAdi) {
	FileGroup fileGroup = getFileGroupService().findByFileGroup(
		dosyaAdi.getId());
	List<FileGroup> fileGroupList = fileGroupService
		.listFileGroup(fileGroup.getName());

	for (FileGroup unlikeFileGroup : fileGroupList) {
	    int unlikeLogFileId = unlikeFileGroup.getId();
	    FileGroup unlikeFileGroupEkleme = fileGroupService
		    .findByFileGroup(unlikeLogFileId);
	    unlikeFileGroupEkleme.setUseless(unlikeFileGroup.getUseless() + 1);
	    fileGroupService.update(unlikeFileGroupEkleme);
	}
	selectRecord();

    }

    public void unselectListener(UnselectEvent event) {
	String itemSelected = event.getObject().toString();
	jiraTagList.remove(itemSelected.toString());
	listeDeneme.remove(itemSelected.toString());

	RequestContext.getCurrentInstance().update(
		"frmUpload:inptDescriptionFU");
    }

    public void readLogFile(String fileName) {
	FileReader file;
	String line = "";
	List<String> cases = new ArrayList<String>();
	try {
	    file = new FileReader(fileName);
	    BufferedReader reader = new BufferedReader(file);
	    while ((line = reader.readLine()) != null) {
		Pattern p = Pattern.compile("(ERROR|FAIL|WARN)");
		Matcher m = p.matcher(line);
		while (m.find()) {
		    if (!cases.contains(m.group())) {
			cases.add(m.group());
		    }
		}
	    }
	    System.out.println(fileName);
	} catch (FileNotFoundException e) {
	    throw new RuntimeException("File not found");
	} catch (IOException e) {
	    throw new RuntimeException("IO Error occured");
	}
	if (!cases.isEmpty()) {
	    for (String query : cases) {
		jiraTagList.add(query);
		listeDeneme.add(query);
	    }
	}
    }

    public void copyFile(String fileName, InputStream in) {
	try {
	    String randomFileName = generateUniqueFileName();
	    // write the inputStream to a FileOutputStream
	    File file = new File(getNewFileName() + randomFileName + "-"
		    + fileName);
	    OutputStream out = new FileOutputStream(file);
	    int read = 0;
	    byte[] bytes = new byte[1024];
	    while ((read = in.read(bytes)) != -1) {
		out.write(bytes, 0, read);
	    }
	    in.close();
	    out.flush();
	    out.close();
	    System.out.println("New file created!");
	    listFile.add(randomFileName + "-" + fileName);
	    readLogFile(file.getPath());
	} catch (IOException e) {
	    System.out.println(e.getMessage());
	}
    }

    public void fillFileName() {
	int i = 0;
	for (String fileName : listFile) {
	    addFileToDatabase(fileName, listFileSize.get(i));
	    i++;
	}
	listFileSize.clear();
	listFile.clear();
	listLogFile();

	txtFileGroupName = null;
	txtFileUpload = null;
	search = null;
	jiraTagList.clear();
    }

    public static void addMessage(String summary, String detail) {
	FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO,
		summary, detail);
	FacesContext.getCurrentInstance().addMessage(null, message);
    }

    public void addMessageError(String summary, String detail) {
	FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR,
		summary, detail);
	FacesContext.getCurrentInstance().addMessage(null, message);
    }

    public void addFileToDatabase(String fileName, String fileSize) {
	listFileGroups.clear();

	try {
	    List<Tag> tagy = new ArrayList<>();
	    List<String> listMerge = new ArrayList<String>(jiraTagList);
	    FileGroup fileGrp2 = getFileGroupService().findByFileGroup(
		    fileGroupId);
	    for (String kayit : listFile) {
		listMerge.add(getSplitFilePath(kayit));
	    }
	    Set<String> hs = new HashSet<>();
	    hs.addAll(listMerge);
	    listMerge.clear();
	    listMerge.addAll(hs);
	    Calendar takvim = Calendar.getInstance();
	    SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
	    for (Tag tag : fileGrp2.getTags()) {
		tagy.add(tag);
	    }
	    for (String kayit : listMerge) {
		Boolean kayitIp = false;
		Tag tag = tagService.findByTag(kayit);
		if (tag == null) {
		    tagy.add(new Tag(kayit, sdf.format(takvim.getTime()), false));
		} else {
		    for (Tag tag3 : tagy) {
			if (kayit.contains(tag3.getName())) {
			    kayitIp = true;
			}
		    }
		    if (!kayitIp) {
			tagy.add(tag);
		    }
		}
	    }

	    fileGrp2.setTags(tagy);
	    getFileGroupService().saveOrUpdate(fileGrp2);

	    LogFile file = new LogFile();
	    file.setPath(fileName);
	    file.setSize(fileSize);
	    file.setCreateDate(sdf.format(takvim.getTime()));
	    file.setEnabled("1");
	    file.setFileGroupId(fileGroupId);
	    getLogFileService().save(file);
	    getListFileGroup();
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    public void copyZip(String fileName, ZipOutputStream zos)
	    throws IOException {
	System.out.println("Writing '" + fileName + "' to zip file");
	File file = new File(fileName);
	FileInputStream fis = new FileInputStream(file);
	ZipEntry zipEntry = new ZipEntry(fileName);
	zos.putNextEntry(zipEntry);
	byte[] bytes = new byte[1024];
	int length;
	while ((length = fis.read(bytes)) >= 0) {
	    zos.write(bytes, 0, length);
	}
	zos.closeEntry();
	fis.close();
    }

    public void addMessageWarn(String summary, String detail) {
	FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_WARN,
		summary, detail);
	FacesContext.getCurrentInstance().addMessage(null, message);
    }

    public void btnFileUpload() throws ClassNotFoundException, SQLException {
	try {

	    FileGroup fileGrp = getFileGroupService().findByFileGroupName(
		    txtFileGroupName);
	    if (fileGrp == null && txtFileGroupName != "") {
		List<Tag> tagy = new ArrayList<Tag>();
		int userid = 0;
		Calendar takvim = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
		Set<String> hs = new HashSet<>();
		hs.addAll(jiraTagList);

		jiraTagList.clear();
		jiraTagList.addAll(hs);

		for (String kayit : jiraTagList) {
		    if (tagService.findByTag(kayit) != null) {
			Tag tagA = tagService.findByTag(kayit);
			tagy.add(tagA);
		    } else {
			tagy.add(new Tag(kayit, sdf.format(takvim.getTime()),
				false));
		    }
		}

		// list File; File Group'a ait file'l
		int idUserr = 0;
		User user = getUserService().idUser(SessionBean.getUserName());
		idUserr = user.getId();
		if (idUserr != 0) {
		    userid = idUserr;
		}
		if (listFile != null) {
		    for (String files : listFile) {
			if (tagService.findByTag(getSplitFilePath(files)) != null) {
			    Tag tagA = tagService
				    .findByTag(getSplitFilePath(files));
			    tagy.add(tagA);
			} else {
			    tagy.add(new Tag(getSplitFilePath(files), sdf
				    .format(takvim.getTime()), false));
			}
		    }
		}
		FileGroup fileGroups = new FileGroup();
		fileGroups.setName(txtFileGroupName);
		fileGroups.setCreateDate(sdf.format(takvim.getTime()));
		fileGroups.setUserId(userid);
		fileGroups.setTags(tagy);
		if (txtFileUpload != "") {
		    fileGroups.setDescription(txtFileUpload);
		}
		getFileGroupService().save(fileGroups);
		FileGroup fileGrp2 = getFileGroupService().findByFileGroupName(
			txtFileGroupName);
		int k = 0;

		for (String query : listFile) {
		    LogFile file = new LogFile();
		    file.setPath(query);
		    file.setSize(listFileSize.get(k));
		    file.setId(fileGrp2.getId());
		    file.setCreateDate(sdf.format(takvim.getTime()));
		    file.setEnabled("1");
		    file.setFileGroupId(fileGrp2.getId());
		    getLogFileService().save(file);
		    k++;
		}
		addMessage("Added: ", mergeList(listFile));
		listFileSize.clear();
		listFile.clear();
		clearList();
		txtFileGroupName = null;
		txtFileUpload = null;
		search = null;
		responseRenderedDescription = false;
		AdiAlani aa = new AdiAlani(fileGrp2.getId(),
			fileGrp2.getName(), fileGrp2.getUser().getName(),
			fileGrp2.getCreateDate());
		listeRecord.add(aa);
	    } else {
		txtFileGroupName = null;
		txtFileUpload = null;
		search = null;
		jiraTagList.clear();
		listFileSize.clear();
		listFile.clear();
		responseRenderedDescription = false;
		search = null;
		clearList();

		if (txtFileGroupName != "" && txtFileGroupName != null) {
		    addMessageError("Error", "File group name already exists!!");
		}

	    }
	    setFileGroup(txtFileGroupName);
	    System.out.println("Goodbye!");
	    listLogFile();
	    responseRenderedDescription = false;

	} catch (Exception e) {

	} finally {
	    builder.delete(0, builder.toString().length());
	    setTxtFileUpload(" ");
	    setTxtFileGroupName(" ");
	}
    }

    public List<String> completeTagDatabase(String languagePrefix) {
	List<String> liste = complete(languagePrefix, false);
	if (liste.size() == 0) {
	    liste.add(languagePrefix.substring(0, languagePrefix.length()));
	}
	return (liste);
    }

    public String mergeList(List<String> list) {
	String merge = "";
	if (!list.isEmpty()) {
	    for (String string : list) {
		merge = merge + getSplitFilePath(string) + ", ";
	    }
	    merge = merge.substring(0, merge.length() - 2);
	}
	return merge;
    }

    public void fileNameAddZip(int id, String fileName) {
	listZip.clear();
	List<LogFile> fileList = getLogFileService().listFile(id);
	for (int k = 0; k < fileList.size(); k++) {
	    LogFile file = fileList.get(k);
	    listZip.add(getServlet("files") + File.separator + file.getPath());
	}
	try {
	    FileOutputStream fos = new FileOutputStream(getServlet("files.zip")
		    + fileName + ".zip");
	    ZipOutputStream zos = new ZipOutputStream(fos);
	    for (String query : listZip) {
		System.out.println(query);
		copyZip(query, zos);
	    }
	    zos.close();
	    fos.close();
	} catch (FileNotFoundException e) {
	    e.printStackTrace();
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }

    public static String readableFileSize(long size) {
	if (size <= 0)
	    return "0";
	final String[] units = new String[] { "B", "KB", "MB", "GB", "TB" };
	int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
	return new DecimalFormat("#,##0.#").format(size
		/ Math.pow(1024, digitGroups))
		+ " " + units[digitGroups];
    }

    public void handleFileUpload(FileUploadEvent event) {
	setNewFileName(getServlet("files"));
	setZipfile(getServlet("files.zip"));
	try {
	    copyFile(event.getFile().getFileName(), event.getFile()
		    .getInputstream());
	    listFileSize.add(readableFileSize(event.getFile().getSize()));
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }

    public void selectRecord() {
	responseRendered = false;
	listeRecord.clear();
	listTagIdInt.clear();
	listFileGroup.clear();
	if (!jiraTagList.isEmpty()) {
	    for (String kayit : jiraTagList) {
		Tag tag = tagService.findByTag(kayit);
		tag.setClickCount(tag.getClickCount() + 1);
		tagService.update(tag);
		listTagIdInt.add(tag.getId());
	    }
	    List listFilee = getFileGroupService().getFilesByTag(listTagIdInt);
	    for (int k = 0; k < listFilee.size(); k++) {
		listFileGroup.add(getFileGroupService().findByFileGroup(
			(Integer) listFilee.get(k)));
	    }
	    listsFileGroup();
	} else {
	    listsAll();
	}
	clearList();
	search = null;
	bringTagCloud();
    }

    public void listsFileGroup() {
	for (int k = 0; k < listFileGroup.size(); k++) {
	    FileGroup fileGroupss = listFileGroup.get(k);
	    AdiAlani aa = new AdiAlani();
	    aa.setAlani(fileGroupss.getName());
	    aa.setUser(fileGroupss.getUser().getName());
	    aa.setDateName(fileGroupss.getCreateDate());
	    aa.setId(fileGroupss.getId());
	    listeRecord.add(aa);
	}
    }

    public void downloadAll() throws FileNotFoundException {
	LoginRest loginRest = new LoginRest();

	fileNameAddZip(fileGroupId, fileGroupName);
	dltFile = fileGroupName;
	InputStream stream = new FileInputStream(getServlet("files.zip") + ""
		+ fileGroupName + ".zip");
	sCFile = new DefaultStreamedContent(stream, "application/zip",
		fileGroupName + ".zip");
    }

    public void render() {
	if (ListPath.isEmpty()) {
	    responseRendered = false;
	} else {
	    responseRendered = true;
	}
    }

    public void delete(AdiAlani aa) {
	FileGroup fileGroup = getFileGroupService().findByFileGroup(aa.getId());
	List<LogFile> logFileList = logFileService.listFile(fileGroup.getId());
	fileGroupService.delete(fileGroup);
	for (LogFile logFile : logFileList) {
	    logFileService.delete(logFile);
	}

	listeRecord.remove(aa);
    }

    public void deleteFile(AdiAlani dFile) {
	LogFile logFile = getLogFileService().findByLogFile(
		Integer.parseInt(dFile.getIdFile()));
	removeFileFromFolder(logFile.getPath(), "files");
	getLogFileService().delete(Integer.parseInt(dFile.getIdFile()));
	ListPath.remove(dFile);
	if (ListPath.isEmpty()) {
	    getFileGroupService().delete(fileGroupId);
	    if (!listeRecord.isEmpty()) {
		for (AdiAlani a : listeRecord) {
		    if (a.getId() == fileGroupId) {
			listeRecord.remove(a);
			break;
		    }
		}
	    }
	    responseRendered = false;
	}
	FileGroup fileGroup = getFileGroupService()
		.findByFileGroup(fileGroupId);
	Tag tag = tagService.findByTag(dFile.getPath());
	List<Tag> lstTag = new ArrayList<>(fileGroup.getTags());
	for (int i = 0; i < lstTag.size(); i++) {
	    Tag tagO = lstTag.get(i);
	    String oldName = tagO.getName();
	    if (oldName.equals(tag.getName())) {
		lstTag.remove(i);
		i--;
	    }
	}
	fileGroup.setTags(lstTag);
	getFileGroupService().update(fileGroup);
	getListFileGroup();
    }

    public void deleteAllFile() {
	FileGroup fileGroup = getFileGroupService()
		.findByFileGroup(fileGroupId);

	if (!listeRecord.isEmpty()) {
	    for (AdiAlani a : listeRecord) {
		if (a.getId() == fileGroupId) {
		    listeRecord.remove(a);
		    break;
		}
	    }
	}
	responseRendered = false;
    }

    public List<String> completeTagJira(String languagePrefix) {
	List<String> liste = complete(languagePrefix, true);
	if (liste.size() == 0) {
	    liste.add(languagePrefix.substring(0, languagePrefix.length()));
	}
	return (liste);
    }

    public void fileRequestParam() {
	Map<String, String> params = FacesContext.getCurrentInstance()
		.getExternalContext().getRequestParameterMap();
	if (isInteger(params.get("fileName"))) {
	    LogFile logFile = getLogFileService().findByLogFile(
		    Integer.parseInt(params.get("fileName")));
	    fileRequestName = getServlet("files") + logFile.getPath();
	} else {
	    fileRequestName = getServlet("files") + params.get("fileName");
	}
    }

    public void removeFileFromFolder(String path, String folder) {
	try {
	    File file = new File(getServlet(folder) + path);
	    if (file.delete()) {
		System.out.println(file.getName() + " is deleted!");
	    } else {
		System.out.println("Delete operation is failed.");
	    }
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    public void tagRequestParamJira() throws IOException {
	Map<String, String> params = FacesContext.getCurrentInstance()
		.getExternalContext().getRequestParameterMap();
	FacesContext faces = FacesContext.getCurrentInstance();
	ExternalContext context = faces.getExternalContext();
	String jiraTagUrl = parameterService.findByParameter("JIRA_TAG_URL")
		.getValue();
	context.redirect(jiraTagUrl + params.get("fileGroupNameJira"));
    }

    public void tagRequestParam() {
	Map<String, String> params = FacesContext.getCurrentInstance()
		.getExternalContext().getRequestParameterMap();
	FileGroup fileGroup = getFileGroupService()
		.findByFileGroup(fileGroupId);
	Tag tag = tagService.findByTag(Integer.parseInt(params
		.get("ffileGroupId")));
	List<Tag> lstTag = new ArrayList<>(fileGroup.getTags());
	for (int i = 0; i < lstTag.size(); i++) {
	    Tag tagO = lstTag.get(i);
	    String oldName = tagO.getName();
	    if (oldName.equals(tag.getName())) {
		lstTag.remove(i);
		i--;
	    }
	}
	fileGroup.setTags(lstTag);
	getFileGroupService().update(fileGroup);
	getListFileGroup();
    }

    public void copyLink() {
	StringSelection stringSelection = new StringSelection(fileRequestName);
	Clipboard clpbrd = Toolkit.getDefaultToolkit().getSystemClipboard();
	clpbrd.setContents(stringSelection, null);
	addMessage("Successful", "Text Copied");
    }

    public void onRowEdit(RowEditEvent event) {
	addMessage("Car Edited", "" + ((AdiAlani) event.getObject()).getId());
    }

    public void onRowCancel(RowEditEvent event) {
	addMessage("Edit Cancelled",
		"" + ((AdiAlani) event.getObject()).getId());
    }

    public void onSelect(SelectEvent event) {
	responseRendered = false;
	TagCloudItem item = (TagCloudItem) event.getObject();
	listeRecord.clear();
	List ListFile = getFileGroupService().lisTagFileGroup(item.getLabel());
	List<FileGroup> fileList = new ArrayList<FileGroup>();
	for (int k = 0; k < ListFile.size(); k++) {
	    Object[] oFile = (Object[]) ListFile.get(k);
	    FileGroup files = new FileGroup();
	    files.setId(Integer.parseInt(oFile[0].toString()));
	    files.setName(oFile[1].toString());
	    files.setUser((User) oFile[2]);
	    files.setCreateDate(oFile[3].toString());
	    fileList.add(files);
	}
	for (FileGroup fileG : fileList) {
	    AdiAlani aa = new AdiAlani();
	    aa.setAlani(fileG.getName());
	    aa.setId(fileG.getId());
	    aa.setUser(fileG.getUser().getName());
	    aa.setDateName(fileG.getCreateDate());
	    listeRecord.add(aa);
	}
	Tag tag = getTagService().findByTag(item.getLabel());
	tag.setClickCount(tag.getClickCount() + 1);
	getTagService().update(tag);
    }

    public void postProcessXLS(Object document) {
	HSSFWorkbook wb = (HSSFWorkbook) document;
	HSSFSheet sheet = wb.getSheetAt(0);
	HSSFRow header = sheet.getRow(0);

	HSSFCellStyle cellStyle = wb.createCellStyle();
	cellStyle.setFillForegroundColor(HSSFColor.GREEN.index);
	cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

	for (int i = 0; i < header.getPhysicalNumberOfCells(); i++) {
	    HSSFCell cell = header.getCell(i);

	    cell.setCellStyle(cellStyle);
	}
    }

    public void preProcessPDF(Object document) throws IOException,
	    BadElementException, DocumentException {
	Document pdf = (Document) document;
	pdf.open();
	pdf.setPageSize(PageSize.A4);
    }

    public void downloadFile(AdiAlani dFile) throws FileNotFoundException {
	LogFile logFile = getLogFileService().findByLogFile(
		Integer.parseInt(dFile.getIdFile()));
	InputStream stream = new FileInputStream(getServlet("files") + ""
		+ logFile.getPath());
	if (logFile.getPath().contains("-")) {
	    sCFile = new DefaultStreamedContent(stream, "", logFile.getPath()
		    .substring(logFile.getPath().indexOf("-") + 1,
			    logFile.getPath().length()));
	} else {
	    sCFile = new DefaultStreamedContent(stream, "", logFile.getPath());
	}
    }

    public void getPDF() throws FileNotFoundException {
	FileInputStream fis = new FileInputStream(new File(previewPath));
	pdfDocument = new DefaultStreamedContent(fis, "application/pdf");
    }

    public void previewFile(AdiAlani pFile) throws FileNotFoundException {
	File file = new File("" + getServlet("files") + pFile.getPath());
	previewPath = ""
		+ getServlet("files.pdf")
		+ pFile.getPath()
			.substring(0, pFile.getPath().lastIndexOf('.'))
		+ ".pdf";
	ConvertPdf.convertTextfileToPDF(file);
	getPDF();
    }

    public void addDescription() {
	if (fileListDescription != "") {
	    FileGroup fileGroups = getFileGroupService().findByFileGroup(
		    fileGroupId);
	    fileGroups.setDescription(fileListDescription);
	    getFileGroupService().update(fileGroups);
	    addMessage("INFO", "description has been added successfully");
	    sendDescription();
	    // testString = null;
	    responseRenderedDescription = false;
	}
    }

    public boolean isInteger(String s) {
	if (StringUtils.isNumeric(s)) {
	    return true;
	} else {
	    return false;
	}
    }

    public void listLogFile() {
	ListPath.clear();
	List<LogFile> fileList = getLogFileService().listFile(fileGroupId);
	for (int k = 0; k < fileList.size(); k++) {
	    // List<String> listFileTags = new ArrayList<String>();
	    LogFile file = fileList.get(k);
	    AdiAlani aa = new AdiAlani();
	    aa.setPath(getSplitFilePath(file.getPath()));
	    aa.setIdFile(file.getId() + "");
	    // LogFile logTags =
	    // getLogFileService().findByLogFile(file.getId());
	    // if (file.getFileTag() != null) {
	    // String[] parts = file.getFileTag().split(",");
	    // for (int j = 0; j < parts.length; j++) {
	    // listFileTags.add(parts[j]);
	    // }
	    // }
	    // aa.setFileTag(listFileTags);
	    aa.setDateName(file.getCreateDate() + "");
	    aa.setSize(file.getSize() + "");
	    ListPath.add(aa);
	}
    }

    public void onRowSelect(SelectEvent event) throws ClassNotFoundException,
	    SQLException {
	fileGroupId = ((AdiAlani) event.getObject()).getId();
	fileGroupName = ((AdiAlani) event.getObject()).getAlani();
	listLogFile();
	sendDescription();
	renderDescription();
	render();
	getListFileGroup();
	renderDescription();
	sendDescription();
    }

    String getSplitFilePath(String path) {
	if (path.contains("-")) {
	    return path.substring(path.indexOf("-") + 1, path.lastIndexOf("."));
	} else {
	    return path;
	}
    }

    public void getListFileGroup() {
	listFileGroups.clear();
	List ListTags = tagService.findByTagId(fileGroupId);
	for (int k = 0; k < ListTags.size(); k++) {
	    Object[] oTag = (Object[]) ListTags.get(k);
	    AdiAlani aa = new AdiAlani();
	    aa.setId(Integer.parseInt(oTag[0].toString()));
	    aa.setFileGroup(oTag[1].toString());
	    aa.setJira(Boolean.parseBoolean(oTag[3].toString()));
	    listFileGroups.add(aa);
	}
    }

    public void renderDescription() {
	if (fileListDescription != null) {
	    responseRenderedDescription = false;
	} else {
	    responseRenderedDescription = true;
	}
    }

    public void getFileReq() {
	fileNameAddZip(fileGroupId, fileGroupName);
	fileRequestName = getServlet("files.zip") + fileGroupName + ".zip";
    }

    public void onRowUnselect(UnselectEvent event) {
	FacesMessage msg = new FacesMessage("Car Unselected",
		((AdiAlani) event.getObject()).getId() + "");
	FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public void sendDescription() {
	FileGroup fileGroups = getFileGroupService().findByFileGroup(
		fileGroupId);
	fileListDescription = fileGroups.getDescription();
    }

    public void download(AdiAlani aDownload) throws ClassNotFoundException,
	    SQLException, FileNotFoundException {
	fileNameAddZip(aDownload.getId(), aDownload.getAlani());
	dltFile = aDownload.getAlani();
	InputStream stream = new FileInputStream(getServlet("files.zip") + ""
		+ aDownload.getAlani() + ".zip");
	sCFile = new DefaultStreamedContent(stream, "application/zip",
		aDownload.getAlani() + ".zip");
    }

    public void downloadAfterDeleteZip() {
	removeFileFromFolder("" + dltFile + ".zip", "files.zip");
    }

    public boolean validate(String name, String password) {
	return getUserService().validate(name, password);
    }

    public String getTxtFileUpload() {
	return txtFileUpload;
    }

    public void setTxtFileUpload(String txtFileUpload) {
	this.txtFileUpload = txtFileUpload;
    }

    public String getTestString() {
	return fileListDescription;
    }

    public void setTestString(String testString) {
	this.fileListDescription = testString;
    }

    public boolean isResponseRendered() {
	return responseRendered;
    }

    public void setResponseRendered(boolean responseRendered) {
	this.responseRendered = responseRendered;
    }

    public String getFileRequestName() {
	return fileRequestName;
    }

    public void setFileRequestName(String fileRequestName) {
	this.fileRequestName = fileRequestName;
    }

    public List<AdiAlani> getListFileGroups() {
	return listFileGroups;
    }

    public void setListFileGroups(List<AdiAlani> listFileGroups) {
	this.listFileGroups = listFileGroups;
    }

    public String getFileGroupName() {
	return fileGroupName;
    }

    public void setFileGroupName(String fileGroupName) {
	this.fileGroupName = fileGroupName;
    }

    public int getFileGroupId() {
	return fileGroupId;
    }

    public void setFileGroupId(int fileGroupId) {
	this.fileGroupId = fileGroupId;
    }

    public List<AdiAlani> getListe() {
	return listeRecord;
    }

    public void setListe(List<AdiAlani> liste) {
	this.listeRecord = liste;
    }

    public FileGroupService getFileGroupService() {
	return fileGroupService;
    }

    public void setFileGroupService(FileGroupService fileGroupService) {
	this.fileGroupService = fileGroupService;
    }

    public TagService getTagService() {
	return tagService;
    }

    public void setTagService(TagService tagService) {
	this.tagService = tagService;
    }

    public LogFileService getLogFileService() {
	return logFileService;
    }

    public void setLogFileService(LogFileService logFileService) {
	this.logFileService = logFileService;
    }

    public UserService getUserService() {
	return userService;
    }

    public void setUserService(UserService userService) {
	this.userService = userService;
    }

    public ParameterService getParameterService() {
	return parameterService;
    }

    public void setParameterService(ParameterService parameterService) {
	this.parameterService = parameterService;
    }

    public static String getServlet(String tur) {
	return SERVER_URL + tur + File.separator;
    }

    public static boolean isPostback() {
	return FacesContext.getCurrentInstance().isPostback();
    }

    public StreamedContent getsCFile() {
	return sCFile;
    }

    public void setsCFile(StreamedContent sCFile) {
	this.sCFile = sCFile;
    }

    public List<AdiAlani> getListPath() {
	return ListPath;
    }

    public void setListPath(List<AdiAlani> listPath) {
	ListPath = listPath;
    }

    public AdiAlani getSelectedAdiAlani() {
	return selectedAdiAlani;
    }

    public void setSelectedAdiAlani(AdiAlani selectedAdiAlani) {
	this.selectedAdiAlani = selectedAdiAlani;
    }

    public String getZipfile() {
	return zipfile;
    }

    public void setZipfile(String zipfile) {
	this.zipfile = zipfile;
    }

    public String getFileGFileGroup() {
	return fileGFileGroup;
    }

    public void setFileGFileGroup(String fileGFileGroup) {
	this.fileGFileGroup = fileGFileGroup;
    }

    public String getFileGroup() {
	return FileGroup;
    }

    public void setFileGroup(String FileGroup) {
	this.FileGroup = FileGroup;
    }

    public List<String> getListeDeneme() {
	return listeDeneme;
    }

    public void setListeDeneme(List<String> listeDeneme) {
	this.listeDeneme = listeDeneme;
    }

    public List<String> getSearch() {
	return search;
    }

    public void setSearch(List<String> search) {
	this.search = search;
    }

    public String getLanguage() {
	return (language);
    }

    public void setLanguage(String language) {
	this.language = language;
    }

    public String getKullaniciGirisi() {
	return KullaniciGirisi;
    }

    public void setKullaniciGirisi(String kullaniciGirisi) {
	KullaniciGirisi = kullaniciGirisi;
    }

    public String getAlani() {
	return Alani;
    }

    public void setAlani(String Alani) {
	this.Alani = Alani;
    }

    public Part getFile() {
	return file;
    }

    public void setFile(Part file) {
	this.file = file;
    }

    public String getNewFileName() {
	return newFileName;
    }

    public void setNewFileName(String newFileName) {
	this.newFileName = newFileName;
    }

    public String getUpperCase() {
	return fileGroupName.substring(0, 1).toUpperCase()
		+ fileGroupName.substring(1, fileGroupName.length());
    }

    public List<String> getListFileSize() {
	return listFileSize;
    }

    public void setListFileSize(List<String> listFileSize) {
	this.listFileSize = listFileSize;
    }

    public List<AdiAlani> getFilteredAdiAlani() {
	return filteredAdiAlani;
    }

    public void setFilteredAdiAlani(List<AdiAlani> filteredAdiAlani) {
	this.filteredAdiAlani = filteredAdiAlani;
    }

    public String getTxtFileGroupName() {
	return txtFileGroupName;
    }

    public void setTxtFileGroupName(String txtFileGroupName) {
	this.txtFileGroupName = txtFileGroupName;
    }

    public String getDltFile() {
	return dltFile;
    }

    public void setDltFile(String dltFile) {
	this.dltFile = dltFile;
    }

    public String getPreviewPath() {
	return previewPath;
    }

    public void setPreviewPath(String previewPath) {
	this.previewPath = previewPath;
    }

    public void setPdfDocument(StreamedContent pdfDocument) {
	this.pdfDocument = pdfDocument;
    }

    public StreamedContent getPdfDocument() {
	return pdfDocument;
    }

    public TagCloudModel getModel() {
	return model;
    }

    public boolean isResponseRenderedDescription() {
	return responseRenderedDescription;
    }

    public void setResponseRenderedDescription(
	    boolean responseRenderedDescription) {
	this.responseRenderedDescription = responseRenderedDescription;
    }

}
