import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class ServidorSocket {
    public static void main(String[] args) {
        // Configuración de la conexión a la base de datos
        String url = "jdbc:mysql://192.168.0.12:3306/sistemavotacion";
        String usuario = "alumno";
        String contraseña = "contrasena";
        String puerto_del_servidor = "1234";

        try {
            // Cargar el controlador JDBC de MySQL
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Crear un socket para escuchar conexiones entrantes
            ServerSocket serverSocket = new ServerSocket(puerto_del_servidor);

            while (true) {
                // Esperar a que un cliente se conecte
                Socket socket = serverSocket.accept();

                try {
                    // Conectarse a la base de datos
                    Connection conexion = DriverManager.getConnection(url, usuario, contraseña);

                    // Recibir la instrucción SQL del cliente
                    DataInputStream dis = new DataInputStream(socket.getInputStream());
                    String sql = dis.readUTF();

                    // Ejecutar la instrucción SQL
                    Statement statement = conexion.createStatement();
                    int filasAfectadas = statement.executeUpdate(sql);

                    // Enviar la respuesta al cliente
                    String respuesta = filasAfectadas > 0 ? "Operación exitosa" : "Error al ejecutar la operación";
                    DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
                    dos.writeUTF(respuesta);

                    // Cerrar la conexión con la base de datos y el cliente
                    conexion.close();
                    socket.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
