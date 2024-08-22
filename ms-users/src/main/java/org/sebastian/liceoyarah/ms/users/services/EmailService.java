package org.sebastian.liceoyarah.ms.users.services;

import java.io.File;

public interface EmailService {

    void sendMail( String[] toUser, String subject, String username, String password );
    void sendMailWithFile(String[] toUser, String subject, String message, File file);

}
