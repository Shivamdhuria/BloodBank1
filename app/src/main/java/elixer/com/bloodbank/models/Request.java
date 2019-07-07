package elixer.com.bloodbank.models;

public class Request {


    private String name;
    private String bloodRequired;
    private long epochTime;
    private String placeOfRequest;
    private Boolean status;

    public Request() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public Request(String name, String bloodRequired, long epochTime, String placeOfRequest, Boolean status) {
        this.name = name;
        this.bloodRequired = bloodRequired;
        this.epochTime = epochTime;
        this.placeOfRequest = placeOfRequest;
        this.status = status;


    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getName() {
        return name;

    }

    public void setName(String name) {
        this.name = name;
    }


    public String getBloodRequired() {
        return bloodRequired;
    }

    public void setBloodRequired(String bloodRequired) {
        this.bloodRequired = bloodRequired;
    }

    public long getEpochTime() {
        return epochTime;
    }

    public void setEpochTime(long epochTime) {
        this.epochTime = epochTime;
    }

    public String getPlaceOfRequest() {
        return placeOfRequest;
    }

    public void setPlaceOfRequest(String placeOfRequest) {
        this.placeOfRequest = placeOfRequest;
    }

    @Override
    public String toString() {
        return "Request{" +
                "name='" + name + '\'' +
                ", bloodRequired='" + bloodRequired + '\'' +
                ", epochTime=" + epochTime +
                ", placeOfRequest='" + placeOfRequest + '\'' +
                ", status=" + status +
                '}';
    }
}
