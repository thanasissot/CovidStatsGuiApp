package covid.service;

import covid.controller.RequestsController;
import covid.model.Coviddata;
import covid.model.Country;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class DataMap {
    
    // οι 2 μεθοδοι λαμβανουν μια παραμετρο τυπου String, την επιλογη του χρηστη για το ειδος δεδομενων
    // μεσω του Requests Controller γινεται η κληση στο Rest Api και επειτα χειριζεται το String
    // που επιστρεφεται στο Response σαν json.
    
    // κανει extract απο το json ολες τις 3 χωρες που μας ενδιαφερουν απο τα δεδομενα (εαν υπαρχουν)
    // και τις αποθηκευει σε ενα HashMap με <Ονομα Χωρας, Country>
    // σε μορφη τετοια ωστε επειτα να χρησιμοποιηθει απο τον κωδικα που καλειται για την απευθειας
    // αποθηκευση των χωρων στη ΒΔ
    public static HashMap<String, Country> countriesFromJson(String dataType) throws Exception {

        HashMap<String, Country> countryList = new HashMap<>();
        // βοηθητικες μεταβλητες
        JsonObject innerObj;
        String countryName;
        String lat;
        String long1;
        Country country;

        // δημιουργια κλησης και επιστροφη δεδομενων σε μορφη string
        String responseData = RequestsController.getApiData(dataType);

        // δημιουργια του JSON αρχειου απο το response String του API
        JsonObject json = new JsonParser().parse(new RequestsController().getApiData(dataType)).getAsJsonObject();

        // μορφοποιηση σε καταλληλη μορφη
        JsonArray dataTypeArray = json.get(dataType).getAsJsonArray();

        for (JsonElement inner : dataTypeArray) {

            // για να χειριστουμε καταλληλα σε καθε loop το JsonElement
            innerObj = inner.getAsJsonObject();

            //  παιρνουμε το ονομα της χωρας, βαση της αλλαγης των απαιτησεων θα κρατησουμε μονο
            //  3 χωρες. Greece, Germany, Italy
            countryName = innerObj.get("Country/Region").toString();

            // αφαιρεση των "" απο το string "χωρα"
            countryName = countryName.substring(1, countryName.length() - 1);

            if (!countryName.equals("Greece") && !countryName.equals("Germany") && !countryName.equals("Italy")) {
                continue;
            }
            
            // παιρνουμε τα lat και long για καθε χωρα
            lat = innerObj.get("Lat").toString();
            long1 = innerObj.get("Long").toString();

            // δημιουργια της Country και set των παραμετρων της
            country = new Country();
            country.setName(countryName);
            country.setLat(Double.parseDouble(lat));
            country.setLong1(Double.parseDouble(long1));
            
            // αποθηκευση
            countryList.put(countryName, country);

        }

        return countryList;
    }

    public static HashMap<String, ArrayList<Coviddata>> coviddataFromJson(String dataType) throws Exception {

        // βοηθητικες μεταβλητες
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d/yy", Locale.ENGLISH);
        short datakind;
        Coviddata coviddata;
        String countryName;
        String trndate;
        // στο diff αποθηκευεται η ποσοτητα της προηγουμενης Date έτσι ωστε για καθε Date 
        // να παιρνουμε Today - Yesterday = Ημερησιο Ποσο
        int diff;
        int qty;
        int proodqty;
        JsonObject innerObj;

        // ευρεση datakind για την αποθηκευση στη βαση δεδομενων
        switch (dataType) {

            case "deaths":
                datakind = 1;
                break;
            case "recovered":
                datakind = 2;
                break;
            case "confirmed":
                datakind = 3;
                break;
            default:
                datakind = -1;
                break;
        }

        HashMap<String, ArrayList<Coviddata>> countryAndCoviddataList = new HashMap<>();
        ArrayList<Coviddata> coviddataList;

        // δημιουργια του JSON αρχειου απο το response String του API
        JsonObject json = new JsonParser().parse(new RequestsController().getApiData(dataType)).getAsJsonObject();

        JsonArray dataTypeArray = json.get(dataType).getAsJsonArray();

        for (JsonElement inner : dataTypeArray) {
            // αρχικοποιηση/ρεσετ μεταβλητων int
            diff = 0;
            // αρχικοποιηση ArrayList
            coviddataList = new ArrayList<>();
            // για να χειριστουμε καταλληλα σε καθε loop το JsonElement
            innerObj = inner.getAsJsonObject();

            //  παιρνουμε το ονομα της χωρας, βαση της αλλαγης των απαιτησεων θα κρατησουμε μονο
            //  3 χωρες. Greece, Germany, Italy
            countryName = innerObj.get("Country/Region").toString();

            // αφαιρεση των "" απο το string "χωρα"
            countryName = countryName.substring(1, countryName.length() - 1);

            if (!countryName.equals("Greece") && !countryName.equals("Germany") && !countryName.equals("Italy")) {
                continue;
            }

            // αφαιρουμε τα παρακατω properties απο το JSON object, μας χρησιμευει στη διαδικασια του 
            // loop των υπολοιπων, διοτι παραμενουν μονο οι ημερομηνιες
            innerObj.remove("Province/State");
            innerObj.remove("Country/Region");
            innerObj.remove("Lat");
            innerObj.remove("Long");

            for (Map.Entry<String, JsonElement> entry : innerObj.entrySet()) {
                // δημιουργια και αποθηκευση coviddata

                // αποθηκευση Dates Qty στο Map 
                coviddata = new Coviddata();
                proodqty = entry.getValue().getAsInt();
                qty = proodqty - diff;
                diff = proodqty;
                trndate = entry.getKey();

                coviddata.setDatakind(datakind);
                coviddata.setProodqty(proodqty);
                // υπαρχουν καποιες "αρνητικες τιμες" σε καποιες ημερομηνιες. εξασφαλιζουμε να μη συνεχισουμε το λαθος στις επομενες
                if (qty >= 0) {
                    coviddata.setQty(qty);
                }
                coviddata.setTrndate(LocalDate.parse(trndate, formatter).toString());

                coviddataList.add(coviddata);

            }

            // αποθηκευση ολων των coviddata με συσχετιση με το συγκεκριμενο country
            countryAndCoviddataList.put(countryName, coviddataList);

        }

        return countryAndCoviddataList;

    }

}
