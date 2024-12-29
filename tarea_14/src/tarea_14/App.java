package tarea_14;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;

import pool.MyDataSource;

public class App {

	public static void main(String[] args) {

		try (Connection conn = MyDataSource.getConnection()) {
			DatabaseMetaData metaData = conn.getMetaData();
			String[] types = { "TABLE" };
			ResultSet tables = metaData.getTables(null, null, "%", types);
			while (tables.next()) {
				System.out.println(tables.getString("TABLE_NAME"));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

}
