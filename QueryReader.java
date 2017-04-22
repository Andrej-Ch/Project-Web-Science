import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;
import java.util.zip.GZIPInputStream;

public class QueryReader {


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


    static class Item {
        String about_me;
        String accept_rate;
        String account_id;
        String age;
        String answer_count;
        String badge_counts;
        String creation_date;
        String display_name;
        String down_vote_count;
        String is_employee;
        String last_access_date;
        String last_modified_date;
        String link;
        String location;
        String profile_image;
        String question_count;
        String reputation;
        String reputation_change_day;
        String reputation_change_month;
        String reputation_change_quarter;
        String reputation_change_week;
        String reputation_change_year;
        String timed_penalty_date;
        String up_vote_count;
        String user_id;
        String user_type;
        String view_count;
        String website_url;

    }

    static class Page {
        List<Item> items;
    }


    public static void main(String[] args) throws IOException {

        String query = "https://api.stackexchange.com/2.2/users?pagesize=15&fromdate=1298764800&todate=1298851200&order=desc&sort=reputation&site=stackoverflow&filter=!4(Yrwr)8xrv7I(d0_";

        String json = readUrl(query);
        Page page = new Gson().fromJson(json, Page.class);



        for (Item item : page.items)
            System.out.println("Username: " + item.account_id + ", Location: " + item.location  + ", Age: " + item.age);
    }
}