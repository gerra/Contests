import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.StringTokenizer;

public class B {
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
        new B().solve();
    }

    private BufferedScanner mReader;
    private PrintStream mWriter;

    private B() throws IOException {
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
        long n = mReader.nextLong();
        long k = mReader.nextLong();
        long a = mReader.nextLong();
        long b = mReader.nextLong();

        if (k == 1) {
            System.out.println((n - 1) * a);
        } else {
            long res = 0;
            while (n != 1) {
                if (n >= k) {
                    res += (n % k) * a;
                    n -= n % k;
                } else {
                    res += (n - 1) * a;
                    n = 1;
                }

                if (n != 1) {
                    long nextN = n / k;
                    res += Math.min((n - nextN) * a, b);
                    n = nextN;
                }
            }
            System.out.println(res);
        }
    }
}