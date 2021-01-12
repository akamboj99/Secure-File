package com.javapointers.javase;

import java.awt.*;
import java.awt.event.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import javax.swing.*;
import javax.swing.filechooser.*;
import javax.crypto.*;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.io.*;
import java.net.*;
import java.util.Scanner;
import javax.imageio.ImageIO;
import java.nio.charset.StandardCharsets;

public class SecureFileGUI extends JFrame implements MouseListener , ActionListener 
{
    static JFrame frame;
    JButton dropboxButton;
    static JTextField pathEntry;
    JButton openFile;
    File[] files;
    static String TextFromPlaintextFile;
    static String TextFromCiphertextFileDES;
    static String TextFromCiphertextFileAES;
    static String TextFromCiphertextFileRC4;
    static SecureFileGUI f1 = new SecureFileGUI();
    static int count;
    static String PathOfImageWithKey;
    static String PathOfUserImage;
    static String ConcatKeysAfterEncryption = "";
    static String CipherFilePathToSave;
    static String CipherImagePathToSave;
    static String PlaintextFileRecoveredPathToSave;

    public SecureFileGUI() 
    {
        frame = new JFrame("Secure File");
        frame.setSize(440,400);
        JButton dropboxButton = new JButton("Upload To Dropbox");
        dropboxButton.setBounds(88,200,250,30);
        frame.add(dropboxButton);
        dropboxButton.addMouseListener(this);
        frame.setLayout(new BorderLayout());
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    
    public void actionPerformed(ActionEvent e){}
    
    public void createGUI()
    {
        pathEntry = new JTextField("No File Selected",100);
        pathEntry.setBounds(53,60,200,30);
        JButton openFile = new JButton("OPEN  FILE");
        openFile.setBounds(273,60,100,30);
        openFile.addActionListener(f1);  
        JButton encryptButton = new JButton("ENCRYPT");
        encryptButton.setBounds(138,130,150,30);
        encryptButton.addActionListener(f1);
        JButton decryptButton = new JButton("DECRYPT");
        decryptButton.setBounds(138,270,150,30);
        decryptButton.addActionListener(f1);
        frame.add(pathEntry);
        frame.add(openFile);
        frame.add(encryptButton);
        frame.add(decryptButton);
        frame.setLayout(null);
        
        openFile.addActionListener(new ActionListener() 
        {
            public void actionPerformed(ActionEvent e) 
            {
                JFileChooser j = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
                j.setDialogTitle("Select a File and an Image");
                j.setMultiSelectionEnabled(true);
                int r = j.showOpenDialog(null); 
                if (r == JFileChooser.APPROVE_OPTION) 
                { 
                    files = j.getSelectedFiles();
                    String fileNames = "";
                    for(File file: files)
                    {
                        fileNames += file.getName() + " ; ";
                    }
                    pathEntry.setText(fileNames);
                } 
            }
        });
        
        encryptButton.addActionListener(new ActionListener() 
        {
            public void actionPerformed(ActionEvent e) 
            {
            	JFileChooser fileChooser1 = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
            	fileChooser1.setDialogTitle("Save Cipher Text File");
                int option = fileChooser1.showSaveDialog(frame);
                if(option == JFileChooser.APPROVE_OPTION)
                {
                   File file = fileChooser1.getSelectedFile();
                   CipherFilePathToSave = file.getAbsolutePath()+".txt";
                }
                
                JFileChooser fileChooser2 = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
                fileChooser2.setDialogTitle("Save Cipher Image");
                int option2 = fileChooser2.showSaveDialog(frame);
                if(option2 == JFileChooser.APPROVE_OPTION)
                {
                   File file = fileChooser2.getSelectedFile();
                   CipherImagePathToSave = file.getAbsolutePath()+".png";
                }
            	
            	for(int i=0; i<2; i++)
            	{
            		if((files[i].getAbsolutePath()).endsWith(".txt"))
                	{
                		BufferedReader in = null;
        				try 
        				{
        					in = new BufferedReader(new FileReader(files[i].getAbsolutePath()));
        				} 
        				catch (FileNotFoundException e1) 
        				{
        					e1.printStackTrace();
        				} 
                        try 
                        {
        					TextFromPlaintextFile = in.readLine();
        				} 
                        catch (IOException e1) 
                        {
        					e1.printStackTrace();
        				}
                	}
                	else
                	{
                		PathOfUserImage = files[i].getAbsolutePath();
                	}
            	}
            	try 
            	{
					f1.RunningTheCompleteEncryptFunction();
				} 
            	catch (Exception e1) 
            	{
					e1.printStackTrace();
				}
            }
        });
        
        decryptButton.addActionListener(new ActionListener() 
        {
            public void actionPerformed(ActionEvent e) 
            {
            	JFileChooser fileChooser3 = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
            	fileChooser3.setDialogTitle("Save Recovered Plaintext File");
                int option3 = fileChooser3.showSaveDialog(frame);
                if(option3 == JFileChooser.APPROVE_OPTION)
                {
                   File file = fileChooser3.getSelectedFile();
                   PlaintextFileRecoveredPathToSave = file.getAbsolutePath()+".txt";
                }
            	
            	for(int i=0; i<2; i++)
            	{
            		if((files[i].getAbsolutePath()).endsWith(".txt"))
                	{
                		BufferedReader xyz = null;
        				try 
        				{
        					xyz = new BufferedReader(new FileReader(files[i].getAbsolutePath()));
        				} 
        				catch (FileNotFoundException e1) 
        				{
        					e1.printStackTrace();
        				} 
                        try 
                        {
        					TextFromCiphertextFileDES = xyz.readLine();
        					TextFromCiphertextFileAES = xyz.readLine();
        					TextFromCiphertextFileRC4 = xyz.readLine();
        				} 
                        catch (IOException e1) 
                        {
        					e1.printStackTrace();
        				}
                	}
                	else
                	{
                		PathOfImageWithKey = files[i].getAbsolutePath();
                	}
            	}
            	try 
                { f1.RunningTheCompleteDecryptFunction(); } 
                catch (Exception e1) 
                { e1.printStackTrace(); }
            }
        });
    }

    public void mouseClicked(MouseEvent e) 
    {
        try { Desktop.getDesktop().browse(new URI("https://www.dropbox.com/h")); } 
        catch (IOException | URISyntaxException e1) { e1.printStackTrace(); }
    }
    public void mousePressed(MouseEvent e) {}
    public void mouseReleased(MouseEvent e) {}
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}
    
    
    private static class DES 
    { 
        // Initial Permutation Table 
        int[] IP = { 58, 50, 42, 34, 26, 18, 10, 2, 60, 52, 44, 36, 28, 20, 12, 4, 62, 54, 46, 38, 30, 22, 14, 6, 64, 56, 
                     48, 40, 32, 24, 16, 8, 57, 49, 41, 33, 25, 17, 9, 1, 59, 51, 43, 35, 27, 19, 11, 3, 61, 53, 45, 
                     37, 29, 21, 13, 5, 63, 55, 47, 39, 31, 23, 15, 7 }; 
        // Inverse Initial Permutation Table 
        int[] IP1 = { 40, 8, 48, 16, 56, 24, 64, 32, 39, 7, 47, 15, 55, 23, 63, 31, 38, 6, 46, 14, 54, 22, 62, 30, 37, 
                      5, 45, 13, 53, 21, 61, 29, 36, 4, 44, 12, 52, 20, 60, 28, 35, 3, 43, 11, 51, 19, 59, 27, 34, 
                      2, 42, 10, 50, 18, 58, 26, 33, 1, 41, 9, 49, 17, 57, 25 }; 
        // first key-hePermutation Table 
        int[] PC1 = { 57, 49, 41, 33, 25, 17, 9, 1, 58, 50, 42, 34, 26, 18, 10, 2, 59, 51, 43, 35, 27, 19, 11, 3, 60, 
                      52, 44, 36, 63, 55, 47, 39, 31, 23, 15, 7, 62, 54, 46, 38, 30, 22, 14, 6, 61, 
                      53, 45, 37, 29, 21, 13, 5, 28, 20, 12, 4 }; 
        // second key-Permutation Table 
        int[] PC2 = { 14, 17, 11, 24, 1, 5, 3, 28, 15, 6, 21, 10, 23, 19, 12, 4, 26, 8, 16, 7, 27, 20, 13, 2, 41, 52, 
                      31, 37, 47, 55, 30, 40, 51, 45, 33, 48, 44, 49, 39, 56, 34, 53, 46, 42, 50, 36, 29, 32 }; 
        // Expansion D-box Table 
        int[] EP = { 32, 1, 2, 3, 4, 5, 4, 5, 6, 7, 8, 9, 8, 9, 10, 11, 12, 13, 12, 13, 14, 15, 16, 17, 16, 17, 18, 
                     19, 20, 21, 20, 21, 22, 23, 24, 25, 24, 25, 26, 27, 28, 29, 28, 29, 30, 31, 32, 1 }; 
        // Straight Permutation Table 
        int[] P = { 16, 7, 20, 21, 29, 12, 28, 17, 1, 15, 23, 26, 5, 18, 31, 10, 2, 8, 24, 14, 32, 27, 3, 9, 19, 13, 
                    30, 6, 22, 11, 4, 25 }; 
        // S-box Table 
        int[][][] sbox = { 
            { { 14, 4, 13, 1, 2, 15, 11, 8, 3, 10, 6, 12, 5, 9, 0, 7 }, 
              { 0, 15, 7, 4, 14, 2, 13, 1, 10, 6, 12, 11, 9, 5, 3, 8 }, 
              { 4, 1, 14, 8, 13, 6, 2, 11, 15, 12, 9, 7, 3, 10, 5, 0 }, 
              { 15, 12, 8, 2, 4, 9, 1, 7, 5, 11, 3, 14, 10, 0, 6, 13 } }, 
            { { 15, 1, 8, 14, 6, 11, 3, 4, 9, 7, 2, 13, 12, 0, 5, 10 }, 
              { 3, 13, 4, 7, 15, 2, 8, 14, 12, 0, 1, 10, 6, 9, 11, 5 }, 
              { 0, 14, 7, 11, 10, 4, 13, 1, 5, 8, 12, 6, 9, 3, 2, 15 }, 
              { 13, 8, 10, 1, 3, 15, 4, 2, 11, 6, 7, 12, 0, 5, 14, 9 } }, 
            { { 10, 0, 9, 14, 6, 3, 15, 5, 1, 13, 12, 7, 11, 4, 2, 8 }, 
              { 13, 7, 0, 9, 3, 4, 6, 10, 2, 8, 5, 14, 12, 11, 15, 1 }, 
              { 13, 6, 4, 9, 8, 15, 3, 0, 11, 1, 2, 12, 5, 10, 14, 7 }, 
              { 1, 10, 13, 0, 6, 9, 8, 7, 4, 15, 14, 3, 11, 5, 2, 12 } }, 
            { { 7, 13, 14, 3, 0, 6, 9, 10, 1, 2, 8, 5, 11, 12, 4, 15 }, 
              { 13, 8, 11, 5, 6, 15, 0, 3, 4, 7, 2, 12, 1, 10, 14, 9 }, 
              { 10, 6, 9, 0, 12, 11, 7, 13, 15, 1, 3, 14, 5, 2, 8, 4 }, 
              { 3, 15, 0, 6, 10, 1, 13, 8, 9, 4, 5, 11, 12, 7, 2, 14 } }, 
            { { 2, 12, 4, 1, 7, 10, 11, 6, 8, 5, 3, 15, 13, 0, 14, 9 }, 
              { 14, 11, 2, 12, 4, 7, 13, 1, 5, 0, 15, 10, 3, 9, 8, 6 }, 
              { 4, 2, 1, 11, 10, 13, 7, 8, 15, 9, 12, 5, 6, 3, 0, 14 }, 
              { 11, 8, 12, 7, 1, 14, 2, 13, 6, 15, 0, 9, 10, 4, 5, 3 } }, 
            { { 12, 1, 10, 15, 9, 2, 6, 8, 0, 13, 3, 4, 14, 7, 5, 11 }, 
              { 10, 15, 4, 2, 7, 12, 9, 5, 6, 1, 13, 14, 0, 11, 3, 8 }, 
              { 9, 14, 15, 5, 2, 8, 12, 3, 7, 0, 4, 10, 1, 13, 11, 6 }, 
              { 4, 3, 2, 12, 9, 5, 15, 10, 11, 14, 1, 7, 6, 0, 8, 13 } }, 
            { { 4, 11, 2, 14, 15, 0, 8, 13, 3, 12, 9, 7, 5, 10, 6, 1 }, 
              { 13, 0, 11, 7, 4, 9, 1, 10, 14, 3, 5, 12, 2, 15, 8, 6 }, 
              { 1, 4, 11, 13, 12, 3, 7, 14, 10, 15, 6, 8, 0, 5, 9, 2 }, 
              { 6, 11, 13, 8, 1, 4, 10, 7, 9, 5, 0, 15, 14, 2, 3, 12 } }, 
            { { 13, 2, 8, 4, 6, 15, 11, 1, 10, 9, 3, 14, 5, 0, 12, 7 }, 
              { 1, 15, 13, 8, 10, 3, 7, 4, 12, 5, 6, 11, 0, 14, 9, 2 }, 
              { 7, 11, 4, 1, 9, 12, 14, 2, 0, 6, 10, 13, 15, 3, 5, 8 }, 
              { 2, 1, 14, 7, 4, 10, 8, 13, 15, 12, 9, 0, 3, 5, 6, 11 } } 
        }; 
        
