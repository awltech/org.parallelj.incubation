package test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.List;

import org.eclipse.swt.widgets.Tree;
import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotView;
import org.eclipse.swtbot.eclipse.gef.finder.SWTGefBot;
import org.eclipse.swtbot.eclipse.gef.finder.widgets.SWTBotGefEditor;
import org.eclipse.swtbot.swt.finder.finders.ChildrenControlFinder;
import org.eclipse.swtbot.swt.finder.matchers.WidgetOfType;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTree;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;
import org.junit.BeforeClass;
import org.junit.Test;

public class TestParallelJAvecSWTBot {

	private static SWTWorkbenchBot bot;
	
	public TestParallelJAvecSWTBot()
	{
		bot = new SWTWorkbenchBot();
		bot.viewByTitle("Welcome").close();
		bot.button("OK").click();
	}
	
	public void testCreerProjetMaven(String path) throws Exception {
		
		String pathURLLine[]=path.split(">");
		bot.menu(pathURLLine[0]).menu(pathURLLine[1]).menu(pathURLLine[2]).click();
		SWTBotShell shell =bot.shell("New");
		shell.activate();
		
		bot.tree().getTreeItem(pathURLLine[3]).expand().select(pathURLLine[4]);
		bot.button("Next >").click();
		bot.button("Next >").click();
		
		
		bot.textWithLabel("Filter:").setText("parallelj-archetype-web");	
		while(bot.table().rowCount()==0)
		bot.sleep(1000);
		bot.table().click(0, 0);
		bot.checkBox("Include snapshot archetypes").select();
		bot.button("Next >").click();
		bot.comboBoxWithLabel("Group Id:").setText("com.atos.swtbot.test");
		bot.comboBoxWithLabel("Artifact Id:").setText("com.atos.swtbot.test.parallelJ");

		bot.button("Finish").click();
		
	}
	
	public void testCreerParallelJDiagram()
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
	}
	
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
	public void testImporterCode() 
	{
		bot.sleep(6000);
		SWTBotView view1 = bot.viewByTitle("Project Explorer");
		 List controls1 = new ChildrenControlFinder(view1.getWidget()).findControls(WidgetOfType.widgetOfType(Tree.class));
		 if (controls1.isEmpty())
		 fail("Tree in Package Explorer View was not found.");
		 SWTBotTree tree1 = new SWTBotTree((Tree) controls1.get(0));
	     tree1.getTreeItem("com.atos.swtbot.test.parallelJ").expand();
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
	
	public void testImporterCodeParMenu() 
	{
		SWTBotView view = bot.viewByTitle("Project Explorer");
		 List controls = new ChildrenControlFinder(view.getWidget()).findControls(WidgetOfType.widgetOfType(Tree.class));
		 if (controls.isEmpty())
		 fail("Tree in Package Explorer View was not found.");
		 SWTBotTree tree = new SWTBotTree((Tree) controls.get(0));
		tree.getTreeItem("com.atos.swtbot.test.parallelJ").expand();
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
		bot.sleep(6000);
		SWTBotView view1 = bot.viewByTitle("Project Explorer");
		 List controls1 = new ChildrenControlFinder(view1.getWidget()).findControls(WidgetOfType.widgetOfType(Tree.class));
		 if (controls1.isEmpty())
		 fail("Tree in Package Explorer View was not found.");
		 SWTBotTree tree1 = new SWTBotTree((Tree) controls1.get(0));
	     tree1.getTreeItem("com.atos.swtbot.test.parallelJ").expand();
		
	}
	
	public void testImporterCodeParClickDroiteSurProjet() 
	{
		SWTBotView view = bot.viewByTitle("Project Explorer");
		 List controls = new ChildrenControlFinder(view.getWidget()).findControls(WidgetOfType.widgetOfType(Tree.class));
		 if (controls.isEmpty())
		 fail("Tree in Package Explorer View was not found.");
		 SWTBotTree tree = new SWTBotTree((Tree) controls.get(0));
		tree.getTreeItem("com.atos.swtbot.test.parallelJ").expand();
		bot.sleep(6000);
		
		while(!bot.cTabItem("test.pjd").isActive())
	    bot.sleep(1000);
	 
	SWTBotView view1 = bot.viewByTitle("Project Explorer");
	 List controls1 = new ChildrenControlFinder(view1.getWidget()).findControls(WidgetOfType.widgetOfType(Tree.class));
	 if (controls1.isEmpty())
	 fail("Tree in Package Explorer View was not found.");
	 SWTBotTree tree1 = new SWTBotTree((Tree) controls1.get(0));
    SWTBotTreeItem treeItem = tree1.getTreeItem("com.atos.swtbot.test.parallelJ").select();
    ContextMenuHelper.clickContextMenu(tree1, "Import...");
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
	bot.sleep(6000);
	SWTBotView view2 = bot.viewByTitle("Project Explorer");
	 List controls2 = new ChildrenControlFinder(view2.getWidget()).findControls(WidgetOfType.widgetOfType(Tree.class));
	 if (controls2.isEmpty())
	 fail("Tree in Package Explorer View was not found.");
	 SWTBotTree tree2 = new SWTBotTree((Tree) controls2.get(0));
     tree2.getTreeItem("com.atos.swtbot.test.parallelJ").expand();
	
	}
	public void testImporterDiagram() 
	{
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
	}
	public void testImporterDiagramParMenu() 
	{
		
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
	}
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
	
	
	
	public void testTransformationDiagram()
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
	
	public void testPropertesDiagram()
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
		editor.click("Bonjourletoutlemonde");
		bot.cTabItem("Properties").activate();
		assertEquals(bot.textWithLabel("Name : ").getText(),"Bonjourletoutlemonde");
		bot.textWithLabel("Name : ").setText("Bonjourtoutlemonde");
		editor.click("essayer");
		editor.click("Bonjourtoutlemonde");
		bot.cTabItem("Properties").activate();
		assertEquals(bot.textWithLabel("Name : ").getText(),"Bonjourtoutlemonde");
		
	}
	
	public void testExecuter()
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
	
}
