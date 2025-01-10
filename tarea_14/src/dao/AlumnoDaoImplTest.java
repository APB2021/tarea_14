package dao;

import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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
		alumno.setNombre("ALBERTO");
		alumno.setApellidos("POLO BARDERA");
		alumno.setGenero('M');
		alumno.setFechaNacimiento(LocalDate.of(1973, 3, 17));
		alumno.setCiclo("DAM");
		alumno.setCurso("2º");

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

	@Test
	void testGetByNia() {
		try {
			// Busca un alumno con un NIA conocido.
			Alumno alumno = alumnoDao.getByNia(1);

			// Comprueba que no sea nulo.
			assertNotNull(alumno, "El alumno con NIA 1 no debe ser nulo.");

			// Verifica que los datos coincidan.
			assertEquals(1, alumno.getNia(), "El NIA del alumno debe ser 1.");
			assertEquals("MARÍA", alumno.getNombre(), "El nombre del alumno debe ser MARÍA.");
			assertEquals("OLALLA GARCÍA", alumno.getApellidos(), "Los apellidos deben ser OLALLA GARCÍA.");
			assertEquals('F', alumno.getGenero(), "El género debe ser F.");
			assertEquals(1979, alumno.getFechaNacimiento().getYear(), "El año de nacimiento debe ser 1979.");
			assertEquals("PEDAGOGÍA TERAPEUTICA", alumno.getCiclo(),
					"El ciclo de MARÍA debe ser PEDAGOGÍA TERAPEUTICA");
			assertEquals("2º", alumno.getCurso(), "El curso de MARÍA debe ser 2º");

		} catch (Exception e) {
			e.printStackTrace();
			fail("No se esperaba ninguna excepción.");
		}
	}

	@Test
	void testUpdate() {
		try {
			// Busca un alumno existente para actualizar
			Alumno alumno = alumnoDao.getByNia(1);

			// Asegúrate de que el alumno exista
			assertNotNull(alumno, "El alumno con NIA 1 debe existir antes de actualizarlo.");

			// Modifica algunos campos del alumno
			alumno.setNombre("María Actualizada");
			alumno.setApellidos("Olalla García Actualizada");
			alumno.setCiclo("Nuevo Ciclo");
			alumno.setCurso("3º");

			// Llama al método update
			int filasActualizadas = alumnoDao.update(alumno);

			// Verifica que se haya actualizado una fila
			assertEquals(1, filasActualizadas, "Debe actualizar exactamente una fila.");

			// Vuelve a obtener el alumno para verificar los cambios
			Alumno alumnoActualizado = alumnoDao.getByNia(1);

			// Verifica que los cambios se hayan aplicado correctamente
			assertEquals("MARÍA ACTUALIZADA", alumnoActualizado.getNombre(), "El nombre debe haberse actualizado.");
			assertEquals("OLALLA GARCÍA ACTUALIZADA", alumnoActualizado.getApellidos(),
					"Los apellidos deben haberse actualizado.");
			assertEquals("NUEVO CICLO", alumnoActualizado.getCiclo(), "El ciclo debe haberse actualizado.");
			assertEquals("3º", alumnoActualizado.getCurso(), "El curso debe haberse actualizado.");

		} catch (Exception e) {
			e.printStackTrace();
			fail("No se esperaba ninguna excepción.");
		}
	}

	@Test
	void testDelete() {
		try {
			// Asegúrate de que el alumno con NIA 1 existe antes de eliminarlo
			Alumno alumno = alumnoDao.getByNia(1);
			assertNotNull(alumno, "El alumno con NIA 1 debe existir antes de eliminarlo.");

			// Llama al método delete
			alumnoDao.delete(1);

			// Verifica que el alumno ya no exista en la base de datos
			Alumno alumnoEliminado = alumnoDao.getByNia(1);
			assertEquals(null, alumnoEliminado, "El alumno con NIA 1 debe haberse eliminado.");

		} catch (Exception e) {
			e.printStackTrace();
			fail("No se esperaba ninguna excepción.");
		}
	}

}
