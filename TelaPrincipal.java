import static java.nio.charset.StandardCharsets.UTF_8;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.nio.charset.StandardCharsets;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
import org.eclipse.paho.client.mqttv3.persist.MqttDefaultFilePersistence;

public class TelaPrincipal extends JFrame {

	MqttClient client;

	 private JTextField textField;

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField url_mqtt;
	private JLabel lblNewLabel;
	private JButton btnDesconectar;
	private JTextField edt_topic_publish;
	private JLabel lblTpico;
	private JLabel lblValor;
	private JTextField edt_topic_value;
	private JTextField edt_topic_subscribe;
	private JLabel lblTopicSubscribe;
	private JLabel lblUser;
	private JLabel lblNewLabel_2;
	private JTextField edt_user;
	private JPasswordField edt_pass;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					TelaPrincipal frame = new TelaPrincipal();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public TelaPrincipal() {
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setBounds(100, 100, 521, 332);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);

		JTextArea log_area = new JTextArea();
		log_area.setBounds(332, 87, 165, 197);
		contentPane.add(log_area);

		JButton btnNewButton = new JButton("Conectar");
		btnNewButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				try {

					MqttDefaultFilePersistence persistence = new MqttDefaultFilePersistence(
							"C:\\Users\\uberdam.cavaletti\\1");

					client = new MqttClient(url_mqtt.getText(), "ClienteJava", persistence);

					MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
					mqttConnectOptions.setUserName(edt_user.getText());
					mqttConnectOptions.setPassword(edt_pass.getPassword());
					// mqttConnectOptions.setSocketFactory(SSLSocketFactory.getDefault()); // using
					// the default socket factory
					client.connect(mqttConnectOptions);

					client.setCallback(new MqttCallback() {

						@Override
						public void connectionLost(Throwable cause) { // Called when the client lost the connection to
																		// the broker

						}

						@Override
						public void messageArrived(String topic, MqttMessage message) {

							String readableData = new String(message.getPayload(), StandardCharsets.UTF_8);
							//System.out.println(topic + ":" + readableData);


							 SwingUtilities.invokeLater(new Runnable() {
						            @Override
						            public void run() {
						            	String text = log_area.getText();
						            	log_area.setText("");
						               	log_area.append(topic + ":" + readableData+"\n");
						            	log_area.append(text);

						            }
						        });

						}


						@Override
						public void deliveryComplete(IMqttDeliveryToken token) { // Called when a outgoing publish is
																					// complete
						}
					});

				} catch (MqttException e1) {
					System.out.print(e1.getMessage());
					e1.printStackTrace();
				}

			}
		});
		btnNewButton.setBounds(3, 118, 89, 23);
		contentPane.add(btnNewButton);

		url_mqtt = new JTextField();
		url_mqtt.setText("tcp://34.238.167.223:1883");
		url_mqtt.setBounds(3, 33, 214, 20);
		contentPane.add(url_mqtt);
		url_mqtt.setColumns(10);

		lblNewLabel = new JLabel("URL");
		lblNewLabel.setBounds(3, 8, 49, 14);
		contentPane.add(lblNewLabel);

		btnDesconectar = new JButton("Desconectar");
		btnDesconectar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				try {
					client.disconnect();
				} catch (MqttException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		btnDesconectar.setBounds(111, 118, 106, 23);
		contentPane.add(btnDesconectar);

		edt_topic_publish = new JTextField();
		edt_topic_publish.setText("topic1");
		edt_topic_publish.setColumns(10);
		edt_topic_publish.setBounds(3, 230, 89, 20);
		contentPane.add(edt_topic_publish);

		lblTpico = new JLabel("Tópico");
		lblTpico.setBounds(3, 205, 49, 14);
		contentPane.add(lblTpico);

		lblValor = new JLabel("Payload");
		lblValor.setBounds(111, 205, 49, 14);
		contentPane.add(lblValor);

		edt_topic_value = new JTextField();
		edt_topic_value.setText("1");
		edt_topic_value.setColumns(10);
		edt_topic_value.setBounds(111, 230, 106, 20);
		contentPane.add(edt_topic_value);

		edt_topic_subscribe = new JTextField();
		edt_topic_subscribe.setText("topic1");
		edt_topic_subscribe.setColumns(10);
		edt_topic_subscribe.setBounds(332, 25, 165, 20);
		contentPane.add(edt_topic_subscribe);

		lblTopicSubscribe = new JLabel("Topic Subscribe");
		lblTopicSubscribe.setBounds(332, 8, 89, 14);
		contentPane.add(lblTopicSubscribe);

		JButton btnPublicar = new JButton("Publicar");
		btnPublicar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				try {
					client.publish(edt_topic_subscribe.getText(), edt_topic_value.getText().getBytes(UTF_8), 2, // QoS =
																												// 2
							false);
				} catch (MqttPersistenceException e1) {
					e1.printStackTrace();
				} catch (MqttException e1) {
					e1.printStackTrace();
				}

			}
		});
		btnPublicar.setBounds(111, 261, 106, 23);
		contentPane.add(btnPublicar);

		JButton btnSub = new JButton("Sub");
		btnSub.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					client.subscribe(edt_topic_subscribe.getText(), 1);
				} catch (MqttException e1) {

					e1.printStackTrace();
				}

			}
		});
		btnSub.setBounds(332, 53, 59, 23);
		contentPane.add(btnSub);

		JButton btn_unsub = new JButton("Unsubscribe");
		btn_unsub.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					client.unsubscribe(edt_topic_subscribe.getText());
				} catch (MqttException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		btn_unsub.setBounds(408, 53, 89, 23);
		contentPane.add(btn_unsub);

		lblUser = new JLabel("User");
		lblUser.setBounds(3, 64, 49, 14);
		contentPane.add(lblUser);

		lblNewLabel_2 = new JLabel("Pass");
		lblNewLabel_2.setBounds(111, 64, 49, 14);
		contentPane.add(lblNewLabel_2);

		edt_user = new JTextField();
		edt_user.setText("broker");
		edt_user.setColumns(10);
		edt_user.setBounds(3, 87, 89, 20);
		contentPane.add(edt_user);

		edt_pass = new JPasswordField();
		edt_pass.setBounds(111, 87, 106, 20);
		contentPane.add(edt_pass);
	}
}
