import java.net.http.*;
import java.net.http.HttpResponse.BodyHandlers;
import java.net.URI;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Map;

public class HttpClientApp {
    public static void main(String[] args) {
        try {
            System.out.println("Виконання GET-запиту...");
            String getUrl = "https://api.openweathermap.org/data/2.5/weather?q=Kyiv&appid=YOUR_API_KEY&units=metric";
            sendGetRequest(getUrl);
            System.out.println("\nВиконання POST-запиту...");
            String postUrl = "https://jsonplaceholder.typicode.com/posts";
            String jsonData = """
                    {
                        "title": "foo",
                        "body": "bar",
                        "userId": 1
                    }
                    """;
            sendPostRequest(postUrl, jsonData);
        } catch (Exception e) {
            System.err.println("Помилка: " + e.getMessage());
        }
    }
    private static void sendGetRequest(String url) {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(url))
                    .GET()
                    .build();
            HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
            printResponse(response);
        } catch (URISyntaxException | IOException | InterruptedException e) {
            System.err.println("Помилка під час виконання GET-запиту: " + e.getMessage());
        }
    }
    private static void sendPostRequest(String url, String jsonData) {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(url))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonData))
                    .build();
            HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
            printResponse(response);
        } catch (URISyntaxException | IOException | InterruptedException e) {
            System.err.println("Помилка під час виконання POST-запиту: " + e.getMessage());
        }
    }
    private static void printResponse(HttpResponse<String> response) {
        System.out.println("Статус-код: " + response.statusCode());
        System.out.println("Заголовки:");
        for (Map.Entry<String, java.util.List<String>> header : response.headers().map().entrySet()) {
            System.out.println(header.getKey() + ": " + String.join(", ", header.getValue()));
        }
        System.out.println("Тіло відповіді:");
        System.out.println(response.body());
    }
}