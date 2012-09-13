package parallelj.birt;

import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.attribute.Anchor;
import org.eclipse.birt.chart.model.attribute.AxisType;
import org.eclipse.birt.chart.model.attribute.ChartDimension;
import org.eclipse.birt.chart.model.attribute.IntersectionType;
import org.eclipse.birt.chart.model.attribute.LegendItemType;
import org.eclipse.birt.chart.model.attribute.Position;
import org.eclipse.birt.chart.model.attribute.TickStyle;
import org.eclipse.birt.chart.model.attribute.impl.BoundsImpl;
import org.eclipse.birt.chart.model.attribute.impl.ColorDefinitionImpl;
import org.eclipse.birt.chart.model.component.Axis;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.component.impl.SeriesImpl;
import org.eclipse.birt.chart.model.data.BaseSampleData;
import org.eclipse.birt.chart.model.data.DataFactory;
import org.eclipse.birt.chart.model.data.OrthogonalSampleData;
import org.eclipse.birt.chart.model.data.Query;
import org.eclipse.birt.chart.model.data.SampleData;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.model.data.impl.QueryImpl;
import org.eclipse.birt.chart.model.data.impl.SeriesDefinitionImpl;
import org.eclipse.birt.chart.model.impl.ChartWithAxesImpl;
import org.eclipse.birt.chart.model.layout.Legend;
import org.eclipse.birt.chart.model.layout.Plot;
import org.eclipse.birt.chart.model.type.BarSeries;
import org.eclipse.birt.chart.model.type.impl.BarSeriesImpl;
import org.eclipse.birt.report.model.api.ElementFactory;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.PropertyHandle;
import org.eclipse.birt.report.model.api.StructureFactory;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.structures.ComputedColumn;
import org.eclipse.birt.report.model.api.extension.ExtendedElementException;

public class NewBarChart {
	
