package StackOverflow;

import com.google.gson.Gson;

import java.io.*;
import java.net.URL;
import java.util.List;
import java.util.zip.GZIPInputStream;

/**
 * Created by Andro on 05-May-17.
 */
public class QuestionIDToCsv {


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
        String accepted_answer_id;
        String answer_count;
        String answers;
        String body;
        String closed_date;
        //String closed_details (closed_details);
        String comment_count;
        String comments;
        String creation_date;
        String delete_vote_count;
        String down_vote_count;
        String downvoted;
        String favorite_count;
        String favorited;
        String is_answered;
        String last_activity_date;
        String last_edit_date;
        //String last_editor (shallow_user);
        String link;
        String locked_date;
        //String migrated_from (migration_info);
        //String migrated_to (migration_info);
        //String notice (notice);
        //String owner (shallow_user);
        String protected_date;
        String question_id;
        String reopen_vote_count;
        String score;
        String share_link;
        String tags;
        String title;
        String up_vote_count;
        String upvoted;
        String view_count;

    }

    static class Page {
        List<Item> items;
    }

    public static void main(String[] args) throws IOException {

        String query = "https://api.stackexchange.com/2.2/questions?pagesize=10&order=desc&min=200&sort=votes&site=stackoverflow&filter=!2.dt38CU)6yRIbWVfDpQc&key=YOsp)YsweXtZMdqoUDFv6w((";

        String json = readUrl(query);
        Page page = new Gson().fromJson(json, Page.class);

        PrintWriter pw = new PrintWriter(new File("Question_IDs.csv"));
        StringBuilder sb = new StringBuilder();

        String s = "";

        for (Item item : page.items) {
            s = s + item.question_id + ";";
            
            System.out.println(s);
        }
        sb.append(s);
        pw.write(sb.toString());
        pw.close();
        System.out.println("List of question IDs saved as \"Question_IDs.csv\"");
    }
}
