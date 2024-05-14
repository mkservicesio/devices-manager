package gr.esdalab.atlas.devices.adapters.rest.resources.out;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

/**
 * Generic Class Re-presents a pageable output.
 */
@Getter
@RequiredArgsConstructor
public class PageableOutput<T>{

    /**
     * Re-presents the total results.
     */
    private final long total;

    /**
     * The actual data. (Sub-list of the total results).
     */
    private final List<T> data;

}
