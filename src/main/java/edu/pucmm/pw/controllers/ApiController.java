package edu.pucmm.pw.controllers;

import edu.pucmm.pw.dtos.LecturaRequest;
import edu.pucmm.pw.dtos.LecturaResponse;
import edu.pucmm.pw.entidades.Lectura;
import edu.pucmm.pw.entidades.Usuario;
import edu.pucmm.pw.services.LecturaServices;
import edu.pucmm.pw.services.UsuarioServices;
import edu.pucmm.pw.utils.DatosEstaticos;
import io.javalin.http.Context;
import io.javalin.http.UnauthorizedResponse;
import org.jetbrains.annotations.NotNull;

public class ApiController {

    /**
     * Almacena la lectura recibida en la base de datos validando el header de seguridad.
     * @param ctx
     */
    public static void persistirLecturaEstacion(@NotNull Context ctx){
        //Validando la petición
        String token = ctx.header(DatosEstaticos.HEADER_SEGURIDAD_TOKEN.getValor());
        Usuario usuario = UsuarioServices.getInstancia().autenticacion(token);
        if(usuario == null){
            throw new UnauthorizedResponse("Token no coincide");
        }

        //Recuperando al información
        var tmp = ctx.bodyAsClass(LecturaRequest.class);
        Lectura lectura = LecturaServices.getInstancia().crearLectura(tmp);

        //
        ctx.json(new LecturaResponse(lectura.getId().toHexString()));
    }
}
