import java.util.Random;
/*
public class Node2 {
    int minArea = 3;
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

    }

    public void generateChildren() {
        //System.out.println((heightE - heightB) + ":" + (widthE - widthB));
        random = new Random();
        int boundH = heightE - (heightB + 3) - 2;
        if (boundH > 0) {
            horizSptPnt = random.nextInt(boundH) + (heightB + 3);
        }
        random = new Random();
        int boundW = widthE - (widthB + 3) - 2;
        if (boundW > 0) {
            vertSptPnt = random.nextInt(boundW) + (widthB + 3);
        }
        random = new Random();
        int vertSptBufferL = random.nextInt(4) + 1;
        int vertSptBufferR = random.nextInt(4) + 1;
        int horiSptBufferL = random.nextInt(5) + 1;
        int horiSptBufferR = random.nextInt(5) + 1;

        if (heightE > heightB && widthE > widthB && heightE - heightB >= minArea * 2 && widthE-widthB >= minArea * 2) {
            random = new Random();
            boolean splitDirection = random.nextBoolean(); //true horizontal, false vertical

            if (splitDirection) { //HORIZONTAL LEFT
                random = new Random();
                createSpace = random.nextInt(100) + 1;

                if ((createSpace < chance || atleastAreas > 0)
                        && horizSptPnt - horiSptBufferL - widthB >= 3 && horizSptPnt - horiSptBufferL > widthB) {
                    atleastAreas--;
                    //left = new Node(heightB, horizSptPnt - 1, widthB, widthE, atleastAreas, chance);
                    left = new Node(heightB, horizSptPnt - horiSptBufferL, widthB, widthE, atleastAreas, chance);
                    //System.out.println(left);
                    left.generateChildren();
                }
                random = new Random();
                createSpace = random.nextInt(100) + 1;
                if ((createSpace < chance || atleastAreas > 0)
                        && heightE - horizSptPnt - horiSptBufferR >= 3 && heightE > horizSptPnt - horiSptBufferR) {
                    atleastAreas--;
                    //right = new Node(horizSptPnt + 1, heightE, widthB, widthE, atleastAreas, chance);
                    right = new Node(horizSptPnt + horiSptBufferR, heightE, widthB, widthE, atleastAreas, chance);
                    //System.out.println(right);
                    right.generateChildren();
                }

            } else { //vERTICAL
                random = new Random();
                createSpace = random.nextInt(100) + 1;

                if ((createSpace < chance || atleastAreas > 0)
                        && vertSptPnt - vertSptBufferL - widthB >= 3 && vertSptPnt - vertSptBufferL > widthB) {
                    atleastAreas--;
                    //left = new Node(heightB, heightE, widthB, vertSptPnt - 1, atleastAreas, chance);
                    left = new Node(heightB, heightE, widthB, vertSptPnt - vertSptBufferL, atleastAreas, chance);
                    //System.out.println(left);
                    left.generateChildren();
                }

                random = new Random();
                createSpace = random.nextInt(100) + 1;

                if ((createSpace < chance || atleastAreas > 0)
                        && widthE - vertSptPnt - vertSptBufferR >= 3 && widthE > vertSptPnt - vertSptBufferR) {
                    atleastAreas--;
                    //right = new Node(heightB, heightE, vertSptPnt + 1, widthE, atleastAreas, chance);
                    right = new Node(heightB, heightE, vertSptPnt + vertSptBufferR, widthE, atleastAreas, chance);
                    //System.out.println(right);
                    right.generateChildren();
                }
            }

        } else if (heightE - heightB >= minArea * 2) { //split horizontally
            random = new Random();
            createSpace = random.nextInt(100) + 1;

            if ((createSpace < chance || atleastAreas > 0)
                    && horizSptPnt - horiSptBufferL - widthB >= 3 && horizSptPnt - horiSptBufferL > widthB) {
                atleastAreas--;
                //left = new Node(heightB, horizSptPnt - 1, widthB, widthE, atleastAreas, chance);
                left = new Node(heightB, horizSptPnt - horiSptBufferL, widthB, widthE, atleastAreas, chance);
                //System.out.println(left);
                left.generateChildren();
            }

            random = new Random();
            createSpace = random.nextInt(100) + 1;

            if ((createSpace < chance || atleastAreas > 0)
                    && heightE - horizSptPnt - horiSptBufferR >= 3 && heightE > horizSptPnt - horiSptBufferR) {
                atleastAreas--;
                //right = new Node(horizSptPnt + 1, heightE, widthB, widthE, atleastAreas, chance);
                right = new Node(horizSptPnt + horiSptBufferR, heightE, widthB, widthE, atleastAreas, chance);
                //System.out.println(right);
                right.generateChildren();
            }

        } else if (widthE - widthB >= minArea * 2) { //split vertically
            random = new Random();
            createSpace = random.nextInt(100) + 1;

            if ((createSpace < chance || atleastAreas > 0)
                    && vertSptPnt - vertSptBufferL - widthB >= 3 && vertSptPnt - vertSptBufferL > widthB) {
                atleastAreas--;
                //left = new Node(heightB, heightE, widthB, vertSptPnt - 1, atleastAreas, chance);
                left = new Node(heightB, heightE, widthB, vertSptPnt - vertSptBufferL, atleastAreas, chance);
                //System.out.println(left);
                left.generateChildren();
            }

            random = new Random();
            createSpace = random.nextInt(100) + 1;

            if ((createSpace < chance || atleastAreas > 0)
                    && widthE - vertSptPnt - vertSptBufferR >= 3 && widthE > vertSptPnt - vertSptBufferR) {
                atleastAreas--;
                //right = new Node(heightB, heightE, vertSptPnt + 1, widthE, atleastAreas, chance);
                right = new Node(heightB, heightE, vertSptPnt + vertSptBufferR, widthE, atleastAreas, chance);
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

    public void nodesToRooms(Node d) {
        int y;// = 9999;
        int height;// = 9999;
        int x;// = 9999;
        int width;// = 9999;
        //System.out.println("Y: "+d.heightB+"-"+d.heightE+", X: "+d.widthB+"-"+d.widthE);

        if (d.heightE - d.heightB >= 3) {
            //int max = d.heightE-2;
            //y = random.nextInt(d.heightE - d.heightB - 1) + d.heightB;
            //System.out.println("Y: "+d.heightB+"-"+d.heightE);
            //int a = d.heightE - 1 - d.heightB;
            //System.out.println("hA: " + a);
            y = random.nextInt(d.heightE - 1 - d.heightB) + d.heightB;
        } else {
            y = d.heightB;
        }
        if (d.heightE - y >= 3) {
            //System.out.println("y: "+y);
            //int b = d.heightE - (y+2);// - 1;
            //System.out.println("hB: "+b);
            height = d.heightE - random.nextInt(d.heightE - (y + 2));//+y;// - 1)+y;// + d.heightB;
        } else {
            height = d.heightE;
        }
        if (d.widthE - d.widthB >= 3) {
            //int a = d.widthE - 2 - d.widthB;
            //System.out.println("X: "+d.widthB+"-"+d.widthE);
            //System.out.println("wA: " + a);
            x = random.nextInt(d.widthE - 2 - d.widthB) + d.widthB;
        } else {
            x = d.widthB;
        }
        if (d.widthE - x >= 3) {
            //System.out.println("x: "+x);
            //int b = d.widthE - (x+2);// - 1;
            //System.out.println("wB: "+b);
            width = d.widthE - random.nextInt(d.widthE - (x + 2));//+x;// - 1)+x;// + d.widthB;
        } else {
            width = d.widthE;
        }

        d.heightB = y;
        d.heightE = height;
        d.widthB = x;
        d.widthE = width;

        //System.out.println("y: "+y+"-"+height+", x: "+x+"-"+width);
        //System.out.println();
    }
}
*/
