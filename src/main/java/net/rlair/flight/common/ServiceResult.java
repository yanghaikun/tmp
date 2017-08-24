package net.rlair.flight.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * Created by Kevin on 2017/8/23.
 */
@JsonSerialize
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ServiceResult {
    public String msg;
    public Object data;
    public boolean succeed;
    public long totalRow;
}
