import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.LinkedList;

import javax.swing.JPanel;
import javax.swing.Timer;

public class Canvas extends JPanel {
	private static final long serialVersionUID = 1L;
	private LinkedList<Circle> circles;
	private BufferedImage im;
	private int w;
	private int h;
	
	private int red = 0;
	private int green = 120;
	private int blue = 240;
	
	public Canvas(int w, int h) {
		this.w = w;
		this.h = h;
		im = new BufferedImage(w, h, BufferedImage.TYPE_4BYTE_ABGR);
		circles = new LinkedList<>();
		Timer t = new Timer(25, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				repaint();
			}
		});
		t.start();
	}
	
	public void clickAt(int x, int y) {		
		Circle c = new Circle();
		c.x = x;
		c.y = y;
		c.current_r = 10;
		c.alpha = 1.0f;
		c.colour = nextColor(c.alpha);
		synchronized(circles) {
			circles.add(c);
		}
	}
	
	private Color nextColor(float alpha) {
		red = ++red % 360;
		green = ++green % 360;
		blue = ++blue % 360;
		
		float r = Math.abs((float) Math.sin(Math.toRadians(red)));
		float g = Math.abs((float) Math.sin(Math.toRadians(green)));
		float b = Math.abs((float) Math.sin(Math.toRadians(blue)));
		
		return new Color(r, g, b, alpha);
	}
	
	@Override
	public Dimension getPreferredSize() {
		return new Dimension(w, h);
	}
	
	@Override
	public void paint(Graphics g) {
		Graphics2D g2 = im.createGraphics();
		g2.setRenderingHint(
				RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setRenderingHint(
				RenderingHints.KEY_ALPHA_INTERPOLATION,
				RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
		g2.setBackground(Color.WHITE);
		g2.clearRect(0, 0, w, h);
		g2.setStroke(new BasicStroke(8));
		ArrayList<Circle> toRm = new ArrayList<>();
		synchronized(circles) {
			for (Circle c : circles) {
				g2.setColor(c.colour);
				g2.drawOval(
						(int)(c.x-(c.current_r/2)),
						(int)(c.y-(c.current_r/2)),
						(int)(c.current_r),
						(int)(c.current_r));
				c.current_r += 2;
				c.alpha -= 0.02;
				if (c.alpha <= 0) {
					toRm.add(c);
				} else {
					c.colour = new Color(
							c.colour.getRed()/255.0f,
							c.colour.getGreen()/255.0f,
							c.colour.getBlue()/255.0f,
							c.alpha);
				}
			}
		}
		synchronized(circles) {
			for (Circle c : toRm) {
				circles.remove(c);
			}
		}
		g2.setStroke(new BasicStroke(10));
		g2.setColor(Color.BLACK);
		g2.drawOval(0, 0, w, h);
		g.drawImage(im, 0, 0, null);
	}
	
	private class Circle {
		public float x;
		public float y;
		public float current_r;
		public float alpha;
		public Color colour;
	}
}
