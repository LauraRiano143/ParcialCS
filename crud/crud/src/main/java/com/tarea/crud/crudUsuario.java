package com.tarea.crud;

import java.sql.Connection;
import utilities.Conexion;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
public class crudUsuario {
    private String nombre;
    private String email;
    private int idRol;

    public crudUsuario(String nombre, String email, int idRol) {
        this.nombre = nombre;
        this.email = email;
        this.idRol = idRol;
    }
    public void crearUsuario() throws SQLException {
        Connection cnn = Conexion.obtenerConexion();
        String query = "INSERT INTO usuario (nombre, email) VALUES (?, ?)";
        String queryAsignacion = "INSERT INTO asignacion (idRol, dni) VALUES (?, ?)";

        try {
            PreparedStatement pstmt = cnn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, nombre);
            pstmt.setString(2, email);
            pstmt.executeUpdate();

            int idUsuario = -1;
            var generatedKeys = pstmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                idUsuario = generatedKeys.getInt(1);
            } else {
                System.out.println("No se pudo obtener el ID del usuario.");
                return;
            }
            PreparedStatement pstmtAsignacion = cnn.prepareStatement(queryAsignacion);
            pstmtAsignacion.setInt(1, idRol);
            pstmtAsignacion.setInt(2, idUsuario);
            pstmtAsignacion.executeUpdate();
            System.out.println("-------------------------");
            System.out.println("USUARIO CREADO CORRECTAMENTE");
            System.out.println("-------------------------");

        } catch (SQLException sqle) {
            System.out.println("ERROR: " + sqle.getMessage());
        }
    }

    public static boolean buscarUsuarioPorDNI(int dniUsuario) throws SQLException {
        try (Connection cnn = Conexion.obtenerConexion()) {
            System.out.println("Conexión obtenida. Ejecutando consulta...");

            String query = "SELECT u.dni, u.nombre, u.email, r.nombre AS nombreRol FROM usuario u " +
                    "JOIN asignacion a ON u.dni = a.dni " +
                    "JOIN rol r ON a.idRol = r.idRol " +
                    "WHERE u.dni = ?";

            try (PreparedStatement pstmt = cnn.prepareStatement(query)) {
                pstmt.setInt(1, dniUsuario);
                ResultSet rs = pstmt.executeQuery();

                if (rs.next()) {
                    String nombre = rs.getString("nombre");
                    String email = rs.getString("email");
                    String nombreRol = rs.getString("nombreRol");

                    System.out.println("-------------------------");
                    System.out.println("USUARIO ENCONTRADO");
                    System.out.println("-------------------------");
                    System.out.println("DNI: " + dniUsuario);
                    System.out.println("Nombre: " + nombre);
                    System.out.println("Email: " + email);
                    System.out.println("Nombre del Rol: " + nombreRol);
                    System.out.println("-------------------------");
                    return true;
                } else {
                    System.out.println("-------------------------");
                    System.out.println("USUARIO CON DNI" + dniUsuario + " NO ENCONTRADO.");
                    System.out.println("-------------------------");
                    return false;
                }
            } catch (SQLException sqle) {
                System.out.println("ERROR AL BUSCAR USUARIO: " + sqle.getMessage());
                return false;
            }
        }
    }

    public static void modificarUsuario(int dniUsuario, String nuevoNombre, String nuevoEmail) {
        Connection cnn = null;
        PreparedStatement pstmt = null;
        try {
            cnn = Conexion.obtenerConexion();
            System.out.println("-------------------------");
            System.out.println("EJECUTANDO CONSULTA...");
            System.out.println("-------------------------");
            String sql = "UPDATE usuario SET nombre = ?, email = ? WHERE dni = ?";
            pstmt = cnn.prepareStatement(sql);
            pstmt.setString(1, nuevoNombre);
            pstmt.setString(2, nuevoEmail);
            pstmt.setInt(3, dniUsuario);
            int rowsUpdated = pstmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("-------------------------");
                System.out.println("USUARIO MODIFICADO CON ÉXITO...");
                System.out.println("-------------------------");
            } else {
                System.out.println("-------------------------");
                System.out.println("NO SE ENCONTRÓ EL USUARIO...");
                System.out.println("-------------------------");
            }
            System.out.println("-------------------------");
            System.out.println("CONSULTA COMPLETADA...");
            System.out.println("-------------------------");
        } catch (SQLException sqle) {
            System.out.println("ERROR AL MODIFICAR: " + sqle.getMessage());
        } finally {
            if (pstmt != null) {
                try {
                    pstmt.close();
                } catch (SQLException e) {
                    System.out.println("Error al cerrar el PreparedStatement: " + e.getMessage());
                }
            }
            if (cnn != null) {
                try {
                    cnn.close();
                } catch (SQLException e) {
                    System.out.println("Error al cerrar la conexión: " + e.getMessage());
                }
            }
        }
    }

    public void eliminarUsuarioPorDNI(int dniUsuario) {
        Connection cnn = null;
        PreparedStatement pstmtEliminar = null;

        try {
            cnn = Conexion.obtenerConexion();
            System.out.println("-------------------------");
            System.out.println("CONEXION OBTENIDA. EJECUTANDO...");
            System.out.println("-------------------------");
            String sql = "DELETE FROM usuario WHERE dni = ?";
            pstmtEliminar = cnn.prepareStatement(sql);
            pstmtEliminar.setInt(1, dniUsuario);
            int rowsDeleted = pstmtEliminar.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("-------------------------");
                System.out.println("USUARIO ELIMINADO CON EXITO");
                System.out.println("-------------------------");
            } else {
                System.out.println("-------------------------");
                System.out.println("NO SE ENCONTRÓ USUARIO");
                System.out.println("-------------------------");
            }
            System.out.println("-------------------------");
            System.out.println("CONSULTA COMPLETA");
            System.out.println("-------------------------");
        } catch (SQLException sqle) {
            System.out.println("ERROR AL ELIMINAR: " + sqle.getMessage());
        } finally {
            if (pstmtEliminar != null) {
                try {
                    pstmtEliminar.close();
                } catch (SQLException e) {
                    System.out.println("Error al cerrar el PreparedStatement: " + e.getMessage());
                }
            }
            if (cnn != null) {
                try {
                    cnn.close();
                } catch (SQLException e) {
                    System.out.println("Error al cerrar la conexión: " + e.getMessage());
                }
            }
        }
    }

    public static void mostrarTodosUsuarios() throws SQLException {
        Connection cnn = null;
        try {
            cnn = Conexion.obtenerConexion();
            System.out.println("CONEXION OBTENIDA. EJECUTANDO...");

            String query = "SELECT u.dni, u.nombre, u.email, r.nombre AS nombreRol FROM usuario u " +
                    "JOIN asignacion a ON u.dni = a.dni " +
                    "JOIN rol r ON a.idRol = r.idRol";

            PreparedStatement pstmt = cnn.prepareStatement(query);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                int dniUsuario = rs.getInt("dni");
                String nombre = rs.getString("nombre");
                String email = rs.getString("email");
                String nombreRol = rs.getString("nombreRol");

                System.out.println("DNI: " + dniUsuario);
                System.out.println("Nombre: " + nombre);
                System.out.println("Email: " + email);
                System.out.println("Nombre del Rol: " + nombreRol);
                System.out.println("-------------------------");
            }

            System.out.println("-------------------------");
            System.out.println("CONSULTA COMPLETADA");
            System.out.println("-------------------------");
        } catch (SQLException sqle) {
            System.out.println("ERROR AL OBTENER LA LISTA: " + sqle.getMessage());
        }
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setIdRol(int idRol) {
        this.idRol = idRol;
    }
}


