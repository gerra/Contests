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
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeSet;

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
        int n = mReader.nextInt();

        long[] vs = new long[n];
        long[] ts = new long[n];
        long[] tsums = new long[n];

        for (int i = 0; i < n; i++) {
            vs[i] = mReader.nextLong();
        }

        for (int i = 0; i < n; i++) {
            ts[i] = mReader.nextLong();
            tsums[i] = (i != 0 ? tsums[i - 1] : 0) + ts[i];
        }

        long[] minus = new long[n + 1];
        int[] days = new int[n + 1];
        int tots = 0;
        for (int i = 0; i < n; i++) {
            Pair pair = find(vs[i], i, ts, tsums);

            days[pair.i]++;
            minus[pair.i] += pair.total;

            tots++;
            tots -= days[i];

            long res = (long) tots * ts[i] + minus[i];
            System.out.print(res + " ");
        }
        System.out.println();
    }

    Pair find(long v, int from, long[] ts, long[] tsums) {
        int l = from, r = tsums.length - 1;
        while (l <= r) {
            int i = (l + r) / 2;
            long total = tsums[i] - (from == 0 ? 0 : tsums[from - 1]);
            if (total == v) {
                return new Pair(i + 1, 0);
            }

            if (total < v) {
                l = i + 1;
            } else {
                r = i - 1;
            }
        }

        if (l != tsums.length) {
            long total = tsums[l] - (from == 0 ? 0 : tsums[from - 1]);
            return new Pair(l, ts[l] - total + v);
        } else {
            return new Pair(l, 0);
        }
    }

    private static class Pair {
        int i;
        long total;

        Pair(int i, long total) {
            this.i = i;
            this.total = total;
        }
    }
}