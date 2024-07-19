import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONObject;

public class CricketMatchesAssignment {
    public static void main(String[] args) {
        try {
            // Send GET request to the API
            URL url = new URL("https://api.cuvora.com/car/partner/cricket-data");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("apiKey", "test-creds@2320");

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                // Read the response from the API
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                // Parse the JSON response
                JSONArray matchesArray = new JSONArray(response.toString());

                // Initialize variables for computations
                int highestScore = 0;
                String teamWithHighestScore = "";
                int matchesWith300PlusScore = 0;

                for (int i = 0; i < matchesArray.length(); i++) {
                    JSONObject matchObject = matchesArray.getJSONObject(i);

                    // Check if the required fields exist
                    if (matchObject.has("t1s") && matchObject.has("t2s") && matchObject.has("t1") && matchObject.has("t2")) {
                        int team1Score = matchObject.getInt("t1s");
                        int team2Score = matchObject.getInt("t2s");

                        // Update highest score and team
                        if (team1Score > highestScore) {
                            highestScore = team1Score;
                            teamWithHighestScore = matchObject.getString("t1");
                        }

                        if (team2Score > highestScore) {
                            highestScore = team2Score;
                            teamWithHighestScore = matchObject.getString("t2");
                        }

                        // Count matches with a total score of 300 or more
                        if (team1Score + team2Score >= 300) {
                            matchesWith300PlusScore++;
                        }
                    } else {
                        System.out.println("Warning: Missing fields in match data.");
                    }
                }

                // Print the results
                System.out.println("Highest Score: " + highestScore + " by " + teamWithHighestScore);
                System.out.println("Number of Matches with total 300 Plus Score: " + matchesWith300PlusScore);

            } else {
                System.out.println("Error: Failed to fetch data from the API. Response Code: " + responseCode);
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
