package com.invoice.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ObjAPIRes {

    Map<String, Object> data;
    String message;
    Short status;

}