        // Array to store the number of rotations that are to be done on each round
        int[] shiftBits = { 1, 1, 2, 2, 2, 2, 2, 2, 1, 2, 2, 2, 2, 2, 2, 1 }; 

        public static String ASCIItoHEX(String ascii)
        {
            String hex = "";
            for (int i = 0; i < ascii.length(); i++) 
            {
                char ch = ascii.charAt(i);
                int in = (int)ch;
                String part = Integer.toHexString(in);
                hex += part;
            }
            return hex;
        }
        
        public static String hexToASCII(String hex) 
        { 
            String ascii = ""; 
            for (int i = 0; i < hex.length(); i += 2) 
            { 
                String part = hex.substring(i, i + 2); 
                char ch = (char)Integer.parseInt(part, 16);
                ascii = ascii + ch; 
            } 
            return ascii; 
        }
        
        String hextoBin(String input) 
        { 
            int n = input.length() * 4; 
            input = Long.toBinaryString(Long.parseUnsignedLong(input, 16)); 
            while (input.length() < n) 
                input = "0" + input; 
            return input; 
        } 
   
        String binToHex(String input) 
        { 
            int n = (int)input.length() / 4; 
            input = Long.toHexString(Long.parseUnsignedLong(input, 2)); 
            while (input.length() < n) 
                input = "0" + input; 
            return input; 
        } 
        
