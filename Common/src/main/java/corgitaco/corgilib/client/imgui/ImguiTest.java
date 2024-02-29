package corgitaco.corgilib.client.imgui;

import corgitaco.corgilib.CorgiLib;
import imgui.ImGui;

public class ImguiTest {

    public static void renderTest() {
        if (CorgiLib.IMGUI_TEST) {
            ImGuiImpl.draw(io -> {
                if (ImGui.begin("Test window")) {

                    ImGui.text("Test text");
                    ImGui.end();
                }
            });
        }
    }
}
