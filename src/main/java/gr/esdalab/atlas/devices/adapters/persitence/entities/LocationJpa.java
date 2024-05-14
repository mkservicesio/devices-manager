package gr.esdalab.atlas.devices.adapters.persitence.entities;

import javax.persistence.*;

import gr.esdalab.atlas.devices.adapters.persitence.entities.converters.CoordinatesConverter;
import gr.esdalab.atlas.devices.adapters.persitence.entities.listeners.AbstractTimestampEntity;
import gr.esdalab.atlas.devices.core.domain.Coordinates;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.Type;

import java.io.Serializable;
import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="locations")
@EqualsAndHashCode(callSuper=false)
public class LocationJpa extends AbstractTimestampEntity implements Serializable {
	
    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

    @Column(unique = true)
    private String identity;

    @Column(name = "friendly_name")
    private String friendlyName;

    @Column
    private boolean indoor;

    @Convert(converter = CoordinatesConverter.class)
    private List<Coordinates.GPSCoordinates> coordinates;

    @ManyToOne
    @JoinColumn(name = "appendage_id", nullable = false, referencedColumnName = "id", updatable= false)
    private AppendageJpa appendage;

    @OneToOne(cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    private LocationMapJpa map;

    @Column(name = "visible")
    private boolean visible;

    /**
     * @return - A draft location.
     */
    public static LocationJpa draft(@NonNull final String identity,
                                    @NonNull final String friendlyName,
                                    @NonNull final AppendageJpa appentage,
                                    final boolean isIndoor,
                                    @NonNull final List<Coordinates.GPSCoordinates> coordinates){
        return LocationJpa.builder()
                .identity(identity)
                .friendlyName(friendlyName)
                .appendage(appentage)
                .indoor(isIndoor)
                .coordinates(coordinates)
                .visible(true)
                .build();
    }

    /**
     * Static implementation for updating an existing location.
     * @return - An updated location.
     */
    public LocationJpa update(@NonNull final String identity,
                              @NonNull final String friendlyName,
                              final boolean isIndoor,
                              @NonNull final List<Coordinates.GPSCoordinates> coordinates){
        return LocationJpa.builder()
                .id(this.id)
                .identity(identity)
                .friendlyName(friendlyName)
                .appendage(this.appendage)
                .indoor(isIndoor)
                .coordinates(coordinates)
                .map(this.map)
                .visible(this.visible)
                .build();
    }
    
}
