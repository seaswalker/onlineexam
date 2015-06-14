package exam.model;

import java.io.Serializable;

import exam.util.json.JSON;
import exam.util.json.JSONAble;
import exam.util.json.JSONObject;

/**
 * 专业
 * @author skywalker
 *
 */
public class Major implements Serializable, JSONAble {

	private static final long serialVersionUID = 4732029763783198033L;
	
	private int id;
	private String name;
	
	public Major(int id) {
		this.id = id;
	}
	
	public Major(String name) {
		this.name = name;
	}
	
	public Major() {}
	
	public Major(int id, String name) {
		this.id = id;
		this.name = name;
	}
	
	public JSON getJSON() {
		JSONObject json = new JSONObject();
		json.addElement("id", String.valueOf(id)).addElement("name", name);
		return json;
	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
}
