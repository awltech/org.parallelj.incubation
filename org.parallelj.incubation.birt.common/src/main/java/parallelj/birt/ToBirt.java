package parallelj.birt;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.logging.Level;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.framework.Platform;
import org.eclipse.birt.report.engine.api.EngineConfig;
import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.api.HTMLActionHandler;
import org.eclipse.birt.report.engine.api.HTMLEmitterConfig;
import org.eclipse.birt.report.engine.api.HTMLRenderOption;
import org.eclipse.birt.report.engine.api.HTMLServerImageHandler;
import org.eclipse.birt.report.engine.api.IReportEngine;
import org.eclipse.birt.report.engine.api.IReportEngineFactory;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.IRunAndRenderTask;
import org.eclipse.birt.report.model.api.DesignConfig;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.DesignEngine;
import org.eclipse.birt.report.model.api.ElementFactory;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.IDesignEngineFactory;
import org.eclipse.birt.report.model.api.PropertyHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.ScriptDataSetHandle;
import org.eclipse.birt.report.model.api.ScriptDataSourceHandle;
import org.eclipse.birt.report.model.api.SessionHandle;
import org.eclipse.birt.report.model.api.StructureFactory;
import org.eclipse.birt.report.model.api.TableHandle;
import org.eclipse.birt.report.model.api.TextItemHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.command.ContentException;
import org.eclipse.birt.report.model.api.command.NameException;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.elements.structures.ComputedColumn;
import org.eclipse.birt.report.model.api.metadata.IMetaDataDictionary;

import com.ibm.icu.util.ULocale;

public class ToBirt {
	
	 IMetaDataDictionary dict = null;
	 ReportDesignHandle design = null;
	 ElementFactory factory = null;
	ScriptDataSetHandle dataSetHandle = null;
	
	String Dataset = "Data Set";
	ComputedColumn cs1, cs2, cs3, cs4 = null;
	ComputedColumn cs5, cs6, cs7, cs8 = null;
	PropertyHandle computedSet =null;
	String pathToRead;
	String pathToBirt;
	ReadFile readfile;
	

	public ToBirt(String pathToRead,String pathToBirt) {
		this.pathToRead = pathToRead;
		this.pathToBirt = pathToBirt;
		this.readfile = new ReadFile(pathToRead);
		this.readfile.readinfo();
		
	}

