public class Edge {
    // särmän alkupiste
    public Point start;

    // särmän loppupiste
    public Point end;

    // särmän viereiset tontit (sites)
    public Point siteLeft;
    public Point siteRight;

    // särmän suunta
    private Point direction;


    public Edge neighbor;

    // kulmakerroin
    public double slope;

    // y-koordinaatin leikkauspiste
    public double yint;

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

    public Edge(Point start, Point end) {
        this.start = start;
        this.end = end;
        Point mid = new Point((end.y + start.y) / 2, (start.x + end.x)/2);
        slope = 0;
        yint = mid.y - slope*mid.x;
    }

    public String toString() {
        return start + ", " + end;
    }
}
