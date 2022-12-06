
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {

    public static AtomicInteger attempt = new AtomicInteger(1);
    public static List<String> suitableCommits = new CopyOnWriteArrayList<>();

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(25);
        for (int hashStartsWithGuess = 0; hashStartsWithGuess <= 0xffff; hashStartsWithGuess++) {
            executorService.submit(new CommitChecker(Integer.toHexString(hashStartsWithGuess)));
        }
        try {
            executorService.shutdown();
            executorService.awaitTermination(1, TimeUnit.DAYS);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println(suitableCommits);
    }
}
