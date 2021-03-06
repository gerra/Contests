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
import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.StringTokenizer;

public class E {
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
            new E(in, out).solve();
            out.flush();
        }
    }

    private BufferedScanner in;
    private PrintWriter out;

    private E(BufferedScanner in, PrintWriter out) throws IOException {
        this.in = in;
        this.out = out;
    }

    private void solve() throws IOException {
        int n = in.nextInt();
        LinkedList<Integer> a = new LinkedList<>();
        for (int i = 0; i < n; i++) {
            a.add(in.nextInt());
        }
        ListIterator<Integer> iterator = a.listIterator();
        while (iterator.hasNext()) {
            int cur = iterator.next();
            if (iterator.hasNext()) {
                int nxt = iterator.next();
                if (cur == nxt) {
                    iterator.remove();
                    iterator.previous();
                    iterator.remove();
                    iterator.add(cur + 1);
                    if (iterator.hasPrevious()) {
                        iterator.previous();
                    }
                }
                if (iterator.hasPrevious()) {
                    iterator.previous();
                }
            }
        }

        out.println(a.size());
        for (Integer x : a) {
            out.print(x + " ");
        }
        out.println();
    }
}