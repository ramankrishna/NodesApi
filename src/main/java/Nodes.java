import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;



public class Nodes {

    public static void main(String[] args) throws IOException, JSONException {
        // TODO Auto-generated method stub
        String url = "https://nodes-on-nodes-challenge.herokuapp.com/nodes/089ef556-dfff-4ff2-9733-654645be56fe";
        String response = getResponse(url);
        System.out.println(response);
        response = removeBrackets(response);
        JSONObject obj = new JSONObject(response);
        JSONArray arr = obj.getJSONArray("child_node_ids");
        ArrayList<String> list = new ArrayList<String>();
        for (int i = 0; i < arr.length(); i++) {
            list.add(arr.getString(i));
        }
        HashSet<String> set = new HashSet<String>();
        HashMap<String, Integer> map = new HashMap<String, Integer>();
        int count = 0;
        while (list.size() > 0) {
            String id = list.get(0);
            if (!set.contains(id)) {
                set.add(id);
                count++;
                String url1 = "https://nodes-on-nodes-challenge.herokuapp.com/nodes/" + id;
                String response1 = getResponse(url1);
                response1 = removeBrackets(response1);
                System.out.println(response1);
                JSONObject obj1 = new JSONObject(response1);
                JSONArray arr1 = obj1.getJSONArray("child_node_ids");
                for (int i = 0; i < arr1.length(); i++) {
                    list.add(arr1.getString(i));
                }
                if (map.containsKey(id)) {
                    map.put(id, map.get(id) + 1);
                } else {
                    map.put(id, 1);
                }
            }
            list.remove(0);
        }
        System.out.println("Total number of unique node IDs: " + count);
        int max = 0;
        String maxId = "";
        for (String key : map.keySet()) {
            if (map.get(key) > max) {
                max = map.get(key);
                maxId = key;
            }
        }
        System.out.println("Most common node ID: " + maxId);
    }

    public static String removeBrackets(String response) {
        return response.substring(1, response.length() - 1);
    }

    public static String getResponse(String url) throws IOException {
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        return response.toString();
    }
}
