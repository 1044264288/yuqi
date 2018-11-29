package com.pyjh.clothing.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class OrderNoUtil {
    /**
     * @return :201807021607465eaa88330
     */
    public static String getOrderNo() {
        String orderNo = "";
        UUID uuid = UUID.randomUUID();
        String sdf = new SimpleDateFormat("yyyyMMddHHMMSS").format(new Date());
        orderNo = sdf + uuid.toString().substring(0, 8);
        return orderNo;
    }

    public static void main(String[] args) {
        System.out.println(getOrderNo());
    }

}
