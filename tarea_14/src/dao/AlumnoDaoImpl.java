package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import model.Alumno;
import pool.MyDataSource;

/**
 * Implementación de la interfaz AlumnoDao utilizando JDBC.
 */
public class AlumnoDaoImpl implements AlumnoDao {

	private static AlumnoDaoImpl instance;

	static {
		instance = new AlumnoDaoImpl();
	}

	private AlumnoDaoImpl() {
	}

	public static AlumnoDaoImpl getInstance() {
		return instance;
	}

	/**
	 * Agrega un nuevo alumno a la base de datos.
	 *
	 * @param alumno Objeto Alumno que se desea agregar.
	 * @return Número de filas afectadas por la operación.
	 * @throws DataAccessException Si ocurre algún error durante la operación.
	 */

	@Override
	public int add(Alumno alumno) {
		String sql = """
				INSERT INTO alumnos (nombre, apellidos, genero, fechaNacimiento, ciclo, curso)
				VALUES (?,?,?,?,?,?);
				""";

		try (Connection conn = MyDataSource.getConnection(); PreparedStatement pstm = conn.prepareStatement(sql)) {
			setAlumnoParams(pstm, alumno);
			return pstm.executeUpdate();
		} catch (Exception e) {
			throw new DataAccessException("Error al agregar el alumno: " + alumno, e);
		}
	}

	/**
	 * Obtiene un alumno de la base de datos a partir de su NIA.
	 *
	 * @param nia Número de Identificación del Alumno (NIA).
	 * @return Un objeto {@link Alumno} con los datos del alumno si se encuentra, o
	 *         {@code null} si no existe.
	 * @throws DataAccessException Si ocurre algún error al realizar la consulta.
	 */

	@Override
	public Alumno getByNia(int nia) {
		String sql = """
				SELECT nia, nombre, apellidos, genero, fechaNacimiento, ciclo, curso, numeroGrupo
				FROM alumnos
				WHERE nia = ?;
				""";

		try (Connection conn = MyDataSource.getConnection(); PreparedStatement pstm = conn.prepareStatement(sql)) {
			pstm.setInt(1, nia);

			try (ResultSet rs = pstm.executeQuery()) {
				if (rs.next()) {
					return mapResultSetToAlumno(rs);
				}
			}
		} catch (Exception e) {
			throw new DataAccessException("Error al obtener el alumno con NIA: " + nia, e);
		}

		return null;
	}

	/**
	 * Obtiene una lista de todos los alumnos almacenados en la base de datos.
	 *
	 * @return Una lista de objetos {@link Alumno}, que contiene todos los registros
	 *         en la tabla de alumnos. Si no hay alumnos, la lista estará vacía.
	 * @throws DataAccessException Si ocurre algún error al realizar la consulta.
	 */

	@Override
	public List<Alumno> getAll() {
		String sql = """
				SELECT nia, nombre, apellidos, genero, fechaNacimiento, ciclo, curso, numeroGrupo
				FROM alumnos;
				""";

		List<Alumno> result = new ArrayList<>();
		try (Connection conn = MyDataSource.getConnection();
				PreparedStatement pstm = conn.prepareStatement(sql);
				ResultSet rs = pstm.executeQuery()) {

			while (rs.next()) {
				result.add(mapResultSetToAlumno(rs));
			}
		} catch (Exception e) {
			throw new DataAccessException("Error al obtener la lista de alumnos.", e);
		}

		return result;
	}

	/**
	 * Actualiza la información de un alumno existente en la base de datos.
	 *
	 * @param alumno Un objeto {@link Alumno} que contiene los datos actualizados
	 *               del alumno. El NIA debe coincidir con un registro existente.
	 * @return El número de filas afectadas por la operación de actualización.
	 * @throws DataAccessException Si ocurre algún error al realizar la
	 *                             actualización o si no se encuentra el registro.
	 */

	@Override
	public int update(Alumno alumno) {
		String sql = """
				UPDATE alumnos SET
				    nombre = ?,
				    apellidos = ?,
				    genero = ?,
				    fechaNacimiento = ?,
				    ciclo  = ?,
				    curso = ?
				WHERE nia = ?;
				""";

		try (Connection conn = MyDataSource.getConnection(); PreparedStatement pstm = conn.prepareStatement(sql)) {
			setAlumnoParams(pstm, alumno);
			pstm.setInt(7, alumno.getNia());

			return pstm.executeUpdate();
		} catch (Exception e) {
			throw new DataAccessException("Error al actualizar el alumno: " + alumno, e);
		}
	}

	/**
	 * Elimina un alumno de la base de datos a partir de su NIA.
	 *
	 * @param nia Número de Identificación del Alumno (NIA) del registro que se
	 *            desea eliminar.
	 * @throws DataAccessException Si ocurre algún error al realizar la eliminación
	 *                             o si no se encuentra el registro.
	 */

	@Override
	public void delete(int nia) {
		String sql = """
				DELETE FROM alumnos
				WHERE nia = ?;
				""";

		try (Connection conn = MyDataSource.getConnection(); PreparedStatement pstm = conn.prepareStatement(sql)) {
			pstm.setInt(1, nia);
			pstm.executeUpdate();
		} catch (Exception e) {
			throw new DataAccessException("Error al eliminar el alumno con NIA: " + nia, e);
		}
	}

	/**
	 * Método auxiliar para asignar parámetros comunes de un Alumno a un
	 * PreparedStatement.
	 *
	 * @param pstm   PreparedStatement al que se asignarán los parámetros.
	 * @param alumno Objeto Alumno cuyos datos se usarán.
	 * @throws Exception si ocurre un error durante la asignación.
	 */
	private void setAlumnoParams(PreparedStatement pstm, Alumno alumno) throws Exception {
		pstm.setString(1, alumno.getNombre().toUpperCase());
		pstm.setString(2, alumno.getApellidos().toUpperCase());
		pstm.setString(3, String.valueOf(alumno.getGenero()).toUpperCase());
		pstm.setDate(4, Date.valueOf(alumno.getFechaNacimiento()));
		pstm.setString(5, alumno.getCiclo().toUpperCase());
		pstm.setString(6, alumno.getCurso());
	}

	/**
	 * Método auxiliar para mapear un ResultSet a un objeto Alumno.
	 *
	 * @param rs el ResultSet obtenido de la consulta
	 * @return un objeto Alumno con los datos del ResultSet
	 * @throws Exception si ocurre un error al mapear los datos
	 */
	private Alumno mapResultSetToAlumno(ResultSet rs) throws Exception {
		Alumno alumno = new Alumno();
		alumno.setNia(rs.getInt("nia"));
		alumno.setNombre(rs.getString("nombre"));
		alumno.setApellidos(rs.getString("apellidos"));
		alumno.setGenero(rs.getString("genero").charAt(0));
		alumno.setFechaNacimiento(rs.getDate("fechaNacimiento").toLocalDate());
		alumno.setCiclo(rs.getString("ciclo"));
		alumno.setCurso(rs.getString("curso"));
		alumno.setGrupo(rs.getString("numeroGrupo"));
		return alumno;
	}
}
