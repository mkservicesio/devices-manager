package gr.esdalab.atlas.devices.core.domain.events;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * Mark interface for location events.
 */
public interface LocationEvent extends DomainEvent{

    /**
     * Location Pre-Update Event.
     * Used to clean up relational data in order then to be updated again.
     */
    @Getter
    class LocationPreUpdateEvent extends ApplicationEvent implements LocationEvent{
        private final int locationId;

        /**
         *
         * @param source
         */
        public LocationPreUpdateEvent(final Object source,
                                       final int locationId) {
            super(source);
            this.locationId = locationId;
        }
    }

    /**
     * Location Pre-Deleted Event.
     * Used to clean up relational data
     */
    @Getter
    class LocationPreDeletedEvent extends ApplicationEvent implements LocationEvent{
        private final int locationId;

        /**
         *
         * @param source
         */
        public LocationPreDeletedEvent(final Object source,
                                       final int locationId) {
            super(source);
            this.locationId = locationId;
        }
    }

}
