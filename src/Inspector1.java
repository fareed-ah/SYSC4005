import java.util.Observable;
import java.util.Random;

public class Inspector1 extends Observable implements Runnable {

    private final Workstation1 workstation1;
    private final Workstation2 workstation2;
    private final Workstation3 workstation3;
    double inspectionStartTime;
    private double blockedTime;
    private Component component;
    private boolean doStop = false;
    private final double component1Average;
    private int totalInspected  = 0;
    private double totalInspectionTime = 0;

    public Inspector1(Workstation1 workstation1, Workstation2 workstation2, Workstation3 workstation3, double component1Average) {
        this.workstation1 = workstation1;
        this.workstation2 = workstation2;
        this.workstation3 = workstation3;
        this.component1Average = component1Average;
        blockedTime = 0;
        component = null;
    }

    @Override
    public void run() {
        while(keepRunning()) {
            if (component == null) {
               getComponent();
            }

            if (System.currentTimeMillis() - inspectionStartTime >= component.getInspectionTime()) {
                blockedTime += sendComponent();

            }
        }
    }

    private double sendComponent(){
        double blockedTime = 0;

        while(!workstation1.isC1BufferAvailable() && !workstation2.isC1BufferAvailable() && !workstation3.isC1BufferAvailable()){
            if(blockedTime == 0){
                blockedTime = System.currentTimeMillis();
            }
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        if(blockedTime!=0){
            blockedTime = System.currentTimeMillis() - blockedTime;
        }

        String sentTo  = "";

        if(workstation1.isC1BufferAvailable() &&
                (workstation1.getC1Buffer() <= workstation2.getC1Buffer() &&
                        workstation1.getC1Buffer() <= workstation3.getC1Buffer())){
            workstation1.addComponent1();
            sentTo = "Workstation 1";
        }else if(workstation2.isC1BufferAvailable() &&
                (workstation2.getC1Buffer() < workstation1.getC1Buffer() &&
                        workstation2.getC1Buffer() <= workstation3.getC1Buffer())){
            workstation2.addComponent1();
            sentTo = "Workstation 2";
        }else if(workstation3.isC1BufferAvailable() &&
                (workstation3.getC1Buffer() < workstation1.getC1Buffer() &&
                        workstation3.getC1Buffer() < workstation2.getC1Buffer())){
            workstation3.addComponent1();
            sentTo = "Workstation 3";
        }

        totalInspected ++;
        totalInspectionTime += (System.currentTimeMillis() - inspectionStartTime);
        System.out.println("******Inspector 1  inspected component1 ******");
        System.out.println("Expected inspection time: "+ component.getInspectionTime()/1000 + "s");
        System.out.println("Actual inspection time: "+ ((System.currentTimeMillis() - inspectionStartTime)/1000) +"s");
        System.out.println("Blocked time: "+ (blockedTime/1000) +"s");

        System.out.println("Workstation1 Buffer size: " + workstation1.getC1Buffer());
        System.out.println("Workstation2 Buffer size: " + workstation2.getC1Buffer());
        System.out.println("Workstation3 Buffer size: " + workstation3.getC1Buffer());
        System.out.println("Sent component to: "+ sentTo);
        System.out.println();

        component = null;

        return blockedTime;
    }

    private void getComponent(){
        setComponent(new Component(Component.ComponentType.COMPONENT_1, randomVariateGeneration(component1Average)*20));
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

    public void setComponent(Component component) {
        this.component = component;
        inspectionStartTime = System.currentTimeMillis();
    }

    public int getTotalInspected() {
        return totalInspected;
    }

    public double getBlockedTime() {
        return blockedTime /1000;
    }

    public double getTotalInspectionTime() {
        return totalInspectionTime / 1000;
    }
}
