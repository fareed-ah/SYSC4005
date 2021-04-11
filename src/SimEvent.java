import java.util.List;

public class SimEvent implements Comparable<SimEvent>{

    public static enum eventType {INSPECT_COMPONENT_1,INSPECT_COMPONENT_2,INSPECT_COMPONENT_3,PRODUCE_P1,PRODUCE_P2,PRODUCE_P3, END_OF_SIM};    // ALQ=Arrival at Loader Queue, EL=End of Loading, EW=End of Weighing, ES=End of Simulation
    private eventType eType;        // Type of the event
    private Double eTime;          // Event Time
    private Component component;
    private Component component2;

    public SimEvent(eventType eType, double eTime) {
        this.eType = eType;
        this.eTime = eTime;
    }

    @Override
    public int compareTo(SimEvent ev) {
        return this.geteTime().compareTo(ev.geteTime());
    }

    public eventType geteType() {
        return eType;
    }

    public void seteType(eventType eType) {
        this.eType = eType;
    }

    public Double geteTime() {
        return eTime;
    }

    public void seteTime(double eTime) {
        this.eTime = eTime;
    }

    public Component getComponent() {
        return component;
    }

    public void setComponent(Component component) {
        this.component = component;
    }

    public void seteTime(Double eTime) {
        this.eTime = eTime;
    }

    public Component getComponent2() {
        return component2;
    }

    public void setComponent2(Component component2) {
        this.component2 = component2;
    }
}