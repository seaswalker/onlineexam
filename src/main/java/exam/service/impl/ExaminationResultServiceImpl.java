package exam.service.impl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import exam.dao.ExaminationResultDao;
import exam.dao.base.BaseDao;
import exam.dao.base.GenerateKeyCallback;
import exam.dto.ERView;
import exam.dto.MarkedQuestion;
import exam.dto.StatisticsData;
import exam.dto.ERView.ERViewQuestion;
import exam.model.ExaminationResult;
import exam.model.QuestionType;
import exam.service.ExaminationResultService;
import exam.service.base.BaseServiceImpl;

@Service("examinationResultService")
public class ExaminationResultServiceImpl extends BaseServiceImpl<ExaminationResult> implements ExaminationResultService {

	private ExaminationResultDao examinationResultDao;
	
	@Resource(name = "examinationResultDao")
	@Override
	protected void setBaseDao(BaseDao<ExaminationResult> baseDao) {
		super.baseDao = baseDao;
		this.examinationResultDao = (ExaminationResultDao) baseDao;
	}
	
	/**
	 * 更新功能没有实现,因为考试结果不可修改
	 */
	@Override
	public void saveOrUpdate(ExaminationResult entity) {
		//首先保存ExaminationResult
		String sql = "insert into examinationresult values(null, ?, ?, ?, ?, ?)";
		int erId = examinationResultDao.getKeyHelper(sql, new GenerateKeyCallback() {
			@Override
			public void setParameters(PreparedStatement ps, Object param) throws SQLException {
				ExaminationResult er = (ExaminationResult) param;
				ps.setInt(1, er.getExamId());
				ps.setString(2, er.getStudentId());
				ps.setInt(3, er.getPoint());
				ps.setTimestamp(4, new Timestamp(er.getTime().getTime()));
				ps.setString(5, er.getExamTitle());
			}
		}, entity);
		//然后保存各个题目
		StringBuilder sb = new StringBuilder("insert into examinationresult_question values");
		for (MarkedQuestion mq : entity.getMarkedQuestions()) {
			sb.append("(null,").append(erId).append(",").append(mq.getQuestionId()).append(",")
				.append(mq.isRight()).append(",'").append(mq.getWrongAnswer()).append("'),");
		}
		examinationResultDao.executeSql(sb.deleteCharAt(sb.length() - 1).toString());
	}
	
	@Override
	public ERView getViewById(int id) {
		//首先查出singlePoints、multiPoints、judgePoints、point、time
		String sql = "select e.singlepoints, e.multipoints, e.judgepoints, er.point, er.time from examinationresult er join exam e on e.id = er.eid where er.id = "
				+ id;
		ERView view = examinationResultDao.query(sql, new ResultSetExtractor<ERView>() {
			@Override
			public ERView extractData(ResultSet rs) throws SQLException, DataAccessException {
				ERView view = new ERView();
				//ResultSetExtractor接口必须手动调用next()
				if (rs.next()) {
					view.setJudgePoints(rs.getInt("judgepoints"));
					view.setSinglePoints(rs.getInt("singlepoints"));
					view.setMultiPoints(rs.getInt("multipoints"));
					view.setPoint(rs.getInt("point"));
					view.setTime(rs.getTimestamp("time"));
				}
				return view;
			}
		});
		//查出所有的ERViewQuestion
		sql = "select q.*, erq.right, erq.wronganswer from examinationresult_question erq join question q on q.id = erq.qid where erq.erid = "
				+ id;
		List<ERViewQuestion> questions = examinationResultDao.query(sql, new RowMapper<ERView.ERViewQuestion>() {
			@Override
			public ERViewQuestion mapRow(ResultSet rs, int rowNum) throws SQLException {
				ERViewQuestion question = new ERViewQuestion();
				//注意设置type在答案的前面!
				question.setType(QuestionType.valueOf(rs.getString("type")));
				question.setAnswer(rs.getString("answer"));
				question.setId(rs.getInt("id"));
				question.setOptionA(rs.getString("optionA"));
				question.setOptionB(rs.getString("optionB"));
				question.setOptionC(rs.getString("optionC"));
				question.setOptionD(rs.getString("optionD"));
				question.setPoint(rs.getInt("point"));
				question.setTitle(rs.getString("title"));
				question.setRight(rs.getBoolean("right"));
				question.setWrongAnswer(rs.getString("wronganswer"));
				return question;
			}
		});
		return filterQuestions(questions, view);
	}
	
