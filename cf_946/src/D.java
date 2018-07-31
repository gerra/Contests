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
        return inputStream;
    }

    public static void main(String[] args) throws IOException {
        new D(getInputStream()).solve();
    }

    private BufferedScanner mReader;

    private D(InputStream inputStream) throws IOException {
        mReader = new BufferedScanner(new BufferedInputStream(inputStream));
    }

    private void solve() throws IOException {
        int n = mReader.nextInt();
        int m = mReader.nextInt();
        int k = mReader.nextInt();
        int[][] g = new int[n + 1][m + 1];

        for (int i = 1; i <= n; i++) {
            String s = mReader.next();
            for (int j = 1; j <= m; j++) {
                g[i][j] = g[i][j - 1] + (s.charAt(j - 1) == '1' ? 1 : 0);
            }
        }

        // сколько часов пробыть в школе в день i, чтобы пропустить x уроков
        int[][] time = new int[n + 1][m + 1];
        for (int i = 1; i <= n; i++) {
            int total = g[i][m];
            Arrays.fill(time[i], Integer.MAX_VALUE);
            time[i][total] = 0;
            if (total == 0) {
                continue;
            }
            for (int l = 1; l <= m; l++) {
                for (int r = l; r <= m; r++) {
                    int skip = (m != r ? g[i][m] - g[i][r] : 0) + g[i][l - 1];
                    time[i][skip] = Math.min(time[i][skip], r - l + 1);
                }
            }
        }

        // сколько часов провести в школе, чтобы посетить i дней и пропустить суммарно j уроков
        int[][] dp = new int[n + 1][k + 1];

        for (int i = 1; i <= n; i++) {
            for (int j = 0; j <= k; j++) {
                dp[i][j] = dp[i - 1][j] + time[i][0];
            }

            for (int skipTotal = 0; skipTotal <= k; skipTotal++) {
                for (int skipNow = 0; skipNow <= skipTotal && skipNow <= g[i][m]; skipNow++) {
                    int cur = dp[i - 1][skipTotal - skipNow] + time[i][skipNow];
                    dp[i][skipTotal] = Math.min(dp[i][skipTotal], cur);
                }
            }
        }

        int res = 0;
        for (int i = 0; i <= k; i++) {
            if (dp[n][i] != -1) {
                res = dp[n][i];
            }
        }
        System.out.println(res);
    }
}