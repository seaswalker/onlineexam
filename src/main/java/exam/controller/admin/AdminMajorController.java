package exam.controller.admin;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import exam.model.Major;
import exam.model.page.PageBean;
import exam.service.MajorService;
import exam.util.DataUtil;
import exam.util.json.JSON;
import exam.util.json.JSONObject;

/**
 * 管理员专业控制器
 * @author skywalker
 *
 */
@Controller
@RequestMapping("/admin/major")
public class AdminMajorController {

	@Resource
	private MajorService majorService;
	@Value("#{properties['major.pageSize']}")
	private int pageSize;
	//显示的页码数量
	@Value("#{properties['major.pageNumber']}")
	private int pageNumber;
	
	/**
	 * 显示专业列表
	 * bug? 模糊搜索不能使用参数?
	 */
	@RequestMapping("/list")
	public String list(String pn, String search, Model model) {
		int pageCode = DataUtil.getPageCode(pn);
		String where = null;
		if(DataUtil.isValid(search)) {
			where = " where name like '%" + search + "%'";
		}
		PageBean<Major> pageBean = majorService.pageSearch(pageCode, pageSize, pageNumber, where, null, null);
		model.addAttribute("pageBean", pageBean);
		model.addAttribute("search", search);
		return "admin/major_list";
	}
	
	/**
	 * 添加专业
	 */
	@RequestMapping("/add")
	@ResponseBody
	public void add(String major, HttpServletResponse response) {
		JSON json = new JSONObject();
		if(!DataUtil.isValid(major)) {
			json.addElement("result", "0").addElement("message", "请输入专业名称");
		}else if(majorService.findByName(major) != null) {
			json.addElement("result", "0").addElement("message", "此专业已存在");
		}else {
			majorService.saveOrUpdate(new Major(major));
			json.addElement("result", "1").addElement("message", "专业添加成功");
		}
		DataUtil.writeJSON(json, response);
	}
	
	/**
	 * 修改专业
	 * @param id
	 * @param major
	 */
	@RequestMapping("/edit")
	@ResponseBody
	public void edit(String id, String major, HttpServletResponse response) {
		JSON json = new JSONObject();
		if(!DataUtil.isNumber(id)) {
			json.addElement("result", "0").addElement("message", "非法数据");
		}else if(!DataUtil.isValid(major)) {
			json.addElement("result", "0").addElement("message", "请输入专业名称");
		}else {
			int _id = Integer.parseInt(id);
			Major m = new Major();
			m.setId(_id);
			m.setName(major);
			majorService.saveOrUpdate(m);
			json.addElement("result", "1").addElement("message", "修改成功");
		}
		DataUtil.writeJSON(json, response);
	}
	
	/**
	 * 删除专业
	 * @param mid 专业id
	 */
	@RequestMapping("/delete/{mid}")
	@ResponseBody
	public void delete(@PathVariable Integer mid, HttpServletResponse response) {
		JSON json = new JSONObject();
		majorService.delete(mid);
		json.addElement("result", "1").addElement("message", "删除成功");
		DataUtil.writeJSON(json, response);
	}
	
}
