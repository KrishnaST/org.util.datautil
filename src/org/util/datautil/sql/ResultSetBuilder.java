package org.util.datautil.sql;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

public class ResultSetBuilder {

	public static final ResultSet getResultSet(final PreparedStatement ps, final String...strings) throws SQLException {
		for (int i = 0; i < strings.length; i++) {
			ps.setString(i+1, strings[i]);
		}
		return ps.executeQuery();
	}
	
	public static final ResultSet getResultSet(final PreparedStatement ps, final String string) throws SQLException {
		ps.setString(1, string);
		return ps.executeQuery();
	}
	
	public static final ResultSet getResultSet(final PreparedStatement ps, final String s1, final String s2) throws SQLException {
		ps.setString(1, s1);
		ps.setString(2, s2);
		return ps.executeQuery();
	}
	
	public static final ResultSet getResultSet(final PreparedStatement ps, final long longer) throws SQLException {
		ps.setLong(1, longer);
		return ps.executeQuery();
	}
	
	public static final ResultSet getResultSet(final PreparedStatement ps, final int integer) throws SQLException {
		ps.setInt(1, integer);
		return ps.executeQuery();
	}
	
	public static final ResultSet getResultSet(final PreparedStatement ps, final byte[] bytes) throws SQLException {
		ps.setBytes(1, bytes);
		return ps.executeQuery();
	}
	
	public static final ResultSet getResultSet(final PreparedStatement ps, final String string, final byte[] bytes) throws SQLException {
		ps.setString(1, string);
		ps.setBytes(2, bytes);
		return ps.executeQuery();
	}
	
	
	public static final ResultSet getResultSet(final PreparedStatement ps, final Object...objects) throws SQLException {
		for (int i = 0; i < objects.length; i++) {
			if(objects[i] instanceof Integer) {
				ps.setInt(i+1, (int) objects[i]);
			}
			else if(objects[i] instanceof Long) {
				ps.setLong(i+1, (Long) objects[i]);
			}
			else if(objects[i] instanceof Byte) {
				ps.setByte(i+1, (Byte) objects[i]);
			}
			else if(objects[i] instanceof Short) {
				ps.setShort(i+1, (Short) objects[i]);
			}
			else if(objects[i] instanceof String) {
				ps.setString(i+1, (String) objects[i]);
			}
			else if(objects[i] instanceof BigDecimal) {
				ps.setBigDecimal(i+1, (BigDecimal) objects[i]);
			}
			else if(objects[i] instanceof Boolean) {
				ps.setBoolean(i+1, (Boolean) objects[i]);
			}
			else if(objects[i] instanceof byte[]) {
				ps.setBytes(i+1, (byte[]) objects[i]);
			}
			else if(objects[i] instanceof java.sql.Date) {
				ps.setDate(i+1, (java.sql.Date) objects[i]);
			}
			else if(objects[i] instanceof Double) {
				ps.setDouble(i+1, (Double) objects[i]);
			}
			else if(objects[i] instanceof Float) {
				ps.setFloat(i+1, (Float) objects[i]);
			}
			else if(objects[i] instanceof Date) {
				ps.setTimestamp(i+1, new Timestamp(((Date) objects[i]).getTime()));
			}
			else throw new SQLException("unknown data type for sql : "+objects[i].getClass().getName());
		}
		return ps.executeQuery();
	}
	
	public static void main(String[] args) throws SQLException {
		try {
			System.out.println(getResultSet(null, 10));
		} catch (Exception e) {e.printStackTrace();}
	}

}
