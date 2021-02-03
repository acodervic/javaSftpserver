package mrRobot;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;

import org.apache.sshd.server.SshServer;
import org.apache.sshd.server.auth.password.PasswordAuthenticator;
import org.apache.sshd.server.channel.ChannelSession;
import org.apache.sshd.server.command.Command;
import org.apache.sshd.server.config.keys.DefaultAuthorizedKeysAuthenticator;
import org.apache.sshd.server.keyprovider.SimpleGeneratorHostKeyProvider;
import org.apache.sshd.server.session.ServerSession;
import org.apache.sshd.server.shell.InteractiveProcessShellFactory;
import org.apache.sshd.server.shell.ShellFactory;
import org.apache.sshd.server.subsystem.SubsystemFactory;
import org.apache.sshd.sftp.server.SftpFileSystemAccessor;
import org.apache.sshd.sftp.server.SftpSubsystemFactory;

public class App {
    public static void main(String[] args) throws IOException {
        if (args.length < 4) {
            System.out.println("must need 3 options! like: user userMd5   listenaddress listenport");
            System.out.println("test  49ba59abbe56e057  0.0.0.0  7888  ");
            return;
        }
        // parse args
        String userString = args[0];
        String passMd5 = args[1];
        String address = args[2];
        Integer port = Integer.parseInt(args[3]);
        System.out.println("listeneHoss=" + address + "    port=" + port);
        //支持sftp
        SubsystemFactory factory = new SftpSubsystemFactory.Builder()
                .withFileSystemAccessor(new SftpFileSystemAccessor() {
                    @Override
                    public String toString() {
                        return SftpFileSystemAccessor.class.getSimpleName() + "[DEFAULT]";
                    }
                }).build();
        //============
        SshServer sshd = SshServer.setUpDefaultServer();
        sshd.setHost(address);
        sshd.setPort(port);
        sshd.setSubsystemFactories(Collections.singletonList(factory));
        sshd.setShellFactory(new ShellFactory() {
            @Override
            public Command createShell(ChannelSession channel) throws IOException {
                 return null;
            }
        });
        
 
		sshd.setShellFactory(new InteractiveProcessShellFactory());
        sshd.setKeyPairProvider(new SimpleGeneratorHostKeyProvider());

        sshd.setPasswordAuthenticator(new PasswordAuthenticator() {
            @Override
            public boolean authenticate(String username, String password, ServerSession session) {
                System.out.println("authen:  user=" + username + "  password=" + password);
                if (userString.equals(userString) && MD5(password).equals(passMd5)) {
                    return true;
                }
                return false;
            }
        });


        // use file ~/.ssh/authorized_keys
        sshd.setPublickeyAuthenticator(new DefaultAuthorizedKeysAuthenticator(false));
        sshd.start();

        try {
            // 等待Ctrlc结束
            CtrlCExit ctrlc = new CtrlCExit();
            Thread t = new Thread(ctrlc);
            t.setName("Ctrl C Thread");
            t.run();
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static String MD5(String input) {
        if (input == null || input.length() == 0) {
            return null;
        }
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(input.getBytes());
            byte[] byteArray = md5.digest();

            BigInteger bigInt = new BigInteger(1, byteArray);
            // 参数16表示16进制
            String result = bigInt.toString(16);
            // 不足32位高位补零
            while (result.length() < 32) {
                result = "0" + result;
            }
            return result;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }
}