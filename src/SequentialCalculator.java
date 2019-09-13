import java.io.FileWriter;
import java.io.PrintWriter;

public class SequentialCalculator {
    CloudData cd = new CloudData();
    static float start = 0;
    float sumx = 0;
    float sumy = 0;
    int length = 1;
    static String time = "time:\t";

    /** fills the cloud data object with the data*/
    public void populate(String filename){
        cd.readData(filename);
        length = cd.dim();
    }

    /** computes sequentially the classification and average*/
    public void compute(){
        sumx=0;
        sumy=0;
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

    /**writes time to a file*/
    public void writeTime(String fileName, String[] strings){
        try {
            FileWriter fileWriter = new FileWriter(fileName);
            PrintWriter pw = new PrintWriter(fileWriter);
            for (String s: strings) {
                pw.write(s+"\n");
            }
            pw.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }


    private static void tick(){
        start = System.currentTimeMillis();
    }
    private static float tock(){
        return (System.currentTimeMillis() - start);
    }


    public static void main(String[] args) {
        SequentialCalculator sc = new SequentialCalculator();
        String temp[] = args[0].split(" ");
        String infile = temp[0];
        String outfile = temp[1];
        /*String[] sA = new String[5];*/
        /*int count = -1;
        for (int g = 750; g <=1500; g+=250) {
            count++;

            String file = "IN20" + "-" + g + "-" + g + ".txt";*/
            sc.populate(infile);

            /*for (int i = 0; i < 9; i++) {
                sc.compute();
            }
            double sum = 0;
            for (int i = 0; i < 50; i++) {*/
                System.gc();
                double s = System.currentTimeMillis();
                sc.compute();
                double e = System.currentTimeMillis();
                double time =((e - s) / 1000);
                sc.write(outfile,time+"");
/*
            }
            System.out.println(sum / 50);
            sA[count]=file+"\t\t\ttime:\t"+(sum/50);

        }
        sc.writeTime("large.txt",sA);*/

    }

}