        //Add zeroes at end of hex string to convert length into multiple of 16 for block-size
        public static String AddZero(String HexadecimalValue)
        {
        for(int i=1;i<=16;i++)
        {
            if(HexadecimalValue.length()%16!=0)
            {
                HexadecimalValue = HexadecimalValue+"0";
                count = i;
            }
        }
        return HexadecimalValue;
        }
        
        public static String RemoveZero(String LetsRemoveZeroes)
        {
        	for(int i=1; i<=count; i++)
        	{        		    		
        			LetsRemoveZeroes = LetsRemoveZeroes.substring(0, LetsRemoveZeroes.length()-1);	
        	}
        	return LetsRemoveZeroes;
        }
 
        // per-mutate input hexadecimal according to specified sequence 
        String permutation(int[] sequence, String input) 
        { 
            String output = ""; 
            input = hextoBin(input); 
            for (int i = 0; i < sequence.length; i++) 
                output += input.charAt(sequence[i] - 1); 
            output = binToHex(output); 
            return output; 
        } 
        
        // Simple xor function on two int arrays
        String xor(String a, String b) 
        { 
            long t_a = Long.parseUnsignedLong(a, 16); 
            long t_b = Long.parseUnsignedLong(b, 16); 
            t_a = t_a ^ t_b; 
            a = Long.toHexString(t_a); 
            while (a.length() < b.length()) 
                a = "0" + a; 
            return a; 
        } 
  
