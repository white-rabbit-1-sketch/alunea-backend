package me.silvermail.backend.service.email.external;

import jakarta.mail.internet.InternetAddress;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;

@Service
public class AddressService {
    public void buildPersonal(InternetAddress sourceAddress, InternetAddress destinationAddress) throws UnsupportedEncodingException {
        String result = null;

        if (destinationAddress.getPersonal() != null) {
            result = destinationAddress.getPersonal();
        } else {
            result = sourceAddress.getAddress().replace("@", " at ");
        }

        destinationAddress.setPersonal(result);
    }

    public String getAddressDomain(InternetAddress internetAddress) {
        String result = null;

        String address = internetAddress.getAddress();
        int atIndex = address.indexOf('@');
        if (atIndex != -1 && atIndex < address.length() - 1) {
            result = address.substring(atIndex + 1);
        }

        return result;
    }

    public String getAddressRecipient(InternetAddress internetAddress) {
        String result = null;

        String address = internetAddress.getAddress();
        int atIndex = address.indexOf('@');
        if (atIndex != -1 && atIndex < address.length() - 1) {
            result = address.substring(0, atIndex);
        }

        return result;
    }
}
