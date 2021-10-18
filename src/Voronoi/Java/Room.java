public class Room {
    int heightB, heightE, widthB, widthE, centerY, centerX;
    Point point;
    Room leftChild;
    Room rightChild;

    public Room(int heightB, int heightE, int widthB, int widthE, int centerY, int centerX, Point p) {
        this.heightB = heightB;
        this.heightE = heightE;
        this.widthB = widthB;
        this.widthE = widthE;
        this.centerY = centerY;
        this.centerX = centerX;
        this.point = p;
    }

    public String toString() {
        return "height: "+heightB+"->"+heightE+", width: "+widthB+"->"+widthE+"\n" +
                "point: y:"+point.y+", x:"+point.x+"\n" +
                "centerY: "+centerY+", centerX: "+centerX;
    }
}
