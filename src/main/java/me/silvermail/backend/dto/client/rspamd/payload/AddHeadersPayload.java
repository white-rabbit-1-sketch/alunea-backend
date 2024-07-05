package me.silvermail.backend.dto.client.rspamd.payload;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AddHeadersPayload {
    @JsonProperty("ARC-Authentication-Results")
    protected ArcAuthenticationResultHeaderPayload arcAuthenticationResultHeader = null;
    @JsonProperty("ARC-Seal")
    protected ArcSealHeaderPayload arcSealHeader = null;
    @JsonProperty("ARC-Message-Signature")
    protected ArcSignatureHeaderPayload arcSignatureHeader = null;

    public ArcAuthenticationResultHeaderPayload getArcAuthenticationResultHeader() {
        return arcAuthenticationResultHeader;
    }

    public void setArcAuthenticationResultHeader(ArcAuthenticationResultHeaderPayload arcAuthenticationResultHeader) {
        this.arcAuthenticationResultHeader = arcAuthenticationResultHeader;
    }

    public ArcSealHeaderPayload getArcSealHeader() {
        return arcSealHeader;
    }

    public void setArcSealHeader(ArcSealHeaderPayload arcSealHeader) {
        this.arcSealHeader = arcSealHeader;
    }

    public ArcSignatureHeaderPayload getArcSignatureHeader() {
        return arcSignatureHeader;
    }

    public void setArcSignatureHeader(ArcSignatureHeaderPayload arcSignatureHeader) {
        this.arcSignatureHeader = arcSignatureHeader;
    }
}
