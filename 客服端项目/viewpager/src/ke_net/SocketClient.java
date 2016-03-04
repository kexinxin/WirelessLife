package ke_net;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;

import ke_base.BaseApplication;
import ke_bean.Disk;
import ke_bean.PcFile;



/**
 * socket访问pc公共类
 * 
 * 
 */
public class SocketClient {

	// 连接超时时间
	private static final int TIMEOUT_CONNECTION = 2000;

	// 重新操作次数
	private static final int RETRY_TIME = 3;

	/**
	 * 获取socket
	 * 
	 * @return
	 * @throws IOException
	 * @throws UnknownHostException
	 * @throws Exception
	 */
	private static Socket getSocket(BaseApplication application)
			throws UnknownHostException, IOException {
		Socket socket = new Socket(application.getIpAdress(), URL.PC_PORT);
		socket.setKeepAlive(true);
		socket.setSoTimeout(TIMEOUT_CONNECTION);
		return socket;
	}

	/**
	 * socket连接方法
	 * 
	 * @return
	 */
	private static String socketConnect(BaseApplication application,
			String method) {

		DataInputStream dis = null;

		DataOutputStream dos = null;

		Socket socket = null;

		String result = "";

		int time = 0;

		do {
			try {
				socket = getSocket(application);

				dos = new DataOutputStream(socket.getOutputStream());

				dos.writeUTF(method);

				dis = new DataInputStream(socket.getInputStream());

				result = dis.readUTF();
				break;

			} catch (IOException e) {
				time++;
				if (time < RETRY_TIME) {
					try {
						Thread.sleep(1000);

						continue;
					} catch (InterruptedException e1) {
					}
				}
				e.printStackTrace();
				 ;
			} finally {
				try {
					if (dis != null) {
						dis.close();
					}
					if (dos != null) {
						dos.close();
					}
					if (socket != null) {
						socket.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
					;
				}

			}
		} while (time < RETRY_TIME);

		return result;
	}

	// 获取盘符信息
	public static List<Disk> getDiskInfo(BaseApplication application)
			 {
		return Disk.parse(socketConnect(application, URL.GET_DISK));
	}

	// 获取盘符下的文件信息
	public static List<PcFile> getFileInfo(BaseApplication application,
			String diskPath) {
		return PcFile.parse(socketConnect(application, diskPath));
	}

	
}
