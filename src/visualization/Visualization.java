package visualization;

import org.jfree.chart.ChartPanel;

import results.Results;

public interface Visualization {

	public void draw(Results res);
	public ChartPanel getChart();
	
}
