import java.io.*;
import java.sql.*;
import java.util.*;
import java.util.Date;


public class Database {
	static String userName = "root";
	static String password = "root";
	static String jdbcDriver = "com.mysql.jdbc.Driver";
	static String dbUrl = "jdbc:mysql://localhost:3306/";
	static String dbName = "ANTEIKU";
	static boolean dbExists = false;
	static Connection conn;
	static Date date = new Date();
	
	
	public static void main(String[] args) throws SQLException, ClassNotFoundException{
		PrintStream out;
		try {
			out = new PrintStream(new FileOutputStream(args[1],true));
			out.println();
			out.println("****\t<"+date+">\t****");
			out.println();
			System.out.println("****<"+date+">****\n"+"Program START:");
			//establish a connection to database
			getConnection();
			createAndUseDatabase();
			
			
			
			//create a table and populate with data
			createTable("COFFEES");
			//populateTable("COFFEES");
			if (args.length==0){
				System.out.println("\n**Invalid INPUT file. Cannot populate database.**\n");
			}
			else{	
				Scanner scn = null;
				try {
					String[] entry = new String[4];
					String line = null;
					scn = new Scanner(new FileReader(args[0]));
					
					System.out.println("Reading file "+args[0]+"...");
					
					
					while (scn.hasNextLine()){
						for(int i=0; i<4; ++i){
							entry[i]=scn.next();
						}
						insert("COFFEES",entry,out);
					}
						
					
				} catch (FileNotFoundException e) {
					System.out.println("\n**File ERROR**\n");
					e.printStackTrace();
				}
					
				
			}
			
			printTable("COFFEES");
			for (int i=1; i<=32 ; ++i){
				update("COFFEES", Integer.toString(i), "quantity", "999", out);
			}
			
			System.out.println();
			printTable("COFFEES");
			
			for (int i=33; i<=64 ; ++i){
				delete("COFFEES", Integer.toString(i), out);
			}
			
			
			System.out.println();
			printTable("COFFEES");
			
			
			GUI.createGUI();
			GUI.out=out;
			

			try {
				System.out.println("Press ENTER to end connection to database.");
				System.in.read();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			deleteDatabase();
			out.println();
			out.println("****\t<"+date+">\t****\tDatabase "+dbName+" deleted.");
			out.println();
			conn.close();
			System.out.println("\nGoodbye!");
			
		} catch (FileNotFoundException e1) {
			System.out.println("A log is required for any database transactions.");
			e1.printStackTrace();
		}
		
	}


	
	
	/**
	 * Inserts in table with a string[] entry and records in log.
	 * @param table
	 * @param entry
	 * @param out 
	 * @throws SQLException
	 */
	public static void insert(String table, String[] entry, PrintStream out) throws SQLException{
		PreparedStatement stmt = null;
		out.println("\t<"+date+">\t");
	    try {
	    	String statement = "insert into " + dbName + "." + table + " " + 
	    			"values(" +
		            entry[0] + ", " + 
		            "'" + entry[1] + "', " + 
		            entry[2] + ", " + 
		            entry[3] + 
		            ");";
	    	stmt = conn.prepareStatement(statement);
	    	out.println("\t+"+statement);
	        stmt.execute();
	    } catch (SQLException e) {
	    	System.out.println("\tCould not insert.");
	    	out.println("\tCould not insert.");
	    } finally {
	        if (stmt != null) {
	          stmt.close();
	        }
	    }
	}

	/**
	 * Updates a field in table and records in log.
	 * @param table
	 * @param cofId
	 * @param updatingField
	 * @param newValue
	 * @param out
	 * @throws SQLException
	 */
	public static void update(String table, String cofId, String updatingField, String newValue, PrintStream out) throws SQLException{
		PreparedStatement stmt = null;
		out.println("\t<"+date+">\t");
	    try {
	    	String statement = "update " + dbName + "." + table + " " + 
	    			"set " +
		            updatingField + " = " + 
		            newValue + " where " + 
		            "cof_id" + " = " + 
		            cofId + 
		            ";";
	    	stmt = conn.prepareStatement(statement);
	    	out.println("\t%"+statement);
	        stmt.execute();
	    } catch (SQLException e) {
	    	System.out.println("\tCould not update.");
	    	out.println("\tCould not update.");
	    } finally {
	        if (stmt != null) {
	          stmt.close();
	        }
	    }
	}
	
	/**
	 * Deletes an entry from table and records in log.
	 * @param table
	 * @param cofId
	 * @param out
	 * @throws SQLException
	 */
	public static void delete(String table, String cofId, PrintStream out) throws SQLException{
		PreparedStatement stmt = null;
		out.println("\n\t<"+date+">\t");
	    try {
	    	String statement = "delete from " + dbName + "." + table + " " + 
	    			"where cof_id = " + cofId + ";";
	    	stmt = conn.prepareStatement(statement);
	    	out.println("\t-"+statement);
	        stmt.execute();
	    } catch (SQLException e) {
	    	System.out.println("\tCould not Delete.");
	    	out.println("\tCould not Delete.");
	    } finally {
	        if (stmt != null) {
	          stmt.close();
	        }
	    }
	}




	/**
	 * Gets a connection. Returns a list of all databases on server.
	 * Also checks if our desired database is on the server. 
	 * (dbExists will be TRUE if found)
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
	public static void getConnection() throws SQLException, ClassNotFoundException {
		
	    Properties connectionProps = new Properties();
	    connectionProps.put("user", userName);
	    connectionProps.put("password", password);
	    Class.forName(jdbcDriver);
		conn = DriverManager.getConnection(dbUrl,connectionProps);
		System.out.println("\n\nConnected to Server: "+dbUrl);
		
		System.out.println("\nList of current existing databases on server: ");
		ResultSet resultSet = conn.getMetaData().getCatalogs();
		//iterate each catalog in the ResultSet
		while (resultSet.next()) {
		  // Get the database name, which is at position 1
		  String databaseName = resultSet.getString(1);
		  System.out.println("\t+ "+databaseName);
		  if (databaseName.equalsIgnoreCase(dbName))
			  dbExists = true;
		}
		resultSet.close();
		System.out.println("\nDatabase Exists: "+dbExists);
	
	}
	
	
	/**
	 * Creates a database if desired database does not exist.
	 * Use the database.
	 */
	public static void createAndUseDatabase(){
		if (!dbExists){
			System.out.println("Creating database "+dbName+"...");
			try{
	        PreparedStatement stmt;
	        stmt = conn.prepareStatement("CREATE DATABASE " + dbName);
	        stmt.execute();
	        }catch (SQLException se){
	            System.out.println("DATABASE "+dbName+" already exists.");
	        }catch (Exception e){
	        	e.printStackTrace();
	        }
		}
		try{
			Properties connectionProps = new Properties();
		    connectionProps.put("user", userName);
		    connectionProps.put("password", password);
		    Class.forName(jdbcDriver);
			conn = DriverManager.getConnection(dbUrl+dbName,connectionProps);
			System.out.println("\n\n\nConnected to Database: "+dbUrl+dbName);
			
	        PreparedStatement stmt;
	        stmt = conn.prepareStatement("USE " + dbName + ";");
	        stmt.execute();
	        }catch (SQLException se){
	            System.out.println("ERROR in DATABASE "+dbName);
	        }catch (Exception e){
	        	e.printStackTrace();
	        }
    }
	
	/**
	 * Checks to see if table exists. Then creates table.
	 * @param table
	 * @throws SQLException
	 */
	public static void createTable(String table) throws SQLException {
		
		boolean tableExists = false;
		System.out.println("\nList of existing tables on database: ");
		ResultSet resultSet = conn.getMetaData().getTables(null, null, "%", null);
		//iterate each table in the ResultSet
		String tableName;
		while (resultSet.next()) {
		  tableName = resultSet.getString(3);
		  System.out.println("\t+ "+tableName);
		  if (tableName.equalsIgnoreCase(table))
			  tableExists = true;
		}
		resultSet.close();
		System.out.println("\nTable Exists: "+tableExists);
		
		if (!tableExists){
			System.out.println("Creating table "+table+"...");
		    String createString =
		        "CREATE TABLE " + table + " (" +
		        "COF_ID integer NOT NULL, " +
		        "COF_NAME varchar(32) NOT NULL, " +
		        "PRICE float NOT NULL, " +
		        "QUANTITY integer NOT NULL, " +
		        "PRIMARY KEY (COF_ID))";
	
		    try {
		    	PreparedStatement stmt = conn.prepareStatement(createString);
		    	stmt.execute();
		    } catch (SQLException e) {
		    	System.out.println("Could not create table.");
		    }
		}
	}
	
	
	/**
	 * Prints table.
	 * @param string
	 * @throws SQLException
	 */
	public static void printTable(String table) throws SQLException {
		PreparedStatement stmt = conn.prepareStatement("select * from "+table+";");
		ResultSet rs = stmt.executeQuery();
		System.out.print("\n");
		while (rs.next()){
			System.out.println(rs.getString("cof_id")+
					"\t"+rs.getString("cof_name")+
					"\n\t$ "+rs.getString("price")+
					"\t\t\t\t\tQTY: "+rs.getString("quantity")+"x");
		}
	}



	/**
	 * Once finished, deletes the database.
	 * @throws SQLException
	 */
	public static void deleteDatabase() throws SQLException {
		System.out.println("\n\nDeleting database...");
		PreparedStatement stmt = conn.prepareStatement("drop database "+dbName+";");
		stmt.execute();
	}
	
	
	/**
	 * Sample populate method.
	 * @param table
	 * @throws SQLException
	 */
	public static void populateTable(String table) throws SQLException {
		System.out.println("\n\n\nPopulating the table "+table+"...");
		PreparedStatement stmt = null;
	    try {
	        stmt = conn.prepareStatement("insert into " + dbName +
		            "." + table + " " +
		            "values(999, 'Colombian', 7.99, 0)");
	        stmt.execute();

	        stmt.executeUpdate(
	            "insert into " + dbName +
	            "." + table + " " +
	            "values(998, 'French_Roast', 8.99, 0)");

	        stmt.executeUpdate(
	            "insert into " + dbName +
	            "." + table + " " +
	            "values(997, 'Espresso', 9.99, 0)");

	        stmt.executeUpdate(
	            "insert into " + dbName +
	            "." + table + " " +
	            "values(996, 'Colombian_Decaf', 8.99, 0)");

	        stmt.executeUpdate(
	            "insert into " + dbName +
	            "." + table + " " +
	            "values(995, 'French_Roast_Decaf', 9.99, 0)");
	    } catch (SQLException e) {
	    	System.out.println("Could not insert.");
	    } finally {
	        if (stmt != null) {
	          stmt.close();
	        }
	    }
	}
}
