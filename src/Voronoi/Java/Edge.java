public class Edge {
    Point start;
    Point end;
    Point siteLeft;
    Point siteRight;
    Point direction;
    Edge neighbor;
    double slope;
    double yint;

    public Edge(Point first, Point left, Point right) {
        start = first;
        siteLeft = left;
        siteRight = right;
        direction = new Point(-(right.x - left.x), right.y - left.y);
        end = null;
        slope = (right.x - left.x)/(left.y - right.y);
        Point mid = new Point((left.y + right.y) / 2, (right.x + left.x)/2);
        yint = mid.y - slope* mid.x;
    }

    public String toString() {
        return start + ", " + end;
    }
}