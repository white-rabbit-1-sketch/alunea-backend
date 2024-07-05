package me.silvermail.backend.dto.client.rspamd.payload;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MilterPayload {
    @JsonProperty("add_headers")
    protected AddHeadersPayload addHeaders = null;

    public AddHeadersPayload getAddHeaders() {
        return addHeaders;
    }

    public void setAddHeaders(AddHeadersPayload addHeaders) {
        this.addHeaders = addHeaders;
    }
}
