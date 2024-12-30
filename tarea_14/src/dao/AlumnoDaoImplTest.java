package dao;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.Alumno;
import pool.MyDataSource;

public class AlumnoDaoImplTest {

	private AlumnoDaoImpl alumnoDao;

	@BeforeEach
	void setUp() {
		// Configuramos el DAO antes de cada prueba
		alumnoDao = AlumnoDaoImpl.getInstance();

		// Opcional: Reinicia o limpia tu base de datos de prueba
		try (var conn = MyDataSource.getConnection(); var stmt = conn.createStatement()) {
			// stmt.execute("DELETE FROM alumnos"); // Limpia la tabla
			// Elimina los datos existentes de la tabla
			stmt.execute("DROP TABLE IF EXISTS alumnos");

			// Crea la tabla de ejemplo
			stmt.execute("""
					CREATE TABLE alumnos (
					    nia INT AUTO_INCREMENT PRIMARY KEY,
					    nombre VARCHAR(50) NOT NULL,
					    apellidos VARCHAR(50) NOT NULL,
					    genero CHAR(1) NOT NULL,  -- 'M' para masculino, 'F' para femenino, etc.
					    fechaNacimiento DATE NOT NULL,
					    ciclo VARCHAR(50),
					    curso VARCHAR(10),
					    numeroGrupo VARCHAR(1) NOT NULL DEFAULT "1"
					);
					""");

			// Inserta datos de prueba

			stmt.execute(
					"""
							INSERT INTO alumnos (nombre, apellidos, genero, fechaNacimiento, ciclo, curso, numeroGrupo)
							VALUES ("MARÍA", "OLALLA GARCÍA", 'F', "1979-12-18", "PEDAGOGÍA TERAPEUTICA", "2º", 1),
							("SILVIA", "POLO BARDERA", 'F', "1978-06-13", "CONTABILIDAD", "2º", 1),
							("MIGUEL", "PEÑA CAMACHO", 'M', "2000-01-01", "DESARROLLO DE APLICACIONES MULTIPLATAFORMA", "2º", 1),
							("LAURA", "BLÁZQUEZ MARTÍN", 'F', "2000-01-01", "DESARROLLO DE APLICACIONES MULTIPLATAFORMA", "2º", 1),
							("CARLOS", "POLO BARDERA", 'M', "1975-12-16", "ELECTRICIDAD", "2º", 1),
							("ALBERTO", "POLO BARDERA", 'M', "1973-03-17", "DAM", "2º", 1);
									    """);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	void testAdd() {
		// Crea un objeto Alumno para insertar
		Alumno alumno = new Alumno();
		alumno.setNombre("Alberto");
		alumno.setApellidos("Polo Bardera");
		alumno.setGenero('M');
		alumno.setFechaNacimiento(LocalDate.of(1973, 3, 17));
		alumno.setCiclo("DAM");
		alumno.setCurso("2");

		try {
			// Llamamos al método add y verificamos el resultado
			int filasInsertadas = alumnoDao.add(alumno);
			assertEquals(1, filasInsertadas, "Debe insertar una fila en la tabla");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	void testGetAll() {
		try {
			// Llamamos al método getAll() y verificamos el tamaño de la lista
			List<Alumno> alumnos = alumnoDao.getAll();

			// Comprobamos que la lista contiene los 6 alumnos insertados
			assertEquals(6, alumnos.size(), "Debe devolver 6 alumnos");

			// Comprobamos que el primer alumno tiene los datos correctos
			Alumno alumno1 = alumnos.get(0);
			assertEquals("MARÍA", alumno1.getNombre(), "El nombre del primer alumno debe ser MARÍA");
			assertEquals("OLALLA GARCÍA", alumno1.getApellidos(),
					"Los apellidos del primer alumno deben ser OLALLA GARCÍA");
			assertEquals('F', alumno1.getGenero(), "El género del primer alumno debe ser F");
			assertEquals(1979, alumno1.getFechaNacimiento().getYear(), "El año de nacimiento de MARÍA debe ser 1979");
			assertEquals("PEDAGOGÍA TERAPEUTICA", alumno1.getCiclo(),
					"El ciclo de MARÍA debe ser PEDAGOGÍA TERAPEUTICA");
			assertEquals("2º", alumno1.getCurso(), "El curso de MARÍA debe ser 2º");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
