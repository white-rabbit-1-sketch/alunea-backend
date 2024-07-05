package me.silvermail.backend.controller;

import jakarta.validation.Valid;
import me.silvermail.backend.dto.controller.request.subscription.information.EmailInformationSubscriptionRequestDto;
import me.silvermail.backend.dto.controller.response.BaseResponseDto;
import me.silvermail.backend.entity.InformationSubscription;
import me.silvermail.backend.service.InformationSubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path="/information-subscription")
public class InformationSubscriptionController {
    @Autowired
    protected InformationSubscriptionService informationSubscriptionService;

    @PostMapping(path="/general-news")
    public BaseResponseDto createAnonymousEmailSubscriptionOnGeneralNews(
            @RequestBody @Valid EmailInformationSubscriptionRequestDto emailInformationSubscriptionRequestDto
    ) {
        InformationSubscription informationSubscription = informationSubscriptionService.getInformationSubscriptionByEmail(
                emailInformationSubscriptionRequestDto.getEmail()
        );

        if (informationSubscription == null) {
            informationSubscriptionService.createInformationSubscription(
                    emailInformationSubscriptionRequestDto.getEmail(),
                    emailInformationSubscriptionRequestDto.getLanguage()
            );
        }

        return new BaseResponseDto();
    }
}