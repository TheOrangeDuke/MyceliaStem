package cms.view;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JPanel;

import cms.view.panel.AddressBar;

public class InfoBar extends JPanel implements GraphicsConstants {

	private static final long serialVersionUID = 1L;

	private static QuickBar quickbar;
	private static AddressBar addressbar;
	
	public InfoBar() {

		this.setBackground(BACK);
		this.setPreferredSize(new Dimension(800, 120));
		this.setLayout(new BorderLayout());
		
		quickbar = new QuickBar();
		this.add(quickbar, BorderLayout.NORTH);
		addressbar = new AddressBar();
		this.add(addressbar, BorderLayout.SOUTH);
		if(!quickbar.getBackground().equals(PRE)){
			System.out.println("Not PRE");
		}

		Thread refresh = new Thread(new Runnable() {
			public void run() {
				while (true) {
					try {
						Thread.sleep(200);
						addressbar.repaint();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

				}
			}
		});

		refresh.start();
	}
	
	/*public static void setDisplayType(DisplayType dt){
		displaytype = dt;
		graphingpanel.setDisplayType(displaytype);
	}
	
	public static DisplayType getDisplayType(){
		return displaytype;
	}
*/
	
}