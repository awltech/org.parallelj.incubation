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



public class NewTableforProcedure {

	ComputedColumn cs5, cs6, cs7, cs8 = null;
	PropertyHandle computedSet =null;

	public NewTableforProcedure(ComputedColumn cs5, ComputedColumn cs6, ComputedColumn cs7,
			ComputedColumn cs8,PropertyHandle computedSet) {
		this.cs5 = cs5;
		this.cs6 = cs6;
		this.cs7 = cs7;
		this.cs8 = cs8;
		this.computedSet = computedSet;
	}
	
	public TableHandle createMyTable(ElementFactory designFactory,String dataset,int m) throws SemanticException{
		TableHandle tableHandle;
	
		CellHandle cell;
		LinkedHashMap<String, String> mappingColonne = new LinkedHashMap<String, String>();
	
		mappingColonne.put("Time"+m, "Time");
		mappingColonne.put("Ave"+m+"", "Average");
		mappingColonne.put("Max"+m, "Maximum");
		mappingColonne.put("Min"+m, "Minimum");
					
		tableHandle = designFactory.newTableItem(dataset,4);
		tableHandle.setWidth("600pt");
		
		tableHandle.setProperty( TableHandle.DATA_SET_PROP, dataset );
		computedSet = tableHandle.getColumnBindings( );
		
		cs5.setExpression( "dataSetRow[\"Time"+m+"\"]" );
		computedSet.addItem( cs5 );
		cs6.setExpression( "dataSetRow[\"Max"+m+"\"]" );
		computedSet.addItem( cs6 );
		cs7.setExpression( "dataSetRow[\"Min"+m+"\"]" );
		computedSet.addItem( cs7 );
		cs8.setExpression( "dataSetRow[\"Ave"+m+"\"]" );
		computedSet.addItem( cs8 );
	
		Set<String> listeColonne = mappingColonne.keySet();
		Iterator<String> iterateurListeColonne = listeColonne.iterator();
		String valeur;
		String libelle;
	
		StyleHandle styleHandle;

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

