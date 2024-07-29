package edu.pucmm.pw.services;


import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import dev.morphia.Datastore;
import dev.morphia.InsertOneOptions;
import dev.morphia.Morphia;
import dev.morphia.config.ManualMorphiaConfig;
import dev.morphia.query.Query;
import dev.morphia.query.filters.Filters;
import edu.pucmm.pw.utils.DatosEstaticos;
import org.bson.types.ObjectId;

public class GestionDB<T> {

    private final Class<T> claseEntidad;
    private final  MongoClient cliente = MongoDbConexion.getMongoClient();
    //private MorphiaConfig config = new ManualMorphiaConfig(); //por si quieren configurar

    public GestionDB(Class<T> claseEntidad) {
        this.claseEntidad = claseEntidad;
    }

    protected Datastore getConexionMorphia(){
        //return Morphia.createDatastore(cliente, MongoDbConexion.getNombreBaseDatos());
        return Morphia.createDatastore(cliente, new ManualMorphiaConfig().database(MongoDbConexion.getNombreBaseDatos()));
    }

    public T crear(T entidad){

        Datastore datastore = getConexionMorphia();
        try {
            return datastore.save(entidad);
        }catch (Exception  e){
            e.printStackTrace();
        }
        return null;
    }
    public Query<T> find(){
        Datastore datastore = getConexionMorphia();
        Query<T> query = datastore.find(claseEntidad);
        return query;
    }
    public T findByID(String id){
        Datastore datastore = getConexionMorphia();
        Query<T> query = datastore.find(claseEntidad).filter(Filters.eq("_id", new ObjectId(id)));
        return  query.first();
    }

    public void deleteById(String id){
        Datastore datastore = getConexionMorphia();
        datastore.find(claseEntidad).filter(Filters.eq("_id", new ObjectId(id))).delete();

    }

    /**
     * Clase interna para realizar la conexión de a la base de datos.
     */
    private static class MongoDbConexion{

        private static MongoClient mongoClient;
        private static String nombreBaseDatos;

        public static MongoClient getMongoClient() {
            if(mongoClient == null){
                ProcessBuilder processBuilder = new ProcessBuilder();
                String URL_MONGODB = processBuilder.environment().get(DatosEstaticos.URL_MONGO.getValor());
                nombreBaseDatos = processBuilder.environment().get(DatosEstaticos.DB_NOMBRE.getValor());
                //
                System.out.println("URL: "+URL_MONGODB);
                System.out.println("BASE_DATOS: "+nombreBaseDatos);
                //
                mongoClient = MongoClients.create(URL_MONGODB);
            }
            return mongoClient;
        }

        public static String getNombreBaseDatos(){
            return nombreBaseDatos;
        }
    }

}
