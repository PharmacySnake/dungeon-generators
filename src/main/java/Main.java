import java.util.Arrays;
import java.util.LinkedList;

public class Main {
    public static void main(String args[]) {
        //long start = System.nanoTime();
        int h = 20; //20
        int w = 80; //80 40
        //BSPDungeon d = new BSPDungeon(h,w,5,50);
        VoronoiDungeon v = new VoronoiDungeon(h,w);

        /*for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                System.out.print(v.dungeon[y][x]);
            }
            System.out.println();
        }
        System.out.println();
        /*for (Edge e : v.edges) {
            System.out.println("sY: "+e.start.y+" sX: "+e.start.x+" eY: "+e.end.y+" eX: "+e.end.x);
            System.out.println("sY: "+Math.round(e.start.y)+" sX: "+Math.round(e.start.x)+" eY: "+Math.round(e.end.y)+" eX: "+Math.round(e.end.x));
            //System.out.println("yInt: "+e.yint+", slope: "+e.slope+", direction: y:"+e.direction.y+", x:"+e.direction.x);
            System.out.println("direction: y:"+e.direction.y+", x:"+e.direction.x);
            System.out.println();
            //
            //System.out.println(e);
        }*/


    }
}
