import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Stack;
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
        int c = mReader.nextInt();
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = mReader.nextInt();
        }
        if (c >= n) {
            long sum = 0;
            int min = Integer.MAX_VALUE;
            for (int x : a) {
                sum += x;
                min = Math.min(min, x);
            }
            System.out.println(c == n ? sum - min : sum);
        } else if (c == 1) {
            System.out.println(0);
        } else {
            long[][] dp = new long[n][2];
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < 2; j++) {
                    dp[i][j] = Long.MAX_VALUE;
                }
            }

            long sumC = 0;
            Queue<NumberWithIndex> mins = new PriorityQueue<>(c);

            for (int i = 0; i < n; i++) {
                sumC += a[i];
                sumC -= i >= c ? a[i - c] : 0;
                mins.add(new NumberWithIndex(a[i], i));
                while (mins.peek().i <= i - c) {
                    mins.poll();
                }

                // 1
                dp[i][0] = (i > 0 ? Math.min(dp[i - 1][0], dp[i - 1][1]) : 0) + a[i];
                // c
                if (i >= c - 1) {
                    dp[i][1] = (i >= c ? Math.min(dp[i - c][0], dp[i - c][1]) : 0) + sumC - mins.peek().a;
                }

//                System.out.println(dp[i][0] + " " + dp[i][1]);
            }

//            System.out.println();

            System.out.println(Math.min(dp[n - 1][0], dp[n - 1][1]));
        }
    }

    private static class NumberWithIndex implements Comparable<NumberWithIndex> {
        private int a;
        private int i;

        NumberWithIndex(int a, int i) {
            this.a = a;
            this.i = i;
        }


        @Override
        public int compareTo(NumberWithIndex o) {
            return a != o.a ? Integer.compare(a, o.a) : Integer.compare(i, o.i);
        }
    }
}