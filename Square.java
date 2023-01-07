import javax.swing.*;
import java.awt.*;
import javax.swing.border.EmptyBorder;

 /** IChess version 1.0
  **
  ** Copyright 2006 Stephen Baker (2913895) and Chris Roy (3048899)
  **/
  
class Square extends JButton 
{
	private static final long serialVersionUID = 70L;
	
	private ImageIcon highlight; //the highlight background
	public Square()
	{
		super();
		highlight = null;  //set no highlight initially
		
		//no border
		this.setBorderPainted(false);
		this.setBorder(new EmptyBorder(0,0,0,0));
	}
	
	public void setHighlight(ImageIcon hl)
	{
		this.highlight = hl; //set the highlight to whatever is passed
	}
	
	//if there is a highlight draw it on the button
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		if(highlight!=null)
		{
			highlight.paintIcon(this,g,0,0);
		}
	}
}
