
import java.util.List;
import java.util.PriorityQueue;
import java.util.ArrayList;
import java.util.Random;

public class VoronoiDungeon {
    String[][] dungeon;
    PriorityQueue<Event> events;
    List<Room> rooms = new ArrayList<>();
    List<Point> sites;
    List<Edge> edges = new ArrayList<>();
    List<Edge> trimmedEdges = new ArrayList<>();
    int height;
    int width;
    Parabola root;
    Random random;

    double yCurrent;

    public VoronoiDungeon(int height, int width) {
        dungeon = new String[height][width];
        this.height = height;
        this.width = width;
        fillArray(height, width);
        sites = randomPoints(10);

        for (Point p : sites) {
            System.out.println(p);
        }
        sites = new ArrayList<>();
        sites.add(new Point(9, 15));
        sites.add(new Point(12, 5));
        sites.add(new Point(7, 23));
        sites.add(new Point(2, 21));
        sites.add(new Point(14, 17));
        sites.add(new Point(17, 62));
        sites.add(new Point(18, 22));
        sites.add(new Point(3, 20));
        sites.add(new Point(6, 47));
        sites.add(new Point(8, 48));
        sites.add(new Point(1, 46));//*/
        //sites.add(new Point(9, 71));

        generateVoronoi();
        printDungeon();
    }

    private void generateVoronoi() {
        events = new PriorityQueue<>();
        for (Point p : sites) {
            events.add(new Event(p, Event.SITE_EVENT));
        }

        //process events (sweep line)
        int count = 0;
        while(!events.isEmpty()) {
            Event e = events.remove();
            yCurrent = e.p.y;
            count++;

            if (e.type == Event.SITE_EVENT) {
                //System.out.println(count+". SITE_EVENT "+e.p);
                handleSite(e.p);
            } else {
                //System.out.println(count+". CIRCLE_EVENT "+e.p);
                handleCircle(e);
            }
        }

        yCurrent = width+height;
        endEdges(root); //close off any dangling edges

        for (Edge e : edges) {
            if (e.neighbor != null) {
                e.start = e.neighbor.end;
                e.neighbor = null;
            }
        }

        /*for (Edge e : edges) {
            System.out.println(e);
        }//*/
        trimEdgeHeads();
        System.out.println();
        System.out.println();
        bresenhamLinesToDungeon();

        printDungeon();
        //System.out.println();
        defineSpace();
        System.out.println();
        shrinkRooms();
        for (Room r : rooms) {
            System.out.println(r+"\n");
        }
        fillArray(height, width);
        drawDungeon();
        printDungeon();
        createHalls();
    }

    public void drawDungeon() {
        for (Room r : rooms) {
            //corners
            System.out.println("h:" + r.heightB + ":" + r.heightE);
            System.out.println("w:" + r.widthB + ":" + r.widthE);
            System.out.println();

            dungeon[r.heightB - 1][r.widthB - 1] = "#";
            dungeon[r.heightB - 1][r.widthE + 1] = "#";
            dungeon[r.heightE + 1][r.widthB - 1] = "#";
            dungeon[r.heightE + 1][r.widthE + 1] = "#";
            //top & bottom
            for (int x = r.widthB; x <= r.widthE; x++) {
                dungeon[r.heightB - 1][x] = "#";
                dungeon[r.heightE + 1][x] = "#";
            }
            //left & right
            for (int y = r.heightB; y <= r.heightE; y++) {
                dungeon[y][r.widthB - 1] = "#";
                dungeon[y][r.widthE + 1] = "#";
            }
            //floor
            for (int y = r.heightB; y <= r.heightE; y++) {
                for (int x = r.widthB; x <= r.widthE; x++) {
                    dungeon[y][x] = ".";
                }
            }
        }
    }

    public void shrinkRooms() {
        random = new Random();

        for (Room r : rooms) {
            int hB = r.heightB;
            int hE = r.heightE;
            int wB = r.widthB;
            int wE = r.widthE;
            System.out.println("Y: "+r.heightB+"->"+r.heightE+", X: "+r.widthB+"->"+r.widthE+", CY: "+r.centerY+", CX: "+r.centerX);

            if (r.centerY - r.heightB > 1) {
                hB = random.nextInt(r.centerY - r.heightB) + r.heightB;
            }
            if (r.heightE - r.centerY > 1) {
                hE = random.nextInt(r.heightE - r.centerY) + (r.centerY + 1);
            }
            if (r.centerX - r.widthB > 1) {
                wB = random.nextInt(r.centerX - r.widthB) + r.widthB;
            }
            if (r.widthE - r.centerX > 1) {
                wE = random.nextInt(r.widthE - r.centerX) + (r.centerX + 1);
            }

            r.heightB = hB;
            r.heightE = hE;
            r.widthB = wB;
            r.widthE = wE;
            r.centerY = ((hE - hB) / 2) + hB;
            r.centerX = ((wE - wB) / 2) + wB;
            System.out.println("height: "+hB+"-"+hE+", width: "+wB+"-"+wE);
            System.out.println();
        }

    }