        String leftCircularShift(String input, int numBits) 
        { 
            int n = input.length() * 4; 
            int perm[] = new int[n]; 
            for (int i = 0; i < n - 1; i++) 
                perm[i] = (i + 2); 
            perm[n - 1] = 1; 
            while (numBits-- > 0) 
                input = permutation(perm, input); 
            return input; 
        } 
  
        // preparing 16 keys for 16 rounds 
        String[] getKeys(String key) 
        { 
            String keys[] = new String[16];  
            key = permutation(PC1, key); 
            for (int i = 0; i < 16; i++) 
            { 
                key = leftCircularShift(key.substring(0, 7), shiftBits[i]) + leftCircularShift(key.substring(7, 14), shiftBits[i]); 
                keys[i] = permutation(PC2, key); 
            } 
            return keys; 
        } 
  
        String sBox(String input) 
        { 
            String output = ""; 
            input = hextoBin(input); 
            for (int i = 0; i < 48; i += 6) 
            { 
                String temp = input.substring(i, i + 6); 
                int num = i / 6; 
                int row = Integer.parseInt( 
                    temp.charAt(0) + "" + temp.charAt(5), 2); 
                int col = Integer.parseInt( 
                    temp.substring(1, 5), 2); 
                output += Integer.toHexString( 
                    sbox[num][row][col]); 
            } 
            return output; 
        } 
  
        String round(String input, String key, int num) 
        { 
            String left = input.substring(0, 8); 
            String temp = input.substring(8, 16); 
            String right = temp; 
            temp = permutation(EP, temp); 
            temp = xor(temp, key); 
            temp = sBox(temp); 
            temp = permutation(P, temp); 
            left = xor(left, temp); 
            return right + left; 
        } 
  