	public void tobirtaction() throws BirtException,IOException{

		
		
		File   f   =   new   File(pathToBirt+ "/report"); 
		if(!f.isDirectory()){ 
		f.mkdir(); 
		} 
	
		createReport();
		createMasterPage();
		createDataSource();
		int i;
		int numcall;
		ArrayList<Integer> eteaveList = new ArrayList<Integer>();
		ArrayList<Integer> ctsaveList = new ArrayList<Integer>();
		ArrayList<Integer> steaveList = new ArrayList<Integer>();
		
		for (i = 1; ; i++){
			numcall = 0;
			if (readfile.procedurelist[i] == null){
				break;
			}
		for (int n = 1; ; n++){
			if (readfile.calllist[i][n] != 0){
				numcall++;
			}
			else
				break;
		}
	
		int[] result1 = this.count(readfile.endtoend, i, numcall);
		int[] result2 = this.count(readfile.createtostart, i, numcall);
		int[] result3 = this.count(readfile.starttoend, i, numcall);
		
		//Add the result to the data used in the report
		
		eteaveList.add(result1 [2]);
		ctsaveList.add(result2 [2]);
		steaveList.add(result3 [2]);
		}
				
		createDataSet(i,eteaveList,ctsaveList,steaveList);
		
		//Add title to table report
		TextItemHandle text = factory.newTextItem("text");
		text.setContentType(DesignChoiceConstants.TEXT_CONTENT_TYPE_HTML);
		text.setContent("<FONT size='5' color='BLACK'><B>["
		+readfile.programname[0]+" Table Report</B></FONT>");
		design.getBody().add(text);
		
		//Create a table
		TableHandle table = new NewTable(cs1, cs2, cs3, cs4, computedSet).createMyTable(factory,Dataset);
		table.setProperty("marginTop", "20pt");	
		//Add table into report design
		design.getBody().add(table);
		//Save the report
		String cheminRapport1 = pathToBirt + "/report/mybirt1.rptdesign";
		design.saveAs(cheminRapport1);
		//Save the report as html
		saveHtml(pathToBirt,1);
		//Drop the table for add new items
		table.drop();
		text.drop();
		
		//Create a line chart
		
		ExtendedItemHandle linechart = (new NewLineChart()).createMyChart(factory,Dataset,i,readfile.programname[0]);
		linechart.setProperty("marginTop", "20pt");
		//Add line chart into report design
		design.getBody( ).add(linechart);
		//Save the report
		String cheminRapport2 = pathToBirt + "/report/mybirt2.rptdesign";
		design.saveAs(cheminRapport2);
		//Save the report as html
		saveHtml(pathToBirt,2);
		//Drop the line chart
	    linechart.drop();
	    
	    //Create a bar chart
	    ExtendedItemHandle barchart = (new NewBarChart()).createMyChart(factory,Dataset,i,readfile.programname[0]);
	    barchart.setProperty("marginTop", "20pt");
	    //Add bar chart into report design
		design.getBody( ).add(barchart);
		//Save the report
		String cheminRapport3 = pathToBirt + "/report/mybirt3.rptdesign";
		design.saveAs(cheminRapport3);
		//Save the report as html
		saveHtml(pathToBirt,3);
		//Drop the line chart
		barchart.drop();
		
		
		//Read the information from procedure list to calculate the value of statistics 
		
		for (i = 1; ; i++){
			numcall = 0;
			if (readfile.procedurelist[i] == null){
				break;
			}
		for (int n = 1; ; n++){
			if (readfile.calllist[i][n] != 0){
				numcall++;
			}
			else
				break;
		}

		int[] result1 = this.count(readfile.endtoend, i, numcall);
		int[] result2 = this.count(readfile.createtostart, i, numcall);
		int[] result3 = this.count(readfile.starttoend, i, numcall);
		
		//Add the result to the data used in the report
		readfile.element[readfile.num] = "Max";
		readfile.endtoend[readfile.num] = result1 [0];
		readfile.createtostart[readfile.num] = result2 [0];
		readfile.starttoend[readfile.num] = result3 [0];
		readfile.element[readfile.num+1] = "Min";
		readfile.endtoend[readfile.num+1] = result1 [1];
		readfile.createtostart[readfile.num+1] = result2 [1];
		readfile.starttoend[readfile.num+1] = result3 [1];
		readfile.element[readfile.num+2] = "Ave";
		readfile.endtoend[readfile.num+2] = result1 [2];
		readfile.createtostart[readfile.num+2] = result2 [2];
		readfile.starttoend[readfile.num+2] = result3 [2];
		
		//Create a data set using the arrays we have
		createDataSetforProcedure(i,numcall);
		
		//Create the reports for each procedure
		TableHandle tableforProcedure = new NewTableforProcedure(cs5, cs6, cs7, cs8, computedSet).createMyTable(factory,Dataset+"for Procedure"+i,i);
		ExtendedItemHandle barchartforProcedure = (new NewBarChartforProcedure()).createMyChart(factory,Dataset+"for Procedure"+i,i,readfile.procedurelist);
		
		barchartforProcedure.setProperty("marginTop", "20pt");
		design.getBody( ).add(barchartforProcedure);
		
		tableforProcedure.setProperty("marginTop", "20pt");	
		design.getBody().add(tableforProcedure);
		
		createDataSetforProcedureinDetail(i,numcall);
		TableHandle tableforProcedureindetail = new NewTableforProcedureinDetail(cs5, cs6, cs7, cs8, computedSet).createMyTable(factory,Dataset+"for Procedure in detail"+i,i);
		tableforProcedureindetail.setProperty("marginTop", "20pt");	
		design.getBody().add(tableforProcedureindetail);
		
        
		String cheminRapport = pathToBirt + "/report/"+"mybirt"+(i+3)+".rptdesign";
		design.saveAs(cheminRapport);
		saveHtml(pathToBirt+"/report",(3+i));
		barchartforProcedure.drop();
		design.saveAs(cheminRapport);
		saveHtml(pathToBirt,(3+i));
		createjsp(pathToBirt,(3+i));
		tableforProcedure.drop();
		tableforProcedureindetail.drop();
		}
		        
		design.close();

	}
	
