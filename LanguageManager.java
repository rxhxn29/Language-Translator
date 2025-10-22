class LanguageManager {
    // Display names and corresponding codes
    private String[] languageNames = {
        "Auto Detect", "English", "Hindi", "Spanish", "French", "German", "Italian", "Russian", "Japanese", "Marathi"
    };

    private String[] languageCodes = {
        "auto", "en", "hi", "es", "fr", "de", "it", "ru", "ja", "mr"
    };

    public String[] getLanguageNames() {
        return languageNames;
    }

    // Get language code for the selected display name
    public String getCodeForName(String name) {
        for (int i = 0; i < languageNames.length; i++) {
            if (languageNames[i].equalsIgnoreCase(name)) {
                return languageCodes[i];
            }
        }
        return "auto"; // default to auto detection
    }
}
