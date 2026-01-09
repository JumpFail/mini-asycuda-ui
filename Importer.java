public class Importer {
    private final int tin;
    private String name;
    private String address;

    public Importer(int tin, String name, String address){
        if (tin <= 0) {
            throw new IllegalArgumentException("TIN must be a positive integer");
        }
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("name is required");
        }
        this.tin = tin;
        this.name = name;
        this.address = address;
    }

    public int getTin() {
        return tin;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("name is required");
        }
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
