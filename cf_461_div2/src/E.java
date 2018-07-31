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

public class E {
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
        new E().solve();
    }

    private BufferedScanner mReader;
    private PrintStream mWriter;

    private E() throws IOException {
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
        int w = mReader.nextInt();
        int b = mReader.nextInt();
        int x = mReader.nextInt();

        int[] count = new int[n];
        int[] cost = new int[n];

        int totalCount = 0;

        for (int i = 0; i < n; i++) {
            count[i] = mReader.nextInt();
            totalCount += count[i];
        }

        for (int i = 0; i < n; i++) {
            cost[i] = mReader.nextInt();
        }

        long[][] maxMana = new long[n][totalCount + 1];

        for (int i = 0; i <= count[0]; i++) {
            maxMana[0][i] = w - (long) i * cost[0];
        }

        int currentMaxCount = count[0];
        for (int i = 1; i < n; i++) {
            int prevMaxCount = currentMaxCount;
            currentMaxCount += count[i];
            for (int j = 0; j <= currentMaxCount; j++) {
                maxMana[i][j] = -1;
                for (int k = Math.max(0, j - prevMaxCount); k <= Math.min(count[i], j); k++) {
                    int prevCount = j - k;
                    long manaCapacity = w + (long) prevCount * b;
                    long mana = maxMana[i - 1][prevCount];
                    if (mana >= 0) {
                        maxMana[i][j] = Math.max(maxMana[i][j],
                                                 Math.min(mana + x, manaCapacity) - (long) cost[i] * k);
                    }
                }
            }
        }

        int result = 0;
        for (int i = totalCount; i >= 0; i--) {
            if (maxMana[n - 1][i] >= 0) {
                result = i;
                break;
            }
        }

        mWriter.println(result);
    }
}