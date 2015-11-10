import java.io.*;
import java.util.*;
import java.awt.*;
import javax.swing.*;
public class RedBlue extends JApplet{
	public static double scalar;
	public static double diffX;
	public static double diffY;
	public static double mapWidth, mapHeight;
	public static Region[] regions;
	public static ArrayList<VoteSpace> spaces = new ArrayList<VoteSpace>();
	
	public void init() {
		Scanner sc = new Scanner (System.in);
		System.out.println("Enter the map to print: ");
		String input1 = sc.next();
		System.out.println("Enter the election year: ");
		String input2 = sc.next();
		String mapFile = input1 + ".txt";
		String elecFile = input1 + input2 + ".txt";
		sc.close();
		parseData(mapFile);
		parseColors(elecFile);
		determineColor();
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
		Color c;
		ArrayList<Double> xPointList = new ArrayList<Double>();
		ArrayList<Double> yPointList = new ArrayList<Double>();;
		
		public Region(String name){
			regionName = name;
		}
		public Color getColor() {
			return c;
		}
		public void setColor (Color d) {
			c = d;
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
	
	public class VoteSpace {
		String spaceName;
		int redVotes;
		int blueVotes;
		int greenVotes;
		Color c;
		public VoteSpace(String name){
			spaceName = name;
		}
		public int getRed(){
			return redVotes;
		}
		public void setRed(int red){
			redVotes = red;
		}
		public int getBlue(){
			return blueVotes;
		}
		public void setBlue(int blue){
			blueVotes = blue;
		}
		public int getGreen(){
			return greenVotes;
		}
		public void setGreen (int green){
			greenVotes = green;
		}
		public String getName() {
			return spaceName;
		}
		public Color getColor() {
			return c;
		}
		public void setColor() {
			if (redVotes > blueVotes) c = Color.RED;
			else c = Color.BLUE;
		}
	}
	
	public void parseColors(String file){
		int counter = 0;
		int currentSpace = 0;
		try {
			Scanner scanner = new Scanner(new BufferedReader(new FileReader(file)));
			scanner.useDelimiter(",");
			
			for (int i = 0; i < 4; i++) {
				scanner.next();
			}

			while (scanner.hasNext()){
				String s;
            	if(counter%4==0){
            		s = scanner.next();
            		spaces.add(new VoteSpace(s));
            		counter = 1;
            	} else if (counter%4==1){
            		spaces.get(currentSpace).setRed(scanner.nextInt());
            		counter = 2;
            	} else if (counter%4==2){
            		spaces.get(currentSpace).setBlue(scanner.nextInt());
            		counter = 3;
            	} else {
            		spaces.get(currentSpace).setGreen(scanner.nextInt());
            		currentSpace++;
            		counter = 0;
            	}
            }
			
			for (int i = 0; i < spaces.size(); i++) {
				spaces.get(i).setColor();
			}
			scanner.close();
		}
		catch(FileNotFoundException ex) {
            System.out.println(
                "Unable to open file '" + 
                file + "'");                
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
            regions = new Region[scanner.nextInt()];
            scanner.nextLine();
            scanner.nextLine();
            regions[0] = new Region(scanner.nextLine());
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
                		counter--;
                	}
                } 
                else {
                	regions[currentRegion].makeRegion();
                	currentRegion++;
                	scanner.nextLine();
                	scanner.nextLine();
                	regions[currentRegion] = new Region(scanner.nextLine());
                	scanner.nextLine();
                	scanner.nextLine();
                }
            	
            }
            regions[currentRegion].makeRegion();
            
            scanner.close();
        }
        catch(FileNotFoundException ex) {
            System.out.println(
                "Unable to open file '" + 
                fileName + "'");                
        }
	}
	
	public void determineColor(){
		for (int i = 0; i < regions.length; i++) {
			for (int j = 0; j < spaces.size(); j++) {
				if (regions[i].getName().equals(spaces.get(j).getName()) || ("\n" + regions[i].getName()).equals(spaces.get(j).getName())) {
					regions[i].setColor(spaces.get(j).getColor());
					break;
				}
			}
		}
	}
	public void paint(Graphics g){
		Graphics2D g2 = (Graphics2D) g;
		Region[] reg2 = new Region[regions.length];
		 for (int i = 0; i < regions.length; i++) {
	        	reg2[i] = new Region(regions[i].getName());
	        	reg2[i].shape = regions[i].getShape();
	        	reg2[i].setColor(regions[i].getColor());
	    }

		for (int i = 0; i < reg2.length; i++) {
			g2.setStroke(new BasicStroke(3));
			g2.setColor(Color.BLACK);
			g2.drawPolygon(reg2[i].getShape());
			g2.setColor(reg2[i].getColor());
			g2.fillPolygon(reg2[i].getShape());
		}


	}

}