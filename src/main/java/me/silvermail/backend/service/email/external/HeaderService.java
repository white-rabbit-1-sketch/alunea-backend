package me.silvermail.backend.service.email.external;

import jakarta.mail.Header;
import jakarta.mail.MessagingException;
import me.silvermail.backend.model.email.external.ExternalEmail;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.List;

@Service
public class HeaderService {
    protected static final List<String> HEADER_WHITELIST = List.of(
            "MIME-Version",
            "From",
            "To",
            "Cc",
            "Subject",
            "Date",
            "Message-ID",
            "References",
            "In-Reply-To",
            "List-Unsubscribe",
            "List-Unsubscribe-Post",
            "Content-Type",
            "Mime-Version",
            "Content-Disposition",
            "Content-Transfer-Encoding"
    );

    public void clearHeaders(ExternalEmail externalEmail) throws MessagingException {
        for (Iterator<Header> it = externalEmail.getMimeMessage().getAllHeaders().asIterator(); it.hasNext(); ) {
            Header header = it.next();

            if (!HEADER_WHITELIST.contains(header.getName())) {
                externalEmail.getMimeMessage().removeHeader(header.getName());
            }
        }
    }
}
