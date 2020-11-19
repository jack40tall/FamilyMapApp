package load_data;

public class Location {
    String latitude;
    String longitude;
    String city;
    String country;

    public float getLatitude() {
        float l = Float.parseFloat(latitude);
        return l;
    }

    public float getLongitude() {
        float l = Float.parseFloat(longitude);
        return l;
    }

    public String getCity() {
        return city;
    }

    public String getCountry() {
        return country;
    }
}
