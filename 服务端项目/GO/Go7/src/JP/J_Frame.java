package JP;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
import javax.swing.*;



import ke_server.ConnectServer;

public class J_Frame extends JFrame {

	/**
	 * 
	 */

	private static final long serialVersionUID = 1L;
	public static JFileChooser j_fd; // 文件选择
	public static InetAddress j_addr; // 获取本机IP地址
	public static int j_colorModel = 0; // 颜色模式编号,每个编号对应一个皮肤颜色
	public static J_Frame j_Frame1; // 主界面
	public static Container j_panel; // 主界面面板
	public static int J_FrameModel = 0;

	public static JLabel j_label1; // 标签:“本机IP地址”
	public static JLabel j_label2; // 本机IP地址
	public static JLabel j_label3; // 标签：“输入端口号”
	public static JLabel j_label4; // 标签：“连接成功！”
	public static JLabel j_label5; // 标签：“聊天室”

	public static JLabel j_label_null1; // 用作填充空间的标签

	public static JTextField j_textField1; // 文本框，用于接受输入的端口号
	public static JTextField j_textField2; //

	public static JTextArea j_textArea1; // 用于“剪切”界面
	public static JTextArea j_textArea2; // 用于“局部通信”界面

	public static JButton j_button1; // 主界面“退出”按钮
	public static JButton j_button2; // 主界面“开启”按钮
	public static JButton j_button3; // 第二界面“上传”按钮
	public static JButton j_button4; // 第二界面“剪切”按钮
	public static JButton j_button5; // 第二界面“局部通信”按钮
	public static JButton j_button6; // 第二界面“ ”按钮
	public static JButton j_button7; // “剪切”界面“发送”按钮
	public static JButton j_button8; // “聊天室”界面“发送”按钮

	public static GridBagLayout layout; // 所有界面通用的网格包布局
	public static GridBagConstraints constraints; // 网格包布局约束

	public static String ke_path;
	public static String ke_path_name;
	static ConnectServer ke_connectServer;

	public J_Frame() throws UnknownHostException {
		// 主窗体初始化

		setSize(300, 450); // 设置窗体的大小
		setLocationRelativeTo(null); // 默认窗体的位置在屏幕中心
		setTitle("Go7");
		setAutoRequestFocus(false);
		this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		init();
	}

	private static void init() throws UnknownHostException {

		j_fd = new JFileChooser();

		// ////////////////////////////////////\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
		j_label_null1 = new JLabel();

		// ////////////////////////////////////\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\

		// ///////////////////////////////////\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
		j_label1 = new JLabel("本机IP地址:");
		j_label1.setFont(new Font("Serif", Font.PLAIN, 26));
		j_label1.setForeground(new Color(240, 240, 240));

		j_addr = InetAddress.getLocalHost(); // 获取本机IP地址
		j_label2 = new JLabel("                       "
				+ j_addr.getHostAddress().toString());
		j_label2.setFont(new Font("Serif", Font.PLAIN, 24));
		j_label2.setForeground(new Color(240, 240, 240));

		j_label3 = new JLabel("输入端口号:");
		j_label3.setFont(new Font("Serif", Font.PLAIN, 24));

		j_label4 = new JLabel("     连接成功！");
		j_label4.setFont(new Font("Serif", Font.PLAIN, 36));
		j_label4.setForeground(new Color(255, 255, 255));

		j_label5 = new JLabel("                聊天室");
		j_label5.setFont(new Font("Serif", Font.BOLD, 25));
		j_label5.setForeground(new Color(255, 255, 255));

		// ///////////////////////////////////\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\

		// ///////////////////////////////////\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
		j_button1 = new JButton("退出");
		j_button1.setBackground(new Color(0, 100, 100));
		j_button1.setForeground(new Color(240, 240, 240));
		j_button1.setFont(new Font("Serif", Font.PLAIN, 24));

		j_button2 = new JButton("开启");
		j_button2.setBackground(new Color(0, 100, 100));
		j_button2.setForeground(new Color(240, 240, 240));
		j_button2.setFont(new Font("Serif", Font.PLAIN, 24));

		j_button3 = new JButton("上传");
		j_button3.setFont(new Font("Serif", Font.PLAIN, 36));
		j_button3.setBackground(new Color(0, 100, 100));

		j_button4 = new JButton("剪切");
		j_button4.setFont(new Font("Serif", Font.PLAIN, 36));
		j_button4.setBackground(new Color(0, 100, 100));

		j_button5 = new JButton("通信");
		j_button5.setFont(new Font("Serif", Font.PLAIN, 36));
		j_button5.setBackground(new Color(0, 100, 100));

		j_button6 = new JButton("  ");
		j_button6.setFont(new Font("Serif", Font.PLAIN, 36));
		j_button6.setBackground(new Color(0, 100, 100));

		j_button7 = new JButton("发     送");
		j_button7.setFont(new Font("Serif", Font.BOLD, 24));
		j_button7.setBackground(new Color(0, 100, 100));

		j_button8 = new JButton("发送");
		j_button8.setFont(new Font("Serif", Font.BOLD, 18));
		j_button8.setBackground(new Color(0, 100, 100));
		// ////////////////////////////////////\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\

		// ////////////////////////////////////\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
		j_textField1 = new JTextField();
		j_textField1.setFont(new Font("Serif", Font.PLAIN, 30));
		j_textField1.setBackground(Color.LIGHT_GRAY);

		j_textField2 = new JTextField();
		j_textField2.setFont(new Font("Serif", Font.PLAIN, 20));
		j_textField2.setBackground(Color.LIGHT_GRAY);

		j_textArea1 = new JTextArea();
		j_textArea2 = new JTextArea();
		// ////////////////////////////////////\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\

		layout = new GridBagLayout();
		constraints = new GridBagConstraints();
	}

