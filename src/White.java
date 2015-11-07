import java.io.*;
import java.util.*;
import java.awt.*;
import javax.swing.*;
public class White extends JApplet{
	public static double scalar;
	public static ArrayList<Region> regions = new ArrayList<Region>();
	public static void scale(ArrayList<Double> list){
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
		ArrayList<Double> xPointList;
		ArrayList<Double> yPointList;
		int[] xPoints;
		int[] yPoints;
		
		public Region(String name){
			regionName = name;
		}
		public String getName(){
			return regionName;
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
			for (int i=0; i<xPoints.length; i++){
				xPoints[i]=(int)(scalar*xPointList.get(i));
				yPoints[i]=(int)(scalar*yPointList.get(i));
			}
			shape = new Polygon(xPoints,yPoints,xPoints.length);
		}
	}
	public static void main(String[] args) {
		String file = args[0]+".txt";
		parseData(file, args[0]);
	}
	
	public static void parseData(String fileName, String skipped){
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
            scanner.nextLine();
            scanner.nextLine();
            
            
            while (scanner.hasNext()){
            	if (scanner.hasNextInt()) scanner.next();
                else if (scanner.hasNextDouble()) {
                	if (counter%2==0){
                		
                	}
                } else if (scanner.next()=="") scanner.next();
                else {
                	
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
		
	}

}
