package edu.pucmm.pw.entidades;

import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Field;
import dev.morphia.annotations.Id;
import org.bson.types.ObjectId;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;

@Entity("grupos")
public class Grupo {
    @Id
    ObjectId id;
    String nombre;
    String estacion;
    Integer cantidadRegistro;
    LocalDateTime ultimaLectura;
    BigDecimal ultimaTemperatura;
    BigDecimal ultimaHumedad;

    public Grupo() {
    }

    public Grupo(String nombre, String estacion, Integer cantidadRegistro, LocalDateTime ultimaLectura, BigDecimal ultimaTemperatura, BigDecimal ultimaHumedad) {
        this.nombre = nombre;
        this.estacion = estacion;
        this.cantidadRegistro = cantidadRegistro;
        this.ultimaLectura = ultimaLectura;
        this.ultimaTemperatura = ultimaTemperatura;
        this.ultimaHumedad = ultimaHumedad;
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getCantidadRegistro() {
        return cantidadRegistro;
    }

    public void setCantidadRegistro(Integer cantidadRegistro) {
        this.cantidadRegistro = cantidadRegistro;
    }

    public LocalDateTime getUltimaLectura() {
        return ultimaLectura;
    }

    public void setUltimaLectura(LocalDateTime ultimaLectura) {
        this.ultimaLectura = ultimaLectura;
    }

    public BigDecimal getUltimaTemperatura() {
        return ultimaTemperatura;
    }

    public void setUltimaTemperatura(BigDecimal ultimaTemperatura) {
        this.ultimaTemperatura = ultimaTemperatura;
    }

    public BigDecimal getUltimaHumedad() {
        return ultimaHumedad;
    }

    public void setUltimaHumedad(BigDecimal ultimaHumedad) {
        this.ultimaHumedad = ultimaHumedad;
    }

    public String getEstacion() {
        return estacion;
    }

    public void setEstacion(String estacion) {
        this.estacion = estacion;
    }

    @Override
    public String toString() {
        return "Grupo{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", cantidadRegistro=" + cantidadRegistro +
                ", ultimaLectura=" + ultimaLectura +
                ", ultimaTemperatura=" + ultimaTemperatura +
                ", ultimaHumedad=" + ultimaHumedad +
                '}';
    }
}
