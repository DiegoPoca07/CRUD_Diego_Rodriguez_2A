package modelo

import java.sql.Connection
import java.sql.DriverManager

class ClaseConexion {

    fun cadenaConexion():Connection? {

        try{
         val url = "jdbc:oracle:thin:@10.10.1.126:1521:xe"
            val usuario = "system"
            val contrasena = "desarrollo"

            val connection = DriverManager.getConnection(url, usuario, contrasena)
            return connection
        }catch (e: Exception){
            println("Este es el error: $e")
            return null
        }

    }

}