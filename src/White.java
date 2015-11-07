import java.io.*;
import java.util.*;
import java.awt.*;
import javax.swing.*;
public class White extends JApplet{
	public static double scalar;
	public static Region[] regions;
	
	
	public void init() {
		System.out.println("init works");
		//Scanner sc = new Scanner (System.in);
		//String input = sc.nextLine();
		//String file = input + ".txt";
		String file = "USA" + ".txt";
		parseData(file);
	}
	
	public void scale(ArrayList<Double> list){
		int height = 1000;//this.getHeight();
		int width = 1000;//this.getWidth();
		double mapHeight = Math.abs(list.get(1)-list.get(3));
		double mapWidth = Math.abs(list.get(0)-list.get(2));
		if (height>=width){
			scalar = width/mapWidth;
		} else {
			scalar = height/mapHeight;
		}
	}
	
	public class Region {
		String regionName;
		Polygon shape;
		ArrayList<Double> xPointList = new ArrayList<Double>();
		ArrayList<Double> yPointList = new ArrayList<Double>();;
		int[] xPoints;
		int[] yPoints;
		
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
			xPoints = new int[xPointList.size()];
			yPoints = new int[yPointList.size()];
			for (int i=0; i < xPoints.length; i++){
				xPoints[i]=(int)(scalar*xPointList.get(i));
				yPoints[i]=(int)(scalar*yPointList.get(i));
			}
			shape = new Polygon(xPoints, yPoints, xPoints.length);
		}
	}
	
	public void parseData(String fileName){
		System.out.println("parse data works");
		
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
            
            for (int i = 0; i < regions.length; i++) {
            	System.out.println(regions[i].getName());
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
		for (int i = 0; i < regions.length; i++) {
			g.drawPolygon(regions[i].getShape());
		}
	}

}