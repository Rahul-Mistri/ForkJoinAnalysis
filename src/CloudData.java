

import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.io.FileWriter;
import java.io.PrintWriter;

public class CloudData {

	Vector [][][] advection; // in-plane regular grid of wind vectors, that evolve over time
	float [][][] convection; // vertical air movement strength, that evolves over time
	int [][][] classification; // cloud type per grid point, evolving over time
	int dimx, dimy, dimt; // data dimensions

	// overall number of elements in the timeline grids
	int dim(){
		return dimt*dimx*dimy;
	}
	
	// convert linear position into 3D location in simulation grid
	void locate(int pos, int [] ind)
	{
		ind[0] = (int) pos / (dimx*dimy); // t
		ind[1] = (pos % (dimx*dimy)) / dimy; // x
		ind[2] = pos % (dimy); // y
	}
	
	// read cloud simulation data from file
	void readData(String fileName){ 
		try{ 
			Scanner sc = new Scanner(new File(fileName), "UTF-8");
			
			// input grid dimensions and simulation duration in timesteps
			dimt = sc.nextInt();
			dimx = sc.nextInt(); 
			dimy = sc.nextInt();
			
			// initialize and load advection (wind direction and strength) and convection
			advection = new Vector[dimt][dimx][dimy];
			convection = new float[dimt][dimx][dimy];
			for(int t = 0; t < dimt; t++)
				for(int x = 0; x < dimx; x++)
					for(int y = 0; y < dimy; y++){
						advection[t][x][y] = new Vector();
						advection[t][x][y].x = sc.nextFloat();
						advection[t][x][y].y = sc.nextFloat();
						convection[t][x][y] = sc.nextFloat();
					}
			
			classification = new int[dimt][dimx][dimy];
			sc.close(); 
		} 
		catch (IOException e){ 
			System.out.println("Unable to open input file "+fileName);
			e.printStackTrace();
		}
		catch (java.util.InputMismatchException e){ 
			System.out.println("Malformed input file "+fileName);
			e.printStackTrace();
		}
	}


	float getLocalAverage(int [] arr){
		int num= 0;
		float sumx = 0;
		float sumy = 0;
		for (int i = Math.max(0, arr[1]-1); i < Math.min(dimx,arr[1]+2); i++) {
			for (int j = Math.max(0, arr[2]-1); j < Math.min(dimy,arr[2]+2); j++) {
				sumx = sumx + advection[arr[0]][arr[i]][arr[j]].x;
				sumy = sumy + advection[arr[0]][arr[i]][arr[j]].y;
				num++;
			}
		}
		Vector v = new Vector();
		v.x = sumx/num;
		v.y = sumy/num;
		return v.getMag();
	}

	void classify(int [] arr){
		float ave = getLocalAverage(arr);
		if(ave<convection[arr[0]][arr[1]][arr[2]]){
			classification[arr[0]][arr[1]][arr[2]]= 0;
		}
		else if(ave>0.2){
			classification[arr[0]][arr[1]][arr[2]]= 1;
		}
		else{
			classification[arr[0]][arr[1]][arr[2]]= 2;
		}
	}
	
	// write classification output to file
	void writeData(String fileName, Vector wind){
		writeData(fileName, wind, "time:\tn/a");
	}
	void writeData(String fileName, Vector wind, String time){
		 try{ 
			 FileWriter fileWriter = new FileWriter(fileName);
			 PrintWriter printWriter = new PrintWriter(fileWriter);
			 printWriter.printf("%d %d %d\n", dimt, dimx, dimy);
			 printWriter.printf("%f %f\n", wind.x, wind.y);
			 
			 for(int t = 0; t < dimt; t++){
				 for(int x = 0; x < dimx; x++){
					for(int y = 0; y < dimy; y++){
						printWriter.printf("%d ", classification[t][x][y]);
					}
				 }
				 printWriter.printf("\n");
		     }

			 printWriter.print(time);
				 
			 printWriter.close();
		 }
		 catch (IOException e){
			 System.out.println("Unable to open output file "+fileName);
				e.printStackTrace();
		 }
	}
}
