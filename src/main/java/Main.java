import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;

public class Main {
    public static void main(String args[]) {
        //long start = System.nanoTime();
        int h = 20; //20
        int w = 80; //80 40
        ArrayList<BSPDungeon> bspDungeons = new ArrayList<>();
        ArrayList<VoronoiDungeon> voronoiDungeons = new ArrayList<>();
        long start = System.nanoTime();
        for (int i = 0; i < 100000; i++) {
            bspDungeons.add(new BSPDungeon(h,w,5,50));
        }
        long end = System.nanoTime();
        long bspTime = end-start;
        System.out.println("nano: "+(bspTime));

        start = System.nanoTime();
        for (int i = 0; i < 100000; i++) {
            voronoiDungeons.add(new VoronoiDungeon(h,w));
        }
        end = System.nanoTime();
        long voronoiTime = end-start;
        System.out.println("nano: "+(voronoiTime));//*/

        Visualisation viz = new Visualisation(bspDungeons, bspTime, voronoiDungeons, voronoiTime);
        viz.run();
    }
}
