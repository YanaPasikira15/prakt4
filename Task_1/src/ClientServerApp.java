import java.io.*;
import java.net.*;
import java.util.Scanner;

public class ClientServerApp {
    public static void main(String[] args) {
        Thread serverThread = new Thread(() -> startServer(12345));
        serverThread.start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        startClient("localhost", 12345);
    }
    private static void startServer(int port) {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Сервер запущено на порту " + port);
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Клієнт підключився: " + clientSocket.getInetAddress());
                try (BufferedReader input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                     PrintWriter output = new PrintWriter(clientSocket.getOutputStream(), true)) {
                    String message;
                    while ((message = input.readLine()) != null) {
                        System.out.println("Отримано від клієнта: " + message);
                        output.println("Від сервера: " + message);
                        if ("exit".equalsIgnoreCase(message)) {
                            System.out.println("Клієнт відключився.");
                            break;
                        }
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Помилка сервера: " + e.getMessage());
        }
    }
    private static void startClient(String hostname, int port) {
        try (Socket socket = new Socket(hostname, port)) {
            System.out.println("Підключено до сервера");
            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter output = new PrintWriter(socket.getOutputStream(), true);
            Scanner scanner = new Scanner(System.in);
            String message;
            System.out.println("Введіть повідомлення (для виходу введіть 'exit'):");
            while (true) {
                System.out.print("> ");
                message = scanner.nextLine();
                output.println(message);
                String response = input.readLine();
                System.out.println(response);
                if ("exit".equalsIgnoreCase(message)) {
                    System.out.println("Відключення від сервера.");
                    break;
                }
            }
        } catch (IOException e) {
            System.err.println("Помилка клієнта: " + e.getMessage());
        }
    }
}