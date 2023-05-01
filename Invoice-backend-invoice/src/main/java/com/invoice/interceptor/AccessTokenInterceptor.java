package com.invoice.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.invoice.util.HTTPResponse;
import com.invoice.util.HttpConstant;
import org.json.JSONObject;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Component
public class AccessTokenInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {

        System.out.println("");
        System.out.println("============");
        System.out.println("");
        System.out.println("ACCESS TOKEN INTERCEPTOR");
        System.out.println("");
        String token = request.getHeader("Authorization");
        String tokenType = request.getHeader("token_type");
        String accountId = request.getHeader("account_id");
        System.out.println("");
        System.out.println("tokenType : " + tokenType);
        System.out.println("");
        System.out.println(request.getRequestURI());
        System.out.println("");

        if (token == null) {
            System.out.println("");
            System.out.println("Token Is Missing");
            System.out.println("");
            response.getWriter().write(new JSONObject(new HTTPResponse(null, "Token is Missing", HttpConstant.NOT_FOUND_STATUS_CODE)).toString());
            response.setContentType("application/json");
            return false;
        }

        if (!tokenType.equals("access")) {

            System.out.println("");
            System.out.println("Invalid Token");
            System.out.println("");
            response.getWriter().write(new JSONObject(new HTTPResponse(null, "Invalid Token", HttpConstant.NOT_FOUND_STATUS_CODE)).toString());
            response.setContentType("application/json");
            return false;
        }

        if (accountId == null) {
            System.out.println("");
            System.out.println("Account Id Is Missing");
            System.out.println("");
            response.getWriter().write(new JSONObject(new HTTPResponse(null, "Account ID is Missing", HttpConstant.NOT_FOUND_STATUS_CODE)).toString());
            response.setContentType("application/json");
            return false;
        }

        try {

            String[] tokenSplit = token.split("\\.");
            String body = new String(Base64.getDecoder().decode(tokenSplit[1]));
            ObjectMapper mapper = new ObjectMapper();

//        Map<String, Object> authenticateAccessTokenResponse = restTemplateUtil.authenticateAccessToken(request.getParameter("account_id"), request.getHeader("Authorization"));
            Map<String, Object> authenticateAccessTokenResponse = mapper.readValue(body, Map.class);
            String decodeAccountId = new String(Base64.getDecoder().decode(authenticateAccessTokenResponse.get("account_id").toString()));


            if (!decodeAccountId.equals(accountId)) {
                System.out.println("");
                System.out.println("Account Id Is In Valid");
                System.out.println("");
                response.setContentType("application/json");
                response.getWriter().write(new JSONObject(authenticateAccessTokenResponse).toString());
                return false;
            }
        } catch (Exception ex) {
            System.out.println("");
            System.out.println("Token Is Not Valid");
            System.out.println("");
            System.out.println("------------------------------");
            System.out.println("------------------------------");
            System.out.println("------------------------------");
            System.out.println("exception");
            System.out.println(ex.getMessage());
            System.out.println("------------------------------");
            System.out.println("------------------------------");
            System.out.println("------------------------------");
            System.out.println("------------------------------");
            ex.printStackTrace();
            System.out.println("------------------------------");
            response.setContentType("application/json");
            Map<String, Object> mp = new HashMap<>();
            mp.put("message", "Token Is Not Valid");
            response.getWriter().write(new JSONObject(mp).toString());
            return false;
        }

        return true;
    }
}
