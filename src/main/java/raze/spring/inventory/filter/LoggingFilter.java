package raze.spring.inventory.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;
import raze.spring.inventory.domain.Activity;
import raze.spring.inventory.security.model.UserAccount;
import raze.spring.inventory.security.service.UserAccountService;
import raze.spring.inventory.service.ActivityService;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@Slf4j
public class LoggingFilter implements Filter {

    private final ActivityService activityService;
    private final UserAccountService userAccountService;

    public LoggingFilter(ActivityService activityService, UserAccountService userAccountService) {
        this.activityService = activityService;
        this.userAccountService = userAccountService;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void destroy() {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper((HttpServletRequest)request);
        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper((HttpServletResponse) response);

        try {
            chain.doFilter(requestWrapper, responseWrapper);
        } finally {
            String requestBody = new String(requestWrapper.getContentAsByteArray());
            String responseBody = new String(responseWrapper.getContentAsByteArray());
            responseWrapper.copyBodyToResponse();

            final String method = requestWrapper.getMethod().trim();
//            if(!method.equals("POST") && !method.equals("PUT") && !method.equals("DELETE")){
////                chain.doFilter(requestWrapper, responseWrapper);
//            }

          if ((!method.equals("POST") && !method.equals("PUT") && !method.equals("DELETE")) ||
                  responseWrapper.getStatus() >= 400
              || requestWrapper.getRequestURL().toString().contains("image")
              || requestWrapper.getRequestURL().toString().equals("/") ) {
//            chain.doFilter(requestWrapper, responseWrapper);
          }

           else logRequest(requestWrapper, responseWrapper, responseBody, method);
//            chain.doFilter(requestWrapper, responseWrapper);

        }

    }

    private void logRequest(ContentCachingRequestWrapper requestWrapper, ContentCachingResponseWrapper responseWrapper, String responseBody, String method) {
        final String path  = requestWrapper.getRequestURI();

        String userAgent = requestWrapper.getHeader("User-Agent");
        if (userAgent == null) userAgent = requestWrapper.getHeader("user-agent");
        String expires = responseWrapper.getHeader("Expires");
        Activity activity = new Activity();
        activity.setIp(this.getClientIpAddress(requestWrapper));
        activity.setExpires(expires);
        activity.setRequestMethod(requestWrapper.getMethod());
        activity.setResponseStatus(responseWrapper.getStatus());
        activity.setUrl(requestWrapper.getRequestURI());


        activity.setRequestMethod(method);
        switch (Method.valueOf(method)) {
            case POST: {
                if(path.contains("/v1/user")) {
                    activity.setEntity("user");
                    activity.setParameter(responseBody.replace("\"", ""));
                }
                if(path.contains("/v1/product")) {
                    activity.setEntity("Product");
                    activity.setParameter(responseBody.replace("\"", ""));
                }
                if(path.contains("/v1/supplier")) {
                    activity.setEntity("supplier");
                    activity.setParameter(responseBody.replace("\"", ""));
                }
                if(path.contains("/v1/customer")) {
                    activity.setEntity("customer");
                    activity.setParameter(responseBody.replace("\"", ""));
                }
                if(path.contains("/v1/sale-invoice")) {
                    activity.setEntity("sale-invoice");
                    activity.setParameter(responseBody.replace("\"", ""));

                }
                if(path.contains("/v1/purchase-invoice")) {
                    activity.setEntity("purchase-invoice");
                    activity.setParameter(responseBody.replace("\"", ""));
                }
                break;
            }
            case PUT: {
                if(path.contains("/v1/user")) {
                    activity.setEntity("user");
                    activity.setParameter(requestWrapper.getParameter("id"));
                }
                if(path.contains("/v1/product")) {
                    activity.setEntity("Product");
                    activity.setParameter(requestWrapper.getParameter("id"));
                }
                if(path.contains("/v1/supplier")) {
                    activity.setEntity("supplier");
                    activity.setParameter(requestWrapper.getParameter("id"));
                }
                if(path.contains("/v1/customer")) {
                    activity.setEntity("customer");
                    activity.setParameter(requestWrapper.getParameter("id"));
                }
                if(path.contains("/v1/sale-invoice")) {
                    activity.setEntity("sale-invoice");
                    activity.setParameter(requestWrapper.getParameter("id"));

                }
                if(path.contains("/v1/purchase-invoice")) {
                    activity.setEntity("purchase-invoice");
                    activity.setParameter(requestWrapper.getParameter("id"));
                }
                if(path.contains("/profile")) {
                    activity.setEntity("profile");
                    activity.setParameter(requestWrapper.getParameter("id"));
                }
            }
            case DELETE: {
                if(path.contains("/v1/user")) {
                    activity.setEntity("user");
                    activity.setParameter(requestWrapper.getParameter("id"));
                }
                if(path.contains("/v1/product")) {
                    activity.setEntity("Product");
                    activity.setParameter(requestWrapper.getParameter("id"));
                }
                if(path.contains("/v1/supplier")) {
                    activity.setEntity("supplier");
                    activity.setParameter(requestWrapper.getParameter("id"));
                }
                if(path.contains("/v1/customer")) {
                    activity.setEntity("customer");
                    activity.setParameter(requestWrapper.getParameter("id"));
                }
                if(path.contains("/v1/sale-invoice")) {
                    activity.setEntity("sale-invoice");
                    activity.setParameter(requestWrapper.getParameter("id"));

                }
                if(path.contains("/v1/purchase-invoice")) {
                    activity.setEntity("purchase-invoice");
                    activity.setParameter(requestWrapper.getParameter("id"));
                }
                break;
            }
            default: {
//                    chain.doFilter(requestWrapper, responseWrapper);
            }
        }


        Matcher m = Pattern.compile("(([^)]+))").matcher(userAgent);
        if (m.find()) {
            activity.setUserAgent(m.group(1));
        }
        if (SecurityContextHolder.getContext().getAuthentication() != null &&
                SecurityContextHolder.getContext().getAuthentication().isAuthenticated() &&
                //when Anonymous Authentication is enabled
                !(SecurityContextHolder.getContext().getAuthentication()
                        instanceof AnonymousAuthenticationToken)) {
            String username =  SecurityContextHolder.getContext().getAuthentication().getName();
            log.debug("interceptor received user {} request ", username);
            final UserAccount user = this.userAccountService.getUserByUsername(username);
            activity.setUser(user);
            if (!activity.getUrl().contains("image") && !activity.getUrl().equals("/"))
                activity = activityService.save(activity);
//                chain.doFilter(requestWrapper, responseWrapper);
        } else if (activity.getUrl().equals("/")) {
            Activity existingActivity = this.activityService.findFirst();
            if (existingActivity != null) {
                activity.setId(existingActivity.getId());
                activity.setCreated(existingActivity.getCreated());
            } else
                activity = this.activityService.save(activity);
        }
    }


    private String getClientIpAddress(HttpServletRequest request) {
        String xForwardedForHeader = request.getHeader("X-Forwarded-For");
        if (xForwardedForHeader == null) {
            return request.getRemoteAddr();
        } else {

            return new StringTokenizer(xForwardedForHeader, ",").nextToken().trim();
        }
    }


}
