package covid.service;

import java.awt.Desktop;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

public class Mappage  {
    
    // μια μεθοδο, χρησιμοποιειται απο τα Jframe02, Jframe03 για τη δημιουργια και ανοιγμα μιας
    // ιστοσελιδας στον αντιστοιχο browser η οποια εμφανιζει τα δεδομενα περι covid
    public static void loadGoogleMap(String[][] locations, String lat, String long1) throws IOException {
        
        // ανοιγμα ενος Writer για την εγγραφη του αρχειου
        FileWriter writer = new FileWriter("mappage.html");
        
        // εχουμε ετοιμο το κωδικα απο την εκφωνηση και απλα συμπλρωνουμε στην απαραιτητη θεση
        // τις συντεταγμενες της κυριας χωρας, δηλαδη που θα εστιασει ο χαρτης
        // και επειτα ολες τις πληροφοριες που θελουμε, δηλαδη για ολες τις χωρες
        // που θελουμε να εμφανιστει ενα marker και τι θα αναγραφεται στο pop up windows
        // του marker.
        String mappageHTML = "<!DOCTYPE html>\n"
                    + "<html>\n"
                    + "<head>\n"
                    + "<meta http-equiv=\"content-type\" content=\"text/html; charset=UTF-8\" />\n"
                    + "<title>Google Maps Multiple Markers</title>\n"
                    + "<script src=\"http://maps.google.com/maps/api/js?sensor=false\"\n"
                    + "type=\"text/javascript\"></script>\n"
                    + "</head>\n"
                    + "<body>\n"
                    + "<div id=\"map\" style=\"width: 1000px; height: 700px;\"></div>\n"
                    + "<script type=\"text/javascript\">\n"
                    + "var locations = " + Arrays.deepToString(locations) + ";\n"
                    + "var map = new google.maps.Map(document.getElementById('map'), {\n"
                    + "zoom : 5,\n"
                    + "center : new google.maps.LatLng(" + lat + ", " + long1 + "),\n"
                    + "mapTypeId: google.maps.MapTypeId.ROADMAP\n"
                    + "});\n"
                    + "var infowindow = new google.maps.InfoWindow();\n"
                    + "var marker, i;\n"
                    + "for (i = 0; i < locations.length; i++) {\n"
                    + "marker = new google.maps.Marker({\n"
                    + "position: new google.maps.LatLng(locations[i][1], locations[i][2]),\n"
                    + "map: map\n"
                    + "});\n"
                    + "google.maps.event.addListener(marker, 'click', (function(marker, i) {\n"
                    + "return function() {\n"
                    + "infowindow.setContent(locations[i][0]);\n"
                    + "infowindow.open(map, marker);\n"
                    + "}\n"
                    + "})(marker, i));\n"
                    + "}\n"
                    + "</script>\n"
                    + "</body>\n"
                    + "</html>";
        
        // εγγραφη στο αρχειο
        writer.write(mappageHTML);
        
        // κλεισιμο του writer
        writer.close();
        
        File htmlFile = new File("mappage.html");
        // εκτελεση του αρχειου html
        Desktop.getDesktop().browse(htmlFile.toURI());
    }
}
