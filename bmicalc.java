/*
	BMI Solver (with plotter)
	
	- Author: Renato Jr.
*/

package bmi.calculator;

import java.awt.AWTEvent;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

class Graph extends JPanel
{
	private float result;
	private double height_idx;
	private int weight_idx;
	
	private float weight, height;
	
	public Graph(int x)
	{
		setBounds(x-100,25,x+75,415);
		setBackground(Color.BLACK);
	}
	
	public void paint(Graphics g)
	{
		Graphics2D g2d = (Graphics2D)g;
		super.paintComponent(g2d);
		
		height_idx = 1.5;
		weight_idx = 40;
		
		g2d.setColor(Color.YELLOW);
		g2d.drawString("BMI: " + String.valueOf(result),8,18);
		
		g2d.setColor(Color.WHITE);
		g2d.drawLine(40,365,450,365); // weight
		g2d.drawLine(40,45,40,365); // height
		
		g2d.setColor(Color.CYAN);
		g2d.drawString("Weight (kg)",200,402); // weight indicator
		
		g2d.setColor(Color.PINK);
		for (int i = 0; i < 6; i++) {
			g2d.drawString(String.valueOf(height_idx).substring(0,3),19,(362-(i*60)-10));
			height_idx += 0.1;
		}
		
		g2d.setColor(Color.YELLOW);
		for (int i = 0; i < 13; i++) {
			g2d.drawString(String.valueOf(weight_idx),(40+((i*33)+2)),380);
			weight_idx += 10;
		}
		
		g2d.setColor(Color.GREEN);
		Polygon p_underweight = new Polygon();
		p_underweight.npoints = 4;
		p_underweight.xpoints[0] = 41;	p_underweight.xpoints[2] = 50;
		p_underweight.xpoints[1] = 165;	p_underweight.xpoints[3] = 41;
		p_underweight.ypoints[0] = 45;	p_underweight.ypoints[2] = 365;
		p_underweight.ypoints[1] = 45;	p_underweight.ypoints[3] = 365;
		g2d.fillPolygon(p_underweight);
		
		g2d.setColor(Color.YELLOW);
		Polygon p_normal = new Polygon();
		p_normal.npoints = 4;
		p_normal.xpoints[0] = 165;	p_normal.xpoints[2] = 100;
		p_normal.xpoints[1] = 250;	p_normal.xpoints[3] = 50;
		p_normal.ypoints[0] = 45;	p_normal.ypoints[2] = 365;
		p_normal.ypoints[1] = 45;	p_normal.ypoints[3] = 365;
		g2d.fillPolygon(p_normal);
		
		int offset1 = 85;
		g2d.setColor(Color.ORANGE);
		Polygon p_overweight = new Polygon();
		p_overweight.npoints = 4;
		p_overweight.xpoints[0] = offset1+165;	p_overweight.xpoints[2] = offset1+45;
		p_overweight.xpoints[1] = offset1+225;	p_overweight.xpoints[3] = offset1+15;
		p_overweight.ypoints[0] = 45;	p_overweight.ypoints[2] = 365;
		p_overweight.ypoints[1] = 45;	p_overweight.ypoints[3] = 365;
		g2d.fillPolygon(p_overweight);
		
		int offset2 = 85;
		g2d.setColor(Color.RED);
		Polygon p_obese = new Polygon();
		p_obese.npoints = 4;
		p_obese.xpoints[0] = offset2+225;	p_obese.xpoints[2] = offset2+365;
		p_obese.xpoints[1] = offset2+365;	p_obese.xpoints[3] = offset2+45;
		p_obese.ypoints[0] = 45;	p_obese.ypoints[2] = 365;
		p_obese.ypoints[1] = 45;	p_obese.ypoints[3] = 365;
		g2d.fillPolygon(p_obese);
		
		checkPointLocation(g2d);
		
		g2d.setColor(Color.WHITE);
		g2d.drawString("Underweight",50,38);
		g2d.drawString("Normal",185,38);
		g2d.drawString("Overweight",250,38);
		g2d.drawString("Obese",365,38);
		
		g2d.setColor(Color.ORANGE);
		g2d.rotate(-1.6,13,250);
		g2d.drawString("Height (m)",13,250); // height indicator
	}
	
	public void getResult(float result)
	{
		this.result = result;
		repaint();
	}
	
	public void getInfo(float w, float h)
	{
		weight = w;
		height = h;
		repaint();
	}
	
	private void checkPointLocation(Graphics2D g)
	{
		g.setColor(Color.WHITE);
		g.fillRect(35,365,5,5);
		repaint();
	}
	
}

public class Main
{

	public static JFrame window;
	public static JLabel title;
	public static float result;
	public static JTextField tf_weight, tf_height;
	
	private static Graph graph;
	
	public static void main(String[] args)
	{
		window = new JFrame("BMI Calculator");
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setSize(800,500);
		window.setResizable(false);
		window.setVisible(true);
		window.setLocationRelativeTo(null); // center to screen
		window.setLayout(null); // we want all object to freely move
		
		title = new JLabel("[ BMI Calculator ]");
		title.setBounds(95,15,title.getText().length()*10,25);
		window.add(title);
		
		converter();
		
		JSeparator sep = new JSeparator();
		sep.setBounds(50,150,210,5);
		window.add(sep);
		
		BMICalculator();
		
		JSeparator sep2 = new JSeparator();
		sep2.setBounds(50,260,210,5);
		window.add(sep2);
		
		DataLoader();
		
		graph = new Graph(window.getSize().width/2);
		window.getContentPane().add(graph);
		window.getContentPane().repaint();
	}
	
