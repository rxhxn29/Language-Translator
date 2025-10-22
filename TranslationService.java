import java.io.*;
import java.net.*;

class TranslationService {
    // Method to translate text via Google API
    public String translate(String text, String source, String target) throws IOException {
        if (text == null || text.trim().isEmpty()) {
            throw new IllegalArgumentException("Text cannot be empty!");
        }

        // Encode URL
        String encodedText = URLEncoder.encode(text, "UTF-8");
        String urlString = String.format(
                "https://translate.googleapis.com/translate_a/single?client=gtx&sl=%s&tl=%s&dt=t&q=%s",
                source, target, encodedText);


        // Setup connection
        HttpURLConnection conn = (HttpURLConnection) new URL(urlString).openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("User-Agent", "Mozilla/5.0");
        conn.setConnectTimeout(15000);
        conn.setReadTimeout(15000);

        int responseCode = conn.getResponseCode();
        if (responseCode != 200) {
            throw new IOException("API returned error code: " + responseCode);
        }

        // Read response
        StringBuilder response = new StringBuilder();
        try (BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
            String line;
            while ((line = in.readLine()) != null) {
                response.append(line);
            }
        }

        // Extract translated text
        String jsonResponse = response.toString();
        if (jsonResponse.startsWith("[[")) {
            int firstQuote = jsonResponse.indexOf("\"");
            int secondQuote = jsonResponse.indexOf("\"", firstQuote + 1);
            if (firstQuote == -1 || secondQuote == -1)
                throw new IOException("No translation found!");

            String translated = jsonResponse.substring(firstQuote + 1, secondQuote);
            return cleanText(translated);
        } else {
            throw new IOException("Unexpected response format from translation API");
        }
    }

    // Helper method (Encapsulation)
    private String cleanText(String text) {
        return text.replace("\\n", "\n")
                   .replace("\\r", "\r")
                   .replace("\\t", "\t")
                   .replace("\\\"", "\"");
    }
}
