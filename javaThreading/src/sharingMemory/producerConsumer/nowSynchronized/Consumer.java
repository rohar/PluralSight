package sharingMemory.producerConsumer.nowSynchronized;

public class Consumer implements Runnable, ProduceObserver {
	private volatile Produce produce = null;
	
	public void onProduction(Produce produce)
	{
		this.produce = produce;
	}
	
	public void run()
	{
		System.out.println("Consumer starting");
		
		while (true) {
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// Can ignore this
			}
			
			if (produce != null) {
				int produceInstance = produce.getInstance();
				Produce.Color color = produce.getColor();
				
				System.out.println("Last produce instance: " + produceInstance);
				System.out.println("Last produce color: " + color.name());
				
				if (produceInstance == 10) {
					break;
				}
			}
		}
		
		System.out.println("Consumer terminating");
	}
}
