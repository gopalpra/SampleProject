package com.invoice.interceptor;

import com.fasterxml.jackson.core.JsonParser;
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
public class AdminScopeInterceptor implements HandlerInterceptor {


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {

        System.out.println("");
        System.out.println("============");
        System.out.println("");
        System.out.println("ADMIN SCOPE INTERCEPTOR");
        System.out.println("");
        System.out.println(request.getRequestURI());
        System.out.println(request.getMethod());
        System.out.println("");
        String token = request.getHeader("Authorization");
        String tokenType = request.getHeader("token_type");
        String adminId = request.getHeader("admin_id");

        System.out.println("token");
        System.out.println(token);
        System.out.println("");
        System.out.println("adminId : " + adminId);
        System.out.println("");
        System.out.println("============");
        System.out.println("");


        if (!tokenType.equals("admin")) {

            System.out.println("");
            System.out.println("Invalid Token");
            System.out.println("");
            response.getWriter().write(new JSONObject(new HTTPResponse(null, "Token is Missing", HttpConstant.NOT_FOUND_STATUS_CODE)).toString());
            response.setContentType("application/json");
            return false;
        }



        if (token == null) {
            System.out.println("Token Is Missing");
            System.out.println("");
            response.getWriter().write(new JSONObject(new HTTPResponse(null, "Token is Missing", HttpConstant.NOT_FOUND_STATUS_CODE)).toString());
            return false;
        }

        if (adminId == null) {
            System.out.println("");
            System.out.println("adminId is not valid");
            System.out.println("");
            response.getWriter().write(new JSONObject(new HTTPResponse(null, "Token is Missing", HttpConstant.NOT_FOUND_STATUS_CODE)).toString());   return false;
        }

        try {

            String[] tokenSplit = token.split("\\.");
            String body = new String(Base64.getDecoder().decode(tokenSplit[1]));
            ObjectMapper mapper = new ObjectMapper();


//        Map<String, Object> authenticateAdminTokenResponse = restTemplateUtil.authenticateAdminToken(adminId, token);
            Map<String, Object> authenticateAdminTokenResponse = mapper.readValue(body, Map.class);
            String decodeAdminId = new String(Base64.getDecoder().decode(authenticateAdminTokenResponse.get("admin_id").toString()));

            System.out.println("");
            System.out.println("authenticateAdminTokenResponse");
            System.out.println(authenticateAdminTokenResponse);
            System.out.println("");

//        if ((int) authenticateAdminTokenResponse.get("status") != 200) {
//
//            System.out.println("");
//            System.out.println("Invalid Token");
//            System.out.println("");
//            response.getWriter().write(new JSONObject(authenticateAdminTokenResponse).toString());
//
//            return false;
//        }


            if (!decodeAdminId.equals(adminId)) {
                System.out.println("");
                System.out.println("Admin Id Is In Valid");
                System.out.println("");
                response.setContentType("application/json");
                response.getWriter().write(new JSONObject(authenticateAdminTokenResponse).toString());
                return false;
            }
        } catch (Exception ex) {
            System.out.println("");
            System.out.println("Token Is Not Valid");
            System.out.println("");
            response.setContentType("application/json");
            Map<String, Object> mp = new HashMap<>();
            mp.put("message", "Token Is Not Valid");
            response.getWriter().write(new JSONObject(mp).toString());
            return false;
        }

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(JsonParser.Feature.ALLOW_COMMENTS);

        Map<String, Map<String, Object>> apiSectionDetails = objectMapper.readValue(ClassLoader.getSystemResourceAsStream("json/apiSectionDetails.json"), Map.class);
        Map<String, Object> apiMethodAndName = apiSectionDetails.get(request.getMethod() + "-" + request.getRequestURI().split("/lams")[1]);

        System.out.println("");
        System.out.println("apiMethodAndName");
        System.out.println(apiMethodAndName);
        System.out.println("");

//        Map<String, Object> adminVerifyScopeResponse = restTemplateUtil.getAdminVerifyScope(adminId, apiMethodAndName.get("section").toString(), apiMethodAndName.get("sub_section").toString());
//
//        System.out.println("adminVerifyScopeResponse");
//        System.out.println(adminVerifyScopeResponse);
//        System.out.println("");
//
//        if ((int) adminVerifyScopeResponse.get("status") != 200) {
//
//            response.getWriter().write(new JSONObject(adminVerifyScopeResponse).toString());
//            return false;
//        }
//
//        int providedAdminScope = (int) ((Map<String, Integer>) adminVerifyScopeResponse.get("data")).get("access_scope");

//        System.out.println("providedAdminScope");
//        System.out.println(providedAdminScope);
//        System.out.println("");

        int accessScope = (int) apiMethodAndName.get("access_scope");

        System.out.println("accessScope");
        System.out.println(accessScope);
        System.out.println("");
//
//        if (providedAdminScope != 1 && providedAdminScope == 0 && providedAdminScope != accessScope) {
//
//            System.out.println("");
//            System.out.println("Admin is unauthorized");
//            System.out.println("");
//
//            response.getWriter().write(new JSONObject(new HTTPResponse(null, "Token is Missing", HttpConstants.NOT_FOUND_STATUS_CODE)).toString());
//        }

        return true;
    }
}

