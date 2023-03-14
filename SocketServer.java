import java.net.*;
import java.io.*;
import java.sql.*;

public class SocketServer {
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;
    private Connection connection;
    private Statement statement;

    public void start(int port) throws IOException {
        // Establecer conexión con la base de datos
        connection = DriverManager.getConnection("jdbc:mysql://192.168.0.12:3306/votos", "alumno", "contrasena");
        statement = connection.createStatement();

        // Establecer el socket y esperar a que un cliente se conecte
        serverSocket = new ServerSocket(port);
        clientSocket = serverSocket.accept();

        // Establecer los flujos de entrada y salida del socket
        out = new PrintWriter(clientSocket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

        String inputLine;

        // Leer los datos enviados por el cliente y almacenarlos en la base de datos
        while ((inputLine = in.readLine()) != null) {
            statement.executeUpdate(inputLine);//"INSERT INTO dulces VALUES ('" + inputLine + "')"
            out.println("OK");
        }

        // Cerrar los flujos de entrada y salida y la conexión con la base de datos
        out.close();
        in.close();
        connection.close();
        clientSocket.close();
        serverSocket.close();
    }

    public static void main(String[] args) throws IOException {
        SocketServer server = new SocketServer();
        server.start(1234);
    }
}