        String encrypt(String plainText, String key) 
        { 
            int i; 
            String keys[] = getKeys(key); 
            plainText = permutation(IP, plainText); 
            for (i = 0; i < 16; i++) 
            { 
                plainText = round(plainText, keys[i], i); 
            } 
            plainText = plainText.substring(8, 16) + plainText.substring(0, 8); 
            plainText = permutation(IP1, plainText); 
            return plainText; 
        } 
  
        String decrypt(String plainText, String key) 
        { 
            int i; 
            String keys[] = getKeys(key); 
            plainText = permutation(IP, plainText); 
            for (i = 15; i > -1; i--) 
            { 
                plainText = round(plainText, keys[i], 15 - i); 
            } 
            plainText = plainText.substring(8, 16) + plainText.substring(0, 8); 
            plainText = permutation(IP1, plainText); 
            return plainText; 
        } 
        
        //Creating random 16 digit hexadecimal key
        static String getAlphaNumericString(int n) 
        { 
            String AlphaNumericString = "ABCDEF" + "0123456789"; 
            StringBuilder sb = new StringBuilder(n); 
            for (int i = 0; i < n; i++) 
            { 
                int index = (int)(AlphaNumericString.length() * Math.random()); 
                sb.append(AlphaNumericString.charAt(index)); 
            } 
            return sb.toString(); 
        }
    } 	

    public static int[] bit_Msg(String msg)
    {
    	int j=0;
    	int[] b_msg=new int[msg.length()*8];
    	for(int i=0;i<msg.length();i++)
    	{
    		int x=msg.charAt(i);
    		String x_s=Integer.toBinaryString(x);
    		while(x_s.length()!=8)
    		{
    			x_s='0'+x_s;
    		}
    		for(int i1=0;i1<8;i1++) 
    		{
    			b_msg[j] = Integer.parseInt(String.valueOf(x_s.charAt(i1)));
    			j++;
    		}
    	}
    	return b_msg;
    }

    public static BufferedImage Read_Image_File(String COVERIMAGEFILE)
    {
    	BufferedImage The_Image = null;
    	File p = new File (COVERIMAGEFILE);
    	try
    	{
    		The_Image = ImageIO.read(p);
    	}
    	catch (IOException e)
    	{
    		e.printStackTrace();
    		System.exit(1);
    	}
    	return The_Image;
    }

    public static void Hide_The_Message (int[] bits, BufferedImage The_Image) throws Exception
    {
    	File f = new File (CipherImagePathToSave);
    	BufferedImage sten_img=null;
    	int bit_l=bits.length/8;
    	int[] bl_msg=new int[8];
    	String bl_s=Integer.toBinaryString(bit_l);
    	while(bl_s.length()!=8)
    	{
    		bl_s='0'+bl_s;
    	}
    	for(int i1=0;i1<8;i1++) 
    	{
    		bl_msg[i1] = Integer.parseInt(String.valueOf(bl_s.charAt(i1)));
    	}
    	int j=0;
    	int b=0;
    	int Current_Bit_Entry=8;

    	for (int x = 0; x < The_Image.getWidth(); x++)
    	{
    		for ( int y = 0; y < The_Image.getHeight(); y++)
    		{
    			if(x==0&&y<8)
    			{
    				int currentPixel = The_Image.getRGB(x, y);	
    				int ori=currentPixel;
    				int red = currentPixel>>16;
    				red = red & 255;
    				int green = currentPixel>>8;
    				green = green & 255;
    				int blue = currentPixel;
    				blue = blue & 255;
    				String x_s=Integer.toBinaryString(blue);
    				String sten_s=x_s.substring(0, x_s.length()-1);
    				sten_s=sten_s+Integer.toString(bl_msg[b]);
    				int temp=Integer.parseInt(sten_s,2);
    				int s_pixel=Integer.parseInt(sten_s, 2);
    				int a=255;
    				int rgb = (a<<24) | (red<<16) | (green<<8) | s_pixel;
    				The_Image.setRGB(x, y, rgb);
    				ImageIO.write(The_Image, "png", f);
    				b++;
    			}
    			else if (Current_Bit_Entry < bits.length+8 )
    			{
    				int currentPixel = The_Image.getRGB(x, y);	
    				int ori=currentPixel;
    				int red = currentPixel>>16;
    				red = red & 255;
    				int green = currentPixel>>8;
    				green = green & 255;
    				int blue = currentPixel;
    				blue = blue & 255;
    				String x_s=Integer.toBinaryString(blue);
    				String sten_s=x_s.substring(0, x_s.length()-1);
    				sten_s=sten_s+Integer.toString(bits[j]);
    				j++;
    				int temp=Integer.parseInt(sten_s,2);
    				int s_pixel=Integer.parseInt(sten_s, 2);
    				int a=255;
    				int rgb = (a<<24) | (red<<16) | (green<<8) | s_pixel;
    				The_Image.setRGB(x, y, rgb);
    				ImageIO.write(The_Image, "png", f);
    				Current_Bit_Entry++;	
    			}
    		}
    	}
    }

