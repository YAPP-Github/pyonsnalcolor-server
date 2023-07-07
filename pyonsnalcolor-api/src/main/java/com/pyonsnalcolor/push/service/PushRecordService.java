package com.pyonsnalcolor.push.service;

import com.pyonsnalcolor.push.repository.PushKeywordRepository;
import com.pyonsnalcolor.push.repository.PushRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PushRecordService {

    @Autowired
    PushRecordRepository pushRecordRepository;
}
