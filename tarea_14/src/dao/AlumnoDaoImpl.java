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

    @Override
    public int add(Alumno alumno) {
        String sql = """
                INSERT INTO alumnos (nombre, apellidos, genero, fechaNacimiento, ciclo, curso)
                VALUES (?,?,?,?,?,?);
                """;

        try (Connection conn = MyDataSource.getConnection(); PreparedStatement pstm = conn.prepareStatement(sql)) {
            pstm.setString(1, alumno.getNombre().toUpperCase());
            pstm.setString(2, alumno.getApellidos().toUpperCase());
            pstm.setString(3, String.valueOf(alumno.getGenero()).toUpperCase());
            pstm.setDate(4, Date.valueOf(alumno.getFechaNacimiento()));
            pstm.setString(5, alumno.getCiclo().toUpperCase());
            pstm.setString(6, alumno.getCurso());
            return pstm.executeUpdate();
        } catch (Exception e) {
            throw new DataAccessException("Error al agregar el alumno: " + alumno, e);
        }
    }

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
            pstm.setString(1, alumno.getNombre().toUpperCase());
            pstm.setString(2, alumno.getApellidos().toUpperCase());
            pstm.setString(3, String.valueOf(alumno.getGenero()).toUpperCase());
            pstm.setDate(4, Date.valueOf(alumno.getFechaNacimiento()));
            pstm.setString(5, alumno.getCiclo().toUpperCase());
            pstm.setString(6, alumno.getCurso());
            pstm.setInt(7, alumno.getNia());

            return pstm.executeUpdate();
        } catch (Exception e) {
            throw new DataAccessException("Error al actualizar el alumno: " + alumno, e);
        }
    }

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
