package com.co.edu.cun.www1104379214.bienestarcun;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by root on 31/10/15.
 */
public class CodMessajes {
    //*************************************
    //*************************************Constantes
    //*************************************

    public static final String TAG1 = "CONTROL";

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

    //TODO: agregar una identificacion para que el usuario la elija y asi pertenezca a la sede que seleccione (las sedes deben ingresarse manualmente)

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
            msmServices.put("0000000000", "Error en el servidor x_x, trabajaremos en ello");

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

    public static final String TitleMenuHome = "Inicio";
    public static final String hintListFacultades = "Selecciones una facultad";


}
