package exam.model.role;

import java.io.Serializable;

import exam.model.Clazz;

/**
 * 学生
 * @author skywalker
 *
 */
public class Student implements Serializable {

	private static final long serialVersionUID = -4447237686888091087L;
	
	private String id;
	private String name;
	private String password;
	private Clazz clazz;
	
	public String getId() {
		return id;
	}
	public Student setId(String id) {
		this.id = id;
		return this;
	}
	public String getName() {
		return name;
	}
	public Student setName(String name) {
		this.name = name;
		return this;
	}
	public String getPassword() {
		return password;
	}
	public Student setPassword(String password) {
		this.password = password;
		return this;
	}
	public Clazz getClazz() {
		return clazz;
	}
	public Student setClazz(Clazz clazz) {
		this.clazz = clazz;
		return this;
	}
	
}
