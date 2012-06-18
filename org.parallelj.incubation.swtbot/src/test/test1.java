package test;
import static org.junit.Assert.*;

import java.awt.Button;
import java.awt.Composite;
import java.util.List;
import java.util.regex.Matcher;

import org.eclipse.draw2d.FigureCanvas;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotView;
import org.eclipse.swtbot.swt.finder.SWTBotAssert;
import org.eclipse.swtbot.swt.finder.finders.ChildrenControlFinder;
import org.eclipse.swtbot.swt.finder.matchers.WidgetOfType;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTree;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;
import org.junit.*;
import org.eclipse.draw2d.FigureCanvas;
import org.eclipse.swtbot.eclipse.gef.finder.SWTGefBot;
import org.eclipse.swtbot.eclipse.gef.finder.widgets.SWTBotGefEditPart;
import org.eclipse.swtbot.eclipse.gef.finder.widgets.SWTBotGefEditor;
import org.eclipse.swtbot.eclipse.gef.finder.widgets.SWTBotGefFigureCanvas;


public class test1 {
private static SWTWorkbenchBot bot;
	
	@BeforeClass
	public static void setUpBeforeClass () throws Exception {
		bot = new SWTWorkbenchBot();
		bot.viewByTitle("Welcome").close();
		bot.button("OK").click();
		
	}
	
