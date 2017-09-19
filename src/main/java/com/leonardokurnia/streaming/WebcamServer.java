package com.leonardokurnia.streaming;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import com.github.sarxos.webcam.Webcam;

public class WebcamServer {

	public static void main(String[] args) {
		
		Webcam webcam = Webcam.getDefault();
		webcam.setViewSize(new Dimension(640, 480));
		webcam.open();
		
		String serverName = "192.168.5.168";
		int serverPort = 6066;
		
		try {
			
			BufferedImage image = webcam.getImage();
			
			ImageIcon imageIcon = new ImageIcon();
			imageIcon.setImage(image);
			JLabel picLabel = new JLabel(imageIcon);
			JFrame window = new JFrame("Webcam Panel");
			window.add(picLabel);
			window.setResizable(true);
			window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			window.pack();
			window.setVisible(true);
			
			while (true) {
				
				Socket socket = new Socket(serverName, serverPort);
				OutputStream outputStream = socket.getOutputStream();
				ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
				
				image = webcam.getImage();
				
				imageIcon.setImage(image);
				window.repaint();
				
				ImageIO.write(image, "jpg", byteArrayOutputStream);
				byte[] size = ByteBuffer.allocate(4).putInt(byteArrayOutputStream.size()).array();
				outputStream.write(size);
				outputStream.write(byteArrayOutputStream.toByteArray());
				outputStream.flush();
				socket.close();
				
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
}
