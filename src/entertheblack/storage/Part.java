package entertheblack.storage;

import java.awt.Image;

import entertheblack.game.Player;
import entertheblack.menu.Assets;

// A type of weapon(TODO) or structure that can be put on a ship.

public class Part {
	int cost; // Cost in credits
	// TODO: Add resource costs.
	String name;
	String info; // A small info text that will be displayed in the customize screen.
	Image img;
	
	boolean weapon;
	boolean engine;
	boolean reactor;
	int techLevel; // Tech level needed to use.
	// structure Stats(mostly 0):
	int hull;
	int speed; // Max speed. Scales with 1/m_Ship
	int force; // Or engine strength.
	int mass;
	double powerProd; // Power produced per tick
	double passivePowerConsumption; // Power used per tick.
	double turnSpeed; // How fast the ship turns. Scales with 1/m_Ship
	// TODO weapon type.
	
	public Part(String data) { // TODO: File Loading
		String[] lines = data.split("\n");
		for(int i = 0; i < lines.length; i++) {
			String [] parts = lines[i].split("=");
			if(parts.length < 2) {
				if(lines[i].equals("Weapon")) {
					weapon = true;
				}
				if(lines[i].equals("Engine")) {
					engine = true;
				}
				if(lines[i].equals("Reactor")) {
					reactor = true;
				}
				continue;
			}
			if(parts[0].equals("Hull")) {
				hull = Integer.parseInt(parts[1]);
			} else if(parts[0].equals("Speed")) {
				speed = Integer.parseInt(parts[1]);
			} else if(parts[0].equals("Force")) {
				force = Integer.parseInt(parts[1]);
			} else if(parts[0].equals("Mass")) {
				mass = Integer.parseInt(parts[1]);
			} else if(parts[0].equals("PowerProduction")) {
				powerProd = Double.parseDouble(parts[1]);
			} else if(parts[0].equals("PowerConsumtion")) {
				passivePowerConsumption = Double.parseDouble(parts[1]);
			} else if(parts[0].equals("Turn")) {
				turnSpeed = Double.parseDouble(parts[1]);
			} else if(parts[0].equals("Name")) {
				name = parts[1];
			} else if(parts[0].equals("Tech")) {
				techLevel = Integer.parseInt(parts[1]);
			} else if(parts[0].equals("Image")) {
				img = Assets.getImage("parts/"+parts[1]+".png");
				if(img == null) {
					System.err.println("Could not find part image "+parts[1]+".png in assets/parts!");
				}
			} else {
				System.err.println("Unknown argument for type part \"" + parts[0] + "\" with value" + parts[1] + ". Skipping line!");
				return;
			}
		}
	}
	
	// Test if this part can be put in a slot, based on the players credits.
	public boolean fitsIn(ShipSlot sl, Player p) {
		if(weapon && !sl.weapon)
			return false;
		if(engine && !sl.engine)
			return false;
		if(reactor && !sl.reactor)
			return false;
		return p.credits >= cost;
	}
}