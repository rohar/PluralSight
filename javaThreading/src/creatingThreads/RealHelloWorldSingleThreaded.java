package creatingThreads;

public class RealHelloWorldSingleThreaded {
    public static class Greeter implements Runnable {
        private String country;

        public Greeter(String country) {
            this.country = country;
        }

        public void run() {
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
            }
            long id = Thread.currentThread().getId();

            System.out.println(id + ": Hello " + country + "!");
        }
    }

    public static void main(String[] args) {
        String countries[] = {"France", "India", "China", "USA", "Germany"};

        for (String country : countries) {
            new Thread(new Greeter(country)).start();
        }
    }
}
