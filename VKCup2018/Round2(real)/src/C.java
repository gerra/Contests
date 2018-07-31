import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.StringTokenizer;

public class C {
    private static class BufferedScanner implements Closeable {
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

        @Override
        public void close() throws IOException {
            mReader.close();
        }
    }

    private static InputStream getInputStream() throws IOException {
        InputStream inputStream;
        if (System.getProperty("ONLINE_JUDGE") != null) {
            inputStream = System.in;
        } else {
            File inputFile = new File("input.txt");
            if (!inputFile.exists()) {
                //noinspection ResultOfMethodCallIgnored
                inputFile.createNewFile();
                throw new FileNotFoundException("File wasn't found, but now is created");
            }
            inputStream = new FileInputStream(inputFile);
        }
        return new BufferedInputStream(inputStream);
    }

    public static void main(String[] args) throws IOException {
        try (BufferedScanner in = new BufferedScanner(getInputStream());
             PrintWriter out = new PrintWriter(System.out)) {
            new C(in, out).solve();
            out.flush();
        }
    }

    private BufferedScanner in;
    private PrintWriter out;

    private C(BufferedScanner in, PrintWriter out) throws IOException {
        this.in = in;
        this.out = out;
    }

    private void solve() throws IOException {
        int n = in.nextInt();
        int[] m = new int[n + 1];
        int[] e = new int[n + 1];
        int[] ec = new int[n + 1];
        for (int i = 1; i <= n; i++) {
            m[i] = in.nextInt();
            e[i] = i - m[i] - 1;
        }
        ec[n] = e[n];
        for (int i = n - 1; i >= 1; i--) {
            ec[i] = Math.min(ec[i + 1], e[i]);
        }
        int notset = 0;
        int[] d = new int[n + 1];
        for (int i = 1; i <= n; i++) {
            if (ec[i] - notset > 0) {
                notset++;
            }
            d[i] = (i - notset) - 1 - m[i];
        }
        long res = 0;
        for (int i = 1; i <= n; i++) {
            res += d[i];
        }
        System.out.println(res);
    }
}