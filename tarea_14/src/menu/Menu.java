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
			case 2 -> listByNia();
			case 3 -> insert();
			case 4 -> update();
			case 5 -> delete();
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
		} catch (SQLException ex) {
			System.err.println(
					"Error insertando el nuevo registro en la base de datos. Vuelva a intentarlo de nuevo o póngase en contacto con el administrador.");
			ex.printStackTrace();
		}

	}

	private void update() {

		String mensaje = """
				ACTUALIZACIÓN DE UN ALUMNO
				==========================
				""";

		System.out.println(mensaje);

		try {

			int nia = pedirNia("Introduzca el nia del alumno a buscar: ");

			Alumno alumno = dao.getByNia(nia);

			if (alumno == null) {
				System.out.println("No hay alumnos registrados en la base de datos con ese nia.");
			} else {
				printCabeceraTablaAlumnos();
				printAlumno(alumno);

				System.out.println();

				String nombre = pedirDato("Nombre del alumno: ");
				nombre = (nombre.isBlank()) ? alumno.getNombre() : nombre;

				String apellidos = pedirDato("Apellidos del alumno: ");
				apellidos = (apellidos.isBlank()) ? alumno.getApellidos() : apellidos;

				char genero = pedirGenero("Género del alumno: ");
				genero = (Character.toString(genero).isBlank()) ? alumno.getGenero() : genero;

				LocalDate fechaNacimiento = pedirFechaNacimiento("Fecha de nacimiento en formato dd/mm/aaaa");
				fechaNacimiento = (fechaNacimiento.toString().isBlank()) ? alumno.getFechaNacimiento()
						: fechaNacimiento;

				String ciclo = pedirDato("Ciclo del alumno: ");
				ciclo = (ciclo.isBlank()) ? alumno.getCiclo() : ciclo;

				String curso = pedirDato("Curso del alumno: ");
				curso = (curso.isBlank()) ? alumno.getCurso() : curso;

				alumno.setNombre(nombre);
				alumno.setApellidos(apellidos);
				alumno.setGenero(genero);
				alumno.setFechaNacimiento(fechaNacimiento);
				alumno.setCiclo(ciclo);
				alumno.setCurso(curso);

				dao.update(alumno);

				System.out.println();

				System.out.println("Alumno con nia " + alumno.getNia() + "actualizado.");

				System.out.println();
			}

		} catch (SQLException ex) {
			System.err.println(
					"Error insertando el nuevo registro en la base de datos. Vuelva a intentarlo de nuevo o póngase en contacto con el administrador.");
			ex.printStackTrace();
		}

	}

	private void printCabeceraTablaAlumnos() {
		//System.out.printf("%3s %15s %25s %1s %8s %10s %10s %10s", "NIA", "NOMBRE", "APELLIDOS", "GÉNERO",
		System.out.printf("%-10s %-15s %-20s %-6s %-15s %-21s %-10s %-10s%n", "NIA", "NOMBRE", "APELLIDOS", "GÉNERO",
				"NACIMIENTO", "CICLO", "CURSO", "GRUPO");
		System.out.println();
		IntStream.range(1, 110).forEach(x -> System.out.print("-"));
		System.out.println();
	}

	private void printAlumno(Alumno alumno) {
		//System.out.printf("%3s %15s %25s %1s %9s %10s %10s %10s \n", alumno.getNia(), alumno.getNombre(),
		System.out.printf("%-10s %-15s %-20s %-6s %-15s %-21s %-10s %-10s%n", alumno.getNia(), alumno.getNombre(),
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
		} catch (SQLException ex) {
			System.err.println(
					"Error conusltando en la base de datos. Vuelva a intentarlo de nuevo o póngase en contacto con el administrador.");
			ex.printStackTrace();
		}

		System.out.println();
	}

	public void listByNia() {
		String mensaje = """
				BÚSQUEDA DE ALUMNOS POR NIA
				===========================
				""";
		System.out.println(mensaje);

		try {
			int nia = pedirNia("Introduzca el nia del alumno a buscar: ");

			Alumno alumno = dao.getByNia(nia);

			if (alumno == null) {
				System.out.println("No hay alumnos registrados en la base de datos con ese nia.");
			} else {
				printCabeceraTablaAlumnos();
				printAlumno(alumno);
			}

			System.out.println();

		} catch (SQLException ex) {
			System.err.println(
					"Error consultando en la base de datos. Vuelva a intentarlo de nuevo o póngase en contacto con el administrador.");
			ex.printStackTrace();
		}
	}

	public void delete() {
		String mensaje = """
				BORRADO DE UN ALUMNO
				====================
				""";
		System.out.println(mensaje);

		try {

			int nia = pedirNia("Introduzca el nia del alumno a borrar: ");

			System.out.printf("Está usted seguro de querer borrar al alumno con nia %s? (s/n)", nia);

			String borrar = reader.nextLine();

			if (borrar.equalsIgnoreCase("s")) {
				dao.delete(nia);
				System.out.printf("El alumno con nia %s ha sido eliminado.", nia);
			}

		} catch (SQLException ex) {
			System.err.println(
					"Error consultando en la base de datos. Vuelva a intentarlo de nuevo o póngase en contacto con el administrador.");
			ex.printStackTrace();
		}

		System.out.println();
	}

	private int pedirNia(String mensaje) {
		System.out.print(mensaje);
		int respuesta = reader.nextInt();
		return respuesta;
	}

	private String pedirDato(String mensaje) {
		System.out.print(mensaje);
		String respuesta = reader.nextLine();
		return respuesta;
	}

	private char pedirGenero(String mensaje) {
		System.out.print(mensaje);
		char respuesta = reader.nextLine().charAt(0);
		return respuesta;
	}

	private LocalDate pedirFechaNacimiento(String mensaje) {
		System.out.print(mensaje);
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
