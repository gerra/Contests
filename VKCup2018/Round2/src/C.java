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

    private static final int B = 30;

    private void solve() throws IOException {
        int n = mReader.nextInt();
        Integer[] as = new Integer[n];

        Node root = new Node();
        Node v;

        for (int i = 0; i < n; i++) {
            int a = mReader.nextInt();
            as[i] = a;
        }

        for (int i = 0; i < n; i++) {
            int p = mReader.nextInt();

            v = root;
            for (int b = B - 1; b >= 0; b--) {
                if ((p & (1 << b)) == 0) {
                    v.createL();
                    v = v.l;
                } else {
                    v.createR();
                    v = v.r;
                }
                if (b == 0) {
                    v.cnt++;
                    v.x = p;
                }
            }
        }

        for (int a : as) {
            v = root;
            int take = 0;
            for (int i = B - 1; i >= 0; i--) {
                if ((a & (1 << i)) == 0) {
                    if (v.l != null) {
                        v = v.l;
                    } else {
                        v = v.r;
                        take |= (1 << i);
                    }
                } else {
                    if (v.r != null) {
                        v = v.r;
                        take |= (1 << i);
                    } else {
                        v = v.l;
                    }
                }
            }
            delete(v, take);
            System.out.print((a ^ take) + " ");
        }
        System.out.println();
    }

    void delete(Node v, int x) {
        v.cnt--;
        if (v.cnt > 0) {
            return;
        }

        boolean allNull = true;
        for (int i = 0; i < B && allNull; i++) {
            if ((x & (1 << i)) == 0) {
                v.root.l = null;
                if (v.root.r != null) {
                    allNull = false;
                }
            } else {
                v.root.r = null;
                if (v.root.l != null) {
                    allNull = false;
                }
            }
            v = v.root;
        }
    }

    private static class Node {
        Node l, r;
        Node root;
        int cnt;
        int x;

        void createL() {
            if (l == null) {
                l = new Node();
                l.root = this;
            }
        }

        void createR() {
            if (r == null) {
                r = new Node();
                r.root = this;
            }
        }
    }
}