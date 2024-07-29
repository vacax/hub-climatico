package edu.pucmm.pw.services;

import com.mongodb.client.result.UpdateResult;
import dev.morphia.Datastore;
import dev.morphia.UpdateOptions;
import dev.morphia.query.filters.Filters;
import dev.morphia.query.updates.UpdateOperators;
import edu.pucmm.pw.Main;
import edu.pucmm.pw.entidades.Grupo;
import edu.pucmm.pw.entidades.Lectura;

import java.math.BigInteger;

public class GrupoServices extends GestionDB<Grupo>{
    private static GrupoServices instancia;

    private GrupoServices() {
        super(Grupo.class);
    }

    public static GrupoServices getInstancia(){
        if(instancia == null){
            instancia = new GrupoServices();
        }
        return instancia;
    }

    public Grupo actualizarInformacionGrupo(Lectura lectura){
        Datastore datastore = getConexionMorphia();

        Grupo grupo = datastore.find(Grupo.class)
                .filter(Filters.eq("nombre", lectura.getGrupo()), Filters.eq("estacion", lectura.getEstacion())).first();
        if(grupo == null){
            grupo = crear(new Grupo(lectura.getGrupo(),
                    lectura.getEstacion(),
                    1,lectura.getFecha(),
                    lectura.getTemperatura(),
                    lectura.getHumedad()));
        }else {
            UpdateResult update = datastore.find(Grupo.class)
                    .filter(Filters.eq("nombre", lectura.getGrupo()), Filters.eq("estacion", lectura.getEstacion()))
                    .update(new UpdateOptions().upsert(true),
                            UpdateOperators.set("ultimaLectura", lectura.getFecha()),
                            UpdateOperators.set("ultimaTemperatura", lectura.getTemperatura()),
                            UpdateOperators.set("ultimaHumedad", lectura.getHumedad()),
                            UpdateOperators.inc("cantidadRegistro"));
            System.out.println("Actualización: "+update.toString());
            grupo = findByID(grupo.getId().toHexString());
        }

        //
        Main.enviarMensajeSubcriptores(grupo);

        return grupo;
    }
}
