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
import java.util.List;
import java.util.StringTokenizer;
import java.util.TreeSet;

public class I {
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
        new I(getInputStream()).solve();
    }

    private BufferedScanner mReader;

    private I(InputStream inputStream) throws IOException {
        mReader = new BufferedScanner(new BufferedInputStream(inputStream));
    }

    private void solve() throws IOException {
        int n = mReader.nextInt();
        List<Integer> times = new ArrayList<>(n);
        int max = 23 * 60 + 59;
        boolean[] bud = new boolean[2 * 25 * 60];
        for (int i = 0; i < n; i++) {
            String s = mReader.next();
            int h = (s.charAt(0) - '0') * 10 + (s.charAt(1) - '0');
            int m = (s.charAt(3) - '0') * 10 + (s.charAt(4) - '0');
            bud[h * 60 + m] = true;
            bud[h * 60 + m + 24 * 60] = true;
        }

        int best = 0;
        for (int i = 0; i <= max + 24*60;) {
            if (bud[i]) {
                i++;
            } else {
                int cur = 0;
                while (i <= max + 24*60 && !bud[i]) {
                    cur++;
                    i++;
                }
                if (cur > best) {
                    best = cur;
                }
            }
        }
        int h = best / 60;
        System.out.print(h < 10 ? ("0" + h) : h);
        System.out.print(":");
        int m = best % 60;
        System.out.println(m < 10 ? ("0" + m) : m);
    }
}