package com.qqzeng.bloom.demo.server.api;

import com.qqzeng.bloom.demo.server.bean.Bus;

import java.util.Date;

/**
 * Created by qqzeng.
 */
public interface BusService {
    public Date getProductDate(Bus bus);

    public Integer getPassagerNumber(Bus bus);
}
