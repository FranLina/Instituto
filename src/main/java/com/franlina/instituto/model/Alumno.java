package com.franlina.instituto.model;

import java.sql.Date;

import com.franlina.instituto.utils.ImageUtils;

public class Alumno {
    private int codigo;
    private String nombre;
    private String apellidos;
    private String dni;
    private Date fechaNacimiento;
    private String email;
    private boolean nuevo;
    private byte[] img;

    public Alumno() {
    }

    public Alumno(int codigo) {
        this.codigo = codigo;
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
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

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public Date getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(Date fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isNuevo() {
        return nuevo;
    }

    public void setNuevo(boolean nuevo) {
        this.nuevo = nuevo;
    }

    public byte[] getImg() {
        return img;
    }

    public void setImg(byte[] bs) {
        this.img = bs;
    }

    public String getImageView() {
        return ImageUtils.getImgData(this.img);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + codigo;
        return result;
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
        if (codigo != other.codigo)
            return false;
        return true;
    }

}
