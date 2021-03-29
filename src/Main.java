import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Main implements Runnable{

    private double component1Average, component2Average,component3Average, ws1Average,ws2Average, ws3Average;
    private final Workstation1 workstation1;
    private final Workstation2 workstation2;
    private final Workstation3 workstation3;
    private final Inspector1 inspector1;
    private final Inspector2 inspector2;
    private final Thread workstation1Thread,  workstation2Thread, workstation3Thread, inspector1Thread, inspector2Thread;
//    private final Inspector inspector1, inspector2;

    public Main (){
        component1Average = readAverage("servinsp1.dat");
        component2Average = readAverage("servinsp22.dat");
        component3Average = readAverage("servinsp23.dat");
        ws1Average = readAverage("ws1.dat");
        ws2Average = readAverage("ws2.dat");
        ws3Average = readAverage("ws3.dat");


        workstation1 = new Workstation1(ws1Average);
        workstation2 = new Workstation2(ws2Average);
        workstation3 = new Workstation3(ws3Average);

        workstation1Thread = new Thread(workstation1);
        workstation2Thread = new Thread(workstation2);
        workstation3Thread = new Thread(workstation3);

        inspector1 = new Inspector1(workstation1, workstation2,workstation3, component1Average);
        inspector2 = new Inspector2(workstation2,workstation3,component2Average,component3Average);

        inspector1Thread  = new Thread(inspector1);
        inspector2Thread  = new Thread(inspector2);
    }

    public static void main(String[]  args){
       Thread mainThread = new Thread(new Main());
       mainThread.start();
    }

    private double readAverage(String file){
       return readData(new  File(file))
                .stream()
                .mapToDouble(a -> a)
                .average().orElse(0.0);
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
        return data;
    }

    @Override
    public void run() {

        int simulationDuration = 40000;
        workstation1Thread.start();
        workstation2Thread.start();
        workstation3Thread.start();
        inspector1Thread.start();
        inspector2Thread.start();


        try {
            Thread.sleep(simulationDuration);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        inspector1.doStop();
        inspector2.doStop();
        workstation1.doStop();
        workstation2.doStop();
        workstation3.doStop();

        int productsProduced = (workstation1.getProductsProduced()
                + workstation2.getProductsProduced()
                + workstation3.getProductsProduced());

        double totalTime = simulationDuration/1000.0;

        System.out.println("-------Simulation Complete----------");

        System.out.println("Simulation duration: " +  totalTime + "s");
        System.out.println("Inspector 1 Blocked Time: " + inspector1.getBlockedTime() + "s");
        System.out.println("Inspector 2 Blocked Time: " + inspector2.getBlockedTime() +"s");
        System.out.println("Total Inspector 1 components: " + inspector1.getTotalInspected());
        System.out.println("Total Inspector 2 components: " + inspector2.getTotalInspected());
        System.out.println("Total time for Inspector 1 components: " + inspector1.getTotalInspectionTime());
        System.out.println("Total time for Inspector 2 components: " + inspector2.getTotalInspectionTime());
        double lamda1 = ((double) inspector1.getTotalInspected())/simulationDuration;
        double lamda2 = ((double)inspector2.getTotalInspected())/simulationDuration;
        double w1 = inspector1.getTotalInspectionTime()/ inspector1.getTotalInspected();
        double w2 = inspector2.getTotalInspectionTime()/ inspector2.getTotalInspected();
        System.out.println("Average Inspector1 Arrival rate (lamda)item/s: " + lamda1);
        System.out.println("Average Inspector2 Arrival rate (lamda)items/s: " + lamda2);
        System.out.println("Avg system time time for Inspector 1 components(w)s: " + w1);
        System.out.println("Avg system time for Inspector 2 components(w)s: " + w2);
        System.out.println("Inspector 1 (l): " + (lamda1 * w1));
        System.out.println("Inspector 2 (l): " + (lamda2 * w2));

        System.out.println("Workstation 1 Production: " + workstation1.getProductsProduced());
        System.out.println("Workstation 2 Production: " + workstation2.getProductsProduced());
        System.out.println("Workstation 3 Production: " + workstation3.getProductsProduced());

        System.out.println("Workstation 1 Throughput: " + workstation1.getProductsProduced() / totalTime + " products/s");
        System.out.println("Workstation 2 Throughput: " + workstation2.getProductsProduced() / totalTime + " products/s");
        System.out.println("Workstation 3 Throughput: " + workstation3.getProductsProduced() / totalTime + " products/s");

        System.out.println("Total Production: " + productsProduced);

    }
}
