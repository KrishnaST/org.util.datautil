package org.util.datautil.sql;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

public final class PseudoClosable {

	private static final AutoCloseable CLOSEABLE = new AutoCloseable() {
		@Override
		public void close() throws IOException {}
	};
	
	
	public static final AutoCloseable getClosable(final PreparedStatement ps, final String...strings) throws SQLException {
		for (int i = 0; i < strings.length; i++) {
			ps.setString(i+1, strings[i]);
		}
		return CLOSEABLE;
	}
	
	public static final AutoCloseable getClosable(final PreparedStatement ps, final String string) throws SQLException {
		ps.setString(1, string);
		return CLOSEABLE;
	}
	
	public static final AutoCloseable getClosable(final PreparedStatement ps, final long longer) throws SQLException {
		ps.setLong(1, longer);
		return CLOSEABLE;
	}
	
	public static final AutoCloseable getClosable(final PreparedStatement ps, final int integer) throws SQLException {
		ps.setInt(1, integer);
		return CLOSEABLE;
	}
	
	public static final AutoCloseable getClosable(final PreparedStatement ps, final Object...objects) throws SQLException {
		for (int i = 0; i < objects.length; i++) {
			
			if(objects[i] == null) {
				ps.setString(i+1, null);
			}
			else if(objects[i] instanceof Integer) {
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
		return CLOSEABLE;
	}
	
	public static void main(String[] args) throws SQLException {
		try {
			System.out.println(getClosable(null, 10));
		} catch (Exception e) {e.printStackTrace();}
	}
}
