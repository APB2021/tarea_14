package menu;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.StringTokenizer;
import java.util.stream.IntStream;

import dao.AlumnoDao;
import dao.AlumnoDaoImpl;
import model.Alumno;

public class Menu {

	private KeyboardReader reader;
	private AlumnoDao dao;

	public Menu() {
		reader = new KeyboardReader();
		dao = AlumnoDaoImpl.getInstance();
	}

	public void init() {

		int opcion = -1;

		do {
			menu();
			opcion = reader.nextInt();

			switch (opcion) {
			case 1 -> listAll();
			case 2 -> System.out.println("2");
			case 3 -> insert();
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

	public void insert() {
		String mensaje = """
				INSERCIÓN DE UN NUEVO ALUMNO
				============================
				""";

		System.out.println(mensaje);

		String nombre = pedirDato("Nombre del alumno: ");
		String apellidos = pedirDato("Apellidos del alumno: ");
		char genero = pedirGenero("Género del alumno: ");
		LocalDate fechaNacimiento = pedirFechaNacimiento("Fecha de nacimiento en formato dd/mm/aaaa");
		String ciclo = pedirDato("Ciclo del alumno: ");
		String curso = pedirDato("Curso del alumno: ");

		try {
			dao.add(new Alumno(nombre, apellidos, genero, fechaNacimiento, ciclo, curso));
			System.out.println("Nuevo alumno registrado correctamente.");
		} catch (SQLException e) {
			System.err.println(
					"Error insertando el nuevo registro en la base de datos. Vuelva a intentarlo de nuevo o póngase en contacto con el administrador.");
			e.printStackTrace();
		}

	}

	private void printCabeceraTablaAlumnos() {
		System.out.printf("%3s %15s %25s %1s %8s %10s %10s %10s", "NIA", "NOMBRE", "APELLIDOS", "GÉNERO",
				"FECHA DE NACIMIENTO", "CICLO", "CURSO", "GRUPO");
		System.out.println();
		IntStream.range(1, 110).forEach(x -> System.out.print("-"));
		System.out.println();
	}

	private void printAlumno(Alumno alumno) {
		System.out.printf("%3s %15s %25s %1s %9s %10s %10s %10s \n", alumno.getNia(), alumno.getNombre(),
				alumno.getApellidos(), alumno.getGenero(),
				alumno.getFechaNacimiento().format(DateTimeFormatter.ofPattern("dd/MM/yy")), alumno.getCiclo(),
				alumno.getCurso(), alumno.getGrupo());
	}

	public void listAll() {
		String mensaje = """
				LISTADO DE TODOS LOS ALUMNOS
				============================
				""";
		System.out.println(mensaje);

		try {
			List<Alumno> result = dao.getAll();

			if (result.isEmpty()) {
				System.out.println("No hay alumnos registrados en la base de datos.");
			} else {
				printCabeceraTablaAlumnos();
				result.forEach(this::printAlumno);
			}
		} catch (SQLException e) {
			System.err.println(
					"Error conusltando en la base de datos. Vuelva a intentarlo de nuevo o póngase en contacto con el administrador.");
			e.printStackTrace();
		}

		System.out.println();
	}

	private String pedirDato(String mensaje) {
		System.out.println(mensaje);
		String respuesta = reader.nextLine();
		return respuesta;
	}

	private char pedirGenero(String mensaje) {
		System.out.println(mensaje);
		char respuesta = reader.nextLine().charAt(0);
		return respuesta;
	}

	private LocalDate pedirFechaNacimiento(String mensaje) {
		System.out.println(mensaje);
		LocalDate respuesta = reader.nextLocalDate();
		return respuesta;
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
