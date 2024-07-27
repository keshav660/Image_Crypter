package Cryptography;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class Cryptography {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new CryptographyFrame().setVisible(true));
    }
}

class CryptographyFrame extends JFrame {
    private JTextField keyField;
    private JLabel fileLabel;
    private File selectedFile;
    private Integer storedKey; // Field to store the encryption key

    public CryptographyFrame() {
        setTitle("File Cryptography System - By Keshav Bhatt");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        
        initUI();
    }
     
    private void initUI() {
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BorderLayout());
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel titleLabel = new JLabel("File Cryptography System", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 32));
        topPanel.add(titleLabel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BorderLayout(10, 10));

        JPanel keyPanel = new JPanel();
        keyPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));

        JLabel keyLabel = new JLabel("Enter Key:");
        keyLabel.setFont(new Font("Arial", Font.PLAIN, 24));
        keyPanel.add(keyLabel);

        keyField = new JTextField(10);
        keyField.setFont(new Font("Arial", Font.PLAIN, 24));
        keyPanel.add(keyField);

        JButton encryptButton = new JButton("Encrypt");
        encryptButton.setFont(new Font("Arial", Font.PLAIN, 20));
        encryptButton.addActionListener(e -> encryptOrDecrypt(true));
        keyPanel.add(encryptButton);

        JButton decryptButton = new JButton("Decrypt");
        decryptButton.setFont(new Font("Arial", Font.PLAIN, 20));
        decryptButton.addActionListener(e -> encryptOrDecrypt(false));
        keyPanel.add(decryptButton);

        centerPanel.add(keyPanel, BorderLayout.NORTH);

        JPanel filePanel = new JPanel();
        filePanel.setLayout(new BorderLayout());

        fileLabel = new JLabel("No file selected", SwingConstants.CENTER);
        fileLabel.setFont(new Font("Arial", Font.ITALIC, 18));
        fileLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        filePanel.add(fileLabel, BorderLayout.CENTER);

        centerPanel.add(filePanel, BorderLayout.CENTER);

        JButton selectButton = new JButton("Select File");
        selectButton.setFont(new Font("Arial", Font.PLAIN, 20));
        selectButton.addActionListener(e -> selectFile());
        centerPanel.add(selectButton, BorderLayout.SOUTH);

        add(topPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
    }

    private void selectFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Image and Text files", "jpg", "png", "gif", "txt"));
        int result = fileChooser.showOpenDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            selectedFile = fileChooser.getSelectedFile();
            fileLabel.setText(selectedFile.getName());
            if (selectedFile.getName().matches(".*\\.(jpg|png|gif)$")) {
                fileLabel.setIcon(new ImageIcon(new ImageIcon(selectedFile.getAbsolutePath()).getImage().getScaledInstance(fileLabel.getWidth(), fileLabel.getHeight(), Image.SCALE_SMOOTH)));
            } else {
                fileLabel.setIcon(null);
            }
        }
    }

    private void encryptOrDecrypt(boolean isEncrypt) {
        if (selectedFile == null) {
            JOptionPane.showMessageDialog(this, "Please select a file first.", "No File Selected", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String keyText = keyField.getText();
        if (keyText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a key.", "No Key Entered", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            int key = Integer.parseInt(keyText);

            // Check key for decryption
            if (!isEncrypt && (storedKey == null || storedKey != key)) {
                JOptionPane.showMessageDialog(this, "Please enter the same key used for encryption.", "Invalid Key", JOptionPane.ERROR_MESSAGE);
                return;
            }

            performOperation(key, isEncrypt);

            // Store the key if encrypting
            if (isEncrypt) {
                storedKey = key;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid key format. Please enter a valid integer key.", "Invalid Key", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void performOperation(int key, boolean isEncrypt) {
        try {
            FileInputStream fis = new FileInputStream(selectedFile);
            byte[] data = new byte[fis.available()];
            fis.read(data);
            fis.close();

            for (int i = 0; i < data.length; i++) {
                data[i] = (byte) (data[i] ^ key);
            }

            FileOutputStream fos = new FileOutputStream(selectedFile);
            fos.write(data);
            fos.close();

            String message = isEncrypt ? "Encryption completed successfully!" : "Decryption completed successfully!";
            JOptionPane.showMessageDialog(this, message, "Operation Successful", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "An error occurred: " + e.getMessage(), "Operation Failed", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
}
