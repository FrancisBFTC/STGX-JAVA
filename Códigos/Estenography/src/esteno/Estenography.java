package esteno;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.text.Caret;
public class Estenography extends JFrame{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextField fieldImage;
	private JTextField fieldText;
	private JLabel panel, labelImage, labelText;
	private JLabel status;
	private JButton button1, button2;
	boolean msgThread = false;
	String statusThread = "";
	
	public Runnable msg = new Runnable(){
		@Override
		public void run(){
			if(statusThread.equals("camuflar")){
				try {
					status.setForeground(Color.green);
					while(msgThread){
						status.setText("camuflando.");
						Thread.sleep(500);
						status.setText("camuflando..");
						Thread.sleep(500);
						status.setText("camuflando...");
					}
					status.setForeground(Color.blue);
					status.setText("terminado.");
				} catch (InterruptedException e) {}
			}
			if(statusThread.equals("ler")){
				status.setForeground(Color.green);
				try {
					status.setForeground(Color.green);
					while(msgThread){
						status.setText("lendo mensagem.");
						Thread.sleep(500);
						status.setText("lendo mensagem..");
						Thread.sleep(500);
						status.setText("lendo mensagem...");
					}
					status.setForeground(Color.blue);
					status.setText("terminado.");
				} catch (InterruptedException e) {}
			}
		}
	};
	public Estenography(){
		this.setBounds(0, 0, 300, 160);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setTitle("STGX - Esteganografia");
		
		panel = new JLabel();
		panel.setText("<html><div style='width:300;height:160;background:black;'></div></html>");
		panel.setBounds(0, 0, 300, 160);
		this.add(panel);
		
		labelImage = new JLabel("Imagem :");
		labelImage.setBounds(2, 20, 60, 20);
		labelImage.setForeground(Color.white);
		panel.add(labelImage);
		
		fieldImage = new JTextField();
		fieldImage.setBounds(60, 20, 200, 20);
		fieldImage.setBackground(Color.black);
		fieldImage.setForeground(Color.white);
		fieldImage.setBorder(BorderFactory.createLineBorder(Color.white));
		fieldImage.setCaretColor(Color.white);
		panel.add(fieldImage);
		
		labelText = new JLabel("Texto :");
		labelText.setBounds(2, 50, 60, 20);
		labelText.setForeground(Color.white);
		panel.add(labelText);
		
		fieldText = new JTextField();
		fieldText.setBounds(60, 50, 200, 20);
		fieldText.setBackground(Color.black);
		fieldText.setForeground(Color.white);
		fieldText.setBorder(BorderFactory.createLineBorder(Color.white));
		fieldText.setCaretColor(Color.white);
		panel.add(fieldText);
		
		button1 = new JButton("Camuflar");
		button1.setBounds(180, 80, 100, 20);
		button1.setBackground(Color.black);
		button1.setForeground(Color.green);
		button1.setBorder(BorderFactory.createLineBorder(Color.green));
		button1.setCursor(new Cursor(Cursor.HAND_CURSOR));
		panel.add(button1);
		
		button2 = new JButton("Descamuflar");
		button2.setBounds(40, 80, 120, 20);
		button2.setBackground(Color.black);
		button2.setForeground(Color.blue);
		button2.setBorder(BorderFactory.createLineBorder(Color.blue));
		button2.setCursor(new Cursor(Cursor.HAND_CURSOR));
		panel.add(button2);
		
		status = new JLabel();
		status.setBounds(170, 100, 200, 20);
		panel.add(status);
		
		button1.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				File img = new File(fieldImage.getText());
				int rgb;
				int cont = 0;
				char ch = 0;
				int ich, NS, NSR, NSG, NSB;
				String hex, SR = "", SG = "", SB= "", S = "";
				String bin, bin1;
				String concatBin = "";
				msgThread = true;
				statusThread = "camuflar";
				new Thread(msg).start();
				
				try {
					BufferedImage image = ImageIO.read(img);
					BufferedImage image1 = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
					Graphics2D graphics = image1.createGraphics();
					graphics.setColor(new Color(255, 255, 255));
					graphics.fillRect(0, 0, image.getWidth(), image.getHeight());
					
					for(int c = 0; c < fieldText.getText().length(); c++){
						ch = fieldText.getText().charAt(c);
						ich = ch;
						bin = Integer.toBinaryString(ich);
						bin1 = "0"+bin;
						concatBin = concatBin + bin1;
						
					}
	
					for(int x = 0; x < image.getWidth(); x++){
						for(int y = 0; y < image.getHeight(); y++){
							rgb = image.getRGB(x, y);
							hex = Integer.toBinaryString(rgb).substring(0, Integer.toBinaryString(rgb).length()-1);
							hex = (cont < concatBin.length()) ? hex + concatBin.charAt(cont) : Integer.toBinaryString(rgb);
					
							S  = hex.substring(0, 8); 
							SR = hex.substring(8, 16);
							SG = hex.substring(16, 24);
							SB = hex.substring(24, 32);

							NS  = Integer.parseInt(S, 2);
							NSR = Integer.parseInt(SR, 2);
							NSG = Integer.parseInt(SG, 2);
							NSB = Integer.parseInt(SB, 2);
						    
							image1.setRGB(x, y, new Color(NSR, NSG, NSB).getRGB());
							cont++;
						}
					}
					File outputfile = new File(img.getName().substring(0, img.getName().length()-4)+".png");
					ImageIO.write(image1, "png", outputfile);
			
					
				} catch (IOException e) {
					e.printStackTrace();
				}
				msgThread = false;
				Thread.interrupted();
			}
		});
		
		button2.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				File img = new File(fieldImage.getText());
				String concatBin = "", concatText = "";
				String hex;
				int bit = 8;
				int rgb, size, cont = 0, n = 0;
				char ch;
				boolean repeatCont = true;
				msgThread = true;
				statusThread = "ler";
				new Thread(msg).start();
				try {
					BufferedImage image = ImageIO.read(img);
					
					for(int x = 0; x < image.getWidth(); x++){
						for(int y = 0; y < image.getHeight(); y++){
						
							rgb = image.getRGB(x, y);
							size = Integer.toBinaryString(rgb).length()-1;
							hex = Integer.toBinaryString(rgb).substring(size, Integer.toBinaryString(rgb).length());
							if(!concatText.contains("[end]")){
								concatBin += hex;
							}
							if(concatBin.length() == 8){
								repeatCont = false;
								cont = 0;
								ch = Character.valueOf((char) Integer.parseInt(concatBin, 2));
								concatText += ch;
								concatBin = "";
							}else{
								repeatCont = true;
							}
							
							
							if(concatText.contains("[end]")){
								break;
							}
							if(repeatCont) {cont++;}else{cont = 0;}
							n++;
						}
					}
					msgThread = false;
					Thread.interrupted();
					JOptionPane.showMessageDialog(null, concatText, "Mensagem", JOptionPane.INFORMATION_MESSAGE);
					
				}catch(IOException e1){
					e1.printStackTrace();
				}
				
			}
		});
		
	}
	
	public static void main(String args[]){
		Estenography Est = new Estenography();
		Est.setVisible(true);
	}
}
