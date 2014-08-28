package cms.display.info;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import cms.databank.OverLord;
import cms.display.GraphicsConstants;
import cms.display.buttons.NodeButton;

public class AddressPanel extends JPanel implements GraphicsConstants {
	private static final long serialVersionUID = 1L;

	private static NodeButton[] button;
	private static NodeButton lastButtonClicked;
	private static Dimension size;
	private static Container thisContainer;
	private static int rows;
	
	public AddressPanel() {
		thisContainer = this;
		this.setBackground(BACK);
		this.setBorder(BorderFactory.createEmptyBorder(ADDRESS_GAP, 0, ADDRESS_GAP, 0));
		rows = (int) Math.ceil((double) OverLord.nodeCore.size()/ ADDRESS_COLUMNS);

		this.setPreferredSize(new Dimension(800, rows * 100));
		this.setMinimumSize(new Dimension(800, 100));
		GridLayout coreLayout = new GridLayout(0, ADDRESS_COLUMNS);
		coreLayout.setHgap(ADDRESS_GAP);
		coreLayout.setVgap(ADDRESS_GAP);

		this.setLayout(coreLayout);
		
		button = new NodeButton[OverLord.nodeCore.size()];
		for (int i = 0; i < OverLord.nodeCore.size(); i++) {
			button[i] = new NodeButton(size, OverLord.nodeCore.get(i));
			this.add("Button", button[i]);
		}
	}

	public static void unselectLast(NodeButton nextLast) {
		if(lastButtonClicked != null) {
			lastButtonClicked.select(false);
		}
		lastButtonClicked = nextLast;
	}
	
	public static NodeButton nodeButton(int ID){
		return button[ID];
	}
	
	public static void updateButtonList() {
		thisContainer.removeAll();
		rows = (int) Math.ceil((double) OverLord.nodeCore.size()/ ADDRESS_COLUMNS);
		button = new NodeButton[OverLord.nodeCore.size()];
		for (int i = 0; i < button.length; i++) {
			button[i] = new NodeButton(size, OverLord.nodeCore.get(i));
			thisContainer.add("Button", button[i]);
		}

		thisContainer.setPreferredSize(new Dimension(800, rows * 100));
		thisContainer.setMinimumSize(new Dimension(800, 100));
		thisContainer.setSize(thisContainer.getPreferredSize());
		thisContainer.repaint();
		thisContainer.revalidate();
		thisContainer.repaint();
	}
	
	public int getRows(){
		return rows;
	}

}
