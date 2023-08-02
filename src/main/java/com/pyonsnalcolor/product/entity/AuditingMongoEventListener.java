package com.pyonsnalcolor.product.entity;

import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;

import java.time.LocalDateTime;

public class AuditingMongoEventListener extends AbstractMongoEventListener<Object> {

    @Override
    public void onBeforeConvert(BeforeConvertEvent<Object> event) {
        Object entity = event.getSource();
        if (entity instanceof BaseTimeEntity) {
            BaseTimeEntity baseTimeEntity = (BaseTimeEntity) entity;
            if (baseTimeEntity.getCreatedDate() == null) {
                baseTimeEntity.setCreatedDate(LocalDateTime.now());
            }
        }
    }
}