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
        String s = mReader.next();
        String t = mReader.next();
        int n = mReader.nextInt();
        int[] prefsS = calcPrefs(s);
        int[] prefsT = calcPrefs(t);
        int[] leftsS = calcLefts(s);
        int[] leftsT = calcLefts(t);
        for (int i = 0; i < n; i++) {
            int l1 = mReader.nextInt() - 1;
            int r1 = mReader.nextInt() - 1;
            int l2 = mReader.nextInt() - 1;
            int r2 = mReader.nextInt() - 1;

            int bSCount = prefsS[r1] - (l1 != 0 ? prefsS[l1 - 1] : 0);
            int bTCount = prefsT[r2] - (l2 != 0 ? prefsT[l2 - 1] : 0);

            int leftS = leftsS[r1] >= l1 ? leftsS[r1] : -1;
            int leftT = leftsT[r2] >= l2 ? leftsT[r2] : -1;

            boolean ok;
            if (bSCount > bTCount || bSCount % 2 != bTCount % 2) {
                ok = false;
            } else if (leftS == -1 && leftT == -1) {
                ok = (r1 - l1 + 1) >= (r2 - l2 + 1)
                     && (r1 - l1 + 1) % 3 == (r2 - l2 + 1) % 3;
            } else if (leftS == -1) {
                ok = (r2 - leftT) <= (r1 - l1);
            } else if (leftT == -1) {
                // unreachable state because of first if-statement
                ok = false;
            } else {
                if (bSCount == bTCount) {
                    ok = (r1 - leftS) >= (r2 - leftT) && (r1 - leftS) % 3 == (r2 - leftT) % 3;
                } else {
                    ok = (r1 - leftS) >= (r2 - leftT);
                }
            }

            System.out.print(ok ? 1 : 0);
        }

        System.out.println();
    }

    int[] calcPrefs(String s) {
        int n = s.length();
        int[] pref = new int[n];
        for (int i = 0; i < n; i++) {
            pref[i] = (s.charAt(i) == 'B' || s.charAt(i) == 'C' ? 1 : 0) + (i != 0 ? pref[i - 1] : 0);
        }
        return pref;
    }

    int[] calcLefts(String s) {
        int n = s.length();
        int[] leftB = new int[n];
        int cur = -1;
        for (int i = 0; i < n; i++) {
            if (s.charAt(i) == 'B' || s.charAt(i) == 'C') {
                cur = i;
            }
            leftB[i] = cur;
        }
        return leftB;
    }
}