package sharingMemory.producerConsumer.nowSynchronized;

public class Main {
	public static void main(String[] args)
	{
		Producer producer = new Producer();
		Consumer consumer = new Consumer();
		
		producer.registerObserver(consumer);
		
		Thread producerThread = new Thread(producer, "producer");
		Thread consumerThread = new Thread(consumer, "consumer");
		
		producerThread.start();
		consumerThread.start();
	}
}
