package com.example.resumegeneratorbackend.controller;

import com.example.resumegeneratorbackend.model.ChangePassword;
import com.example.resumegeneratorbackend.model.EmailData;
import com.example.resumegeneratorbackend.model.Users;
import com.example.resumegeneratorbackend.repository.UsersRepository;
import com.example.resumegeneratorbackend.service.SendGridService;
import com.example.resumegeneratorbackend.service.UserService;
import com.example.resumegeneratorbackend.utility.RandomString;
import com.sendgrid.*;
import org.springframework.web.bind.annotation.*;


import org.springframework.beans.factory.annotation.Autowired;
import java.io.IOException;
@RestController
@CrossOrigin
@RequestMapping("api/email")
public class MailController {

    @Autowired
    SendGridService sendGridService;

    @Autowired
    UserService usersService;

    @RequestMapping(value = "/email/", method = RequestMethod.POST)
    public String index(@RequestBody EmailData emailData) {
        String response = sendGridService.sendMail(emailData);
        return response;
    }

    @PostMapping("/pass")
    public String ind(@RequestBody ChangePassword email) throws IOException {
        String generatedValue = RandomString.getAlphaNumericString(10);
        Users users = usersService.findByEmail(email.getToEmail());
        users.setPassword(generatedValue);
        usersService.updateUserInformatio(users, users.getId());
        Email from = new Email();
        from.setEmail("fredrik.lunde@frontedgeit.se");
        Email to = new Email();
        to.setEmail(email.getToEmail());
        String subject = "Forgot password From Front Edge";
        Content content = new Content("text/plain", "Here is your code to login in Front-Ege IT Resume Page : \n" +generatedValue);
        Mail mail = new Mail(from, subject, to, content);
        SendGrid sg = new SendGrid("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");
        Request request = new Request();
        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sg.api(request);
            System.out.println(response.getStatusCode());
            System.out.println(response.getBody());
            System.out.println(response.getHeaders());
        } catch (IOException ex) {
            throw ex;
        }
        return "success to send an email";
    }


}
