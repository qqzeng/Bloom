package com.qqzeng.bloom.demo.server.impl;

import com.qqzeng.bloom.demo.server.api.BusService;
import com.qqzeng.bloom.demo.server.bean.Bus;
import com.qqzeng.bloom.server.annotations.BloomService;

import java.util.Date;

/**
 * @author qqzeng
 * @desc
 */
@BloomService(value = BusService.class)
public class BusServiceImpl implements BusService {
    @Override
    public Date getProductDate(Bus bus) {
        return bus.getProductDate();
    }

    @Override
    public Integer getPassagerNumber(Bus bus) {
        return bus.getPersonList().size();
    }
}
