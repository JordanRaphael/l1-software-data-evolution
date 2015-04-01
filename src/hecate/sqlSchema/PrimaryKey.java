package hecate.sqlSchema;



import java.util.Map;
import java.util.TreeMap;

public class PrimaryKey implements SqlItem{
	private String name;
	protected TreeMap<String, Attribute> key;
	public PrimaryKey() {
		this.name = null;
		this.key = new TreeMap<String, Attribute>();
	}
	
	public PrimaryKey(String name, TreeMap<String, Attribute> k) {
		this.key = k;
		this.name = name;
	}
	
	public void  add(Attribute attr) {
		key.put(attr.getName(), attr);
	}
	
	
	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public String print() {
		String buff = new String();
		buff = "Primary Key: ";
		for (Map.Entry<String, Attribute> entry : this.key.entrySet()) {
			Attribute a = entry.getValue();
			buff += a.toString() + " ";
		}
		buff += "\n";
		return buff;
	}

	
	
	public boolean isEmpty() {
		return key.isEmpty();
	}
	
	public String toString() {
		return this.name;
	}


	@Override
	public void setMode(int mode) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getMode() {
		// TODO Auto-generated method stub
		return 0;
	}
}
