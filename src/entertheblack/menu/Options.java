package entertheblack.menu;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import entertheblack.gui.ActionListener;
import entertheblack.gui.Screen;
import entertheblack.gui.components.Button;

public class Options extends Screen implements ActionListener {
	List<Button> buttons = new ArrayList<>();
	
	int buttonsel = 1;
	
	public Options() {
		buttons.add(new Button(690, 190, 500, 50, this, 1, "Controls"));
		buttons.add(new Button(690, 340, 500, 50, this, 2, "Graphics(WIP)"));
		buttons.add(new Button(690, 490, 500, 50, this, 3, "Back To Menu"));
		buttons.add(new Button(690, 640, 500, 50, this, 4, "Exit Game"));
		buttons.get(0).selectedB = true;
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == 38 && buttonsel > 1) {
			buttons.get(buttonsel-1).selectedB = false;
			buttonsel--;
			buttons.get(buttonsel-1).selectedB = true;
		}
		
		if (e.getKeyCode() == 40 && buttonsel < buttons.size() && buttonsel > 0) {
			buttons.get(buttonsel-1).selectedB = false;
			buttonsel++;
			buttons.get(buttonsel-1).selectedB = true;
		}
		
		if (e.getKeyCode() == 17 && buttonsel > 0) {
			buttons.get(buttonsel-1).pressedB = true;
			buttonsel *= -1;
		}
		
		if (e.getKeyCode() != 17 && buttonsel < 0) {
			buttonsel *= -1;
			buttons.get(buttonsel-1).pressedB = false;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == 17 && buttonsel < 0) {
			buttons.get(-buttonsel-1).trigger();
			this.buttonsel = 1;
		}
	}

	@Override
	public void paint(Graphics2D g) {
		g.drawImage(Assets.bg, 0, 0, 1920, 1080, null);
		g.setFont(new Font("Sansserif", 0, 20));
		for(Button b : buttons) {
			b.paint(g);
		}
	}

	@Override
	public void mouseUpdate(int x, int y, boolean pressed) {
		for(Button b : buttons) {
			b.mouseUpdate(x, y, pressed);
		}
	}

	@Override
	public void pressed(int id) {
		if(id == 1)
			Assets.screen = new Controls();
		// TODO graphics settings.
		if(id == 3)
			Assets.screen = new MainMenu();
		if(id == 4)
			System.exit(1);
	}

}