	static String DECODEDMESSAGEFILE;
	public static String b_msg="";
	public static int len = 0;

	public static BufferedImage Read_Image_File_1(String COVERIMAGEFILE)
	{
		BufferedImage The_Image = null;
		File p = new File (COVERIMAGEFILE);
		try
		{
			The_Image = ImageIO.read(p);
		}
		catch (IOException e)
		{
			e.printStackTrace();
			System.exit(1);
		}
		return The_Image;
	}

	public static void Decode_The_Message (BufferedImage Y_Image) throws Exception
	{
		int j=0;
		int Current_Bit_Entry=0;
		String bx_msg="";
		for (int x = 0; x < Y_Image.getWidth(); x++)
		{
			for ( int y = 0; y < Y_Image.getHeight(); y++)
			{
				if(x==0&&y<8)
				{
					int currentPixel = Y_Image.getRGB(x, y);	
					int red = currentPixel>>16;
					red = red & 255;
					int green = currentPixel>>8;
	         		green = green & 255;
	         		int blue = currentPixel;
	         		blue = blue & 255;
	         		String x_s=Integer.toBinaryString(blue);
	         		bx_msg+=x_s.charAt(x_s.length()-1);
	         		len=Integer.parseInt(bx_msg,2);
				}
				else if(Current_Bit_Entry<len*8)
				{
					int currentPixel = Y_Image.getRGB(x, y);	
					int red = currentPixel>>16;
					red = red & 255;
					int green = currentPixel>>8;
    				green = green & 255;
    				int blue = currentPixel;
    				blue = blue & 255;
    				String x_s=Integer.toBinaryString(blue);
    				b_msg+=x_s.charAt(x_s.length()-1);
    				Current_Bit_Entry++;	
				}
			}
		}
	}
	
	//AES
	
	static Cipher cipher1;
	public static String encrypt(String plainText, SecretKey secretKey)throws Exception //function to encrypt the plaintext
	{
		byte[] plainTextByte = plainText.getBytes();
		cipher1.init(Cipher.ENCRYPT_MODE, secretKey);
		byte[] encryptedByte = cipher1.doFinal(plainTextByte);
		Base64.Encoder encoder = Base64.getEncoder();
		String encryptedText = encoder.encodeToString(encryptedByte);
		return encryptedText;
	}
	
	public static String decrypt(String encryptedText, SecretKey secretKey)throws Exception //function to decrypt the ciphertext
	{
		Base64.Decoder decoder = Base64.getDecoder();
		byte[] encryptedTextByte = decoder.decode(encryptedText);
		cipher1.init(Cipher.DECRYPT_MODE, secretKey);
		byte[] decryptedByte = cipher1.doFinal(encryptedTextByte);
		String decryptedText = new String(decryptedByte);
		return decryptedText;
	}
	
