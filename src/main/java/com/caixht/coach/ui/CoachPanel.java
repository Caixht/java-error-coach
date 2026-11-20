package com.caixht.coach.ui;

import com.intellij.openapi.components.Service;

import javax.swing.*;


@Service(Service.Level.PROJECT)
public final class CoachPanel {

    private JTextArea area;

    public void attach(JTextArea area) {
        this.area = area;
    }

    public void show(String text) {
        if (area != null) {
            area.setText(text);
        }
    }
}