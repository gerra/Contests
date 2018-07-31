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

public class C {
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
        new C().solve();
    }

    private BufferedScanner mReader;
    private PrintStream mWriter;

    private C() throws IOException {
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
        int t = mReader.nextInt();
        for (int i = 0; i < t; i++) {
            int x = mReader.nextInt();

            boolean f = false;
            for (int n = 1; n <= 50_000; n++) {
                if (n * n < x) {
                    continue;
                }
                int l = (int) Math.sqrt(n * n - x);
                if (l != 0 && l * l == n * n - x) {
                    int m1 = n / l;
                    int m2 = (n + l - 1) / l;
                    if (n / m1 == l) {
                        System.out.println(n + " " + m1);
                        f = true;
                        break;
                    } else if (n / m2 == l) {
                        System.out.println(n + " " + m2);
                        f = true;
                        break;
                    }
                }
            }
            if (!f) {
                System.out.println(-1);
            }

            if (f) {
//                System.out.println(x);
            }
        }
    }
}