package sharingMemory.producerConsumer.unsynchronized;

public class Main {
	public static void main(String[] args)
	{
		Producer producer = new Producer();
		Consumer consumer = new Consumer(producer);
		
		Thread producerThread = new Thread(producer, "producer");
		Thread consumerThread = new Thread(consumer, "consumer");
		
		producerThread.start();
		consumerThread.start();
	}
}
