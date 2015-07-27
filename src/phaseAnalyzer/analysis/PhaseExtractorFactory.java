/**
 * 
 */
package phaseAnalyzer.analysis;

/**
 * @author pvassil
 *
 */
public class PhaseExtractorFactory {

	public IPhaseExtractor createPhaseExtractor(String concreteClassName){
		if (concreteClassName.equals("BottomUpPhaseExtractor")){
			return new BottomUpPhaseExtractor();
		}
/*
 * 		else if (concreteClassName.equals("ANOTHERNAME")){
			return new ANOTHERNAME();
		}
		else ... and so on 
 */
		System.out.println("If the code got up to here, you passed a wrong argument to the PhaseExtractorFactory");
		return null;
	}
	
	
}
