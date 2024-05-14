package gr.esdalab.atlas.devices.adapters.persitence.entities.listeners;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;
import org.hibernate.annotations.UpdateTimestamp;

import com.vladmihalcea.hibernate.type.json.JsonStringType;

/**
 * All the entities must extend this class for auto createdAt and updatedAt columns.
 * @author kasnot
 */

@Setter
@Getter
@TypeDefs({
		@TypeDef(name = "json", typeClass = JsonStringType.class),
		@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
})
@MappedSuperclass
@EntityListeners(value = {TimeEntityListener.class})
public abstract class AbstractTimestampEntity {
	
	@Column(updatable=false)
	@CreationTimestamp
	private Date createdAt;
	
	@Column(insertable=false)
	@UpdateTimestamp
	private Date updatedAt;
	
}