	public ExtendedItemHandle createMyChart(ElementFactory designFactory,String dataset,int i,String name){
		
		//Create an Extended Item Handle in the design
		ExtendedItemHandle eih = designFactory.newExtendedItem ( null,"Chart" );
		//Create 4 columns
		ComputedColumn cs1=null, cs2=null, cs3=null, cs4=null;
		try{
			
			//Set the property of chart
			eih.setHeight( "400pt" );
			eih.setWidth( "575pt" );
			eih.setProperty (ExtendedItemHandle.DATA_SET_PROP, dataset);
			eih.setProperty("outputFormat", "JPG");
			
			//Set property of data and and bind them with dataSet
			PropertyHandle cs = eih.getColumnBindings();
			cs1 = StructureFactory.createComputedColumn();
			cs2 = StructureFactory.createComputedColumn();
			cs3 = StructureFactory.createComputedColumn();
			cs4 = StructureFactory.createComputedColumn();
			
			cs1.setName("elementidmeaning");
			cs1.setDataType("string");
			cs2.setName("endtoend");
			cs2.setDataType("integer");
			cs3.setName("createtostart");
			cs3.setDataType("integer");
			cs4.setName("starttoend");
			cs4.setDataType("integer");
			
			cs1.setExpression("dataSetRow[\"elementidmeaning\"]");
			cs2.setExpression("dataSetRow[\"endtoend\"]");
			cs3.setExpression("dataSetRow[\"createtostart\"]");
			cs4.setExpression("dataSetRow[\"starttoend\"]");
			
			cs.addItem(cs1);
			cs.addItem(cs2);
			cs.addItem(cs3);
			cs.addItem(cs4);
		}
		catch (SemanticException e){
			e.printStackTrace();
		}
		
		//Set property of chart
		ChartWithAxes cwaBar = ChartWithAxesImpl.create();
		cwaBar.setType("Bar Chart");
		cwaBar.setSubType("Side-by-side");
		cwaBar.getBlock().setBounds(BoundsImpl.create( 0, 0, 450, 175));
		
		//SamplaData
		SampleData sd = DataFactory.eINSTANCE.createSampleData();
		BaseSampleData sdBase = DataFactory.eINSTANCE.createBaseSampleData();
		sdBase.setDataSetRepresentation( "Category-A, Category-B" );
		sd.getBaseSampleData().add(sdBase);
		
		OrthogonalSampleData sdOrthogonal = DataFactory.eINSTANCE.createOrthogonalSampleData();
		sdOrthogonal.setDataSetRepresentation("4,12");
		sdOrthogonal.setSeriesDefinitionIndex(0);
		sd.getOrthogonalSampleData().add(sdOrthogonal);
		
		//Background of the chart
		cwaBar.setSampleData( sd );
		cwaBar.getBlock().setBackground(ColorDefinitionImpl.WHITE());
		
		//Dimension
		cwaBar.getBlock().getOutline().setVisible(true);
		cwaBar.setDimension(ChartDimension.TWO_DIMENSIONAL_WITH_DEPTH_LITERAL);
		
		//Set Title of chart
		Plot p = cwaBar.getPlot();
		p.getClientArea().setBackground(ColorDefinitionImpl.create(255, 255, 225));
		
		p.getOutline().setVisible(false);
		cwaBar.getTitle().getLabel().getCaption().setValue("["+name+" Bar Chart");
		
		Legend legend = cwaBar.getLegend();
		legend.getText().getFont().setSize(16);
		legend.getInsets().set(10, 5, 0, 0);
		legend.setAnchor(Anchor.NORTH_LITERAL);
		legend.setItemType( LegendItemType.SERIES_LITERAL );
		
		//Set property of Axis x
		Axis xAxis = cwaBar.getPrimaryBaseAxes()[0];
		xAxis.setType(AxisType.TEXT_LITERAL);
		xAxis.getMajorGrid().setTickStyle(TickStyle.BELOW_LITERAL);
		xAxis.getOrigin().setType(IntersectionType.VALUE_LITERAL);
		
		xAxis.getTitle().setVisible(true);
		xAxis.getTitle().getCaption().setValue("Element ID");
		xAxis.getLabel().getCaption().getFont().setRotation(90);
		
		//Set property of Axis y
		Axis yAxis = cwaBar.getPrimaryOrthogonalAxis(xAxis);
		yAxis.getMajorGrid().setTickStyle(TickStyle.LEFT_LITERAL);
		yAxis.getTitle().setVisible(true);
		yAxis.getTitle().getCaption().setValue("Time(ms)");
		yAxis.setType(AxisType.LINEAR_LITERAL);
		yAxis.getLabel().getCaption().getFont().setRotation(90);
		
		//Bind the data with Axis x and Axis y
		Series seCategory = SeriesImpl.create();
		Query xQ = QueryImpl.create( "row[\"elementidmeaning\"]" );
		seCategory.getDataDefinition( ).add( xQ );
		
		BarSeries bs1 = (BarSeries) BarSeriesImpl.create();
		bs1.setSeriesIdentifier("END TO END");//Set the label in the right side of chart
		Query yQ1 = QueryImpl.create( "row[\"endtoend\"]" );
		bs1.getDataDefinition().add(yQ1);
		bs1.setRiserOutline(null);
	    bs1.getLabel().setVisible(true);
	    bs1.setLabelPosition(Position.OUTSIDE_LITERAL);
	    SeriesDefinition sdY1 = SeriesDefinitionImpl.create();
		sdY1.getSeriesPalette().shift(-1);//Set the color of bar
	    sdY1.getSeries().add(bs1);
	    yAxis.getSeriesDefinitions().add(sdY1);
	    
	    BarSeries bs2 = (BarSeries) BarSeriesImpl.create();
	    bs2.setSeriesIdentifier("CREATE TO START");
	    Query yQ2 = QueryImpl.create( "row[\"createtostart\"]" );
	    bs2.getDataDefinition().add(yQ2);
	    bs2.setRiserOutline(null);
	    bs2.getLabel().setVisible(true);
	    bs2.setLabelPosition(Position.OUTSIDE_LITERAL);
	    SeriesDefinition sdY2 = SeriesDefinitionImpl.create();
	    sdY2.getSeriesPalette().shift(-3);
	    sdY2.getSeries().add(bs2);
	    yAxis.getSeriesDefinitions().add(sdY2);
	    
	    BarSeries bs3 = (BarSeries) BarSeriesImpl.create();
	    bs3.setSeriesIdentifier("START TO END");
	    Query yQ3 = QueryImpl.create( "row[\"starttoend\"]" );
	    bs3.getDataDefinition().add( yQ3 );
	    bs3.setRiserOutline(null);
	    bs3.getLabel().setVisible(true);
	    bs3.setLabelPosition(Position.OUTSIDE_LITERAL);
	    SeriesDefinition sdY3 = SeriesDefinitionImpl.create();
	    sdY3.getSeriesPalette().shift(-9);
	    sdY3.getSeries().add(bs3);
	    yAxis.getSeriesDefinitions().add(sdY3);
	    
	    SeriesDefinition sdX = SeriesDefinitionImpl.create();
	    xAxis.getSeriesDefinitions().add(sdX);
	    sdX.getSeriesPalette().shift(-9);
	    sdX.getSeries().add(seCategory);
	    
	    //If a data equals zero, hide the value label
	    //Adjust the size of chart automatically
	    cwaBar.setScript("function beforeDrawDataPointLabel( dph, label, icsc )"
	    +"{ if( label.getCaption().getValue() == '0' ){" 
	    +"label.setVisible(false);}}");
	    
	    
	    try{
	    	eih.getReportItem().setProperty( "chart.instance", cwaBar );
	    }
	    catch ( ExtendedElementException e ){
	    	e.printStackTrace();
	    }
	    return eih;
	}
}
