package edu.pucmm.pw.services;

import edu.pucmm.pw.dtos.LecturaRequest;
import edu.pucmm.pw.entidades.Lectura;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LecturaServices extends GestionDB<Lectura>{

    private static LecturaServices instancia;

    private LecturaServices() {
        super(Lectura.class);
    }

    public static LecturaServices getInstancia(){
        if(instancia == null){
            instancia =new LecturaServices();
        }
        return instancia;
    }

    /**
     *
     * @param lecturaDto
     * @return
     */
    public Lectura crearLectura(LecturaRequest lecturaDto){
        Lectura lectura = new Lectura(
                lecturaDto.estacion(),
                lecturaDto.grupo(),
                LocalDateTime.parse(lecturaDto.fecha(), DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")),
                lecturaDto.temperatura(),
                lecturaDto.humedad());
        //
        var l = crear(lectura);
        if(l !=null){
            GrupoServices.getInstancia().actualizarInformacionGrupo(l);
        }
        return l;
    }
}
