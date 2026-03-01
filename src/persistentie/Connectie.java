package persistentie;

import java.io.File;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

public class Connectie {

	private static final String MYSQL_DB = "ID430820_KingDomino";
	private static final String MYSQL_USER = "root";
	private static final String MYSQL_PWD = "admin";
	private static final int RANDOM_LOCAL_PORT = 3306;
	
	public static final String MYSQL_JDBC = "jdbc:mysql://localhost:" + RANDOM_LOCAL_PORT + "/" + MYSQL_DB + "?user="
	+ MYSQL_USER + "&password=" + MYSQL_PWD;
	
	private static final int MYSQL_PORT = 3306, SSH_PORT = 22;
	private static final String SSH_PRIVATE_KEY_PATH = "C:\\Users\\antho\\.ssh\\secondaryKeys\\secondaryKey";
	private static final String MYSQL_SERVER_URL = MYSQL_USER + ".db.webhosting.be";
	private final String SSH_SERVER_URL = "ssh090.webhosting.be", SSH_USER = "sdp1g55be";

	private int allocatedLocalPort = 0;

	private Session sshSession;

	public Connectie() {
		// createSshConnection();
	}

	public void closeConnection() {
		if (this.sshSession != null) {
			this.sshSession.disconnect();
		}
	}

	private void createSshConnection() {
		// Nieuwe ssh connectie opzetten indien er nog geen is
		if (this.sshSession == null) {
			try {
				JSch jsch = new JSch();
				this.sshSession = jsch.getSession(SSH_USER, SSH_SERVER_URL, SSH_PORT);
				// this.sshSession.setPassword(SSH_PWD);

				File file = new File(SSH_PRIVATE_KEY_PATH);
				jsch.addIdentity(file.getAbsolutePath());
				java.util.Properties config = new java.util.Properties();
				config.put("StrictHostKeyChecking", "no");
				config.put("ConnectionAttempts", "3");
				this.sshSession.setConfig(config);

				System.out.println("Establishing SSH connection using username and password...");
				// 10 sec timeout
				this.sshSession.connect(10000);
				System.out.println("SSH connection established!");
				System.out.println("  Details: ");
				System.out.println("    User: " + SSH_USER);
				System.out.println("    Server:Port: " + SSH_SERVER_URL + ":" + SSH_PORT);

				this.allocatedLocalPort = this.sshSession.setPortForwardingL(RANDOM_LOCAL_PORT, MYSQL_SERVER_URL,
						MYSQL_PORT);
				System.out.println(
						"  Forwarded port on " + MYSQL_SERVER_URL + ": " + allocatedLocalPort + " -> " + MYSQL_PORT);
			} catch (Exception e) {
				System.out.println("Could not establish SSH connection!");
				e.printStackTrace();
			}
		} else {
			if (!this.sshSession.isConnected()) {
				try {
					System.out.println("Reopening ssh connection...");
					this.sshSession.connect();
				} catch (Exception e) {
					System.err.print(e);
				}
			}
		}
	}
}