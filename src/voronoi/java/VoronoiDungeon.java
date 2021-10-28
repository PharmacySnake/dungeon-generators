import java.util.List;
import java.util.PriorityQueue;
import java.util.ArrayList;
import java.util.Random;

public class VoronoiDungeon {
    // taulukko joka sisältää luolaston
    public String[][] dungeon;

    // eventit
    private PriorityQueue<Event> events;

    // lista luoduista huoneista
    private List<Room> rooms;

    // lista tonteista
    private List<Point> sites;

    // lista särmistä
    private List<Edge> edges;

    // koko luolaston korkeus
    private int height;

    // koko luolaston leveys
    private int width;

    private Parabola root;

    // mr. rng
    private Random random;

    // nykyinen y-koordinaatti
    private double yCurrent;

    public VoronoiDungeon(int height, int width) {
        this.height = height;
        this.width = width;
        dungeon = new String[height][width];
        rooms = new ArrayList<>();
        edges = new ArrayList<>();
        fillArray(height, width);
        sites = randomPoints(10);

        generateVoronoi();
        trimEdgeHeads();
        bresenhamLinesToDungeon();
        defineSpace();
        shrinkRooms();
        fillArray(height, width);
        drawDungeon();
        createHalls();
    }

    // tehdään voronoita
    private void generateVoronoi() {
        events = new PriorityQueue<>();
        for (Point p : sites) {
            events.add(new Event(p, Event.SITE_EVENT));
        }

        // käsitellään tapahtumat (events)
        while(!events.isEmpty()) {
            Event e = events.remove();
            yCurrent = e.p.y;

            if (e.type == Event.SITE_EVENT) {
                handleSite(e.p);
            } else {
                handleCircle(e);
            }
        }

        yCurrent = width+height;
        endEdges(root);

        for (Edge e : edges) {
            if (e.neighbor != null) {
                e.start = e.neighbor.end;
                e.neighbor = null;
            }
        }


    }

