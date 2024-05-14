package gr.esdalab.atlas.devices.core.domain.events.listeners.locations;

import gr.esdalab.atlas.devices.core.application.cases.AssociationsUseCase;
import gr.esdalab.atlas.devices.core.domain.events.AssociationEvent;
import gr.esdalab.atlas.devices.core.domain.vehicles.Vehicle;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class LocationsAssociationEventsListener implements ApplicationListener<AssociationEvent.LocationAssociationEvent<?>> {

    private final AssociationsUseCase associationsUseCase;

    /**
     *
     * @param event Event Handler.
     */
    @Override
    public void onApplicationEvent(final AssociationEvent.LocationAssociationEvent<?> event) {
        log.info("Location association event received, for location[{}], going to handle.", event.getLocationId());
        if( event.getAssociation() instanceof Vehicle.ActiveVehicle ){
            handle(event.getLocationId(), (Vehicle.ActiveVehicle)event.getAssociation());
        }
    }

    /**
     *
     * @param locationId
     * @param association
     */
    private void handle(final int locationId,
                        @NonNull final Vehicle.ActiveVehicle association) {
        associationsUseCase.associationLocationWith(locationId, association);
    }
}
