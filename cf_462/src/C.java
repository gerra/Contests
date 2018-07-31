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
        int n = mReader.nextInt();

        int a[] = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = mReader.nextInt() - 1;
        }

        int dpDown[][][] = new int[n][n + 1][2];
        for (int l = 0; l < n; l++) {
            for (int r = l + 1; r <= n; r++) {
                if (a[r - 1] == 0) {
                    dpDown[l][r][0] = Math.max(dpDown[l][r - 1][0], dpDown[l][r - 1][1]) + 1;
                    dpDown[l][r][1] = dpDown[l][r - 1][1];
                } else {
                    dpDown[l][r][0] = dpDown[l][r - 1][0];
                    dpDown[l][r][1] = dpDown[l][r - 1] [1] + 1;
                }
            }
        }

        int dpFront[][] = new int[n][2];
        for (int i = 0; i < n; i++) {
            if (a[i] == 0) {
                dpFront[i][0] = (i > 0 ? dpFront[i - 1][0] : 0) + 1;
                dpFront[i][1] = i > 0 ? dpFront[i - 1][1] : 0;
            } else {
                dpFront[i][0] = i > 0 ? dpFront[i - 1][0] : 0;
                dpFront[i][1] = (i > 0 ? Math.max(dpFront[i - 1][0], dpFront[i - 1][1]) : 0) + 1;
            }
        }

        int dpBack[][] = new int[n + 1][2];
        for (int i = n - 1; i >= 0; i--) {
            if (a[i] == 0) {
                dpBack[i][0] = Math.max(dpBack[i + 1][0], dpBack[i + 1][1]) + 1;
                dpBack[i][1] = dpBack[i + 1][1];
            } else {
                dpBack[i][0] = dpBack[i + 1][0];
                dpBack[i][1] = dpBack[i + 1][1] + 1;
            }
        }

        int best = 0;
        for (int l = 0; l < n; l++) {
            for (int r = l; r < n; r++) {

                for (int i1 = 0; i1 < 2; i1++) {
                    for (int i2 = i1; i2 < 2; i2++) {
                        for (int i3 = i2; i3 < 2; i3++) {
                            best = Math.max(best, (l > 0 ? dpFront[l - 1][i1] : 0)
                                                  + dpDown[l][r + 1][i2]
                                                  + dpBack[r + 1][i3]);
                        }
                    }
                }

            }
        }

        System.out.println(best);
    }
}