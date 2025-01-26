package org.example.mr_yinkina.yinkinapro.module;

public enum Category {
    COMBAT("战斗"),
    RENDER("渲染"),
    EXPLOIT("功能"),
    PLAYER("玩家");

    private final String name;

    Category(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
} 