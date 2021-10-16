public class Parabola {

    public static int isFocus = 0;
    public static int isVertex = 1;
    int type;
    Point point;
    Edge edge;
    Event event;

    Parabola parent;
    Parabola left;
    Parabola right;

    public Parabola() {
        type = isVertex;
    }

    public Parabola(Point p) {
        point = p;
        type = isFocus;
    }

    public void setLeft(Parabola p) {
        left = p;
        p.parent = this;
    }

    public void setRight(Parabola p) {
        right = p;
        p.parent = this;
    }

    public static Parabola getLeftChild(Parabola p) {
        if (p == null) return null;
        Parabola child = p.left;
        while(child.type == isVertex) child = child.right;
        return child;
    }

    public static Parabola getRightChild(Parabola p) {
        if (p == null) return null;
        Parabola child = p.right;
        while (child.type == isVertex) child = child.left;
        return child;
    }

    public static Parabola getLeftParent(Parabola p) {
        Parabola parent = p.parent;
        if (parent == null) return null;
        Parabola last = p;
        while (parent.left == last) {
            if (parent.parent == null) return null;
            last = parent;
            parent = parent.parent;
        }
        return parent;
    }

    public static Parabola getRightParent(Parabola p) {
        Parabola parent = p.parent;
        if (parent == null) return null;
        Parabola last = p;
        while (parent.right == last) {
            if (parent.parent == null) return null;
            last = parent;
            parent = parent.parent;
        }
        return parent;
    }
}
