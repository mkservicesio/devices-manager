package gr.esdalab.atlas.devices.core.domain.events.listeners.locations;

import gr.esdalab.atlas.devices.core.application.cases.AssociationsUseCase;
import gr.esdalab.atlas.devices.core.domain.events.LocationEvent;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class LocationsEventsListener{

    private final AssociationsUseCase associationsUseCase;

    /**
     *
     * @param event
     */
    @EventListener(value = { LocationEvent.class })
    public void handle(final LocationEvent event){
        log.info("Handling location event..");
        if( event instanceof LocationEvent.LocationPreDeletedEvent ){
            doHandle((LocationEvent.LocationPreDeletedEvent)event);
        }else if( event instanceof LocationEvent.LocationPreUpdateEvent ){
            doHandle((LocationEvent.LocationPreUpdateEvent)event);
        }
    }

    /**
     *
     * @param event
     */
    private void doHandle(@NonNull final LocationEvent.LocationPreDeletedEvent event) {
        log.info("Handling location pre-deleted event");
        associationsUseCase.cleanUpForLocation(event.getLocationId());
    }

    /**
     *
     * @param event
     */
    private void doHandle(@NonNull final LocationEvent.LocationPreUpdateEvent event) {
        log.info("Handling location pre-deleted event");
        associationsUseCase.cleanUpForLocation(event.getLocationId());
    }

}
