package com.lifelinelink.backend.entity;

public enum Role {
    USER, ADMIN;
    
    @Override
    public String toString() {
        return name();
    }
}