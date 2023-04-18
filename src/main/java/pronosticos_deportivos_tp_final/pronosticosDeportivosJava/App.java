package pronosticos_deportivos_tp_final.pronosticosDeportivosJava;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collection;
import java.util.ArrayList;
import java.util.Scanner;

public class App {
	public static Collection<Equipo> equipos = new ArrayList<>();
	public static int puntosTotales = 0;

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);

		System.out.print("Ingrese ruta del archivo CSV con resultados:");
		String csvResultados = scanner.nextLine(); // "C:\\Users\\MILI\\Documents\\resultados.csv";

		System.out.print("Ingrese ruta del archivo CSV con resultados:");
		String csvPronostico = scanner.nextLine(); // "C:\\Users\\MILI\\Documents\\pronostico.csv";

		Ronda ronda = new Ronda("1");

		leerResultados(csvResultados, ronda);
		leerPronostico(csvPronostico, ronda);

		System.out.print("Puntos totales: " + puntosTotales);
	}

	public static Equipo buscarEquipo(String nombreEquipo, Collection<Equipo> equipos) {
		for (Equipo equipo : equipos) {
			if (equipo.getNombre().equals(nombreEquipo)) {
				return equipo;
			}
		}
		return null;
	}

	public static void leerResultados(String csvResultados, Ronda ronda) {
		String renglonFila;

		try (BufferedReader br = new BufferedReader(new FileReader(csvResultados))) {
			// Lee la primera línea y la descarta
			br.readLine();

			while ((renglonFila = br.readLine()) != null) {
				String[] filaArray = renglonFila.split(",");

				Equipo equipo1 = new Equipo(filaArray[0]);
				equipos.add(equipo1);

				Equipo equipo2 = new Equipo(filaArray[3]);
				equipos.add(equipo2);

				int cantGolesEquipo1 = Integer.parseInt(filaArray[1]);
				int cantGolesEquipo2 = Integer.parseInt(filaArray[2]);

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

				Equipo equipo1 = buscarEquipo(filaArray[0], equipos);
				Equipo equipo2 = buscarEquipo(filaArray[4], equipos);

				Partido partido = ronda.getPartido(equipo1, equipo2);

				boolean pronosticoEmpate = filaArray[2].equals("X");
				Equipo pronosticoGanador = filaArray[1].equals("X") ? equipo1 : equipo2;

				Pronostico pronostico = pronosticoEmpate ? new Pronostico(partido)
						: new Pronostico(partido, pronosticoGanador);
				puntosTotales = puntosTotales + pronostico.puntos();

			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
