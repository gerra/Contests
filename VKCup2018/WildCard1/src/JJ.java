import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.StringTokenizer;
import java.util.TreeSet;

public class JJ {
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
        if (System.getProperty("ONLINE_JJUDGE") != null) {
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
        new JJ(getInputStream()).solve();
    }

    private BufferedScanner mReader;

    private JJ(InputStream inputStream) throws IOException {
        mReader = new BufferedScanner(new BufferedInputStream(inputStream));
    }

    private void solve() throws IOException {
        int n = mReader.nextInt();
        TreeSet<Segment> segments = new TreeSet<>();
        for (int i = 0; i < n; i++) {
            int l = mReader.nextInt();
            int r = mReader.nextInt();
            Segment segment = new Segment(l, r);
            if (segments.isEmpty()) {
                segments.add(segment);
            } else {
                if (segments.first().l > segment.r || segments.last().r < segment.l) {
                    segments.add(segment);
                } else {
                    Segment from = segments.higher(segment);
                    if (from == null) {
                        from = segments.floor(segment);
                    }
                    Segment mayBeFrom = segments.lower(from);
                    if (mayBeFrom != null && mayBeFrom.r >= segment.l) {
                        from = mayBeFrom;
                    }
                    if (segment.r >= from.l) {
                        int newL = Math.min(from.l, segment.l);
                        int newR = Math.max(from.r, segment.r);
                        while (from != null && segment.l <= from.r && segment.r >= from.l) {
                            segments.remove(from);
                            newR = Math.max(newR, from.r);
                            from = segments.higher(from);
                        }
                        segments.add(new Segment(newL, newR));
                    } else {
                        segments.add(segment);
                    }

                    if (i == 15) {
                        System.out.println("oops");
                    }
                }
            }
            System.out.print(segments.size() + " ");
        }
        System.out.println();
    }

    private static class Segment implements Comparable<Segment> {
        final int l;
        final int r;

        Segment(int l, int r) {
            this.l = l;
            this.r = r;
        }

        @Override
        public int compareTo(Segment o) {
            return l != o.l ? l - o.l : r - o.r;
        }
    }
}