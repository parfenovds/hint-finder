import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class CommitChecker implements Runnable {
    private static final String REPOSITORY_URL = "https://github.com/gooverdian/hh-school-git-crash-course/commit/";
    private static final String WHAT_SHOULD_BE_FOUND_IN_COMMIT = "???";
    private static final String KEY = "INSERT_YOUR_GITHUB_TOKEN_HERE"; //token must be clear (with no privileges) to avoid 5k GitHub limits!
    private final String hashStartsWithGuess;

    public CommitChecker(String hashStartsWithGuess) {
        this.hashStartsWithGuess = hashStartsWithGuess;
    }

    @Override
    public void run() {
        HttpClient client = HttpClient.newHttpClient();
        String authorization = "Bearer {" + KEY + "}";
        HttpRequest request = HttpRequest.newBuilder()
                .setHeader("Authorization", authorization)
                .uri(URI.create(REPOSITORY_URL + hashStartsWithGuess))
                .GET()
                .build();
        try {
            System.out.println("Attempt number: " + Main.attempt.incrementAndGet());
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 404 && response.body().contains(WHAT_SHOULD_BE_FOUND_IN_COMMIT)) {
                System.out.println("Found a suitable commit: " + hashStartsWithGuess);
                Main.suitableCommits.add(hashStartsWithGuess);
                System.out.println("list now consists of: " + Main.suitableCommits);
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
