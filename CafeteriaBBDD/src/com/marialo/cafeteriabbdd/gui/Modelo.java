package com.marialo.cafeteriabbdd.gui;

import java.io.*;
import java.sql.*;
import java.time.LocalDate;
import java.util.Properties;

public class Modelo {
    private String ip;
    private String user;
    private String password;
    private String adminPassword;

    public Modelo() {
        getPropValues();
    }

    public String getIp() {
        return ip;
    }
    public String getUser() {
        return user;
    }
    public String getPassword() {
        return password;
    }
    public String getAdminPassword() {
        return adminPassword;
    }

    private Connection conexion;

    void conectar() {
        try {
            conexion = DriverManager.getConnection("jdbc:mysql://" + ip + ":3306/cafeteria_db", user, password);
        } catch (SQLException sqle) {
            try {
                conexion = DriverManager.getConnection(
                        "jdbc:mysql://" + ip + ":3306/", user, password);

                PreparedStatement statement = null;

                String code = leerFichero();
                String[] query = code.split("--");
                for (String aQuery : query) {
                    statement = conexion.prepareStatement(aQuery);
                    statement.executeUpdate();
                }
                assert statement != null;
                statement.close();

            } catch (SQLException | IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String leerFichero() throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader("cafeteria_db.sql")) ;
        String linea;
        StringBuilder stringBuilder = new StringBuilder();
        while ((linea = reader.readLine()) != null) {
            stringBuilder.append(linea);
            stringBuilder.append(" ");
        }

        return stringBuilder.toString();
    }

    void desconectar() {
        try {
            conexion.close();
            conexion = null;
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
    }

    // ========== CLIENTES ==========
    void insertarCliente(String nombre, String apellidos, String email, String telefono) {
        String sql = "INSERT INTO cliente (nombre, apellidos, email, telefono) VALUES (?, ?, ?, ?);";
        PreparedStatement sentencia = null;

        try {
            sentencia = conexion.prepareStatement(sql);
            sentencia.setString(1, nombre);
            sentencia.setString(2, apellidos);
            sentencia.setString(3, email);
            sentencia.setString(4, telefono);
            sentencia.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (sentencia != null) {
                try {
                    sentencia.close();
                } catch (SQLException sqle) {
                    sqle.printStackTrace();
                }
            }
        }
    }

    void modificarCliente(int id, String nombre, String apellidos, String email, String telefono) {
        String sql = "UPDATE cliente SET nombre = ?, apellidos = ?, email = ?, telefono = ? WHERE id_cliente = ?;";
        PreparedStatement sentencia = null;

        try {
            sentencia = conexion.prepareStatement(sql);
            sentencia.setString(1, nombre);
            sentencia.setString(2, apellidos);
            sentencia.setString(3, email);
            sentencia.setString(4, telefono);
            sentencia.setInt(5, id);
            sentencia.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (sentencia != null) {
                try {
                    sentencia.close();
                } catch (SQLException sqle) {
                    sqle.printStackTrace();
                }
            }
        }
    }

    void eliminarCliente(int id) {
        String sql = "DELETE FROM cliente WHERE id_cliente = ?;";
        PreparedStatement sentencia = null;

        try {
            sentencia = conexion.prepareStatement(sql);
            sentencia.setInt(1, id);
            sentencia.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (sentencia != null)
                try {
                    sentencia.close();
                } catch (SQLException sqle) {
                    sqle.printStackTrace();
                }
        }
    }

    ResultSet consultarClientes() throws SQLException {
        String sql = "SELECT id_cliente as 'ID', " +
                "nombre as 'Nombre', " +
                "apellidos as 'Apellidos', " +
                "email as 'Email', " +
                "fecha_registro as 'Fecha Registro', " +
                "telefono as 'Teléfono' " +
                "FROM cliente;";
        PreparedStatement sentencia = conexion.prepareStatement(sql);
        return sentencia.executeQuery();
    }

    public boolean existeCliente(String email) {
        String salesConsult = "SELECT existe_cliente(?)";
        PreparedStatement function;
        boolean existeEmail = false;

        try {
            function = conexion.prepareStatement(salesConsult);
            function.setString(1, email);
            ResultSet rs = function.executeQuery();
            rs.next();

            existeEmail = rs.getBoolean(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return existeEmail;
    }

    public int obtenerIDCliente(String email) {
        String sql = "SELECT id_cliente FROM cliente WHERE email = ?";
        PreparedStatement sentencia = null;
        int id = 0;

        try {
            sentencia = conexion.prepareStatement(sql);
            sentencia.setString(1, email);
            ResultSet rs = sentencia.executeQuery();
            rs.next();

            id = rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return id;
    }

    // ========== EMPLEADOS ==========
    void insertarEmpleado(String codigoEmpleado, String nombre, String apellidos, String dni, LocalDate fechaContratacion) {
        String sql = "INSERT INTO empleado (codigo_empleado, nombre, apellidos, dni, fecha_contratacion) VALUES (?, ?, ?, ?, ?);";
        PreparedStatement sentencia = null;

        try {
            sentencia = conexion.prepareStatement(sql);
            sentencia.setString(1, codigoEmpleado);
            sentencia.setString(2, nombre);
            sentencia.setString(3, apellidos);
            sentencia.setString(4, dni);
            sentencia.setDate(5, Date.valueOf(fechaContratacion));
            sentencia.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (sentencia != null) {
                try {
                    sentencia.close();
                } catch (SQLException sqle) {
                    sqle.printStackTrace();
                }
            }
        }
    }

    void modificarEmpleado(int id, String codigoEmpleado, String nombre, String apellidos, String dni, LocalDate fechaContratacion) {
        String sql = "UPDATE empleado SET codigo_empleado = ?, nombre = ?, apellidos = ?, dni = ?, fecha_contratacion = ? WHERE id_empleado = ?;";
        PreparedStatement sentencia = null;

        try {
            sentencia = conexion.prepareStatement(sql);
            sentencia.setString(1, codigoEmpleado);
            sentencia.setString(2, nombre);
            sentencia.setString(3, apellidos);
            sentencia.setString(4, dni);
            sentencia.setDate(5, Date.valueOf(fechaContratacion));
            sentencia.setInt(6, id);
            sentencia.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (sentencia != null) {
                try {
                    sentencia.close();
                } catch (SQLException sqle) {
                    sqle.printStackTrace();
                }
            }
        }
    }

    void eliminarEmpleado(int id) {
        String sql = "DELETE FROM empleado WHERE id_empleado = ?;";
        PreparedStatement sentencia = null;

        try {
            sentencia = conexion.prepareStatement(sql);
            sentencia.setInt(1, id);
            sentencia.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (sentencia != null)
                try {
                    sentencia.close();
                } catch (SQLException sqle) {
                    sqle.printStackTrace();
                }
        }
    }

    ResultSet consultarEmpleados() throws SQLException {
        String sql = "SELECT id_empleado as 'ID', " +
                "codigo_empleado as 'Código', " +
                "nombre as 'Nombre', " +
                "apellidos as 'Apellidos', " +
                "dni as 'DNI', " +
                "fecha_contratacion as 'Fecha Contratación' " +
                "FROM empleado;";
        PreparedStatement sentencia = conexion.prepareStatement(sql);
        return sentencia.executeQuery();
    }

    public boolean existeEmpleado(String codigo) {
        String salesConsult = "SELECT existe_empleado(?)";
        PreparedStatement function;
        boolean existeCodigo = false;

        try {
            function = conexion.prepareStatement(salesConsult);
            function.setString(1, codigo);
            ResultSet rs = function.executeQuery();
            rs.next();

            existeCodigo = rs.getBoolean(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return existeCodigo;
    }

    public int obtenerIDEmpleado(String codigoEmpleado) {
        String sql = "SELECT id_empleado FROM empleado WHERE codigo_empleado = ?";
        PreparedStatement sentencia = null;
        int id = 0;

        try {
            sentencia = conexion.prepareStatement(sql);
            sentencia.setString(1, codigoEmpleado);
            ResultSet rs = sentencia.executeQuery();
            rs.next();

            id = rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return id;
    }

    // ========== PRODUCTOS ==========
    void insertarProducto(String codigo, String nombre, String categoria, double precio) {
        String sql = "INSERT INTO producto (codigo, nombre, categoria, precio) VALUES (?, ?, ?, ?);";
        PreparedStatement sentencia = null;

        try {
            sentencia = conexion.prepareStatement(sql);
            sentencia.setString(1, codigo);
            sentencia.setString(2, nombre);
            sentencia.setString(3, categoria);
            sentencia.setDouble(4, precio);
            sentencia.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (sentencia != null) {
                try {
                    sentencia.close();
                } catch (SQLException sqle) {
                    sqle.printStackTrace();
                }
            }
        }
    }

    void modificarProducto(int id, String codigo, String nombre, String categoria, double precio) {
        String sql = "UPDATE producto SET codigo = ?, nombre = ?, categoria = ?, precio = ? WHERE id_producto = ?;";
        PreparedStatement sentencia = null;

        try {
            sentencia = conexion.prepareStatement(sql);
            sentencia.setString(1, codigo);
            sentencia.setString(2, nombre);
            sentencia.setString(3, categoria);
            sentencia.setDouble(4, precio);
            sentencia.setInt(5, id);
            sentencia.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (sentencia != null) {
                try {
                    sentencia.close();
                } catch (SQLException sqle) {
                    sqle.printStackTrace();
                }
            }
        }
    }

    void eliminarProducto(int id) {
        String sql = "DELETE FROM producto WHERE id_producto = ?;";
        PreparedStatement sentencia = null;

        try {
            sentencia = conexion.prepareStatement(sql);
            sentencia.setInt(1, id);
            sentencia.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (sentencia != null)
                try {
                    sentencia.close();
                } catch (SQLException sqle) {
                    sqle.printStackTrace();
                }
        }
    }

    ResultSet consultarProductos() throws SQLException {
        String sql = "SELECT id_producto as 'ID', " +
                "codigo as 'Código', " +
                "nombre as 'Nombre', " +
                "categoria as 'Categoría', " +
                "precio as 'Precio' " +
                "FROM producto;";
        PreparedStatement sentencia = conexion.prepareStatement(sql);
        return sentencia.executeQuery();
    }

    public boolean existeProducto(String codigo) {
        String salesConsult = "SELECT existe_producto(?)";
        PreparedStatement function;
        boolean existeCodigo = false;

        try {
            function = conexion.prepareStatement(salesConsult);
            function.setString(1, codigo);
            ResultSet rs = function.executeQuery();
            rs.next();

            existeCodigo = rs.getBoolean(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return existeCodigo;
    }

    // ========== PEDIDOS ==========
    void insertarPedido(int idCliente, int idEmpleado, double total, String tipoPago) {
        String sql = "INSERT INTO pedido (id_cliente, id_empleado, total, tipo_pago) VALUES (?, ?, ?, ?);";
        PreparedStatement sentencia = null;

        try {
            sentencia = conexion.prepareStatement(sql);
            sentencia.setInt(1, idCliente);
            sentencia.setInt(2, idEmpleado);
            sentencia.setDouble(3, total);
            sentencia.setString(4, tipoPago);
            sentencia.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (sentencia != null)
                try {
                    sentencia.close();
                } catch (SQLException sqle) {
                    sqle.printStackTrace();
                }
        }
    }

    ResultSet consultarPedidos() throws SQLException {
        String sql = "SELECT p.id_pedido as 'ID', " +
                "CONCAT(c.nombre, ' ', c.apellidos) as 'Cliente', " +
                "CONCAT(e.nombre, ' ', e.apellidos) as 'Empleado', " +
                "p.fecha_pedido as 'Fecha', " +
                "p.total as 'Total', " +
                "p.tipo_pago as 'Tipo Pago' " +
                "FROM pedido p " +
                "JOIN cliente c ON p.id_cliente = c.id_cliente " +
                "JOIN empleado e ON p.id_empleado = e.id_empleado";
        PreparedStatement sentencia = conexion.prepareStatement(sql);
        return sentencia.executeQuery();
    }

    public int obtenerUltimoIdPedido() {
        String sql = "SELECT LAST_INSERT_ID()";
        try (Statement stmt = conexion.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }


    // ========== DETALLES PEDIDO ==========
    void insertarDetallePedido(int idPedido, int idProducto, int cantidad, double subtotal) {
        String sql = "INSERT INTO detalle_pedido (id_pedido, id_producto, cantidad, subtotal) VALUES (?, ?, ?, ?)";
        PreparedStatement sentencia = null;
        try {
            sentencia = conexion.prepareStatement(sql);
            sentencia.setInt(1, idPedido);
            sentencia.setInt(2, idProducto);
            sentencia.setInt(3, cantidad);
            sentencia.setDouble(4, subtotal);
            sentencia.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    ResultSet consultarDetallesPedido(int idPedido) throws SQLException {
        String sql = "SELECT p.nombre as 'Producto', " +
                "d.cantidad as 'Cantidad', p.precio as 'Precio', d.subtotal as 'Subtotal'" +
                "FROM detalle_pedido d " +
                "JOIN producto p ON d.id_producto = p.id_producto " +
                "WHERE d.id_pedido = ? ";
        PreparedStatement sentencia = conexion.prepareStatement(sql);
        sentencia.setInt(1, idPedido);
        return sentencia.executeQuery();
    }


    // ========== CONFIGURACIÓN ==========
    private void getPropValues() {
        InputStream inputStream = null;
        try {
            Properties prop = new Properties();
            String propFileName = "config.properties";

            inputStream = new FileInputStream(propFileName);

            prop.load(inputStream);
            ip = prop.getProperty("ip");
            user = prop.getProperty("user");
            password = prop.getProperty("pass");
            adminPassword = prop.getProperty("admin");

        } catch (Exception e) {
            System.out.println("Exception: " + e);
        } finally {
            try {
                if (inputStream != null) inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    void setPropValues(String ip, String user, String pass, String adminPass) {
        try {
            Properties prop = new Properties();
            prop.setProperty("ip", ip);
            prop.setProperty("user", user);
            prop.setProperty("pass", pass);
            prop.setProperty("admin", adminPass);
            OutputStream out = new FileOutputStream("config.properties");
            prop.store(out, null);

        } catch (IOException ex) {
            ex.printStackTrace();
        }
        this.ip = ip;
        this.user = user;
        this.password = pass;
        this.adminPassword = adminPass;
    }
}
