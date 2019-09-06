public class SequentialCalculator {
    CloudData cd = new CloudData();
    static float start = 0;
    float sumx = 0;
    float sumy = 0;
    int length = 1;
    static String time = "time:\t";

    public void populate(String filename){
        cd.readData(filename);
        length = cd.dim();
    }

    public void compute(){

        int[] pos = new int[3];
        for (int i = 0; i < length; i++) {
            cd.locate(i, pos);
            sumx+=cd.advection[pos[0]][pos[1]][pos[2]].x;
            sumy+=cd.advection[pos[0]][pos[1]][pos[2]].y;
            cd.classify(pos);
        }
    }

    public void write(String filename, String t){
        cd.writeData(filename,new Vector(sumx/length,sumy/length),t);
    }

    private static void tick(){
        start = System.currentTimeMillis();
    }
    private static float tock(){
        return (System.currentTimeMillis() - start) / 1000.0f;
    }


    public static void main(String[] args) {
        SequentialCalculator sc = new SequentialCalculator();
        sc.populate("I1");
        System.gc();
        tick();
        sc.compute();
        time = time +tock();
        sc.write("O1",time);

    }
}