    // piirretään huoneet luolasto-taulukkoon
    public void drawDungeon() {
        for (Room r : rooms) {
            /*System.out.println("h:" + r.heightB + ":" + r.heightE);
            System.out.println("w:" + r.widthB + ":" + r.widthE);
            System.out.println();//*/

            //corners
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

    // kutistetaan huoneita, mikäli rng suo
    public void shrinkRooms() {
        random = new Random();

        for (Room r : rooms) {
            int hB = r.heightB;
            int hE = r.heightE;
            int wB = r.widthB;
            int wE = r.widthE;
            //System.out.println("Y: "+r.heightB+"->"+r.heightE+", X: "+r.widthB+"->"+r.widthE+", CY: "+r.centerY+", CX: "+r.centerX);

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
            //System.out.println("height: "+hB+"-"+hE+", width: "+wB+"-"+wE);
            //System.out.println();
        }

    }

    // määritellään mikä kokoinen huone voi alustavasti olla
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
            }
        }
    }

    // luodaan käytävät huoneiden välille
    public void createHalls() {
        for (int i = 1; i < rooms.size(); i++) {
            int centerY1 = Math.round(rooms.get(i - 1).centerY);
            int centerX1 = Math.round(rooms.get(i - 1).centerX);
            int centerY2 = Math.round(rooms.get(i).centerY);
            int centerX2 = Math.round(rooms.get(i).centerX);
            int maxY = Math.max(centerY1, centerY2);
            int maxX = Math.max(centerX1, centerX2);
            int minY = Math.min(centerY1, centerY2);
            int minX = Math.min(centerX1, centerX2);
            double rnd = Math.random();

            if ((centerY1 <= centerY2 && centerX1 >= centerX2)
                    || (centerY1 >= centerY2 && centerX1 <= centerX2)) {
                if (rnd < 0.5) {// minY, minX
                    for (int y = minY; y <= maxY; y++) {
                        if (!dungeon[y][minX - 1].equals(".")) dungeon[y][minX - 1] = "#";
                        dungeon[y][minX] = ".";
                        if (!dungeon[y][minX + 1].equals(".")) dungeon[y][minX + 1] = "#";
                    }
                    for (int x = minX; x <= maxX; x++) {
                        if (!dungeon[minY - 1][x].equals(".")) dungeon[minY - 1][x] = "#";
                        dungeon[minY][x] = ".";
                        if (!dungeon[minY + 1][x].equals(".")) dungeon[minY + 1][x] = "#";
                    }
                    if (!dungeon[minY - 1][minX - 1].equals(".")) dungeon[minY - 1][minX - 1] = "#";

                } else {// maxY, maxX
                    for (int y = minY; y <= maxY; y++) {
                        if (!dungeon[y][maxX - 1].equals(".")) dungeon[y][maxX - 1] = "#";
                        dungeon[y][maxX] = ".";
                        if (!dungeon[y][maxX + 1].equals(".")) dungeon[y][maxX + 1] = "#";
                    }
                    for (int x = minX; x <= maxX; x++) {
                        if (!dungeon[maxY - 1][x].equals(".")) dungeon[maxY - 1][x] = "#";
                        dungeon[maxY][x] = ".";
                        if (!dungeon[maxY + 1][x].equals(".")) dungeon[maxY + 1][x] = "#";
                    }
                    if (!dungeon[maxY + 1][maxX + 1].equals(".")) dungeon[maxY + 1][maxX + 1] = "#";

                }
            } else if ((centerY1 <= centerY2 && centerX1 <= centerX2)
                    || (centerY1 >= centerY2 && centerX1 >= centerX2)) {
                if (rnd < 0.5) {// minY, maxX
                    for (int y = minY; y <= maxY; y++) {
                        if (!dungeon[y][maxX - 1].equals(".")) dungeon[y][maxX - 1] = "#";
                        dungeon[y][maxX] = ".";
                        if (!dungeon[y][maxX + 1].equals(".")) dungeon[y][maxX + 1] = "#";
                    }
                    for (int x = minX; x <= maxX; x++) {
                        if (!dungeon[minY - 1][x].equals(".")) dungeon[minY - 1][x] = "#";
                        dungeon[minY][x] = ".";
                        if (!dungeon[minY + 1][x].equals(".")) dungeon[minY + 1][x] = "#";
                    }
                    if (!dungeon[minY - 1][maxX + 1].equals(".")) dungeon[minY - 1][maxX + 1] = "#";

                } else {// maxY, minX
                    for (int y = minY; y <= maxY; y++) {
                        if (!dungeon[y][minX - 1].equals(".")) dungeon[y][minX - 1] = "#";
                        dungeon[y][minX] = ".";
                        if (!dungeon[y][minX + 1].equals(".")) dungeon[y][minX + 1] = "#";
                    }
                    for (int x = minX; x <= maxX; x++) {
                        if (!dungeon[maxY - 1][x].equals(".")) dungeon[maxY - 1][x] = "#";
                        dungeon[maxY][x] = ".";
                        if (!dungeon[maxY + 1][x].equals(".")) dungeon[maxY + 1][x] = "#";
                    }
                    if (!dungeon[maxY + 1][minX - 1].equals(".")) dungeon[maxY + 1][minX - 1] = "#";
                }
            }

        }
    }

    // Bresenhamin algoritmi viivojen piirtämiseksi taulukkoon (hyödynnetään defineSpace:ssa)
    private void bresenhamLinesToDungeon() {
        for (Edge e : edges) {
            if (e.start.y < height && e.start.y >= 0 && e.start.x < width && e.start.x >= 0
                    && e.end.y < height && e.end.y >= 0 && e.end.x < width && e.end.x >= 0) {
                int y1 = (int) Math.round(e.start.y);
                int y2 = (int) Math.round(e.end.y);
                int x1 = (int) Math.round(e.start.x);
                int x2 = (int) Math.round(e.end.x);

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

    // lyhennetään särmien päitä, mikäli ne ovat luolasto-taulukon rajaaman alueen ulkopuolella
    private void trimEdgeHeads() {
        double startY;
        double startX;
        double endY;
        double endX;
        Edge verticalDir = new Edge(new Point(0, 0), new Point(0, width - 1));
        Edge verticalDir2 = new Edge(new Point(height - 1, 0), new Point(height - 1, width - 1));
        Edge horizontalDir = new Edge(new Point(0, 0), new Point(0, height -1));
        Edge horizontalDir2 = new Edge(new Point(width-1, 0), new Point(width - 1, height - 1));

        for (Edge e : edges) {
            startY = e.start.y;
            startX = e.start.x;
            endY = e.end.y;
            endX = e.end.x;

            Point topIntercept = getEdgeIntersection(e, verticalDir);
            Point bottomIntercept = getEdgeIntersection(e, verticalDir2);
            Point leftIntercept = getEdgeIntersection(e, horizontalDir);
            Point rightIntercept = getEdgeIntersection(e, horizontalDir2);

            // liuta ehtoja, miksi käsittelyssä olevaa särmää ei tarvitse käsitellä
            if ((((e.start.y < height - 1 && e.start.y > 0) && (e.end.y < height - 1 && e.end.y > 0))
                    && ((e.start.x < width - 1 && e.start.x > 0) && (e.end.x < width - 1 && e.end.x > 0)))
                    || (((startX < 0 && endX < 0) || startX > width - 1 && endX > width - 1)
                    || ((startY < 0 && endY < 0) || (startY > height - 1 && endY > height - 1)))
                    || (topIntercept == null || bottomIntercept == null || leftIntercept == null || rightIntercept == null)) {

                // käsiteltävän särmän alkupään piste on luolasto-taulukon alueen sisäpuolella
            } else if (startY < height - 1 && startY > 0 && startX < width - 1 && startX > 0) {
                if (endY < 0) {
                    endY = topIntercept.y;
                    endX = topIntercept.x;
                } else if (endY > height - 1) {
                    endY = bottomIntercept.y;
                    endX = bottomIntercept.x;
                }

                // käsiteltävän särmän loppupään piste on luolasto-taulukon alueen sisäpuolella
            } else if (endY < height - 1 && endY > 0 && endX < width - 1 && endX > 0) {
                if (startY < 0) {
                    startY = topIntercept.y;
                    startX = topIntercept.x;
                } else if (startY > height - 1) {
                    startY = bottomIntercept.y;
                    startX = bottomIntercept.x;
                }

                // käsiteltävän särmän pisteiden alku-Y ja loppu-Y ovat luolasto-taulukon alueen ulkopuolella
            } else if ((startY < 0 && endY > height - 1) || startY > height - 1 && endY < 0) {
                if (startY < 0) {
                    startY = topIntercept.y;
                    startX = topIntercept.x;
                } else if (startY > height - 1) {
                    startY = bottomIntercept.y;
                    startX = bottomIntercept.x;
                }

                if (endY < 0) {
                    endY = topIntercept.y;
                    endX = topIntercept.x;
                } else if (endY > height - 1) {
                    endY = bottomIntercept.y;
                    endX = bottomIntercept.x;
                }

                // käsiteltävän särmän alkupään tai loppupään piste on luolasto-taulukon alueen ulkopuolella
                // napataan ne harhailijat, jotka pääsi muiden verkkojen läpi
            } else if (startY < 0 || endY > height - 1 || startY > height - 1 || endY < 0
                    || startX < 0 || endX > width - 1 || startX > width - 1 || endX < 0) {
                if (startY < 0) {
                    startY = topIntercept.y;
                    startX = topIntercept.x;
                } else if (startY > height - 1) {
                    startY = bottomIntercept.y;
                    startX = bottomIntercept.x;
                }

                if (endY < 0) {
                    endY = topIntercept.y;
                    endX = topIntercept.x;
                } else if (endY > height - 1) {
                    endY = bottomIntercept.y;
                    endX = bottomIntercept.x;

                } if (startX < 0) {
                    startY = leftIntercept.x;
                    startX = leftIntercept.y;
                } else if (startX > width - 1) {
                    startY = rightIntercept.x;
                    startX = rightIntercept.y;

                } if (endX < 0) {
                    endY = leftIntercept.x;
                    endX = leftIntercept.y;
                } else if (endX > width - 1) {
                    endY = rightIntercept.x;
                    endX = rightIntercept.y;
                }
            }
            e.start.y = Math.ceil(startY);
            e.start.x = Math.ceil(startX);
            e.end.y = Math.ceil(endY);
            e.end.x = Math.ceil(endX);
        }
    }

    // viimeistelee kaikki keskeneräiset särmät
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

    // etsitään kahden paraabelin x:n leikkauspiste
    private double getXofEdge(Parabola par) {
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

        // poistetaan pisteeseen liittyvät eventit, koska pisteita tullaan muuttamaan
        if (p0.event != null) {
            events.remove(p0.event);
            p0.event = null;
        }
        if (p2.event != null) {
            events.remove(p2.event);
            p2.event = null;
        }

        // uusi verteksi
        Point p = new Point(getY(p1.point, e.p.x), e.p.x);

        // päätellään särmät
        xLeft.edge.end = p;
        xRight.edge.end = p;
        edges.add(xLeft.edge);
        edges.add(xRight.edge);

        // aloitetaan uusi puolittaja (särmä) verteksistä, jonka alkuperäinen särmä on korkeammalla puussa
        Parabola higher = new Parabola();
        Parabola par = p1;
        while(par != root) {
            par = par.parent;
            if (par == xLeft) higher = xLeft;
            if (par == xRight) higher = xRight;
        }
        higher.edge = new Edge(p, p0.point, p2.point);

        // poistetaan p1-piste, sekä sen parent rantaviivasta
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

    // tarkastetaan tapahtuuko kehä-tapahtumaa
    private void checkCircleEvent(Parabola b) {
        Parabola leftParent = Parabola.getLeftParent(b);
        Parabola rightParent = Parabola.getRightParent(b);

        if (leftParent == null || rightParent == null) return;

        Parabola a = Parabola.getLeftChild(leftParent);
        Parabola c = Parabola.getRightChild(rightParent);

        if (a == null || c == null || a.point == c.point) return;
        if (orientation(a.point, b.point, c.point) != 1) return;

        // särmät kohtaavat kehä-tapahtuman takia muodostaakseen verteksin
        Point start = getEdgeIntersection(leftParent.edge, rightParent.edge);
        if (start == null) return;

        //lasketaan säde
        double dx = b.point.x - start.x;
        double dy = b.point.y - start.y;
        double d = Math.sqrt((dx*dx) + (dy*dy));
        if (start.y + d < yCurrent) return; //must be after sweep line

        Point ep = new Point(start.y+d, start.x);

        //lisätään kehä-tapahtuma
        Event e = new Event(ep, Event.CIRCLE_EVENT);
        e.arc = b;
        b.event = e;
        events.add(e);
    }

    // kahden särmän leikkauspiste
    private Point getEdgeIntersection(Edge a, Edge b) {
        if (b.slope == a.slope && b.yint != a.yint) return null;

        double x = (b.yint - a.yint)/(a.slope - b.slope);
        double y = a.slope * x + a.yint;

        return new Point(y, x);
    }

    // etsitään pisteen orientaatio suhteessa särmään
    private int orientation(Point a, Point b, Point c) {
        double area = (b.x-a.x)*(c.y-a.y) - (b.y-a.y)*(c.x-a.x);
        if (area < 0) return -1;
        else if (area > 0) return 1;
        else return 0;
    }

    // selvitetään y-koordinaatti
    private double getY(Point p, double x) {
        double dp = 2*(p.y - yCurrent);
        double a1 = 1/dp;
        double b1 = -2*p.x/dp;
        double c1 = (p.x*p.x + p.y*p.y - yCurrent*yCurrent)/dp;
        return a1*x*x + b1*x + c1;
    }

    // käsitellään tontit (sites)
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

    // etsitään paraabeli x-koordinaatin mukaan
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

    // luodaan x-määrä satunnaisia pisteitä listalle
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

    // alustetaan luolasto-taulukko
    private void fillArray(int height, int width) {
        for (int i = 0; i < height; i++) {
            for (int points = 0; points < width; points++) {
                dungeon[i][points] = " ";
            }
        }
    }

    private void printDungeon() {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                System.out.print(dungeon[y][x]);
            }
            System.out.println();
        }
    }
}