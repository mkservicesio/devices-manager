package gr.esdalab.atlas.devices.core.domain.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

/**
 *
 * @param <T>
 */
@Getter
@RequiredArgsConstructor
public class PageableOf<T> {

    /**
     * The total rows count of the pageable request.
     */
    private final long total;

    /**
     * The pageable data.
     */
    private final List<T> data;

}
