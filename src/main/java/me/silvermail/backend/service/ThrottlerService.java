package me.silvermail.backend.service;

import me.silvermail.backend.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class ThrottlerService {
    protected static final String SENT_OUTBOUND_EXTERNAL_EMAIL_COUNT_KEY = "throttler:user:%s:email:external:outbound:sent:count";
    protected static final String USER_AUTH_TOKEN_CREATE_REQUEST_COUNT_KEY = "throttler:user:%s:request:security:user-auth-token:create:count";
    protected static final String RESET_USER_PASSWORD_EMAIL_SEND_REQUEST_COUNT_KEY = "throttler:user:%s:request:user:reset-user-password-email:send:count";
    protected static final String USER_EMAIL_VERIFICATION_EMAIL_SEND_REQUEST_COUNT_KEY = "throttler:user:%s:request:user:user-email-verification-email:send:count";
    protected static final String MAILBOX_EMAIL_VERIFICATION_EMAIL_SEND_REQUEST_COUNT_KEY = "throttler:user:%s:request:mailbox:mailbox-email-verification-email:send:count";

    @Autowired
    protected RedisService redisService;

    @Value("${throttler.email.external.outbound.sent.threshold}")
    protected int outboundExternalEmailSentThreshold;

    @Value("${throttler.email.external.outbound.sent.duration}")
    protected Duration outboundExternalEmailSentDuration;

    @Value("${throttler.request.security.user-auth-token.create.threshold}")
    protected int userAuthTokenCreateRequestThreshold;

    @Value("${throttler.request.security.user-auth-token.create.duration}")
    protected Duration userAuthTokenCreateRequestDuration;

    @Value("${throttler.request.user.reset-user-password-email.send.threshold}")
    protected int resetUserPasswordEmailSendRequestThreshold;

    @Value("${throttler.request.user.reset-user-password-email.send.duration}")
    protected Duration resetUserPasswordEmailSendRequestDuration;

    @Value("${throttler.request.user.user-email-verification-email.send.threshold}")
    protected int userEmailVerificationEmailSendRequestThreshold;

    @Value("${throttler.request.user.user-email-verification-email.send.duration}")
    protected Duration userEmailVerificationEmailSendRequestDuration;

    @Value("${throttler.request.mailbox.mailbox-email-verification-email.send.threshold}")
    protected int mailboxEmailVerificationEmailSendRequestThreshold;

    @Value("${throttler.request.mailbox.mailbox-email-verification-email.send.duration}")
    protected Duration mailboxEmailVerificationEmailSendRequestDuration;

    public boolean isSentOutboundExternalEmailThresholdExceeded(User user)  {
        boolean result = false;

        int count = redisService.getCounter(buildSentOutboundExternalEmailKey(user));
        if (count >= outboundExternalEmailSentThreshold) {
            result = true;
        }

        return result;
    }

    public boolean isUserAuthTokenCreateRequestThresholdExceeded(User user)  {
        boolean result = false;

        int count = redisService.getCounter(buildUserAuthTokenCreateRequestKey(user));
        if (count >= userAuthTokenCreateRequestThreshold) {
            result = true;
        }

        return result;
    }

    public boolean isResetUserPasswordEmailSendRequestThresholdExceeded(User user)  {
        boolean result = false;

        int count = redisService.getCounter(buildResetUserPasswordEmailSendRequestKey(user));
        if (count >= resetUserPasswordEmailSendRequestThreshold) {
            result = true;
        }

        return result;
    }

    public boolean isUserEmailVerificationEmailSendRequestThresholdExceeded(User user)  {
        boolean result = false;

        int count = redisService.getCounter(buildUserEmailVerificationEmailSendRequestKey(user));
        if (count >= userEmailVerificationEmailSendRequestThreshold) {
            result = true;
        }

        return result;
    }

    public boolean isMailboxEmailVerificationEmailSendRequestThresholdExceeded(User user)  {
        boolean result = false;

        int count = redisService.getCounter(buildMailboxEmailVerificationEmailSendRequestKey(user));
        if (count >= mailboxEmailVerificationEmailSendRequestThreshold) {
            result = true;
        }

        return result;
    }

    public void incrementSentOutboundExternalEmail(User user)  {
        redisService.increment(
                buildSentOutboundExternalEmailKey(user),
                1,
                outboundExternalEmailSentDuration
        );
    }

    public void incrementUserAuthTokenCreateRequest(User user)  {
        redisService.increment(
                buildUserAuthTokenCreateRequestKey(user),
                1,
                userAuthTokenCreateRequestDuration
        );
    }

    public void incrementResetUserPasswordEmailSendRequest(User user)  {
        redisService.increment(
                buildResetUserPasswordEmailSendRequestKey(user),
                1,
                resetUserPasswordEmailSendRequestDuration
        );
    }

    public void incrementUserEmailVerificationEmailSendRequest(User user)  {
        redisService.increment(
                buildUserEmailVerificationEmailSendRequestKey(user),
                1,
                userEmailVerificationEmailSendRequestDuration
        );
    }

    public void incrementMailboxEmailVerificationEmailSendRequest(User user)  {
        redisService.increment(
                buildMailboxEmailVerificationEmailSendRequestKey(user),
                1,
                mailboxEmailVerificationEmailSendRequestDuration
        );
    }

    protected String buildSentOutboundExternalEmailKey(User user) {
        return String.format(SENT_OUTBOUND_EXTERNAL_EMAIL_COUNT_KEY, user.getId());
    }

    protected String buildUserAuthTokenCreateRequestKey(User user) {
        return String.format(USER_AUTH_TOKEN_CREATE_REQUEST_COUNT_KEY, user.getId());
    }

    protected String buildResetUserPasswordEmailSendRequestKey(User user) {
        return String.format(RESET_USER_PASSWORD_EMAIL_SEND_REQUEST_COUNT_KEY, user.getId());
    }

    protected String buildUserEmailVerificationEmailSendRequestKey(User user) {
        return String.format(USER_EMAIL_VERIFICATION_EMAIL_SEND_REQUEST_COUNT_KEY, user.getId());
    }

    protected String buildMailboxEmailVerificationEmailSendRequestKey(User user) {
        return String.format(MAILBOX_EMAIL_VERIFICATION_EMAIL_SEND_REQUEST_COUNT_KEY, user.getId());
    }
}

