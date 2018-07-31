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

public class D {
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
        new D().solve();
    }

    private BufferedScanner mReader;
    private PrintStream mWriter;

    private D() throws IOException {
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

        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = mReader.nextInt();
        }

        String s = mReader.next();
        int[] b = new int[n];
        for (int i = 0; i < n; i++) {
            b[i] = s.charAt(i) == '1' ? 1 : 0;
        }

        int l = -1_000_000_000;
        int r = 1_000_000_000;

        int it = 4;
        while (it < n) {
            if (b[it] == 1 && check(b, it, 0)) {
                l = getMax(a, it, l);
            } else if (b[it] == 1 && check(b, it, 1)) {
                // do nothing, hope a...<=r
            } else if (b[it] == 1 && b[it - 1] != 1) {
                // WTF
            } else if (b[it] == 0 && check(b, it, 1)) {
                r = getMin(a, it, r);
            } else if (b[it] == 0 && check(b, it, 0)) {
                // do nothing, hope a...>=l
            } else if (b[it] == 0 && b[it - 1] != 0) {
                // WTF
            }
            it++;
        }

        System.out.println(l + " " + r);
    }

    private boolean check(int[] b, int to, int need) {
        for (int i = to - 4; i < to; i++) {
            if (b[i] != need) {
                return false;
            }
        }
        return true;
    }

    private int getMax(int[] a, int to, int initial) {
        int result = initial;
        for (int i = to - 4; i <= to; i++) {
            result = Math.max(result, a[i] + 1);
        }
        return result;
    }

    private int getMin(int[] a, int to, int initial) {
        int result = initial;
        for (int i = to - 4; i <= to; i++) {
            result = Math.min(result, a[i] - 1);
        }
        return result;
    }
}