package parallelj.birt;


import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Set;

import org.eclipse.birt.report.model.api.CellHandle;
import org.eclipse.birt.report.model.api.ColorHandle;
import org.eclipse.birt.report.model.api.DataItemHandle;
import org.eclipse.birt.report.model.api.ElementFactory;
import org.eclipse.birt.report.model.api.LabelHandle;
import org.eclipse.birt.report.model.api.PropertyHandle;
import org.eclipse.birt.report.model.api.RowHandle;
import org.eclipse.birt.report.model.api.StyleHandle;
import org.eclipse.birt.report.model.api.TableHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.elements.structures.ComputedColumn;



public class NewTable {
	
	ComputedColumn cs1, cs2, cs3, cs4 = null;
	PropertyHandle computedSet =null;

	public NewTable(ComputedColumn cs1, ComputedColumn cs2, ComputedColumn cs3,
			ComputedColumn cs4,PropertyHandle computedSet) {
		this.cs1 = cs1;
		this.cs2 = cs2;
		this.cs3 = cs3;
		this.cs4 = cs4;
		this.computedSet = computedSet;
	}

	public TableHandle createMyTable(ElementFactory designFactory,String dataset) throws SemanticException{
		
		//Create new table and new cell
		TableHandle tableHandle;
		CellHandle cell;
		LinkedHashMap<String, String> mappingColonne = new LinkedHashMap<String, String>();
		
		//Set title of the columns
		
		mappingColonne.put("elementidmeaning", "ElementIDMeaning");
		mappingColonne.put("endtoend", "EndTOEnd");
		mappingColonne.put("createtostart", "CreateTOStart");
		mappingColonne.put("starttoend", "StartTOEnd");
		
		//Set number of the columns and width
		tableHandle = designFactory.newTableItem(dataset,4);
		tableHandle.setWidth("600pt");
		
		//Bind each column with dataSet
		tableHandle.setProperty( TableHandle.DATA_SET_PROP, dataset );
		computedSet = tableHandle.getColumnBindings( );
		
		cs1.setExpression( "dataSetRow[\"elementidmeaning\"]" );
		computedSet.addItem( cs1 );
		cs2.setExpression( "dataSetRow[\"endtoend\"]" );
		computedSet.addItem( cs2 );
		cs3.setExpression( "dataSetRow[\"createtostart\"]" );
		computedSet.addItem( cs3 );
		cs4.setExpression( "dataSetRow[\"starttoend\"]" );
		computedSet.addItem( cs4 );
		
		

		Set<String> listeColonne = mappingColonne.keySet();
		Iterator<String> iterateurListeColonne = listeColonne.iterator();
		String valeur;
		String libelle;
	
		StyleHandle styleHandle;
		
		
		
		//Set the style of the Header of the table and add titles into Header
		RowHandle tableHeader = (RowHandle) tableHandle.getHeader().get(0);
		int n = -1;
		iterateurListeColonne = listeColonne.iterator();
		while (iterateurListeColonne.hasNext()){
			n = n + 1;
			valeur = iterateurListeColonne.next();
			libelle = mappingColonne.get(valeur);
			LabelHandle label1 = designFactory.newLabel(valeur);
			label1.setText(libelle);
			cell = (CellHandle) tableHeader.getCells().get(n);
			cell.getContent().add(label1);
			styleHandle = cell.getPrivateStyle();
			ColorHandle colorHandle = styleHandle.getBackgroundColor();
			colorHandle.setValue("gray");
			styleHandle.setTextAlign(DesignChoiceConstants.TEXT_ALIGN_CENTER);
			styleHandle.setVerticalAlign(DesignChoiceConstants.VERTICAL_ALIGN_MIDDLE);
			styleHandle.setBorderTopStyle(DesignChoiceConstants.LINE_STYLE_SOLID);
			styleHandle.setBorderRightStyle(DesignChoiceConstants.LINE_STYLE_SOLID);
			styleHandle.setBorderLeftStyle(DesignChoiceConstants.LINE_STYLE_SOLID);
		}
		
		//Set the style of the Detail of the table
		RowHandle tableDetail = (RowHandle) tableHandle.getDetail().get(0);
		n = -1;
		iterateurListeColonne = listeColonne.iterator();
		while (iterateurListeColonne.hasNext()){
			n = n + 1;
			valeur = iterateurListeColonne.next();
			cell = (CellHandle) tableDetail.getCells().get(n);
			DataItemHandle data = designFactory.newDataItem("data_"+valeur);
			data.setResultSetColumn(valeur);
			cell.getContent().add(data);
			styleHandle = cell.getPrivateStyle();
			styleHandle.setTextAlign(DesignChoiceConstants.TEXT_ALIGN_CENTER);
			styleHandle.getBorderBottomWidth().setValue(0.5);   
	        styleHandle.getBorderTopWidth().setValue(0.5); 
			styleHandle.setBorderBottomStyle(DesignChoiceConstants.LINE_STYLE_DOTTED);
			styleHandle.setBorderTopStyle(DesignChoiceConstants.LINE_STYLE_DOTTED);
			styleHandle.setBorderRightStyle(DesignChoiceConstants.LINE_STYLE_SOLID);
			styleHandle.setBorderLeftStyle(DesignChoiceConstants.LINE_STYLE_SOLID);
		}
		return tableHandle;
	}
}

