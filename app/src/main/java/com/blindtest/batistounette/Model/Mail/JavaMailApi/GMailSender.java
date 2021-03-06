package com.blindtest.batistounette.Model.Mail.JavaMailApi;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.Security;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * Created by Batistounette on 07/06/2020.
 */

public class GMailSender extends javax.mail.Authenticator {

    private String mMailHost = "smtp.gmail.com";
    private String mUser;
    private String mPassword;
    private Session mSession;

    static {
        Security.addProvider(new JSSEProvider());
    }

    public GMailSender(String user, String password) {
        this.mUser = user;
        this.mPassword = password;

        Properties props = new Properties();
        props.setProperty("mail.transport.protocol", "smtp");
        props.setProperty("mail.host", mMailHost);
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class",
                "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.socketFactory.fallback", "false");
        props.setProperty("mail.smtp.quitwait", "false");

        mSession = Session.getDefaultInstance(props, this);
    }

    protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(mUser, mPassword);
    }

    public synchronized void sendMail(String subject, String body, String recipients) throws Exception {
        try{
            MimeMessage message = new MimeMessage(mSession);
            DataHandler handler = new DataHandler(new ByteArrayDataSource(body.getBytes(), "text/plain"));
            message.setFrom(new InternetAddress("Blind Test <sender>"));
            message.setSubject(subject);
            message.setDataHandler(handler);

            if (recipients.indexOf(',') > 0) {
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipients));
            }

            else {
                message.setRecipient(Message.RecipientType.TO, new InternetAddress(recipients));
            }

            Transport.send(message);
        }

        catch(Exception e){
            e.printStackTrace();
        }
    }

    public static class ByteArrayDataSource implements DataSource {
        private byte[] data;
        private String type;

        public ByteArrayDataSource(byte[] data, String type) {
            super();
            this.data = data;
            this.type = type;
        }

        public ByteArrayDataSource(byte[] data) {
            super();
            this.data = data;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getContentType() {
            if (type == null)
                return "application/octet-stream";
            else
                return type;
        }

        public InputStream getInputStream() throws IOException {
            return new ByteArrayInputStream(data);
        }

        public String getName() {
            return "ByteArrayDataSource";
        }

        public OutputStream getOutputStream() throws IOException {
            throw new IOException("Not Supported");
        }
    }
}
