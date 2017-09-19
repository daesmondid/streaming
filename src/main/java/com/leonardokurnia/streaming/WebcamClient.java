package com.leonardokurnia.streaming;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class WebcamClient extends Thread {

	private ServerSocket serverSocket;

	public WebcamClient(int port) throws IOException {
		serverSocket = new ServerSocket(port);
	}

	public void run() {
		
		ImageIcon imageIcon = new ImageIcon();
		JLabel picLabel = new JLabel(imageIcon);
		BufferedImage image;
		JFrame window = new JFrame("Test webcam panel");
		window.add(picLabel);
		window.setResizable(true);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setSize(new Dimension(640, 480));
		window.setVisible(true);
		
		while (true) {
			
			try {
				
				Socket server = serverSocket.accept();
				InputStream inputStream = server.getInputStream();
				byte[] sizeAr = new byte[4];
				inputStream.read(sizeAr);
				int size = ByteBuffer.wrap(sizeAr).asIntBuffer().get();
				byte[] imageAr = new byte[size];
				inputStream.read(imageAr);
				
				image = ImageIO.read(new ByteArrayInputStream(imageAr));
				
				imageIcon.setImage(image);
				window.repaint();
				
				server.close();

			} catch (SocketTimeoutException s) {
				System.out.println("Socket timed out!");
				break;
			} catch (IOException e) {
				e.printStackTrace();
				break;
			}
			
		}
	}

	public static void main(String[] args) {
		int port = Integer.parseInt(args[0]);
		try {
			Thread t = new WebcamClient(port);
			t.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
