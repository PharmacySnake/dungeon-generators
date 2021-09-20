import java.util.*;

public class BSPDungeon {
    int minArea = 3;
    int heightB,heightE,widthB,widthE;
    BSPDungeon leftChild;
    BSPDungeon rightChild;
    Random random = new Random();
    int HorizSptPnt;
    int VertSptPnt;
    int createSpace;

    public BSPDungeon(int y, int h, int x, int w) {
        heightB = y;
        heightE = h;
        widthB = x;
        widthE = w;
        leftChild = null;
        rightChild = null;

    }

    public void generateSpaces() {
        System.out.println((heightE - heightB) + ":" + (widthE - widthB));
        random = new Random();
        //HorizSptPnt = random.nextInt(heightE+1-heightB)+heightB;
        int boundH = heightE-(heightB+3)-2;
        if (boundH > 0) {
            HorizSptPnt = random.nextInt(boundH)+(heightB+3);
        }
        random = new Random();
        //VertSptPnt = random.nextInt(widthE+1-widthB)+widthB;
        int boundW = widthE-(widthB+3)-2;
        if (boundW > 0) {
            VertSptPnt = random.nextInt(boundW)+(widthB+3);
        }
        int usableSpace;

        if (heightE-heightB >= minArea*2 && widthE-widthB >= minArea*2) {
            random = new Random();
            boolean splitDirection = random.nextBoolean(); //true horizontal, false vertical

            if (splitDirection) { //HORIZONTAL LEFT
                random = new Random();
                createSpace = random.nextInt(100)+1;
                if (createSpace < 76) {
                    //usableSpace = HorizSptPnt - heightB;
                    //if (usableSpace >= minArea) {
                        leftChild = new BSPDungeon(heightB, HorizSptPnt - 1, widthB, widthE);
                        System.out.println(leftChild);
                        leftChild.generateSpaces();
                    //}
                }
                random = new Random();
                createSpace = random.nextInt(100)+1;
                if (createSpace < 76) {
                    //usableSpace = heightE - HorizSptPnt;
                    //if (usableSpace >= minArea) { //HORIZONTAL RIGHT
                        rightChild = new BSPDungeon(HorizSptPnt + 1, heightE, widthB, widthE);
                        System.out.println(rightChild);
                        rightChild.generateSpaces();
                    //}
                }


            } else { //vERTICAL
                random = new Random();
                createSpace = random.nextInt(100)+1;
                if (createSpace < 76) {
                    //usableSpace = VertSptPnt - widthB;
                    //if (usableSpace >= minArea) { //VERTICAL SPLIT LEFT
                        leftChild = new BSPDungeon(heightB, heightE, widthB, VertSptPnt - 1);
                        System.out.println(leftChild);
                        leftChild.generateSpaces();
                    //}
                }
                random = new Random();
                createSpace = random.nextInt(100)+1;
                if (createSpace < 76) {
                    //usableSpace = widthE - VertSptPnt;
                    //if (usableSpace >= minArea) { //VERTICAL SPLIT RIGHT
                        rightChild = new BSPDungeon(heightB, heightE, VertSptPnt + 1, widthE);
                        System.out.println(rightChild);
                        rightChild.generateSpaces();
                    //}
                }

            }


        } else if (heightE-heightB >= minArea*2) { //split horizontally
            //HorizSptPnt = random.nextInt(heightE+1);
            random = new Random();
            createSpace = random.nextInt(100)+1;
            if (createSpace < 76) {
                //usableSpace = HorizSptPnt - heightB;
                //if (usableSpace >= minArea) { //HORIZONTAL SPLIT LEFT
                    leftChild = new BSPDungeon(heightB, HorizSptPnt - 1, widthB, widthE);
                    System.out.println(leftChild);
                    leftChild.generateSpaces();
                //}
            }
            random = new Random();
            createSpace = random.nextInt(100)+1;
            if (createSpace < 76) {
                //usableSpace = heightE - HorizSptPnt;
                //if (usableSpace >= minArea) { //HORIZONTAL SPLIT RIGHT
                    rightChild = new BSPDungeon(HorizSptPnt + 1, heightE, widthB, widthE);
                    System.out.println(rightChild);
                    rightChild.generateSpaces();
                //}
            }


        } else if (widthE-widthB >= minArea*2) { //split vertically
            //VertSptPnt = random.nextInt(widthE+1);
            random = new Random();
            createSpace = random.nextInt(100)+1;
            if (createSpace < 76) {
                //usableSpace = VertSptPnt - widthB;
                //if (usableSpace >= minArea) { //VERTICAL SPLIT LEFT
                    leftChild = new BSPDungeon(heightB, heightE, widthB, VertSptPnt - 1);
                    System.out.println(leftChild);
                    leftChild.generateSpaces();
                //}
            }
            random = new Random();
            createSpace = random.nextInt(100)+1;
            if (createSpace < 76) {
                //usableSpace = widthE - VertSptPnt;
                //if (usableSpace >= minArea) { //VERTICAL SPLIT RIGHT
                    rightChild = new BSPDungeon(heightB, heightE, VertSptPnt + 1, widthE);
                    System.out.println(rightChild);
                    rightChild.generateSpaces();
                //}
            }
        }

        System.out.println("jabadabaduu");
    }

