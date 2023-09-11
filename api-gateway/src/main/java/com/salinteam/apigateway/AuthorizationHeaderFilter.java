package com.salinteam.apigateway;

import com.salinteam.apigateway.security.CompanyPrincipal;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.PRE_TYPE;

@Component
public class AuthorizationHeaderFilter extends ZuulFilter {

    private Logger logger = LoggerFactory.getLogger(this.getClass());


    @Override
    public String filterType() {
        return PRE_TYPE;
    }

    @Override
    public int filterOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();
        Optional<String> authorizationHeader = getAuthorizationHeader();
        authorizationHeader.ifPresent(s -> ctx.addZuulRequestHeader("Authorization", s));
        //getting the current HTTP request that is to be handle
        HttpServletRequest request = RequestContext.getCurrentContext().getRequest();
//prints the detail of the requestin the log
        logger.info("request -> {} request uri-> {}", request, request.getRequestURI());
        return null;
    }

    private Optional<String> getAuthorizationHeader() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String accessToken = ((CompanyPrincipal) authentication.getPrincipal()).getJwt();
        System.out.println("ZuulFilter " + accessToken);

        if (accessToken == null) {
            return Optional.empty();
        } else {
            String tokenType = "Bearer";
            String authorizationHeaderValue = String.format("%s %s", tokenType, accessToken);
            return Optional.of(authorizationHeaderValue);
        }
    }
}
