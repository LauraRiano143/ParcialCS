package utilities;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {

    private static Connection conexion = null;

    private static Connection conectar() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
            return DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/clasesroles", "root", "");
        } catch (SQLException e) {
            throw new SQLException("Error de MySQL: " + e.getMessage());
        } catch (Exception e) {
            throw new SQLException("Error inesperado: " + e.getMessage());
        }
    }

    public static Connection obtenerConexion() throws SQLException {
        /*if (conexion == null) {
            conexion = conectar();
        }
        return conexion;*/

        return conectar();
    }
    public static void cerrarConexion() {
        if (conexion != null) {
            try {
                conexion.close();
            } catch (SQLException e) {
                System.out.println("Error al cerrar la conexión: " + e.getMessage());
            }
        }
    }


}
