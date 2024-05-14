package gr.esdalab.atlas.devices.core.domain.events;

import lombok.Getter;
import lombok.NonNull;
import org.springframework.context.ApplicationEvent;

/**
 * Associated events.
 */
public abstract class AssociationEvent extends ApplicationEvent implements DomainEvent{

    /**
     * Default Constructor
     * @param source
     */
    public AssociationEvent(final Object source) {
        super(source);
    }

    /**
     * Location related association event
     */
    @Getter
    public static class LocationAssociationEvent<T> extends AssociationEvent{

        private final int locationId;
        private final T association;

        /**
         * Default Constructor
         *
         * @param source
         */
        public LocationAssociationEvent(final Object source,
                                        final int locationId,
                                        @NonNull final T association) {
            super(source);
            this.locationId = locationId;
            this.association = association;
        }
    }

    @Getter
    public static class LocationDiAssociationEvent<T> extends AssociationEvent{

        private final int locationId;
        private final T association;

        /**
         * Default Constructor
         *
         * @param source
         */
        public LocationDiAssociationEvent(final Object source,
                                        final int locationId,
                                        @NonNull final T association) {
            super(source);
            this.locationId = locationId;
            this.association = association;
        }
    }

}
