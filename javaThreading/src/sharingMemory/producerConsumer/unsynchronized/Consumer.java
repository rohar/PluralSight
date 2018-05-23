package sharingMemory.producerConsumer.unsynchronized;

public class Consumer implements Runnable {
	private final Producer producer;
	
	// We shouldn't couple producers and consumers like this
	// See Inter-thread Communication and Signalling
	public Consumer(Producer producer)
	{
		this.producer = producer;
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
			
			Produce produce = producer.getLatestProduce();

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
