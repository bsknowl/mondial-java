/**
 * Created by Scotty Waggoner on 2/25/14.
 */
public class CityProvinceCountry {
    private final City city;
    private final Province province;
    private final Country country;

    CityProvinceCountry(City city, Province province, Country country){
        this.city = city;
        this.province = province;
        this.country = country;
    }

    public String getCity() {
        if(city == null){
            return null;
        }
        return city.getName();
    }

    public String getProvince() {
        if(province == null){
            return null;
        }
        return province.getName();
    }

    public String getCountry() {
        if(country == null){
            return null;
        }
        return country.getName();
    }
    public String getCountryCode() {
        if(country == null){
            return null;
        }
        return country.getCode();
    }
}
