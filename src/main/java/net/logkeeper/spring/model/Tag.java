package net.logkeeper.spring.model;
// Generated 16.Mar.2016 09:29:26 by Hibernate Tools 3.4.0.CR1

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

/**
 * Tag generated by hbm2java
 */
@Entity
@Table(name = "tag", catalog = "fileupload")
public class Tag implements Serializable {

	private Integer id;
	private String name;
	private String createDate;
	private Set<FileGroup> filegroup = new HashSet<FileGroup>(0);
        private int clickCount;
        private boolean jira;
        
	public Tag()
	{
		
	}

	public Tag(String name) {
		this.name = name;
	}

	public Tag(String name, String createDate) {
		this.name = name;
		this.createDate = createDate;
	}
	
	public Tag(String name, String createDate, boolean jira) {
		this.name = name;
		this.createDate = createDate;
		this.jira=jira;
	}
	
	
	
	@Override
	public String toString() {
		return "Tag [id=" + id + ", name=" + name + ", createDate=" + createDate + ", filegroup=" + filegroup
				+ ", clickCount=" + clickCount + ", jira=" + jira + "]";
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)

	@Column(name = "id", unique = true, nullable = false)
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "name", nullable = false)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "create_date", length = 45)
	public String getCreateDate() {
		return this.createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	@ManyToMany(fetch=FetchType.EAGER, mappedBy = "tags")
	@Cascade(CascadeType.DELETE)
	public Set<FileGroup> getFilegroup() {
		return this.filegroup;
	}
	
	@Column(name = "clickCount")
	public int getClickCount() {
		return this.clickCount;
	}

	public void setClickCount(int clickCount) {
		this.clickCount = clickCount;
	}
	
	@Column(name = "jira")
	public Boolean getJira() {
		return this.jira;
	}

	public void setJira(Boolean jira) {
		this.jira = jira;
	}
	public void setNames(Set<FileGroup> filegroup) {
		this.filegroup = filegroup;
	}

	public void setFilegroup(Set<FileGroup> filegroup) {
		this.filegroup = filegroup;
	}
	
}
