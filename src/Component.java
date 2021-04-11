public class Component {

    private double workstationBufferArrivalTime = 0;
    private double workstationBufferDepartureTime = 0;
    private double totalWaitTime = 0;
    public Component(double workstationBufferArrivalTime){
        this.workstationBufferArrivalTime = workstationBufferArrivalTime;
    }

    public double getTotalWaitTime() {
        return totalWaitTime;
    }

    public void setTotalWaitTime(double totalWaitTime) {
        this.totalWaitTime = totalWaitTime;
    }

    public void incTotalWaitTime(double totalWaitTime) {
        this.totalWaitTime += totalWaitTime;
    }

    public double getWorkstationBufferArrivalTime() {
        return workstationBufferArrivalTime;
    }

    public void setWorkstationBufferArrivalTime(double workstationArrivalTime) {
        this.workstationBufferArrivalTime = workstationArrivalTime;
    }

    public double getWorkstationBufferDepartureTime() {
        return workstationBufferDepartureTime;
    }

    public void setWorkstationBufferDepartureTime(double workstationBufferDepartureTime) {
        this.workstationBufferDepartureTime = workstationBufferDepartureTime;
    }
}
