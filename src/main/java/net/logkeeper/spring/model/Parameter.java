package net.logkeeper.spring.model;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "parameter", catalog = "fileupload")
public class Parameter implements Serializable {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Integer id;
    @Column(name = "key_n", unique = true, length = 255)
    private String key;
    @Column(name = "value_n", length = 255)
    private String value;

    public Parameter() {
    }

    public Parameter(String key, String value) {

	this.key = key;
	this.value = value;
    }

    public Integer getId() {
	return this.id;
    }

    public void setId(Integer id) {
	this.id = id;
    }

    public String getKey() {
	return this.key;
    }

    public void setKey(String key) {
	this.key = key;
    }

    public String getValue() {
	return this.value;
    }

    public void setValue(String value) {
	this.value = value;
    }

    @Override
    public String toString() {
	return "Parameter [id=" + id + ", key=" + key + ", value=" + value
		+ "]";
    }

}
