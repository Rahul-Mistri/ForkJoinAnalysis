import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class ParallelCalculator extends RecursiveTask<Vector> {

    static final ForkJoinPool fjPool = new ForkJoinPool();
    static CloudData cd = new CloudData();

    static void populate(String filename){
        cd.readData(filename);
    }

    static Vector sum(){
        return fjPool.invoke(new ParallelCalculator(0,cd.dim()));
    }

    static void write(String filename, String t, Vector v ){
        cd.writeData(filename,new Vector(v.x/cd.dim(),v.y/cd.dim()),t);
    }


    int lo;
    int hi;
    static final int SEQUENTIAL_CUTOFF=1000000000;


    ParallelCalculator( int l, int h) {
        lo=l; hi=h;
    }

    static Vector vSum(Vector a, Vector b){
        a.x += b.x;
        a.y += b.y;
        return a;
    }


    protected Vector compute(){// return answer - instead of run
        if((hi-lo) < SEQUENTIAL_CUTOFF) {
            int [] pos = new int[3];
            float sumx =0;
            float sumy =0;
            for(int i=lo; i < hi; i++){
                cd.locate(i,pos);
                sumx += cd.advection[pos[0]][pos[1]][pos[2]].x;
                sumy += cd.advection[pos[0]][pos[1]][pos[2]].y;
                cd.classify(pos);
            }
            return new Vector(sumx,sumy);
        }
        else {
            ParallelCalculator left = new ParallelCalculator(lo,(hi+lo)/2);
            ParallelCalculator right= new ParallelCalculator((hi+lo)/2,hi);

            // order of next 4 lines
            // essential – why?
            left.fork();
            Vector rightAns = right.compute();
            Vector leftAns  = left.join();
            return vSum(rightAns, leftAns);
        }
    }




    static void writeTime(String fileName, String[] strings){
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

    public static void main(String[] args) {
        String[] sA = new String[4];
        int count = -1;
        for (int g = 750; g <=1500; g+=250) {
            count++;

            String file = "IN20" + "-" + g + "-" + g + ".txt";
            populate(file);
            System.gc();
            Vector average;
            for (int i = 0; i <9; i++) {
                average = sum();
            }
            double sum=0;
            for (int i = 0; i < 50; i++) {
                System.gc();
                double s = System.currentTimeMillis();
                average = sum();
                double e = System.currentTimeMillis();
                sum+=((e-s)/1000);
                //write("parallel_run_"+(i+1),"Sequential Cut Off: "+SEQUENTIAL_CUTOFF+"\ntime: "+times[i], average);
            }
            System.out.println(sum/50);
            sA[count]=file+"\t\t\tTime:\t"+(sum/50);



        }
        writeTime("large"+SEQUENTIAL_CUTOFF, sA);

    }
}
