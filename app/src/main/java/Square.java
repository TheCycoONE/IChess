import javax.swing.*;
import java.awt.*;
import javax.swing.border.EmptyBorder;

class Square extends JButton 
{
	private static final long serialVersionUID = 70L;
	
	private ImageIcon highlight;
	public Square()
	{
		super();
		highlight = null;
		
		this.setBorderPainted(false);
		this.setBorder(new EmptyBorder(0,0,0,0));
	}
	
	public void setHighlight(ImageIcon hl)
	{
		this.highlight = hl;
	}
	
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		if(highlight!=null)
		{
			highlight.paintIcon(this,g,0,0);
		}
	}
}
