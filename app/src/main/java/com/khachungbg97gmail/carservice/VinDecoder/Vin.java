package com.khachungbg97gmail.carservice.VinDecoder;

/**
 * Created by ASUS on 10/14/2018.
 */

public class Vin {
    private final String value;
    private Country country=Country.getDefault();
    public Vin(String vin){
        value=vin.toUpperCase();
        if(!isValidSize()){
            throw new IllegalArgumentException("Invalid size");
        }
        assertValidCharacter();
        // Validate checksum.
        char weightDigit=getWeightDigit();
        if (vin.charAt(8) != weightDigit) {
            throw new IllegalArgumentException("Invalid checksum");
        }

    }
    /**
     * @return Checksum digit (Position 9 in VIN).
     */
    public char getWeightDigit(){
        int prodSum=0;
        for(int i=0;i<value.length();i++){
            String letter= value.substring(i,i+1);
            int factor=VinConstants.WEIGHT_FACTOR[i];
            int weight=VinConstants.WEIGHTS.get(letter);
            prodSum+=factor*weight;
        }
        if(prodSum%11==10){
            return 'X';
        }else {
            return (char) ('0'+prodSum%11);
        }

    }
    /**
     * @return Whether production line is less than 500 unit per year.
     */
    public boolean isLess500(){
        char ch=value.charAt(2);
        return ch=='9';
    }
    public String getValue(){
        return value;
    }
    private boolean isValidSize(){
        return value.length()==17;
    }
    private void assertValidCharacter(){
        for(int i=0;i<value.length();i++){
            String str=value.substring(i,i+1);
            if(VinConstants.ALPHABET_INDEX.get(str)==null){
                throw new IllegalArgumentException("Character at index"+i+"is invalid");

            }

        }

    }
    public Country getCountry(){
        return  country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }
    public String getManufacturerCode(){
        return isLess500()? value.substring(11,13):value.substring(1,3);
    }
    public String getPlantCode(){
        return value.substring(10,11);
    }
    public String getWMICode(){
        return value.substring(0,3);

    }
    public String getCountryCode() {
        return value.substring(0, 2);
    }
    public String getCountryName() {
        return country.get(getCountryCode());
    }
    public  String getVdsCode(){
        return value.substring(3,8);

    }
    public String getSerialNumber(){
        return isLess500()? value.substring(14,17):value.substring(11,17);
    }
    public int getYear() {
        String yearModelCode = value.substring(9, 10);
        int yearModelVal = VinConstants.YEAR_INDEX.get(yearModelCode);
        yearModelVal = 1980 + (yearModelVal % 30);
        return yearModelVal;
    }
}
