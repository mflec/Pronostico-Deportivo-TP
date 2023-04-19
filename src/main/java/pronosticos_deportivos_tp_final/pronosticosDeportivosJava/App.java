package pronosticos_deportivos_tp_final.pronosticosDeportivosJava;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;
import java.util.HashSet;
import java.util.Set;

public class App {
	public static Set<Equipo> equipos = new HashSet<>();
	public static Set<Persona> personas = new HashSet<>();
	public static Set<Ronda> rondas = new HashSet<>();

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);

		System.out.println("Ingrese ruta del archivo CSV con resultados:");
		String csvResultados = scanner.nextLine();// "C:\\Users\\MILI\\Documents\\resultadosFull.csv";

		System.out.println("Ingrese ruta del archivo CSV con los datos de los pronosticos:");
		String csvPronostico = scanner.nextLine(); // "C:\\Users\\MILI\\Documents\\pronosticoFull.csv";

		System.out.println("Ingrese el número de ronda al que corresponde el pronostico:");
		String nroRonda = scanner.nextLine();

		leerResultados(csvResultados);

		Ronda pronosticoNroRonda = new Ronda(nroRonda);

		if (rondas.contains(pronosticoNroRonda)) {
			Ronda ronda = getRonda(nroRonda);

			leerPronostico(csvPronostico, ronda);

			for (Persona persona : personas) {
				persona.getPronosticos();
			}
		}
	}

	public static void leerResultados(String csvResultados) {
		String renglonFila;

		try (BufferedReader br = new BufferedReader(new FileReader(csvResultados))) {
			// Lee la primera línea y la descarta
			br.readLine();

			while ((renglonFila = br.readLine()) != null) {
				String[] filaArray = renglonFila.split(",");

				String nombreDeRonda = filaArray[0];

				Ronda ronda = getRonda(nombreDeRonda);

				String nombreEquipo1 = filaArray[1];
				String nombreEquipo2 = filaArray[4];

				Equipo equipo1 = getEquipo(nombreEquipo1);
				Equipo equipo2 = getEquipo(nombreEquipo2);

				int cantGolesEquipo1 = Integer.parseInt(filaArray[2]);
				int cantGolesEquipo2 = Integer.parseInt(filaArray[3]);

				Partido partido = new Partido(equipo1, cantGolesEquipo1, equipo2, cantGolesEquipo2);

				ronda.setPartido(partido);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void leerPronostico(String csvPronostico, Ronda ronda) {
		String renglonFila;

		try (BufferedReader br = new BufferedReader(new FileReader(csvPronostico))) {
			// Lee la primera línea y la descarta
			br.readLine();

			while ((renglonFila = br.readLine()) != null) {
				String[] filaArray = renglonFila.split(",");

				String nombrePersona = filaArray[0];

				Persona personaQuePronosticaPersona = getPersona(nombrePersona);

				Equipo equipo1 = getEquipo(filaArray[1]);
				Equipo equipo2 = getEquipo(filaArray[5]);

				Partido partido = ronda.getPartido(equipo1, equipo2);

				boolean pronosticoEmpate = filaArray[3].equals("X") || filaArray[3].equals("x");
				Equipo pronosticoGanador = filaArray[2].equals("X") || filaArray[2].equals("x") ? equipo1 : equipo2;

				Pronostico pronostico = pronosticoEmpate ? new Pronostico(partido)
						: new Pronostico(partido, pronosticoGanador);

				personaQuePronosticaPersona.setPronostico(pronostico);

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
