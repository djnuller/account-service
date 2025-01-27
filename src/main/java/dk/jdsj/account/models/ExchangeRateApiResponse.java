package dk.jdsj.account.models;

import lombok.Getter;

import java.math.BigDecimal;

// generated from https://json2csharp.com/code-converters/json-to-pojo
@Getter
public class ExchangeRateApiResponse {
    public String result;
    public String documentation;
    public String terms_of_use;
    public int time_last_update_unix;
    public String time_last_update_utc;
    public int time_next_update_unix;
    public String time_next_update_utc;
    public String base_code;
    public String target_code;
    public BigDecimal conversion_rate;
    public BigDecimal conversion_result;
}
