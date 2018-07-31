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
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeSet;

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
        int k = mReader.nextInt();
        String s = mReader.next();

        TreeSet<Character> letters = new TreeSet<>();
        for (char c : s.toCharArray()) {
            letters.add(c);
        }

        if (k > n) {
            StringBuilder sb = new StringBuilder(s);
            buildSuffix(k, letters.first(), sb);
            System.out.println(sb.toString());
        } else {
            for (int i = k - 1; i >= 0; i--) {
                Character next = letters.higher(s.charAt(i));
                if (next != null) {
                    StringBuilder sb = new StringBuilder(s.substring(0, i));
                    sb.append(next);
                    buildSuffix(k, letters.first(), sb);
                    System.out.println(sb.toString());
                    break;
                }
            }
        }
    }

    private void buildSuffix(int k, char c, StringBuilder sb) {
        int curLength = sb.length();
        for (int i = 0; i < k - curLength; i++) {
            sb.append(c);
        }
    }
}