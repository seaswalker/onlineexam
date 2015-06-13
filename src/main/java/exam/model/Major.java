package exam.model;

import java.io.Serializable;

/**
 * 专业
 * @author skywalker
 *
 */
public class Major implements Serializable {

	private static final long serialVersionUID = 4732029763783198033L;
	
	private int id;
	private String name;
	
	public Major(int id) {
		this.id = id;
	}
	
	public Major() {}
	
	public Major(int id, String name) {
		this.id = id;
		this.name = name;
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
