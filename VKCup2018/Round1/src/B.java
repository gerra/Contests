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
import java.util.Collections;
import java.util.List;
import java.util.StringTokenizer;

public class B {
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
        new B().solve();
    }

    private BufferedScanner mReader;
    private PrintStream mWriter;

    private B() throws IOException {
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

        char[][] plan = new char[n][12];
        List<EmptySeat> emptySeats = new ArrayList<>(10 * n);
        int result = 0;
        for (int i = 0; i < n; i++) {
            String s = mReader.next();
            for (int j = 0; j < s.length(); j++) {
                plan[i][j] = s.charAt(j);
                if (s.charAt(j) == '.') {
                    emptySeats.add(new EmptySeat(i, j, getNearPassengersCount(s, j, 'S')));
                } else if (s.charAt(j) == 'S') {
                    result += getNearPassengersCount(s, j, 'P') + getNearPassengersCount(s, j, 'S');
                }
            }
        }

        Collections.sort(emptySeats);
        for (int i = 0; i < k; i++) {
            EmptySeat emptySeat = emptySeats.get(i);
            result += emptySeat.cnt;
            plan[emptySeat.row][emptySeat.place] = 'x';
        }

        System.out.println(result);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < 12; j++) {
                System.out.print(plan[i][j]);
            }
            System.out.println();
        }
    }

    private int getNearPassengersCount(String row, int place, char passengerType) {
        int cnt = 0;
        if (place > 0) {
            cnt += (row.charAt(place - 1) == passengerType) ? 1 : 0;
        }
        if (place < row.length() - 1) {
            cnt += (row.charAt(place + 1) == passengerType) ? 1 : 0;
        }
        return cnt;
    }

    private static class EmptySeat implements Comparable<EmptySeat> {

        private int row;
        private int place;
        private int cnt;

        public EmptySeat(int row, int place, int cnt) {
            this.row = row;
            this.place = place;
            this.cnt = cnt;
        }

        @Override
        public int compareTo(EmptySeat o) {
            return cnt - o.cnt;
        }
    }
}