import java.util.ArrayList;

public class Main {
    public static void main(String args[]) {
        int h = 20;
        int w = 80;
        ArrayList<BSPDungeon> bspDungeons = new ArrayList<>();
        ArrayList<VoronoiDungeon> voronoiDungeons = new ArrayList<>();

        // BSP-luolasto
        long start = System.nanoTime();
        for (int i = 0; i < 100000; i++) {
            bspDungeons.add(new BSPDungeon(h,w,5,50));
        }
        long end = System.nanoTime();
        long bspTime = end-start;

        // Voronoi-luolasto
        start = System.nanoTime();
        for (int i = 0; i < 100000; i++) {
            voronoiDungeons.add(new VoronoiDungeon(h,w));
        }
        end = System.nanoTime();
        long voronoiTime = end-start;

        Visualisation viz = new Visualisation(bspDungeons, bspTime, voronoiDungeons, voronoiTime);
        viz.run();
    }
}
