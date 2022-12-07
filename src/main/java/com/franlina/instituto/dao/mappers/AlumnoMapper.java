package com.franlina.instituto.dao.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.franlina.instituto.model.Alumno;

import io.micrometer.common.lang.Nullable;

public class AlumnoMapper implements org.springframework.jdbc.core.RowMapper<Alumno> {
    @Override
    @Nullable
    public Alumno mapRow(ResultSet rs, int rowNum) throws SQLException {
        Alumno alumnos = new Alumno();
        alumnos.setCodigo(rs.getInt("codigo"));
        alumnos.setNombre(rs.getString("nombre"));
        alumnos.setApellidos(rs.getString("apellidos"));
        alumnos.setDni(rs.getString("dni"));
        alumnos.setEmail(rs.getString("email"));
        alumnos.setFechaNacimiento(rs.getDate("fecha_nacimiento"));
        alumnos.setNuevo(rs.getBoolean("nuevo"));
        alumnos.setImg(rs.getBytes("img"));
        return alumnos;
    }
}
