/**
 * 
 */
package parser;

/**
 * @author pvassil
 *
 */
public class ParserFactory {

	public IParser createParser(String concreteClassName){
		if (concreteClassName.equals("SimpleTextParser")){
			return new SimpleTextParser();
		}
/*
 * 		else if (concreteClassName.equals("ANOTHERNAME")){
			return new ANOTHERNAME();
		}
		else ... and so on 
 */
		System.out.println("If the code got up to here, you passed a wrong argument to the ParserFactory");
		return null;
	}
	
}
