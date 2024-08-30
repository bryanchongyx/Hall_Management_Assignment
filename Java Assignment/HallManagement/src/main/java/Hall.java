public class Hall {
    private String hallName;
    private String hallType;
    private int capacity;
    private double ratePerHour;
    private String availabilityStart;
    private String availabilityEnd;
    private String maintenanceStart;
    private String maintenanceEnd;
    private String remarks;

    public Hall(String hallName, String hallType, int capacity, double ratePerHour) {
        this.hallName = hallName;
        this.hallType = hallType;
        this.capacity = capacity;
        this.ratePerHour = ratePerHour;
        this.availabilityStart = "";
        this.availabilityEnd = "";
        this.maintenanceStart = "";
        this.maintenanceEnd = "";
        this.remarks = "";
    }

    // Getters and setters for all fields
    public String getHallName() {
        return hallName;
    }

    public void setHallName(String hallName) {
        this.hallName = hallName;
    }

    public String getHallType() {
        return hallType;
    }

    public void setHallType(String hallType) {
        this.hallType = hallType;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public double getRatePerHour() {
        return ratePerHour;
    }

    public void setRatePerHour(double ratePerHour) {
        this.ratePerHour = ratePerHour;
    }

    public String getAvailabilityStart() {
        return availabilityStart;
    }

    public void setAvailabilityStart(String availabilityStart) {
        this.availabilityStart = availabilityStart;
    }

    public String getAvailabilityEnd() {
        return availabilityEnd;
    }

    public void setAvailabilityEnd(String availabilityEnd) {
        this.availabilityEnd = availabilityEnd;
    }

    public String getMaintenanceStart() {
        return maintenanceStart;
    }

    public void setMaintenanceStart(String maintenanceStart) {
        this.maintenanceStart = maintenanceStart;
    }

    public String getMaintenanceEnd() {
        return maintenanceEnd;
    }

    public void setMaintenanceEnd(String maintenanceEnd) {
        this.maintenanceEnd = maintenanceEnd;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public void setAvailability(String startDate, String startTime, String endDate, String endTime, String remarks) {
        this.availabilityStart = startDate + " " + startTime;
        this.availabilityEnd = endDate + " " + endTime;
        this.remarks = remarks;
    }

    public void setMaintenance(String startDate, String startTime, String endDate, String endTime, String remarks) {
        this.maintenanceStart = startDate + " " + startTime;
        this.maintenanceEnd = endDate + " " + endTime;
        this.remarks = remarks;
    }
}
