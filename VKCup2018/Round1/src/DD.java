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
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.StringTokenizer;

public class DD {
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
        new DD().solve();
    }

    private BufferedScanner mReader;
    private PrintStream mWriter;

    private DD() throws IOException {
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

    int n;
    int a, b;
    List<Integer> gates;
    List<Integer> keys;

    long[] leftDP = new long[100_001];
    long[] rightDP = new long[100_001];

    int[] leftPos = new int[100_001];
    int[] rightPos = new int[100_001];

    long[] leftDistance;
    long[] rightDistance;

    int l;
    int r;

    int curLeftL = -1;
    int curLeftR = -1;

    int curRightL = -1;
    int curRightR = -1;

    private void solve() throws IOException {
        n = mReader.nextInt();
        a = mReader.nextInt() - 1;
        b = mReader.nextInt() - 1;

        gates = new ArrayList<>(n - 1);
        keys = new ArrayList<>(n);

        for (int i = 0; i < n - 1; i++) {
            gates.add(mReader.nextInt());
        }

        for (int i = 0; i < n; i++) {
            keys.add(mReader.nextInt());
        }

        if (a > b) {
            Collections.reverse(gates);
            Collections.reverse(keys);
            a = n - a - 1;
            b = n - b - 1;
        }

        leftDistance = new long[n];
        rightDistance = new long[n];

        Arrays.fill(leftDP, -1);
        Arrays.fill(rightDP, -1);

        Arrays.fill(leftPos, -1);
        Arrays.fill(rightPos, -1);

        r = a;
        l = a;

        leftDP[keys.get(a)] = 0;
        rightDP[keys.get(a)] = 0;

        leftPos[keys.get(a)] = a;
        rightPos[keys.get(a)] = a;

        while (r < b) {
            int prevL = l;
            int prevR = r;
            processToLeft();
            processToRight();
            if (prevL == l && prevR == r) {
                break;
            }
        }

        System.out.println(r < b ? -1 : rightDistance[b]);
    }

    void processToLeft() {
        while (l > 0 && leftDP[gates.get(l - 1)] != -1) {
            l--;
            int key = keys.get(l);
            leftDistance[l] = leftDistance[l + 1] + 1;
            if (leftDP[key] == -1) {
                leftPos[key] = l;
                leftDP[key] = leftDistance[l];
            }
        }

        if (l == 0) {
            return;
        }

        int need = gates.get(l - 1);
        if (rightDP[need] != -1) {
            updateLeftUsingRight();
        } else {
            if (l == curLeftL /*&& r == curRightR*/) {
                return;
            }
            curLeftL = l;
            curLeftR = r;
            processToRight();

            if (l == curLeftL) {
                updateLeftUsingRight();
            }
        }
    }

    private void updateLeftUsingRight() {
        int need = gates.get(l - 1);
        if (rightDP[need] != -1) {
            l--;
            leftDistance[l] = Math.max(rightDP[need] + Math.abs(rightPos[need] - l), leftDistance[l + 1] + 1);
            int newKey = keys.get(l);
            if (leftDP[newKey] == -1) {
                leftPos[newKey] = l;
                leftDP[newKey] = leftDistance[l];
            }
        }
    }

    void processToRight() {
        while (r < n - 1 && rightDP[gates.get(r)] != -1) {
            r++;
            int key = keys.get(r);
            rightDistance[r] = rightDistance[r - 1] + 1;
            if (rightDP[key] == -1) {
                rightPos[key] = r;
                rightDP[key] = rightDistance[r];
            }
        }

        if (r == n - 1) {
            return;
        }

        int need = gates.get(r);
        if (leftDP[need] != -1) {
            updateRightUsingLeft();
        } else {
            if (/*l == curRightL &&*/ r == curRightR) {
                return;
            }

            curRightL = l;
            curRightR = r;
            processToLeft();

            if (r == curRightR) {
                updateRightUsingLeft();
            }
        }
    }

    private void updateRightUsingLeft() {
        int need = gates.get(r);
        if (leftDP[need] != -1) {
            r++;
            rightDistance[r] = Math.max(leftDP[need] + Math.abs(leftPos[need] - r), rightDistance[r - 1] + 1);
            int newKey = keys.get(r);
            if (rightDP[newKey] == -1) {
                rightPos[newKey] = l;
                rightDP[newKey] = rightDistance[r];
            }
        }
    }
}