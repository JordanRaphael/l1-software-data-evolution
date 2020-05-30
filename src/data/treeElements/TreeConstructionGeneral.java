package data.treeElements;

import java.util.Map;
import java.util.TreeMap;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;

import data.dataKeeper.GlobalDataManager;
import data.dataPPL.pplSQLSchema.PPLSchema;
import data.dataPPL.pplSQLSchema.PPLTable;

public class TreeConstructionGeneral {

	private GlobalDataManager dataKeeper = null;

	public TreeConstructionGeneral(GlobalDataManager dataKeeper) {
		this.dataKeeper = dataKeeper;
	}

	public JTree constructTree() {

		DefaultMutableTreeNode top = new DefaultMutableTreeNode("Versions");
		TreeMap<String, PPLSchema> schemas = dataKeeper.getAllPPLSchemas();

		for (Map.Entry<String, PPLSchema> pplSc : schemas.entrySet()) {

			DefaultMutableTreeNode a = new DefaultMutableTreeNode(pplSc.getKey());
			top.add(a);
			TreeMap<String, PPLTable> tables = pplSc.getValue().getTables();

			for (Map.Entry<String, PPLTable> pplT : tables.entrySet()) {
				DefaultMutableTreeNode a1 = new DefaultMutableTreeNode(pplT.getKey());
				a.add(a1);
			}

		}

		JTree treeToConstruct = new JTree(top);

		return treeToConstruct;

	}
}
