package edu.pucmm.pw.entidades;

import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;
import org.bson.types.ObjectId;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity("lecturas")
public class Lectura {

    @Id
    ObjectId id;
    LocalDateTime fecha;
    String estacion;
    String grupo;
    BigDecimal temperatura;
    BigDecimal humedad;

    public Lectura() {
    }

    public Lectura(String estacion, String grupo,LocalDateTime fecha, BigDecimal temperatura, BigDecimal humedad) {
        this.fecha = fecha;
        this.estacion = estacion;
        this.grupo = grupo;
        this.temperatura = temperatura;
        this.humedad = humedad;
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public String getEstacion() {
        return estacion;
    }

    public void setEstacion(String estacion) {
        this.estacion = estacion;
    }

    public String getGrupo() {
        return grupo;
    }

    public void setGrupo(String grupo) {
        this.grupo = grupo;
    }

    public BigDecimal getTemperatura() {
        return temperatura;
    }

    public void setTemperatura(BigDecimal temperatura) {
        this.temperatura = temperatura;
    }

    public BigDecimal getHumedad() {
        return humedad;
    }

    public void setHumedad(BigDecimal humedad) {
        this.humedad = humedad;
    }
}
