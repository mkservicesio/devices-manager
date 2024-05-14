package gr.esdalab.atlas.devices.adapters.persitence.entities.listeners;

import java.util.Date;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

public class TimeEntityListener {
	
	@PrePersist
    public void setCreationDate(AbstractTimestampEntity ate) {
		ate.setCreatedAt(new Date());
    }
	
	@PreUpdate
	public void setLastModifiedDate(AbstractTimestampEntity ate) {
		ate.setUpdatedAt(new Date());
    }


}