    private void defineSpace() {
        int heightB, heightE, widthB, widthE;
        boolean hB, hE, wB, wE;


        for (Point p : sites) {
            hB = hE = wB = wE = false;
            int py = (int)Math.round(p.y);
            int px = (int)Math.round(p.x);
            heightB = py;
            heightE = py;
            widthB = px;
            widthE = px;

            while (!hB || !hE || !wB || !wE) {
                if (heightB-1 > 0 && !dungeon[heightB-1][px].equals("+")) {
                    heightB--;
                } else {
                    hB = true;
                }
                if (heightE+1 < height-1 && !dungeon[heightE+1][px].equals("+")) {
                    heightE++;
                } else {
                    hE = true;
                }
                if (widthB-1 > 0 && !dungeon[py][widthB-1].equals("+")) {
                    widthB--;
                } else {
                    wB = true;
                }
                if (widthE+1 < width-1 && !dungeon[py][widthE+1].equals("+")) {
                    widthE++;
                } else {
                    wE = true;
                }
            }
            if (heightE - heightB >= 2 && widthE - widthB >= 2) {
                Room room = new Room(heightB, heightE, widthB, widthE, ((heightE-heightB) / 2) + heightB, ((widthE-widthB) / 2) + widthB, p);
                rooms.add(room);
                System.out.println(room);
                System.out.println();
            }
        }
    }

    public void createHalls() {
        for (int i = 1; i < rooms.size(); i++) {
            if (i == rooms.size() -1) {
                System.out.println();
            }
            int centerY1 = Math.round(rooms.get(i-1).centerY);
            int centerX1 = Math.round(rooms.get(i-1).centerX);
            int centerY2 = Math.round(rooms.get(i).centerY);
            int centerX2 = Math.round(rooms.get(i).centerX);
            int maxY = Math.max(centerY1, centerY2);
            int maxX = Math.max(centerX1, centerX2);
            int minY = Math.min(centerY1, centerY2);
            int minX = Math.min(centerX1, centerX2);//*/

            System.out.println("ROOM1 "+rooms.get(i-1)+"\n" +
                    "ROOM2 "+rooms.get(i)+"\n");
            /*System.out.println("MaxY: " + maxY);
            System.out.println("MaxX: " + maxX);
            System.out.println("MinY: " + minY);
            System.out.println("MinX: " + minX);//*/

        //if (Math.random() < 0.5) {
            for (int y = minY; y <= maxY; y++) {
                if (dungeon[y][minX - 1] != ".") dungeon[y][minX - 1] = "#";
                dungeon[y][minX] = ".";
                if (dungeon[y][minX + 1] != ".") dungeon[y][minX + 1] = "#";
                //printDungeon();
            }
            for (int x = minX; x <= maxX; x++) {
                if (dungeon[minY - 1][x] != ".") dungeon[minY - 1][x] = "#";
                dungeon[minY][x] = ".";
                if (dungeon[minY + 1][x] != ".") dungeon[minY + 1][x] = "#";
                //printDungeon();
            }
            if (dungeon[minY - 1][minX - 1] != ".") dungeon[minY - 1][minX - 1] = "#";
        }
    }

    private void bresenhamLinesToDungeon() {
        System.out.println("hw: "+height+", "+width);
        for (Edge e : edges) {
            if (e.start.y < height && e.start.y >= 0 && e.start.x < width && e.start.x >= 0
                    && e.end.y < height && e.end.y >= 0 && e.end.x < width && e.end.x >= 0) {
                int y1 = (int) Math.round(e.start.y);
                int y2 = (int) Math.round(e.end.y);
                int x1 = (int) Math.round(e.start.x);
                int x2 = (int) Math.round(e.end.x);
                System.out.println("(" + y1 + "," + x1 + ") - (" + y2 + "," + x2 + ")");

                int d = 0;
                int dy1 = Math.abs(y1 - y2);
                int dx1 = Math.abs(x1 - x2);
                int dy2 = 2 * dy1;
                int dx2 = 2 * dx1;

                int iy = y1 < y2 ? 1 : -1;
                int ix = x1 < x2 ? 1 : -1;

                int y = y1;
                int x = x1;

                if (dx1 >= dy1) {
                    while (true) {
                        dungeon[y][x] = "+";
                        if (x == x2) break;

                        x += ix;
                        d += dy2;
                        if (d > dx1) {
                            y += iy;
                            d -= dx2;
                        }
                    }
                } else {
                    while (true) {
                        dungeon[y][x] = "+";
                        if (y == y2) break;
                        y += iy;
                        d += dx2;
                        if (d > dy1) {
                            x += ix;
                            d -= dy2;
                        }
                    }
                }
            }
        }
    }