	public static void j_UI1() {

		J_FrameModel = 1;

		j_panel.removeAll();
		j_panel.setLayout(layout);
		j_panel.setBackground(new Color(100, 100, 100));

		j_panel.add(j_label1);
		j_panel.add(j_label2);
		j_panel.add(j_label_null1);
		j_panel.add(j_button1);
		j_panel.add(j_button2);

		constraints.insets = new Insets(5, 5, 5, 5);
		constraints.fill = GridBagConstraints.BOTH;
		constraints.weightx = 6;
		constraints.weighty = 1;
		constraints.gridwidth = GridBagConstraints.REMAINDER;
		layout.setConstraints(j_label1, constraints);

		constraints.weightx = 6;
		constraints.weighty = 1;
		constraints.gridwidth = GridBagConstraints.REMAINDER;
		layout.setConstraints(j_label2, constraints);

		constraints.weightx = 6;
		constraints.weighty = 10;
		constraints.gridwidth = GridBagConstraints.REMAINDER;
		layout.setConstraints(j_label_null1, constraints);

		constraints.weightx = 3;
		constraints.weighty = 2;
		constraints.gridwidth = 1;
		layout.setConstraints(j_button1, constraints);

		constraints.weightx = 3;
		constraints.weighty = 2;
		constraints.gridwidth = GridBagConstraints.REMAINDER;
		layout.setConstraints(j_button2, constraints);
		j_Frame1.repaint(); // 刷新界面
		j_Frame1.setVisible(true); // 显示窗体

		// 退出按钮的监听事件，鼠标点击时关闭窗体
	}

