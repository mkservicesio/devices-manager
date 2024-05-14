package gr.esdalab.atlas.devices.adapters.persitence;

import gr.esdalab.atlas.devices.adapters.persitence.entities.NetworkLayerJpa;
import gr.esdalab.atlas.devices.adapters.persitence.entities.NetworkLayerInterfaceJpa;
import gr.esdalab.atlas.devices.adapters.persitence.entities.embeddable.NetworkLayerPrimary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NetworkInterfacesJpaRepository extends JpaRepository<NetworkLayerInterfaceJpa, NetworkLayerPrimary> {


    /**
     * Delete all interfaces by network layer ID.
     * @param netLayerId
     * @return
     */
    @Modifying
    @Query("DELETE FROM NetworkLayerInterfaceJpa nli WHERE nli.networkPK.networkLayerId = :netLayerId")
    void deleteByNetworkLayerId(int netLayerId);


    /**
     *
     * @param networkLayer
     * @return
     */
    List<NetworkLayerInterfaceJpa> findByNetworkLayer(NetworkLayerJpa networkLayer);

}
