package edu.pucmm.pw;

import edu.pucmm.pw.controllers.ApiController;
import edu.pucmm.pw.entidades.Grupo;
import edu.pucmm.pw.entidades.Usuario;
import edu.pucmm.pw.services.UsuarioServices;
import edu.pucmm.pw.utils.DatosEstaticos;
import io.javalin.Javalin;
import io.javalin.rendering.template.JavalinThymeleaf;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.eclipse.jetty.websocket.api.Session;

import static io.javalin.apibuilder.ApiBuilder.*;
import static j2html.TagCreator.*;

public class Main {

    public static List<Session> usuariosConectados = new ArrayList<>();

    public static void main(String[] args) {

        var URL_MONGO = System.getenv(DatosEstaticos.URL_MONGO.getValor());
        var BASE_DATOS = System.getenv(DatosEstaticos.DB_NOMBRE.getValor());

        if(URL_MONGO==null){
            System.out.println("Debe indicar la variable de ambiente URL_MONGO");
            System.exit(-1);
        }

        if(BASE_DATOS==null){
            System.out.println("Debe indicar la variable de ambiente BASE_DATOS");
            System.exit(-1);
        }

        /**
         * Creando el usuario admin por defecto para la primera ejecución
         */
        validarUsuarioPorDefecto();

        var render = new JavalinThymeleaf();

        var app = Javalin.create(javalinConfig -> {
                    // Definiendo los archivo public
                    javalinConfig.staticFiles.add("/publico");

                    // Habilitando los Webjars.
                    //javalinConfig.staticFiles.enableWebjars();

                    //En la versión 6, cambio la forma de registrar los sistemas de plantilla.
                    // ver en https://javalin.io/migration-guide-javalin-5-to-6
                    javalinConfig.fileRenderer(render);

                    //Rutas para API
                    javalinConfig.router.apiBuilder(() -> {
                        path("/api", () -> {

                            /*//Consulta generales
                            get("/db",RegistroController::creacionRegistroDns);
                            get("/lista",RegistroController::listadoRegistroHtml);
                            get("/crear", RegistroController::formularioCreacion);
                            get("/botones", RegistroController::listadoBotonesPermisos);
                            post(RegistroController::creacionRegistroDns);

                            //Consulta de un registro
                            path("/{id}",() -> {
                                get(RegistroController::formularioEdicion);
                                delete("",RegistroController::eliminarRegistroDns);
                                put(RegistroController::editarRegistroDns);
                            });*/

                            get("/", ctx -> {
                                ctx.result("API Registro de Datos Estaciones Meteorologicas");
                            });

                            post("/", ApiController::persistirLecturaEstacion);
                        });

                        path("/admin/", () -> {

                            /**
                             * Filtro de autenticación
                             */
                            before(ctx -> {
                                //validando si existe el usuario logueado.
                                //System.out.println("Logueado: "+DatosEstaticos.USUARIO.name());
                                Usuario usuario = ctx.sessionAttribute(DatosEstaticos.USUARIO.name());
                                /**
                                 * Si, el usuario no está en la sesión, y la vista no es login.html y no es el endpoint de autenticar,
                                 * lo mando al login.html, lo contrario continuamos con la peticion.
                                 */
                                if(usuario== null && !(ctx.path().contains("login.html") || ctx.path().contains("/autenticar"))){
                                    ctx.redirect("/login.html");
                                }
                            });

                            //Enviando al templates
                            get("/", ctx -> {
                                Map<String, Object> modelo = new HashMap<>();
                                modelo.put("titulo", "Centro de Datos del Clima - ITT363");
                                ctx.render("/templates/index_admin.html", modelo);
                            });


                        });

                        /*path("/usuario", () -> {

                         *//**
                         * Únicamente los usuarios administradores tienen permisos.
                         *//*
                            before(ctx -> {
                                Usuario usuario = ctx.sessionAttribute(DatosEstaticos.USUARIO.name());
                                if(usuario==null || !usuario.isAdministrador()){
                                    throw new UnauthorizedResponse("No tiene acceso, debe ser administrador");
                                }
                            });

                            get("/", ctx -> {
                                Map<String, Object> modelo = new HashMap<>();
                                modelo.put("titulo", "Usuarios Registro DNS DigitalOcean - "+Dominio);
                                ctx.render("/templates/index_usuarios.html", modelo);
                            });
                            //Consulta generales
                            get("/lista", UsuarioController::listadoRegistroHtml);
                            get("/crear", UsuarioController::formularioCreacion);
                            get("/botones", UsuarioController::listadoBotonesPermisos);
                            post(UsuarioController::creacionUsuario);

                            //Consulta de un registro
                            path("/{id}",() -> {
                                get(UsuarioController::formularioEdicion);
                                delete("",UsuarioController::eliminarUsuario);
                                put(UsuarioController::editarUsuario);
                            });
                        });*/
                    });
                })

                /**
                 * Para nuestro ejemplo no importa los valores recibido, lo estaremos validando.
                 */
                .post("/autenticar", ctx -> {
                    //
                    String username = ctx.formParam("username");
                    String password = ctx.formParam("password");
                    //
                    Usuario usuario = UsuarioServices.getInstancia().autenticacion(username, password);
                    ctx.sessionAttribute(DatosEstaticos.USUARIO.getValor(), usuario);
                    //
                    ctx.redirect("/admin/");
                })

                //Salida del sistema.
                .get("/logout", ctx -> {
                    ctx.req().getSession().invalidate();
                    ctx.redirect("/");
                })

                //Enviando al template
                /* .get("/", ctx -> {
                    ctx.result("Aplicación Hub Climatica - Proyecto ITT363");
                 })*/

                .put("/messages", ctx -> {
                    ctx.result("Hola mundo HTMX");
                })

                .ws("/lecturas", wsConfig -> {

                    wsConfig.onConnect(wsConnectContext -> {
                        System.out.println("Conectando: "+wsConnectContext.session.toString());
                        wsConnectContext.enableAutomaticPings();
                        usuariosConectados.add(wsConnectContext.session);
                    });

                    wsConfig.onClose(wsCloseContext -> {
                        System.out.println("Desconectando: "+wsCloseContext.session.toString());
                        usuariosConectados.remove(wsCloseContext.session);
                    });

                    wsConfig.onMessage(wsMessageContext -> {
                        String mensaje  = wsMessageContext.message();
                        System.out.println("Mensaje Recibido: "+mensaje);
                        /*wsMessageContext.send("<div id=\"lecturas\" hx-swap-oob=\"beforeend\">Notificación</div>");
                        wsMessageContext.send("<div id=\"chat_room\" hx-swap-oob=\"morphdom\">En ChatRoom</div>");*/
                    });


                })

                .start(7000);
    }

