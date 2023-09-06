package com.tarea.crud;

public interface ProductoFactory {
    Producto crearProducto(String nombre, double precio, int dniUsuario);
}

