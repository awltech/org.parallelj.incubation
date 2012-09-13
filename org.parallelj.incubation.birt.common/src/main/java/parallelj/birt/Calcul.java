package parallelj.birt;

import java.util.Date;

public class Calcul {
	
	private int[] timeused;
	public String[] procedurelist;
	int[][] calllist;
	
	
	public String[] getProcedurelist() {
		return procedurelist;
	}

	public int[][] getCalllist() {
		return calllist;
	}

	
	int num;
	String[] programname;
	
	public Calcul( int num, String[] programname)
	{
		
		this.num=num;
		this.programname=programname;
	}

	//Calculate the meaning of each element
	//The meaning of the element includes the name of procedure and which time it execute
	public String[] calculelement(String elementid[], String processorid[],String state[]) {
		
		String[] element;
		element = new String[num+3];
		
		String[][] nameprocedure;
		nameprocedure= new String[num][num];
		
		String[] program;
		program = new String[num];
		
		procedurelist = new String[num];
		calllist = new int[num][num];
		
		int m = 0;
		
		for (int j = 0;j < num ; j++){
			//If Build Program
			if(elementid[j].equals("-I0001")){
				element[j] = "Build Program ["+programname[j-1];
			}
			
			//If Warning
			else if(elementid[j].equals("-W0001") || elementid[j].equals("-W0002")
					|| elementid[j].equals("-W0003")	|| elementid[j].equals("-W0004")){
				element[j] = elementid[j];
			}
			
			//"0.0.0.0/0.0.0.0" is the first line when a program starts
			else if (elementid[j].equals("0.0.0.0/0.0.0.0")){
				element[j] = "Start Program";
			}
			
			else{
				
				//num1 and num2 refer to different numbers in element id
				int num1,num2;
				
				//Processor id equals 0
				//Start of program or procedure
				if (processorid[j].equals("-[0]")){
					String temp1[] = elementid[j].split("/");
					
					//Start of program
					if (temp1[0].equals("0.0.0.0")){
						String[] proname = state[j].split("\\.");
						int length = proname.length;
						element[j] = proname[length-1];
						String[] temp2 = temp1[1].split("\\.");
						num1 = Integer.parseInt(temp2[0]);
						program[num1] = proname[length-1];
					}
					
					//Start of procedure
					else{
						String[] temp2 = temp1[1].split("\\.");
						num1 = Integer.parseInt(temp2[0]);
						num2 = Integer.parseInt(temp2[1]);
						String[] procedure = state[j].split(":");
						element[j] = procedure[0];
						nameprocedure[num1][num2] = procedure[0];
					}
				}
				
				//Execution of procedure
				else{
					String temp1[] = elementid[j].split("/");
					String[] temp2 = temp1[1].split("\\.");
					
					//Finish of procedure
					if (Integer.parseInt(temp2[1]) == 0){
						num1 = Integer.parseInt(temp2[0]);
						element[j] = "Finish " +program[num1];
					}
					
					//Calculate the times of each procedure and the meaning of procedure
					else{
						num1 = Integer.parseInt(temp2[0]);
						num2 = Integer.parseInt(temp2[1]);
						element[j] = nameprocedure[num1][num2] + "_" + Integer.parseInt(temp2[3]);
						int temp = -1;
						for (int i = 0; i <= m; i++){
							if (procedurelist[i] == nameprocedure[num1][num2]){
									temp = i;							
							}
						}
						
						//Put all the procedures in one list
						if (temp == -1){
							m++;
							procedurelist[m] = nameprocedure[num1][num2];
							temp = m;
						}
						
						//Record the order of the procedure
						calllist[temp][Integer.parseInt(temp2[3])] = j;
					}
				}
			}
		}
		return element;
	}
	
	//Calculate the difference between the end of the last procedure and this procedure
	public int[] calcultimeendtoend(Date time[], String elementid[]){
		timeused = new int[num+3];
		
		for (int j = 0;j < num-1; j++){
			timeused[0] = 0;
			timeused[j+1] = (int) (time[j+1].getTime()-time[j].getTime());
		}
		return timeused;
	}

	//Calculate the difference between the creation and the start of the procedure
	public int[] calcultimecreatetostart(String timingstring[], String elementid[]){
		timeused = new int[num+3];
		
		for (int j = 0;j < num; j++){
			if(timingstring[j] == null && elementid [j] != null){
				timeused[j] = 0;
			
			}
			else{
				String[] eachtime = timingstring[j].split(":");
				timeused[j] = (int) (Long.parseLong(eachtime[1])-Long.parseLong(eachtime[0]));
			}
		}
		return timeused;
	}
	
	//Calculate the difference between the start and the completion of the procedure
	public int[] calcultimestarttoend(String timingstring[], String elementid[]){
		timeused = new int[num+3];
		
		
		for (int j = 0;j < num ; j++){
			
			//Build program or warning
			if(timingstring[j] == null &&  elementid[j] != null){
				timeused[j] = 0;
			}
			
			else{
				String[] eachtime = timingstring[j].split(":");
				timeused[j] = Integer.parseInt(eachtime[3]);
			}
		}
		return timeused;
	}

	//Calculate the value of average, maximum, and minimum time of each time of each procedure
	public int[] count(int[] time,int m,int n) {
		 int[] result = new int[3];
		 int max = time[calllist[m][1]], min = time[calllist[m][1]], sum = time[calllist[m][1]];
		 int a = 1;
		 for (int i = 2; i <= n; i++) {
			
			 max = max > time[calllist[m][i]] ? max : time[calllist[m][i]];
			 min = min < time[calllist[m][i]] ? min : time[calllist[m][i]];
			 sum += time[calllist[m][i]];
			 a++;
		}
		 
		 result[0] = max;
		 result[1] = min;
		 result[2] = sum / a;
		 return result;
	}  
}
