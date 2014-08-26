package cms.view.element;

import java.awt.BasicStroke;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cms.model.DataStore;
import cms.model.data.DisplayMemoryStorage;
import cms.view.DisplayType;
import cms.view.Graph;

public class GraphingHistogram extends GraphingParent {
	private static final long serialVersionUID = 1L;
	public static int number_offset = 50;
	public static int line_offset = 42;

	public GraphingHistogram(DisplayType displaytype) {
		super(displaytype);
	}
	
	/*private class Vector{
		private final double[] x_values;
		private final double[] y_values;
		
		public Vector(int x1, int y1, int x2, int y2){
			x_values = new double[2];
			y_values = new double[2];
			x_values[0] = x1;
			x_values[1] = x2;
			y_values[0] = y1;
			y_values[1] = y2;		
		}
		
		public double magnetude(){
			return Math.sqrt((x_values[1]- x_values[0])*(x_values[1]- x_values[0])
						+ (y_values[1] - y_values[0])*(y_values[1] - y_values[0]));
		}
		
		public double height(){
			return (y_values[1] - y_values[0]);
		}
		
		public double width(){
			return (x_values[1]- x_values[0]);
		}
		
		public double direction(){	
			return Math.atan(this.height()/this.width());
		}
	}*/
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		//background
		g.setColor(BACK);
		g.fillRect(0, 0, getWidth(), getHeight());
		
		//font values
		Font f = new Font("Dialog", Font.BOLD, 12);
		g.setFont(f);
		
		this.setDisplayType(Graph.getDisplayType());
	
		//read-through values and average
		double[] values;
		
		/**TODO Receive which scale chosen from a slider on the GUI, ranges from 0 to 3 (1s, 10s, 5min, 2h30)*/
		int scale = 0;
		int size = DisplayMemoryStorage.getSize();
		double max = 0.0;
		int coreid = (DataStore.getSelectedCore() == -1) ? 0 : DataStore.getSelectedCore();
		
		if(displaytype == DisplayType.TEMPERATURE){
			max = 100.0;
			values = DataStore.coreA.get(coreid).memstore[0].getmem()[scale];
		} else if(displaytype == DisplayType.CPU){
			max = 100.0;
			values = DataStore.coreA.get(coreid).memstore[1].getmem()[scale];
		} else if(displaytype == DisplayType.RAM){
			max = 512.0;
			values = DataStore.coreA.get(coreid).memstore[2].getmem()[scale];
		} else if(displaytype == DisplayType.PARTICLES){
			max = 0.0;
			values = DataStore.coreA.get(coreid).memstore[3].getmem()[scale];
		} else{
			values = new double[size];
		}
		
		//Calculating average and sum
		double average = 0.0;
		for(int i = 0 ; i < values.length ; i++){
			average += values[i];
		}
		average /= size;
		
		if(displaytype == DisplayType.PARTICLES){
			/*double scatter = 0.0;
			for(int i = 0 ; i < values.length ; i++){
				scatter += (values[i]-average)*(values[i]-average);
			}
			scatter /= values.length-1;
			max = average + 3*Math.sqrt(scatter);*/
			for(int i = 0 ; i < values.length ; i++){
				if(values[i] > max){
					max = values[i];
				}
			}
			max *= 2;
		}
		
		//displayscale
		double displayscale = getHeight()/max;
		//average drawing
		g.setColor(AVA);
		g.fillRect(0, getHeight() - (int)(average*displayscale), getWidth()/*- y_axis*/, 1);
		String avg = Double.toString(average);
		Pattern pattern = Pattern.compile("^\\d+(\\.\\d)?");
		Matcher matcher = pattern.matcher(avg);
		if (matcher.find()) {
			avg = avg.substring(matcher.start(), matcher.end());
		} else {
			avg = "err";
		}
		
		g.drawString(avg, 4, getHeight() - ((int)(average*displayscale) + 2)); //text
		
		//design values
		int offset = (getWidth()-150)/(size-1);
		int num = number_offset;
		int line = line_offset;
		
		//indicator drawing
		int[] y_values = new int[2];
		g.setColor(ABS);
		for(int i = 0; i < size-1; i++){
			y_values[0] = getHeight()- (int)(displayscale*values[i]);
			y_values[1] = getHeight()- (int)(displayscale * values[i+1]);
			Graphics2D g2 = (Graphics2D)g;
			g2.setStroke(new BasicStroke(3));
			g2.draw(new Line2D.Float(getWidth()- offset*i - line, y_values[0], getWidth() - offset*(i+1) - line, y_values[1]));
			g.drawString(Double.toString(values[i]), getWidth() - offset*i - num, getHeight() - ((int)(values[i]*displayscale) + 8));
		}
		g.drawLine(getWidth(), getHeight()- (int)(displayscale * values[0]), getWidth() - line, getHeight()- (int)(displayscale * values[0]));
		
		String val = Double.toString(values[size-1]);
		Matcher valm = pattern.matcher(val);
		if (valm.find()) {
			val = val.substring(valm.start(), valm.end());
		} else {
			val = "err";
		}
		
		g.drawString(val, getWidth() - offset*(size-1) - num, getHeight() - ((int)(values[size-1]*displayscale) + 8));
	}
}
