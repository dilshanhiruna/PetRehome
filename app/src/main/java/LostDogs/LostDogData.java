package LostDogs;

public class LostDogData {
    //Dog properties
    private String dogname;
    private String dogage;
    private String doggender;
    private String dogsize;
    private String dogbreed;
    private String doglostdate;

    //Owner properties
    private String ownername;
    private String ownercontact;
    private String owneremail;
    private String ownerlocation;
    private String description;

    public LostDogData() {
    }

    public LostDogData(String dogname, String dogage, String doggender, String dogsize, String dogbreed, String doglostdate, String ownername, String ownercontact, String owneremail, String ownerlocation, String description) {
        this.dogname = dogname;
        this.dogage = dogage;
        this.doggender = doggender;
        this.dogsize = dogsize;
        this.dogbreed = dogbreed;
        this.doglostdate = doglostdate;
        this.ownername = ownername;
        this.ownercontact = ownercontact;
        this.owneremail = owneremail;
        this.ownerlocation = ownerlocation;
        this.description = description;
    }

    public String getDogname() {
        return dogname;
    }

    public void setDogname(String dogname) {
        this.dogname = dogname;
    }

    public String getDogage() {
        return dogage;
    }

    public void setDogage(String dogage) {
        this.dogage = dogage;
    }

    public String getDoggender() {
        return doggender;
    }

    public void setDoggender(String doggender) {
        this.doggender = doggender;
    }

    public String getDogsize() {
        return dogsize;
    }

    public void setDogsize(String dogsize) {
        this.dogsize = dogsize;
    }

    public String getDogbreed() {
        return dogbreed;
    }

    public void setDogbreed(String dogbreed) {
        this.dogbreed = dogbreed;
    }

    public String getDoglostdate() {
        return doglostdate;
    }

    public void setDoglostdate(String doglostdate) {
        this.doglostdate = doglostdate;
    }

    public String getOwnername() {
        return ownername;
    }

    public void setOwnername(String ownername) {
        this.ownername = ownername;
    }

    public String getOwnercontact() {
        return ownercontact;
    }

    public void setOwnercontact(String ownercontact) {
        this.ownercontact = ownercontact;
    }

    public String getOwneremail() {
        return owneremail;
    }

    public void setOwneremail(String owneremail) {
        this.owneremail = owneremail;
    }

    public String getOwnerlocation() {
        return ownerlocation;
    }

    public void setOwnerlocation(String ownerlocation) {
        this.ownerlocation = ownerlocation;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
