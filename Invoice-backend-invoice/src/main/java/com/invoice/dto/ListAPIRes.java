package com.invoice.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ListAPIRes {

    List<Map<String, Object>> data;
    String message;
    Short status;
}
