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
        new E(getInputStream()).solve();
    }

    private BufferedScanner mReader;

    private E(InputStream inputStream) throws IOException {
        mReader = new BufferedScanner(new BufferedInputStream(inputStream));
    }

    String tryEqual(String s) {
        char[] res = new char[s.length()];
        for (int l = 0; l < s.length() / 2; l++) {
            int r = s.length() - 1 - l;

            res[l] = s.charAt(l);
            res[r] = res[l];
        }

        boolean isOk = true;
        boolean allEquals = true;
        for (int i = s.length() / 2; i < s.length(); i++) {
            allEquals &= (res[i] == s.charAt(i));
            if (res[i] > s.charAt(i)) {
                isOk = false;
                break;
            }
        }

        if (allEquals) {
            isOk = false;
        }

        if (isOk) {
            return new String(res);
        }

        int it = s.length() / 2;
        while (it >= 0 && s.charAt(it) == '0') {
            it--;
        }

        if (it != 0 || s.charAt(0) != '1') {
            res[it]--;
            res[s.length() - 1 - it] = res[it];
            for (int i = it + 1; i < s.length() / 2; i++) {
                res[i] = '9';
                res[s.length() - 1 - i] = res[i];
            }
        } else {
            return null; // 10000000...000(0|1)
        }

        return new String(res);
    }

    private void solve() throws IOException {
        int t = mReader.nextInt();
        for (int i = 0; i < t; i++) {
            String s = mReader.next();
            String equal = tryEqual(s);
            if (equal != null) {
                System.out.println(equal);
            } else {
                for (int j = 0; j < s.length() - 2; j++) {
                    System.out.print(9);
                }
                System.out.println();
            }
        }
    }
}