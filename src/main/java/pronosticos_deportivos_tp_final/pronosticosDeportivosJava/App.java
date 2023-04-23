package pronosticos_deportivos_tp_final.pronosticosDeportivosJava;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;
import java.util.HashSet;
import java.util.Set;
import java.sql.*;

public class App {
	public static Set<Equipo> equipos = new HashSet<>();
	public static Set<Persona> personas = new HashSet<>();
	public static Set<Ronda> rondas = new HashSet<>();
	public static Connection con;
	public static ConexionBD conexionBD;

	public static void main(String[] args) {
		try {
			String rutaArchivoConfiguracion = args[0];
		    
			// Lee el archivo de configuración
			BufferedReader br = new BufferedReader(new FileReader(rutaArchivoConfiguracion));
			
			String url = br.readLine();
			String user = br.readLine();
			String password = br.readLine();
			int puntos = (br.readLine() == null || br.readLine().isEmpty()) ? 1 : Integer.parseInt(br.readLine());			
			int puntosExtra = (br.readLine() == null || br.readLine().isEmpty()) ? 2 : Integer.parseInt(br.readLine());
			
			br.close();

			conexionBD = new ConexionBD(url, user, password);

			Scanner scanner = new Scanner(System.in);

			System.out.println("¿Leer resultados desde la BD? (Y/n)");
			boolean leerResultadosBD = scanner.nextLine().toLowerCase().equals("y");

			System.out.println("¿Leer pronosticos desde la BD? (Y/n)");
			boolean leerPronosticosBD = scanner.nextLine().toLowerCase().equals("y");

			if (leerResultadosBD) {
				String peticion = "SELECT p.id_partido, e1.nombre AS equipoA, e2.nombre AS equipoB, p.golesA, p.golesB, p.id_ronda "
						+ "FROM partido p " + "INNER JOIN equipo e1 ON p.equipoA = e1.codigo_iso "
						+ "INNER JOIN equipo e2 ON p.equipoB = e2.codigo_iso";

				ResultSet resultados = conexionBD.consultarBD(peticion);
				leerResultadosBD(resultados);
				
			} else {				
				String rutaArchivoResultados =  args[1];
				leerResultados(rutaArchivoResultados);
			}

			if (leerPronosticosBD) {
				ResultSet pronosticos = conexionBD
						.consultarBD("SELECT p.id, p.id_partido, e.nombre AS equipo, p.resultado, p.usuario "
								+ "FROM pronostico p " + "INNER JOIN equipo e ON p.equipo = e.codigo_iso");
				leerPronosticoBD(pronosticos);
			} else {
				
				System.out.println("Ingrese el número de ronda al que corresponde el pronostico:");
				String nroRonda = scanner.nextLine();
				
			    String rutaArchivoPronosticos = args[2];
				leerPronostico(rutaArchivoPronosticos, nroRonda);
			}

			for (Persona persona : personas) {
				persona.getPronosticos(puntos, puntosExtra);
			}

			scanner.close();
			conexionBD.close();
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	// Métodos que insertan los datos de resultado y los de prónostico de manera
	// individual
	public static void leerResultado(String nombreDeRonda, String nombreEquipo1, int cantGolesEquipo1,
			int cantGolesEquipo2, String nombreEquipo2) {
		Ronda ronda = getRonda(nombreDeRonda);

		Equipo equipo1 = getEquipo(nombreEquipo1);
		Equipo equipo2 = getEquipo(nombreEquipo2);

		Partido partido = new Partido(equipo1, cantGolesEquipo1, equipo2, cantGolesEquipo2);

		ronda.setPartido(partido);
	}

	public static void leerPronostico(String nombrePersona, String nroRonda, Equipo equipo1, Equipo equipo2,
			Boolean pronosticoEmpate, Equipo pronosticoGanador) {
		Persona personaQuePronosticaPersona = getPersona(nombrePersona);

		Ronda ronda = getRonda(nroRonda);

		Partido partido = ronda.getPartido(equipo1, equipo2);

		Pronostico pronostico = pronosticoEmpate ? new Pronostico(partido) : new Pronostico(partido, pronosticoGanador);

		personaQuePronosticaPersona.setPronostico(pronostico);
	}

	// Métodos que recorren los datos de resultados y de pronosticos traido de la BD

	public static void leerResultadosBD(ResultSet rs) throws SQLException {
		while (rs.next()) {
			String nroRonda = rs.getString("id_ronda");

			String nombreEquipo1 = rs.getString("equipoA");
			String nombreEquipo2 = rs.getString("equipoB");
			int cantGolesEquipo1 = rs.getInt("golesA");
			int cantGolesEquipo2 = rs.getInt("golesB");

			leerResultado(nroRonda, nombreEquipo1, cantGolesEquipo1, cantGolesEquipo2, nombreEquipo2);
		}
	}

	public static void leerPronosticoBD(ResultSet pronosticos) throws Exception {
		while (pronosticos.next()) {
			String id_partido = pronosticos.getString("id_partido");
			String nombrePersona = pronosticos.getString("usuario");
			String resultado = pronosticos.getString("resultado");
			String equipoElegidoNombre = pronosticos.getString("equipo");

			String consulta = "SELECT e1.nombre AS equipoA_nombre, e2.nombre AS equipoB_nombre, p.id_ronda "
					+ "FROM partido p " + "JOIN equipo e1 ON p.equipoA = e1.codigo_iso "
					+ "JOIN equipo e2 ON p.equipoB = e2.codigo_iso " + "WHERE p.id_partido = " + id_partido;

			ResultSet rs = conexionBD.consultarBD(consulta);

			String nombreEquipo1 = "";
			String nombreEquipo2 = "";
			String nroRonda = "";

			if (rs.next()) {
				nombreEquipo1 = rs.getString("equipoA_nombre");
				nombreEquipo2 = rs.getString("equipoB_nombre");
				nroRonda = rs.getString("id_ronda");
			}

			Equipo equipo1 = getEquipo(nombreEquipo1);
			Equipo equipo2 = getEquipo(nombreEquipo2);

			boolean pronosticoEmpate = resultado.equals("E");
			Equipo pronosticoGanador = nombreEquipo1.equals(equipoElegidoNombre) ? equipo1 : equipo2;

			leerPronostico(nombrePersona, nroRonda, equipo1, equipo2, pronosticoEmpate, pronosticoGanador);
		}
	}

	// Métodos que leen los CSV con formato indicado en las indicaciones del TP

	public static void leerResultados(String csvResultados) throws SQLException {
		String renglonFila;

		try (BufferedReader br = new BufferedReader(new FileReader(csvResultados))) {
			// Lee la primera línea y la descarta
			br.readLine();

			while ((renglonFila = br.readLine()) != null) {
				String[] filaArray = renglonFila.split(",");
				String nroRonda = filaArray[0];

				String nombreEquipo1 = filaArray[1];
				String nombreEquipo2 = filaArray[4];

				int cantGolesEquipo1 = Integer.parseInt(filaArray[2]);
				int cantGolesEquipo2 = Integer.parseInt(filaArray[3]);

				leerResultado(nroRonda, nombreEquipo1, cantGolesEquipo1, cantGolesEquipo2, nombreEquipo2);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void leerPronostico(String csvPronostico, String nroRonda) {
		String renglonFila;

		try (BufferedReader br = new BufferedReader(new FileReader(csvPronostico))) {
			// Lee la primera línea y la descarta
			br.readLine();

			while ((renglonFila = br.readLine()) != null) {
				String[] filaArray = renglonFila.split(",");

				String nombrePersona = filaArray[0];

				Equipo equipo1 = getEquipo(filaArray[1]);
				Equipo equipo2 = getEquipo(filaArray[5]);

				boolean pronosticoEmpate = filaArray[3].equals("X") || filaArray[3].equals("x");
				Equipo pronosticoGanador = filaArray[2].equals("X") || filaArray[2].equals("x") ? equipo1 : equipo2;

				leerPronostico(nombrePersona, nroRonda, equipo1, equipo2, pronosticoEmpate, pronosticoGanador);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static Equipo getEquipo(String nombreEquipo) {
		for (Equipo equipo : equipos) {
			if (equipo.getNombre().equals(nombreEquipo)) {
				return equipo;
			}
		}
		Equipo nuevoEquipo = new Equipo(nombreEquipo);
		equipos.add(nuevoEquipo);
		return nuevoEquipo;
	}

	public static Persona getPersona(String nombre) {
		for (Persona persona : personas) {
			if (persona.getNombre().equals(nombre)) {
				return persona;
			}
		}
		Persona nuevaPersona = new Persona(nombre);
		personas.add(nuevaPersona);
		return nuevaPersona;
	}

	public static Ronda getRonda(String nro) {
		if (!rondas.contains(new Ronda(nro))) {
			Ronda ronda = new Ronda(nro);
			rondas.add(ronda);
			return ronda;
		} else {
			for (Ronda ronda : rondas) {
				if (ronda.getNumber().equals(nro)) {
					return ronda;
				}
			}
			return null;
		}
	}
}
