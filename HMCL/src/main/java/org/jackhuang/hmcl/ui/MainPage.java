/*
 * Hello Minecraft! Launcher.
 * Copyright (C) 2017  huangyuhui <huanghongxun2008@126.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see {http://www.gnu.org/licenses/}.
 */
package org.jackhuang.hmcl.ui;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXMasonryPane;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import org.jackhuang.hmcl.Main;
import org.jackhuang.hmcl.event.EventBus;
import org.jackhuang.hmcl.event.ProfileChangedEvent;
import org.jackhuang.hmcl.event.ProfileLoadingEvent;
import org.jackhuang.hmcl.event.RefreshedVersionsEvent;
import org.jackhuang.hmcl.game.GameVersion;
import org.jackhuang.hmcl.game.LauncherHelper;
import org.jackhuang.hmcl.game.ModpackHelper;
import org.jackhuang.hmcl.game.Version;
import org.jackhuang.hmcl.mod.MismatchedModpackTypeException;
import org.jackhuang.hmcl.mod.UnsupportedModpackException;
import org.jackhuang.hmcl.setting.Profile;
import org.jackhuang.hmcl.setting.Settings;
import org.jackhuang.hmcl.task.Schedulers;
import org.jackhuang.hmcl.task.Task;
import org.jackhuang.hmcl.task.TaskExecutor;
import org.jackhuang.hmcl.ui.construct.MessageBox;
import org.jackhuang.hmcl.ui.construct.MessageDialogPane;
import org.jackhuang.hmcl.ui.construct.TaskExecutorDialogPane;
import org.jackhuang.hmcl.ui.download.DownloadWizardProvider;
import org.jackhuang.hmcl.ui.wizard.DecoratorPage;
import org.jackhuang.hmcl.util.Lang;
import org.jackhuang.hmcl.util.OperatingSystem;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public final class MainPage extends StackPane implements DecoratorPage {

    private final StringProperty title = new SimpleStringProperty(this, "title", Main.i18n("main_page"));

    @FXML
    private JFXButton btnRefresh;

    @FXML
    private JFXButton btnAdd;

    @FXML
    private JFXMasonryPane masonryPane;

    {
        FXUtils.loadFXML(this, "/assets/fxml/main.fxml");

        EventBus.EVENT_BUS.channel(RefreshedVersionsEvent.class).register(() -> Platform.runLater(this::loadVersions));
        EventBus.EVENT_BUS.channel(ProfileLoadingEvent.class).register(this::onProfilesLoading);
        EventBus.EVENT_BUS.channel(ProfileChangedEvent.class).register(this::onProfileChanged);

        btnAdd.setOnMouseClicked(e -> Controllers.getDecorator().startWizard(new DownloadWizardProvider(), Main.i18n("install")));
        FXUtils.installTooltip(btnAdd, 0, 5000, 0, new Tooltip(Main.i18n("install")));
        btnRefresh.setOnMouseClicked(e -> Settings.INSTANCE.getSelectedProfile().getRepository().refreshVersionsAsync().start());
        FXUtils.installTooltip(btnRefresh, 0, 5000, 0, new Tooltip(Main.i18n("button.refresh")));
    }

    private Node buildNode(Profile profile, String version, String game) {
        VersionItem item = new VersionItem();
        item.setUpdate(profile.getRepository().isModpack(version));
        item.setGameVersion(game);
        item.setVersionName(version);
        item.setOnLaunchButtonClicked(e -> {
            if (Settings.INSTANCE.getSelectedAccount() == null)
                Controllers.dialog(Main.i18n("login.no_Player007"));
            else
                LauncherHelper.INSTANCE.launch(version, null);
        });
        item.setOnScriptButtonClicked(e -> {
            if (Settings.INSTANCE.getSelectedAccount() == null)
                Controllers.dialog(Main.i18n("login.no_Player007"));
            else {
                FileChooser chooser = new FileChooser();
                chooser.setInitialDirectory(profile.getRepository().getRunDirectory(version));
                chooser.setTitle(Main.i18n("version.launch_script.save"));
                chooser.getExtensionFilters().add(OperatingSystem.CURRENT_OS == OperatingSystem.WINDOWS
                        ? new FileChooser.ExtensionFilter(Main.i18n("extension.bat"), "*.bat")
                        : new FileChooser.ExtensionFilter(Main.i18n("extension.sh"), "*.sh"));
                File file = chooser.showSaveDialog(Controllers.getStage());
                if (file != null)
                    LauncherHelper.INSTANCE.launch(version, file);
            }
        });
        item.setOnSettingsButtonClicked(e -> {
            Controllers.getDecorator().showPage(Controllers.getVersionPage());
            Controllers.getVersionPage().load(version, profile);
        });
        item.setOnUpdateButtonClicked(event -> {
            FileChooser chooser = new FileChooser();
            chooser.setTitle(Main.i18n("modpack.choose"));
            chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(Main.i18n("modpack"), "*.zip"));
            File selectedFile = chooser.showOpenDialog(Controllers.getStage());
            if (selectedFile != null) {
                TaskExecutorDialogPane pane = new TaskExecutorDialogPane(null);
                try {
                    TaskExecutor executor = ModpackHelper.getUpdateTask(profile, selectedFile, version, ModpackHelper.readModpackConfiguration(profile.getRepository().getModpackConfiguration(version)))
                            .then(Task.of(Schedulers.javafx(), Controllers::closeDialog)).executor();
                    pane.setExecutor(executor);
                    pane.setCurrentState(Main.i18n("modpack.update"));
                    executor.start();
                    Controllers.dialog(pane);
                } catch (UnsupportedModpackException e) {
                    Controllers.dialog(Main.i18n("modpack.unsupported"), Main.i18n("message.error"), MessageBox.ERROR_MESSAGE);
                } catch (MismatchedModpackTypeException e) {
                    Controllers.dialog(Main.i18n("modpack.mismatched_type"), Main.i18n("message.error"), MessageBox.ERROR_MESSAGE);
                } catch (IOException e)  {
                    Controllers.dialog(Main.i18n("modpack.invalid"), Main.i18n("message.error"), MessageBox.ERROR_MESSAGE);
                }
            }
        });
        File iconFile = profile.getRepository().getVersionIcon(version);
        if (iconFile.exists())
            item.setImage(new Image("file:" + iconFile.getAbsolutePath()));
        return item;
    }

    public void onProfilesLoading() {
        // TODO: Profiles
    }

    public void onProfileChanged(ProfileChangedEvent event) {
        Platform.runLater(() -> loadVersions(event.getProfile()));
    }

    private void loadVersions() {
        loadVersions(Settings.INSTANCE.getSelectedProfile());
    }

    private void loadVersions(Profile profile) {
        List<Node> children = new LinkedList<>();
        for (Version version : profile.getRepository().getVersions()) {
            children.add(buildNode(profile, version.getId(), Lang.nonNull(GameVersion.minecraftVersion(profile.getRepository().getVersionJar(version.getId())), "Unknown")));
        }
        FXUtils.resetChildren(masonryPane, children);
    }

    public String getTitle() {
        return title.get();
    }

    @Override
    public StringProperty titleProperty() {
        return title;
    }

    public void setTitle(String title) {
        this.title.set(title);
    }
}