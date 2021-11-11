package ru.zim.ates.common.config.security;

import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

public class AuthHeaderHandlingRequestWrapper extends HttpServletRequestWrapper {

    private String tokenValue;

    public AuthHeaderHandlingRequestWrapper(HttpServletRequest request, String tokenValue) {
        super(request);
        this.tokenValue = tokenValue;
    }

    @Override
    public String getHeader(String name) {
        String header = super.getHeader(name);
        if (header == null && getAuthHeaderName().equals(name)) {
            return getAuthHeaderValue();
        } else {
            return header;
        }
    }

    @Override
    public Enumeration getHeaders(String name) {
        List<String> values = Collections.list(super.getHeaders(name));
        if (getAuthHeaderName().equals(name)) {
            values.add(getAuthHeaderValue());
        }
        return Collections.enumeration(values);
    }

    @Override
    public Enumeration getHeaderNames() {
        List<String> names = Collections.list(super.getHeaderNames());
        if (!names.contains(getAuthHeaderName())) {
            names.add(getAuthHeaderValue());
        }
        return Collections.enumeration(names);
    }

    private String getAuthHeaderValue() {
        return String.format("%s %s", "Bearer", tokenValue);
    }

    private String getAuthHeaderName() {
        return "Authorization";
    }
}
