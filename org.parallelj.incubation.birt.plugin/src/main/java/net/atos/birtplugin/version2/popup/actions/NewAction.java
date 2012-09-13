package net.atos.birtplugin.version2.popup.actions;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import net.atos.birtplugin.version2.Activator;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.core.resources.IResource;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IActionDelegate;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.browser.IWebBrowser;
import org.eclipse.ui.browser.IWorkbenchBrowserSupport;
import org.eclipse.ui.part.FileEditorInput;

import parallelj.birt.IsEmpty;
import parallelj.birt.ReadFile;
import parallelj.birt.ToBirt;


public class NewAction implements IObjectActionDelegate {

	private IStructuredSelection select;
	
	/**
	 * Constructor for Action1.
	 */
	public NewAction() {
		super();
	}

	/**
	 * @see IObjectActionDelegate#setActivePart(IAction, IWorkbenchPart)
	 */
	public void setActivePart(IAction action, IWorkbenchPart targetPart) {
		//shell = targetPart.getSite().getShell();
	}

	/**
	 * @see IActionDelegate#run(IAction)
	 */
	public void run(IAction action) {
		Object obj = select.getFirstElement();
		
		
		//Get the directory of the project
		String path;
		try {
			IResource file = (IResource) obj; 
			path = file.getLocation().toOSString();
		 }
		
		catch (ClassCastException e1) {
			try {
				IJavaElement element = (IJavaElement) obj;
				path = element.getResource().getLocation().toOSString();
			}
			catch (ClassCastException e2) {
				try {
					FileEditorInput edit = (FileEditorInput)obj;
					path = edit.getPath().toOSString();
				}
				catch (ClassCastException e3) {
					path = obj.getClass().toString();
				}
			}
		}
		
		//Find all the .log files
		ArrayList<String> al=new ArrayList<String>();
		
		File dir = new File(path); 
		
		for (File file : dir.listFiles()) {
	    	if (file.getName().endsWith((".jpg"))) {
	    		file.delete();
	    	}
	    }

		for (File file : dir.listFiles()) {
			if (file.getName().endsWith(("logs"))) {
				al.add(file.getName());
			}
		}
		
		//If there is no .log file
		int n = al.size();
		if(n == 0){
			JOptionPane.showMessageDialog(null,"No .log file found.");
		}
		
		//Add all the .log files in the array
		else{
			al.clear();
			dir = new File(path+"/logs");
		    for (File file : dir.listFiles()) {
		    	if (file.getName().endsWith((".log"))) {
		    		al.add(file.getName());
		    	}
		    }
		    n = al.size();
		    for (int i = n-1; i >= 0; i--){
		    	try {
		    		IsEmpty isEmpty = new IsEmpty(path+"/logs/"+(String) al.get(i));
					if(isEmpty.findContent()){
						al.remove(i);
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		    }
		    n = al.size();
		    
		    String[] posibilities = null;
		    posibilities = new String[n];
		    
		    for (int i = 0; i < n; i++){
		    	String pathToRead = path+"/logs/"+(String) al.get(i);
		    	ReadFile readfile = new ReadFile(pathToRead);
		    	readfile.readinfo();
		    	String[] programname = readfile.getProgramname();
				posibilities[i] = "["+programname[0]+(String) al.get(i);
							
		    }
		    
		    //Create a dialog which shows all the .log files to be chosen
		    String s = (String)JOptionPane.showInputDialog(null,
		    		"Choose the .log file...","Create BIRT Report",
		    		JOptionPane.PLAIN_MESSAGE,null,posibilities,null);
		    
		    if ((s != null) && (s.length() > 0)) {
		    	String[] filename = s.split("]");
		    	File   f   =   new   File(path+ "/"+filename[1]); 
				if(!f.isDirectory()){ 
				f.mkdir(); 
				} 
				
		    	

		    	String pathToRead=path+"/logs/"+filename[1];
	    		String pathToBirt=path+ "/"+filename[1];
		    	
		    		ToBirt tobirt= new ToBirt(pathToRead,pathToBirt);
		    		try {
						tobirt.tobirtaction();
					} catch (BirtException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
		    		ReadFile readfile = new ReadFile(pathToRead);
			    	readfile.readinfo();
	
		    	
		    	//Create a web page called Index
		    	String name ="report.html";
		    	String folder=pathToBirt+"/";
		    	
		    	String content=" <p> <a href='"+folder+"mybirt1.html'target='_blank'>BIRT Report Table</a> </p>"
        		+ " <p> <a href='"+folder+"mybirt2.html'target='_blank'>BIRT Report Line Chart</a> </p>"
        		+ " <p> <a href='"+folder+"mybirt3.html'target='_blank'>BIRT Report Bar Chart</a> </p>"
        		+ " <p> <a href='"+folder+"Procedure.html'target='_blank'>Detail of Procedure</a> </p>";
		    	
		       	File file = new File(folder+name);
		    	
		    	try {
		    		file.createNewFile();
		    		PrintStream printStream = new PrintStream(new FileOutputStream(file));
		    		printStream.println("<html><head><title>report</title></head><body>"
		    		+"<div align='center'>"
		    		+ content
		    		+"</div>"
		    		+"</body></html>");
		    	} catch (IOException e) {
		    		e.printStackTrace();
		    	}
		    	
		    	//Create a web page includes all the name of procedures
		    	String newcontent = null;
		    	name ="procedure.html";

		       	file = new File(folder+name);
		       	
		       	for (int i = 1; ;i++){
		       		
		       		if (readfile.getProcedurelist()[i] ==null){
		       			break;
		       		}
		       		else{
		       			if (i == 1){
		       				newcontent = " <p> <a href='"+folder+"report/"+readfile.getProcedurelist()[i]
		       						+".html'target='_blank'>BIRT Report "+readfile.getProcedurelist()[i]+"</a> </p>";
		       			}
		       			else{
		       				newcontent = newcontent+" <p> <a href='"+folder+"report/"+readfile.getProcedurelist()[i]
		       						+".html'target='_blank'>BIRT Report "+readfile.getProcedurelist()[i]+"</a> </p>";
		       			}
		       		}
		       	}
		       	
	
		    	try {
		    		file.createNewFile();
		    		PrintStream printStream = new PrintStream(new FileOutputStream(file));
		    		printStream.println("<html><head><title>procedure</title></head><body>"
		    		+"<div align='center'>"
		    		+ newcontent
		    		+"</div>"
		    		+"</body></html>");
		    	} catch (IOException e) {
		    		e.printStackTrace();
		    	}
		    	
		    	//Open an Eclipse browser
		    	IWorkbenchBrowserSupport browserSupport = Activator.getDefault()
		    			.getWorkbench().getBrowserSupport();
		    	
		    	try {
		    		IWebBrowser browser = browserSupport
		    				.createBrowser(IWorkbenchBrowserSupport.LOCATION_BAR, null,
		    						"BIRT Report Browser", "BIRT Report Browser");
		    		URL url = new URL("file://"+folder+"report.html");
		    		browser.openURL(url);
		    	} catch (PartInitException e) {
		    		// TODO Auto-generated catch block
		    		e.printStackTrace();
		    	} catch (MalformedURLException e) {
		    		// TODO Auto-generated catch block
		    		e.printStackTrace();
		    	}
		    }
		}
	}

	/**
	 * @see IActionDelegate#selectionChanged(IAction, ISelection)
	 */
	public void selectionChanged(IAction action, ISelection selection) {
		if (selection instanceof IStructuredSelection){
			select = (IStructuredSelection) selection;
		}
	}
}
