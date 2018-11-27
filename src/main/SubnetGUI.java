package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class SubnetGUI extends JFrame{

	String[] classes = {"C","B","A"};
	String[] modes = {"Networks","Hosts"};
	JTextField ipBox = new JTextField(12);
	JComboBox classBox = new JComboBox(classes);
	JTextField requirementBox = new JTextField(12);
	JComboBox modeBox = new JComboBox(modes);
	JEditorPane resultPane = new JEditorPane();
	JEditorPane concludePane = new JEditorPane();
	JButton startBtn = new JButton("Calculate now~!!");

	SubnetCalculator calculator = new SubnetCalculator();

	public SubnetGUI(){
		initComponents();
		initActions();
		//this.pack();
	}

	private void initComponents() {
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setMinimumSize(new Dimension(500,500));
		this.setLayout(new BorderLayout());
		this.setTitle("Subnet Calculator");
		JPanel northPanel = new JPanel();
		northPanel.setLayout(new BoxLayout(northPanel, BoxLayout.Y_AXIS));
		northPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), "Requirement Panel"));
		JPanel northPanelRow1 = new JPanel();
		northPanelRow1.add(new JLabel("IP:"));
		ipBox.setText("192.168.0.1");
		northPanelRow1.add( ipBox );
		northPanelRow1.add(new JLabel("Class:"));
		northPanelRow1.add( classBox );
		JPanel northPanelRow2 = new JPanel();
		northPanelRow2.add(new JLabel("Requirement:"));
		requirementBox.setText("50");
		northPanelRow2.add(requirementBox);
		northPanelRow2.add(modeBox);
		JPanel northPanelRow3 = new JPanel();
		northPanelRow3.add(startBtn);
		northPanel.add(northPanelRow1);
		northPanel.add(northPanelRow2);
		northPanel.add(northPanelRow3);
		this.add(northPanel, BorderLayout.NORTH);
		//Result pages
		concludePane.setEditable(false);
		concludePane.setContentType("text/html");
		concludePane.setText("<div style='padding: 10px; text-align: center;'><strong>This is calculating panel</strong></div>");
		concludePane.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), "Calculation Result"));
		this.add(concludePane, BorderLayout.CENTER);
		resultPane.setEditable(false);
		resultPane.setContentType("text/html");
		resultPane.setText("<div style='padding: 10px;'><strong>This is calculation result panel</strong></div>");
		resultPane.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), "Result IP"));
		this.add(resultPane, BorderLayout.SOUTH);
	}

	private void initActions() {
		startBtn.addActionListener(new CalculateAction());

	}

	class CalculateAction implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			String selectedMode = modeBox.getSelectedItem().toString();
			String requirement = requirementBox.getText();
			String selectedClass = classBox.getSelectedItem().toString();
			calculator.clearMessage();
			try{
				calculator.setIP(ipBox.getText());
				//If requirement is integer
				if(Util.isInteger(requirement)) {
					//Calculate
					if( selectedMode.equals("Networks") ) {
						calculator.calculateNetmaskFromNetwork(Util.toInteger(requirement), selectedClass);
					}
					else {
						calculator.calculateNetmaskFromHost(Util.toInteger(requirement), selectedClass);
					}
					//If no error message
					if(calculator.getMessage().equals("")){
						loadResult();
						loadConclusion(Util.toInteger(requirement) , selectedMode);
					}
					//If error message
					else{
						setRedResult(calculator.getMessage());
					}
				}
				//If requirement is not integer
				else {
					setRedResult("Requirement is not a valid integer.");
				}
			}
			catch(Exception error){
				setRedResult("Invalid IP: Please input another IP address.");
			}
		}

		private void setRedResult(String message){
			resultPane.setText("<div style='padding: 10px;'>"
					+ "<strong style='color: red;'>"+message+"</strong>"
					+"</div>");
			concludePane.setText("<div style='padding: 10px; text-align: center; color: red;'><strong>Cannot calculate,</strong><br/><br/>please check the error in result panel.</div>");
		}

		private void loadResult(){
			resultPane.setText("<div style='padding: 10px;'>"
					+ "<strong>Network: </strong>" + calculator.getNetwork() + "<br/>"
					+ "<strong>Subnet mask: </strong>" + calculator.getSubnet() + "<br/>"
					+ "<strong>First add: </strong>" + calculator.getFirstadd() + "<br/>"
					+ "<strong>Last add: </strong>" + calculator.getLastadd() + "<br/>"
					+ "<strong>Broadcast: </strong>" + calculator.getBroadcast() + "<br/>"
					+"</div>");
		}

		private void loadConclusion(int requirement, String mode){
			concludePane.setText("<div style='padding: 10px; text-align: center;'>"
					+ "The requirement is <strong>" + requirement + " " + mode.toLowerCase() + "</strong>.<br/>"
					+ "The IP is <strong>" + calculator.getNetwork() + "</strong> in <strong>" + classBox.getSelectedItem().toString() + " class</strong>.<br/><br/>"
					+ "In the calculation, it requires <strong>" + calculator.getBit() + " Bits</strong> in the IP address for the " + mode.toLowerCase() + ".<br/>"
					+ "So, our <strong>Subnet mask is "+calculator.getSubnet()+"</strong>.<br/><br/>"
					+ "So, we can get <strong>" + calculator.getNumberOfHosts() + " hosts</strong> and <strong>" + calculator.getNumberOfNetworks() + " networks</strong>.<br/><br/>"
					+ "The IP of  <strong>first add is " + calculator.getFirstadd() + "</strong><br/>and <strong>last add is " + calculator.getLastadd() + "</strong><br/>"
					+ "and <strong>broadcast IP is " + calculator.getBroadcast() + "</strong>."
					+ "</div>");
		}
	}
	
	public void start(){
		this.show();
	}

}
