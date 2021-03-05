import com.sun.corba.se.spi.orbutil.threadpool.Work;

import java.util.*;

public class Inspector implements Runnable {
    private HashMap<ComponentType,List<Double>> componentTypes;
    private HashMap<ComponentType, List<Workstation>> workstationList;
    private boolean doStop = false;
    private double blockedTime = 0;

    public Inspector(HashMap<ComponentType,List<Double>> componentTypes, HashMap<ComponentType, List<Workstation>> workstationList) {
        this.componentTypes = componentTypes;
        this.workstationList = workstationList;
    }

    @Override
    public void run() {
        while(!componentTypes.isEmpty() && keepRunning()) {
            ComponentType component = getRandomComponent();

            try {
                Thread.sleep((long) (componentTypes.get(component).remove(0) * 1L)); //* 1000L));

                Workstation workstation = findBuffer(component);
                if(workstation == null) {
                    long blockedStartTime = System.currentTimeMillis();
                    while (workstation == null) {
                        //blocked
                        workstation = findBuffer(component);
                    }
                    long blockedEndTime = System.currentTimeMillis();
                    blockedTime += (blockedEndTime - blockedStartTime);
                }

                workstation.put(component);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if(componentTypes.get(component).isEmpty()){
                componentTypes.remove(component);
            }
        }
    }

    private Workstation findBuffer(ComponentType componentType){
        Workstation workstationBuffer = null;
        List<Workstation> workstationList = this.workstationList.get(componentType);

        for (Workstation workstation: workstationList) {
            if((workstationBuffer == null && workstation.getBufferMap().get(componentType) <2) || (workstationBuffer != null &&  workstation.getBufferMap().get(componentType) < workstationBuffer.getBufferMap().get(componentType))){
                workstationBuffer = workstation;
            }
        }
        return workstationBuffer;
    }

    public double getBlockedTime() {
        return blockedTime;
    }

    public synchronized void doStop() {
        this.doStop = true;
    }

    private synchronized boolean keepRunning() {
        return !this.doStop;
    }

    private ComponentType getRandomComponent(){
        Random rand = new Random();
        return (ComponentType) componentTypes.keySet().toArray()[rand.nextInt(componentTypes.keySet().size())];
    }
}