    void RunningTheCompleteEncryptFunction() throws Exception
    {
    	try 
    	{ 
	        BufferedWriter out = new BufferedWriter(new FileWriter(CipherFilePathToSave,true));
	    }
	    catch(IOException e) 
    	{
	    	System.out.println("error occured");
	    	e.printStackTrace();
	    }
    	String Inside_File_To_Be_Encrypted = TextFromPlaintextFile;
    	System.out.println("\nPlaintext to be encrypted : \n" + TextFromPlaintextFile + "\n\n");
        int leng= Inside_File_To_Be_Encrypted.length();
		if(leng%3==1)
		{
			leng=leng+2;
			Inside_File_To_Be_Encrypted += "  ";
		}
		else if(leng%3==2)
		{
			leng=leng+1;
			Inside_File_To_Be_Encrypted += " ";
		}
		else
		{
			leng=leng;
		}
		int n8=3;
		//Stores the array of string
		String[] newStr = new String [n8];
		int tempo=0;
		int len_size=leng/n8;
		for(int i=0;i<leng;i=i+len_size)
		{
			String part= Inside_File_To_Be_Encrypted.substring(i, i+len_size);  
           newStr[tempo] = part;
           tempo++;
		}
		String plaintext1,plaintext2,plaintext3;
		for(int i=0;i<newStr.length;i++)
		{
			if(i==0)
			{
				plaintext1=newStr[i];
			}
			if(i==1)
			{
				plaintext2=newStr[i];
			}
			if(i==2)
			{
				plaintext3=newStr[i];
			}
		}     
		System.out.println("Encryption starts here : \n");
		for(int x=0;x<newStr.length;x++)
		{
			if(x==0)
			{
				plaintext1=newStr[x];
				DES cipher = new DES();
				String key = cipher.getAlphaNumericString(16); 
				String plaintext = plaintext1;
				String hexvalue = cipher.ASCIItoHEX(plaintext);
				String NewHexValue = cipher.AddZero(hexvalue);
				String[] BlocksOfPlaintext = new String[100000];
				String[] EncryptedText = new String[100000];
				for(int i=0;i<NewHexValue.length();i=i+16)
				{
					BlocksOfPlaintext[i] = NewHexValue.substring(i, i+16);
					String text = BlocksOfPlaintext[i];
					EncryptedText[i] = cipher.encrypt(text, key);
				}
				String CombinedCipherText="";
				for(int i=0;i<NewHexValue.length();i=i+16)
				{
					CombinedCipherText = CombinedCipherText+EncryptedText[i];
				}
				try 
				{ 
					BufferedWriter out = new BufferedWriter(new FileWriter(CipherFilePathToSave,true));
					out.write(CombinedCipherText);
					out.write("\n");
					out.close();
				}
				catch(IOException e) 
				{
					System.out.println("error occured");
					e.printStackTrace();
				} 
				ConcatKeysAfterEncryption = ConcatKeysAfterEncryption + key + "\n";
				System.out.println("DES : \nKey : "+key+"\nEncrypted text : "+CombinedCipherText+"\n");
			}
			
			if(x==1)
			{
				plaintext2=newStr[x];
				KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
				keyGenerator.init(128);
				SecretKey secretKey = keyGenerator.generateKey();
				byte[] aesKey = secretKey.getEncoded();
				String aesKeyBase64 = Base64.getEncoder().encodeToString(aesKey);
				cipher1 = Cipher.getInstance("AES");
				String plainText,encryptedText,decryptedText;
				plainText = plaintext2;
				encryptedText = encrypt(plainText, secretKey);
				try 
				{ 
					BufferedWriter out = new BufferedWriter(new FileWriter(CipherFilePathToSave,true));
					out.write(encryptedText);
					out.write("\n");
					out.close();
		        }
				finally 
				{}
				ConcatKeysAfterEncryption = ConcatKeysAfterEncryption + aesKeyBase64 + "\n";
				System.out.println("AES : \nKey : "+aesKeyBase64+"\nEncrypted text : "+encryptedText+"\n");
			}
			
			//RC4
			
			if(x==2)
			{
				plaintext3=newStr[x];
			    byte[] plainBytes = plaintext3.getBytes(StandardCharsets.UTF_8);
			    // generate a rc4 key
			    KeyGenerator rc4KeyGenerator = KeyGenerator.getInstance("RC4");
			    SecretKey key = rc4KeyGenerator.generateKey();
			    byte[] rc4Key = key.getEncoded();
			    String rc4KeyBase64 = Base64.getEncoder().encodeToString(rc4Key);
			    // encryption 
			    Cipher cipherEnc = Cipher.getInstance("RC4");  // Transformation of the algorithm
			    cipherEnc.init(Cipher.ENCRYPT_MODE, key);
			    byte[] cipherBytes = cipherEnc.doFinal(plainBytes);
			    Base64.Encoder encoder = Base64.getEncoder();
			    String encryptedrc4Text = encoder.encodeToString(cipherBytes);
			    try 
			    { 
				    BufferedWriter out = new BufferedWriter(new FileWriter(CipherFilePathToSave,true));
				    out.write(encryptedrc4Text);
				    out.close();
				}
			    finally 
			    {}
			    ConcatKeysAfterEncryption = ConcatKeysAfterEncryption + rc4KeyBase64;
			    System.out.println("RC4 : \nKey : "+rc4KeyBase64+"\nEncrypted text : "+encryptedrc4Text+"\n");
			}
		}  
		
		String Inside_Message_File = ConcatKeysAfterEncryption;
		int[] bits=bit_Msg(Inside_Message_File);
		BufferedImage The_Image = Read_Image_File(PathOfUserImage);
		Hide_The_Message(bits, The_Image);
		BufferedReader in = new BufferedReader(new FileReader(CipherFilePathToSave)); 
		String mystring = in.readLine(); 
    }
    
