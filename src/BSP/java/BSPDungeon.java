import java.util.*;

public class BSPDungeon {
    int height;
    int width;
    int chance;
    String[][] dungeon;
    Node nodes;
    LinkedList<Node> selectedNodes;

    public BSPDungeon(int height,int width,int roomsAtLeast,int chance) {
        this.height = height;
        this.width = width;
        this.chance = chance;
        this.dungeon = new String[height+1][width+1];
        this.nodes = new Node(1,height-1,1,width-1,roomsAtLeast,chance);
        selectedNodes = new LinkedList<>();

        nodes.generateChildren();

        nodesToList(nodes);
        for (Node node : selectedNodes) {
            System.out.println(node);
        }
        System.out.println(selectedNodes.size());
        selectNodes();
        /*for (Node node : selectedNodes) {
            System.out.println(node);
        }*/
        //System.out.println(selectedNodes.size());
        selectedNodes.removeAll(Collections.singleton(null));
        for (Node node : selectedNodes) {
            System.out.println(node);
        }
        System.out.println(selectedNodes.size());

        //nodes.printChild(nodes);
        initializeDungeon(height, dungeon);

        nodesToDungeon(nodes);
        printDungeon();

        System.out.println();
        System.out.println("lopulliset huoneet");


        //nodes.flatten(nodes);

        //LinkedList<Node> flattenedNodes = new LinkedList<>();


        //nodesToList(nodes);
        //flatNodesToList(flattenedNodes);

        /*for (Node node : flattenedNodes) {
            nodes.nodesToRooms(node);
        }*/
        for (Node node : selectedNodes) {
            nodes.nodesToRooms(node);
        }
        for (Node node : selectedNodes) {
          //  if (reservedRoom(node, dungeon)) {
                drawDungeon(node);
            //}
        }

        printDungeon();
    }

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
                    ;
                }
            }
        }
    }

    private void flatNodesToList(LinkedList<Node> flattenedNodes) {
        while (nodes.right != null) {
            flattenedNodes.push(nodes.right);
            nodes = nodes.right;
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

    public void nodesToDungeon(Node d) {
        if (d.left != null) {
            //yLÄ
            for (int x = d.left.widthB; x <= d.left.widthE; x++) {
                dungeon[d.left.heightB - 1][x] = "#";
            }
            //aLA
            for (int x = d.left.widthB; x <= d.left.widthE; x++) {
                dungeon[d.left.heightE + 1][x] = "#";
            }
            //vASEN
            for (int y = d.left.heightB; y <= d.left.heightE; y++) {
                dungeon[y][d.left.widthB - 1] = "#";
            }
            //oIKEA
            for (int y = d.left.heightB; y <= d.left.heightE; y++) {
                dungeon[y][d.left.widthE + 1] = "#";
            }
            nodesToDungeon(d.left);
        }
        if (d.right != null) {
            //yLÄ
            for (int x = d.right.widthB; x <= d.right.widthE; x++) {
                dungeon[d.right.heightB - 1][x] = "#";
            }
            //aLA
            for (int x = d.right.widthB; x <= d.right.widthE; x++) {
                dungeon[d.right.heightE + 1][x] = "#";
            }
            //vASEN
            for (int y = d.right.heightB; y <= d.right.heightE; y++) {
                dungeon[y][d.right.widthB - 1] = "#";
            }
            //oIKEA
            for (int y = d.right.heightB; y <= d.right.heightE; y++) {
                dungeon[y][d.right.widthE + 1] = "#";
            }
            nodesToDungeon(d.right);
        }
    }

    public String[][] createHalls(int y1, int x1, int y2, int x2, String[][] r) {
        int pointY1 = y1 / 2;
        int pointX1 = x1 / 2;
        int pointY2 = y2 / 2;
        int pointX2 = x2 / 2;

        if (pointX2 - pointX1 < 0) {
            if (pointY2 - pointY1 < 0) {
                if (Math.random() < 0.5) {
                    for (int y = pointY1; y <= pointY2; y++) {
                        r[y][pointX1 - 1] = "#";
                        r[y][pointX1] = ".";
                        r[y][pointX1 + 1] = "#";
                    }
                    for (int x = pointX1; x <= pointX2; x++) {
                        r[pointY2][x] = "#";
                        r[pointY2][x] = ".";
                        r[pointY2][x] = "#";
                    }
                } else {
                    for (int y = pointY1; y <= pointY2; y++) {
                        r[y][pointX2 - 1] = "#";
                        r[y][pointX2] = ".";
                        r[y][pointX2 + 1] = "#";
                    }
                    for (int x = pointX1; x <= pointX2; x++) {
                        r[pointY1][x] = "#";
                        r[pointY1][x] = ".";
                        r[pointY1][x] = "#";
                    }
                }
            }
        }
        return r;
    }

    public boolean reservedRoom(Node d, String[][] r) {
        for (int y = d.heightB; y <= d.heightE; y++) {
            for (int x = d.widthB; x <= d.widthE; x++) {
                if (r[y][x] != " ") {
                    return false;
                }
            }
        }
        /*if (r[d.heightB][d.widthB] != " " || r[d.heightB][d.widthE] != " " || r[d.heightE][d.widthB] != " " || r[d.heightE][d.widthE] != " ") {
            return false;
        }*/

        return true;
    }

    public void drawDungeon(Node d) {
        //║╔╗╚╝═░

        //corners
        dungeon[d.heightB - 1][d.widthB - 1] = "╔";
        dungeon[d.heightB - 1][d.widthE + 1] = "╗";
        dungeon[d.heightE + 1][d.widthB - 1] = "╚";
        dungeon[d.heightE + 1][d.widthE + 1] = "╝";
        //top & bottom
        for (int x = d.widthB; x <= d.widthE; x++) {
            dungeon[d.heightB - 1][x] = "═";
            dungeon[d.heightE + 1][x] = "═";
        }
        //left & right
        for (int y = d.heightB; y <= d.heightE; y++) {
            dungeon[y][d.widthB - 1] = "║";
            dungeon[y][d.widthE + 1] = "║";
        }//*/
        //floor
        for (int y = d.heightB; y <= d.heightE; y++) {
            for (int x = d.widthB; x <= d.widthE; x++) {
                dungeon[y][x] = ".";
            }
        }
    }

    private void initializeDungeon(int h, String[][] dungeon) {
        for (int i = 0; i <= h; i++) {
            Arrays.fill(dungeon[i], " ");
        }
    }
}
