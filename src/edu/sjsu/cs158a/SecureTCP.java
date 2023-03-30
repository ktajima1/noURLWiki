package edu.sjsu.cs158a;
import picocli.CommandLine;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.concurrent.Callable;

import static java.awt.SystemColor.info;

public class SecureTCP implements Callable<Integer> {
    public static void main(String[] args) {
        System.exit(new CommandLine(new SecureTCP()).execute(args));
    }

    @CommandLine.Parameters(paramLabel = "host")
    String host;

    @CommandLine.Parameters(paramLabel = "port")
    int port;

    @CommandLine.Option(names = { "--useTLS"})
    boolean useTLS;

    @Override
    public Integer call() throws Exception {
        Socket s;
        if (useTLS) { //the useTLS flag determines whether to open a general socket or a SSL socket (secure socket)
            System.out.printf("connecting to %s:%d with TLS\n", host, port);
            s = SSLSocketFactory.getDefault().createSocket(host, port);
        } else {
            System.out.printf("connecting to %s:%d raw\n", host, port);
            s = new Socket(host, port);
        }
        System.out.printf("connected with %s\n", s);
        try (s; var br = new BufferedReader(new InputStreamReader(System.in))) {
            String line;
            var os = s.getOutputStream();
            while ((line = br.readLine()).length() > 0) {
                os.write(line.getBytes());
                os.write("\r\n".getBytes());
            }
            os.write("\r\n".getBytes());
            //shuts down the outputstream of a socket so that the socket can only receive inputs from the server; signals to server
            //that the outputstream is closed and that the client will not be sending anything
            s.shutdownOutput();
            //reads the bytes from an inputstream and loops it to the outputstream
            s.getInputStream().transferTo(System.out);
        }
        return 0;
    }
}