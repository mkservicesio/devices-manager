package gr.esdalab.atlas.devices.adapters.persitence.entities;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import gr.esdalab.atlas.devices.adapters.persitence.entities.embeddable.NetworkLayerPrimary;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="network_layer_interfaces")
public class NetworkLayerInterfaceJpa {
	
	@EmbeddedId
	private NetworkLayerPrimary networkPK;

	@ManyToOne(fetch= FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name="networkLayerId", nullable = false, referencedColumnName = "id", insertable=false, updatable= false,
			foreignKey = @ForeignKey(name ="AmbientDevicesDatatypes_networkLayerId_FK"))
	private NetworkLayerJpa networkLayer;
	
	@Column
	@NotNull
	private String urn;
}