	public static void j_UI2() {

		try {
			
			ke_connectServer = new ConnectServer();
			ke_connectServer.service();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		J_FrameModel = 2;

		j_panel.removeAll(); // 移除面板中的所有组件

		j_panel.setLayout(layout);
		j_panel.add(j_label4);
		j_panel.add(j_button3);
		j_panel.add(j_button4);
		j_panel.add(j_button5);
		j_panel.add(j_button6);

		constraints.insets = new Insets(10, 10, 10, 10);
		constraints.fill = GridBagConstraints.BOTH;

		constraints.weightx = 4;
		constraints.weighty = 1;
		constraints.gridwidth = GridBagConstraints.REMAINDER;
		layout.setConstraints(j_label4, constraints);

		constraints.weightx = 2;
		constraints.weighty = 2;
		constraints.gridwidth = 1;
		layout.setConstraints(j_button3, constraints);

		constraints.weightx = 2;
		constraints.weighty = 2;
		constraints.gridwidth = GridBagConstraints.REMAINDER;
		layout.setConstraints(j_button4, constraints);

		constraints.weightx = 2;
		constraints.weighty = 2;
		constraints.gridwidth = 1;
		layout.setConstraints(j_button5, constraints);

		constraints.weightx = 2;
		constraints.weighty = 2;
		constraints.gridwidth = GridBagConstraints.REMAINDER;
		layout.setConstraints(j_button6, constraints);

		j_Frame1.repaint(); // 刷新界面
		// j_Frame1.validate();
		// j_Frame1.setVisible(false);
		j_Frame1.setVisible(true); // 刷新界面

	}

	public static void j_UI3() {

		J_FrameModel = 3;

		j_panel.removeAll();

		j_panel.setLayout(layout);

		j_panel.add(j_textArea1);
		j_panel.add(j_button7);

		constraints.insets = new Insets(3, 5, 3, 5);
		constraints.fill = GridBagConstraints.BOTH;
		constraints.weightx = 1;
		constraints.weighty = 20;
		constraints.gridwidth = GridBagConstraints.REMAINDER;
		layout.setConstraints(j_textArea1, constraints);

		constraints.weightx = 1;
		constraints.weighty = 1;
		constraints.gridwidth = GridBagConstraints.REMAINDER;
		layout.setConstraints(j_button7, constraints);

		j_Frame1.repaint(); // 刷新界面
		// j_Frame1.validate();
		// j_Frame1.setVisible(false);
		j_Frame1.setVisible(true); // 刷新界面

	}

	public static void j_UI4() {

		J_FrameModel = 4;

		j_panel.removeAll();

		j_panel.setLayout(layout);

		j_panel.add(j_label5);
		j_panel.add(j_textArea2);
		j_panel.add(j_textField2);
		j_panel.add(j_button8);

		constraints.fill = GridBagConstraints.BOTH;
		constraints.insets = new Insets(2, 4, 2, 4);

		constraints.weightx = 10;
		constraints.weighty = 1;
		constraints.gridwidth = GridBagConstraints.REMAINDER;
		layout.setConstraints(j_label5, constraints);

		constraints.weightx = 10;
		constraints.weighty = 9;
		constraints.gridwidth = GridBagConstraints.REMAINDER;
		layout.setConstraints(j_textArea2, constraints);

		constraints.weightx = 9;
		constraints.weighty = 1;
		constraints.gridwidth = 1;
		layout.setConstraints(j_textField2, constraints);

		constraints.weightx = 1;
		constraints.weighty = 1;
		constraints.gridwidth = GridBagConstraints.REMAINDER;
		layout.setConstraints(j_button8, constraints);

		j_Frame1.repaint();
		// j_Frame1.validate();
		// j_Frame1.setVisible(false);
		j_Frame1.setVisible(true); // 刷新界面
	}

	public static void main(String[] args) throws NullPointerException,
			UnknownHostException {
		// TODO Auto-generated method stub

		j_Frame1 = new J_Frame();
		j_panel = j_Frame1.getContentPane();
		j_UI1();
		j_button1.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				j_Frame1.dispose();
			}
		});

		// 开启按钮的监听事件，鼠标点击时开启热点，并跳转到第二界面

		j_button2.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				j_UI2();

			}
		});

		// 按钮“上传”的事件监听
		j_button3.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				int result = j_fd.showOpenDialog(j_fd);
				if (result == JFileChooser.APPROVE_OPTION) {

					ke_path = j_fd.getSelectedFile().getAbsolutePath();
					ke_path_name=j_fd.getSelectedFile().getName();
					System.out.println(ke_path_name);
					ke_connectServer.updateFile(ke_path,ke_path_name);

				} else {
					ke_path = null;

				}

			}
		});
		j_button4.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				j_UI3();
			}
		});
		j_button5.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				j_UI4();
			}
		});
		j_Frame1.addWindowListener(new WindowAdapter() {

			public void windowClosing(WindowEvent e) {
				// TODO Auto-generated method stub

				switch (J_FrameModel) {
				case 1:
					j_Frame1.dispose();
					break;
				case 2:
					j_UI1();
					break;
				case 3:
					j_UI2();
					break;
				case 4:
					j_UI2();
					break;
				}
			}

		});
		j_Frame1.setLocationRelativeTo(null);
		j_Frame1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	/////////////////////////////////////////////////////////////////////////////////////////////////////////
		//				屏幕监控
	/////////////////////////////////////////////////////////////////////////////////////////////////////////
		new Thread(new Runnable() {
			
			public void run() {
				// TODO Auto-generated method stub
				ServerSocket D_screen_server = null;
				Timer  timer_temp = null;
				Socket D_client_temp = null;
				try {
					
					D_screen_server = new ServerSocket(8888);
				}catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					System.out.println("D"+"IOException1");
				}catch(Exception e){
					e.printStackTrace();
				}
				while(true){
						
					try {
						final Socket D_client = D_screen_server.accept();
						final ObjectOutputStream D_output = new ObjectOutputStream(D_client.getOutputStream());
						System.out.println("done");
						D_client_temp = D_client;
						final Robot D_robot = new Robot(); 
						
						Timer timer =new Timer(1000, new  ActionListener() {
								
							public void actionPerformed(ActionEvent arg0){
								// TODO Auto-generated method stub
								BufferedImage D_bimage = D_robot.createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
								ByteArrayOutputStream D_out = new ByteArrayOutputStream();
									
								try {
									ImageIO.write(D_bimage, "JPG", D_out);
									byte[] D_temp = D_out.toByteArray();
									System.out.println(D_temp.length+"");
									
									D_output.writeObject(D_temp);
									D_output.flush();
									}catch(SocketException e1){
										
									}catch (IOException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									} catch(Exception e2){
										
									}
									
							}
						});
						timer.start();
						timer_temp = timer;
						InputStream in = D_client.getInputStream();
						if(in.read()==80){
							if(timer!=null)
								timer.stop();
							if(D_client_temp!=null)
								D_client_temp.close();
							if(in!=null)
								in.close();
						}
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
						if(timer_temp!=null)
						timer_temp.stop();
						try {
							if(D_client_temp!=null)
								D_client_temp.close();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
							
					}catch(Exception e9){
						e9.printStackTrace();
						if(timer_temp!=null)
						timer_temp.stop();
						try {
							if(D_client_temp!=null)
								D_client_temp.close();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
						
				}
			}
		}).start();
		

		////////////////////////////////////////////////////////////////////////////////////
		//							关机
		////////////////////////////////////////////////////////////////////////////////////
		new Thread(new Runnable() {
			
			public void run() {
				// TODO Auto-generated method stub
				try {
					ServerSocket server  =new ServerSocket(8889);
					Socket shutdown_client = server.accept();
					Runtime.getRuntime().exec("shutdown -p");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}).start();
		///////////////////////////////////////////////////////////////////////////////////////////
		//					键盘鼠标控制(单双击)
		///////////////////////////////////////////////////////////////////////////////////////////
		new Thread(new Runnable() {
			
			public void run() {
				// TODO Auto-generated method stub
				ServerSocket key_mouse_server = null;
				try {
					key_mouse_server = new ServerSocket(9000);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}catch(Exception e){
					e.printStackTrace();
				}
				while(true){
					Socket key_mouse_client = null;
					InputStream in = null;
					try {
						key_mouse_client = key_mouse_server.accept();
						int info = 0;
						in = key_mouse_client.getInputStream();
						Robot robot = new  Robot();
						while((info = in.read())!=-1){
							switch(info){

							case 205:
								robot.mousePress(InputEvent.BUTTON1_MASK);
								robot.mouseRelease(InputEvent.BUTTON1_MASK);
								break;
							case 206:
								robot.mousePress(InputEvent.BUTTON3_MASK);
								robot.mouseRelease(InputEvent.BUTTON3_MASK);
								break;
							default:
								robot.keyPress(info);
								robot.keyRelease(info);
								break;
							}
						}
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}catch(Exception e1){
							try {
								if(key_mouse_client!=null)
									key_mouse_client.close();
								if(in!=null)
									in.close();
							} catch (IOException e4) {
								// TODO Auto-generated catch block
								e4.printStackTrace();
							}
					}
				}
			}
		}).start();
		////////////////////////////////////////////////////////////////////////
		///////////////////鼠标移动/////////////////////////////////////////////
		new Thread(new Runnable() {
			
			public void run() {
				// TODO Auto-generated method stub
				ServerSocket mouse_move_server = null;
				try {
					mouse_move_server = new ServerSocket(9001);
					}catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}catch(Exception e){
						e.printStackTrace();
					}
					while(true){
						Socket mouse_move = null;
						DataInputStream in = null;
						try {
						mouse_move = mouse_move_server.accept();
						in = new  DataInputStream(mouse_move.getInputStream());
						Robot robot = new  Robot();
						Point mousePosition = MouseInfo.getPointerInfo().getLocation();
						int x = mousePosition.x;
						int y = mousePosition.y;
						Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
						final int screen_width = dim.width;
						final int screen_height = dim.height;
						int xd = -1,yd=-1;
						int count = 0;
						int temp= 0;
						while(true){
							try {
								temp = in.readInt();
								if(count==0){
									xd = temp;
									count++;
								}else if(count==1){
									yd = temp;
									System.err.println("( "+xd+" , "+yd+" )");
									if(x+xd>screen_width){
										x = screen_width;
									}
									else if(x+xd<0){
										x = 0;
									}
									if(y+yd>screen_width){
										y = screen_height;
									}	
									else if(y+yd<0){
										y = 0;
									}else {
										x = x+xd;
										y = y+yd;
									}
									robot.mouseMove(x, y);
									count=0;
								}
							}catch(EOFException e6){
								break;
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
								break;
							}
							
						}
					}catch(Exception e){
						try {
							if(mouse_move!=null)
								mouse_move.close();
							if(in!=null)
								in.close();
							
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}catch(Exception e2){}
						
					}
				}
				
			}
		}).start();
	}
	

}
