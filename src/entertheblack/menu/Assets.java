package entertheblack.menu;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import javax.imageio.ImageIO;

import entertheblack.fight.Game;
import entertheblack.fight.MPGame;
import entertheblack.game.Animation;
import entertheblack.game.Player;
import entertheblack.game.ResourceType;
import entertheblack.game.Star;
import entertheblack.game.World;
import entertheblack.gui.Screen;
import entertheblack.storage.Node;
import entertheblack.storage.Part;
import entertheblack.storage.ShipData;
import entertheblack.storage.Species;
import entertheblack.storage.Variant;
import entertheblack.storage.WeaponData;

// Several static things used everywhere.

public class Assets {
	public static Screen screen = new MainMenu();
	public static Game game = new MPGame();
	private static HashMap<String, Image> stars = new HashMap<>(), planets = new HashMap<>();
	
	public static World curWorld;
	
	public static List<Part> parts = new ArrayList<>();
	public static List<ShipData> shipData = new ArrayList<>();
	public static List<WeaponData> weaponData = new ArrayList<>();
	public static List<Animation> animations = new ArrayList<>();
	public static List<Species> species = new ArrayList<>();
	public static List<Variant> variants = new ArrayList<>();
	
	public static ResourceType[] resources;
	
	public static int[] Controls;
	
	public static int gamemode = 0;
	
	//static BufferedImage ships = new BufferedImage(100, 50, 2);
	public static Color btn, btnpr, btnbg, btnsl; // Button colors
	public static Color text; // text color.
	public static Color toolTipbg, toolTiptext;
	public static Image bg, bgMenu, hb;
	
	public static Variant getVariant(String name) {
		for(Variant v : variants) {
			if(v.name.equals(name))
				return v;
		}
		return null;
	}
	
	public static ShipData getShipData(String name) {
		for(ShipData sd : shipData) {
			if(sd.name.equals(name))
				return sd;
		}
		return null;
	}
	public static Part getPart(String name) {
		for(Part p : parts) {
			if(p.name.equals(name))
				return p;
		}
		return null;
	}
	public static Animation getAnimation(String name) {
		for(Animation a : animations) {
			if(a.name.equals(name))
				return a;
		}
		return null;
	}
	
	// Some people seem to use windows which for unknown reasons still uses "\" as path separator.
	public static String takeCareOfWindows(String path) {
		String[] sep = path.split("/");
		StringBuilder ret = new StringBuilder();
		for(int i = 0; i < sep.length; i++) {
			if(i != 0) {
				ret.append(File.separator);
			}
			ret.append(sep[i]);
		}
		return ret.toString();
	}

