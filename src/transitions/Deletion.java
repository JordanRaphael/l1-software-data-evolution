package transitions;

/**
 * 
 */


import javax.xml.bind.annotation.XmlRootElement;

import sqlSchema.Table;

/**
 * @author iskoulis
 *
 */
@XmlRootElement
public class Deletion extends Transition {

	public Deletion() {
		super();
	}
	
	public void table(Table newTable) {
		super.table(newTable);
		this.type = "DeleteTable";
	}
}
