package ee.pri.bcup.server.dao;

import java.util.HashSet;
import java.util.Set;

/**
 * Helper class to query more than one database on single MySql server over
 * single connection.
 */
public class DatabaseEnv {
	private static final Set<String> VALID_KEYS;
	
	static {
		VALID_KEYS = new HashSet<String>();
		VALID_KEYS.add("ee");
		VALID_KEYS.add("fi");
		VALID_KEYS.add("en");
		VALID_KEYS.add("alpha");
		VALID_KEYS.add("raivo");
	}
	
	private String databaseName;
	private String tablePrefix;
	
	public DatabaseEnv() {
		this.tablePrefix = "";
	}

	public String getDatabaseName() {
		return databaseName;
	}

	public void setDatabaseName(String databaseName) {
		this.databaseName = databaseName;
	}

	public String getTablePrefix() {
		return tablePrefix;
	}

	public void setTablePrefix(String tablePrefix) {
		this.tablePrefix = tablePrefix;
	}
	
	public static boolean isValidKey(String databaseEnvKey) {
		return VALID_KEYS.contains(databaseEnvKey);
	}

}
