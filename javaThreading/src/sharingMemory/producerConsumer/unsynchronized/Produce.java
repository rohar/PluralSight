package sharingMemory.producerConsumer.unsynchronized;

public class Produce {
	enum Color {Red, Blue, Green, Yellow};
	
	private int instance = 0;
	private Color color;
	
	public void setInstance(int instance)
	{
		this.instance = instance;
	}
	
	public int getInstance()
	{
		return this.instance;
	}
	
	public void setColor(Color color)
	{
		this.color = color;
	}
	
	public Color getColor()
	{
		return this.color;
	}
}
