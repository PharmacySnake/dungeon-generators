import java.util.*;

public class BSPDungeon {
    int minArea = 3;
    int heightB,heightE,widthB,widthE;
    BSPDungeon leftChild;
    BSPDungeon rightChild;
    Random random = new Random();
    int splitHiPoint;
    int splitWiPoint;
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
        //int boundHi = heightE - (minArea * 2) + 1;
        //int boundWi = widthE - (minArea * 2) + 1;
        //System.out.println("hiBo: " + boundHi);
        //System.out.println("wiBo: " + boundWi);
        System.out.println((heightE - heightB) + ":" + (widthE - widthB));
        //if (boundHi > 0 && boundWi > 0) {
        //splitHiPoint = random.nextInt(boundHi) + minArea;
        //splitWiPoint = random.nextInt(boundWi) + minArea;
        splitHiPoint = random.nextInt(heightE+1-heightB)+heightB;
        splitWiPoint = random.nextInt(widthE+1-widthB)+widthB;
        int usableSpace;

        if (heightE > minArea && widthE > minArea) {
            boolean splitDirection = random.nextBoolean(); //true horizontal, false vertical

            if (splitDirection) { //HORIZONTAL LEFT
                createSpace = random.nextInt(100)+1;
                if (createSpace < 76) {
                    usableSpace = splitHiPoint - heightB;
                    if (usableSpace >= minArea) {
                        leftChild = new BSPDungeon(heightB, splitHiPoint - 1, widthB, widthE);
                        System.out.println(leftChild);
                        leftChild.generateSpaces();
                    }
                }
                createSpace = random.nextInt(100)+1;
                if (createSpace < 76) {
                    usableSpace = heightE - splitHiPoint;
                    if (usableSpace >= minArea) { //HORIZONTAL RIGHT
                        rightChild = new BSPDungeon(splitHiPoint + 1, heightE, widthB, widthE);
                        System.out.println(rightChild);
                        rightChild.generateSpaces();
                    }
                }


            } else {
                createSpace = random.nextInt(100)+1;
                if (createSpace < 76) {
                    usableSpace = splitWiPoint - widthB;
                    if (usableSpace >= minArea) { //VERTICAL SPLIT LEFT
                        leftChild = new BSPDungeon(heightB, heightE, widthB, splitWiPoint - 1);
                        System.out.println(leftChild);
                        leftChild.generateSpaces();
                    }
                }
                createSpace = random.nextInt(100)+1;
                if (createSpace < 76) {
                    usableSpace = widthE - splitWiPoint;
                    if (usableSpace >= minArea) { //VERTICAL SPLIT RIGHT
                        rightChild = new BSPDungeon(heightB, heightE, splitWiPoint + 1, widthE);
                        System.out.println(rightChild);
                        rightChild.generateSpaces();
                    }
                }

            }


        } else if (heightE >= minArea) { //split horizontally
            //splitHiPoint = random.nextInt(heightE+1);
            createSpace = random.nextInt(100)+1;
            if (createSpace < 76) {
                usableSpace = splitHiPoint - heightB;
                if (usableSpace >= minArea) { //HORIZONTAL SPLIT LEFT
                    leftChild = new BSPDungeon(heightB, splitHiPoint - 1, widthB, widthE);
                    System.out.println(leftChild);
                    leftChild.generateSpaces();
                }
            }
            createSpace = random.nextInt(100)+1;
            if (createSpace < 76) {
                usableSpace = heightE - splitHiPoint;
                if (usableSpace >= minArea) { //HORIZONTAL SPLIT RIGHT
                    rightChild = new BSPDungeon(splitHiPoint + 1, heightE, widthB, widthE);
                    System.out.println(rightChild);
                    rightChild.generateSpaces();
                }
            }


        } else if (widthE >= minArea) { //split vertically
            //splitWiPoint = random.nextInt(widthE+1);
            createSpace = random.nextInt(100)+1;
            if (createSpace < 76) {
                usableSpace = splitWiPoint - widthB;
                if (usableSpace >= minArea) { //VERTICAL SPLIT LEFT
                    leftChild = new BSPDungeon(heightB, heightE, widthB, splitWiPoint - 1);
                    System.out.println(leftChild);
                    leftChild.generateSpaces();
                }
            }
            createSpace = random.nextInt(100)+1;
            if (createSpace < 76) {
                usableSpace = widthE - splitWiPoint;
                if (usableSpace >= minArea) { //VERTICAL SPLIT RIGHT
                    rightChild = new BSPDungeon(heightB, heightE, splitWiPoint + 1, widthE);
                    System.out.println(rightChild);
                    rightChild.generateSpaces();
                }
            }



        } else {
            System.out.println("jabadabaduu");
            return;
        }
        //}
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

    public void printTree(BSPDungeon d) {
        if (d.leftChild != null) {
            System.out.println(d.leftChild);
            printTree(d.leftChild);
        }
        if (d.rightChild != null) {
            System.out.println(d.rightChild);
            printTree(d.rightChild);
        }
    }

    public String[][] writeTree(BSPDungeon d, String[][] r) {
        if (d.leftChild != null) {
            for (int y = d.leftChild.heightB; y < d.leftChild.heightE; y++) {
                for (int x = d.leftChild.widthB; x < d.leftChild.widthE; x++) {
                    r[y][x] = ".";
                }
            }
            r = writeTree(d.leftChild, r);
        }
        if (d.rightChild != null) {
            for (int y = d.rightChild.heightB; y < d.rightChild.heightE; y++) {
                for (int x = d.rightChild.widthB; x < d.rightChild.widthE; x++) {
                    r[y][x] = ".";
                }
            }
            r = writeTree(d.rightChild, r);
        }
        return r;
    }
    public static void main(String[] args) {
        //BSPDungeon d = new BSPDungeon(0, 9, 0, 11);
        BSPDungeon d = new BSPDungeon(0, 26, 0, 39);
        d.generateSpaces();
        System.out.println("tästä alas");
        d.printTree(d);
        //String[][] luolasto = new String[10][12];
        String[][] luolasto = new String[26][39];
        //for (int i = 0; i < 10; i++) {
        for (int i = 0; i < 26; i++) {
            Arrays.fill(luolasto[i], "#");
        }

        luolasto = d.writeTree(d, luolasto);
        //for (int y = 0; y < 10; y++) {
        for (int y = 0; y < 26; y++) {
            //for (int x = 0; x < 12; x++) {
            for (int x = 0; x < 39; x++) {
                System.out.print(luolasto[y][x]);
            }
            System.out.println();
        }


    }
}