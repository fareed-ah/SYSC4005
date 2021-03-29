import java.util.Arrays;
import java.util.Observable;
import java.util.Random;

public class Inspector2 extends Observable implements Runnable{

   private Workstation2 workstation2;
   private Workstation3 workstation3;
   private double blockedTime, component2Average, component3Average;
   private Component component;
   double inspectionStartTime;
   boolean isBlocked;
   private boolean doStop = false;
   private int totalInspected = 0;
    private double totalInspectionTime = 0;

    public Inspector2(Workstation2 workstation2, Workstation3 workstation3, double component2Average, double component3Average) {
        this.workstation2 = workstation2;
        this.workstation3 = workstation3;
        this.component2Average = component2Average;
        this.component3Average = component3Average;
        blockedTime =0;
        component = null;
        isBlocked = false;
        component = null;
    }

    @Override
    public void run() {

        while(keepRunning()) {
            if (component == null) {
               getComponent();
            }

            if (System.currentTimeMillis() - inspectionStartTime >= component.getInspectionTime()) {
                blockedTime += sendComponent(component.getType());
            }
        }

    }

    private double sendComponent(Component.ComponentType type){
        double blockedTime = 0;
        String sentTo = "";
        switch (type){
            case COMPONENT_2:
                 blockedTime =  workstation2.addComponent2();
                 sentTo = "Workstation 2";
                break;
            case COMPONENT_3:
                blockedTime =   workstation3.addComponent3();
                sentTo = "Workstation 3";
                break;
        }
        totalInspected ++;
        totalInspectionTime += (System.currentTimeMillis() - inspectionStartTime);
        System.out.println("******Inspector 2  inspected " + component.getType()+"******");
        System.out.println("Expected inspection time: "+ component.getInspectionTime()/1000 + "s");
        System.out.println("Actual inspection time: "+ ((System.currentTimeMillis() - inspectionStartTime)/1000) +"s");
        System.out.println("Blocked time: "+ (blockedTime/1000) +"s");

        System.out.println("Workstation2 Buffer size: " + workstation2.getC2Buffer());
        System.out.println("Workstation3 Buffer size: " + workstation3.getC3Buffer());
        System.out.println("Sent component to: "+ sentTo);
        System.out.println();
        component = null;
        return blockedTime;
    }

    private void getComponent(){
        Random rand = new Random();
        Component.ComponentType type = Arrays.asList(Component.ComponentType.COMPONENT_2, Component.ComponentType.COMPONENT_3).get(rand.nextInt(2));
        setComponent(new Component(type, randomVariateGeneration(type == Component.ComponentType.COMPONENT_2? component2Average: component3Average)*20));
    }

    private double randomVariateGeneration(double average){
        Random rand = new Random();
        return ((-1*average) * (Math.log(1-rand.nextDouble())));
    }

    public void setComponent(Component component) {
        this.component = component;
        inspectionStartTime = System.currentTimeMillis();
    }

    public synchronized void doStop() {
        this.doStop = true;
    }

    private synchronized boolean keepRunning() {
        return !this.doStop;
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
