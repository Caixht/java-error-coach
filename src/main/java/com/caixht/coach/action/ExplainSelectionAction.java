package com.caixht.coach.action;

import com.caixht.coach.Coach;
import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;

public class ExplainSelectionAction extends AnAction {

    public ExplainSelectionAction() {
        super("解释选中的报错");
    }

    @Override
    public void actionPerformed(AnActionEvent e) {
        Editor editor = e.getData(CommonDataKeys.EDITOR);
        if (editor == null) {
            return;
        }
        String text = editor.getSelectionModel().getSelectedText();
        if (text == null || text.trim().isEmpty()) {
            Messages.showWarningDialog("请先选中一段报错文本！", "提示");
            return;
        }
        Project project = e.getProject();
        String result = Coach.explain(text);
        Messages.showInfoMessage(project, result, "Java 报错教练");
    }
}