package util;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.SendFailedException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
public class MailAccess
{
   private Session mailSession = null;
   public MailAccess()
   {
   }
   /**
    * @param name resource reference name for default mail session
    */
   public MailAccess(String name) throws Exception
   {
      mailSession = getMailSession(name);
   }
   public void send(String from, String to, String subject, String content)
      throws SendFailedException, MessagingException
   {
      Message mesg = new MimeMessage(mailSession);
      // from
      mesg.setFrom(new InternetAddress(from));
      // to (one recipient)
      InternetAddress recipients[] = new InternetAddress[1];
      recipients[0] = new InternetAddress(to);
      mesg.setRecipients(Message.RecipientType.TO, recipients);
      // subject
      mesg.setSubject(subject);
      // content
      mesg.setContent(content, "text/plain");
      Transport.send(mesg);
   }
   // for server that needs SMTP authentication
   public void send(String from, String to, String subject, String content,
         String smtp, String username, String password) throws SendFailedException,
         MessagingException
   {
      Message mesg = new MimeMessage(mailSession);
      // from
      mesg.setFrom(new InternetAddress(from));
      // to (one recipient)
      InternetAddress recipients[] = new InternetAddress[1];
      recipients[0] = new InternetAddress(to);
      mesg.setRecipients(Message.RecipientType.TO, recipients);
      // subject
      mesg.setSubject(subject);
      // content
      mesg.setContent(content, "text/plain");
      Transport tr = mailSession.getTransport();
      tr.connect(smtp, username, password);
      tr.sendMessage(mesg, mesg.getAllRecipients());
      tr.close();
   }
   private Session getMailSession(String name) throws Exception
   {
      Session session = null;
      try
      {
         Context ctx = new InitialContext();
         ctx = (Context)ctx.lookup("java:comp/env");
         session = (Session)ctx.lookup(name);
      }
      catch(Exception e)
      {
         throw new Exception("Error while getting mail session for JNDI name: "
               + name);
      }
      return session;
   }
}
