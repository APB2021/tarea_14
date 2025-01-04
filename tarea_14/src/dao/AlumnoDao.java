package dao;

import java.sql.SQLException;
import java.util.List;

import model.Alumno;

public interface AlumnoDao {

	int add(Alumno alumno) throws SQLException;

	Alumno getByNia(int nia) throws SQLException;

	List<Alumno> getAll() throws SQLException;

	int update(Alumno alumno) throws SQLException;

	void delete(int nia) throws SQLException;

}