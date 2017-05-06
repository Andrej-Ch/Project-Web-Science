
import com.google.gson.Gson;
import java.io.*;
import java.net.URL;
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

/*    static class Item {
        object owner;
        String is_accepted;
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
        String answer_id;
        String reopen_vote_count;
        String score;
        String share_link;
        String tags;
        String title;
        String up_vote_count;
        String upvoted;
        String view_count;
    }*/

    class Page {
        List<Item> items;
    }

    static class Item{
        class owner{
            String user_id;
        }
        String is_accepted;
        String score;
        String answer_id;
        String question_id;
    }

    public static void main(String[] args) throws IOException {

        String query = "";
        String csvFile = "Question_IDs.csv";
        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ";";


        PrintWriter pw = new PrintWriter(new File("Answer_IDs.csv"));
        StringBuilder sb = new StringBuilder();

        try {

            br = new BufferedReader(new FileReader(csvFile));
            while ((line = br.readLine()) != null) {


                String[] questionID = line.split(cvsSplitBy);

                for (int i = 0; i < questionID.length; i++) {

                    query = "https://api.stackexchange.com/2.2/questions/" + questionID[i] + "/answers?order=desc&sort=activity&site=stackoverflow&filter=!1zSsisBB8s2GFyJI4JT0.&key=YOsp)YsweXtZMdqoUDFv6w((";


                    String json = readUrl(query);
                    Page page = new Gson().fromJson(json, Page.class);

                    sb.append(questionID[i]+";");

                    for (Item item : page.items) {
                        sb.append(item.answer_id);
                        sb.append(";");
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

        System.out.println("List of Answers IDs saved as \"Answer_IDs.csv\"");

    }
}
