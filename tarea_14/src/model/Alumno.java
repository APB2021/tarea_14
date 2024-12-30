package model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

public class Alumno implements Serializable {

	private static final long serialVersionUID = -1773328651409080184L;

	// Atributos privados de la clase Alumno:

	private int nia;
	private String nombre;
	private String apellidos;
	private char genero;
	private LocalDate fechaNacimiento;
	private String ciclo;
	private String curso;
	private String grupo;

	// Constructores de la clase Alumno:

	// Constructor vacío:
	public Alumno() {

	}

	// Constructor sin el nia ni el grupo:
	public Alumno(String nombre, String apellidos, char genero, LocalDate fechaNacimiento, String ciclo, String curso) {
		this.nombre = nombre;
		this.apellidos = apellidos;
		this.genero = genero;
		this.fechaNacimiento = fechaNacimiento;
		this.ciclo = ciclo;
		this.curso = curso;

	}

	// Constructor con todos los atributos:
	public Alumno(int nia, String nombre, String apellidos, char genero, LocalDate fechaNacimiento, String ciclo,
			String curso, String grupo) {
		this.nia = nia;
		this.nombre = nombre;
		this.apellidos = apellidos;
		this.genero = genero;
		this.fechaNacimiento = fechaNacimiento;
		this.ciclo = ciclo;
		this.curso = curso;
		this.grupo = grupo;
	}

	// Getters & Setters:

	public int getNia() {
		return nia;
	}

	public void setNia(int nia) {
		this.nia = nia;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellidos() {
		return apellidos;
	}

	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}

	public char getGenero() {
		return genero;
	}

	public void setGenero(char genero) {
		this.genero = genero;
	}

	public LocalDate getFechaNacimiento() {
		return fechaNacimiento;
	}

	public void setFechaNacimiento(LocalDate fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
	}

	public String getCiclo() {
		return ciclo;
	}

	public void setCiclo(String ciclo) {
		this.ciclo = ciclo;
	}

	public String getCurso() {
		return curso;
	}

	public void setCurso(String curso) {
		this.curso = curso;
	}

	public String getGrupo() {
		return grupo;
	}

	public void setGrupo(String grupo) {
		this.grupo = grupo;
	}

	@Override
	public int hashCode() {
		return Objects.hash(apellidos, ciclo, curso, fechaNacimiento, genero, grupo, nia, nombre);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Alumno other = (Alumno) obj;
		return Objects.equals(apellidos, other.apellidos) && Objects.equals(ciclo, other.ciclo)
				&& Objects.equals(curso, other.curso) && Objects.equals(fechaNacimiento, other.fechaNacimiento)
				&& genero == other.genero && Objects.equals(grupo, other.grupo) && nia == other.nia
				&& Objects.equals(nombre, other.nombre);
	}

	@Override
	public String toString() {
		return "Alumno [nia=" + nia + ", nombre=" + nombre + ", apellidos=" + apellidos + ", genero=" + genero
				+ ", fechaNacimiento=" + fechaNacimiento + ", ciclo=" + ciclo + ", curso=" + curso + ", grupo=" + grupo
				+ "]";
	}

}