    private void trimEdgeHeads() {
        double startY;
        double startX;
        double endY;
        double endX;
        Edge verticalDir = new Edge(new Point(0, 0), new Point(0, width - 1));
        Edge verticalDir2 = new Edge(new Point(height - 1, 0), new Point(height - 1, width - 1));
        Edge horizontalDir = new Edge(new Point(0, 0), new Point(height - 1, 0));
        Edge horizontalDir2 = new Edge(new Point(0, width - 1), new Point(height - 1, width - 1));

        for (Edge e : edges) {
            startY = e.start.y;
            startX = e.start.x;
            endY = e.end.y;
            endX = e.end.x;

            //System.out.println("startY: " + Math.round(startY) + ", startX: " + Math.round(startX));
            //System.out.println("endY: " + Math.round(endY) + ", endX: " + Math.round(endX));
            System.out.println("startY: " + startY + ", startX: " + startX);
            System.out.println("endY: " + endY + ", endX: " + endX);

            //startY: -0.0999999999999801, startX: 63.1
            //endY: 32.15240641711229, endX: 42.3663101604278
            if (startY == -0.0999999999999801 && startX == 63.1 && endY == 32.15240641711229 && endX == 42.3663101604278) {
                System.out.println("jouuuu");
            }//*/

            /*System.out.println("vertical direction: " + getEdgeIntersection(e, verticalDir));
            System.out.println("vertical direction 2: " + getEdgeIntersection(e, verticalDir2));
            System.out.println("horizontal direction: " + getEdgeIntersection(e, horizontalDir));
            System.out.println("horizontal direction 2: " + getEdgeIntersection(e, horizontalDir2));//*/
            Point topIntercept = getEdgeIntersection(e, verticalDir);
            Point bottomIntercept = getEdgeIntersection(e, verticalDir2);
            double vertCrossPoint = ((e.start.x - e.end.x) * (verticalDir.start.y - verticalDir.end.y)) - ((e.start.y - e.end.y) * (verticalDir.start.x - verticalDir.end.x));
            System.out.println("vertical xp: " + vertCrossPoint);
            double horiCrossPoint = ((e.start.x - e.end.x) * (horizontalDir.start.y - horizontalDir.end.y)) - ((e.start.y - e.end.y) * (horizontalDir.start.x - horizontalDir.end.x));
            System.out.println("horizontal xp: " + horiCrossPoint);

            if ((((e.start.y < height - 1 && e.start.y > 0) && (e.end.y < height - 1 && e.end.y > 0))
                    && ((e.start.x < width - 1 && e.start.x > 0) && (e.end.x < width - 1 && e.end.x > 0)))
                    || (((startX < 0 && endX < 0) || startX > width - 1 && endX > width - 1)
                    || ((startY < 0 && endY < 0) || (startY > height - 1 && endY > height - 1)))) {
                System.out.println("ei käsitelty\n");

            } else if (startY < height - 1 && startY > 0) {
                //if (vertCrossPoint < 0 && horiCrossPoint < 0) {
                if (endY < 0) {
                    endY = topIntercept.y;
                    endX = topIntercept.x;
                    /*System.out.println("starty: " + startY + ", startx: " + startX);
                    System.out.println("endy: "+endY+", endx: "+endX);
                    System.out.println();//*/
                } else if (endY > height - 1) {
                    endY = bottomIntercept.y;
                    endX = bottomIntercept.x;
                    /*System.out.println("starty: " + startY + ", startx: " + startX);
                    System.out.println("endy: "+endY+", endx: "+endX);
                    System.out.println();//*/
                }

                e.end.y = endY;
                e.end.x = endX;

            } else if (endY < height - 1 && endY > 0) {
                if (startY < 0) {
                    startY = topIntercept.y;
                    startX = topIntercept.x;
                    /*System.out.println("starty: " + startY + ", startx: " + startX);
                    System.out.println("endy: "+endY+", endx: "+endX);
                    System.out.println();//*/
                } else if (startY > height - 1) {
                    startY = bottomIntercept.y;
                    startX = bottomIntercept.x;
                    /*System.out.println("starty: " + startY + ", startx: " + startX);
                    System.out.println("endy: "+endY+", endx: "+endX);
                    System.out.println();//*/
                }
                e.start.y = startY;
                e.start.x = startX;

            } else if ((startY < 0 && endY > height-1) || startY > height-1 && endY < 0) {
                /*System.out.println("nega to pos tai pos to nega");
                System.out.println("starty: " + startY + ", startx: " + startX);
                System.out.println("endy: "+endY+", endx: "+endX);//*/
                if (startY < 0) {
                    startY = topIntercept.y;
                    startX = topIntercept.x;
                    /*System.out.println("starty: " + startY + ", startx: " + startX);
                    System.out.println("endy: "+endY+", endx: "+endX);
                    System.out.println();//*/
                } else if (startY > height - 1) {
                    startY = bottomIntercept.y;
                    startX = bottomIntercept.x;
                    /*System.out.println("starty: " + startY + ", startx: " + startX);
                    System.out.println("endy: "+endY+", endx: "+endX);
                    System.out.println();//*/
                }
                if (endY < 0) {
                    endY = topIntercept.y;
                    endX = topIntercept.x;
                    /*System.out.println("starty: " + startY + ", startx: " + startX);
                    System.out.println("endy: "+endY+", endx: "+endX);
                    System.out.println();//*/
                } else if (endY > height - 1) {
                    endY = bottomIntercept.y;
                    endX = bottomIntercept.x;
                    /*System.out.println("starty: " + startY + ", startx: " + startX);
                    System.out.println("endy: "+endY+", endx: "+endX);
                    System.out.println();//*/
                }
                e.start.y = startY;
                e.start.x = startX;
                e.end.y = endY;
                e.end.x = endX;
            }
        }
    }

