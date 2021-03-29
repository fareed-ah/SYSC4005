public class Component {

    public enum ComponentType{
        COMPONENT_1,
        COMPONENT_2,
        COMPONENT_3,
    }

    private ComponentType type;
    private double inspectionTime;

    public Component(ComponentType type, double inspectionTime){
        this.type = type;
        this.inspectionTime = inspectionTime;
    }

    public ComponentType getType() {
        return type;
    }

    public void setType(ComponentType type) {
        this.type = type;
    }

    public double getInspectionTime() {
        return inspectionTime;
    }

    public void setInspectionTime(double inspectionTime) {
        this.inspectionTime = inspectionTime;
    }
}
