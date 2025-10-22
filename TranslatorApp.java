import javax.swing.*;
import java.awt.*;


class TranslatorApp extends JFrame {
    private JTextArea inputText, outputText;
    private JComboBox<String> sourceLang, targetLang;
    private JButton translateButton;

    // Composition: uses LanguageManager & TranslationService
    private LanguageManager langManager = new LanguageManager();
    private TranslationService translator = new TranslationService();
    // Constructor
    public TranslatorApp() {
        setTitle("Language Translator (OOP Version)");
        setSize(600, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        ImageIcon image = new ImageIcon("icon.png"); // creating object of img
        this.setIconImage(image.getImage());

        createUI();
        addListeners();

        setVisible(true);
    }

    private void createUI() {
        // Top Panel: language selectors
        JPanel topPanel = new JPanel();
    sourceLang = new JComboBox<>(langManager.getLanguageNames());
    sourceLang.setSelectedItem("Auto Detect");
    targetLang = new JComboBox<>(langManager.getLanguageNames());
    targetLang.setSelectedItem("English");
        topPanel.add(new JLabel("From:"));
        topPanel.add(sourceLang);
        topPanel.add(new JLabel("To:"));
        topPanel.add(targetLang);
    add(topPanel, BorderLayout.NORTH);

        // Center Panel: Text areas
        JPanel centerPanel = new JPanel(new GridLayout(2, 1, 10, 10));
        inputText = new JTextArea("Enter text here...");
        outputText = new JTextArea();
        outputText.setEditable(false);
        centerPanel.add(new JScrollPane(inputText));
        centerPanel.add(new JScrollPane(outputText));
        add(centerPanel, BorderLayout.CENTER);

        // Bottom Panel: Button
        JPanel bottomPanel = new JPanel();
        translateButton = new JButton("Translate");
        bottomPanel.add(translateButton);
        add(bottomPanel, BorderLayout.SOUTH);
    }
    // Method to add action listeners
    private void addListeners() {
        translateButton.addActionListener(e -> translateText());
    }
    // Method to handle translation
    private void translateText() {
        String text = inputText.getText().trim();
    String sourceName = (String) sourceLang.getSelectedItem();
    String targetName = (String) targetLang.getSelectedItem();
    String source = langManager.getCodeForName(sourceName);
    String target = langManager.getCodeForName(targetName);


        if (text.isEmpty() || text.equals("Enter text here...")) {
            JOptionPane.showMessageDialog(this, "Please enter text to translate!");
            return;
        }

        if (source.equals(target)) {
            outputText.setText(text);
            return;
        }

        outputText.setText("Translating...");
        translateButton.setEnabled(false);
        // do not block UI thread    
        SwingWorker<String, Void> worker = new SwingWorker<String, Void>() {
            @Override
            protected String doInBackground() {
                try {
                    return translator.translate(text, source, target);
                } catch (Exception ex) {
                    return "Translation failed: " + ex.getMessage();
                }
            }

            @Override
            protected void done() {
                try {
                    outputText.setText(get());
                } catch (Exception ex) {
                    outputText.setText("Error: " + ex.getMessage());
                } finally {
                    translateButton.setEnabled(true);
                }
            }
        };
        worker.execute();
    }
}