package dao;

import java.util.List;

import model.Alumno;

/**
 * Interfaz que define las operaciones CRUD para la entidad Alumno.
 */
public interface AlumnoDao {

    /**
     * Inserta un nuevo alumno en la base de datos.
     *
     * @param alumno el alumno a insertar
     * @return el ID generado para el nuevo alumno
     * @throws DataAccessException si ocurre un error al acceder a la base de datos
     */
    int add(Alumno alumno) throws DataAccessException;

    /**
     * Obtiene un alumno por su NIA.
     *
     * @param nia el NIA del alumno a buscar
     * @return el alumno encontrado, o null si no existe
     * @throws DataAccessException si ocurre un error al acceder a la base de datos
     */
    Alumno getByNia(int nia) throws DataAccessException;

    /**
     * Obtiene todos los alumnos registrados en la base de datos.
     *
     * @return una lista de alumnos
     * @throws DataAccessException si ocurre un error al acceder a la base de datos
     */
    List<Alumno> getAll() throws DataAccessException;

    /**
     * Actualiza los datos de un alumno existente.
     *
     * @param alumno el alumno con los datos actualizados
     * @return el número de filas afectadas
     * @throws DataAccessException si ocurre un error al acceder a la base de datos
     */
    int update(Alumno alumno) throws DataAccessException;

    /**
     * Elimina un alumno por su NIA.
     *
     * @param nia el NIA del alumno a eliminar
     * @throws DataAccessException si ocurre un error al acceder a la base de datos
     */
    void delete(int nia) throws DataAccessException;
}
