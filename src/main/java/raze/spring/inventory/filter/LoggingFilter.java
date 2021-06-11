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
import java.util.Arrays;
import java.util.Objects;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@Slf4j
public class LoggingFilter implements Filter {
  private final static String[] PATHS = {"product", "customer", "supplier", "user", "transaction", "purchase-invoice", "sale-invoice"};

    private final ActivityService activityService;
    private final UserAccountService userAccountService;

    public LoggingFilter(ActivityService activityService, UserAccountService userAccountService) {
        this.activityService = activityService;
        this.userAccountService = userAccountService;
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


          if ((!method.equals("POST") && !method.equals("PUT") && !method.equals("DELETE")) ||
                  responseWrapper.getStatus() >= 400
//              || requestWrapper.getRequestURL().toString().contains("image")
              || requestWrapper.getRequestURL().toString().equals("/") ) {
          }

           else logRequest(requestWrapper, responseWrapper, requestBody, responseBody, method);

        }

    }

    private void logRequest(ContentCachingRequestWrapper requestWrapper, ContentCachingResponseWrapper responseWrapper,String requestBody, String responseBody, String method) throws IOException {
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

        Arrays.stream(PATHS).forEach(p -> {
            if(path.contains(p) ) {
                activity.setEntity(p);
                final String contentType = Objects.requireNonNullElse(requestWrapper.getContentType(), "");
                switch (Method.valueOf(method)) {
                    case POST:
                        activity.setParameter(responseBody.replace("\"", ""));
                        break;
                    case PUT:
                    case DELETE:
                        if(contentType.contains("application/json")){
                            final String[] strArr = requestBody.split(",");
                            for (String s : strArr) {
                                if(s.contains("\"id\":")) {
                                    String id = s.replace("\"id\":", "").replace("\"", "").replace("{", "").replace(":", "");
                                    activity.setParameter(id);
                                }
                            }

                        } else if(contentType.contains("multipart/form-data")) {
                                activity.setParameter(requestWrapper.getParameter("id"));
                        }
                        break;

                }
            }
        });


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
                activityService.save(activity);
        } else if (activity.getUrl().equals("/")) {
            Activity existingActivity = this.activityService.findFirst();
            if (existingActivity != null) {
                activity.setId(existingActivity.getId());
                activity.setCreated(existingActivity.getCreated());
            } else
                this.activityService.save(activity);
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
