package com.tarea.crud;

import java.sql.Connection;
import utilities.Conexion;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ProductoNormal implements ProductoFactory {
    @Override
    public Producto crearProducto(String nombre, double precio, int dniUsuario) {
        Producto producto = new Producto(nombre, precio);

        try (Connection connection = Conexion.obtenerConexion()) {
            // Insertar el producto en la tabla 'producto'
            String insertProductoSQL = "INSERT INTO producto (nombre, precio, idTipo) VALUES (?, ?, ?)";

            try (PreparedStatement preparedStatement = connection.prepareStatement(insertProductoSQL, PreparedStatement.RETURN_GENERATED_KEYS)) {
                preparedStatement.setString(1, nombre);
                preparedStatement.setDouble(2, precio);
                preparedStatement.setInt(3, 2);

                preparedStatement.executeUpdate();

                int idProducto = -1;
                var generatedKeys = preparedStatement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    idProducto = generatedKeys.getInt(1);
                } else {
                    System.out.println("No se pudo obtener el ID del producto.");
                    return null;
                }

                String insertSeleccionSQL = "INSERT INTO seleccion (IdUsuario, IdProducto) VALUES (?, ?)";
                try (PreparedStatement preparedStatementSeleccion = connection.prepareStatement(insertSeleccionSQL)) {
                    preparedStatementSeleccion.setInt(1, dniUsuario);
                    preparedStatementSeleccion.setInt(2, idProducto);
                    preparedStatementSeleccion.executeUpdate();
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al conectar a la base de datos o al insertar el producto: " + e.getMessage());
        }

        return producto;
    }

}
