package cms.view;

import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JPanel;

import cms.model.DataFactory;
import cms.view.element.NodeButton;

public class AddressBar extends JPanel implements GraphicsConstants {
	private static final long serialVersionUID = 1L;

	private static NodeButton[] button;
	private static NodeButton lastButtonClicked;
	private Dimension size;

	public AddressBar() {
		this.setBackground(BACK);
		int rows = (int) Math.ceil((double) DataFactory.core.length
				/ ADDRESS_COLUMNS);

		this.setPreferredSize(new Dimension(800, rows * 100));
		GridLayout coreLayout = new GridLayout(rows, ADDRESS_COLUMNS);
		coreLayout.setHgap(ADDRESS_GAP);
		coreLayout.setVgap(ADDRESS_GAP);

		this.setLayout(coreLayout);
		
		button = new NodeButton[DataFactory.core.length];
		for (int i = 0; i < DataFactory.core.length; i++) {
			button[i] = new NodeButton(size, DataFactory.core[i]);
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

}