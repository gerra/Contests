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

public class F {
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
        new F(getInputStream()).solve();
    }

    private BufferedScanner mReader;

    private F(InputStream inputStream) throws IOException {
        mReader = new BufferedScanner(new BufferedInputStream(inputStream));
    }

    private void solve() throws IOException {
        int x = mReader.nextInt();

        long[] len = new long[x + 1];

        int[] first = new int[x + 1];
        int[] last = new int[x + 1];

        long[] pref = new long[x + 1];
        long[] suff = new long[x + 1];

        long[] res = new long[x + 1];

        len[0] = 1;
        len[1] = 1;

        first[1] = 1;
        last[1] = 1;

        for (int i = 2; i <= x; i++) {
            len[i] = len[i - 1] + len[i - 2];

            first[i] = first[i - 1];
            last[i] = last[i - 2];

            pref[i] = pref[i - 1] + (res[i - 1] * len[i - 2] + pref[i - 2]);
            if (last[i - 1] == 1 && first[i - 2] == 1) {
                pref[i] += len[i - 1] * len[i - 2];
            }

            suff[i] = suff[i - 2] + (res[i - 2] * len[i - 1] + suff[i - 1]);
            if (last[i - 1] == 1 && first[i - 2] == 1) {
                suff[i] += len[i - 1] * len[i - 2];
            }

            res[i] = res[i - 1] + res[i - 2] + suff[i - 1] * len[i - 2] + pref[i - 2] * len[i - 1];
            if (last[i - 1] == 1 && first[i - 2] == 1) {
                res[i] += len[i - 1] * len[i - 2];
            }
        }

        System.out.println(res[x]);
    }
}