	@Override
	public StatisticsData getStatisticsData(int eid) {
		String sql = "select er.sid, er.point, er.examtitle, s.name, e.points from examinationresult er join student s on s.id = er.sid join exam e on e.id = er.eid where er.eid = "
				+ eid;
		List<Helper> helpers = examinationResultDao.query(sql, new RowMapper<Helper>() {
			@Override
			public Helper mapRow(ResultSet rs, int rowNum) throws SQLException {
				return ExaminationResultServiceImpl.Helper.Builder().name(rs.getString("name"))
						.point(rs.getInt("point")).sid(rs.getString("sid")).title(rs.getString("examtitle")).total(rs.getInt("points"));
			}
		});
		return prepareData(helpers);
	}
	
	/**
	 * 为StatisticsData设置数据
	 * @param helpers
	 * @return
	 */
	private StatisticsData prepareData(List<Helper> helpers) {
		StatisticsData data = new StatisticsData();
		Helper first = helpers.get(0);
		int totalPoint = first.total;
		int max = first.point, min = first.point, point = 0, sp = (int)(totalPoint * 0.6), ep = (int)(totalPoint * 0.8), np = (int)(totalPoint * 0.9);
		List<String> maxNames  = new ArrayList<String>(), minNames = new ArrayList<>();
		data.setPersonCount(helpers.size());
		data.setTitle(first.examTitle);
		data.setExamPoints(totalPoint);
		//统计各分数段成绩
		data.setSixtyPoint(sp);
		data.setEighttyPoint(ep);
		data.setNinetyPoint(np);
		for (Helper helper : helpers) {
			point = helper.point;
			//寻找最高分和最低分
			if (point > max) {
				max = point;
			} else if (point < min) {
				min = point;
			}
			//设置分数区间
			if (point < sp) {
				data.addUnderSixty(point);
			} else if (point < ep) {
				data.addSixtyAndEighty(point);
			} else if (point < np) {
				data.addEightyAndNinety(point);
			} else {
				data.addAboveNinety(point);
			}
		}
		//第二遍循环搜索最高分和最低分的名字
		for (Helper helper : helpers) {
			if (helper.point == max) {
				maxNames.add(helper.name);
			} else if (helper.point == min) {
				minNames.add(helper.name);
			}
		}
		//最高分和最低分相等的极端情况
		if (max == min) {
			minNames = maxNames;
		}
		data.setHighestPoint(max);
		data.setLowestPoint(min);
		data.addHightestName(maxNames);
		data.addLowestNames(minNames);
		return data;
	}
	
	/**
	 * 把各个题型的题目分拣到题型list中
	 * @param questions
	 * @param view
	 */
	private ERView filterQuestions(List<ERViewQuestion> questions, ERView view) {
		questions.stream().forEach(question -> {
			if (question.getType() == QuestionType.SINGLE) {
				view.addSingleQuestion(question);
			} else if (question.getType() == QuestionType.MULTI) {
				view.addMultiQuestion(question);
			} else {
				view.addJudgeQuestion(question);
			}
		});
		return view;
	}
	
	/**
	 * 内部类，封装用作考试成绩统计的一条记录，不用ExaminationResult的原因是
	 * 需要查出考生的姓名以便统计最高分和最低分
	 * @author skywalker
	 *
	 */
	@SuppressWarnings("unused")
	private static class Helper {
		
		String sid;
		int point;
		//改试卷的总分
		int total;
		String examTitle;
		String name;
		
		private Helper() {}
		
		/**
		 * 构造器模式
		 * @return
		 */
		public static Helper Builder() {
			return new Helper();
		}
		
		public Helper sid(String sid) {
			this.sid = sid;
			return this;
		}
		
		public Helper point(int point) {
			this.point = point;
			return this;
		}
		
		public Helper name(String name) {
			this.name = name;
			return this;
		}
		
		public Helper title(String title) {
			this.examTitle = title;
			return this;
		}
		
		public Helper total(int total) {
			this.total = total;
			return this;
		}
		
	}

}
