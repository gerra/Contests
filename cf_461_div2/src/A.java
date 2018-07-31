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
            File inputFile = new File("in.txt");
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
        int y = mReader.nextInt();
        int x = mReader.nextInt();

        if (x <= 0) {
            System.out.println("No");
            return;
        }

        int k = x - 1;
        if (k == 0 && y == 0 || y >= k && (y - k) % 2 == 0 && k > 0) {
            System.out.println("Yes");
        } else {
            System.out.println("No");
        }
    }
}