	public static String readFile(String fileName) {
		fileName = takeCareOfWindows("assets/"+fileName);
		try {
			File file = new File(fileName);
			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);
			StringBuffer sb = new StringBuffer();
			String line;
			while((line = br.readLine()) != null) {
				sb.append(line);
				sb.append("\n");
			}
			fr.close();
			return sb.toString();
		} catch(Exception e) {e.printStackTrace();}
		return "";
	}
	
	public static void writeFile(String data, String fileName) {
		try {
			FileWriter f = new FileWriter(takeCareOfWindows("assets/"+fileName));
			f.write(data);
			f.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// Game content should be able to reach auto-generated images.
	public static Image getLocalImage(String name) {
		if(name.equals("bg_menu"))
			return bgMenu;
		if(name.equals("bg"))
			return bg;
		return null;
	}

	public static Image getImage(String fileName) {
		if(fileName.charAt(0) == '$')
			return getLocalImage(fileName.substring(1));
		fileName = takeCareOfWindows("assets/"+fileName);
		try {
			return ImageIO.read(new File(fileName));
		} catch(Exception e) {}//e.printStackTrace();}
		return null;
	}
	
	private static void loadStars() {
		String fileName = takeCareOfWindows("assets/stars/");
		File[] f = (new File(fileName)).listFiles();
		for (File file : f) {
			if (file != null) {
				String name = file.getName();
				try {
					stars.put(name, ImageIO.read(file));
					System.out.println("Loaded image "+name+".");
				} catch (IOException e) {}
			}
		}
	}
	
	private static void loadPlanets() {
		String fileName = takeCareOfWindows("assets/planets/");
		File[] f = (new File(fileName)).listFiles();
		for (File file : f) {
			if (file != null) {
				String name = file.getName();
				try {
					planets.put(name, ImageIO.read(file));
					System.out.println("Loaded image "+name+".");
				} catch (IOException e) {}
			}
		}
	}
	
	public static Image getPlanetImg(String key) {
		Image ret = stars.get(key);
		if(ret == null)
			ret = planets.get(key);
		if(ret == null) {
			System.err.println("No Planet/Star Image named \""+key+"\" found!");
			return randPlanetImg(1);
		}
		return ret;
	}
	
	public static Image randPlanetImg(double eng) {
		Entry<String, Image> [] imgs = planets.entrySet().toArray(new Entry[0]);
		int selection = (int)(Math.random()*imgs.length);
		return imgs[selection].getValue();
	}
	
	public static Image randStarImg(double eng) {
		Entry<String, Image> [] imgs = stars.entrySet().toArray(new Entry[0]);
		int selection = (int)(Math.random()*imgs.length);
		return imgs[selection].getValue();
	}

	static int parseInt(String str) {
		return Integer.parseInt(str.trim());
	}
	
	public static List<ShipData> createShipData(String data, String file) {
		List<ShipData> list = new ArrayList<>();
		Node ship = new Node(data);
		Node[] ships = ship.nextNodes;
		for(int i = 0; i < ships.length; i++) {
			list.add(new ShipData(ships[i], file));
		}
		return list;
	}
	
	static void createWeaponData(String data, String file) {
		Node weapon = new Node(data);
		Node[] weapons = weapon.nextNodes;
		for(int i = 0; i < weapons.length; i++) {
			weaponData.add(new WeaponData(weapons[i], file));
		}
	}
	
	static void createVariant(String data, String file) {
		Node variant = new Node(data);
		Node[] variants = variant.nextNodes;
		for(int i = 0; i < variants.length; i++) {
			Assets.variants.add(new Variant(variants[i], file));
		}
	}
	
	static void loadResources() {
		String res = readFile("resources.txt");
		Node resource = new Node(res);
		Node[] data = resource.nextNodes;
		resources = new ResourceType[data.length];
		for(int i = 0; i < data.length; i++) {
			resources[i] = new ResourceType(data[i], "assets/resources.txt");
		}
	}
	
	static void loadSettings(String file) {
		// Initialize standard settings in case the settings file is corrupted/incomplete:
		btn = new Color(111, 111, 111);
		btnsl = new Color(204, 198, 24);
		btnpr = new Color(169, 159, 0);
		btnbg = new Color(0, 0, 0);
		text = new Color(200, 200, 200);
		toolTipbg = new Color(255, 255, 200);
		toolTiptext = new Color(0, 0, 0);
		
		Controls = new int[]{ 37, 39, 17, 16, 38, 65, 68, 70, 71, 87 };
		// Read the data file and overwrite the standard settings.
		try {
			String [] lines = file.split("\n");
			for(String line : lines) {
				String [] val = line.split("=");
				if(val.length <= 1)
					continue;
				if(val[0].equals("btn")) {
					String[] rgb = val[1].split(",");
					btn = new Color(Integer.parseInt(rgb[0]), Integer.parseInt(rgb[1]), Integer.parseInt(rgb[2]));
				}
				else if(val[0].equals("btnpr")) {
					String[] rgb = val[1].split(",");
					btnpr = new Color(Integer.parseInt(rgb[0]), Integer.parseInt(rgb[1]), Integer.parseInt(rgb[2]));
				}
				else if(val[0].equals("btnsl")) {
					String[] rgb = val[1].split(",");
					btnsl = new Color(Integer.parseInt(rgb[0]), Integer.parseInt(rgb[1]), Integer.parseInt(rgb[2]));
				}
				else if(val[0].equals("btnbg")) {
					String[] rgb = val[1].split(",");
					btnbg = new Color(Integer.parseInt(rgb[0]), Integer.parseInt(rgb[1]), Integer.parseInt(rgb[2]));
				}
				else if(val[0].equals("text")) {
					String[] rgb = val[1].split(",");
					text = new Color(Integer.parseInt(rgb[0]), Integer.parseInt(rgb[1]), Integer.parseInt(rgb[2]));
				}
				else if(val[0].equals("tooltip")) {
					String[] rgb = val[1].split(",");
					toolTipbg = new Color(Integer.parseInt(rgb[0]), Integer.parseInt(rgb[1]), Integer.parseInt(rgb[2]));
				}
				else if(val[0].equals("tooltiptext")) {
					String[] rgb = val[1].split(",");
					toolTiptext = new Color(Integer.parseInt(rgb[0]), Integer.parseInt(rgb[1]), Integer.parseInt(rgb[2]));
				}
				else if(val[0].equals("keys")) {
					String[] keys = val[1].split(",");
					if(keys.length == Controls.length) {
						for(int j = 0; j < Controls.length; j++) {
							Controls[j] = Integer.parseInt(keys[j]);
						}
					} else {
						System.err.println("Wrong number of arguments in settings.txt for keys: "+keys.length+" instead of "+Controls.length+".");
					}
				}
			}
		} catch(Exception e) { // Catch any error if occuring when reading the settings and revert to standard settings:
			System.err.println("File \"settings.txt\" corrupted:");
			e.printStackTrace();
			System.err.println("Reverting to standard settings.");
		}
	}
	
	private static void addColors(StringBuilder sb, Color c) {
		sb.append(c.getRed());
		sb.append(",");
		sb.append(c.getGreen());
		sb.append(",");
		sb.append(c.getBlue());
	}
	
	static void saveSettings() {
		// Generate the file:
		StringBuilder sb = new StringBuilder();
		// Add button colors:
		sb.append("btn=");
		addColors(sb, btn);
		sb.append("\n");
		sb.append("btnsl=");
		addColors(sb, btnsl);
		sb.append("\n");
		sb.append("btnpr=");
		addColors(sb, btnpr);
		sb.append("\n");
		sb.append("btnbg=");
		addColors(sb, btnbg);
		sb.append("\n");
		sb.append("text=");
		addColors(sb, text);
		sb.append("\n");
		sb.append("tooltip=");
		addColors(sb, toolTipbg);
		sb.append("\n");
		sb.append("tooltiptext=");
		addColors(sb, toolTiptext);
		sb.append("\n");
		// Add key preferences:
		sb.append("keys=");
		for(int i = 0; i < Controls.length; i++) {
			if(i != 0)
				sb.append(",");
			sb.append(Controls[i]);
		}
		sb.append("\n");
		
		writeFile(sb.toString(), "settings.txt");
	}
	
	private static void loadAnimations() {
		String res = readFile("animations.txt");
		Node animation = new Node(res);
		Node[] data = animation.nextNodes;
		for(int i = 0; i < data.length; i++) {
			animations.add(new Animation(data[i], "assets/animations.txt"));
		}
	}
	
	private static void loadParts() {
		String res = readFile("parts.txt");
		Node part = new Node(res);
		Node[] data = part.nextNodes;
		for(int i = 0; i < data.length; i++) {
			parts.add(new Part(data[i], "assets/parts.txt"));
		}
	}
	
	static Color tempToColor(int temp) {
		int r, g, b;
		if(temp < 1000)
			r = temp*256/1000;
		if(temp < 6600)
			r = 255;
		else
			r = (int)(329.698727446*Math.pow(temp/100-60, -0.1332047592));
		if(temp < 6600)
			g = (int)(temp/100*99.4708025861*Math.log(temp/100)-161.1195681661);
		else
			g = (int)(288.1221695283*Math.pow(temp/100-60, -0.0755148492));
		if(temp > 6600)
			b = 255;
		else if(temp < 1900)
			b = 0;
		else
			b = (int)(138.5177312231*Math.log(temp/100-10)-305.0447927307);
		if(r < 0) r = 0;
		if(r > 255) r = 255;
		if(g < 0) g = 0;
		if(g > 255) g = 255;
		if(b < 0) b = 0;
		if(b > 255) b = 255;
		return new Color(r, g, b);
	}
	
	static Image generateRandomStarField(int stars, int width, int height) {
		BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = (Graphics2D)img.getGraphics();
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		for(int i = 0; i < stars; i++) {
			int x = (int)(Math.random()*img.getWidth())+img.getWidth()/2;
			int y = (int)(Math.random()*img.getHeight())+img.getHeight()/2;
			int temp = (int)(Math.random()*10000);
			int size = (int)(Math.random()*5*Math.random()*Math.random())+1;
			Color c = tempToColor(temp);
			g.setColor(tempToColor(temp));
			for(int j = 0; j < size; j++) {
				g.setColor(new Color(c.getRed(), c.getGreen(), c.getBlue(), (int)(64)));
				g.fillOval(x-size+j, y-size+j, 2*size-2*j, 2*size-2*j);
				g.fillOval(x-size+j-img.getWidth(), y-size+j, 2*size-2*j, 2*size-2*j);
				g.fillOval(x-size+j, y-size+j-img.getHeight(), 2*size-2*j, 2*size-2*j);
				g.fillOval(x-size+j-img.getWidth(), y-size+j-img.getHeight(), 2*size-2*j, 2*size-2*j);
			}
		}
		return img;
	}

	static void loadData() {
		System.out.println((int)'\t');
		bgMenu = generateRandomStarField(1000, 1920, 1080);
		bg = generateRandomStarField(1000, 1000, 1000);
		// TODO: Load color from settings.txt
		hb = getImage("hb.png");
		loadSettings(readFile("settings.txt"));
		loadStars();
		loadPlanets();
		loadResources();
		loadAnimations();
		loadParts();
		String [] spec = readFile("species.txt").split("\n");
		for(int i = 0; i < spec.length; i++) {
			Species local = new Species(spec[i]);
			species.add(local);
			createWeaponData(readFile(spec[i]+"/weapons"), "assets/"+spec[i]+"/weapons");
			System.out.println("Registered species: "+spec[i]+".");
		}
		for(ShipData sd : shipData) {
			sd.assignWeaponData(weaponData);
		}
		for(int i = 0; i < spec.length; i++) {
			createVariant(readFile(spec[i]+"/variants.txt"), "assets/"+spec[i]+"/variants");
			System.out.println("Registered species: "+spec[i]+".");
		}
		game.reset(0,  0);
	}
	
	public static void saveGame(World curWorld) {
		StringBuilder file = new StringBuilder();
		// Save world:
		file.append("{");
		curWorld.map.save(file);
		file.append("}");
		Player p = curWorld.player;
		file.append("{");
		p.save(file);
		file.append("}");
		file.append("{");
		curWorld.save(file);
		file.append("}");
		writeFile(file.toString(), "saves/save.txt");
	}

	public static void resetSettings() {
		// Load the standard settings:
		loadSettings("");
		// Save them:
		saveSettings();
		
	}
}