import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
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

    List<Edge>[] g;
    long[] res;
    long[] a;
    City[] cities;

    private void solve() throws IOException {
        int n = mReader.nextInt();
        int m = mReader.nextInt();

        res = new long[n + 1];

        g = new ArrayList[n + 1];
        for (int i = 1; i <= n; i++) {
            g[i] = new ArrayList<>();
        }

        for (int i = 0; i < m; i++) {
            int a = mReader.nextInt();
            int b = mReader.nextInt();
            long w = mReader.nextLong();

            g[a].add(new Edge(b, w));
            g[b].add(new Edge(a, w));
        }

        a = new long[n + 1];
        cities = new City[n];
        for (int i = 1; i <= n; i++) {
            City localCity = new City(i, mReader.nextLong());
            a[i] = localCity.c;
            cities[i - 1] = localCity;
            res[i] = localCity.c;
        }

        Arrays.sort(cities, Comparator.comparingLong(c -> c.c));

        PriorityQueue<City> q = new PriorityQueue<>(n, Comparator.comparingLong(c -> c.c));
        boolean u[] = new boolean[n + 1];
        for (City city : cities) {
            if (u[city.i]) continue;

            q.add(city);
            u[city.i] = true;

            while (!q.isEmpty()) {
                City city1 = q.poll();
                int c = city1.i;
                for (Edge e : g[city1.i]) {
                    int to = e.to;
                    if (2 * e.w + res[c] < res[to]) {
                        res[to] = 2 * e.w + res[c];
                        q.add(new City(to, a[to]));
                    }
                    u[to] = true;
                }
            }
        }

//        for (City city : cities) {
//            int c = city.i;
//            for (Edge e : g[city.i]) {
//                int to = e.to;
//                res[to] = Math.min(2 * e.w + res[c], res[to]);
//            }
//        }

        for (int i = 1; i <= n; i++) {
            System.out.print((res[i]) + " ");
        }
        System.out.println();
    }

    private static class City {
        int i;
        long c;

        public City(int i, long c) {
            this.i = i;
            this.c = c;
        }
    }

    private static class Edge {
        int to;
        long w;

        public Edge(int to, long w) {
            this.to = to;
            this.w = w;
        }
    }
}