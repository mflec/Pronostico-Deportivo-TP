package pronosticos_deportivos_tp_final.pronosticosDeportivosJava;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ConexionBD {
	private Connection con; 
	
	ConexionBD(String url, String user, String password) {
		try {
			// Se registra driver
			String DRIVER = "com.mysql.jdbc.Driver";
			Class.forName(DRIVER); 
					
			//Creando conexión a la base de datos
			this.con = DriverManager.getConnection(url, user, password);
		} catch (Exception e) {
			System.out.println(e);
		}
	}
	
	public ResultSet consultarBD(String sql) throws Exception {
		// Crea la sentencia
		Statement stmt = con.createStatement();
		return stmt.executeQuery(sql);
	}
	
	
	public int manipularBD(String sql) throws SQLException {
		Statement stmt = con.createStatement();
		return stmt.executeUpdate(sql);
	}
		
	public void close() throws SQLException {
		//Se cierra Conexión
		this.con.close();
	}
}
