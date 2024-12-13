import java.net.*;
import java.util.Scanner;

public class UDPApp {
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
        byte[] buffer = new byte[1024];
        try (DatagramSocket serverSocket = new DatagramSocket(port)) {
            System.out.println("Сервер запущено на порту " + port);
            while (true) {
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                serverSocket.receive(packet);
                String receivedMessage = new String(packet.getData(), 0, packet.getLength());
                System.out.println("Отримано повідомлення: " + receivedMessage);
                if ("exit".equalsIgnoreCase(receivedMessage.trim())) {
                    System.out.println("Сервер завершив роботу.");
                    break;
                }
            }
        } catch (Exception e) {
            System.err.println("Помилка сервера: " + e.getMessage());
        }
    }
    private static void startClient(String serverAddress, int serverPort) {
        try (DatagramSocket clientSocket = new DatagramSocket();
             Scanner scanner = new Scanner(System.in)) {
            System.out.println("Клієнт готовий. Введіть повідомлення (для виходу введіть 'exit'):");
            while (true) {
                System.out.print("> ");
                String message = scanner.nextLine();
                byte[] buffer = message.getBytes();
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length, InetAddress.getByName(serverAddress), serverPort);
                clientSocket.send(packet);
                if ("exit".equalsIgnoreCase(message.trim())) {
                    System.out.println("Клієнт завершив роботу.");
                    break;
                }
            }
        } catch (Exception e) {
            System.err.println("Помилка клієнта: " + e.getMessage());
        }
    }
}