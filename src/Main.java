import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Main implements Runnable{

    private final Workstation workstation1, workstation2, workstation3;
    private final Thread workstation1Thread,  workstation2Thread, workstation3Thread, inspector1Thread, inspector2Thread;
    private final Inspector inspector1, inspector2;

    public Main (){
         workstation1 = new Workstation(ProductType.PRODUCT_1,readData(new  File("ws1.dat")));
         workstation2 = new Workstation(ProductType.PRODUCT_2,readData(new  File("ws2.dat")));
         workstation3 = new Workstation(ProductType.PRODUCT_3,readData(new  File("ws3.dat")));

         workstation1Thread = new Thread(workstation1);
         workstation2Thread = new Thread(workstation2);
         workstation3Thread = new Thread(workstation3);

        HashMap<ComponentType, List<Double>> inspector1Data = new HashMap<>();
        inspector1Data.put(ComponentType.COMPONENT_1, readData(new  File("servinsp1.dat")));

        HashMap<ComponentType, List<Workstation>> inspector1Workstation = new HashMap<>();
        inspector1Workstation.put(ComponentType.COMPONENT_1, Arrays.asList(workstation1, workstation2, workstation3));
        inspector1 = new Inspector(inspector1Data, inspector1Workstation);
        inspector1Thread  = new Thread(inspector1);

        HashMap<ComponentType, List<Workstation>> inspector2Workstation = new HashMap<>();
        inspector2Workstation.put(ComponentType.COMPONENT_2, Collections.singletonList(workstation2));
        inspector2Workstation.put(ComponentType.COMPONENT_3, Collections.singletonList(workstation3));
        HashMap<ComponentType, List<Double>> inspector2Data = new HashMap<>();
        inspector2Data.put(ComponentType.COMPONENT_2, readData(new  File("servinsp22.dat")));
        inspector2Data.put(ComponentType.COMPONENT_3, readData(new  File("servinsp23.dat")));
        inspector2 = new Inspector(inspector2Data, inspector2Workstation);
        inspector2Thread = new Thread(inspector2);
    }
    public static void main(String[]  args){
       Thread mainThread = new Thread(new Main());
       mainThread.start();
    }

    private static List<Double> randomVariateGeneration(List<Double> input){
        Random rand = new Random();
        List<Double> rng = new ArrayList<>();
        double average = input
                .stream()
                .mapToDouble(a -> a)
                .average().orElse(0.0);
        for(int i= 0; i< 300; i++){
            rng.add((-1*average) * (Math.log(1-rand.nextDouble())));
        }
        return rng;
    }

    private static List<Double> readData(File file){
        Scanner scnr = null;
        List<Double> data = new ArrayList<>();

        try {
            scnr = new Scanner(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        while(scnr.hasNextDouble()){
            Double time = scnr.nextDouble();
            data.add(time);
        }
        return randomVariateGeneration(data);
    }

    @Override
    public void run() {
        workstation1Thread.start();
        workstation2Thread.start();
        workstation3Thread.start();
        inspector1Thread.start();
        inspector2Thread.start();

        long startTime = System.currentTimeMillis();
        while(inspector1Thread.isAlive() && inspector2Thread.isAlive()){
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        inspector1.doStop();
        inspector2.doStop();
        workstation1.doStop();
        workstation2.doStop();
        workstation3.doStop();

        long endTime = System.currentTimeMillis();

        System.out.println("-------Simulation Complete----------");
        System.out.println("Simulation duration: " + (endTime - startTime));
        System.out.println("Inspector 1 Blocked Time: " + inspector1.getBlockedTime());
        System.out.println("Inspector 2 Blocked Time: " + inspector2.getBlockedTime());
        System.out.println("Workstation 1 Production: " + workstation1.getProductsProduced());
        System.out.println("Workstation 2 Production: " + workstation2.getProductsProduced());
        System.out.println("Workstation 3 Production: " + workstation3.getProductsProduced());
        System.out.println("Total Production: " + (workstation1.getProductsProduced()
                + workstation2.getProductsProduced()
                + workstation3.getProductsProduced()));
    }
}
