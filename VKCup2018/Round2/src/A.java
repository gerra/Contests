import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class A {
    private static class BufferedScanner {
        BufferedReader mReader;
        StringTokenizer mTokenizer;

        BufferedScanner(InputStream inputStream) {
            mReader = new BufferedReader(new InputStreamReader(inputStream));
        }

        String next() {
            while (mTokenizer == null || !mTokenizer.hasMoreElements()) {
                String line = readLine();
                if (line != null) {
                    mTokenizer = new StringTokenizer(line);
                } else {
                    break;
                }
            }
            return mTokenizer != null ? mTokenizer.nextToken() : null;
        }

        int nextInt() {
            return Integer.parseInt(next());
        }

        long nextLong() {
            return Long.parseLong(next());
        }

        double nextDouble() {
            return Double.parseDouble(next());
        }

        String nextLine() {
            return readLine();
        }

        private String readLine() {
            try {
                return mReader.readLine();
            } catch (IOException ignore) {
                return null;
            }
        }
    }

    public static void main(String[] args) throws IOException {
        new A().solve();
    }

    private BufferedScanner mReader;
    private PrintStream mWriter;

    private A() throws IOException {
        InputStream actualInputStream;
        if (System.getProperty("ONLINE_JUDGE") != null) {
            actualInputStream = System.in;
        } else {
            File inputFile = new File("input.txt");
            if (!inputFile.exists()) {
                //noinspection ResultOfMethodCallIgnored
                inputFile.createNewFile();
                throw new FileNotFoundException("File wasn't found, but now is created");
            }
            actualInputStream = new FileInputStream(inputFile);
        }
        mReader = new BufferedScanner(new BufferedInputStream(actualInputStream));
        mWriter = System.out;
    }

    private void solve() throws IOException {
        int x2 = mReader.nextInt();

        boolean[] notPrimes = new boolean[x2 + 1];
        List<Integer> primes = new ArrayList<>();
        for (int i = 2; i <= x2; i++) {
            if (notPrimes[i]) {
                continue;
            }
            primes.add(i);
            if ((long) i * i <= x2) {
                for (int j = i * i; j <= x2; j += i) {
                    notPrimes[j] = true;
                }
            }
        }

        System.out.println(solve(x2, primes));
    }

    private int solve(int x2, List<Integer> primes) {
        int maxPrime = 0;
        for (int j = primes.size() - 1; j >= 0; j--) {
            if (x2 % primes.get(j) == 0) {
                maxPrime = primes.get(j);
                break;
            }
        }

        int x0 = x2;
        int l = x2 - maxPrime + 1;
        for (int prime : primes) {
            int min = (l + prime - 1) / prime * prime;
            for (int x1 = min; x1 <= x2; x1 += prime) {
                if (x1 - prime + 1 >= 3) {
                    x0 = Math.min(x0, x1 - prime + 1);
                }
            }
        }

        return x0;
    }
}