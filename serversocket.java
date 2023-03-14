import java.io.*;
import java.net.*;
import java.sql.*;

public class ServidorSocket {
    public static void main(String[] args) {
        try {
            // Registro del controlador JDBC
            Class.forName("nombre_del_controlador_jdbc");

            // Creación del servidor socket
            ServerSocket serverSocket = new ServerSocket(puerto_servidor);

            // Escucha de conexiones entrantes
            System.out.println("Servidor socket iniciado en el puerto " + puerto_servidor);
            Socket socket = serverSocket.accept();
            System.out.println("Cliente conectado desde " + socket.getInetAddress());

            // Recepción de la consulta SQL
            InputStream inputStream = socket.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String consultaSql = bufferedReader.readLine();
            System.out.println("Consulta recibida: " + consultaSql);

            // Conexión al servidor de base de datos
            Connection conexion = DriverManager.getConnection(url_bd, usuario_bd, password_bd);

            // Ejecución de la consulta SQL en el servidor de base de datos
            Statement statement = conexion.createStatement();
            statement.executeUpdate(consultaSql);
            System.out.println("Consulta ejecutada correctamente en el servidor de base de datos");

            // Cierre de los recursos
            statement.close();
            conexion.close();
            bufferedReader.close();
            inputStream.close();
            socket.close();
            serverSocket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
