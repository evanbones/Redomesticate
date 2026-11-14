package com.evandev.redomesticate.compat.alexsmobs;

import com.evandev.redomesticate.Constants;
import com.evandev.redomesticate.api.PetCommand;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.TamableAnimal;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class AlexsMobsCompat {
    private static final String PACKAGE = "com.github.alexthe666.alexsmobs.";

    private static final ClassValue<Bridge> BRIDGES = new ClassValue<>() {
        @Override
        protected Bridge computeValue(Class<?> type) {
            if (!type.getName().startsWith(PACKAGE) || !TamableAnimal.class.isAssignableFrom(type)) {
                return Bridge.NONE;
            }
            try {
                return Bridge.create(type);
            } catch (ReflectiveOperationException | RuntimeException e) {
                Constants.LOG.warn("Failed to bridge Alex's Mobs commands for {}", type.getName(), e);
                return Bridge.NONE;
            }
        }
    };

    public static boolean hasNativeCommands(Mob mob) {
        return BRIDGES.get(mob.getClass()) != Bridge.NONE;
    }

    public static PetCommand getCommand(Mob mob) {
        return BRIDGES.get(mob.getClass()).get(mob);
    }

    public static void setCommand(Mob mob, PetCommand command) {
        BRIDGES.get(mob.getClass()).set(mob, command);
    }

    private record Bridge(@Nullable Method getCommand, @Nullable Method setCommand,
                          @Nullable Field forcedSitField, @Nullable EntityDataAccessor<Boolean> forcedSitData) {
        static final Bridge NONE = new Bridge(null, null, null, null);

        @SuppressWarnings("unchecked")
        static Bridge create(Class<?> type) throws ReflectiveOperationException {
            Method getCommand = findMethod(type, "getCommand");
            Method setCommand = findMethod(type, "setCommand", int.class);
            if (getCommand == null || setCommand == null) {
                getCommand = null;
                setCommand = null;
            }

            Field forcedSitField = null;
            EntityDataAccessor<Boolean> forcedSitData = null;
            for (Class<?> c = type; c != TamableAnimal.class; c = c.getSuperclass()) {
                for (Field field : c.getDeclaredFields()) {
                    if (field.getName().equals("forcedSit") && field.getType() == boolean.class && !Modifier.isStatic(field.getModifiers())) {
                        field.setAccessible(true);
                        forcedSitField = field;
                    } else if (field.getName().equals("FORCED_SIT") && field.getType() == EntityDataAccessor.class && Modifier.isStatic(field.getModifiers())) {
                        field.setAccessible(true);
                        forcedSitData = (EntityDataAccessor<Boolean>) field.get(null);
                    }
                }
            }

            if (getCommand == null && forcedSitField == null && forcedSitData == null) {
                return NONE;
            }
            return new Bridge(getCommand, setCommand, forcedSitField, forcedSitData);
        }

        @Nullable
        private static Method findMethod(Class<?> type, String name, Class<?>... params) {
            try {
                Method method = type.getMethod(name, params);
                return method.getDeclaringClass().getName().startsWith(PACKAGE) ? method : null;
            } catch (NoSuchMethodException e) {
                return null;
            }
        }

        PetCommand get(Mob mob) {
            try {
                if (getCommand != null) {
                    return switch ((int) getCommand.invoke(mob)) {
                        case 1 -> PetCommand.FOLLOW;
                        case 2 -> PetCommand.SIT;
                        default -> PetCommand.WANDER;
                    };
                }
                return isForcedToSit(mob) ? PetCommand.SIT : PetCommand.WANDER;
            } catch (ReflectiveOperationException e) {
                return PetCommand.WANDER;
            }
        }

        void set(Mob mob, PetCommand command) {
            boolean sit = command == PetCommand.SIT;
            try {
                if (setCommand != null) {
                    setCommand.invoke(mob, switch (command) {
                        case WANDER -> 0;
                        case FOLLOW -> 1;
                        case SIT -> 2;
                    });
                }
                if (forcedSitField != null) {
                    forcedSitField.setBoolean(mob, sit);
                }
                if (forcedSitData != null) {
                    mob.getEntityData().set(forcedSitData, sit);
                }
                ((TamableAnimal) mob).setOrderedToSit(sit);
            } catch (ReflectiveOperationException e) {
                Constants.LOG.warn("Failed to set Alex's Mobs command for {}", mob.getType().getDescriptionId(), e);
            }
        }

        private boolean isForcedToSit(Mob mob) throws IllegalAccessException {
            if (forcedSitData != null) {
                return mob.getEntityData().get(forcedSitData);
            }
            return forcedSitField != null && forcedSitField.getBoolean(mob);
        }
    }
}
