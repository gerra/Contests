import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Objects;
import java.util.StringTokenizer;

public class D {
    private static class BufferedScanner implements Closeable {
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

        @Override
        public void close() throws IOException {
            mReader.close();
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
        return new BufferedInputStream(inputStream);
    }

    public static void main(String[] args) throws IOException {
        try (BufferedScanner in = new BufferedScanner(getInputStream());
             PrintWriter out = new PrintWriter(System.out)) {
            new D(in, out).solve();
            out.flush();
        }
    }

    private BufferedScanner in;
    private PrintWriter out;

    private D(BufferedScanner in, PrintWriter out) throws IOException {
        this.in = in;
        this.out = out;
    }

    private void solve() throws IOException {
        int n = in.nextInt();
        int w = in.nextInt();

        Time[] times = new Time[n * 2];
        for (int i = 0; i < n; i++) {
            int x = Math.abs(in.nextInt());
            int v = Math.abs(in.nextInt());

            double startTimeValue = (double) x / (v + w);
            double finishTimeValue = (double) x / (v - w);

            FinishTime finishTime = new FinishTime(i, finishTimeValue);
            StartTime startTime = new StartTime(i, startTimeValue, finishTime);

            times[i * 2] = startTime;
            times[i * 2 + 1] = finishTime;
        }

        Arrays.sort(times);

        List<Double> allTimeValues = new ArrayList<>(times.length);
        for (Time time : times) {
            allTimeValues.add(time.value);
        }

        CoordinatesZipper coordinatesZipper = new CoordinatesZipper<>(allTimeValues, true);
        coordinatesZipper.zip();

        CalcOpenIntervalsVisitor timeVisitor = new CalcOpenIntervalsVisitor(coordinatesZipper);

        for (Time time : times) {
            time.acceptTimeVisitor(timeVisitor);
        }

        System.out.println(timeVisitor.getOpenedIntervalsCount());
    }

    private static abstract class Time implements Comparable<Time> {
        final int id;
        final double value;

        Time(int id, double value) {
            this.id = id;
            this.value = value;
        }

        @Override
        public int compareTo(Time o) {
            return Double.compare(value, o.value) == 0
                           ? Integer.compare(getPriority(), o.getPriority())
                           : Double.compare(value, o.value);
        }

        public abstract void acceptTimeVisitor(TimeVisitor timeVisitor);

        @Override
        public String toString() {
            return "Time{" +
                   "id=" + id +
                   ", value=" + value +
                   '}';
        }

        protected abstract int getPriority();
    }

    private static class StartTime extends Time {
        FinishTime finishTime;

        StartTime(int id, double value, FinishTime finishTime) {
            super(id, value);
            this.finishTime = finishTime;
        }

        @Override
        public void acceptTimeVisitor(TimeVisitor timeVisitor) {
            timeVisitor.visit(this);
        }

        @Override
        protected int getPriority() {
            return 1;
        }

        public FinishTime getFinishTime() {
            return finishTime;
        }
    }

    private static class FinishTime extends Time {
        FinishTime(int id, double value) {
            super(id, value);
        }

        @Override
        public void acceptTimeVisitor(TimeVisitor timeVisitor) {
            timeVisitor.visit(this);
        }

        @Override
        protected int getPriority() {
            return 2;
        }
    }

    private interface TimeVisitor {
        void visit(StartTime startTime);
        void visit(FinishTime finishTime);
    }

    private static class CalcOpenIntervalsVisitor implements TimeVisitor {
        private final CoordinatesZipper<Double> coordinatesZipper;

        private final PrefixBasedQueried openedIntervals;

        private int openedTimeIntervalsCount = 0;
        private long result = 0;

        CalcOpenIntervalsVisitor(CoordinatesZipper<Double> coordinatesZipper) {
            this.coordinatesZipper = coordinatesZipper;

            openedIntervals = new Fenwick(this.coordinatesZipper.size());
        }

        @Override
        public void visit(StartTime startTime) {
//            result += openedTimeIntervalsCount;
            openedTimeIntervalsCount++;

            result += openedIntervals.get(coordinatesZipper.get(startTime.finishTime.value),
                                          coordinatesZipper.size() - 1);
            openedIntervals.add(coordinatesZipper.get(startTime.finishTime.value), 1);

            System.out.println("Start: " + startTime);
        }

        @Override
        public void visit(FinishTime finishTime) {
            openedTimeIntervalsCount--;

            openedIntervals.add(coordinatesZipper.get(finishTime.value), -1);

            System.out.println("Finish: " + finishTime);
        }

        long getOpenedIntervalsCount() {
            return result;
        }
    }

    private interface PrefixBasedQueried {
        void add(int i, int x);
        int get(int i);
        int get(int l, int r);
    }

    private static class Fenwick implements PrefixBasedQueried {
        final int[] a;

        public Fenwick(int size) {
            a = new int[size + 1];
        }

        @Override
        public void add(int i, int x) {
            i++;
            for (; i < a.length; i += i & (-i)) {
                a[i] += x;
            }
        }

        @Override
        public int get(int i) {
            i++;
            int result = 0;
            for (; i > 0; i -= i & (-i)) {
                result += a[i];
            }
            return result;
        }

        @Override
        public int get(int l, int r) {
            return get(r) - get(l - 1);
        }
    }

    private static class CoordinatesZipper<T extends Comparable<T>> {
        private Map<T, Integer> zippedCoordinates = null;

        private List<T> toZip;
        private boolean sorted;

        public CoordinatesZipper(List<T> toZip, boolean sorted) {
            this.toZip = new ArrayList<>(toZip);
            this.sorted = sorted;

            if (!this.sorted) {
                Collections.sort(this.toZip);
            }
        }

        public void zip() {
            if (zippedCoordinates != null) {
                return;
            }
            zippedCoordinates = new HashMap<>(toZip.size());
            int id = 0;
            ListIterator<T> iterator = toZip.listIterator();
            while (iterator.hasNext()) {
                T current = iterator.next();
                boolean advanced = false;
                while (iterator.hasNext()) {
                    advanced = true;
                    if (!Objects.equals(iterator.next(), current)) {
                        break;
                    }
                }
                if (advanced) {
                    iterator.previous();
                }
                zippedCoordinates.put(current, id++);
            }
        }

        public int get(T notZipped) {
            return zippedCoordinates.getOrDefault(notZipped, -1);
        }

        public int size() {
            return zippedCoordinates.size();
        }
    }
}