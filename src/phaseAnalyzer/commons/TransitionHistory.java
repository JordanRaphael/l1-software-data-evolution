/**
 * This class represents the sequence of transitions that we receive as input.
 */
package phaseAnalyzer.commons;

import java.util.ArrayList;

public class TransitionHistory {

	private ArrayList<TransitionStats> values;
	private int totalUpdates;
	private double totalTime;

	public TransitionHistory() {
		values = new ArrayList<TransitionStats>();
	}

	public TransitionHistory(ArrayList<TransitionStats> values) {
		this.values = values;
	}

	public int getTotalUpdates() {
		return totalUpdates;
	}

	public void addValue(TransitionStats v) {
		values.add(v);
	}

	public void consoleVerticalReport() {
		for (TransitionStats v : values) {
			System.out.println(v.toStringShort());
		}
		System.out.println();
	}

	public ArrayList<TransitionStats> getValues() {
		return values;
	}

	public void setTotalUpdates(int totalUpdates) {
		this.totalUpdates = totalUpdates;
	}

	public void setTotalTime() {
		totalTime = (values.get(values.size() - 1).getTime() - values.get(0).getTime()) / 86400;

	}

	public double getTotalTime() {
		return totalTime;
	}

}
