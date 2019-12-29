package comSecurise.client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.EncodedKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import comSecurise.outil;

public class Client extends Thread {

	public JFrame frame;
	private JTextField textField;
	private JScrollPane scrollPane;
	private static JTextPane textPane;
	private static SimpleAttributeSet attributeSet;
	private static StyledDocument doc;

	/**
	 * Launch the application.
	 */

	static Socket socket;
	static DataInputStream dataIn;
	static DataOutputStream dataOut;
	static PrivateKey privateKey;
	static PublicKey publicKey;
	static PublicKey pubKeyServeur;

	public static void main(String[] args) throws NoSuchAlgorithmException, IOException, InvalidKeySpecException {
		KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
		keyGen.initialize(2048);
		KeyPair keypair = keyGen.genKeyPair();
		privateKey = keypair.getPrivate();
		publicKey = keypair.getPublic();

		byte[] key = publicKey.getEncoded();
		FileOutputStream keyfos = new FileOutputStream("cleClient");
		keyfos.write(key);
		keyfos.close();

		FileInputStream keyfis = new FileInputStream("cleServeur");
		byte[] encKey = new byte[keyfis.available()];
		keyfis.read(encKey);
		keyfis.close();
		EncodedKeySpec pubKeySpec = new X509EncodedKeySpec(encKey);
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		pubKeyServeur = keyFactory.generatePublic(pubKeySpec);

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Client window = new Client();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		String message = "";
		try {
			socket = new Socket("127.0.0.1", 5000);

			dataOut = new DataOutputStream(socket.getOutputStream());
			dataIn = new DataInputStream(socket.getInputStream());

			String dechiffre = "";
			while (!dechiffre.equals("EXIT")) {
				message = dataIn.readUTF();

				// dechiffremnt
				dechiffre = outil.dechiffrement(privateKey, message);

				StyleConstants.setItalic(attributeSet, true);
				StyleConstants.setForeground(attributeSet, Color.black);
				StyleConstants.setBackground(attributeSet, Color.pink);
				StyleConstants.setFontSize(attributeSet, 18);

				StyleConstants.setAlignment(attributeSet, StyleConstants.ALIGN_LEFT);

				int length = doc.getLength();

				// doc = textArea.getStyledDocument();
				doc.insertString(doc.getLength(), "\n" + "Alice :   " + dechiffre + "\n", attributeSet);

				doc.setParagraphAttributes(length + 1, 1, attributeSet, false);

				// verifier la singnature
				// sBoolean singnature = outil.verifierSignature(pubKeyServeur, message ,
				// dechiffre);
				// dataOut.writeUTF(singnature);

				// textArea.setText(textArea.getText().trim() + "\n" + dechiffre);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the application.
	 */
	public Client() {

		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */

	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 500, 400);
		frame.setTitle("===> Alice ");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// modification de l'icone de la fenetre
		Toolkit kit = Toolkit.getDefaultToolkit();
		Image img = kit.getImage("logo.jpg");
		frame.setIconImage(img);

		JPanel panelsouth = new JPanel();
		frame.getContentPane().add(panelsouth, BorderLayout.SOUTH);

		textField = new JTextField();
		textField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if (textField.getText().equals("EXIT")) {
					try {
						dataOut.writeUTF("Alice :   Aurevoir");
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					frame.dispose();
				}
			}
		});
		panelsouth.add(textField);
		textField.setColumns(22);

		JButton btnEnvoyer = new JButton("envoyer");
		btnEnvoyer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String messageOut = "";
				try {
					messageOut = textField.getText().trim();

					// singnature
					// String singnature = outil.signature(privateKey, messageOut);
					// dataOut.writeUTF(singnature);

					// Chiffrement du message
					String chiffre = outil.chiffrement(pubKeyServeur, messageOut);

					dataOut.writeUTF(chiffre);

					StyleConstants.setItalic(attributeSet, true);
					StyleConstants.setForeground(attributeSet, Color.black);
					StyleConstants.setBackground(attributeSet, Color.green);
					StyleConstants.setFontSize(attributeSet, 18);

					StyleConstants.setAlignment(attributeSet, StyleConstants.ALIGN_RIGHT);

					int length = doc.getLength();
					doc.insertString(doc.getLength(), "\n" + "Moi :   " + messageOut + "\n", attributeSet);
					doc.setParagraphAttributes(length + 1, 1, attributeSet, false);

					textField.setText("");

				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}
		});
		panelsouth.add(btnEnvoyer);

		textPane = new JTextPane();
		scrollPane = new JScrollPane(textPane);
		textPane.setEditable(false);
		frame.getContentPane().add(scrollPane, BorderLayout.CENTER);

		attributeSet = new SimpleAttributeSet();
		StyleConstants.setBold(attributeSet, true);

		doc = textPane.getStyledDocument();

	}

}
