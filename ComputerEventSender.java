import java.awt.*;
import java.awt.event.*;

public class ComputerEventSender
{
	public synchronized void addEventListener(ActionListener l)
	{
		_listeners.add( l );
	}
}