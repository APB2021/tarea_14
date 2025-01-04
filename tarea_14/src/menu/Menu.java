package menu;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.StringTokenizer;

public class Menu {

	private KeyboardReader reader;

	public Menu() {
		reader = new KeyboardReader();
	}

	public void init() {

		int opcion = -1;

		do {
			menu();
			opcion = reader.nextInt();

			switch (opcion) {
			case 1 -> System.out.println("1");
			case 2 -> System.out.println("2");
			case 3 -> System.out.println("3");
			case 4 -> System.out.println("4");
			case 5 -> System.out.println("5");
			case 0 -> System.out.println("Saliendo del programa...");
			default -> System.err.println("El número introducido no se corresponde con una operación válida.");
			}
		} while (opcion != 0);

	}

	public void menu() {

		String textoMenu = """
				SISTEMA DE GESTIÓN DE ALUMNOS
				=============================
				-> Introduzca una opción de entre las siguientes:
				0: Salir.
				1: Listar todos los alumnos.
				2: Listar un alumno por su nia.
				3: Insertar un nuevo alumno.
				4: Actualizar un alumno.
				5: Eliminar un alumno.
				=============================
				Opcion:
				""";

		System.out.print(textoMenu);
	}

	static class KeyboardReader {

		BufferedReader br;
		StringTokenizer st;
		private String PATTERN = "dd/MM/yyyy";

		public KeyboardReader() {
			br = new BufferedReader(new InputStreamReader(System.in));
		}

		String next() {
			while (st == null || !st.hasMoreElements()) {
				try {
					st = new StringTokenizer(br.readLine());

				} catch (IOException ex) {
					System.err.print("Error leyendo del teclado.");
					ex.printStackTrace();
				}
			}

			return st.nextToken();
		}

		int nextInt() {
			return Integer.parseInt(next());
		}

		double nextDouble() {
			return Double.parseDouble(next());
		}

		LocalDate nextLocalDate() {
			return LocalDate.parse(next(), DateTimeFormatter.ofPattern(PATTERN));
		}

		String nextLine() {
			String str = "";
			try {
				if (st.hasMoreElements()) {
					str = st.nextToken("\n");
				} else {
					str = br.readLine();
				}

			} catch (IOException ex) {
				System.err.print("Error leyendo del teclado.");
				ex.printStackTrace();

			}

			return str;
		}

	}

}
