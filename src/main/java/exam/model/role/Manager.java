package exam.model.role;

import java.io.Serializable;

/**
 * 管理员
 * @author skywalker
 *
 */
public class Manager implements Serializable {

	private static final long serialVersionUID = -1615504534445176240L;
	
	private int id;
	private String name;
	private String password;
	
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
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
}
