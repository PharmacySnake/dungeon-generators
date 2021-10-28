public class Event implements Comparable <Event> {

    public static int SITE_EVENT = 0;
    public static int CIRCLE_EVENT = 1;

    public Point p;
    public int type;
    public Parabola arc;

    public Event (Point p, int type) {
        this.p = p;
        this.type = type;
        arc = null;
    }

    public int compareTo(Event other) {
        return this.p.compareTo(other.p);
    }
}
