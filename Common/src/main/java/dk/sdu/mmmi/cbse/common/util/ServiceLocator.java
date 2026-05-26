package dk.sdu.mmmi.cbse.common.util;

import java.lang.module.Configuration;
import java.lang.module.ModuleDescriptor;
import java.lang.module.ModuleFinder;
import java.lang.module.ModuleReference;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.ServiceConfigurationError;
import java.util.ServiceLoader;
import java.util.stream.Collectors;

public enum ServiceLocator {

    INSTANCE;

    private final ModuleLayer pluginLayer;

    ServiceLocator() {
        this.pluginLayer = createPluginLayer(Path.of("plugins"));
    }

    public <T> List<T> locateAll(Class<T> service) {
        if (pluginLayer == null) {
            return List.of();
        }

        List<T> services = new ArrayList<>();
        try {
            for (T instance : ServiceLoader.load(pluginLayer, service)) {
                services.add(instance);
            }
        } catch (ServiceConfigurationError error) {
            error.printStackTrace();
        }
        return services;
    }

    private ModuleLayer createPluginLayer(Path pluginsDir) {
        try {
            if (!Files.isDirectory(pluginsDir)) {
                return null;
            }

            ModuleFinder finder = ModuleFinder.of(pluginsDir);
            List<String> pluginNames = finder.findAll()
                    .stream()
                    .map(ModuleReference::descriptor)
                    .map(ModuleDescriptor::name)
                    .collect(Collectors.toList());

            if (pluginNames.isEmpty()) {
                return null;
            }

            Configuration configuration = ModuleLayer.boot()
                    .configuration()
                    .resolve(finder, ModuleFinder.of(), pluginNames);

            return ModuleLayer.boot()
                    .defineModulesWithOneLoader(configuration, ClassLoader.getSystemClassLoader());
        } catch (Exception exception) {
            System.err.println("Plugin layer disabled: " + exception.getMessage());
            return null;
        }
    }
}
