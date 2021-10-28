import java.util.Random;

public class Node {
    // alueen vähittäiskoko
    private int minArea;

    // noden neljä kulmaa, korkeus alku ja loppu, leveys alku ja loppu
    public int heightB, heightE, widthB, widthE;

    // noden viereiset lapset
    public Node left;
    public Node right;

    // so random
    private Random random;

    // horisontaalinen leikkauspiste
    private int horizSptPnt;

    // vertikaalinen leikkauspiste
    private int vertSptPnt;

    int createSpace;
    int atleastAreas;
    int chance;

    public Node(int y, int h, int x, int w, int atLeast, int chance) {
        this.heightB = y;
        this.heightE = h;
        this.widthB = x;
        this.widthE = w;
        this.left = null;
        this.atleastAreas = atLeast;
        this.chance = chance;
        minArea = 5;

    }

    // luodaan lapsia jakamalla käsiteltävä alue joko vertikaalisesti tai horisontaalisesti
    public void generateChildren() {
        random = new Random();
        int boundH = (heightE - (minArea - 1)) - (heightB + (minArea - 1)) + 1;
        if (boundH > 0) {
            horizSptPnt = random.nextInt(boundH) + (heightB + (minArea - 1));
        }
        random = new Random();
        int boundW = (widthE - (minArea - 1)) - (widthB + (minArea -1)) + 1;
        if (boundW > 0) {
            vertSptPnt = random.nextInt(boundW) + (widthB + (minArea - 1));
        }

        if (heightE > heightB && widthE > widthB && heightE - heightB >= minArea * 2 + 1 && widthE-widthB >= minArea * 2 + 1) {
            random = new Random();
            boolean splitDirection = random.nextBoolean(); //true horizontal, false vertical

            // split horizontally
            if (splitDirection) {
                random = new Random();
                createSpace = random.nextInt(100) + 1;
                if ((createSpace < chance || atleastAreas > 0) &&
                        (horizSptPnt - heightB >= minArea - 1 && widthE - widthB >= minArea - 1)) {
                    atleastAreas--;
                    left = new Node(heightB, horizSptPnt, widthB, widthE, atleastAreas, chance);
                    left.generateChildren();
                }

                random = new Random();
                createSpace = random.nextInt(100) + 1;
                if ((createSpace < chance || atleastAreas > 0) &&
                        (heightE - horizSptPnt >= minArea - 1 && widthE - widthB >= minArea - 1)) {
                    atleastAreas--;
                    right = new Node(horizSptPnt, heightE, widthB, widthE, atleastAreas, chance);
                    right.generateChildren();
                }

                // split vertically
            } else {
                random = new Random();
                createSpace = random.nextInt(100) + 1;
                if ((createSpace < chance || atleastAreas > 0) &&
                        (heightE - heightB >= minArea - 1 && vertSptPnt - widthB >= minArea - 1)) {
                    atleastAreas--;
                    left = new Node(heightB, heightE, widthB, vertSptPnt, atleastAreas, chance);
                    left.generateChildren();
                }
                random = new Random();
                createSpace = random.nextInt(100) + 1;
                if ((createSpace < chance || atleastAreas > 0) &&
                        (heightE - heightB >= minArea - 1 && widthE - vertSptPnt >= minArea - 1)) {
                    atleastAreas--;
                    right = new Node(heightB, heightE, vertSptPnt, widthE, atleastAreas, chance);
                    right.generateChildren();
                }
            }

            // split horizontally
        } else if (heightE - heightB >= minArea * 2 + 1) {
            random = new Random();
            createSpace = random.nextInt(100) + 1;
            if ((createSpace < chance || atleastAreas > 0) &&
                    (horizSptPnt - heightB >= minArea - 1 && widthE - widthB >= minArea - 1)) {
                atleastAreas--;
                left = new Node(heightB, horizSptPnt, widthB, widthE, atleastAreas, chance);
                left.generateChildren();
            }
            random = new Random();
            createSpace = random.nextInt(100) + 1;
            if ((createSpace < chance || atleastAreas > 0) &&
                    (heightE - horizSptPnt >= minArea - 1 && widthE - widthB >= minArea - 1)) {
                atleastAreas--;
                right = new Node(horizSptPnt, heightE, widthB, widthE, atleastAreas, chance);
                right.generateChildren();
            }

            // split vertically
        } else if (widthE - widthB >= minArea * 2) {
            random = new Random();
            createSpace = random.nextInt(100) + 1;
            if ((createSpace < chance || atleastAreas > 0) &&
                    (heightE - heightB >= minArea - 1 && vertSptPnt - widthB >= minArea - 1)) {
                atleastAreas--;
                left = new Node(heightB, heightE, widthB, vertSptPnt, atleastAreas, chance);
                left.generateChildren();
            }
            random = new Random();
            createSpace = random.nextInt(100) + 1;
            if ((createSpace < chance || atleastAreas > 0) &&
                    (heightE - heightB >= minArea - 1 && widthE - vertSptPnt >= minArea - 1)) {
                atleastAreas--;
                right = new Node(heightB, heightE, vertSptPnt, widthE, atleastAreas, chance);
                right.generateChildren();
            }
        }
    }

    // kutistetaan nodea, jos random sanoo niin
    public void shrinkNodes(Node d) {
        int y;
        int height;
        int x;
        int width;
        int centerY = ((d.heightE-d.heightB) / 2) + d.heightB;
        int centerX = ((d.widthE-d.widthB) / 2) + d.widthB;

        if (centerY - d.heightB > 1) {
            y = random.nextInt(centerY - (d.heightB + 1)) + (d.heightB + 1);
        } else {
            y = d.heightB + 1;
        }

        if (d.heightE - centerY > 1) {
            height = random.nextInt(d.heightE - (centerY+1)) + (centerY + 1);
        } else {
            height = d.heightE - 1;
        }

        if (centerX - d.widthB > 1) {
            x = random.nextInt(centerX - (d.widthB + 1)) + (d.widthB + 1);
        } else {
            x = d.widthB + 1;
        }

        if (d.widthE - x > 1) {
            width = random.nextInt(d.widthE - (centerX+1)) + (centerX + 1);
        } else {
            width = d.widthE - 1;
        }

        d.heightB = y;
        d.heightE = height;
        d.widthB = x;
        d.widthE = width;
    }

    @Override
    public String toString() {
            return "Node{" +
                    "heightB=" + heightB +
                    ", heightE=" + heightE +
                    ", widthB=" + widthB +
                    ", widthE=" + widthE +
                    '}';
    }
        
    public void printChild(Node d) {
            if (d.left != null) {
                System.out.println(d.left);
                printChild(d.left);
            }
            if (d.right != null) {
                System.out.println(d.right);
                printChild(d.right);
            }
        }
}