    //end all unfinished edges
    private void endEdges(Parabola p) {
        if (p.type == Parabola.isFocus) {
            return;
        }
        double endX = getXofEdge(p);
        double endY = p.edge.slope * endX + p.edge.yint;
        p.edge.end = new Point(endY, endX);
        edges.add(p.edge);


        endEdges(p.left);
        endEdges(p.right);
    }

    //returns current x-coordinate of an unfinished edge
    private double getXofEdge(Parabola par) {
        //find intersection of two parabolas
        Parabola left = Parabola.getLeftChild(par);
        Parabola right = Parabola.getRightChild(par);

        Point p = left.point;
        Point o = right.point;

        double dp = 2*(p.y - yCurrent);
        double a1 = 1/dp;
        double b1 = -2*p.x/dp;
        double c1 = (p.x*p.x + p.y*p.y - yCurrent*yCurrent)/dp;

        double dp2 = 2*(o.y - yCurrent);
        double a2 = 1/dp2;
        double b2 = -2*o.x/dp2;
        double c2 = (o.x*o.x + o.y*o.y - yCurrent*yCurrent)/dp2;

        double a = a1-a2;
        double b = b1-b2;
        double c = c1-c2;

        double disc = b*b - 4*a*c;
        double x1 = (-b + Math.sqrt(disc))/(2*a);
        double x2 = (-b - Math.sqrt(disc))/(2*a);

        double oy;
        if (p.y > o.y) oy = Math.max(x1, x2);
        else oy = Math.min(x1, x2);

        return oy;
    }

    private void handleCircle(Event e) {
        Parabola p1 = e.arc;
        Parabola xLeft = Parabola.getLeftParent(p1);
        Parabola xRight = Parabola.getRightParent(p1);
        Parabola p0 = Parabola.getLeftChild(xLeft);
        Parabola p2 = Parabola.getRightChild(xRight);

        //remove associated events since the points will be altered
        if (p0.event != null) {
            events.remove(p0.event);
            p0.event = null;
        }
        if (p2.event != null) {
            events.remove(p2.event);
            p2.event = null;
        }

        Point p = new Point(getY(p1.point, e.p.x), e.p.x); //new vertex

        //end edges
        xLeft.edge.end = p;
        xRight.edge.end = p;
        edges.add(xLeft.edge);
        edges.add(xRight.edge);

        //start new bisector (edge) from this vertex on which ever original edge is higher in tree
        Parabola higher = new Parabola();
        Parabola par = p1;
        while(par != root) {
            par = par.parent;
            if (par == xLeft) higher = xLeft;
            if (par == xRight) higher = xRight;
        }
        higher.edge = new Edge(p, p0.point, p2.point);

        //delete p1 and parent (boundary edge) from beach line
        Parabola gparent = p1.parent.parent;
        if (p1.parent.left == p1) {
            if (gparent.left == p1.parent) gparent.setLeft(p1.parent.right);
            if (gparent.right == p1.parent) gparent.setRight(p1.parent.right);
        } else {
            if (gparent.left == p1.parent) gparent.setLeft(p1.parent.left);
            if (gparent.right == p1.parent) gparent.setRight(p1.parent.left);
        }

        p1.parent = null;

        checkCircleEvent(p0);
        checkCircleEvent(p2);
    }

