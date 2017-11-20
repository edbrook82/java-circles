import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;

import javax.swing.JFrame;

public class Circles {

	public static void main(String[] args) {
		int w = 800;
		int h = 800;
		final JFrame main = new JFrame("Click-Circles");
		
		main.setUndecorated(true);
		main.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				main.setShape(new Ellipse2D.Double(
						0, 0,
						main.getWidth(), main.getHeight()));
				main.setOpacity(0.75f);
			}
		});
		
		main.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		main.setSize(w, h);
		main.setResizable(false);
		final Canvas cc = new Canvas(w, h);
		
		cc.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				main.dispose();
				System.exit(0);
			}
		});
		
		cc.addMouseMotionListener(new MouseAdapter() {
			private int step = 1;
			private int lx = 0;
			private int ly = 0;
			
			@Override
			public void mouseMoved(MouseEvent e) {
				if (Math.abs(lx-e.getX()) > step || Math.abs(ly-e.getY()) > step) {
					cc.clickAt(e.getX(), e.getY());
					lx = e.getX();
					ly = e.getY();
				}
			}
		});
		main.getContentPane().add(cc);
		main.pack();
		main.setVisible(true);
	}

}