	public static void converter()
	{
		JLabel converter = new JLabel("Converter");
		converter.setBounds(115,50,title.getText().length()*10,25);
		window.add(converter);
		
		// lb -> kg
		JLabel name1 = new JLabel("lb");
		name1.setBounds(65,75,200,25);
		window.add(name1);
		
		JTextField tf_lb = new JTextField("0");
		tf_lb.setBounds(85,75,50,25);
		window.add(tf_lb);
		
		JLabel arrow1 = new JLabel("->");
		arrow1.setBounds(145,75,200,25);
		window.add(arrow1);
		
		JTextField tf_kg = new JTextField("0");
		tf_kg.setBounds(175,75,50,25);
		window.add(tf_kg);
		
		JLabel name2 = new JLabel("kg");
		name2.setBounds(230,75,200,25);
		window.add(name2);
		
		// cm -> m
		JLabel name3 = new JLabel("ft");
		name3.setBounds(65,100,200,25);
		window.add(name3);
		
		JTextField tf_ft = new JTextField("0");
		tf_ft.setBounds(85,100,50,25);
		window.add(tf_ft);
		
		JLabel arrow2 = new JLabel("->");
		arrow2.setBounds(145,100,200,25);
		window.add(arrow2);
		
		JTextField tf_m = new JTextField("0");
		tf_m.setBounds(175,100,50,25);
		window.add(tf_m);
		
		JLabel name4 = new JLabel("m");
		name4.setBounds(230,100,200,25);
		window.add(name4);
		
		// actions listeners
		tf_lb.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub
				try {
					if (e.getKeyCode() == KeyEvent.VK_ENTER)
					{
						if (tf_lb.getText().length() > 0) tf_kg.setText(String.valueOf(Double.parseDouble(tf_lb.getText()) * 0.454));
						if (tf_kg.getText().length() > 6) tf_kg.setText(tf_kg.getText().substring(0,6));
					}
				} catch (IllegalStateException ee) {
				}
			}
		});
		tf_kg.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub
				try {
					if (e.getKeyCode() == KeyEvent.VK_ENTER)
					{
						if (tf_kg.getText().length() > 0) tf_lb.setText(String.valueOf(Double.parseDouble(tf_kg.getText()) / 0.454));
						if (tf_lb.getText().length() > 6) tf_lb.setText(tf_lb.getText().substring(0,6));
					}
				} catch (IllegalStateException ee) {
				}
			}
		});
		
		tf_ft.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub
				try {
					if (e.getKeyCode() == KeyEvent.VK_ENTER)
					{
						if (tf_ft.getText().length() > 0) tf_m.setText(String.valueOf(Double.parseDouble(tf_ft.getText()) * 0.3048));
						if (tf_m.getText().length() > 6) tf_m.setText(tf_m.getText().substring(0,5));
					}
				} catch (IllegalStateException ee) {
				}
			}
		});
		tf_m.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub
				try {
					if (e.getKeyCode() == KeyEvent.VK_ENTER)
					{
						if (tf_m.getText().length() > 0) tf_ft.setText(String.valueOf(Double.parseDouble(tf_m.getText()) * 100));
						if (tf_ft.getText().length() > 6) tf_ft.setText(tf_ft.getText().substring(0,5));
					}
				} catch (IllegalStateException ee) {
				}
			}
		});
		
	}
	
	public static void BMICalculator()
	{
		JLabel name1 = new JLabel("Weight (kg): ");
		name1.setBounds(50,175,100,25);
		window.add(name1);
		
		tf_weight = new JTextField();
		tf_weight.setBounds(155,175,100,25);
		window.add(tf_weight);
		
		JLabel name2 = new JLabel("Height (m): ");
		name2.setBounds(50,205,100,25);
		window.add(name2);
		
		tf_height = new JTextField();
		tf_height.setBounds(155,205,100,25);
		window.add(tf_height);
		
		// actions
		Timer t = new Timer(2,new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				try
				{
					if (tf_weight.getText().length() > 6) {
						String t = tf_weight.getText();
						tf_weight.setText(t.substring(0,6));
					}
					
					if (tf_height.getText().length() > 6) {
						String t = tf_height.getText();
						tf_height.setText(t.substring(0,6));
					}
				}
				catch (IllegalStateException ee) {  }
			}
		});
		
		t.start();
	}
	
	public static void DataLoader()
	{
		JButton calculator = new JButton("Calculate BMI");
		calculator.setBounds(90,300,calculator.getText().length()*10,25);
		window.add(calculator);
		
		JButton loader = new JButton("Load Data (CSV File)");
		loader.setBounds(55,335,loader.getText().length()*10,25);
		window.add(loader);
		
		JLabel error = new JLabel("");
		error.setForeground(Color.RED);
		error.setBounds(75,385,300,25);
		window.add(error);
		
		// actions
		calculator.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if (tf_height.getText().length() <= 0 || tf_weight.getText().length() <= 0)
					error.setText("Please provide an input!");
				else {
					if (error.getText().length() > 0) error.setText(" ");
					result = (float) (Float.parseFloat(tf_weight.getText()) / Math.pow(Float.parseFloat(tf_height.getText()),2));
					graph.getResult(result);
					graph.getInfo(Float.parseFloat(tf_weight.getText()),Float.parseFloat(tf_height.getText()));
				}
			}
		});
		
		Timer t = new Timer(2, new ActionListener() {
			int timer = 0;
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if (timer < 3000) timer++;
				if (timer >= 3000) { error.setText(" "); timer = 0; }
			}
		});
		
		t.start();
	}

}
