package dk.sdu.mmmi.cbse.main;

import dk.sdu.mmmi.cbse.common.services.IEntityProcessingService;
import dk.sdu.mmmi.cbse.common.services.IGamePluginService;
import dk.sdu.mmmi.cbse.common.services.IPostEntityProcessingService;
import dk.sdu.mmmi.cbse.common.util.ServiceLocator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.ServiceLoader;

@Configuration
class ModuleConfig {

    @Bean
    public Game game() {
        return new Game(gamePluginServices(), entityProcessingServices(), postEntityProcessingServices());
    }

    @Bean
    public List<IEntityProcessingService> entityProcessingServices() {
        return loadServices(IEntityProcessingService.class);
    }

    @Bean
    public List<IGamePluginService> gamePluginServices() {
        return loadServices(IGamePluginService.class);
    }

    @Bean
    public List<IPostEntityProcessingService> postEntityProcessingServices() {
        return loadServices(IPostEntityProcessingService.class);
    }

    private <T> List<T> loadServices(Class<T> serviceType) {
        LinkedHashMap<String, T> services = new LinkedHashMap<>();
        ServiceLoader.load(serviceType)
                .stream()
                .map(ServiceLoader.Provider::get)
                .forEach(service -> services.put(service.getClass().getName(), service));
        ServiceLocator.INSTANCE.locateAll(serviceType)
                .forEach(service -> services.putIfAbsent(service.getClass().getName(), service));
        return List.copyOf(services.values());
    }
}
