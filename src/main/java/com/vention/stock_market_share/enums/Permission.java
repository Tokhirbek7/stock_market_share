package com.vention.stock_market_share.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Permission {
    ADMIN_READ("admin:read"),
    ADMIN_UPDATE("admin:update"),
    ADMIN_CREATE("admin:create"),
    ADMIN_DELETE("admin_delete"),
    USER_READ("user:read"),
    USER_UPDATE_BY_ID("user:update"),
    USER_DELETE_BY_ID("user_delete"),
    USER_CREATE("user:create");
    @Getter
    private final String permission;
}
