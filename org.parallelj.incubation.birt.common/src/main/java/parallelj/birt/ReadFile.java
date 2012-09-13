package parallelj.birt;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class ReadFile {
	Date[] Time;
	String[] processorid;
	String[] elementid;
	String[] timing;
	String[] state;
	int[]endtoend;
	int[]createtostart;
	int[]starttoend;
	String[] programname;

	String[] element;
	int num;
	String[] procedurelist;


	int[][] calllist;
	
	public String[] getProgramname() {
		return programname;
	}

	
	public String[] getProcedurelist() {
		return procedurelist;
	}

	String pathToRead;
	
	
	public ReadFile(String pathToRead) {
		this.pathToRead = pathToRead;
	}


	public void readinfo(){
		
		File file = new File(pathToRead);
        ArrayList<Date> timeList = new ArrayList<Date>();
        ArrayList<String> elementidList = new ArrayList<String>();
        ArrayList<String> programnameList = new ArrayList<String>();
        ArrayList<String> processoridList = new ArrayList<String>();
        
        ArrayList<String> timingList = new ArrayList<String>();
        ArrayList<String> stateList = new ArrayList<String>();
                
        if(file.exists()){
        	if(file.isFile()){
        		try{
        			BufferedReader input = new BufferedReader (new FileReader(file));
        			String text;
        			
        			int j = 0;
        			
        			//Read .log file line by line
        			text = input.readLine();
        			if(text != null){
              			do {
            				//Split the elements in each line
            				String textline[]=text.split(" ");
            				
            				if (textline.length > 6){
            				
            				//Read the time of create this line
            				DateFormat datetime = new SimpleDateFormat("dd-MM HH:mm:ss,SSS");
            				try {
								timeList.add(j,datetime.parse(textline[2]+" "+textline[3]));
							} catch (ParseException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
    						            				
            				if (textline[0].equals("INFO")){
                				String textsp[] = textline[textline.length-4].split("\t");
                				if (textsp.length != 1){
            						elementidList.add(j,textsp[0]);
            						
            						//Read the name of this program
            						if (textsp[0].equals("-I0001")){
            							String name[]=textline[textline.length-1].split("\\.");
            							int n = name.length;
            							programnameList.add(name[n-1]);
            						}
            						//For this line, there is no processorid,timing nor state.
            						processoridList.add(j,null);
                                    timingList.add(j,null);
                                    stateList.add(j,null);
            						j++;
            					}
            					
            					else{
            						//Read the processorid, elementid, timing and state
            						processoridList.add(j,textline[textline.length-5]);
                                    elementidList.add(j,textline[textline.length-4]);
                                    timingList.add(j,textline[textline.length-3]);
                                    stateList.add(j,textline[textline.length-2]);
                                   
                                    j++;
                                }
                			}
                			
                				else{
                					String textsp[] = textline[textline.length-1].split("\t");
                					processoridList.add(j,null);
                                    elementidList.add(j,textsp[0]);
                                    timingList.add(j,null);
                                    stateList.add(j,null);
                                   
                                    j++;
                				}
            				}
                				
                		}while ((text = input.readLine()) != null);
        		
        		num = elementidList.size();
        		
        		//According to the size of array set the variable for calculate	
        		Time = new Date[num];
        	    processorid = new String[num];
        	    elementid = new String[(num+3)];
        	    timing = new String[num];
        	    state = new String[num];
        	    endtoend = new int[(num+3)];
        	    createtostart = new int[(num+3)];
        	    starttoend = new int[(num+3)];
        	    element = new String[(num+3)];
        	    programname = new String[programnameList.size()];
        	    
        	    for(int i = 0; i<j; i++){
        	    	Time[i] = timeList.get(i);
        			processorid[i] = processoridList.get(i);
        			elementid[i] = elementidList.get(i);
        			timing[i] = timingList.get(i);
        			state[i] = stateList.get(i);
        		}

        	    for(int i = 0; i < programnameList.size() ; i++){
        	    	programname[i] = programnameList.get(i);
        	    }
        	    
        	    Calcul calcul= new Calcul(num, programname);
        	    //Send the information read in the file for calculating
        	    //Obtain the result used in report
        	    endtoend =calcul.calcultimeendtoend(Time,elementid);
                createtostart = calcul.calcultimecreatetostart(timing,elementid);
                starttoend = calcul.calcultimestarttoend(timing,elementid);
                element = calcul.calculelement(elementid, processorid,state);
                procedurelist = calcul.getProcedurelist();
                calllist = calcul.getCalllist();
                
        	}	
        	} 
        		catch(IOException ioException){
        			System.err.println("File Error!");
        		}
        	}
        }
        
        //If file does not exist
        else{
        	System.err.println("Does not exist!");
        }
    }
}