    void RunningTheCompleteDecryptFunction() throws Exception
    {
    	System.out.println("\nDecryption starts here : \n");
    	String InputForDESDecryption = TextFromCiphertextFileDES;
    	String InputForAESDecryption = TextFromCiphertextFileAES;
    	String InputForRC4Decryption = TextFromCiphertextFileRC4;
    	
    	BufferedImage Y_Image=Read_Image_File_1(PathOfImageWithKey);
    	Decode_The_Message(Y_Image);
    	
    	String msg="";
    	for(int x=0;x<len*8;x=x+8)
    	{
    		String sub=b_msg.substring(x,x+8);	
    		int m=Integer.parseInt(sub,2);
    		char ch=(char) m;
    		msg+=ch;
    	}  
    	DECODEDMESSAGEFILE = msg;
    	BufferedReader bfr = new BufferedReader(new StringReader(DECODEDMESSAGEFILE));
    	String KeyForDESDecryption=bfr.readLine();
    	String KeyForAESDecryption=bfr.readLine();
    	String KeyForRC4Decryption=bfr.readLine();
	
    	//DES starts here
    	
    	DES cipher = new DES();
    	String[] BlocksOfCipherText = new String[100000];
    	String[] DecryptedText = new String[100000];
    	for(int k=0;k<InputForDESDecryption.length();k=k+16)
    	{
    		BlocksOfCipherText[k] = InputForDESDecryption.substring(k, k+16);
    		String text = BlocksOfCipherText[k];
    		DecryptedText[k] = cipher.decrypt(text, KeyForDESDecryption); 
    	}
    	String CombinedDecryptedText="";
    	for(int b=0;b<InputForDESDecryption.length();b=b+16)
    	{
    		CombinedDecryptedText = CombinedDecryptedText+DecryptedText[b];
    	}
    	CombinedDecryptedText = cipher.RemoveZero(CombinedDecryptedText);
    	String OriginalPlaintextRecovered = cipher.hexToASCII(CombinedDecryptedText);
    	System.out.println("DES : \nKey : "+KeyForDESDecryption+"\nDecrypted text : "+OriginalPlaintextRecovered+"\n");
		
    	//AES starts here
    	
    	byte[] decodedKey = Base64.getDecoder().decode(KeyForAESDecryption);
    	SecretKey secretKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");
    	cipher1 = Cipher.getInstance("AES");	
    	String decryptedText = decrypt(InputForAESDecryption,secretKey);
    	System.out.println("DES : \nKey : "+KeyForAESDecryption+"\nDecrypted text : "+decryptedText+"\n");

    	//RC4 starts here
    	
    	byte[] decodedrc4Key = Base64.getDecoder().decode(KeyForRC4Decryption);
    	SecretKey Key = new SecretKeySpec(decodedrc4Key, 0, decodedrc4Key.length, "RC4");        
    	Base64.Decoder decoder = Base64.getDecoder();
    	byte[] encryptedrc4TextByte = decoder.decode(InputForRC4Decryption);
    	Cipher cipherDec = Cipher.getInstance("RC4");  // Transformation of the algorithm
    	cipherDec.init(Cipher.DECRYPT_MODE,Key);
    	byte[] decryptBytes = cipherDec.doFinal(encryptedrc4TextByte);
    	System.out.println("DES : \nKey : "+KeyForRC4Decryption+"\nDecrypted text : "+new String(decryptBytes)+"\n");

    	try 
    	{ 
		 	BufferedWriter bfr1 = new BufferedWriter(new FileWriter(PlaintextFileRecoveredPathToSave,true));
		    bfr1.write(OriginalPlaintextRecovered);
		    bfr1.write(decryptedText);
		    bfr1.write(new String(decryptBytes));
		    bfr1.close(); 
    	}
    	catch(IOException e) 
    	{
			System.out.println("error occured");
		    e.printStackTrace();
    	} 
    	
    	try 
    	{ 
		 	BufferedReader bfr2 = new BufferedReader(new FileReader(PlaintextFileRecoveredPathToSave));
		 	String PlaintextRecoveredafterDecryption = bfr2.readLine();
		 	System.out.println("Plaintext Recovered after Decryption : \n"+PlaintextRecoveredafterDecryption);
    	}
    	catch(IOException e) 
    	{
			System.out.println("error occured");
		    e.printStackTrace();
    	}
    }
    
    public static void main(String args[]) 
    {
        f1.createGUI();
    }
}



