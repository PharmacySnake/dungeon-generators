import java.util.Random;

public class Node {
    int minArea;
    int heightB, heightE, widthB, widthE;
    Node left;
    Node right;
    Random random;// = new Random();
    int horizSptPnt;
    int vertSptPnt;
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

    public void generateChildren() {
        //System.out.println((heightE - heightB) + ":" + (widthE - widthB));
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

            if (splitDirection) { //HORIZONTAL LEFT
                random = new Random();
                createSpace = random.nextInt(100) + 1;
                if ((createSpace < chance || atleastAreas > 0) &&
                        (horizSptPnt - heightB >= minArea - 1 && widthE - widthB >= minArea - 1)) {
                    atleastAreas--;
                    left = new Node(heightB, horizSptPnt, widthB, widthE, atleastAreas, chance);
                    //System.out.println(left);
                    left.generateChildren();
                }

                random = new Random();
                createSpace = random.nextInt(100) + 1;
                if ((createSpace < chance || atleastAreas > 0) &&
                        (heightE - horizSptPnt >= minArea - 1 && widthE - widthB >= minArea - 1)) {
                    atleastAreas--;
                    right = new Node(horizSptPnt, heightE, widthB, widthE, atleastAreas, chance);
                    //System.out.println(right);
                    right.generateChildren();
                }

            } else { //vERTICAL
                random = new Random();
                createSpace = random.nextInt(100) + 1;
                if ((createSpace < chance || atleastAreas > 0) &&
                        (heightE - heightB >= minArea - 1 && vertSptPnt - widthB >= minArea - 1)) {
                    atleastAreas--;
                    left = new Node(heightB, heightE, widthB, vertSptPnt, atleastAreas, chance);
                    //System.out.println(left);
                    left.generateChildren();
                }
                random = new Random();
                createSpace = random.nextInt(100) + 1;
                if ((createSpace < chance || atleastAreas > 0) &&
                        (heightE - heightB >= minArea - 1 && widthE - vertSptPnt >= minArea - 1)) {
                    atleastAreas--;
                    right = new Node(heightB, heightE, vertSptPnt, widthE, atleastAreas, chance);
                    //System.out.println(right);
                    right.generateChildren();
                }
            }
            
        } else if (heightE - heightB >= minArea * 2 + 1) { //split horizontally
            random = new Random();
            createSpace = random.nextInt(100) + 1;
            if ((createSpace < chance || atleastAreas > 0) &&
                    (horizSptPnt - heightB >= minArea - 1 && widthE - widthB >= minArea - 1)) {
                atleastAreas--;
                left = new Node(heightB, horizSptPnt, widthB, widthE, atleastAreas, chance);
                //System.out.println(left);
                left.generateChildren();
            }
            random = new Random();
            createSpace = random.nextInt(100) + 1;
            if ((createSpace < chance || atleastAreas > 0) &&
                    (heightE - horizSptPnt >= minArea - 1 && widthE - widthB >= minArea - 1)) {
                atleastAreas--;
                right = new Node(horizSptPnt, heightE, widthB, widthE, atleastAreas, chance);
                //System.out.println(right);
                right.generateChildren();
            }
            
        } else if (widthE - widthB >= minArea * 2) { //split vertically
            random = new Random();
            createSpace = random.nextInt(100) + 1;
            if ((createSpace < chance || atleastAreas > 0) &&
                    (heightE - heightB >= minArea - 1 && vertSptPnt - widthB >= minArea - 1)) {
                atleastAreas--;
                left = new Node(heightB, heightE, widthB, vertSptPnt, atleastAreas, chance);
                //System.out.println(left);
                left.generateChildren();
            }
            random = new Random();
            createSpace = random.nextInt(100) + 1;
            if ((createSpace < chance || atleastAreas > 0) &&
                    (heightE - heightB >= minArea - 1 && widthE - vertSptPnt >= minArea - 1)) {
                atleastAreas--;
                right = new Node(heightB, heightE, vertSptPnt, widthE, atleastAreas, chance);
                //System.out.println(right);
                right.generateChildren();
            }
        }
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

    public void flatten(Node d) {
            if (d == null) return;
            Node left = d.left;
            Node right = d.right;
            d.left = null;

            flatten(left);
            flatten(right);

            d.right = left;
            Node current = d;
            while (current.right != null) current = current.right;
            current.right = right;

        }

    public void shrinkNodes(Node d) {
        int y;
        int height;
        int x;
        int width;
        int centerY = ((d.heightE-d.heightB) / 2) + d.heightB;
        int centerX = ((d.widthE-d.widthB) / 2) + d.widthB;
        //System.out.println("Y: "+d.heightB+"-"+d.heightE+", X: "+d.widthB+"-"+d.widthE);

        if (centerY - d.heightB > 1) {
            //int max = d.heightE-2;
                //y = random.nextInt(d.heightE - d.heightB - 1) + d.heightB;
                //System.out.println("Y: "+d.heightB+"-"+d.heightE);
                //int a = d.heightE - 1 - d.heightB;
                //System.out.println("hA: " + a);
            y = random.nextInt(centerY - (d.heightB + 1)) + (d.heightB + 1);
        } else {
            y = d.heightB + 1;
        }
        if (d.heightE - centerY > 1) {
            //System.out.println("y: "+y);
                //int b = d.heightE - (y+2);// - 1;
                //System.out.println("hB: "+b);
            height = random.nextInt(d.heightE - (centerY+1)) + (centerY + 1);
        } else {
            height = d.heightE - 1;
        }
        if (centerX - d.widthB > 1) {
            //int a = d.widthE - 2 - d.widthB;
                //System.out.println("X: "+d.widthB+"-"+d.widthE);
                //System.out.println("wA: " + a);
            x = random.nextInt(centerX - (d.widthB + 1)) + (d.widthB + 1);
        } else {
            x = d.widthB + 1;
        }
        if (d.widthE - x > 1) {
            //System.out.println("x: "+x);
                //int b = d.widthE - (x+2);// - 1;
                //System.out.println("wB: "+b);
            width = random.nextInt(d.widthE - (centerX+1)) + (centerX + 1);
        } else {
            width = d.widthE - 1;
        }

        d.heightB = y;
        d.heightE = height;
        d.widthB = x;
        d.widthE = width;

        //System.out.println("y: "+y+"-"+height+", x: "+x+"-"+width);
            //System.out.println();
    }
}
