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
        try (var conn = MyDataSource.getConnection(); 
             var stmt = conn.createStatement()) {
            stmt.execute("DELETE FROM alumnos"); // Limpia la tabla
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
}
