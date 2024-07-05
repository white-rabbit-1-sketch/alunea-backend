package me.silvermail.backend.dto.client.rspamd;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import me.silvermail.backend.dto.client.rspamd.payload.MilterPayload;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CheckResponse {
    @JsonProperty("dkim-signature")
    protected String dkimSignature = null;
    protected MilterPayload milter = null;

    public MilterPayload getMilter() {
        return milter;
    }

    public void setMilter(MilterPayload milter) {
        this.milter = milter;
    }

    public String getDkimSignature() {
        return dkimSignature;
    }

    public void setDkimSignature(String dkimSignature) {
        this.dkimSignature = dkimSignature;
    }
}
