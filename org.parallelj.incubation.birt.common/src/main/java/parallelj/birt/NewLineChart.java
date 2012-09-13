package parallelj.birt;

import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.attribute.Anchor;
import org.eclipse.birt.chart.model.attribute.AxisType;
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
import org.eclipse.birt.chart.model.type.LineSeries;
import org.eclipse.birt.chart.model.type.impl.LineSeriesImpl;
import org.eclipse.birt.report.model.api.ElementFactory;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.PropertyHandle;
import org.eclipse.birt.report.model.api.StructureFactory;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.structures.ComputedColumn;
import org.eclipse.birt.report.model.api.extension.ExtendedElementException;

public class NewLineChart {
	
	public ExtendedItemHandle createMyChart(ElementFactory designFactory,String dataset,int i, String name){
		
		//Create an Extended Item Handle in the design
		ExtendedItemHandle eih = designFactory.newExtendedItem ( null,"Chart" );
		//Create 4 columns
		ComputedColumn cs1 = null, cs2 = null, cs3 = null, cs4 = null;
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
		ChartWithAxes cwaLine = ChartWithAxesImpl.create();
		cwaLine.setType("Line Chart");
		cwaLine.setSubType("Overlay");
		cwaLine.getBlock().setBounds( BoundsImpl.create( 0, 0, 450, 175));
		
		//SamplaData
		SampleData sd = DataFactory.eINSTANCE.createSampleData();
	    BaseSampleData sdBase = DataFactory.eINSTANCE.createBaseSampleData();
	    sdBase.setDataSetRepresentation("");
	    sd.getBaseSampleData().add(sdBase);

	    OrthogonalSampleData sdOrthogonal = DataFactory.eINSTANCE.createOrthogonalSampleData();
	    sdOrthogonal.setDataSetRepresentation("");
	    sdOrthogonal.setSeriesDefinitionIndex(0);
	    sd.getOrthogonalSampleData().add(sdOrthogonal);
	    
	    //Background of the chart
	    cwaLine.getBlock().getOutline().setVisible(true);
	    cwaLine.setSampleData(sd);
		cwaLine.getBlock().setBackground(ColorDefinitionImpl.WHITE());

		//Set Title of chart	
		Plot p = cwaLine.getPlot();
		p.getClientArea().setBackground(ColorDefinitionImpl.create(255, 255, 225));
		
		p.getOutline().setVisible(false);
		cwaLine.getTitle().getLabel().getCaption().setValue("["+name+" Line Chart");
		
		////Set property of Legend
		Legend lg = cwaLine.getLegend();
		lg.getText().getFont().setSize(16);
		lg.getInsets().set(10, 5, 0, 0);
		lg.setAnchor(Anchor.NORTH_LITERAL);
		lg.setItemType( LegendItemType.SERIES_LITERAL );
		
		//Set property of Axis x
		Axis xAxis = cwaLine.getPrimaryBaseAxes()[0];
		xAxis.setType(AxisType.TEXT_LITERAL);
		xAxis.getMajorGrid().setTickStyle(TickStyle.BELOW_LITERAL);
		xAxis.getOrigin().setType(IntersectionType.MIN_LITERAL);
		
		xAxis.getTitle().setVisible(true);
		xAxis.getTitle().getCaption().setValue("Element ID");
		xAxis.getLabel().getCaption().getFont().setRotation(90);
		
		//Set property of Axis y
		Axis yAxis = cwaLine.getPrimaryOrthogonalAxis(xAxis);
		yAxis.getMajorGrid().setTickStyle(TickStyle.LEFT_LITERAL);
		yAxis.getTitle().setVisible(true);
		yAxis.getTitle().getCaption().setValue("Time(ms)");
		yAxis.setType(AxisType.LINEAR_LITERAL);
		yAxis.getLabel().getCaption().getFont().setRotation(90);
		
		//Bind the data with Axis x and Axis y
		Series seCategory = SeriesImpl.create();
		Query xQ = QueryImpl.create( "row[\"elementidmeaning\"]" );
		seCategory.getDataDefinition( ).add( xQ );
		
		LineSeries bs1 = (LineSeries) LineSeriesImpl.create();
		bs1.setSeriesIdentifier("END TO END");//Set the label in the right side of chart
		Query yQ1 = QueryImpl.create( "row[\"endtoend\"]" );
		bs1.getDataDefinition().add(yQ1);
	    bs1.getLabel().setVisible(true);
	    bs1.setLabelPosition(Position.ABOVE_LITERAL);
	    SeriesDefinition sdY1 = SeriesDefinitionImpl.create();
	    yAxis.getSeriesDefinitions().add(sdY1);
	    sdY1.getSeriesPalette().shift(-1);//Set the color of bar
		sdY1.getSeries().add(bs1);
		
		LineSeries bs2 = (LineSeries) LineSeriesImpl.create();
		bs2.setSeriesIdentifier("CREATE TO START");
		Query yQ2 = QueryImpl.create( "row[\"createtostart\"]" );
		bs2.getDataDefinition().add(yQ2);
	    bs2.getLabel().setVisible(true);
	    bs2.setLabelPosition(Position.ABOVE_LITERAL);
	    SeriesDefinition sdY2 = SeriesDefinitionImpl.create();
	    yAxis.getSeriesDefinitions().add(sdY2);
	    sdY2.getSeriesPalette().shift(-3);
		sdY2.getSeries().add(bs2);
	    
		LineSeries bs3 = (LineSeries) LineSeriesImpl.create();
		bs3.setSeriesIdentifier("START TO END");
		Query yQ3 = QueryImpl.create( "row[\"starttoend\"]" );
		bs3.getDataDefinition().add(yQ3);
	    bs3.getLabel().setVisible(true);
	    bs3.setLabelPosition(Position.ABOVE_LITERAL);
	    SeriesDefinition sdY3 = SeriesDefinitionImpl.create();
	    yAxis.getSeriesDefinitions().add(sdY3);
	    sdY3.getSeriesPalette().shift(-9);
		sdY3.getSeries().add(bs3);
	    	    
	    SeriesDefinition sdX = SeriesDefinitionImpl.create();
	    xAxis.getSeriesDefinitions().add(sdX);
	    sdX.getSeriesPalette().shift(-1);
	    sdX.getSeries().add(seCategory);
	    
	    //If a data equals zero, hide the value label
	    //Adjust the size of chart automatically
	    cwaLine.setScript("function beforeDrawDataPointLabel( dph, label, icsc )"
	    	    +"{ if( label.getCaption().getValue() == '0' ){" 
	    	    +"label.setVisible(false);}}");
	    	    
	    
	    try{
	    	eih.getReportItem().setProperty( "chart.instance", cwaLine );
	    }
	    catch ( ExtendedElementException e ){
	    	e.printStackTrace();
	    }
	    return eih;
	}
}