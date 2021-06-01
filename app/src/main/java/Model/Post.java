package Model;

public class Post {
    private String petName;
    private String petModel;
    private String description;
    private String phone;
    private int id;
    static int counter = 0;

    public Post(String petName, String petModel, String description,String phone) {
        this.petName = petName;
        this.petModel = petModel;
        this.description = description;
        this.phone = phone;
        this.id = counter++;
    }

    public String getId() {
        return String.valueOf(id);
    }

    public String getPhone() {
        return phone;
    }

    public void setPetName(String petName) {
        this.petName = petName;
    }

    public void setPetModel(String petModel) {
        this.petModel = petModel;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPetName() {
        return petName;
    }

    public String getPetModel() {
        return petModel;
    }

    public String getDescription() {
        return description;
    }
}
