import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.*;

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

    public static void main(String[] args) throws IOException {
        new F().solve();
    }

    private BufferedScanner mReader;
    private PrintStream mWriter;

    private F() throws IOException {
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

    private int[] cnts;
    private int[] cntCnts;

    private void solve() throws IOException {
        int n = mReader.nextInt();
        int q = mReader.nextInt();
        int[] a = new int[n];
        List<ChangeQuery> changeQueries = new ArrayList<>(q);
        List<FindQuery> findQueries = new ArrayList<>(q);

        int[] b = new int[n];
        Set<Integer> allValues = new TreeSet<>();
        for (int i = 0; i < n; i++) {
            a[i] = mReader.nextInt();
            b[i] = a[i];
            allValues.add(a[i]);
        }

        for (int i = 0; i < q; i++) {
            int t = mReader.nextInt();
            if (t == 1) {
                int left = mReader.nextInt() - 1;
                int right = mReader.nextInt() - 1;

                findQueries.add(new FindQuery(i, changeQueries.size(), left, right));
            } else if (t == 2) {
                int index = mReader.nextInt() - 1;
                int value = mReader.nextInt();
                allValues.add(value);

                changeQueries.add(new ChangeQuery(index, value, b[index]));

                b[index] = value;
            }
        }

        Map<Integer, Integer> oldToNew = new HashMap<>(allValues.size());
        int id = 1;
        for (Integer value : allValues) {
            oldToNew.put(value, id++);
        }
        for (int i = 0; i < n; i++) {
            a[i] = oldToNew.get(a[i]);
            b[i] = oldToNew.get(b[i]);
        }
        for (ChangeQuery query : changeQueries) {
            query.x = oldToNew.get(query.x);
            query.prev = oldToNew.get(query.prev);
        }

        final int blockSize = (int) Math.pow(n, 2f / 3);
        findQueries.sort((o1, o2) -> o1.c / blockSize != o2.c / blockSize
                                             ? Integer.compare(o1.c, o2.c)
                                             : o1.l / blockSize != o2.l / blockSize
                                                       ? Integer.compare(o1.l, o2.l)
                                                       : Integer.compare(o1.r, o2.r));

        int curChangeQueryIndex = 0;
        int curLeft = 0;
        int curRight = -1;

        cnts = new int[allValues.size() + 1];
        cntCnts = new int[n + 1];

        int[] results = new int[q];

        for (FindQuery findQuery : findQueries) {
            while (curRight < findQuery.r) {
                add(a[++curRight]);
            }
            while (curLeft > findQuery.l) {
                add(a[--curLeft]);
            }
            while (curRight > findQuery.r) {
                del(a[curRight--]);
            }
            while (curLeft < findQuery.l) {
                del(a[curLeft++]);
            }

            while (curChangeQueryIndex < findQuery.c) {
                ChangeQuery changeQuery = changeQueries.get(curChangeQueryIndex++);
                if (changeQuery.prev != changeQuery.x) {
                    a[changeQuery.i] = changeQuery.x;

                    if (changeQuery.isInRange(curLeft, curRight)) {
                        del(changeQuery.prev);
                        add(changeQuery.x);
                    }
                }
            }
            while (curChangeQueryIndex > findQuery.c) {
                ChangeQuery changeQuery = changeQueries.get(--curChangeQueryIndex);
                if (changeQuery.prev != changeQuery.x) {
                    a[changeQuery.i] = changeQuery.prev;

                    if (changeQuery.isInRange(curLeft, curRight)) {
                        add(changeQuery.prev);
                        del(changeQuery.x);
                    }
                }
            }

            int result = 1;
            while (cntCnts[result] != 0) {
                result++;
            }

            results[findQuery.i] = result;
        }

        for (int result : results) {
            if (result != 0) {
                System.out.println(result);
            }
        }
    }

    private void add(int x) {
        incrementCntCnt(cnts[x]);
        incrementCnt(cnts, x);
    }

    private void del(int x) {
        decrementCntCnt(cnts[x]);
        decrementCnt(cnts, x);
    }

    private void incrementCnt(int[] cnt, int x) {
        if (x > 0) {
            cnt[x]++;
        }
    }

    private void decrementCnt(int[] cnt, int x) {
        if (x > 0 && cnt[x] > 0) {
            cnt[x]--;
        }
    }

    private void incrementCntCnt(int cnt) {
        decrementCnt(cntCnts, cnt);
        incrementCnt(cntCnts, cnt + 1);
    }

    private void decrementCntCnt(int cnt) {
        decrementCnt(cntCnts, cnt);
        incrementCnt(cntCnts, cnt - 1);
    }

    private static class ChangeQuery {
        final int i;
        int x;
        int prev;

        ChangeQuery(int i, int x, int prev) {
            this.i = i;
            this.x = x;
            this.prev = prev;
        }

        boolean isInRange(int l, int r) {
            return i >= l && i <= r;
        }
    }

    private static class FindQuery {
        final int i;
        final int c;
        final int l;
        final int r;

        FindQuery(int i, int c, int l, int r) {
            this.i = i;
            this.c = c;
            this.l = l;
            this.r = r;
        }
    }
}