    /**
     * Crea el usuario admin por defecto la primera vez que es ejecutado
     */
    private static void validarUsuarioPorDefecto(){
        var lista = UsuarioServices.getInstancia().listaUsuarios();
        if(lista.isEmpty()){
            UsuarioServices.getInstancia().crear(new Usuario("admin", "admin", true));
        }
    }

    /**
     *
     * @param grupo
     */
    public static void enviarMensajeSubcriptores(Grupo grupo){
        for(Session sesionConectada : usuariosConectados){
            try {
                String divTabla = div(
                        table(
                                tr(th("Información Grupo #"+grupo.getNombre()).withColspan("2")),
                                tr(
                                        td("Estación"), td(grupo.getEstacion())
                                ),
                                tr(
                                        td("Fecha Lectura"), td(grupo.getUltimaLectura().toString())
                                ),
                                tr(
                                        td("Cantidad Registro"), td(""+grupo.getCantidadRegistro())
                                ),
                                tr(
                                        td("Temperatura"), td(grupo.getUltimaTemperatura().toPlainString())),
                                tr(
                                        td("Humedad"), td(grupo.getUltimaHumedad().toPlainString())
                                )
                        ).withStyle("border: 1px solid black;")
                ).withId("lecturas").attr("hx-swap-oob", "beforeend").render();
                System.out.println("Enviando mensaje: "+divTabla);
                sesionConectada.getRemote().sendString(divTabla);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Obtener el puerto de la aplicación
     * @return
     */
    private static int getPuertoAplicacion() {
        ProcessBuilder processBuilder = new ProcessBuilder();
        if (processBuilder.environment().get("PORT") != null) {
            return Integer.parseInt(processBuilder.environment().get("PORT"));
        }
        return 7000; //Retorna el puerto por defecto en caso de no estar en Heroku.
    }
}