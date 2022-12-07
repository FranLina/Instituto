package com.franlina.instituto.dao.impl;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Order;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

import com.franlina.instituto.dao.AlumnosDAO;
import com.franlina.instituto.dao.mappers.AlumnoMapper;
import com.franlina.instituto.model.Alumno;

import jakarta.annotation.PostConstruct;

@Repository
public class AlumnosDAOImpl extends JdbcDaoSupport implements AlumnosDAO {

    @Autowired
    DataSource dataSource;

    @PostConstruct
    private void initialize() {
        setDataSource(dataSource);
    }

    @Override
    public Page<Alumno> findAll(Pageable page) {
        String queryCount = "select count(1) from Alumnos";
        Integer total = getJdbcTemplate().queryForObject(queryCount, Integer.class);

        Order order = !page.getSort().isEmpty() ? page.getSort().toList().get(0) : Order.by("codigo");

        String query = "SELECT * FROM Alumnos ORDER BY " + order.getProperty() + " "
                + order.getDirection().name() + " LIMIT " + page.getPageSize() + " OFFSET " + page.getOffset();

        final List<Alumno> alumnos = getJdbcTemplate().query(query, new RowMapper<Alumno>() {

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
                return alumnos;
            }

        });

        return new PageImpl<Alumno>(alumnos, page, total);
    }

    @Override
    public Alumno findById(int codigo) {
        String query = "select * from Alumnos where codigo = ?";

        Object params[] = { codigo };
        int types[] = { Types.INTEGER };

        Alumno alumno = (Alumno) getJdbcTemplate().queryForObject(query, params, types, new AlumnoMapper());

        return alumno;
    }

    @Override
    public void insert(Alumno alumno) {
        String query = "insert into Alumnos (nombre," +
                " apellidos," +
                " dni," +
                " fecha_nacimiento," +
                " email," +
                " nuevo," +
                " img)" +
                " values (?, ?, ?, ?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        getJdbcTemplate().update(new PreparedStatementCreator() {

            @Override
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement ps = getConnection().prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

                ps.setString(1, alumno.getNombre());
                ps.setString(2, alumno.getApellidos());
                ps.setString(3, alumno.getDni());
                ps.setDate(4, alumno.getFechaNacimiento());
                ps.setString(5, alumno.getEmail());
                ps.setBoolean(6, alumno.isNuevo());
                InputStream is = new ByteArrayInputStream(alumno.getImg());

                ps.setBlob(7, is);
                return ps;
            }
        }, keyHolder);

        alumno.setCodigo(keyHolder.getKey().intValue());

    }

    @Override
    public void update(Alumno alumno) {
        String query = "update Alumnos set nombre = ?, apellidos = ?, dni = ?, email = ?, nuevo = ?, fecha_nacimiento = ? where codigo = ?";
        Object[] params = {
                alumno.getNombre(),
                alumno.getApellidos(),
                alumno.getDni(),
                alumno.getEmail(),
                alumno.isNuevo(),
                alumno.getFechaNacimiento(),
                alumno.getCodigo()
        };

        final int[] types = { Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.BOOLEAN, Types.DATE,
                Types.INTEGER };

        int update = getJdbcTemplate().update(query, params, types);
    }

    @Override
    public void updateImageView(Alumno alumno) {
        String query = "update Alumnos set img = ? where codigo = ?";
        Object[] params = {
                alumno.getImg(),
                alumno.getCodigo()
        };

        final int[] types = { Types.BLOB, Types.INTEGER };

        int update = getJdbcTemplate().update(query, params, types);

    }

    @Override
    public void delete(int codigo) {
        String query = "delete from Alumnos where codigo = ?";
        Object[] params = { codigo };

        final int[] types = { Types.INTEGER };

        int update = getJdbcTemplate().update(query, params, types);
    }

}