	@Test
	public void testChoisirMavenProjet () throws Exception {

		bot.menu("File").menu("New").menu("Other...").click();
		SWTBotShell shell =bot.shell("New");
		shell.activate();
		
		bot.tree().getTreeItem("Maven").expand().select("Maven Project");
		bot.button("Next >").click();
		bot.button("Next >").click();
}
	@Test
	public void testDefinirArchetype () throws Exception {
		bot.textWithLabel("Filter:").setText("parallelj-archetype-web");	
		while(bot.table().rowCount()==0)
			bot.sleep(1000);
		bot.table().click(0, 0);
		bot.checkBox("Include snapshot archetypes").select();
		bot.button("Next >").click();
	
}
	@Test
	public void testDefinirProjetID () throws Exception {
		
		bot.comboBoxWithLabel("Group Id:").setText("com.atos.swtbot.test");
		bot.comboBoxWithLabel("Artifact Id:").setText("com.atos.swtbot.test.parallelJ");

		bot.button("Finish").click();
}
	//@Test
	public void testCreerDiagrammeParMenu()
	{
		bot.menu("File").menu("New").menu("Other...").click();
		SWTBotShell shell =bot.shell("New");
		shell.activate();
		
		bot.tree().getTreeItem("Examples").expand().select("ParallelJ Diagram");
		bot.button("Next >").click();
		
		bot.textWithLabel("Enter or select the parent folder:").setText("com.atos.swtbot.test.parallelJ/src/main/resources/META-INF/ParallelJ");
		bot.textWithLabel("File name:").setText("test.pjd");
		while(!bot.button("Finish").isEnabled())
			bot.sleep(1000);
		bot.button("Finish").click();
		bot.sleep(20000);
	}
	//@Test
	public void testCreerDiagrammeParClickDroiteProjet()
	{
	 
		SWTBotView view = bot.viewByTitle("Project Explorer");
		 List controls = new ChildrenControlFinder(view.getWidget()).findControls(WidgetOfType.widgetOfType(Tree.class));
		 if (controls.isEmpty())
		 fail("Tree in Package Explorer View was not found.");
		 SWTBotTree tree = new SWTBotTree((Tree) controls.get(0));
        SWTBotTreeItem treeItem = tree.getTreeItem("com.atos.swtbot.test.parallelJ").select();
        ContextMenuHelper.clickContextMenu(tree, "ParallelJ", "Create New ParallelJ Diagram");
        SWTBotShell shell =bot.shell("ParallelJ");
		shell.activate();
		bot.textWithLabel("New ParallelJ Diagram name :").setText("test");
		bot.button("OK").click();
	}
	@Test
	public void testCreerDiagrammeParClickDroiteFichier()
	{
		
		
		SWTBotView view = bot.viewByTitle("Project Explorer");
		 List controls = new ChildrenControlFinder(view.getWidget()).findControls(WidgetOfType.widgetOfType(Tree.class));
		 if (controls.isEmpty())
		 fail("Tree in Package Explorer View was not found.");
		 SWTBotTree tree = new SWTBotTree((Tree) controls.get(0));
		tree.getTreeItem("com.atos.swtbot.test.parallelJ").expand();
		bot.sleep(6000);
       SWTBotTreeItem treeItem = tree.getTreeItem("com.atos.swtbot.test.parallelJ").expand().expandNode("src/main/resources").expandNode("META-INF").expandNode("ParallelJ").select();
       ContextMenuHelper.clickContextMenu(tree, "ParallelJ", "Create New ParallelJ Diagram");
       SWTBotShell shell =bot.shell("ParallelJ");
		shell.activate();
		bot.textWithLabel("New ParallelJ Diagram name :").setText("test");
		bot.button("OK").click();
	}
//@Test
	public void testImporterCodeParMenu() 
	{
		bot.sleep(6000);
		while(!bot.cTabItem("test.pjd").isActive())
	    bot.sleep(1000);
		bot.menu("File").menu("Import...").click();
		SWTBotShell shell =bot.shell("Import");
		shell.activate();
		
		bot.tree().getTreeItem("General").expand().select("File System");
		bot.button("Next >").click();

		bot.comboBoxWithLabel("From directory:").setText("D:/Utilisateurs/A534346/workspace/com.atos.parallelJ.demo/src/main/java");
		bot.comboBoxWithLabel("From directory:").setSelection(0);  //refresh le data

		bot.textWithLabel("Into folder:").setText("com.atos.swtbot.test.parallelJ/src/main/java");
		bot.checkBoxInGroup("Overwrite existing resources without warning", "Options").select();
		bot.shell("Import").activate();
		while(!bot.button("Select All").isEnabled())
			bot.sleep(1000);
		bot.button("Select All").click();
		bot.button("Finish").click();
	}
	@Test
	public void testImporterCodeParClickDroiteSurProjet() 
	{
		bot.sleep(6000);
		while(!bot.cTabItem("test.pjd").isActive())
	    bot.sleep(1000);
	 
	SWTBotView view = bot.viewByTitle("Project Explorer");
	 List controls = new ChildrenControlFinder(view.getWidget()).findControls(WidgetOfType.widgetOfType(Tree.class));
	 if (controls.isEmpty())
	 fail("Tree in Package Explorer View was not found.");
	 SWTBotTree tree = new SWTBotTree((Tree) controls.get(0));
    SWTBotTreeItem treeItem = tree.getTreeItem("com.atos.swtbot.test.parallelJ").select();
    ContextMenuHelper.clickContextMenu(tree, "Import...");
    SWTBotShell shell =bot.shell("Import");
	shell.activate();
	
	bot.tree().getTreeItem("General").expand().select("File System");
	bot.button("Next >").click();

	bot.comboBoxWithLabel("From directory:").setText("D:/Utilisateurs/A534346/workspace/com.atos.parallelJ.demo/src/main/java");
	bot.comboBoxWithLabel("From directory:").setSelection(0);  //refresh le data

	bot.textWithLabel("Into folder:").setText("com.atos.swtbot.test.parallelJ/src/main/java");
	bot.checkBoxInGroup("Overwrite existing resources without warning", "Options").select();
	bot.shell("Import").activate();
	while(!bot.button("Select All").isEnabled())
		bot.sleep(1000);
	bot.button("Select All").click();
	bot.button("Finish").click();
	
	}
	//@Test
	public void testImporterDiagramParMenu() 
	{
		bot.sleep(6000);
		while(!bot.cTabItem("test.pjd").isActive())
	    bot.sleep(1000);
		bot.menu("File").menu("Import...").click();
		SWTBotShell shell =bot.shell("Import");
		shell.activate();
		
		bot.tree().getTreeItem("General").expand().select("File System");
		bot.button("Next >").click();

		bot.comboBoxWithLabel("From directory:").setText("D:/Utilisateurs/A534346/workspace/com.atos.parallelJ.demo/src/main/resources/META-INF/ParallelJ");
		bot.comboBoxWithLabel("From directory:").setSelection(1);  //refresh le data

		bot.textWithLabel("Into folder:").setText("com.atos.swtbot.test.parallelJ/src/main/resources/META-INF/ParallelJ");
		bot.checkBoxInGroup("Overwrite existing resources without warning", "Options").select();
		bot.shell("Import").activate();
		while(!bot.button("Select All").isEnabled())
			bot.sleep(1000);
		bot.button("Select All").click();
		bot.button("Finish").click();
		bot.sleep(6000);
	}
	@Test
	public void testImporterDiagramParClickDroiteSurProjet() 
	{
		while(!bot.cTabItem("test.pjd").isActive())
		    bot.sleep(1000);
		 
		SWTBotView view = bot.viewByTitle("Project Explorer");
		 List controls = new ChildrenControlFinder(view.getWidget()).findControls(WidgetOfType.widgetOfType(Tree.class));
		 if (controls.isEmpty())
		 fail("Tree in Package Explorer View was not found.");
		 SWTBotTree tree = new SWTBotTree((Tree) controls.get(0));
	    SWTBotTreeItem treeItem = tree.getTreeItem("com.atos.swtbot.test.parallelJ").select();
	    ContextMenuHelper.clickContextMenu(tree, "Import...");
	    SWTBotShell shell =bot.shell("Import");
		shell.activate();
	    bot.tree().getTreeItem("General").expand().select("File System");
		bot.button("Next >").click();

		bot.comboBoxWithLabel("From directory:").setText("D:/Utilisateurs/A534346/workspace/com.atos.parallelJ.demo/src/main/resources/META-INF/ParallelJ");
		bot.comboBoxWithLabel("From directory:").setSelection(1);  //refresh le data

		bot.textWithLabel("Into folder:").setText("com.atos.swtbot.test.parallelJ/src/main/resources/META-INF/ParallelJ");
		bot.checkBoxInGroup("Overwrite existing resources without warning", "Options").select();
		bot.shell("Import").activate();
		while(!bot.button("Select All").isEnabled())
			bot.sleep(1000);
		bot.button("Select All").click();
		bot.button("Finish").click();
		
	}

	
	//@Test
	public void testTransformationDiagramParClickDroiteSurProjetVueProjet()
	{
		while(!bot.cTabItem("test.pjd").isActive())
		    bot.sleep(1000);
	 
		SWTBotView view = bot.viewByTitle("Project Explorer");
		 List controls = new ChildrenControlFinder(view.getWidget()).findControls(WidgetOfType.widgetOfType(Tree.class));
		 if (controls.isEmpty())
		 fail("Tree in Package Explorer View was not found.");
		 SWTBotTree tree = new SWTBotTree((Tree) controls.get(0));
        SWTBotTreeItem treeItem = tree.getTreeItem("com.atos.swtbot.test.parallelJ").select();
        ContextMenuHelper.clickContextMenu(tree, "ParallelJ", "Generate Code From Diagrams");
        bot.button("OK").click();
	}
	//@Test
	public void testTransformationDiagramParClickDroiteSurProjetVuePackage() 
	{
		while(!bot.cTabItem("test.pjd").isActive())
		    bot.sleep(1000);
		 bot.menu("Window").menu("Show View").menu("Other...").click();
		 SWTBotShell shell =bot.shell("Show View");
			shell.activate();
			 bot.tree().getTreeItem("Java").expand().select("Package Explorer");
			 bot.button("OK").click();
		 
		 SWTBotView view = bot.viewByTitle("Package Explorer");
		 List controls = new ChildrenControlFinder(view.getWidget()).findControls(WidgetOfType.widgetOfType(Tree.class));
		 if (controls.isEmpty())
		 fail("Tree in Package Explorer View was not found.");
		 SWTBotTree tree = new SWTBotTree((Tree) controls.get(0));
        SWTBotTreeItem treeItem = tree.getTreeItem("com.atos.swtbot.test.parallelJ").select();
        ContextMenuHelper.clickContextMenu(tree, "ParallelJ", "Generate Code From Diagrams");
        bot.button("OK").click();
		 
		 }
	//@Test
	public void testTransformationDiagramParClickDroiteSurFichierVuePackage() 
	{
		while(!bot.cTabItem("test.pjd").isActive())
		    bot.sleep(1000);
		 bot.menu("Window").menu("Show View").menu("Other...").click();
		 SWTBotShell shell =bot.shell("Show View");
			shell.activate();
			 bot.tree().getTreeItem("Java").expand().select("Package Explorer");
			 bot.button("OK").click();
		 
		SWTBotView view = bot.viewByTitle("Package Explorer");
		 List controls = new ChildrenControlFinder(view.getWidget()).findControls(WidgetOfType.widgetOfType(Tree.class));
		 if (controls.isEmpty())
		 fail("Tree in Package Explorer View was not found.");
		 SWTBotTree tree = new SWTBotTree((Tree) controls.get(0));
       SWTBotTreeItem treeItem = tree.getTreeItem("com.atos.swtbot.test.parallelJ").expand().expandNode("src/main/resources").expandNode("META-INF").expandNode("ParallelJ").select();
       ContextMenuHelper.clickContextMenu(tree, "ParallelJ", "Generate Code From Diagrams");
	}

