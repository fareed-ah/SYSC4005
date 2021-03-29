import java.util.Random;

public class Workstation3 implements Runnable {

        private int c1Buffer;
        private int c3Buffer;
        private double average;
        private boolean doStop = false;
        private int productsProduced = 0;

        public Workstation3(double average) {
            this.c1Buffer = 0;
            this.c3Buffer = 0;
            this.average = average;
        }

        private void makeProduct(){
            if(c1Buffer > 0 && c3Buffer > 0){
                try {
                    Thread.sleep((long) randomVariateGeneration(average) * 20);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                c1Buffer --;
                c3Buffer --;
                productsProduced++;
            }
        }

        public void addComponent1(){
            c1Buffer ++;
        }

        public double addComponent3(){

            if(isC3BufferAvailable()){
                c3Buffer++;
                return 0;
            }

            double blockedTime = System.currentTimeMillis();
            while(!isC3BufferAvailable()){
                try {
                   Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            c3Buffer ++;
            return System.currentTimeMillis() - blockedTime;
        }

        public synchronized boolean isC1BufferAvailable(){
            return c1Buffer < 2;
        }
        public synchronized boolean isC3BufferAvailable(){
            return c3Buffer < 2;
        }

    public int getC1Buffer() {
        return c1Buffer;
    }

    public int getC3Buffer() {
        return c3Buffer;
    }

    @Override
    public void run() {
        while(keepRunning()) {
            makeProduct();
        }
    }

    private double randomVariateGeneration(double average){
        Random rand = new Random();
        return ((-1*average) * (Math.log(1-rand.nextDouble())));
    }

    public synchronized void doStop() {
        this.doStop = true;
    }

    private synchronized boolean keepRunning() {
        return !this.doStop;
    }

    public int getProductsProduced() {
        return productsProduced;
    }
}
