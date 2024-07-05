package me.silvermail.backend.service;

import me.silvermail.backend.entity.InformationSubscription;
import me.silvermail.backend.repository.InformationSubscriptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class InformationSubscriptionService {
    @Autowired
    protected InformationSubscriptionRepository informationSubscriptionRepository;

    public InformationSubscription getInformationSubscriptionByEmail(String email) {
        return informationSubscriptionRepository.findByEmail(email);
    }

    public InformationSubscription createInformationSubscription(String email, String language) {
        email = email.trim();

        InformationSubscription informationSubscription = new InformationSubscription();
        informationSubscription.setEmail(email);
        informationSubscription.setLanguage(language);
        informationSubscription.setCreateTime(new Date());

        informationSubscriptionRepository.save(informationSubscription);

        return informationSubscription;
    }
}