    @Override
    public String toString() {
        return "BSPDungeon{" +
                "heightB=" + heightB +
                ", heightE=" + heightE +
                ", widthB=" + widthB +
                ", widthE=" + widthE +
                '}';
    }

    public void printChild(BSPDungeon d) {
        if (d.leftChild != null) {
            System.out.println(d.leftChild);
            printChild(d.leftChild);
        }
        if (d.rightChild != null) {
            System.out.println(d.rightChild);
            printChild(d.rightChild);
        }
    }

    private static void printTree(String[][] luolasto, int toY, int toX) {
        for (int y = 0; y < toY; y++) {
            for (int x = 0; x < toX; x++) {
                System.out.print(luolasto[y][x]);
            }
            System.out.println();
        }
    }

    public String[][] writeTree(BSPDungeon d, String[][] r) {
        if (d.leftChild != null) {
            //yLÄ
            //if (d.leftChild.heightB >= 0 && d.leftChild.widthB >= 0 && d.leftChild.widthE+2 >= 39) {
                for (int x = d.leftChild.widthB; x <= d.leftChild.widthE; x++) {
                    r[d.leftChild.heightB][x] = "#";
                }
            //}
            //aLA
            //if (d.leftChild.heightE+2 <= 26 && d.leftChild.widthB >= 0 && d.leftChild.widthE+2 <= 39) {
                for (int x = d.leftChild.widthB; x <= d.leftChild.widthE; x++) {
                    r[d.leftChild.heightE][x] = "#";
                }
            //}
            //vASEN
            //if (d.leftChild.widthB >= 0) {
                for (int y = d.leftChild.heightB; y <= d.leftChild.heightE; y++) {
                    r[y][d.leftChild.widthB] = "#";
                }
            //}
            //oIKEA
            //if (d.leftChild.widthE+2 <= 39) {
                for (int y = d.leftChild.heightB; y <= d.leftChild.heightE; y++) {
                    r[y][d.leftChild.widthE] = "#";
                }
            //}
            //printTree(r);
            //System.out.println();
            r = writeTree(d.leftChild, r);
        }
        if (d.rightChild != null) {
            //yLÄ
           //if (d.rightChild.heightB >= 0 && d.rightChild.widthB >= 0 && d.rightChild.widthE+2 >= 39) {
                for (int x = d.rightChild.widthB; x <= d.rightChild.widthE; x++) {
                    r[d.rightChild.heightB][x] = "#";
                }
            //}
            //aLA
            //if (d.rightChild.heightE+2 <= 26 && d.rightChild.widthB >= 0 && d.rightChild.widthE+2 <= 39) {
                for (int x = d.rightChild.widthB; x <= d.rightChild.widthE; x++) {
                    r[d.rightChild.heightE][x] = "#";
                }
            //}
            //vASEN
            //if (d.rightChild.widthB >= 0) {
                for (int y = d.rightChild.heightB; y <= d.rightChild.heightE; y++) {
                    r[y][d.rightChild.widthB] = "#";
                }
            //}
            //oIKEA
            //if (d.rightChild.widthE+2 <= 39) {
                for (int y = d.rightChild.heightB; y <= d.rightChild.heightE; y++) {
                    r[y][d.rightChild.widthE] = "#";
                }
            //}
                //printTree(r);
                //System.out.println();
                r = writeTree(d.rightChild, r);
        }
        //System.out.println();
        return r;
    }
    public static void main(String[] args) {
        int h = 27;
        int w = 40;
        //BSPDungeon d = new BSPDungeon(0, 9, 0, 11);
        BSPDungeon d = new BSPDungeon(1, h-1, 1, w-1);
        d.generateSpaces();
        System.out.println("tästä alas");
        d.printChild(d);
        String[][] luolasto = new String[h+1][w+1];
        for (int i = 0; i < h; i++) {
            Arrays.fill(luolasto[i], ".");
        }
        for (int i = 0; i < w; i++) {
            luolasto[0][i] = ".";
            luolasto[h-1][i] = ".";
        }
        for (int i = 0; i < h; i++) {
            luolasto[i][0] = ".";
            luolasto[i][w-1] = ".";
        }

        luolasto = d.writeTree(d, luolasto);
        System.out.println();
        printTree(luolasto, h, w);


    }


}