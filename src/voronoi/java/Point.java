public class Point implements Comparable <Point> {

    double y;
    double x;

    public Point(double y, double x) {
        this.y = y;
        this.x = x;
    }

    public int compareTo (Point other) {
        if (this.y == other.y) {
            if (this.x == other.x) return 0;
            else if (this.x > other.x) return 1;
            else return -1;
        } else if (this.y > other.y) {
            return 1;
        } else {
            return -1;
        }
    }

    @Override
    public String toString() {
        return "(" + y + ", " + x + ")";
    }
}
