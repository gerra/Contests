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
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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

    int n;
    int a, b;
    List<Integer> gates;
    List<Integer> keys;

    Set<Integer> leftKeys;
    Set<Integer> rightKeys;

    long[] dpLeft = new long[100_001];
    long[] dpRight = new long[100_001];

    long[] steps;
    long[] stepsColor;
    long[] stepsToLeft;
    long[] stepsToRight;

    int[] posLeft = new int[100_001];
    int[] posRight = new int[100_001];

    int leftLeft, leftRight;
    int rightLeft, rightRight;

    int l, r;

    private void solve() throws IOException {
        n = mReader.nextInt();
        a = mReader.nextInt() - 1;
        b = mReader.nextInt() - 1;

        steps = new long[n];
        stepsToLeft = new long[n];
        stepsToRight = new long[n];
        gates = new ArrayList<>(n - 1);
        keys = new ArrayList<>(n);
        usedToLeft = new boolean[n];
        usedToRight = new boolean[n];
        stepsColor = new long[100_001];

        Arrays.fill(dpLeft, -1);
        Arrays.fill(posLeft, -1);
        Arrays.fill(dpRight, -1);
        Arrays.fill(posRight, -1);
        Arrays.fill(steps, -1);
        Arrays.fill(stepsColor, -1);

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

        leftLeft = a;
        leftRight = a;

        rightLeft = a;
        rightRight = a;

        l = a;
        r = a;

        dpLeft[keys.get(a)] = 0;
        dpRight[keys.get(a)] = 0;

        steps[a] = 0;
        stepsToLeft[a] = 0;
        stepsToRight[a] = 0;

        posLeft[keys.get(a)] = a;
        posRight[keys.get(a)] = a;

        stepsColor[keys.get(a)] = a;

        fillLeft();
        fillRight();

        while (true) {
            fillLeft();
            fillRight();
        }
    }

    boolean usedToLeft[];
    boolean usedToRight[];

    int calcToLeft(int need) {
        if (dpLeft[need] != -1) {
            return posLeft[need];
        }
        if (fillLeft()) {
            return dpLeft[need] != -1 ? posLeft[need] : -1;
        }
        if (dpLeft[need] != -1) {
            return posLeft[need];
        }

        if (usedToLeft[l]) {
            return -1;
        }
        usedToLeft[l] = true;

        int prevL = l;
        int position = calcToRight(gates.get(l - 1));
        if (position == -1) {
            return -1;
        }
        if (prevL == l) {
            l--;
            steps[l] = steps[position] + Math.abs(position - l);
            if (dpLeft[keys.get(l)] == -1) {
                dpLeft[keys.get(l)] = steps[l];
                posLeft[keys.get(l)] = l;
            }
            if (dpLeft[need] != -1) {
                return posLeft[need];
            }
            return calcToLeft(need);
        } else {
            return calcToLeft(need);
        }
    }

    int calcToRight(int need) {
        if (dpRight[need] != -1) {
            return posRight[need];
        }
        if (fillRight()) {
            return dpRight[need] != -1 ? posRight[need] : -1;
        }
        if (dpRight[need] != -1) {
            return posRight[need];
        }

        if (usedToRight[r]) {
            return -1;
        }
        usedToLeft[r] = true;

        int prevR = r;
        int position = calcToLeft(gates.get(r));
        if (position == -1) {
            return -1;
        }
        if (prevR == r) {
            r++;
            steps[r] = steps[position] + Math.abs(position - r);
            if (dpRight[keys.get(r)] == -1) {
                dpRight[keys.get(r)] = steps[r];
                posRight[keys.get(r)] = r;
            }
            if (dpRight[need] != -1) {
                return posRight[need];
            }
            return calcToRight(need);
        } else {
            return calcToRight(need);
        }
    }

    boolean fillLeft() {
        while (l > 0) {
//            if (stepsColor[gates.get(l - 1)])

            if (dpLeft[1] != -1) {
                l--;
                steps[l] = steps[l + 1] + 1;
                if (dpLeft[keys.get(l)] == -1) {
                    dpLeft[keys.get(l)] = steps[l];
                    posLeft[keys.get(l)] = l;
                }
            }
        }
        return l == 0;
    }

    boolean fillRight() {
        while (r < n - 1 && dpRight[gates.get(r)] != -1) {
            r++;
            stepsToRight[r] = stepsToRight[r - 1] + 1;
            steps[r] = steps[r - 1] + 1;
            if (dpRight[keys.get(r)] == -1) {
                dpRight[keys.get(r)] = steps[r];
                posRight[keys.get(r)] = r;
            }
        }
        return r == n - 1;
    }
}