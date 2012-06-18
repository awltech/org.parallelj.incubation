package test;



import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class test2 {
	private static SWTWorkbenchBot bot;
	
	@BeforeClass
	public static void setUpBeforeClass () throws Exception {
		bot = new SWTWorkbenchBot();
	}
	@Test
	public void CanTestParallelJAvecSWTBot() throws Exception {
		TestParallelJAvecSWTBot newTestBot = new TestParallelJAvecSWTBot();
		
		String pathURL="File>New>Other...>Maven>Maven Project";
		
		newTestBot.testCreerProjetMaven(pathURL);
		
		newTestBot.testCreerDiagrammeParClickDroiteProjet();
		
		newTestBot.testImporterCodeParClickDroiteSurProjet();
		newTestBot.testImporterDiagramParMenu();
		newTestBot.testTransformationDiagramParClickDroiteSurDiagrammeVueProject();
		newTestBot.testPropertesDiagram();
		newTestBot.testExecuter();
	}
	public void TestDessiner(){
		
	}
	@AfterClass
	 public static void tearDownAfterClass() throws  Exception {
	 bot.sleep(9000);
	}	
		
	
}
