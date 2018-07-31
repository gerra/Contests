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
        int gc = mReader.nextInt();
        int dc = mReader.nextInt();
        int fc = mReader.nextInt();

        List<Integer> gs = new ArrayList<>(gc);
        List<Integer> ds = new ArrayList<>(dc);
        List<Integer> fs = new ArrayList<>(fc);

        List<Player> all = new ArrayList<>(gc + dc + fc);

        all.addAll(readPlayers(gc, 1, gs));
        all.addAll(readPlayers(dc, 2, ds));
        all.addAll(readPlayers(fc, 3, fs));

        Collections.sort(all);

        long result = 0;
        for (Player player : all) {
            int needG = 1 - (player.type == 1 ? 1 : 0);
            int needD = 2 - (player.type == 2 ? 1 : 0);
            int needF = 3 - (player.type == 3 ? 1 : 0);

            result += find(gs, needG, player.number)
                      * find(ds, needD, player.number)
                      * find(fs, needF, player.number);
        }

        System.out.println(result);
    }

    private long find(List<Integer> numbers, int needCnt, int from) {
        if (needCnt == 0) {
            return 1;
        }

        int l = Collections.binarySearch(numbers, from);

        if (l < 0) {
            l = -l - 1;
        } else {
            l++; // we fixed this player (just +1 because of all numbers are different)
        }

        if (l >= numbers.size() || numbers.get(l) > from * 2) {
            return 0;
        }

        int r = Collections.binarySearch(numbers, from * 2);
        if (r < 0) {
            r = -r - 1;
            r--;
        }

        long c = r - l + 1;

        if (c < needCnt) {
            return 0;
        }

        if (needCnt == 1) {
            return c;
        } else if (needCnt == 2) {
            return c * (c - 1) / 2;
        } else /*if (needCnt == 3)*/ {
            return c * (c - 1) * (c - 2) / 6;
        }
    }

    private List<Player> readPlayers(int cnt, int type, List<Integer> toSave) {
        List<Player> players = new ArrayList<>(cnt);
        for (int i = 0; i < cnt; i++) {
            int number = mReader.nextInt();
            toSave.add(number);
            players.add(new Player(type, number));
        }
        Collections.sort(toSave);
        return players;
    }

    private static class Player implements Comparable<Player> {
        private final int type;
        private final int number;

        Player(int type, int number) {
            this.type = type;
            this.number = number;
        }

        @Override
        public int compareTo(Player o) {
            return number - o.number;
        }
    }
}