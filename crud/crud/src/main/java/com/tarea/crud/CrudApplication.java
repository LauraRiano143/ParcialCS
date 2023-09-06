package com.tarea.crud;

import java.sql.Connection;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import utilities.Conexion;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

@SpringBootApplication
public class CrudApplication {


	public static void main(String[] args) {

		try {
			Connection cnn = Conexion.obtenerConexion();
			System.out.println("Conectado exitosamente");
			ResultSet rs = null;
			SpringApplication.run(CrudApplication.class, args);
			Scanner scanner = new Scanner(System.in);
			crudUsuario miCrudUsuario = null;

			while (true){
				System.out.println("MENU PRINCIPAL INMOBILIARIA:");
				System.out.println("1. Crear usuario");
				System.out.println("2. Buscar usuario por ID");
				System.out.println("3. Modificar usuario");
				System.out.println("4. Eliminar usuario");
				System.out.println("5. Mostrar todos los usuarios");
				System.out.println("6. Crear producto");
				System.out.println("7. Salir");
				System.out.print("Seleccione una opción: ");
				int opcion = scanner.nextInt();

				switch (opcion){
					case 1:
						System.out.println("-------------------------");
						System.out.println("CREAR USUARIO");
						System.out.println("-------------------------");
						System.out.print("Ingrese el nombre de usuario: ");
						String nombre = scanner.next();
						System.out.print("Ingrese el email: ");
						String email = scanner.next();
						System.out.println("Seleccione el tipo de rol:");
						System.out.println("1. Administrador");
						System.out.println("2. Usuario");
						System.out.println("3. Visitante");
						System.out.print("Seleccione el tipo de rol: ");
						int tipoRol = scanner.nextInt();

						if (miCrudUsuario == null) {
							miCrudUsuario = new crudUsuario(nombre, email, tipoRol);
						} else {
							miCrudUsuario.setNombre(nombre);
							miCrudUsuario.setEmail(email);
							miCrudUsuario.setIdRol(tipoRol);
						}
						try {
							miCrudUsuario.crearUsuario();
							System.out.println("-------------------------");
							System.out.println("USUARIO CREADO");
							System.out.println("-------------------------");
						} catch (SQLException sqle) {
							System.out.println("Error al crear el usuario: " + sqle.getMessage());
						}
						break;
					case 2:
						System.out.println("-------------------------");
						System.out.println("BUSCAR USUARIO POR DNI");
						System.out.println("-------------------------");
						System.out.print("Ingrese el DNI del usuario a buscar: ");
						int dniUsuarioABuscar = scanner.nextInt();

						try {
							crudUsuario.buscarUsuarioPorDNI(dniUsuarioABuscar);
						} catch (SQLException sqle) {
							System.out.println("-------------------------");
							System.out.println("ERROR AL BUSCAR USUARIO: " + sqle.getMessage());
							System.out.println("-------------------------");
						}
						break;
					case 3:
						System.out.println("-------------------------");
						System.out.println("MODIFICAR USUARIO");
						System.out.println("-------------------------");
						try {
							System.out.print("Ingrese el DNI del usuario a modificar: ");
							int dniUsuarioAModificar = scanner.nextInt();
							if (miCrudUsuario.buscarUsuarioPorDNI(dniUsuarioAModificar)) {
								System.out.print("Ingrese el nuevo nombre: ");
								String nuevoNombre = scanner.next();
								System.out.print("Ingrese el nuevo email: ");
								String nuevoEmail = scanner.next();
								miCrudUsuario.modificarUsuario(dniUsuarioAModificar, nuevoNombre, nuevoEmail);
							} else {
								System.out.println("-----------------------------------------");
								System.out.println("USUARIO CON DNI: " + dniUsuarioAModificar + " NO ENCONTRADO.");
								System.out.println("-----------------------------------------");
							}
						} catch (SQLException sqle) {
							System.out.println("ERROR AL MODIFICAR: " + sqle.getMessage());
						}
						break;
					case 4:
						System.out.println("-------------------------");
						System.out.println("ELIMINAR USUARIO");
						System.out.println("-------------------------");
						System.out.print("Ingrese el DNI del usuario a eliminar: ");
						int dniUsuarioAEliminar = scanner.nextInt();
						miCrudUsuario.eliminarUsuarioPorDNI(dniUsuarioAEliminar);
						break;
					case 5:
						System.out.println("-------------------------");
						System.out.println("MOSTRAR TODOS LOS USUARIOS");
						System.out.println("-------------------------");
						try {
							crudUsuario.mostrarTodosUsuarios();
						} catch (SQLException sqle) {
							System.out.println("------------------------------------");
							System.out.println("ERROR AL MOSTRAR USUARIOS: " + sqle.getMessage());
							System.out.println("------------------------------------");
						}
						break;
					case 6:
						System.out.println("_________________________");
						System.out.println("REGISTRAR PRODUCTO");
						System.out.println("_________________________");

						System.out.print("Ingrese el ID del usuario registrado: ");
						int dniUsuario= scanner.nextInt();

						try {
							// Intenta buscar al usuario por su DNI
							boolean usuarioEncontrado = crudUsuario.buscarUsuarioPorDNI(dniUsuario);

							if (!usuarioEncontrado) {
								// Si el usuario no se encuentra, muestra un mensaje de error y sale del caso
								System.out.println("Usuario con ID " + dniUsuario + " no encontrado.");
								break;
							}

							System.out.print("¿El producto tiene descuento? (Sí: 1 / No: 2): ");
							int tieneDescuento = scanner.nextInt();
							ProductoFactory productoFactory;

							if (tieneDescuento == 1) {
								productoFactory = new ProductoDescuento();
							} else if (tieneDescuento == 2) {
								productoFactory = new ProductoNormal();
							} else {
								System.out.println("Opción de descuento no válida.");
								break;
							}

							System.out.print("Ingrese el nombre del producto: ");
							String nombreProducto = scanner.next();

							System.out.print("Ingrese el precio del producto: ");
							double precio = scanner.nextDouble();
							Producto producto = productoFactory.crearProducto(nombreProducto, precio, dniUsuario);

							System.out.println("Producto creado: " + producto.getNombre() + ", Precio: " + producto.getPrecio());
						} catch (SQLException sqle) {
							System.out.println("-------------------------");
							System.out.println("ERROR AL BUSCAR USUARIO: " + sqle.getMessage());
							System.out.println("-------------------------");
						}
						break;
					case 7:
						System.out.println("-------------------------");
						System.out.println("SALIENDO");
						System.out.println("-------------------------");
						scanner.close();
						System.exit(0);
						break;
					default:
						System.out.println("-------------------------");
						System.out.println("OPCION INVALIDA");
						System.out.println("-------------------------");
						break;
				}
			}

		} catch (SQLException e) {
			System.out.println("----------------------------");
			System.out.println("ERROR AL OBTENER CONEXION: " + e.getMessage());
			System.out.println("----------------------------");
		}

	}

}
