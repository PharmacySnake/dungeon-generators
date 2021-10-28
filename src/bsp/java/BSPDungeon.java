import java.util.*;

public class BSPDungeon {
    // koko luolaston korkeus
    private int height;

    // koko luolaston leveys
    private int width;

    // todennäköisyys, että luodaan uusi huone, mikäli vähittäismäärä on täyttynyt
    private int chance;

    // taulukko, joka sisältää luolaston
    public String[][] dungeon;
    private Node nodes;

    // valikoidut nodet, joita käytetään luolaston huoneiden tekemiseen
    private LinkedList<Node> selectedNodes;

    public BSPDungeon(int height,int width,int roomsAtLeast,int chance) {
        this.height = height;
        this.width = width;
        this.chance = chance;
        this.dungeon = new String[height+1][width+1];
        this.nodes = new Node(1,height-1,1,width-1,roomsAtLeast,chance);
        selectedNodes = new LinkedList<>();

        nodes.generateChildren();
        nodesToList(nodes);
        selectNodes();
        selectedNodes.removeAll(Collections.singleton(null));
        initializeDungeon(height, dungeon);

        for (Node node : selectedNodes) {
            nodes.shrinkNodes(node);
        }
        for (Node node : selectedNodes) {
            drawDungeon(node);
        }
        for (int i = 1; i < selectedNodes.size(); i++) {
            createHalls(selectedNodes.get(i-1), selectedNodes.get(i));
        }
    }

    // siirretään koko puun nodet listalle
    private void nodesToList(Node node) {
        if (node.left != null) {
            selectedNodes.push(node.left);
            nodesToList(node.left);
            selectedNodes.push(node.right);
        }
        if (node.right != null) {
            selectedNodes.push(node.right);
            nodesToList(node.right);
        }
    }

    // valikoidaan nodet listalta, niin ettei tule päällekkäisiä huoneita
    private void selectNodes() {
        int previousHeightB = -1;
        int previousHeightE = -1;
        int previousWidthB = -1;
        int previousWidthE = -1;
        LinkedList<Node> tempList = new LinkedList<>(selectedNodes);
        Node node;

        for (int i = 0; i < tempList.size(); i++) {
            node = tempList.get(i);
            if (node != null) {
                if (node.heightB <= previousHeightB && node.heightE >= previousHeightE
                        && node.widthB <= previousWidthB && node.widthE >= previousWidthE) {
                    selectedNodes.remove(i);
                    selectedNodes.add(i, null);
                } else {
                    previousHeightB = node.heightB;
                    previousHeightE = node.heightE;
                    previousWidthB = node.widthB;
                    previousWidthE = node.widthE;
                }
            }
        }
    }

    // luodaan käytävät huoneiden välille
    public void createHalls(Node d1, Node d2) {
        int centerY1 = ((d1.heightE - d1.heightB) / 2) + d1.heightB;
        int centerX1 = ((d1.widthE - d1.widthB) / 2) + d1.widthB;
        int centerY2 = ((d2.heightE - d2.heightB) / 2) + d2.heightB;
        int centerX2 = ((d2.widthE - d2.widthB) / 2) + d2.widthB;
        int maxY = Math.max(centerY1, centerY2);
        int maxX = Math.max(centerX1, centerX2);
        int minY = Math.min(centerY1, centerY2);
        int minX = Math.min(centerX1, centerX2);
        double rnd = Math.random();

        // centerY1, centerY2, centerX1, centerX2 sijainti toisistaan
        if ((centerY1 <= centerY2 && centerX1 >= centerX2)
                || (centerY1 >= centerY2 && centerX1 <= centerX2)) {

            // arvotaan mennäänkö ylä- vai alakautta
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

    // piirretään luolaston huoneet taulukkoon valikoitujen nodejen perusteella
    public void drawDungeon(Node d) {
        //corners
        dungeon[d.heightB - 1][d.widthB - 1] = "#";
        dungeon[d.heightB - 1][d.widthE + 1] = "#";
        dungeon[d.heightE + 1][d.widthB - 1] = "#";
        dungeon[d.heightE + 1][d.widthE + 1] = "#";

        //top & bottom
        for (int x = d.widthB; x <= d.widthE; x++) {
            dungeon[d.heightB - 1][x] = "#";
            dungeon[d.heightE + 1][x] = "#";
        }
        //left & right
        for (int y = d.heightB; y <= d.heightE; y++) {
            dungeon[y][d.widthB - 1] = "#";
            dungeon[y][d.widthE + 1] = "#";
        }
        //floor
        for (int y = d.heightB; y <= d.heightE; y++) {
            for (int x = d.widthB; x <= d.widthE; x++) {
                dungeon[y][x] = ".";
            }
        }
    }

    // alustetaan luolasto-taulukko
    private void initializeDungeon(int h, String[][] dungeon) {
        for (int i = 0; i <= h; i++) {
            Arrays.fill(dungeon[i], " ");
        }
    }

    private void printDungeon() {
        for (int y = 0; y <= height; y++) {
            for (int x = 0; x <= width; x++) {
                System.out.print(dungeon[y][x]);
            }
            System.out.println();
        }
    }
}
