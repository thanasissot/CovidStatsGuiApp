package covid.controller;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

public class RequestsController {
    
    // κωδικας που μας δοθηκε ετοιμος σαν προταση χρησης απο την εκφωνηση της εργασιας
    // χωριστηκε σε κομματια για την βελτιστοποιηση της αναγνωσιμοτητας
    // και την χρηση μιας τελικης μεθοδου της getApiData που περιειχε ολη τη λογικη
    // που θελαμε
    private static OkHttpClient client = new OkHttpClient();

    private static Request request(String urlToCall) {
        return new Request.Builder().url(urlToCall).build();
    }

    private static String response(String urlToCall) {
        try (Response response = client.newCall(request(urlToCall)).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                String responseString = response.body().string();
                return responseString;
            }
        } catch (IOException e) {
            System.out.println("Αδυναμια συνδεσης με το διαδικτυο ή μη-ανταπόκριση του API");
            e.printStackTrace();
            return null;
        }
        return "";
    }
    
    
    // η μεθοδος αυτη λαμβανει σαν παραμετρο ενα String το οποιο περιεχει το ειδος δεδομενων που
    // επιθυμει ο χρηστης να ανακτησει απο το API, και χειριζεται την υπολοιπη διαδικασια μονο του
    // επιστρεφει ενα String το response body
    public static String getApiData(String dataType) throws Exception {
        if (!(dataType.equals("deaths") || dataType.equals("confirmed") || dataType.equals("recovered"))) {
            throw new Exception("dataType needs to be \"deaths\",\"confirmed\" or \"recovered\"");
        }

        String urlToCall = "https://covid2019-api.herokuapp.com/timeseries/" + dataType;

        String response = RequestsController.response(urlToCall);

        return response;
    }
}
