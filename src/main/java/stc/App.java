package stc;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Application launches single- and multi-threaded implementations of Conway's Game of Life
 * Initial configuration is read from file
 * Final configurations are stored in files
 * Filenames and an amount of steps are passed through arguments of console application
 * Application statically stores durations of execution of both implementations
 */
public class App {
    static long durationSingle = -1;
    static long durationMulti = -1;

    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException {
        if (args.length < 4) return;

        String fIn = args[0];
        int N = Integer.parseInt(args[1]);
        String fOut1 = args[2];
        String fOut2 = args[3];

        Life simpleLife = new Life(fIn);
        ExecutorService service = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() - 2);
        MultithreadedLife multithreadedLife = new MultithreadedLife(fIn, service);

        Instant simpleStart = Instant.now();
        for (int i = 0; i < N; i++)
            simpleLife.step();
        Instant simpleEnd = Instant.now();

        Instant multiStart = Instant.now();
        for (int i = 0; i < N; i++) {
            multithreadedLife.step();
        }
        Instant multiEnd = Instant.now();
        service.shutdown();

        durationSingle = Duration.between(simpleStart, simpleEnd).toMillis();
        durationMulti = Duration.between(multiStart, multiEnd).toMillis();

        simpleLife.saveToFile(fOut1);
        multithreadedLife.saveToFile(fOut2);
    }
}


