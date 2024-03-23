import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.*;
import java.awt.*;
import java.security.Key;
import java.util.Base64;

public class CardDetailsFrame extends JFrame {
    private JTextField cardNumberField;
    private JTextField cvvField;
    private JTextField validUptoField;
    private JButton encryptButton;
    private JButton decryptButton;
    private JTextArea resultArea;
    private String encryptedCardNumber;
    private String encryptedCVV;
    private String encryptedValidUpto;

    public CardDetailsFrame() {
        // Set the title of the frame
        setTitle("Card Encryption and Decryption");

        // Set the frame to open in full screen
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        // Set the default close operation
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Set the layout manager to BorderLayout
        setLayout(new BorderLayout());

        // Initialize the components
        initComponent();    

        // Initialize the event handlers
        initEvent();    
    }

    private void initComponent() {
        // Create a new JPanel with a BoxLayout
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        // Set the border of the panel
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Create the heading label
        JLabel headingLabel = new JLabel("Card Encryption and Decryption");
        headingLabel.setFont(new Font("Serif", Font.BOLD, 30));
        headingLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Create the form panel
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBorder(BorderFactory.createTitledBorder("Enter card details"));

        // Create the card number label and text field
        JLabel cardNumberLabel = new JLabel("Card Number");
        cardNumberLabel.setFont(new Font("Serif", Font.BOLD, 20));
        cardNumberField = new JTextField(15);
        cardNumberField.setToolTipText("Enter a 15-digit card number");

        // Create the CVV label and text field
        JLabel cvvLabel = new JLabel("CVV");
        cvvLabel.setFont(new Font("Serif", Font.BOLD, 20));
        cvvField = new JTextField(15);
        cvvField.setToolTipText("Enter a 3-digit CVV");

        // Create the valid upto label and text field
        JLabel validUptoLabel = new JLabel("Valid Upto");
        validUptoLabel.setFont(new Font("Serif", Font.BOLD, 20));
        validUptoField = new JTextField(15);
        validUptoField.setToolTipText("Enter a date in the format MM/YYYY");

        // Add the labels and text fields to the form panel
        formPanel.add(cardNumberLabel);
        formPanel.add(cardNumberField);
        formPanel.add(cvvLabel);
        formPanel.add(cvvField);
        formPanel.add(validUptoLabel);
        formPanel.add(validUptoField);

        // Create the button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Create the encrypt and decrypt buttons
        encryptButton = new JButton("Encrypt");
        decryptButton = new JButton("Decrypt");

        // Add the buttons to the button panel
        buttonPanel.add(encryptButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        buttonPanel.add(decryptButton);

        // Create the result area
        resultArea = new JTextArea(10, 30);
        resultArea.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        // Add the heading label, form panel, button panel, and result area to the panel
        panel.add(headingLabel);
        panel.add(formPanel);
        panel.add(buttonPanel);
        panel.add(resultArea);

        // Add the panel to the frame
        add(panel);
    }

    private void initEvent() {
        // Add an action listener to the encrypt button
        encryptButton.addActionListener(e -> {
            try {
                // Validate the input
                if (!isValidInput()) {
                    // Show a popup message if the input is incorrect
                    JOptionPane.showMessageDialog(null, "Wrong input. Please enter a 15-digit card number, a 3-digit CVV, and a date in the format MM/YYYY.");
                    return;
                }

                // Encrypt the card number, CVV, and valid upto
                encryptedCardNumber = encrypt(cardNumberField.getText());
                encryptedCVV = encrypt(cvvField.getText());
                encryptedValidUpto = encrypt(validUptoField.getText());

                // Append the encrypted values to the result area
                resultArea.append("Encrypted Card Number: " + encryptedCardNumber + "\n");
                resultArea.append("Encrypted CVV: " + encryptedCVV + "\n");
                resultArea.append("Encrypted Valid Upto: " + encryptedValidUpto + "\n");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        // Add an action listener to the decrypt button
        decryptButton.addActionListener(e -> {
            try {
                // Decrypt the card number, CVV, and valid upto
                String decryptedCardNumber = decrypt(encryptedCardNumber);
                String decryptedCVV = decrypt(encryptedCVV);
                String decryptedValidUpto = decrypt(encryptedValidUpto);

                // Append the decrypted values to the result area
                resultArea.append("Decrypted Card Number: " + decryptedCardNumber + "\n");
                resultArea.append("Decrypted CVV: " + decryptedCVV + "\n");
                resultArea.append("Decrypted Valid Upto: " + decryptedValidUpto + "\n");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
    }

    private boolean isValidInput() {
        // Validate the card number, CVV, and valid upto
        return cardNumberField.getText().matches("\\d{15}") && cvvField.getText().matches("\\d{3}") && validUptoField.getText().matches("\\d{2}/\\d{4}");
    }

    private String encrypt(String valueToEnc) throws Exception {
        // Generate the key
        Key key = generateKey();

        // Get an instance of the Cipher for AES encryption
        Cipher c = Cipher.getInstance("AES");

        // Initialize the Cipher for encryption
        c.init(Cipher.ENCRYPT_MODE, key);

        // Encrypt the value
        byte[] encValue = c.doFinal(valueToEnc.getBytes());

        // Return the encrypted value as a Base64 encoded string
        return Base64.getEncoder().encodeToString(encValue);
    }

    private String decrypt(String encryptedValue) throws Exception {
        // Generate the key
        Key key = generateKey();

        // Get an instance of the Cipher for AES decryption
        Cipher c = Cipher.getInstance("AES");

        // Initialize the Cipher for decryption
        c.init(Cipher.DECRYPT_MODE, key);

        // Decode the Base64 encoded string
        byte[] decodedValue = Base64.getDecoder().decode(encryptedValue);

        // Decrypt the value
        byte[] decValue = c.doFinal(decodedValue);

        // Return the decrypted value as a string
        return new String(decValue);
    }

    private Key generateKey() throws Exception {
        // The secret key
        byte[] keyValue = new byte[]{'T', 'h', 'i', 's', 'I', 's', 'A', 'S', 'e', 'c', 'r', 'e', 't', 'K', 'e', 'y'};

        // Return the secret key specification
        return new SecretKeySpec(keyValue, "AES");
    }

    public static void main(String[] args) {
        try {
            // Set the look and feel to Nimbus
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Create and display the frame
        CardDetailsFrame frame = new CardDetailsFrame();
        frame.setVisible(true);
    }
}
