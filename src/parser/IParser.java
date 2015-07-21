package parser;

import commons.TransitionHistory;

public interface IParser {
	public abstract TransitionHistory parse(String fileName, String delimeter);
}
