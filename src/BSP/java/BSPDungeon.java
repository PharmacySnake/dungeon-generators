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
        System.out.println();
        nodesToList(nodes);
        for (Node node : selectedNodes) {
            System.out.println(node);
        }
        System.out.println(selectedNodes.size());
        selectNodes();

        selectedNodes.removeAll(Collections.singleton(null));
        for (Node node : selectedNodes) {
            System.out.println(node);
        }
        System.out.println(selectedNodes.size());

        initializeDungeon(height, dungeon);

        for (int i = 1; i < selectedNodes.size(); i++) {
            createHalls(selectedNodes.get(i-1), selectedNodes.get(i));
        }

        /*for (Node node : selectedNodes) {
            nodesToDungeon2(node);
        }*/
        printDungeon();
        System.out.println();
        System.out.println("lopulliset huoneet");

        for (Node node : selectedNodes) {
            nodes.shrinkNodes(node);
        }
        for (Node node : selectedNodes) {
          //  if (reservedRoom(node, dungeon)) {
                drawDungeon(node);
            //}
        }
        for (int i = 1; i < selectedNodes.size(); i++) {
            createHalls(selectedNodes.get(i-1), selectedNodes.get(i));
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

    public void nodesToDungeon2(Node d) {
        //if (d.left != null) {
            //yLÄ
            for (int x = d.widthB; x <= d.widthE; x++) {
                dungeon[d.heightB][x] = "#";
            }
            //aLA
            for (int x = d.widthB; x <= d.widthE; x++) {
                dungeon[d.heightE][x] = "#";
            }
            //vASEN
            for (int y = d.heightB; y <= d.heightE; y++) {
                dungeon[y][d.widthB] = "#";
            }
            //oIKEA
            for (int y = d.heightB; y <= d.heightE; y++) {
                dungeon[y][d.widthE] = "#";
            }
            //nodesToDungeon(d.left);
        //}
        /*if (d.right != null) {
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
            //nodesToDungeon(d.right);
        }*/
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

    public void createHalls(Node d1, Node d2) {
        int centerY1 = ((d1.heightE - d1.heightB) / 2) + d1.widthB;
        int centerX1 = ((d1.widthE - d1.widthB) / 2) + d1.widthB;
        int centerY2 = ((d2.heightE - d2.heightB) / 2) + d2.widthB;
        int centerX2 = ((d2.widthE - d2.widthB) / 2) + d2.widthB;
        int maxY = Math.max(((d1.heightE - d1.heightB) / 2) + d1.heightB, ((d2.heightE - d2.heightB) / 2) + d2.heightB);
        int maxX = Math.max(((d1.widthE - d1.widthB) / 2) + d1.widthB, ((d2.widthE - d2.widthB) / 2) + d2.widthB);
        int minY = Math.min(((d1.heightE - d1.heightB) / 2) + d1.heightB, ((d2.heightE - d2.heightB) / 2) + d2.heightB);
        int minX = Math.min(((d1.widthE - d1.widthB) / 2) + d1.widthB, ((d2.widthE - d2.widthB) / 2) + d2.widthB);//*/

        System.out.println(centerY1);
        System.out.println(centerX1);
        System.out.println(centerY2);
        System.out.println(centerX2);
        System.out.println("MaxY: "+maxY);
        System.out.println("MaxX: "+maxX);
        System.out.println("MinY: "+minY);
        System.out.println("MinX: "+minX);//*/

        //if (Math.random() < 0.5) {
            for (int y = minY; y <= maxY; y++) {
                    if (dungeon[y][minX - 1] != "." && dungeon[y][minX - 1] != "║") dungeon[y][minX - 1] = "#";
                    dungeon[y][minX] = ".";
                    if (dungeon[y][minX + 1] != "." && dungeon[y][minX - 1] != "║") dungeon[y][minX + 1] = "#";
            }
            for (int x = minX; x <= maxX; x++) {
                    if (dungeon[minY - 1][x] != "." && dungeon[minY - 1][x] != "║") dungeon[minY - 1][x] = "#";
                    dungeon[minY][x] = ".";
                    if (dungeon[minY + 1][x] != "." && dungeon[minY - 1][x] != "║") dungeon[minY + 1][x] = "#";
            }
            if (dungeon[minY - 1][minX -1] != ".") dungeon[minY - 1][minX - 1] = "#";
        //} else {
            /*for (int y = minY; y <= maxY; y++) {
                dungeon[y][centerX2 - 1] = "#";
                dungeon[y][centerX2] = ".";
                dungeon[y][centerX2 + 1] = "#";
            }
            for (int x = minX; x <= maxX; x++) {
                dungeon[centerY1][x] = "#";
                dungeon[centerY1][x] = ".";
                dungeon[centerY1][x] = "#";
            }*/
        //}
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
        System.out.println("h:"+d.heightB+":"+d.heightE);
        System.out.println("w:"+d.widthB+":"+d.widthE);
        System.out.println();
        /*dungeon[d.heightB - 1][d.widthB - 1] = "╔";
        dungeon[d.heightB - 1][d.widthE + 1] = "╗";
        dungeon[d.heightE + 1][d.widthB - 1] = "╚";
        dungeon[d.heightE + 1][d.widthE + 1] = "╝";//*/

        dungeon[d.heightB - 1][d.widthB - 1] = "#";
        dungeon[d.heightB - 1][d.widthE + 1] = "#";
        dungeon[d.heightE + 1][d.widthB - 1] = "#";
        dungeon[d.heightE + 1][d.widthE + 1] = "#";
        //top & bottom
        /*for (int x = d.widthB; x <= d.widthE; x++) {
            dungeon[d.heightB - 1][x] = "═";
            dungeon[d.heightE + 1][x] = "═";
        }*/
        for (int x = d.widthB; x <= d.widthE; x++) {
            dungeon[d.heightB - 1][x] = "#";
            dungeon[d.heightE + 1][x] = "#";
        }
        //left & right
        /*for (int y = d.heightB; y <= d.heightE; y++) {
            dungeon[y][d.widthB - 1] = "║";
            dungeon[y][d.widthE + 1] = "║";
        }//*/
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

    private void initializeDungeon(int h, String[][] dungeon) {
        for (int i = 0; i <= h; i++) {
            Arrays.fill(dungeon[i], " ");
        }
    }
}
