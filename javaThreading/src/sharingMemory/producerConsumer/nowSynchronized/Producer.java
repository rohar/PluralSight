package sharingMemory.producerConsumer.nowSynchronized;

import java.util.ArrayList;
import java.util.List;

public class Producer implements Runnable {
	private volatile List<ProduceObserver> observers = new ArrayList<>();
	
	public void registerObserver(ProduceObserver observer)
	{
		observers.add(observer);
	}
	
	public void run()
	{
		System.out.println("Producer starting");
		
		for (int i=1; i<=10; i++) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// Can ignore this
			}
			
			Produce.ProduceBuilder builder = new Produce.ProduceBuilder();
			builder.withInstance(i);
			builder.withColor(Produce.Color.values()[i % Produce.Color.values().length]);
			Produce latestProduce = builder.build();
			
			observers.forEach(observer -> observer.onProduction(latestProduce));
		}
		
		System.out.println("Producer terminating");
	}
}
