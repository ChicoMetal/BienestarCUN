package com.co.edu.cun.www1104379214.bienestarcun;



import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by root on 31/10/15.
 */
public class Constantes {
    //*************************************
    //*************************************Constantes
    //*************************************

    public static final String TAG = "CONTROL";

    public static final String UsrSuperAd = "1111";
    public static final String UsrAd = "1110";
    public static final String UsrAdCircle = "1101";
    public static final String UsrDocente = "1100";
    public static final String UsrStudent = "1011";
    public static final String UsrExStudent = "1010";
    public static final String UsrPsicologa = "1001";
    public static final String UsrLoginOff = "1000";


    public static final int TiempoEsperaTask = 2000;

    //datos usuario por default

    private static final String sedeSincelejo = "Sincelejo",
                                sedeBogota = "Bogota",
                                SedeMonteria = "Monteria";//sedes

    public static final String[] DftUsrNameSedes = {sedeSincelejo, sedeBogota,SedeMonteria };//indices para mostrar (deben ser iguales a las llaves de los codigos)

    public static final JSONObject DftUsrId = new JSONObject();
    static {
        try {
            DftUsrId.put(sedeSincelejo, "1000000000");
            DftUsrId.put(sedeBogota,    "3000000000");
            DftUsrId.put(SedeMonteria,  "6000000000");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }




    public static final String DftUsrName = "usuario";
    public static final String DftUsrPass = "usuario";
    public static final String DftUsrToken = "default";

    //1111 = Super Administrador
    //1110 = Administrador
    //1101 = Administrador de circulo
    //1100 = Docente CUN
    //1011 = Estudiante
    //1010 = Estudiante egresado
    //1001 = Psicologa
    //1000 = Usuario sin loguear


    //****************************************
    //****************************************Services
    //****************************************

    public static final JSONObject msmServices = new JSONObject();

    static{
        try {
            //local mensaje
            msmServices.put("0", "Ocurrio un error, verifica tu conexion");

            //mensajes php
            msmServices.put("0000", "Error al conectar");
            msmServices.put("0001", "Error al ejecutar el query");
            msmServices.put("0010", "No se encontraron datos");
            msmServices.put("1000", "Todo ha ido bien");
            msmServices.put("0100", "Error al seleccionar la tabla");
            msmServices.put("0011", "Error en la instruccion");
            msmServices.put("1100", "Instruccion ejecutada correctamente");
            msmServices.put("1101", "Peticion indeterminada");
            msmServices.put("000000000", "Error en el servidor x_x, trabajaremos en ello");

            //mensajes de login
            msmServices.put("11111", "Usuario activo");
            msmServices.put("10000", "El usuario no existe");
            msmServices.put("10001", "Usuario bloqueado");
            msmServices.put("10010", "Combinacion de datos erronea");
            msmServices.put("10110", "Sesion finalizada");

            //status notificaciones
            msmServices.put("111", "Pendiente");
            msmServices.put("110", "Aplazado");
            msmServices.put("000", "Cancelado");

            //respuestas deserciones
            msmServices.put("00", "No existe el usuario a reportar");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //****************************************
    //****************************************Status code response
    //****************************************

    public static final JSONObject StatusServices = new JSONObject();

    static{
        try {
            //status 1xx Respuestas informativas
            StatusServices.put("100", "Continue");
            StatusServices.put("101", "Switching Protocols");
            StatusServices.put("102", "Processing ");
            StatusServices.put("103", "Checkpoint");
            //status 2xx Peticiones correctas
            StatusServices.put("200", "OK");
            StatusServices.put("201", "Created");
            StatusServices.put("202", "Accepted");
            StatusServices.put("203", "Non-Authoritative Information (desde HTTP/1.1)");
            StatusServices.put("204", "No Content");
            StatusServices.put("205", "Reset Content");
            StatusServices.put("206", "Partial Content");
            StatusServices.put("207", "Multi-Status (Multi-Status, WebDAV)");
            //status 3xx Redirecciones
            StatusServices.put("208", "Already Reported (WebDAV)");
            StatusServices.put("300", "Multiple Choices");
            StatusServices.put("301", "Found");
            StatusServices.put("302", "See Other (desde HTTP/1.1)");
            StatusServices.put("303", "Not Modified");
            StatusServices.put("304", "Use Proxy (desde HTTP/1.1)");
            StatusServices.put("305", "Switch Proxy");
            StatusServices.put("306", "Temporary Redirect (desde HTTP/1.1)");
            StatusServices.put("307", "Permanent Redirect");
            StatusServices.put("308", "Bad Request");
            //status 4xx Errores del cliente
            StatusServices.put("401", "Unauthorized");
            StatusServices.put("402", "Payment Required");
            StatusServices.put("403", "Forbidden");
            StatusServices.put("404", "Not Found");
            StatusServices.put("405", "Method Not Allowed");
            StatusServices.put("406", "Not Acceptable");
            StatusServices.put("407", "Proxy Authentication Required");
            StatusServices.put("408", "Request Timeout");
            StatusServices.put("409", "Conflict");
            StatusServices.put("410", "Gone");
            StatusServices.put("411", "Length Required");
            StatusServices.put("412", "Precondition Failed");
            StatusServices.put("413", "Request Entity Too Large");
            StatusServices.put("414", "Request-URI Too Long");
            StatusServices.put("415", "Unsupported Media Type");
            StatusServices.put("416", "Requested Range Not Satisfiable");
            StatusServices.put("417", "Expectation Failed");
            StatusServices.put("418", "I'm a teapot");
            StatusServices.put("422", "Unprocessable Entity ");
            StatusServices.put("423", "Locked ");
            StatusServices.put("424", "Failed Dependency");
            StatusServices.put("425", "Unassigned");
            StatusServices.put("426", "Upgrade Required");
            StatusServices.put("428", "Precondition Required");
            StatusServices.put("429", "Too Many Requests");
            StatusServices.put("431", "Request Header Fileds Too Large");
            StatusServices.put("449", "Microsoft");
            StatusServices.put("451", "Unavailable for Legal Reasons");
            //status 5xx Errores de servidor
            StatusServices.put("500", "Internal Server Error");
            StatusServices.put("501", "Not Implemented");
            StatusServices.put("502", "Bad Gateway");
            StatusServices.put("503", "Service Unavailable");
            StatusServices.put("504", "Gateway Timeout");
            StatusServices.put("505", "HTTP Version Not Supported");
            StatusServices.put("506", "Variant Also Negotiates ");
            StatusServices.put("507", "Insufficient Storage ");
            StatusServices.put("508", "Loop Detected (WebDAV)");
            StatusServices.put("509", "Bandwidth Limit Exceeded");
            StatusServices.put("510", "Not Extended ");
            StatusServices.put("511", "Network Authentication Required");



        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    //*****************************************
    //*****************************************Mensaje cadena
    //*****************************************



    public static final String ErrorServicesPeticion = "['msm', '0']";
    public static final String ErrorServerPeticion = "['msm', '0000000000']";
    public static final String LocalInsertError = "Error al insertar los datos";
    public static final String LocalInsert = "Se han insertado los datos";
    public static final String LoginWell = " Felicidades, has ingresado ";
    public static final String FormError = " Errores en el formulario ";

    public static final String ProcesImgError = "Error al procesar la imagen";
    public static final String CarryImageError = "Ha ocurrido un error cargar la imagen";
    public static final String SendImageError = "Error al enviar la imagen";
    public static final String SendImageWell = "Se ha subido la imagen";





    //************************************
    //************************************Variables globales
    //************************************
    public static final int LIMIT_DISPLAY_CATEGORY = 5;
    public static final int NUM_COLUMNS_SMALL = 1;
    public static final int NUM_COLUMNS_LARGE = 2;
    public static final String TitleMenuHome = "Inicio";
    public static final String hintListFacultades = "Seleccione una facultad";
    public final static String EVENT_SEND_IDSOCKET = "saveIdSocket";
    public final static String EVENT_SEND_GET_MESSAGE = "new message";
    public final static int INDICADOR_MENSAJE_REMITENTE = 0;
    public final static int INDICADOR_MENSAJE_RECEPTOR = 1;
    public final static int INSTANCE_ITINERARIOS_ADMIN_CIRCLE = 0;
    public final static int SHOW_ITINERARIO_ASISTENCIA = 1;
    public final static int SHOW_ITINERARIO_EVIDENCIAS = 2;
    public final static int SHOW_ITINERARIO_CANCEL = 3;
    public final static String ITINERARIO_ASISTENCIA_TRUE = "111";
    public final static String ITINERARIO_ASISTENCIA_FALSE = "000";
    public static final int TIME_LIMIT_WAIT_SERVER = 10;
    public static final int TIME_LIMIT_WAIT_SERVER_LONG = 50;





}
