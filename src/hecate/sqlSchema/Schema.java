package hecate.sqlSchema;

import java.util.Map;
import java.util.TreeMap;

public class Schema implements SqlItem{

	private String name;
	private TreeMap<String, Table> tables;

	public Schema(TreeMap<String, Table> t) {
		this.tables = t;
	}	
	
	public Schema() {
		this.tables = new TreeMap<String, Table>();
	}
	
	
	public Schema(String name) {
		this.tables = new TreeMap<String, Table>();
		this.name = name;
	}

	public TreeMap<String, Table> getTables() {
		return this.tables;
	}

	public void addTable(Table table) {
		this.tables.put(table.getName(), table);
	}
	
	public String toString() {
		return name;
	}

	public int[] getSize() {
		int attr = 0;
		for (Table t : this.tables.values()) {
			attr += t.getSize();
		}
		int[] res = {this.tables.size(), attr};
		return res;
	}
	
	public String print() {
		String buff = new String();
		buff = "Shema: \n\n";
		for (Map.Entry<String, Table> entry : this.tables.entrySet()) {
			
			Table a=entry.getValue();
			buff += "  " + a.print() + "\n";
		}
		return buff;
	}

	public void setTitle(String title) {
		this.name = title;
	}

	public Table getTableAt(int i) {
		int c = 0;
		if (i >= 0 && i < tables.size()){
			for (Map.Entry<String, Table> t : tables.entrySet()) {
				if (c == i) {
					return t.getValue();
				}
				c++;
			}
		}
		return null;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return name;
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