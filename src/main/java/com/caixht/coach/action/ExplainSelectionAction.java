package com.caixht.coach.action;

import com.caixht.coach.Coach;
import com.caixht.coach.Explanation;
import com.caixht.coach.app.CoachAnswer;
import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.caixht.coach.Coach;
import com.caixht.coach.Explanation;
import com.caixht.coach.app.ExplainService;
import com.caixht.coach.ui.CoachPanel;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowManager;
import org.jetbrains.annotations.NotNull;


public class ExplainSelectionAction extends AnAction {

    public ExplainSelectionAction() {
        super("解释选中的报错");
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        Project project = e.getProject();
        Editor editor = e.getData(CommonDataKeys.EDITOR);
        if (project == null || editor == null) {
            return;
        }

        String text = editor.getSelectionModel().getSelectedText();
        if (text == null || text.isBlank()) {
            Messages.showWarningDialog("先选中一段报错文字", "Java 报错教练");
            return;
        }

        ToolWindow toolWindow = ToolWindowManager.getInstance(project).getToolWindow("Coach");
        if (toolWindow != null) {
            toolWindow.activate(null, true);
        }
        CoachPanel panel = project.getService(CoachPanel.class);
        panel.show("正在看这条报错……");

        ApplicationManager.getApplication().executeOnPooledThread(() -> {
            String result;
            try {
                CoachAnswer answer = new ExplainService().explain(text);
                result = Coach.render(answer.explanation());
                if (!answer.fromModel()) {
                    result = "（本地规则，未连模型：" + answer.note() + "）\n\n" + result;
                }
            } catch (Exception failed) {
                result = "教练自己出错了：" + failed;
            }

            String finalResult = result;
            ApplicationManager.getApplication().invokeLater(() -> panel.show(finalResult));
        });
    }
}