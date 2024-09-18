package com.codigo.exameng6.utils;

public class Constants {
    public static final Integer OK_DNI_CODE = 200;
    public static final String OK_DNI_MESS = "EJECUTADO SIN PROBLEMAS";
    public static final Integer ERROR_DNI_CODE = 500;
    public static final String ERROR_DNI_MESS = "ERROR CON EL DNI";
    public static final Integer STATUS_ACTIVE = 1;
    public static final Integer STATUS_INACTIVE = 0;
    public static final Integer ERROR_CODE_LIST_EMPTY= 204;
    public static final String ERROR_MESS_LIST_EMPTY = "NO HAY REGISTROS";
    public static final String REDIS_KEY_API_USERS = "MS:USERS:DNI:";
    public static final Integer REDIS_EXP = 5;
    public static final Integer ERROR_CODE_400 = 400;
    public static final String ERROR_MESS_400 = "Request inválido";
    public static final Integer ERROR_TRX_CODE = 409;
    public static final String ERROR_TRX_MESS = "ERROR DURANTE LA TRANSACCION ";
    public static final Integer ERROR_CODE_401 = 401;
    public static final String ERROR_MESS_401 = "Autenticación fallida. El token proporcionado no es válido o ha expirado.";
}
