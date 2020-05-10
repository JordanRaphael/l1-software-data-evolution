package tableClustering.clusterExtractor.commons;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import data.dataPPL.pplSQLSchema.PPLTable;

public class Cluster {

	private int birth;
	private String birthVersion;
	private int death;
	private String deathVersion;
	private int totalChanges = 0;
	private TreeMap<String, PPLTable> tables = null;

	public Cluster() {

		tables = new TreeMap<String, PPLTable>();
	}

	public Cluster(int birth, String birthVersion, int death, String deathVersion, int totalChanges) {

		this.birth = birth;
		this.birthVersion = birthVersion;
		this.death = death;
		this.deathVersion = deathVersion;
		this.totalChanges = totalChanges;
		tables = new TreeMap<String, PPLTable>();

	}

	public TreeMap<String, PPLTable> getTables() {
		return tables;
	}

	public ArrayList<String> getNamesOfTables() {
		ArrayList<String> tablesNames = new ArrayList<String>();
		for (Map.Entry<String, PPLTable> pplTb : tables.entrySet()) {
			tablesNames.add(pplTb.getKey());
		}
		return tablesNames;
	}

	public void addTable(PPLTable table) {
		tables.put(table.getName(), table);
	}

	public int getBirth() {
		return birth;
	}

	public int getDeath() {
		return death;
	}

	public String getBirthSqlFile() {
		return birthVersion;
	}

	public String getDeathSqlFile() {
		return deathVersion;
	}

	public int getTotalChanges() {
		return totalChanges;
	}

	public double distance(Cluster anotherCluster, Double birthWeight, Double deathWeight, Double changeWeight,
			int dbDuration) {

		double normalizedChangeDistance = Math.abs(
				(totalChanges - anotherCluster.totalChanges) / ((double) (totalChanges + anotherCluster.totalChanges)));
		double normalizedBirthDistance = Math.abs((birth - anotherCluster.birth) / (double) dbDuration);
		double normalizedDeathDistance = Math.abs((death - anotherCluster.death) / (double) dbDuration);
		double normalizedTotalDistance = changeWeight * normalizedChangeDistance + birthWeight * normalizedBirthDistance
				+ deathWeight * normalizedDeathDistance;

		return normalizedTotalDistance;
	}

	public Cluster mergeWithNextCluster(Cluster nextCluster) {

		Cluster newCluster = new Cluster();

		int minBirth;
		String minBirthVersion = "";
		if (birth <= nextCluster.birth) {
			minBirth = birth;
			minBirthVersion = birthVersion;
		} else {
			minBirth = nextCluster.birth;
			minBirthVersion = nextCluster.birthVersion;
		}

		int maxDeath;
		String maxDeathVersion = "";
		if (death >= nextCluster.death) {
			maxDeath = death;
			maxDeathVersion = deathVersion;
		} else {
			maxDeath = nextCluster.death;
			maxDeathVersion = nextCluster.deathVersion;

		}

		newCluster.birth = minBirth;
		newCluster.birthVersion = minBirthVersion;
		newCluster.death = maxDeath;
		newCluster.deathVersion = maxDeathVersion;

		newCluster.totalChanges = totalChanges + nextCluster.totalChanges;

		for (Map.Entry<String, PPLTable> tab : tables.entrySet()) {

			newCluster.addTable(tab.getValue());

		}

		for (Map.Entry<String, PPLTable> tabNext : nextCluster.getTables().entrySet()) {

			newCluster.addTable(tabNext.getValue());

		}

		return newCluster;
	}

	public String toString() {

		String toReturn = "Cluster";

		toReturn = toReturn + "\t" + birth + "\t" + death + "\t" + totalChanges + "\n";

		for (Map.Entry<String, PPLTable> t : tables.entrySet()) {
			toReturn = toReturn + t.getKey() + "\t" + t.getValue().getBirthVersionID() + "\t"
					+ t.getValue().getDeathVersionID() + "\t" + t.getValue().getTotalChanges() + "\n";
		}

		return toReturn;

	}
}
