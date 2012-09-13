package parallelj.birt;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class IsEmpty {
	String fileName;
	
	public IsEmpty(String fileName) {
		super();
		this.fileName = fileName;
	}

	public Boolean findContent() throws IOException{
		File file = new File(fileName);
		BufferedReader input = new BufferedReader (new FileReader(file));
	    String text;	        			
	    text = input.readLine();
	    if(text != null){
	        int i = -1;
         	do{
         		String testString = "-[1]";
         		i = text.indexOf(testString);
         		if ( i != -1){
         			return false;
         		}
         	}while ((text = input.readLine()) != null);
        }
	    return true;
	}
}