	//@Test
	public void testTransformationDiagramParClickDroiteSurFichierVueProjet() 
		{
		while(!bot.cTabItem("test.pjd").isActive())
		    bot.sleep(1000);
		 bot.menu("Window").menu("Show View").menu("Other...").click();
		 SWTBotShell shell =bot.shell("Show View");
			shell.activate();
			 bot.tree().getTreeItem("Java").expand().select("Projet Explorer");
			 bot.button("OK").click();
		 
		SWTBotView view = bot.viewByTitle("Projet Explorer");
		 List controls = new ChildrenControlFinder(view.getWidget()).findControls(WidgetOfType.widgetOfType(Tree.class));
		 if (controls.isEmpty())
		 fail("Tree in Package Explorer View was not found.");
		 SWTBotTree tree = new SWTBotTree((Tree) controls.get(0));
       SWTBotTreeItem treeItem = tree.getTreeItem("com.atos.swtbot.test.parallelJ").expand().expandNode("src/main/resources").expandNode("META-INF").expandNode("ParallelJ").select();
       ContextMenuHelper.clickContextMenu(tree, "ParallelJ", "Generate Code From Diagrams");
		
		}
	@Test
	public void testTransformationDiagramParClickDroiteSurDiagrammeVuePackage() 
		{
		while(!bot.cTabItem("test.pjd").isActive())
		    bot.sleep(1000);
		 bot.menu("Window").menu("Show View").menu("Other...").click();
		 SWTBotShell shell =bot.shell("Show View");
			shell.activate();
			 bot.tree().getTreeItem("Java").expand().select("Package Explorer");
			 bot.button("OK").click();
		 
		SWTBotView view = bot.viewByTitle("Package Explorer");
		 List controls = new ChildrenControlFinder(view.getWidget()).findControls(WidgetOfType.widgetOfType(Tree.class));
		 if (controls.isEmpty())
		 fail("Tree in Package Explorer View was not found.");
		 SWTBotTree tree = new SWTBotTree((Tree) controls.get(0));
       SWTBotTreeItem treeItem = tree.getTreeItem("com.atos.swtbot.test.parallelJ").expand().expandNode("src/main/resources").expandNode("META-INF").expandNode("ParallelJ").expandNode("essayer.pjd").select();
       ContextMenuHelper.clickContextMenu(tree, "ParallelJ", "Generate Code From File");
		}
	@Test
	public void testTransformationDiagramParClickDroiteSurDiagrammeVueProject() 
		{
		while(!bot.cTabItem("test.pjd").isActive())
		    bot.sleep(1000);
		
		SWTBotView view = bot.viewByTitle("Project Explorer");
		 List controls = new ChildrenControlFinder(view.getWidget()).findControls(WidgetOfType.widgetOfType(Tree.class));
		 if (controls.isEmpty())
		 fail("Tree in Package Explorer View was not found.");
		 SWTBotTree tree = new SWTBotTree((Tree) controls.get(0));
       SWTBotTreeItem treeItem = tree.getTreeItem("com.atos.swtbot.test.parallelJ").expand().expandNode("src/main/resources").expandNode("META-INF").expandNode("ParallelJ").expandNode("essayer.pjd").select();
       ContextMenuHelper.clickContextMenu(tree, "ParallelJ", "Generate Code From File");
		
		}
	@Test
	public void testVerificationDesPropertesDiagram()
	{
		while(!bot.cTabItem("test.pjd").isActive())
		    bot.sleep(1000);
		SWTBotView view = bot.viewByTitle("Project Explorer");
		 List controls = new ChildrenControlFinder(view.getWidget()).findControls(WidgetOfType.widgetOfType(Tree.class));
		 if (controls.isEmpty())
		 fail("Tree in Package Explorer View was not found.");
		 SWTBotTree tree = new SWTBotTree((Tree) controls.get(0));
		 SWTBotTreeItem treeItem = tree.getTreeItem("com.atos.swtbot.test.parallelJ").select();
		 treeItem.expand().expandNode("src/main/resources").expandNode("META-INF").expandNode("ParallelJ").expandNode("essayer.pjd").select().doubleClick();
		
		 bot.menu("Window").menu("Show View").menu("Properties").click();
		SWTGefBot bot1 = new SWTGefBot();
		System.out.print(bot.activeEditor().getTitle());
		SWTBotGefEditor editor = bot1.gefEditor(bot.activeEditor().getTitle());
		editor.click("Helloworld");
		bot.cTabItem("Properties").activate();
		assertEquals(bot.textWithLabel("Name : ").getText(),"Helloworld");
	}
	@Test
	public void testModificationDesPropertesDiagram()
	{
		SWTGefBot bot1 = new SWTGefBot();
		System.out.print(bot.activeEditor().getTitle());
		SWTBotGefEditor editor = bot1.gefEditor(bot.activeEditor().getTitle());
		editor.click("Bonjourletoutlemonde");
		bot.cTabItem("Properties").activate();
		assertEquals(bot.textWithLabel("Name : ").getText(),"Bonjourletoutlemonde");
		bot.textWithLabel("Name : ").setText("Bonjourtoutlemonde");
		editor.click("essayer");
		editor.click("Bonjourtoutlemonde");
		bot.cTabItem("Properties").activate();
		assertEquals(bot.textWithLabel("Name : ").getText(),"Bonjourtoutlemonde");
		
	}
	//@Test
	public void testEcrireNote()
	{
		SWTGefBot bot1 = new SWTGefBot();
		//System.out.print(bot.activeEditor().getTitle());
		//SWTBotGefEditor editor = bot1.gefEditor(bot.activeEditor().getTitle());
		//bot1.toolbarToggleButton(3).click();
		
		//SWTBotGefEditPart editPart;
		//editPart=editor.getEditPart("Note - Create a note");
		//editor.drag(editPart, 10, 10);
		//editor.activateDefaultTool();//click("Palette");
		//bot.toolbarButtonWithTooltipInGroup("Note - Create a Note", "Palette").click();
		
		//bot.cTabItem("test.pjd").activate();
		//bot.toolbarButtonWithTooltip("Note - Create a Note").click();
		//bot.viewByTitle("Palette").toolbarPushButton("Note - Create a Note").click();
		//bot.viewByTitle("Palette").toolbarButton("Note - Create a Note").click();
		//bot.toolbarButton("Note - Create a Note").click();
		
	}
	//public SWTBotText textWithLabelInGroup(String label, String inGroup, int index) { 
		//Matcher matcher = allOf(widgetOfType(Text.class), withLabel(label), inGroup(inGroup)); 
		//return new SWTBotText((Text) widget(matcher, index), matcher);}
	//@Test
	public void testDessinerProgram()
	{
		
	}
	
