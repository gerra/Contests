import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.Arrays;
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

    private static int[][] FD = new int[][] {
            {1, 0, 0},
            {0, 1, 0},
            {0, 0, 1}
    };

    private static int[][] TD = new int[][] {
            {-1, 0, 0},
            {0, -1, 0},
            {0, 0, -1}
    };

    private BufferedScanner mReader;
    private PrintStream mWriter;
    int n, m, k;
    boolean[][][] g;

    private D() throws IOException {
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
        n = mReader.nextInt();
        m = mReader.nextInt();
        k = mReader.nextInt();

        g = new boolean[n][m][k];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                String s = mReader.next();
                assert s.length() == k;
                for (int z = 0; z < k; z++) {
                    g[i][j][z] = s.charAt(z) == '1';
                }
            }
        }

        int[] from = new int[3];
        int[] to = new int[3];

        int result = 0;

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                for (int z = 0; z < k; z++) {
                    if (!g[i][j][z]) {
                        continue;
                    }

                    int cur[] = new int[] {i, j, z};
                    boolean isBad = false;
                    for (int[] aFD : FD) {
                        go(cur, aFD, to);
                        if (!isValid(to)) {
                            continue;
                        }
                        for (int[] aTD : TD) {
                            go(cur, aTD, from);
                            if (!isValid(from)) {
                                continue;
                            }

                            g[i][j][z] = false;
                            if (!check(from, to, 0)) {
                                isBad = true;
                            }
                            g[i][j][z] = true;
                            if (isBad) {
                                break;
                            }
                        }
                        if (isBad) {
                            break;
                        }
                    }

                    if (isBad) {
                        result++;
                    }

                }
            }
        }
        mWriter.println(result);
    }

    private void go(int[] cur, int[] dir, int[] dst) {
        assert cur.length == dir.length;
        assert dst.length >= cur.length;

        for (int i = 0; i < dir.length; i++) {
            dst[i] = cur[i] + dir[i];
        }
    }

    private boolean isValid(int[] cur) {
        assert cur.length == 3;

        return isValid(cur[0], n)
               && isValid(cur[1], m)
               && isValid(cur[2], k)
               && g[cur[0]][cur[1]][cur[2]];
    }

    private boolean isValid(int c, int max) {
        return c >= 0 && c < max;
    }

    private boolean check(int[] from, int[] to, int step) {
        if (Arrays.equals(from, to)) {
            return true;
        }

        int[] newFrom = new int[from.length];
        for (int[] aFD : FD) {
            go(from, aFD, newFrom);
            if (isValid(newFrom) && step < 2 && check(newFrom, to, step + 1)) {
                return true;
            }
        }
        return false;
    }
}