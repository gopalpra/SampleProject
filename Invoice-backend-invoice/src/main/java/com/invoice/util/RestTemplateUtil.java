package com.invoice.util;

import com.invoice.constant.ServiceAPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;
import java.util.UUID;

@Component
public class RestTemplateUtil
{
    @Autowired
    private RestTemplate restTemplate;

    @Value("${services.organization.address}" + ":" + "${services.organization.port}" + ServiceAPI.Organization.orgBranch)
    private String getOrganizationBranch;

    @Value("${services.admin.address}" + ":" + "${services.admin.port}" + ServiceAPI.Admin.currency)
    private String getCurrencyData;
    @Value("${services.admin.address}" + ":" + "${services.admin.port}" + ServiceAPI.Admin.tax)
    private String getTaxData;
    @Value("${services.organization.address}" + ":" + "${services.organization.port}" + ServiceAPI.Organization.orgData)
    private String getOrganizationData;

    //-----------------------------------------------------------------------------------------------------------------------------------
    //------------------------------------------ GET ORGANIZATION BRANCH DATA--------------------------------------------------------------------

    public Map<String,Object> getOrganizationBranchData(UUID id, String accountId, String accessToken, String tokenType) {


        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getOrganizationBranch).queryParam("id",id);
        System.out.println("");
        System.out.println("account_id : {}"+accountId);
        System.out.println("token : {}"+accessToken);
        System.out.println("token_type : {}"+tokenType);


        HttpHeaders accessTokenHeader = new HttpHeaders();
        accessTokenHeader.add("Authorization", accessToken);
        accessTokenHeader.add("account_id", accountId);
        accessTokenHeader.add("token_type", tokenType);

        HttpEntity<MultiValueMap<String, String>> accessTokenHttpEntity = new HttpEntity(accessTokenHeader);

        Map<String,Object> getOrganizationResponse = restTemplate.exchange(builder.toUriString(),
                HttpMethod.GET,
                accessTokenHttpEntity,
                Map.class).getBody();

        System.out.println("");
        System.out.println("getOrganizationBranchResponse");
        System.out.println(getOrganizationResponse);
        System.out.println("");

        return getOrganizationResponse;
    }


    //-----------------------------------------------------------------------------------------------------------------------------------
    //------------------------------------------ GET CURRENCY DATA--------------------------------------------------------------------

    public Map<String,Object> getCurrencyData(String accountId, String accessToken, String tokenType) {


        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getCurrencyData);
        System.out.println("");
        System.out.println("account_id : {}"+accountId);
        System.out.println("token : {}"+accessToken);
        System.out.println("token_type : {}"+tokenType);


        HttpHeaders accessTokenHeader = new HttpHeaders();
        accessTokenHeader.add("Authorization", accessToken);
        accessTokenHeader.add("account_id", accountId);
        accessTokenHeader.add("token_type", tokenType);

        HttpEntity<MultiValueMap<String, String>> accessTokenHttpEntity = new HttpEntity(accessTokenHeader);

        Map<String,Object> getCurrencyData = restTemplate.exchange(builder.toUriString(),
                HttpMethod.GET,
                accessTokenHttpEntity,
                Map.class).getBody();

        System.out.println("");
        System.out.println("getCurrencyData");
        System.out.println(getCurrencyData);
        System.out.println("");

        return getCurrencyData;
    }


    //-----------------------------------------------------------------------------------------------------------------------------------
    //------------------------------------------ GET ORGANIZATION DATA--------------------------------------------------------------------

    public Map<String,Object> getOrganizationData(UUID id, String accountId, String accessToken, String tokenType) {


        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getOrganizationData).queryParam("id",id);
        System.out.println("");
        System.out.println("account_id : {}"+accountId);
        System.out.println("token : {}"+accessToken);
        System.out.println("token_type : {}"+tokenType);


        HttpHeaders accessTokenHeader = new HttpHeaders();
        accessTokenHeader.add("Authorization", accessToken);
        accessTokenHeader.add("account_id", accountId);
        accessTokenHeader.add("token_type", tokenType);

        HttpEntity<MultiValueMap<String, String>> accessTokenHttpEntity = new HttpEntity(accessTokenHeader);

        Map<String,Object> getOrganizationResponse = restTemplate.exchange(builder.toUriString(),
                HttpMethod.GET,
                accessTokenHttpEntity,
                Map.class).getBody();

        System.out.println("");
        System.out.println("getOrganizationResponse");
        System.out.println(getOrganizationResponse);
        System.out.println("");

        return getOrganizationResponse;
    }
    //-----------------------------------------------------------------------------------------------------------------------------------
    //------------------------------------------ GET TAX DATA--------------------------------------------------------------------

    public Map<String,Object> getTAXData(String accountId, String accessToken, String tokenType) {


        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getTaxData);
        System.out.println("");
        System.out.println("account_id : {}"+accountId);
        System.out.println("token : {}"+accessToken);
        System.out.println("token_type : {}"+tokenType);


        HttpHeaders accessTokenHeader = new HttpHeaders();
        accessTokenHeader.add("Authorization", accessToken);
        accessTokenHeader.add("account_id", accountId);
        accessTokenHeader.add("token_type", tokenType);

        HttpEntity<MultiValueMap<String, String>> accessTokenHttpEntity = new HttpEntity(accessTokenHeader);

        Map<String,Object> getTaxData = restTemplate.exchange(builder.toUriString(),
                HttpMethod.GET,
                accessTokenHttpEntity,
                Map.class).getBody();

        System.out.println("");
        System.out.println("getTaxData");
        System.out.println(getTaxData);
        System.out.println("");

        return getTaxData;
    }


}
