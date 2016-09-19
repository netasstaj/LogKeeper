package net.logkeeper.managedController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.servlet.http.HttpSession;

import net.logkeeper.spring.service.ParameterService;

import org.apache.log4j.Logger;
import org.json.JSONObject;

@ManagedBean(name = "netClientGet")
@SessionScoped
public class NetClientGet implements Serializable {
    private static Logger logger = Logger.getLogger(NetClientGet.class);
    private static final long serialVersionUID = 4778576272893200307L;

    public NetClientGet() {
    }

    private static String jsonDataStr;

    public static String getJsonDataStr() {
	return jsonDataStr;
    }

    public static void setJsonDataStr(String jsonDataStr) {
	NetClientGet.jsonDataStr = jsonDataStr;
    }

    public InputStream restfulConn(String metod,
	    ParameterService parameterService, String urlString,
	    String jsessionId) {
	try {
	    HttpSession session = SessionBean.getSession();
	    jsessionId = (String) session.getAttribute("jsessionId");
	    URL url = new URL(urlString);
	    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	    conn.setRequestMethod(metod);
	    conn.setRequestProperty("Accept", "application/json");
	    conn.setRequestProperty("Cookie", "JSESSIONID=" + jsessionId);

	    if (conn.getResponseCode() == 404) {
		throw new RuntimeException("Failed : HTTP error code : "
			+ conn.getResponseCode());
	    }

	    return conn.getInputStream();
	} catch (MalformedURLException e) {
	    e.printStackTrace();
	    return null;
	} catch (IOException e) {
	    e.printStackTrace();
	    return null;
	}
    }

    public static String connectionRest(String method,
	    JSONObject kullaniciBilgileri, String urlDonen) {
	URL url;

	HttpURLConnection connection = null;
	try {
	    url = new URL(urlDonen);
	    connection = (HttpURLConnection) url.openConnection();
	    connection.setRequestMethod(method);
	    connection.setRequestProperty("Content-Type", "application/json");
	    connection.setDoInput(true);
	    connection.setDoOutput(true);
	    OutputStream os = (OutputStream) connection.getOutputStream();
	    os.write(kullaniciBilgileri.toString().getBytes("UTF-8"));

	    InputStream is = connection.getInputStream();
	    BufferedReader br = new BufferedReader(new InputStreamReader(is));

	    String jsonData = "";
	    String line;
	    while ((line = br.readLine()) != null) {
		jsonData += line + "\n";
	    }
	    jsonDataStr = jsonData;
	    return jsonData;
	} catch (IOException e) {
	    e.printStackTrace();
	    return null;
	}
    }

    public static String descriptionRestConn(String method, String urlString,
	    String jsessionId) {

	URL url;

	HttpURLConnection connection = null;
	try {
	    HttpSession session = SessionBean.getSession();
	    jsessionId = (String) session.getAttribute("jsessionId");
	    url = new URL(urlString);
	    connection = (HttpURLConnection) url.openConnection();
	    connection.setRequestMethod(method);
	    connection.setRequestProperty("Content-Type", "application/json");
	    connection.setRequestProperty("Cookie", "JSESSIONID=" + jsessionId);

	    if (connection.getResponseCode() != 200) {
		throw new RuntimeException("Failed : HTTP error code : "
			+ connection.getResponseCode());
	    }

	    InputStream is = connection.getInputStream();
	    BufferedReader br = new BufferedReader(new InputStreamReader(is));

	    String jsonData = "";
	    String line;
	    while ((line = br.readLine()) != null) {
		jsonData += line + "\n";
	    }
	    jsonDataStr = jsonData;
	    return jsonData;
	} catch (IOException e) {
	    e.printStackTrace();
	    return null;
	}

    }

    public String jsonData(ParameterService parameterService, String url,
	    String jsessionId) {
	HttpSession session = SessionBean.getSession();
	jsessionId = (String) session.getAttribute("jsessionId");
	InputStream is = restfulConn("GET", parameterService, url, jsessionId);
	BufferedReader br = new BufferedReader(new InputStreamReader(is));
	String jsonData = "";
	try {
	    String line;
	    while ((line = br.readLine()) != null) {
		jsonData += line + "\n";
	    }
	} catch (IOException e) {
	    e.printStackTrace();
	} finally {
	    try {
		if (br != null)
		    br.close();
	    } catch (IOException ex) {
		ex.printStackTrace();
	    }
	}
	jsonDataStr = jsonData;

	return jsonData;
    }
}
