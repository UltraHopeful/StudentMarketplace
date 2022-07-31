package ca.dal.marketplace.service;

import ca.dal.marketplace.entity.UserSecurityToken;

public interface MailService {

    // send verification token to mail of user
    boolean sendTokenInMail(String userEmail,String emailSubject,String emailMessage);

}