    private void checkCircleEvent(Parabola b) {
        Parabola leftParent = Parabola.getLeftParent(b);
        Parabola rightParent = Parabola.getRightParent(b);

        if (leftParent == null || rightParent == null) return;

        Parabola a = Parabola.getLeftChild(leftParent);
        Parabola c = Parabola.getRightChild(rightParent);

        if (a == null || c == null || a.point == c.point) return;
        if (orientation(a.point, b.point, c.point) != 1) return;

        //edges will intersect to form a vertex for a cirlcle event
        Point start = getEdgeIntersection(leftParent.edge, rightParent.edge);
        if (start == null) return;

        //compute radius
        double dx = b.point.x - start.x;
        double dy = b.point.y - start.y;
        double d = Math.sqrt((dx*dx) + (dy*dy));
        if (start.y + d < yCurrent) return; //must be after sweep line

        Point ep = new Point(start.y+d, start.x);
        //System.out.println("added circle event " + ep);

        //add circle event
        Event e = new Event(ep, Event.CIRCLE_EVENT);
        e.arc = b;
        b.event = e;
        events.add(e);
    }

    private Point getEdgeIntersection(Edge a, Edge b) {
        if (b.slope == a.slope && b.yint != a.yint) return null;

        double x = (b.yint - a.yint)/(a.slope - b.slope);
        double y = a.slope*x + a.yint;

        return new Point(y, x);
    }

    private int orientation(Point a, Point b, Point c) {
        double area = (b.x-a.x)*(c.y-a.y) - (b.y-a.y)*(c.x-a.x);
        if (area < 0) return -1;
        else if (area > 0) return 1;
        else return 0;
    }

    private double getY(Point p, double x) {
        double dp = 2*(p.y - yCurrent);
        double a1 = 1/dp;
        double b1 = -2*p.x/dp;
        //          p.x² + p.y² - yCur² / dp
        double c1 = (p.x*p.x + p.y*p.y - yCurrent*yCurrent)/dp;
        return a1*x*x + b1*x + c1;
    }

    private void handleSite(Point p) {
        if (root == null) {
            root = new Parabola(p);
            return;
        }

        Parabola par = getParabolaByX(p.x);
        if (par.event != null) {
            events.remove(par.event);
            par.event = null;
        }

        Point start = new Point(getY(par.point, p.x), p.x);
        Edge edgeLeft = new Edge(start, par.point, p);
        Edge edgeRight = new Edge(start, p, par.point);
        edgeLeft.neighbor = edgeRight;
        edgeRight.neighbor = edgeLeft;
        par.edge = edgeLeft;
        par.type = Parabola.isVertex;

        Parabola p0 = new Parabola(par.point);
        Parabola p1 = new Parabola(p);
        Parabola p2 = new Parabola(par.point);

        par.setLeft(p0);
        par.setRight(new Parabola());
        par.right.edge = edgeRight;
        par.right.setLeft(p1);
        par.right.setRight(p2);

        checkCircleEvent(p0);
        checkCircleEvent(p2);
    }

    private Parabola getParabolaByX(double xx) {
        Parabola par = root;
        double x = 0;
        while (par.type == Parabola.isVertex) {
            x = getXofEdge(par);
            if (x>xx) par = par.left;
            else par = par.right;
        }
        return par;
    }

    private void printDungeon() {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                System.out.print(dungeon[y][x]);
            }
            System.out.println();
        }
    }

    private void fillArray(int height, int width) {
        for (int i = 0; i < height; i++) {
            for (int points = 0; points < width; points++) {
                dungeon[i][points] = " ";
            }
        }
    }

    private List<Point> randomPoints(int amount) {
        boolean[] yys = new boolean[height];
        random = new Random();
        List<Point> points = new ArrayList<>();
        int y;
        int x;
        for (int i = 0; i < amount; i++) {
            while (true) {
                y = random.nextInt(height - 2) + 1;
                if (yys[y] == false) {
                    yys[y] = true;
                    break;
                }
            }
            x = random.nextInt(width - 2) + 1;
            points.add(new Point(y, x));
            dungeon[y][x] = "*";
        }
        return points;
    }
}