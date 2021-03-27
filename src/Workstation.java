import java.util.HashMap;
import java.util.List;

public class Workstation implements Runnable{

    private HashMap<ComponentType,Integer> bufferMap = new HashMap<>();
    private int productsProduced = 0;
    private ProductType productType;
    private final List<Double> productionTimes;
    private boolean doStop = false;

    public Workstation(ProductType productType, List<Double> productionTimes) {
        this.productType = productType;
        this.productionTimes  = productionTimes;
        setBufferMap(this.productType);
    }

    public HashMap<ComponentType, Integer> getBufferMap() {
        return bufferMap;
    }

    private void setBufferMap(ProductType productType){
        switch (productType){
            case PRODUCT_1:
                this.bufferMap = new HashMap<>();
                bufferMap.put(ComponentType.COMPONENT_1,0);
                break;
            case PRODUCT_2:
                bufferMap.put(ComponentType.COMPONENT_1,0);
                bufferMap.put(ComponentType.COMPONENT_2,0);
                break;
            case PRODUCT_3:
                bufferMap.put(ComponentType.COMPONENT_1,0);
                bufferMap.put(ComponentType.COMPONENT_3,0);
                break;
        }
    }

    public synchronized void put(ComponentType componentType){
        while (bufferMap.get(componentType) == 2) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        bufferMap.put(componentType,bufferMap.get(componentType) +1);
        notifyAll();
    }

    @Override
    public void run() {
        while(!productionTimes.isEmpty() && keepRunning()){
            if(canMakeProduct()){
              makeProduct();
            }
        }
    }

    private boolean canMakeProduct(){
        boolean makeProduct = true;
        for (ComponentType component: bufferMap.keySet()) {
            if(bufferMap.get(component) ==0){
                makeProduct = false;
            }
        }
        return makeProduct;
    }

    private void makeProduct(){
        try {
            Thread.sleep((long) (productionTimes.remove(0) * 20L));
            //  System.out.println("Produced: " + productType.name());
            productsProduced ++;
            for (ComponentType component: bufferMap.keySet()) {
                bufferMap.put(component,bufferMap.get(component)-1) ;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
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
