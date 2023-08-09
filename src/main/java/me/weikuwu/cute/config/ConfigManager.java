package me.weikuwu.cute.config;

import me.weikuwu.cute.CatMod;
import me.weikuwu.cute.config.settings.*;
import me.weikuwu.cute.config.types.*;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import me.weikuwu.cute.config.types.Boolean;
import me.weikuwu.cute.config.types.Number;
import me.weikuwu.cute.exceptions.TypeMismatchException;


import java.io.File;
import java.io.Reader;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ConfigManager {

    private static final String fileName = "config/Cat/catmod.cfg";

    public static ArrayList<Setting> collect(Class<Config> instance) {
        ArrayList<Setting> settings = new ArrayList<>();

        Field[] fields = instance.getDeclaredFields();

        for (Field field : fields) {
            Property annotation = field.getAnnotation(Property.class);
            if (annotation != null) {
                switch (annotation.type()) {
                    case BOOLEAN:
                    case CHECKBOX:
                        if (field.getType() == boolean.class || field.getType() == Boolean.class) {
                            settings.add(new Boolean(annotation, field, annotation.type()));
                        } else {
                            throw new TypeMismatchException("type boolean or Boolean", field.getType().getName());
                        }
                        break;

                    case NUMBER:
                        if (field.getType() == int.class || field.getType() == Integer.class) {
                            settings.add(new Number(annotation, field));
                        } else {
                            throw new TypeMismatchException("type int or Integer", field.getType().getName());
                        }
                        break;

                    case SELECT:
                        if (field.getType() == int.class || field.getType() == Integer.class) {
                            settings.add(new Select(annotation, field));
                        } else {
                            throw new TypeMismatchException("type int or Integer", field.getType().getName());
                        }
                        break;

                    case BUTTON:
                        if (field.getType() == Runnable.class) {
                            settings.add(new Button(annotation, field));
                        } else {
                            throw new TypeMismatchException("type Runnable", field.getType().getName());
                        }
                        break;

                    case FOLDER:
                        if (field.getType() == boolean.class || field.getType() == Boolean.class) {
                            settings.add(new Folder(annotation, field));
                        } else {
                            throw new TypeMismatchException("type boolean or Boolean", field.getType().getName());
                        }
                        break;

//                    case SPACER:
//                        settings.add(new SpacerSetting(annotation, field));
//                        break;
                }
            }
        }

        // Relationships that need to be set after all settings have been collected
        for (Setting setting : settings) {

            if (!setting.annotation.parent().equals("")) {
                setting.parent = (ParentSetting) ConfigManager.getSettingByName(setting.annotation.parent(), settings);
                if (setting.parent != null) setting.parent.children.add(setting);
            }
        }

        return settings;
    }

    public static Setting getSettingByName(String name, ArrayList<Setting> settings) {
        for (Setting setting : settings) {
            if (setting.name.equals(name)) return setting;
        }
        return null;
    }

    public static Setting getSettingByFieldName(String fieldName, ArrayList<Setting> settings) {
        for (Setting setting : settings) {
            if (!(setting instanceof Button) && setting.field.getName().equals(fieldName)) return setting;
        }
        return null;
    }

    public static void save() {
        try {
            HashMap<String, Object> settingsToSave = new HashMap<>();
            for (Setting setting : CatMod.settings) {
                if (setting instanceof DoNotSave) continue;
                settingsToSave.put(setting.field.getName(), setting.get(Object.class));
            }
            String json = new Gson().toJson(settingsToSave);
            Files.write(Paths.get(fileName), json.getBytes(StandardCharsets.UTF_8));
        } catch (Exception error) {
            System.out.println("Error saving config file");
            error.printStackTrace();
        }
    }

    public static void load() {
        try {
            File file = new File(fileName);
            if (file.exists()) {
                Reader reader = Files.newBufferedReader(Paths.get(fileName));
                Type type = new TypeToken<HashMap<String, Object>>() {
                }.getType();

                HashMap<String, Object> settingsFromConfig = new Gson().fromJson(reader, type);

                for (Map.Entry<String, Object> fromConfig : settingsFromConfig.entrySet()) {
                    Setting beingUpdated = getSettingByFieldName(fromConfig.getKey(), CatMod.settings);
                    if (beingUpdated != null) {
                        if (beingUpdated instanceof Number || beingUpdated instanceof Select) {
                            beingUpdated.set(((Double) fromConfig.getValue()).intValue());
                        } else {
                            beingUpdated.forceSet(fromConfig.getValue());
                        }
                    } else {
                        // This is gross, remove it after most config files have been migrated!
                        beingUpdated = getSettingByName(fromConfig.getKey(), CatMod.settings);
                        if (beingUpdated != null) {
                            if (beingUpdated instanceof Number || beingUpdated instanceof Select) {
                                beingUpdated.set(((Double) fromConfig.getValue()).intValue());
                            } else {
                                beingUpdated.forceSet(fromConfig.getValue());
                            }
                        }
                    }
                }
            }
        } catch (Exception error) {
            System.out.println("Error while loading config file");
            error.printStackTrace();
        }
    }

}