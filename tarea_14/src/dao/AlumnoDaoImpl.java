package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.Alumno;
import pool.MyDataSource;

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

	@Override
	public int add(Alumno alumno) throws SQLException {

		String sql = """
							INSERT INTO alumnos (nombre, apellidos, genero, fechaNacimiento, ciclo, curso)
							VALUES (?,?,?,?,?,?);
				""";

		int result = -14;

		try (Connection conn = MyDataSource.getConnection(); PreparedStatement pstm = conn.prepareStatement(sql)) {

			// Para JDBC los índices comienzan en "1":
			pstm.setString(1, alumno.getNombre());
			pstm.setString(2, alumno.getApellidos());
			pstm.setString(3, String.valueOf(alumno.getGenero()));
			pstm.setDate(4, Date.valueOf(alumno.getFechaNacimiento()));
			pstm.setString(5, alumno.getCiclo());
			pstm.setString(6, alumno.getCurso());

			result = pstm.executeUpdate();

		} catch (Exception e) {

		}

		return result;
	}

	@Override
	public Alumno getbyNia(int nia) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Alumno> getAll() throws SQLException {

		String sql = """
				SELECT nia, nombre, apellidos, genero, fechaNacimiento, ciclo, curso, numeroGrupo
				FROM alumnos;
				""";

		List<Alumno> result = new ArrayList<>();

		try (Connection conn = MyDataSource.getConnection();
				PreparedStatement pstm = conn.prepareStatement(sql);
				ResultSet rs = pstm.executeQuery()) {

			Alumno alumno;

			while (rs.next()) {
				alumno = new Alumno();
				alumno.setNia(rs.getInt("nia"));
				alumno.setNombre(rs.getString("nombre"));
				alumno.setApellidos(rs.getString("apellidos"));
				alumno.setGenero(Character.valueOf(rs.getString("genero").charAt(0)));
				alumno.setFechaNacimiento(rs.getDate("fechaNacimiento").toLocalDate());
				alumno.setCiclo(rs.getNString("ciclo"));
				alumno.setCurso(rs.getString("curso"));
				alumno.setGrupo(rs.getString("numeroGrupo"));
				
				// Tras construir el 'alumno' lo añadimos a la lista de resultados para devolverlo posteriormente:
				result.add(alumno);
			}

		}

		return result;
	}

	@Override
	public int update(int nia) throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void delete(int nia) throws SQLException {
		// TODO Auto-generated method stub

	}

}
