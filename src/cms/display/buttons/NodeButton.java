package cms.display.buttons;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JComponent;

import cms.databank.structures.Node;
import cms.databank.structures.NodeState;
import cms.display.GraphicsConstants;
import cms.display.info.AddressPanel;

/**
 * The <code>NodeButton</code> class is a JComponent displaying a Node.
 * @author Eduard Parachivescu
 * @author Philippe Hebert
 * @version 1
 * -tag @refactor Philippe Hebert
 */
public class NodeButton extends JComponent implements MouseListener, GraphicsConstants {
	private static final long serialVersionUID = 1L;

	private Node core;
	private boolean select = false; 
	private Color background, foreground;
	

	public NodeButton(Dimension size, Node core) {
		super();
		this.enableInputMethods(true);
		this.setFocusable(true);
		
		this.core = core;
		
		this.addMouseListener(this);
		this.setPreferredSize(size);
		this.setVisible(true);
	}

	public void select(boolean select) {
		core.setSelected(select);
		this.select = select;
		repaint();
	}
	
	public Node getCore(){
		return this.core;
	}
	
	@Override
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		
		//Background
		if(select){
			background = SELECTED;
		} else {
			background = UNSELECTED;
		}
		g.setColor(background);
		g.fillRect(0, 0, getWidth(), getHeight());
		
		//Icon
		NodeState state = core.getState();
		switch(state){
		case ABSENT: foreground = ABS; break;
		case PRESENT: foreground = PRE; break;
		case AVAILABLE: foreground = AVA; break;
		case RUNNING: foreground = RUN; break;
		case ERROR: foreground = ERR; break;
		default: foreground = ABS; break;
		}
		IconBuilder.icon(g, foreground, background, getWidth(), getHeight(), core.getType(), core);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		AddressPanel.unselectLast(this);
		select(true);
		if(select){
			System.out.println("g>Call: " + core.getId());
		}
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
	}
}
