package fiap.wtu_ancora.service;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Email;
import com.sendgrid.helpers.mail.objects.Personalization;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EmailSender {

    // O MANEZAO QUE FICA PROCURANDO KEY EM PROJETO DOS OUTROS, ESSE PROJETO É DE ESTUDOS, E ESSA CHAVE É GRATUITA :)
    private static final String SDK = "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";
    private static final String TEMPLATE_ID = "d-4404f136d3ee4554840bbe81779e60eb";
    private static final ExecutorService executorService = Executors.newFixedThreadPool(2);

    public static CompletableFuture<Void> sendInviteEmailAsync(String userEmail, String eventTitle) {
        return CompletableFuture.runAsync(() -> {
            Email emailFrom = new Email("fiapjrv@gmail.com");
            Email emailTo = new Email(userEmail);

            Mail mail = new Mail();
            mail.setFrom(emailFrom);
            mail.setTemplateId(TEMPLATE_ID);

            Personalization personalization = new Personalization();
            personalization.addDynamicTemplateData("title", eventTitle);
            personalization.addTo(emailTo);
            mail.addPersonalization(personalization);

            SendGrid sendGrid = new SendGrid(SDK);
            Request request = new Request();

            try {
                request.setMethod(Method.POST);
                request.setEndpoint("mail/send");
                request.setBody(mail.build());
                Response response = sendGrid.api(request);
                System.out.println("Email enviado! Status code: " + response.getStatusCode());
            } catch (IOException e) {
                System.err.println("Erro ao enviar e-mail para: " + userEmail);
            }
        }, executorService);
    }

    public static void sendEmailsToMultipleRecipients(Set<String> emailList, String eventTitle) {
        emailList.forEach(email -> {
            sendInviteEmailAsync(email, eventTitle).thenRun(() ->
                    System.out.println("Envio de e-mail aysnc concluído para: " + email)
            );
        });
    }
}