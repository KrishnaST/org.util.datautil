/**
 * @author krishna.telgave
 */
module org.util.datautil {

	exports org.util.datautil;
	exports org.util.datautil.db;

	requires static transitive com.fasterxml.jackson.core;
	requires static transitive com.fasterxml.jackson.databind;
	requires static transitive java.sql;

}