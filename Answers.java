package StackOverflow;

import com.google.gson.Gson;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPInputStream;

public class Answers {

    private static String readUrl(String query) throws IOException {

        URL url = new URL(query);
        BufferedReader in = new BufferedReader(new InputStreamReader(new GZIPInputStream(url.openStream())));
        String line;
        StringBuffer content = new StringBuffer();
        while ((line = in.readLine()) != null) {
            content.append(line);
        }
        return String.valueOf(content);
    }


    class Page {
        List<Item> items;
    }

    static class Item{
        class Owner{
            String user_id;
        }
        Owner owner;
        String is_accepted;
        String score;
        String answer_id;
        String question_id;
    }

    class Users {
        List<User> items;
    }
    static class User{
        String location;
    }

    public static void main(String[] args) throws IOException {


        List<String> statesList = new ArrayList<String>();                      //Create a list of USA states
        String[] states = { "AK","AL","AR","AZ","CA","CO","CT","DC",
                "DE","FL","GA","GU","HI","IA","ID", "IL","IN","KS",
                "KY","LA","MA","MD","ME","MH","MI","MN","MO","MS","MT",
                "NC","ND","NE","NH","NJ","NM","NV","NY", "OH","OK","OR",
                "PA","PR","PW","RI","SC","SD","TN","TX","UT","VA","VI",
                "VT","WA","WI","WV","WY", "Massachusetts", "Seattle", "California", "Hampshire", "USA" };
        for(int i =  0; i < states.length; i++){
            statesList.add(states[i]);
        }

        List<String> ignoreList = new ArrayList<String>();          // list of undesirable outputs
        String[] toIgnore = { "Virtual", "git.ninja", "北京","PH", "Earth", "Europe", "moon", "Everywhere", "cloud&quot;", "T&#252;rkiy", "T&#252;rkiye", "&#39;Straya", "Kerala, India", "Project" };
        for(int i =  0; i < toIgnore.length; i++){
            ignoreList.add(toIgnore[i]);
        }


        List<String> countriesList = new ArrayList<String>();                      //Create a list of countries
        String[] countries = { "Afghanistan", "Albania", "Algeria", "American Samoa", "Andorra", "Angola", "Anguilla", "Antarctica",
                "Antigua and Barbuda", "Argentina", "Armenia", "Aruba", "Australia", "Austria", "Azerbaijan", "Bahamas", "Bahrain", "Bangladesh",
                "Barbados", "Belarus", "Belgium", "Belize", "Benin", "Bermuda", "Bhutan", "Bolivia", "Bosnia and Herzegowina", "Botswana", "Bouvet Island",
                "Brazil", "British Indian Ocean Territory", "Brunei Darussalam", "Bulgaria", "Burkina Faso", "Burundi", "Cambodia", "Cameroon", "Canada",
                "Cape Verde", "Cayman Islands", "Central African Republic", "Chad", "Chile", "China", "Christmas Island", "Cocos (Keeling) Islands", "Colombia",
                "Comoros", "Congo", "Congo, the Democratic Republic of the", "Cook Islands", "Costa Rica", "Cote d'Ivoire", "Croatia (Hrvatska)", "Cuba",
                "Cyprus", "Czech Republic", "Denmark", "Djibouti", "Dominica", "Dominican Republic", "East Timor", "Ecuador", "Egypt", "El Salvador",
                "Equatorial Guinea", "Eritrea", "Estonia", "Ethiopia", "Falkland Islands (Malvinas)", "Faroe Islands", "Fiji", "Finland", "France", "France Metropolitan",
                "French Guiana", "French Polynesia", "French Southern Territories", "Gabon", "Gambia", "Georgia", "Germany", "Ghana", "Gibraltar", "Greece", "Greenland",
                "Grenada", "Guadeloupe", "Guam", "Guatemala", "Guinea", "Guinea-Bissau", "Guyana", "Haiti", "Heard and Mc Donald Islands", "Holy See (Vatican City State)",
                "Honduras", "Hong Kong", "Hungary", "Iceland", "India", "Indonesia", "Iran (Islamic Republic of)", "Iraq", "Ireland", "Israel", "Italy", "Jamaica", "Japan",
                "Jordan", "Kazakhstan", "Kenya", "Kiribati", "Korea, Democratic People's Republic of", "Korea, Republic of", "Kuwait", "Kyrgyzstan", "Lao, People's Democratic Republic",
                "Latvia", "Lebanon", "Lesotho", "Liberia", "Libyan Arab Jamahiriya", "Liechtenstein", "Lithuania", "Luxembourg", "Macau", "Macedonia, The Former Yugoslav Republic of",
                "Madagascar", "Malawi", "Malaysia", "Maldives", "Mali", "Malta", "Marshall Islands", "Martinique", "Mauritania", "Mauritius", "Mayotte", "Mexico", "Micronesia, " +
                "Federated States of", "Moldova, Republic of", "Monaco", "Mongolia", "Montserrat", "Morocco", "Mozambique", "Myanmar", "Namibia", "Nauru", "Nepal", "Netherlands",
                "Netherlands Antilles", "New Caledonia", "New Zealand", "Nicaragua", "Niger", "Nigeria", "Niue", "Norfolk Island", "Northern Mariana Islands", "Norway", "Oman",
                "Pakistan", "Palau", "Panama", "Papua New Guinea", "Paraguay", "Peru", "Philippines", "Pitcairn", "Poland", "Portugal", "Puerto Rico", "Qatar", "Reunion", "Romania",
                "Russian Federation", "Rwanda", "Saint Kitts and Nevis", "Saint Lucia", "Saint Vincent and the Grenadines", "Samoa", "San Marino", "Sao Tome and Principe", "Saudi Arabia",
                "Senegal", "Seychelles", "Sierra Leone", "Singapore", "Slovakia (Slovak Republic)", "Slovenia", "Solomon Islands", "Somalia", "South Africa", "South Georgia and the South Sandwich Islands", "Spain", "Sri Lanka", "St. Helena", "St. Pierre and Miquelon", "Sudan", "Suriname", "Svalbard and Jan Mayen Islands", "Swaziland", "Sweden", "Switzerland", "Syrian Arab Republic", "Taiwan, Province of China", "Tajikistan", "Tanzania, United Republic of", "Thailand", "Togo", "Tokelau", "Tonga", "Trinidad and Tobago", "Tunisia", "Turkey", "Turkmenistan", "Turks and Caicos Islands", "Tuvalu", "Uganda", "Ukraine", "United Arab Emirates", "United Kingdom", "United States", "United States Minor Outlying Islands", "Uruguay", "Uzbekistan", "Vanuatu", "Venezuela", "Vietnam", "Virgin Islands (British)", "Virgin Islands (U.S.)", "Wallis and Futuna Islands", "Western Sahara", "Yemen", "Yugoslavia", "Zambia", "Zimbabwe" };
        for(int i =  0; i < countries.length; i++){
            countriesList.add(countries[i]);
        }

        String query = "";
        String csvFile = "Question_IDs.csv";
        String csvFile2 = "Question_Owner_Location.csv";
        BufferedReader br = null;
        BufferedReader br2 = null;
        String line = "";
        String cvsSplitBy = ";";
        String[] ownersCountry = new String[2];


        PrintWriter pw = new PrintWriter(new File("Answer_IDs.csv"));
        StringBuilder sb = new StringBuilder();

        try {

            br2 = new BufferedReader(new FileReader(csvFile2));
            while ((line = br2.readLine()) != null) {
                
                ownersCountry = line.split(cvsSplitBy);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }}



        try {

            br = new BufferedReader(new FileReader(csvFile));
            while ((line = br.readLine()) != null) {


                String[] questionID = line.split(cvsSplitBy);
                

                for (int i = 0; i < questionID.length; i++) {

                    query = "https://api.stackexchange.com/2.2/questions/" + questionID[i] + "/answers?order=desc&sort=activity&site=stackoverflow&filter=!1zSsisBB8s2GFyJI4JT0.&key=YOsp)YsweXtZMdqoUDFv6w((";


                    String json = readUrl(query);
                    Page page = new Gson().fromJson(json, Page.class);
                    sb.append(ownersCountry[i]+";");
                    System.out.println(ownersCountry[i]);


                    for (Item item : page.items) {
                        if (item.owner.user_id != null) {
                            String queryCountry = "https://api.stackexchange.com/2.2/users/" + item.owner.user_id + "?order=desc&sort=reputation&site=stackoverflow&filter=!23IkMbZQw76oILMeqALl.&key=YOsp)YsweXtZMdqoUDFv6w((";
                            String jsonCountry = readUrl(queryCountry);
                            Users users = new Gson().fromJson(jsonCountry, Users.class);



                            if (users.items.get(0).location != null) //ignore null countries
                                {
                                    String output = users.items.get(0).location.substring(users.items.get(0).location.lastIndexOf(" ")+1); // use last word of location (removes cities)


                                    for(String str: statesList) {  // change state names to States (USA)
                                        if(str.trim().contains(output))
                                            output = "States";
                                         }

                                    for(String ignore: ignoreList) {  // ignore all outputs of undesirable values
                                        if(ignore.trim().contains(output))
                                            output = "";
                                    }

/*                                   for(String country: countriesList) { //
                                       if (country.trim().contains(output)) {

                                       }
                                   }*/

                                       if (output != ""){
                                       //System.out.println(output);
                                       sb.append(output);
                                       sb.append(";");
                                        }
                                   }
                        }


                    }
                    sb.append("\n");
                }

            }
            pw.write(sb.toString());

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        pw.close();

        System.out.println("List of Locations IDs saved as \"Answer_IDs.csv\"");

    }
}