	@Test
	public void testRunConfiguration()
	{
		bot.cTabItem("test.pjd").activate();
		while(!bot.cTabItem("test.pjd").isActive())
		    bot.sleep(1000);
		SWTBotView view = bot.viewByTitle("Project Explorer");
		 List controls = new ChildrenControlFinder(view.getWidget()).findControls(WidgetOfType.widgetOfType(Tree.class));
		 if (controls.isEmpty())
		 fail("Tree in Package Explorer View was not found.");
		 SWTBotTree tree = new SWTBotTree((Tree) controls.get(0));
        SWTBotTreeItem treeItem = tree.getTreeItem("com.atos.swtbot.test.parallelJ").select();
        ContextMenuHelper.clickContextMenu(tree, "Run As", "Run Configurations...");
        
        SWTBotShell shell =bot.shell("Run Configurations");
		shell.activate();
		controls = new ChildrenControlFinder(view.getWidget()).findControls(WidgetOfType.widgetOfType(Tree.class));
		 if (controls.isEmpty())
		 fail("Tree in type View was not found.");
		 tree = new SWTBotTree((Tree) controls.get(0));
		 bot.tree().expandNode("AspectJ Load-Time Weaving Application").select().click();
		 
		 bot.toolbarButtonWithTooltip("New launch configuration").click();
		 bot.tree().expandNode("AspectJ Load-Time Weaving Application").expandNode("New_configuration").select().doubleClick();
		 bot.cTabItem("Main").activate();
		 bot.textInGroup("Project:").setText("com.atos.swtbot.test.parallelJ");
		 bot.textInGroup("Main class:").setText("mainessayer");
		 bot.cTabItem("LTW Aspectpath").activate();
		 bot.treeWithLabel("Load-Time Weaving Aspectpath:").expandNode("User Entries").select();
		 bot.button(3).click();
		 
		 shell =bot.shell("Project Selection");
		 shell.activate();
		 bot.button("Select All").click();
		 bot.button("OK").click();
		 bot.button("Run").click();
		 bot.button("OK").click();
		 
	}
	
	 @AfterClass
	public static void tearDownAfterClass() throws  Exception {
	 bot.sleep(9000);
	}	
	
}
	

