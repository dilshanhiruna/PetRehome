package PostAd;

public class AdListingData {
    private String title;
    private String breed;
    private String size;
    private String gender;
    private String age;
    private String description;
    private String email;
    private String phone;
    private String district;
    private String city;

    public  AdListingData(){}

    public AdListingData(String title, String breed, String size, String gender, String age, String description, String email, String phone, String district, String city) {
        this.title = title;
        this.breed = breed;
        this.size = size;
        this.gender = gender;
        this.age = age;
        this.description = description;
        this.email = email;
        this.phone = phone;
        this.district = district;
        this.city = city;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
