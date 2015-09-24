package exam.util;

import java.awt.Font;
import java.awt.SystemColor;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.general.DefaultPieDataset;

import exam.dto.StatisticsData;

/**
 * 生成统计图例
 * @author skywalker
 *
 */
public abstract class JFreechartUtil {

	/**
	 * 生成考试成绩图表
	 * 包括一个饼图、一个柱状图
	 * @param data 统计数据
	 * @param path 图片保存的路径，包含文件名
	 * @throws IOException 
	 */
	public static void generateChart(StatisticsData data, String path) throws IOException {
		DefaultPieDataset dataSet = new DefaultPieDataset();
		//设置数据源
		dataSet.setValue("<60%", data.getUnderSixty().size());
		dataSet.setValue("60%-80%", data.getSixtyAndEighty().size());
		dataSet.setValue("80%-90%", data.getEightyAndNinety().size());
		dataSet.setValue(">90%", data.getAboveNinety().size());
		JFreeChart pie = ChartFactory.createPieChart("各分数段学生人数统计", dataSet, true, true, false);
		Font font = new Font("微软雅黑", Font.PLAIN, 12);
		PiePlot plot = (PiePlot) pie.getPlot();
		//解决中文显示为口，仅适用于于饼图
		pie.setTitle(new TextTitle("各分数段学生人数统计", font));
		plot.setLabelFont(font);
		pie.getLegend().setItemFont(font);
		//没有数据时显示的内容
		plot.setNoDataMessage("没有数据");
		//显示百分比，{0} 表示选项， {1} 表示数值， {2} 表示所占比例 ,小数点后两位
		plot.setLabelGenerator(new StandardPieSectionLabelGenerator( "{0}={1}({2})", NumberFormat.getNumberInstance(),
			    new DecimalFormat("0.00%")));
		plot.setLegendLabelGenerator(new StandardPieSectionLabelGenerator("{0}={1}({2})"));
		//图片背景色设为白色
		plot.setBackgroundPaint(SystemColor.WHITE);
		//保存
		ChartUtilities.saveChartAsPNG(new File(path), pie, 500, 500);
	}
	
}
