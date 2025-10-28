package com.doruk.dnotes.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.util.Date;
import java.util.Scanner;

import com.doruk.dnotes.DIFactory;
import com.doruk.dnotes.enums.Preference;
import com.doruk.dnotes.store.GlobalConstants;

public class UpdatesTracker {

    /**
     * Fetches latest release tag from GitHub.
     * @return latest release tag (like v1.2.6)
     */
    private static String fetchLatestReleaseTag() {
        try {
            URI uri = new URI(GlobalConstants.UPDATE_CHECK_URL);
            URL url = uri.toURL();
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Accept", "application/vnd.github+json");
            conn.setRequestMethod("GET");

            int responseCode = conn.getResponseCode();
            if (responseCode != 200) {
                System.err.println("Failed to fetch release info, code: " + responseCode);
                System.out.println(new String(conn.getErrorStream().readAllBytes()));
                return null;
            }

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) response.append(line);
            in.close();

//            JSONObject json = new JSONObject(response.toString());
//            return json.getString("tag_name");
            System.out.println(response);
            System.out.println("tag: " + parseAndGetTag(response.toString()));
            return parseAndGetTag(response.toString());
        } catch (IOException | URISyntaxException e) {
            DIFactory.createLogger().error(Thread.currentThread(), e);
            return null;
        }
    }

    private static String parseAndGetTag(String json) {
        try (Scanner scanner = new Scanner(json)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();
                if (line.startsWith("\"tag_name\":")) {
                    int firstQuote = line.indexOf("\"", 10);
                    int secondQuote = line.indexOf("\"", firstQuote + 1);
                    return line.substring(firstQuote + 1, secondQuote);
                }
            }
        }
        return null;
    }

    /**
     * Converts version string like "v1.2.6" to integer 126
     */
    private static int versionStringToInt(String version) {
        if (version == null || version.isEmpty()) return 0;
        // remove 'v' and dots
        String numeric = version.replaceAll("[^0-9]", "");
        try {
            return Integer.parseInt(numeric);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return 0;
        }
    }

    private static boolean isOneDayPassed() {
        var dt = DIFactory.createGlobalPreference().loadLong(Preference.LastUpdateChecked, new Date().getTime());
        return true; // for testing
//        return (new Date().getTime() - dt) > (6 * 60 * 60 * 1000); // 6 hours
    }

    /**
     * Checks if a newer release exists
     */
    public static boolean isUpdateAvailable() {
        if (!isOneDayPassed())
            return false;

        String latestTag = fetchLatestReleaseTag();

        // if succeed fetching, update last update checked time
        DIFactory.createGlobalPreference().saveLong(Preference.LastUpdateChecked, new Date().getTime());

        if (latestTag == null) return false;

        int latestVersion = versionStringToInt(latestTag);
        return latestVersion > GlobalConstants.APP_VERSION_CODE;
    }
}
