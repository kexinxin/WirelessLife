package ke_server;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.ProgressMonitor;
import javax.swing.Timer;

import ke_thread.DownThread;
import ke_thread.ServerThread;


/**
 * 与客户端连接的查询server
 * 
 * 
 */
public class ConnectServer {

	private static ServerSocket serverSocket;

	private static ServerSocket downSocket;

	private static ServerSocket updateSocket;

	private static ServerSocket fileNameSeverSocket;

	Socket upsocket;
	Socket fieNamesocket;

	private static final int port = 8624;

	private static final int down_port = 8625;

	private static final int update_port = 2732;

	private static final int FileName_port = 2777;

	static String FilePath;


	private final JDialog barDialog = new JDialog();


	public ConnectServer() throws IOException {
		serverSocket = new ServerSocket(port);
		downSocket = new ServerSocket(down_port);
		updateSocket = new ServerSocket(update_port);
		// updateFileSocket=new ServerSocket(updateFile_port);
		fileNameSeverSocket = new ServerSocket(FileName_port);
		System.out.println("服务器连接启动.");
	}

	/**
	 * 开始服务
	 * 
	 * @throws IOException
	 */
	public void service() throws IOException {
		// 把电脑的文件系统发到手机端的socket
		new Thread(new Runnable() {

			public void run() {
				while (true) {
					System.out.println("查询服务器正在等待连接...");
					Socket socket;
					try {
						socket = serverSocket.accept();
						new Thread(new ServerThread(socket)).start();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
		// 把电脑文件发到手机端的线程
		new Thread(new Runnable() {

			public void run() {
				while (true) {
					System.out.println("下载服务器正在等待连接...");
					Socket socket;
					try {
						socket = downSocket.accept();
						new DownThread(socket).start();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
		// 从手机上传文件到电脑上的线程
		new Thread(new Runnable() {

			public void run() {
				// TODO Auto-generated method stub
				try {
					final ServerSocket server = new ServerSocket(33456);
					final ServerSocket serverPath = new ServerSocket(36666);
					Thread th = new Thread(new Runnable() {
						public void run() {
							while (true) {
								try {
									System.out.println("开端监听...");
									Socket socket = server.accept();
									Socket socketPath = serverPath.accept();
									System.out.println("有链接");
									receiveFilePath(socketPath);
									receiveFile(socket);
								} catch (Exception e) {
								}
							}
						}

					});

					th.run(); // 启动线程运行
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		}).start();

		new Thread(new Runnable() {
			public void run() {
				while (true) {

					System.out.println("从电脑上传到手机的服务器正在等待连接...");
					try {
						fieNamesocket = fileNameSeverSocket.accept();
						upsocket = updateSocket.accept();
						// upFiesocket=updateFileSocket.accept();
						System.out.println("链接成功");

					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();

	}

	// 获取文件
	public static void receiveFile(Socket socket) {

		byte[] inputByte = null;
		int length = 0;
		DataInputStream dis = null;
		FileOutputStream fos = null;

		try {
			try {

				dis = new DataInputStream(socket.getInputStream());
				fos = new FileOutputStream(new File("f:/" + FilePath));

				inputByte = new byte[1024];
				System.out.println("开端接管数据...");
				while ((length = dis.read(inputByte, 0, inputByte.length)) > 0) {
					System.out.println(length);
					fos.write(inputByte, 0, length);

					fos.flush();
				}
				System.out.println("完成接管");
			} finally {
				if (fos != null)
					fos.close();
				if (dis != null)
					dis.close();
				if (socket != null)
					socket.close();
			}
		} catch (Exception e) {

		}

	}

	// 获取文件路径
	private void receiveFilePath(Socket socketPath) throws IOException {
		// TODO 自动生成的方法存根
		BufferedReader br = new BufferedReader(new InputStreamReader(
				socketPath.getInputStream()));
		// 进行普通IO操作
		String line = br.readLine();
		FilePath = line;
		// 关闭输入流、socket
		br.close();

		socketPath.close();
	}

	// 从电脑的java文件工具中上传文件
	public void updateFile(final String path, final String name) {

		// new Thread() {
		// public void run() {
		// bar = new FileProgressBar();

		try {
			String content = name;
			OutputStream os = fieNamesocket.getOutputStream();
			os.write((content + "\n").getBytes("utf-8"));
			os.write((content + "\n").getBytes("utf-8"));

		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		// 关闭输出流，关闭Socket

		int answer = JOptionPane.showConfirmDialog(null, "确定上传");

		try {
			File file = new File(path);
			FileInputStream fis = null;
			DataOutputStream dos = null;
			dos = new DataOutputStream(upsocket.getOutputStream());
			byte[] sendBytes = null;
			int length = 0;
			fis = new FileInputStream(file);
			sendBytes = new byte[1024];
//			runBar(fis.available());

			
			while ((length = fis.read(sendBytes, 0, sendBytes.length)) > 0) {
				dos.write(sendBytes, 0, length);
				System.out.println(length);
				dos.flush();


			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		barDialog.setVisible(false);

		barDialog.dispose();// 关闭文件进度显示框

		JOptionPane.showMessageDialog(null, "文件接受完成!");
		// }
		// }.start();

	}

	
}
