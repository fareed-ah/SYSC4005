import java.util.Random;

public class Workstation1 implements Runnable {
    private int c1Buffer;
    private double average;
    private boolean doStop = false;
    private int productsProduced = 0;

    public Workstation1(double average) {
        this.c1Buffer = 0;
        this.average = average;
    }

    private void makeProduct(){
        if(c1Buffer > 0){
            try {
                Thread.sleep((long) randomVariateGeneration(average)*20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            c1Buffer --;
            productsProduced ++;
        }
    }

    public void addComponent1(){
        c1Buffer ++;
    }

    public boolean isC1BufferAvailable(){
        return c1Buffer < 2;
    }

    public int getC1Buffer() {
        return c1Buffer;
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
