import java.io.*;
import java.util.*;
import java.awt.*;
import javax.swing.*;
public class White extends JApplet{
	public static double scalar;
	public static double diffX;
	public static double diffY;
	public static double mapWidth, mapHeight;
	public static Region[] regions;
	
	public void init() {
		Scanner sc = new Scanner (System.in);
		String input = sc.nextLine();
		String file = input + ".txt";
		sc.close();
		//String file = "USA" + ".txt";
		parseData(file);
		repaint();
	}
	
	public void scale(ArrayList<Double> list){
		int height = getHeight();
		int width = getWidth();
		mapHeight = Math.abs(list.get(1)-list.get(3));
		mapWidth = Math.abs(list.get(0)-list.get(2));
		diffX = -1*list.get(0);
		diffY = -1*list.get(1);
		if (mapWidth >= mapHeight){
			scalar = width/mapWidth;
		} else {
			scalar = height/mapHeight;
		}
	}
	public double scaleX(double point){
		return getWidth()/2-scalar*mapWidth/2+scalar*point;
	}
	public double scaleY(double point){
		return getHeight()/2+scalar*mapHeight/2-scalar*point;
	}
	
	public class Region {
		String regionName;
		Polygon shape;
		ArrayList<Double> xPointList = new ArrayList<Double>();
		ArrayList<Double> yPointList = new ArrayList<Double>();;

		
		public Region(String name){
			regionName = name;
		}
		public String getName(){
			return regionName;
		}
		public Polygon getShape() {
			return shape;
		}
		public void addX(Double d){
			xPointList.add(d);
		}
		public void addY(Double d){
			yPointList.add(d);
		}
		public void makeRegion(){
			int[] xPoints = new int[xPointList.size()];
			int[] yPoints = new int[yPointList.size()];
			for (int i=0; i < xPoints.length; i++){
				xPoints[i]=(int)(scaleX(xPointList.get(i)+diffX));
				yPoints[i]=(int)(scaleY(yPointList.get(i)+diffY));
			}
			shape = new Polygon(xPoints, yPoints, xPoints.length);
		}
	}
	
	public void parseData(String fileName){

		ArrayList<Double> boundNums = new ArrayList<Double>();
		int currentRegion = 0;
		int counter = 0;
		
		try {
            Scanner scanner = 
                new Scanner(new BufferedReader(new FileReader(fileName)));
            
            for (int i=0; i<4; i++){
            		boundNums.add(Double.parseDouble(scanner.next()));
            }
            scale(boundNums);
            
            for (int j = 0; j < boundNums.size(); j++) {
            	System.out.println(boundNums.get(j));
            }
            
            regions = new Region[scanner.nextInt()];
            regions[0] = new Region(scanner.next());
            scanner.nextLine();
            scanner.nextLine();
            
            while (scanner.hasNext()){
            	if (scanner.hasNextInt()) scanner.next();
            	else if (scanner.hasNextDouble()) {
                	if (counter%2 == 0){
                		regions[currentRegion].addX(scanner.nextDouble());
                		counter++;
                	}
                	else {
                		regions[currentRegion].addY(scanner.nextDouble());
                		counter++;
                	}
                } 
                else {
                	regions[currentRegion].makeRegion();
                	currentRegion++;
                	regions[currentRegion] = new Region(scanner.next());
                	scanner.nextLine();
                	scanner.nextLine();
                }
            	
            }
            
            
            scanner.close();
        }
        catch(FileNotFoundException ex) {
            System.out.println(
                "Unable to open file '" + 
                fileName + "'");                
        }
	}
	
	public void paint(Graphics g){
		Region[] reg2 = new Region[regions.length];
        for (int i = 0; i < regions.length; i++) {
        	reg2[i] = new Region(regions[i].getName());
        	reg2[i].shape = regions[i].getShape();
        }
		for (int i = 0; i < reg2.length; i++) {
			g.drawPolygon(reg2[i].getShape());
		}

	}

}