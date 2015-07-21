package analysis;

import commons.PhaseCollector;
import commons.TransitionHistory;

public interface IPhaseExtractor {

	public PhaseCollector extractAtMostKPhases(TransitionHistory transitionHistory, int numPhases,
									float timeWeight,float changeWeight,boolean preProcessingTime,boolean preProcessingChange );
}