	//Configure BIRT Home, BIRT Report Engine and BIRT Design Engine
	private synchronized void createReport() throws BirtException{
		 
		DesignConfig config = new DesignConfig();
		config.setBIRTHome(null);
		Platform.startup(config);
		IDesignEngineFactory iDesignEngineFactory = (IDesignEngineFactory) Platform
				.createFactoryObject(IDesignEngineFactory.EXTENSION_DESIGN_ENGINE_FACTORY);
		iDesignEngineFactory.createDesignEngine(config);
	
	    SessionHandle session = new DesignEngine(null).newSessionHandle((ULocale) null);
	
	    dict = new DesignEngine(null).getMetaData();
	    design = session.createDesign();
	    factory = design.getElementFactory();
	}
	
	//Set the property of Master Page
	private void createMasterPage() throws ContentException, NameException{
		DesignElementHandle element = factory.newSimpleMasterPage("Master Page");
		try {
			element.setProperty("type", "custom");
			element.setProperty("width", "600pt");
			element.setProperty("height", "400pt");
		} catch (SemanticException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		design.getMasterPages().add(element);
	}
	
	//Create data source which is necessary for data set
	private void createDataSource() throws ContentException, NameException{
		ScriptDataSourceHandle dataSourceHandle = factory.newScriptDataSource( "Data Source" );
		design.getDataSources( ).add( dataSourceHandle );
	}
	
	//Create data set
	private void createDataSet(int i, ArrayList<Integer> eteave,ArrayList<Integer> ctsave,ArrayList<Integer> steave ) throws SemanticException{
			
		dataSetHandle = factory.newScriptDataSet(Dataset);
		dataSetHandle.setDataSource( "Data Source" );
		
		//Create arrays for put in data
		String str =  "i=0;" + "sourcedata = new Array(";
		
		for (int j = 0; j < (i-2); j++){
			str = str + "new Array(4), ";
		}
		str = str + "new Array(4));";
		
		//Read the result calculated into dataSet
		for (int j = 0; j < (i-1); j++){
			str = str + "sourcedata["+j+"][0] = \" "+readfile.procedurelist[j+1]+"\"; sourcedata["+j+"][1] = "+eteave.get(j)+";"
		+ "sourcedata["+j+"][2] = "+ctsave.get(j)+" ; sourcedata["+j+"][3] = "+steave.get(j)+";";
		}
			
		dataSetHandle.setOpen(str);
		
		//Set six rows and put the data in them
		dataSetHandle.setFetch( "if ( i < "+(i-1)+" ){"
		+ "row[\"elementidmeaning\"] = sourcedata[i][0]; row[\"endtoend\"] = sourcedata[i][1];"
		+ "row[\"createtostart\"] = sourcedata[i][2]; row[\"starttoend\"] = sourcedata[i][3];"
		+ "i++; return true;}" + "else return false;" );
		
		//Set the property of rows
		
		cs1 = StructureFactory.createComputedColumn( );
		cs1.setName( "elementidmeaning" );
		cs1.setExpression( "row[\"elementidmeaning\"]" );
		cs1.setDataType( "string" );
		 			 
		cs2 = StructureFactory.createComputedColumn( );
		cs2.setName( "endtoend" );
		cs2.setExpression( "row[\"endtoend\"]" );
		cs2.setDataType( "integer" );
		 
		cs3 = StructureFactory.createComputedColumn( );
		cs3.setName( "createtostart" );
		cs3.setExpression( "row[\"createtostart\"]" );
		cs3.setDataType( "integer" );
		 
		cs4 = StructureFactory.createComputedColumn( );
		cs4.setName( "starttoend" );
		cs4.setExpression( "row[\"starttoend\"]" );
		cs4.setDataType( "integer" );
		
		computedSet = dataSetHandle.getPropertyHandle( ScriptDataSetHandle.COMPUTED_COLUMNS_PROP );
		computedSet.addItem( cs1 );
		computedSet.addItem( cs2 );
		computedSet.addItem( cs3 );
		computedSet.addItem( cs4 );

		design.getDataSets( ).add( dataSetHandle );
	}
	
	private  void createDataSetforProcedure(int m,int n) throws SemanticException{
		
		dataSetHandle = factory.newScriptDataSet(Dataset+"for Procedure"+m);
		dataSetHandle.setDataSource( "Data Source" );
		String str =  "i=0;" + "sourcedata = new Array(";
		
		for (int j = 0; j < 2; j++){
			str = str + "new Array(4), ";
		}
		str = str + "new Array(4));";
		str = str + "sourcedata[0][0] = 'End to End';" 
				+ "sourcedata[1][0] = 'Create to Start';" 
				+ "sourcedata[2][0] = 'Start to End';";
		
		for (int i = 1; i < 4; i++){
		str = str + " sourcedata[0]["+i+"] = "+readfile.endtoend[readfile.num+i-1]+";"
				+ "sourcedata[1]["+i+"] = "+readfile.createtostart[readfile.num+i-1]+";" 
				+" sourcedata[2]["+i+"] = "+readfile.starttoend[readfile.num+i-1]+";";
		}
		
		dataSetHandle.setOpen(str);
		
		dataSetHandle.setFetch( "if ( i < 3 ){"
		
		+ "row[\"Time"+m+"\"] = sourcedata[i][0]; row[\"Max"+m+"\"] = sourcedata[i][1];"
		+ "row[\"Min"+m+"\"] = sourcedata[i][2]; row[\"Ave"+m+"\"] = sourcedata[i][3];"
		+ "i++; return true;}" + "else return false;" );
		
		cs5 = StructureFactory.createComputedColumn( );
		cs5.setName( "Time"+m );
		cs5.setExpression( "row[\"Time"+m+"\"]" );
		cs5.setDataType( "string" );
		 			 
		cs6 = StructureFactory.createComputedColumn( );
		cs6.setName( "Max"+m );
		cs6.setExpression( "row[\"Max"+m+"\"]" );
		cs6.setDataType( "integer" );
		 
		cs7 = StructureFactory.createComputedColumn( );
		cs7.setName( "Min"+m );
		cs7.setExpression( "row[\"Min"+m+"\"]" );
		cs7.setDataType( "integer" );
		 
		cs8 = StructureFactory.createComputedColumn( );
		cs8.setName( "Ave"+m );
		cs8.setExpression( "row[\"Ave"+m+"\"]" );
		cs8.setDataType( "integer" );
		

		computedSet = dataSetHandle.getPropertyHandle( ScriptDataSetHandle.COMPUTED_COLUMNS_PROP );
		computedSet.addItem( cs5 );
		computedSet.addItem( cs6 );
		computedSet.addItem( cs7 );
		computedSet.addItem( cs8 );
		
		design.getDataSets( ).add( dataSetHandle );
		
	}
	
	private  void createDataSetforProcedureinDetail(int m,int n) throws SemanticException{
		
		dataSetHandle = factory.newScriptDataSet(Dataset+"for Procedure in detail"+m);
		dataSetHandle.setDataSource( "Data Source" );
		String str =  "i=0;" + "sourcedata = new Array(";
		
		for (int j = 0; j < n-1; j++){
			str = str + "new Array(4), ";
		}
		str = str + "new Array(4));";
		
		for (int i = 0; i < n; i++){
			int a =readfile.calllist[m][i+1];
		str = str + "sourcedata["+i+"][0] = \" "+readfile.element[a]+"\" ; sourcedata["+i+"][1] = "+readfile.endtoend[a]+";"
				+ "sourcedata["+i+"][2] = "+readfile.createtostart[a]+"; sourcedata["+i+"][3] = "+readfile.starttoend[a]+";";
		}
		
		dataSetHandle.setOpen(str);
		
		dataSetHandle.setFetch( "if ( i <"+n+" ){"
		
		+ "row[\"elementidmeaning_p"+m+"\"] = sourcedata[i][0]; row[\"endtoend_p"+m+"\"] = sourcedata[i][1];"
		+ "row[\"createtostart_p"+m+"\"] = sourcedata[i][2]; row[\"starttoend_p"+m+"\"] = sourcedata[i][3];"
		+ "i++; return true;}" + "else return false;" );
		
	
		cs5 = StructureFactory.createComputedColumn( );
		cs5.setName( "elementidmeaning_p"+m );
		cs5.setExpression( "row[\"elementidmeaning_p"+m+"\"]" );
		cs5.setDataType( "string" );
		 			 
		cs6 = StructureFactory.createComputedColumn( );
		cs6.setName( "endtoend_p"+m );
		cs6.setExpression( "row[\"endtoend_p"+m+"\"]" );
		cs6.setDataType( "integer" );
		 
		cs7 = StructureFactory.createComputedColumn( );
		cs7.setName( "createtostart_p"+m );
		cs7.setExpression( "row[\"createtostart_p"+m+"\"]" );
		cs7.setDataType( "integer" );
		 
		cs8 = StructureFactory.createComputedColumn( );
		cs8.setName( "starttoend_p"+m );
		cs8.setExpression( "row[\"starttoend_p"+m+"\"]" );
		cs8.setDataType( "integer" );
		

		computedSet = dataSetHandle.getPropertyHandle( ScriptDataSetHandle.COMPUTED_COLUMNS_PROP );
		computedSet.addItem( cs5 );
		computedSet.addItem( cs6 );
		computedSet.addItem( cs7 );
		computedSet.addItem( cs8 );
		
		design.getDataSets( ).add( dataSetHandle );
		
	}
	private void saveHtml(String pathToBirt, int i) throws EngineException{
		IReportEngine engine=null;
		EngineConfig Econfig = null;
		
		//Configure report engine
		try{
			Econfig = new EngineConfig( );
			Econfig.setEngineHome( null );
			Econfig.setLogConfig(null, Level.FINE);
			
			Platform.startup( Econfig );
			IReportEngineFactory Ifactory = (IReportEngineFactory) Platform.createFactoryObject( IReportEngineFactory.EXTENSION_REPORT_ENGINE_FACTORY );
			engine = Ifactory.createReportEngine( Econfig );
			engine.changeLogLevel( Level.WARNING );
		}
		catch( Exception ex){
			ex.printStackTrace();
		}

		//Configure emitter to handle actions and images
		HTMLEmitterConfig emitterConfig = new HTMLEmitterConfig( );
		emitterConfig.setActionHandler( new HTMLActionHandler( ) );
		HTMLServerImageHandler imageHandler = new HTMLServerImageHandler( );
		emitterConfig.setImageHandler( imageHandler );
		IReportRunnable Idesign = null;

		//Open the saved report design 
		String[] str = pathToBirt.split("report");
		Idesign = engine.openReportDesign(str[0] +"/report/mybirt"+i+".rptdesign"); 
		
		//Create task to run and render the report
		IRunAndRenderTask task = engine.createRunAndRenderTask(Idesign); 

		HTMLRenderOption options = new HTMLRenderOption();
		options.setBaseImageURL(str[0]); 
		options.setImageDirectory(str[0]);
	
		//Set output location
		if (i < 4){
			options.setOutputFileName(pathToBirt +"/mybirt"+i+".html");
		}
		else{
			options.setOutputFileName(pathToBirt + "/"+ readfile.procedurelist[i-3]+".html");
		}
			
		

		//Set output format
		options.setOutputFormat("html");
		task.setRenderOption(options);

		task.run();
		task.close();
		engine.shutdown();
		
	}
	
	private void createjsp(String pathToBirt, int i){
		ArrayList<String> al=new ArrayList<String>();
		File dir = new File(pathToBirt);
		for (File file : dir.listFiles()) {
	    	if (file.getName().endsWith((".jpg"))) {
	    		al.add(file.getName());
	    	}
	    }
		
		String name = readfile.procedurelist[i-3]+".jsp";
		File file = new File(pathToBirt+"/"+name);
		
		try {
			file.createNewFile();
			PrintStream printStream = new PrintStream(new FileOutputStream(file));
			printStream.println("<HTML><title>"+ readfile.procedurelist[i-3]+"</title><BODY><img src='"+al.get(i-2)+"'/><%@ include file='"+ readfile.procedurelist[i-3]+".html' %></BODY></HTML>");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	public int[] count(int[] time,int m,int n) {
		 int[] result = new int[3];
		 int max = time[readfile.calllist[m][1]], min = time[readfile.calllist[m][1]], sum = time[readfile.calllist[m][1]];
		 int a = 1;
		 for (int i = 2; i <= n; i++) {
			
			 max = max > time[readfile.calllist[m][i]] ? max : time[readfile.calllist[m][i]];
			 min = min < time[readfile.calllist[m][i]] ? min : time[readfile.calllist[m][i]];
			 sum += time[readfile.calllist[m][i]];
			 a++;
		}
		 
		 result[0] = max;
		 result[1] = min;
		 result[2] = sum / a;
		 return result;
	}  
		 
}